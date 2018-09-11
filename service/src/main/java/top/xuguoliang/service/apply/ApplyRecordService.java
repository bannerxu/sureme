package top.xuguoliang.service.apply;

import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
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
import top.xuguoliang.models.order.*;
import top.xuguoliang.service.apply.cms.ApplyRecordVO;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private OrderItemDao orderItemDao;

    @Resource
    private MoneyWaterDao moneyWaterDao;

    @Resource
    private WxPayService wxPayService;

    /**
     * 分页查询申请记录
     *
     * @param pageable 分页信息
     * @return 分页申请记录
     */
    public Page<ApplyRecordVO> findPageApplyRecord(Pageable pageable) {
        return applyRecordDao.findAll(pageable).map(applyRecord -> {
            Integer orderId = applyRecord.getOrderId();
            // 查询订单信息
            Order order = orderDao.findOne(orderId);
            if (ObjectUtils.isEmpty(order) || order.getDeleted()) {
                logger.error("查询申请记录时，id为{}的订单不存在", orderId);
                return null;
            }

            // 商品和规格信息
            List<String> commodityTitles = new ArrayList<>();
            List<String> skuNames = new ArrayList<>();
            List<OrderItem> orderItems = orderItemDao.findByOrderIdIs(orderId);
            orderItems.forEach(orderItem -> {
                skuNames.add(orderItem.getSkuName());
                commodityTitles.add(orderItem.getCommodityTitle());
            });

            // 封装返回值
            ApplyRecordVO vo = new ApplyRecordVO();
            BeanUtils.copyNonNullProperties(applyRecord, vo);
            vo.setCommodityTitles(commodityTitles);
            vo.setSkuNames(skuNames);
            vo.setOrderNumber(order.getOrderNumber());
            vo.setRealPayMoney(order.getRealPayMoney());

            return vo;
        });
    }

    /**
     * 审核，如果通过，向微信发起退款申请
     *
     * @param applyRecordId 申请记录id
     * @param isPass        是否通过
     */
    @Transactional(rollbackOn = Exception.class)
    public void audit(Integer applyRecordId, Integer isPass) {
        Date now = new Date();
        ApplyRecord applyRecord = applyRecordDao.findOne(applyRecordId);
        if (ObjectUtils.isEmpty(applyRecord)) {
            logger.error("审核失败：申请记录{} 不存在", applyRecordId);
            throw new ValidationException(MessageCodes.CMS_APPLY_RECORD_NOT_EXIST);
        }
        if (isPass.equals(0)) {
            applyRecord.setApplyStatus(ApplyStatus.APPLY_FAIL);
            applyRecord.setAuditTime(now);
        } else {
            applyRecord.setApplyStatus(ApplyStatus.APPLY_SUCCESS);
            applyRecord.setAuditTime(now);
            Integer orderId = applyRecord.getOrderId();
            Order order = orderDao.findOne(orderId);
            if (ObjectUtils.isEmpty(order) || order.getDeleted()) {
                logger.error("退款失败：订单{} 不存在", orderId);
                throw new ValidationException(MessageCodes.CMS_ORDER_NOT_EXIST);
            }

            String orderNumber = order.getOrderNumber();
            try {
                // 发起退款申请
                WxPayRefundResult refundResult = refund(orderNumber, order.getRealPayMoney());
                String returnCode = refundResult.getReturnCode();
                if (StringUtils.isEmpty(returnCode)) {
                    logger.error("申请退款失败：调用微信申请退款接口返回值为空");
                }
                if ("SUCCESS".equals(returnCode)) {
                    logger.info("订单id{}，订单号{} 发起微信退款成功", orderId, orderNumber);
                } else {
                    logger.info("订单id{}、订单号{} 发起微信退款失败，原因：{}", orderId, orderNumber, refundResult.getReturnMsg());
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
     * @throws WxPayException 微信支付异常
     */
    private WxPayRefundResult refund(String orderNumber, BigDecimal realPayMoney) throws WxPayException {
        int money = realPayMoney.multiply(new BigDecimal("100")).intValue();
        WxPayRefundRequest wxPayRefundRequest = WxPayRefundRequest
                .newBuilder()
                .notifyUrl("https://sureme-web.suremeshop.com/api/payment/refundNotify")
                .outTradeNo(orderNumber)
                .totalFee(money)
                .refundFee(money)
                .outRefundNo(orderNumber)
                .build();

        return wxPayService.refund(wxPayRefundRequest);
    }
}
