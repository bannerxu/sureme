package top.xuguoliang.service.withdraw;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.common.utils.CommonSpecUtil;
import top.xuguoliang.models.user.User;
import top.xuguoliang.models.user.UserDao;
import top.xuguoliang.models.withdraw.Withdraw;
import top.xuguoliang.models.withdraw.WithdrawDao;
import top.xuguoliang.models.withdraw.WithdrawStatus;
import top.xuguoliang.service.withdraw.cms.WithdrawPageVO;

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
}
