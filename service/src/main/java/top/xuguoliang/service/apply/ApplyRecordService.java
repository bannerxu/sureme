package top.xuguoliang.service.apply;

import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.models.apply.ApplyRecord;
import top.xuguoliang.models.apply.ApplyRecordDao;
import top.xuguoliang.models.apply.ApplyStatus;
import top.xuguoliang.models.moneywater.MoneyWater;
import top.xuguoliang.models.moneywater.MoneyWaterDao;
import top.xuguoliang.models.moneywater.MoneyWaterType;
import top.xuguoliang.models.order.Order;
import top.xuguoliang.models.order.OrderDao;
import top.xuguoliang.models.order.OrderStatusEnum;
import top.xuguoliang.service.apply.cms.ApplyRecordVO;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Service
public class ApplyRecordService {

    private static final Logger logger = LoggerFactory.getLogger(ApplyRecordService.class);

    @Resource
    private ApplyRecordDao applyRecordDao;

    @Resource
    private OrderDao orderDao;

    @Resource
    private MoneyWaterDao moneyWaterDao;

    /**
     * 分页查询申请记录
     *
     * @param pageable 分页信息
     * @return 分页申请记录
     */
    public Page<ApplyRecordVO> findPageApplyRecord(Pageable pageable) {
        return applyRecordDao.findAll(pageable).map(applyRecord -> {
            ApplyRecordVO vo = new ApplyRecordVO();
            BeanUtils.copyNonNullProperties(applyRecord, vo);
            return vo;
        });
    }

    /**
     * 审核
     *
     * @param applyRecordId 申请记录id
     * @param isPass        是否通过
     */
    public void audit(Integer applyRecordId, Integer isPass) {
        Date now = new Date();
        ApplyRecord applyRecord = applyRecordDao.findOne(applyRecordId);
        if (ObjectUtils.isEmpty(applyRecord)) {
            logger.error("审核失败：申请记录{} 不存在", applyRecordId);
            throw new ValidationException(MessageCodes.CMS_APPLY_RECORD_NOT_EXIST);
        }
        if (isPass.equals(0)) {
            applyRecord.setApplyStatus(ApplyStatus.APPLY_FAIL);
        } else {
            applyRecord.setApplyStatus(ApplyStatus.APPLY_SUCCESS);
            Integer orderId = applyRecord.getOrderId();
            Order order = orderDao.findOne(orderId);
            if (ObjectUtils.isEmpty(order) || order.getDeleted()) {
                logger.error("退款失败：订单{} 不存在", orderId);
                throw new ValidationException(MessageCodes.CMS_ORDER_NOT_EXIST);
            }
            String orderNumber = order.getOrderNumber();
            try {
                WxPayRefundResult refundResult = refund(orderNumber, order.getRealPayMoney());
                String returnCode = refundResult.getReturnCode();
                if (StringUtils.isEmpty(returnCode)) {
                    logger.error("申请退款失败：调用微信申请退款接口返回值为空");
                }
                if ("SUCCESS".equals(returnCode)) {
                    logger.info("订单id{}，订单号{} 退款成功", orderId, orderNumber);
                    // 退款成功
                    order.setOrderStatus(OrderStatusEnum.ORDER_REFUNDED);
                    orderDao.save(order);
                    MoneyWater moneyWater = new MoneyWater();
                    moneyWater.setUserId(order.getUserId());
                    moneyWater.setType(MoneyWaterType.REFUND);
                    moneyWater.setMoney(order.getRealPayMoney());
                    moneyWater.setTime(now);
                    moneyWater.setOrderId(orderId);
                    moneyWater.setDeleted(false);
                    moneyWaterDao.save(moneyWater);
                } else {
                    logger.info("订单id{}，订单号{} 退款失败：{}", orderId, orderNumber, refundResult.getReturnMsg());
                }

            } catch (WxPayException e) {
                e.printStackTrace();
            }
        }
        applyRecord.setAuditTime(now);
        applyRecordDao.save(applyRecord);
    }

    /**
     * 发起微信退款申请
     *
     * @param orderNumber  订单号码
     * @param realPayMoney 退款金额
     * @return 退款申请结果
     * @throws WxPayException
     */
    private WxPayRefundResult refund(String orderNumber, BigDecimal realPayMoney) throws WxPayException {
        WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
//        wxPayRefundRequest.setNotifyUrl("https://");
        wxPayRefundRequest.setOutTradeNo(orderNumber);
        int money = realPayMoney.multiply(new BigDecimal("100")).intValue();
        wxPayRefundRequest.setTotalFee(money);
        wxPayRefundRequest.setRefundFee(money);
        wxPayRefundRequest.setOutRefundNo(orderNumber);

        return new WxPayServiceImpl().refund(wxPayRefundRequest);
    }
}
