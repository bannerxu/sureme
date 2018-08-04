package top.xuguoliang.service.order.cms;

import lombok.Data;
import org.springframework.data.domain.Pageable;
import top.xuguoliang.models.order.OrderStatusEnum;

/**
 * @author jinguoguo
 */
@Data
public class OrderCmsPageParamVO {
    private Pageable pageable;
    private OrderStatusEnum orderStatus;
}
