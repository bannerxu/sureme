package top.banner.service.moneywater;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.banner.common.utils.BeanUtils;
import top.banner.models.moneywater.MoneyWaterDao;
import top.banner.models.order.Order;
import top.banner.models.order.OrderDao;
import top.banner.service.moneywater.cms.MoneyWaterVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Service
public class MoneyWaterService {

    @Resource
    private MoneyWaterDao moneyWaterDao;

    @Resource
    private OrderDao orderDao;

    public Page<MoneyWaterVO> findAll(Pageable pageable) {
        return moneyWaterDao.findAll(pageable).map(moneyWater -> {
            Order order = orderDao.getOne(moneyWater.getOrderId());
            MoneyWaterVO vo = new MoneyWaterVO();
            if (null != order) {
                vo.setOrderNumber(order.getOrderNumber());
            }
            BeanUtils.copyNonNullProperties(moneyWater, vo);
            return vo;
        });
    }
}
