package top.xuguoliang.models.banner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author jinguoguo
 */
public interface BannerDao extends JpaSpecificationExecutor<Banner>, JpaRepository<Banner, Integer> {
    /**
     * 通过轮播类型查找轮播
     * @param bannerType 轮播类型
     * @return 轮播列表
     */
    List<Banner> findByBannerTypeIs(BannerTypeEnum bannerType);
}
