package top.xuguoliang.service.brokerage;


import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.models.brokerage.Brokerage;
import top.xuguoliang.models.brokerage.BrokerageDao;
import top.xuguoliang.models.order.Order;
import top.xuguoliang.models.order.OrderDao;
import top.xuguoliang.models.user.User;
import top.xuguoliang.models.user.UserDao;
import top.xuguoliang.service.brokerage.web.BrokerageVO;

import javax.annotation.Resource;

@Service
public class BrokerageWebService {
    @Resource
    private BrokerageDao brokerageDao;
    @Resource
    private OrderDao orderDao;
    @Resource
    private UserDao userDao;

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

        Order order = orderDao.findOne(brokerage.getOrderId());
        BeanUtils.copyNonNullProperties(order, brokerageVO);

        User user = userDao.findOne(brokerage.getUserId());
        BeanUtils.copyNonNullProperties(user, brokerageVO);

        BeanUtils.copyNonNullProperties(brokerage, brokerageVO);
        return brokerageVO;
    }

    // TODO: 2018-08-05  订单支付后要记录佣金
}
