package top.banner.service.brokerage;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.banner.common.utils.BeanUtils;
import top.banner.common.utils.NumberingUtil;
import top.banner.models.brokerage.Brokerage;
import top.banner.models.brokerage.BrokerageDao;
import top.banner.models.order.Order;
import top.banner.models.order.OrderDao;
import top.banner.models.shareitem.ShareItem;
import top.banner.models.shareitem.ShareItemDao;
import top.banner.models.user.User;
import top.banner.models.user.UserDao;
import top.banner.models.withdraw.Withdraw;
import top.banner.models.withdraw.WithdrawDao;
import top.banner.service.brokerage.web.BrokerageTotalVO;
import top.banner.service.brokerage.web.BrokerageVO;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author jinguoguo
 */
@Service
public class BrokerageWebService {
    @Resource
    private BrokerageDao brokerageDao;
    @Resource
    private OrderDao orderDao;
    @Resource
    private UserDao userDao;
    @Resource
    private ShareItemDao shareItemDao;
    @Resource
    private NumberingUtil numberingUtil;
    @Resource
    private WithdrawDao withdrawDao;

    /**
     * 佣金明细
     *
     * @param userId
     * @param pageable
     * @return
     */
    public Page<BrokerageVO> findPage(Integer userId, Pageable pageable) {
        return brokerageDao.findAllByUserId(userId, pageable)
                .map(this::toBrokerageVO);
    }

    /**
     * brokerage --> brokerageVO
     */
    private BrokerageVO toBrokerageVO(Brokerage brokerage) {
        BrokerageVO brokerageVO = new BrokerageVO();

        Order order = orderDao.getOne(brokerage.getOrderId());
        BeanUtils.copyNonNullProperties(order, brokerageVO);

        User user = userDao.getOne(brokerage.getUserId());
        BeanUtils.copyNonNullProperties(user, brokerageVO);

        BeanUtils.copyNonNullProperties(brokerage, brokerageVO);
        return brokerageVO;
    }

    /**
     * 创建佣金记录
     *
     * @param outTradeNo 订单号
     */
    public void insert(String outTradeNo) {
        Order order = orderDao.findByOrderNumberEquals(outTradeNo);
        if (order != null) {
            BigDecimal totalMoney = order.getTotalMoney();
            Integer userId = order.getUserId();

            //获取上线和返现率
            Integer topUserId2 = getTopUser(userId);

            if (topUserId2 != null) {
                Date date = new Date();
                //再次获取上线
                Integer topUserId1 = getTopUser(topUserId2);
                //判断是一级代理，还是二级代理
                if (topUserId1 != null) {
                    Brokerage brokerage1 = new Brokerage(order.getOrderId(), order.getOrderNumber(), totalMoney.multiply(new BigDecimal(0.1)), 0.1, date, topUserId1);
                    brokerageDao.save(brokerage1);
                    updateUserBalance(topUserId1, totalMoney.multiply(new BigDecimal(0.1)));
                    Brokerage brokerage2 = new Brokerage(order.getOrderId(), order.getOrderNumber(), totalMoney.multiply(new BigDecimal(0.05)), 0.05, date, topUserId2);
                    brokerageDao.save(brokerage2);
                    updateUserBalance(topUserId2, totalMoney.multiply(new BigDecimal(0.05)));
                } else {
                    //如果一级代理为null，那么二级代理就是一级代理
                    Brokerage brokerage1 = new Brokerage(order.getOrderId(), order.getOrderNumber(), totalMoney.multiply(new BigDecimal(0.1)), 0.1, date, topUserId2);
                    brokerageDao.save(brokerage1);
                    updateUserBalance(topUserId2, totalMoney.multiply(new BigDecimal(0.1)));
                }
            }
        }
    }

    /**
     * 修改用户余额
     */
    private void updateUserBalance(Integer userId, BigDecimal money) {
        User one = userDao.getOne(userId);
        one.setBalance(one.getBalance().add(money));
        userDao.save(one);
    }

    /**
     * 获取上线id
     *
     * @param userId
     * @return
     */
    private Integer getTopUser(Integer userId) {
        ShareItem shareItem = shareItemDao.findByBeShareUserId(userId);
        if (shareItem != null) {
            return shareItem.getShareItemId();
        }
        return null;
    }

    /**
     * 提现
     *
     * @param money  提现金额
     * @param userId 用户id
     * @return 用户信息
     */
    public User withdraw(BigDecimal money, Integer userId) {
        User one = userDao.getOne(userId);
        one.setBalance(one.getBalance().subtract(money));
        one.setFreezeBalance(one.getFreezeBalance().add(money));
        userDao.save(one);

        Withdraw withdraw = new Withdraw(numberingUtil.getWithdrawCode(userId), money, userId);
        withdrawDao.save(withdraw);
        return one;
    }

    /**
     * 获取累计佣金总额
     *
     * @param userId 用户id
     * @return a
     */
    public BrokerageTotalVO getTotal(Integer userId) {
        BigDecimal money = BigDecimal.ZERO;
        List<Brokerage> list = brokerageDao.findByUserId(userId);
        for (Brokerage brokerage : list) {
            money = money.add(brokerage.getMoney());
        }
        return new BrokerageTotalVO(money);
    }


}
