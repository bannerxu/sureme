package top.banner.models.apply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface ApplyRecordDao extends JpaSpecificationExecutor<ApplyRecord>, JpaRepository<ApplyRecord, Integer> {
}
