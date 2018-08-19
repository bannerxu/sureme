package top.xuguoliang.models.order;

/**
 * @author jinguoguo
 */

public enum OrderStatusEnum {
    // 待付款
    ORDER_WAITING_PAYMENT,
    // 已付款，待发货
    ORDER_WAITING_SEND,
    // 已发货
    ORDER_SENT,
    // 已收货
    ORDER_RECEIVED,
    // 申请退款
    ORDER_APPLY_REFUND,
    // 已退款
    ORDER_REFUNDED,
    // 已退货
    ORDER_RETURNED,
    // 查询所有，给前端用的
    QUERY_ALL
}
