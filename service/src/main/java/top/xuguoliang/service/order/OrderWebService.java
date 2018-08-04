package top.xuguoliang.service.order;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.models.commodity.Commodity;
import top.xuguoliang.models.commodity.CommodityDao;
import top.xuguoliang.models.order.Order;
import top.xuguoliang.models.order.OrderDao;
import top.xuguoliang.models.order.OrderTypeEnum;
import top.xuguoliang.models.user.Address;
import top.xuguoliang.models.user.AddressDao;
import top.xuguoliang.models.user.UserDao;
import top.xuguoliang.service.order.web.OrderWebCreateParamVO;
import top.xuguoliang.service.order.web.OrderWebResultVO;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Service
public class OrderWebService {

    private static final Logger logger = LoggerFactory.getLogger(OrderWebService.class);

    @Resource
    private OrderDao orderDao;

    @Resource
    private CommodityDao commodityDao;

    @Resource
    private UserDao userDao;

    @Resource
    private AddressDao addressDao;


    /**
     * 创建订单
     *
     * @param orderWebCreateParamVO 订单创建需要的信息
     * @return 创建后的订单
     */
    public OrderWebResultVO createOrder(OrderWebCreateParamVO orderWebCreateParamVO) {
        Date date = new Date();
        // 返回值
        OrderWebResultVO vo = new OrderWebResultVO();
        // 订单实体
        Order order = new Order();

        // 从参数中取出有用信息
        OrderTypeEnum orderType = orderWebCreateParamVO.getOrderType();
        Integer addressId = orderWebCreateParamVO.getAddressId();
        Integer commodityId = orderWebCreateParamVO.getCommodityId();

        Address address = addressDao.findOne(addressId);
        if (ObjectUtils.isEmpty(address) || address.getDeleted()) {
            logger.error("调用创建订单业务失败：找不到对应的地址");
            throw new ValidationException(MessageCodes.WEB_ADDRESS_NOT_EXIST, "地址不存在，请重新选择地址");
        }

        Commodity commodity = commodityDao.findOne(commodityId);
        if (ObjectUtils.isEmpty(commodity) || commodity.getDeleted()) {
            logger.error("调用创建订单业务失败：找不到对应的商品");
            throw new ValidationException(MessageCodes.WEB_COMMODITY_NOT_EXIST, "商品不存在，请重新下单");
        }

        // 设置订单属性
        BeanUtils.copyNonNullProperties(address, order);
        BeanUtils.copyNonNullProperties(commodity, order);
        order.setOrderType(orderType);
        order.setCreateTime(date);
        order.setUpdateTime(date);
        order.setDeleted(false);

        return vo;
    }
}
