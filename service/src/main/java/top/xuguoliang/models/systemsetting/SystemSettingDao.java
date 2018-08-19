package top.xuguoliang.models.systemsetting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface SystemSettingDao extends JpaSpecificationExecutor<SystemSetting>, JpaRepository<SystemSetting, Integer> {
}
