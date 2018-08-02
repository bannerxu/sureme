package top.xuguoliang.models.banner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface BannerDao extends JpaSpecificationExecutor<Banner>, JpaRepository<Banner, Integer> {
}
