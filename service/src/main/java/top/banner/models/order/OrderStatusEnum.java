package top.banner.models.order;

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
    // 已收货，待评价
    ORDER_RECEIVED,
    // 已评价
    ORDER_COMMENTED,
    // 申请退款
    ORDER_APPLY_REFUND,
    // 已退款
    ORDER_REFUNDED,
    // 已退货
    ORDER_RETURNED,
    // QUERY_ALL
    QUERY_ALL
}
