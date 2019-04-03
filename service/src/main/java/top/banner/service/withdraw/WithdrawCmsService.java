package top.banner.service.withdraw;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import top.banner.common.utils.BeanUtils;
import top.banner.common.utils.CommonSpecUtil;
import top.banner.common.utils.WeChatUtil;
import top.banner.models.user.User;
import top.banner.models.user.UserDao;
import top.banner.models.withdraw.Withdraw;
import top.banner.models.withdraw.WithdrawDao;
import top.banner.models.withdraw.WithdrawStatus;
import top.banner.service.withdraw.cms.WithdrawPageVO;
import top.banner.service.withdraw.cms.WithdrawUpdateVO;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WithdrawCmsService {
    @Resource
    private CommonSpecUtil<Withdraw> specUtil;
    @Resource
    private UserDao userDao;
    @Resource
    private WithdrawDao withdrawDao;
    @Resource
    private WeChatUtil weChatUtil;


    public Page<WithdrawPageVO> findPage(String nickName, WithdrawStatus status, Date startTime, Date endTime, String code, Pageable pageable) {
        List<Integer> idList = userDao.findByNickNameLike(nickName == null ? "%%" : "%" + nickName + "%").stream()
                .map(User::getUserId)
                .collect(Collectors.toList());

        Specifications<Withdraw> spec = Specifications.where(specUtil.in("userId", idList.size() == 0 ? null : idList))
                .and(specUtil.equal("withdrawStatus", status))
                .and(specUtil.timeBetween("createTime", startTime, endTime))
                .and(specUtil.equal("code", code));
        return withdrawDao.findAll(spec, pageable).map(this::toWithdrawPageVO);
    }

    private WithdrawPageVO toWithdrawPageVO(Withdraw withdraw) {
        WithdrawPageVO withdrawPageVO = new WithdrawPageVO();
        BeanUtils.copyNonNullProperties(withdraw, withdrawPageVO);
        User one = userDao.findOne(withdraw.getUserId());
        withdrawPageVO.setNickName(one.getNickName());
        return withdrawPageVO;
    }

    /**
     * 处理提现
     *
     * @param updateVO
     * @return
     */
    public WithdrawPageVO update(WithdrawUpdateVO updateVO) {
        Withdraw withdraw = withdrawDao.findOne(updateVO.getWithdrawId());
        Assert.notNull(withdraw, "提现记录不存在");

        if (withdraw.getWithdrawStatus().equals(WithdrawStatus.WAIT)) {
            if (updateVO.getWithdrawStatus().equals(WithdrawStatus.WAIT)) {
                return toWithdrawPageVO(withdraw);
            } else if (updateVO.getWithdrawStatus().equals(WithdrawStatus.SUCCESS)) {
                //提现成功
                User user = userDao.findOne(withdraw.getUserId());
                try {
                    if (weChatUtil.businessPayment(user.getOpenId(), withdraw.getMoney(), "佣金提现")) {
                        User one = userDao.findOne(withdraw.getUserId());
                        one.setFreezeBalance(one.getFreezeBalance().subtract(withdraw.getMoney()));
                        userDao.save(one);

                        BeanUtils.copyNonNullProperties(updateVO, withdraw);
                        withdrawDao.save(withdraw);
                    } else {
                        withdrawFailure(new WithdrawUpdateVO(updateVO.getWithdrawId(), WithdrawStatus.FAILURE, "提现失败"), withdraw);
                    }


                } catch (Exception e) {
                    withdrawFailure(new WithdrawUpdateVO(updateVO.getWithdrawId(), WithdrawStatus.FAILURE, e.getMessage()), withdraw);
                }


            } else if (updateVO.getWithdrawStatus().equals(WithdrawStatus.FAILURE)) {

                withdrawFailure(updateVO, withdraw);
            }
        }
        return toWithdrawPageVO(withdraw);
    }

    private void withdrawFailure(WithdrawUpdateVO updateVO, Withdraw withdraw) {
        //提现失败
        User one = userDao.findOne(withdraw.getUserId());
        one.setBalance(one.getBalance().add(withdraw.getMoney()));
        one.setFreezeBalance(one.getFreezeBalance().subtract(withdraw.getMoney()));
        userDao.save(one);

        BeanUtils.copyNonNullProperties(updateVO, withdraw);
        withdrawDao.save(withdraw);

    }
}
