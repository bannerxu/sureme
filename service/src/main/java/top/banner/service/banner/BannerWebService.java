package top.banner.service.banner;

import org.springframework.stereotype.Service;
import top.banner.common.utils.BeanUtils;
import top.banner.models.banner.Banner;
import top.banner.models.banner.BannerDao;
import top.banner.models.banner.BannerTypeEnum;
import top.banner.service.banner.web.BannerWebResultVO;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jinguoguo
 */
@Service
public class BannerWebService {

    @Resource
    private BannerDao bannerDao;

    /**
     * 查找所有首页轮播图
     *
     * @return 轮播
     */
    public List<BannerWebResultVO> findAll(BannerTypeEnum bannerType) {
        return bannerDao.findByBannerTypeIs(bannerType)
                .stream()
                .map(this::mapBannerToVO)
                .collect(Collectors.toList());
    }

    /**
     * 把轮播对象转成VO
     *
     * @param banner 轮播
     * @return vo
     */
    private BannerWebResultVO mapBannerToVO(Banner banner) {
        BannerWebResultVO vo = new BannerWebResultVO();
        BeanUtils.copyNonNullProperties(banner, vo);
        return vo;
    }

}
