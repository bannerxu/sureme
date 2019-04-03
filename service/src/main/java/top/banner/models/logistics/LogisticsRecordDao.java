package top.banner.models.logistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface LogisticsRecordDao extends JpaSpecificationExecutor<LogisticsRecord>, JpaRepository<LogisticsRecord, Integer> {
    /**
     * 查询物流信息
     * @param orderId 订单id
     * @return 物流信息
     */
    LogisticsRecord findByOrderIdIs(Integer orderId);
}
