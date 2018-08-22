package top.xuguoliang.service.apply;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.models.apply.ApplyRecord;
import top.xuguoliang.models.apply.ApplyRecordDao;
import top.xuguoliang.models.apply.ApplyStatus;
import top.xuguoliang.service.apply.cms.ApplyRecordVO;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author jinguoguo
 */
@Service
public class ApplyRecordService {

    private static final Logger logger = LoggerFactory.getLogger(ApplyRecordService.class);

    @Resource
    private ApplyRecordDao applyRecordDao;

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
     * @param applyRecordId 申请记录id
     * @param isPass 是否通过
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
            // TODO 退款申请通过，发起微信退款；另外需要在退款回调中设置订单状态和添加流水记录
        }
        applyRecord.setAuditTime(now);
        applyRecordDao.save(applyRecord);
    }
}
