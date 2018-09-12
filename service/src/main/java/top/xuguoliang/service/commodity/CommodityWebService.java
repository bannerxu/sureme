package top.xuguoliang.service.commodity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.common.utils.CommonSpecUtil;
import top.xuguoliang.common.utils.PainterUtil;
import top.xuguoliang.common.utils.WeChatUtil;
import top.xuguoliang.models.comment.CommodityComment;
import top.xuguoliang.models.comment.CommodityCommentDao;
import top.xuguoliang.models.commodity.*;
import top.xuguoliang.models.coupon.Coupon;
import top.xuguoliang.models.coupon.CouponDao;
import top.xuguoliang.models.coupon.PersonalCoupon;
import top.xuguoliang.models.coupon.PersonalCouponDao;
import top.xuguoliang.models.user.User;
import top.xuguoliang.models.user.UserDao;
import top.xuguoliang.service.comment.web.CommodityCommentWebResultVO;
import top.xuguoliang.service.commodity.web.CommodityWebCouponVO;
import top.xuguoliang.service.commodity.web.CommodityWebDetailVO;
import top.xuguoliang.service.commodity.web.CommodityWebResultVO;
import top.xuguoliang.service.qiniu.QiNiuService;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jinguoguo
 */
@Service
public class CommodityWebService {

    private static final Logger logger = LoggerFactory.getLogger(CommodityWebService.class);

    @Resource
    private CommonSpecUtil<Commodity> commonSpecUtil;
    @Resource
    private CommodityDao commodityDao;
    @Resource
    private CommodityCommentDao commodityCommentDao;
    @Resource
    private CommodityBannerDao commodityBannerDao;
    @Resource
    private CouponDao couponDao;
    @Resource
    private StockKeepingUnitDao stockKeepingUnitDao;
    @Resource
    private PersonalCouponDao personalCouponDao;
    @Resource
    private UserDao userDao;
    @Resource
    private QiNiuService qiNiuService;
    @Resource
    private WeChatUtil weChatUtil;

    /**
     * 分页查询商品
     *
     * @param categoryId 分类id，为空查询所有
     * @param pageable   分页信息
     * @return 分页商品
     */
    public Page<CommodityWebResultVO> findPage(Integer categoryId, Pageable pageable) {
        // 未删除的
        Specification<Commodity> deleted = commonSpecUtil.equal("deleted", false);
        if (ObjectUtils.isEmpty(categoryId)) {
            return commodityDao.findAll(deleted, pageable).map(this::convertCommodityToVO);
        } else {
            // 指定分类
            Specifications<Commodity> spec = Specifications.where(deleted)
                    .and(commonSpecUtil.equal("categoryId", categoryId));
            return commodityDao.findAll(spec, pageable).map(this::convertCommodityToVO);
        }
    }

    /**
     * 商品转VO
     *
     * @param commodity 商品
     * @return VO
     */
    private CommodityWebResultVO convertCommodityToVO(Commodity commodity) {
        CommodityWebResultVO vo = new CommodityWebResultVO();
        BeanUtils.copyNonNullProperties(commodity, vo);

        // 商品图片
        Integer commodityId = commodity.getCommodityId();
        List<CommodityBanner> banners = commodityBannerDao.findByCommodityIdIsAndDeletedIsFalseOrderByCommodityBannerIdAsc(commodityId);
        if (!ObjectUtils.isEmpty(banners)) {
            CommodityBanner commodityBanner = banners.get(0);
            vo.setCommodityImage(commodityBanner.getCommodityBannerUrl());
        }

        return vo;
    }

    /**
     * 获取商品详情（查询单个商品）
     *
     * @param commodityId 商品id
     * @return 商品
     */
    public CommodityWebDetailVO getCommodityDetail(Integer userId, Integer commodityId) {
        Commodity commodity = commodityDao.findOne(commodityId);
        if (ObjectUtils.isEmpty(commodity) || commodity.getDeleted()) {
            logger.error("查询商品错误：商品不存在");
            throw new ValidationException(MessageCodes.WEB_COMMODITY_NOT_EXIST);
        }
        List<CommodityComment> comments = commodityCommentDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
        List<CommodityBanner> banners = commodityBannerDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);
        List<StockKeepingUnit> skus = stockKeepingUnitDao.findByCommodityIdIsAndDeletedIsFalse(commodityId);

        List<CommodityWebCouponVO> couponVOs = new ArrayList<>();
        List<Coupon> coupons = couponDao.findByDeletedIsFalse();
        if (!ObjectUtils.isEmpty(coupons)) {
            coupons.forEach(coupon -> {
                Integer couponId = coupon.getCouponId();
                CommodityWebCouponVO couponVO = new CommodityWebCouponVO();
                BeanUtils.copyNonNullProperties(coupon, couponVO);
                PersonalCoupon personalcoupon = personalCouponDao.findByUserIdIsAndCouponIdIsAndDeletedIsFalse(userId, couponId);
                if (!ObjectUtils.isEmpty(personalcoupon)) {
                    couponVO.setIsPulled(true);
                }
            });
        }

        CommodityWebDetailVO vo = new CommodityWebDetailVO();
        BeanUtils.copyNonNullProperties(commodity, vo);
        vo.setComments(comments);
        vo.setCommodityBanners(banners);
        vo.setStockKeepingUnits(skus);
        vo.setCoupons(couponVOs);

        return vo;
    }

    /**
     * 分页商品评论
     *
     * @param commodityId 商品id
     * @param pageable    分页信息
     * @return 分页文章评论
     */
    public Page<CommodityCommentWebResultVO> findPageComment(Integer commodityId, Pageable pageable) {
        return commodityCommentDao.findAllByCommodityIdIsAndDeletedIsFalse(commodityId, pageable).map(commodityComment -> {
            CommodityCommentWebResultVO vo = new CommodityCommentWebResultVO();
            BeanUtils.copyNonNullProperties(commodityComment, vo);
            Integer userId = commodityComment.getUserId();
            User user = userDao.findOne(userId);
            vo.setNickname(user.getNickName());
            vo.setAvatarUrl(user.getAvatarUrl());

            return vo;
        });
    }

    /**
     * 生成分享图片
     *
     * @param width 二维码宽度
     * @param path  路径
     * @param scene 业务参数
     * @param skuId 规格id
     * @return 图片二维码
     */
    public String generateShareImage(Integer width, String path, String scene, Integer skuId) {
        // 获取二维码
        String qrCode = weChatUtil.getQRCode(width, path, scene);

        // 获取规格、商品信息
        StockKeepingUnit sku = stockKeepingUnitDao.findOne(skuId);
        String money = "￥ " + sku.getDiscountPrice().toPlainString();
        Integer commodityId = sku.getCommodityId();
        String commodityTitle = commodityDao.findOne(commodityId).getCommodityTitle();
        CommodityBanner banner = commodityBannerDao.findFirstByCommodityIdIsAndDeletedIsFalseOrderByCreateTimeAsc(commodityId);
        String bannerUrl = banner.getCommodityBannerUrl();

        // 合成并上传到七牛，返回七牛url
        return paintImage(bannerUrl, qrCode, commodityTitle, money, "");
    }


    /**
     * 画图
     *
     * @param commodityBannerUrl 商品图片地址
     * @param qrCodeUrl          二维码地址
     * @param commodityTitle     商品标题
     * @param money              价格
     * @param bottom             底部文字
     * @return 图片url地址
     */
    public String paintImage(String commodityBannerUrl, String qrCodeUrl, String commodityTitle, String money, String bottom) {
        try {
            // 把商品标题切分成多份
            int length = commodityTitle.length();
            int line = length / 18;
            String[] titles = new String[line + 1];
            for (int i = 0; i <= line; i++) {
                System.out.println(17 * i + " , " + 17 * (i + 1));
                if (i < line) {
                    String s = commodityTitle.substring(17 * i, 17 * (i + 1));
                    titles[i] = s;
                } else {
                    String s = commodityTitle.substring(17 * i, length);
                    titles[i] = s;
                }
            }

            // 本地模板文件
            File img = ResourceUtils.getFile("classpath:Template.png");
            // 读入图片
            Image src = ImageIO.read(img);

            int width = src.getWidth(null);
            int height = src.getHeight(null);
            // 缓冲图片对象
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            // 图形类
            Graphics2D graphics2D = image.createGraphics();
            // 画模板
            PainterUtil.paintImage(src, graphics2D, 0, 0, width, height, 1F);
            // 画商品图片
            PainterUtil.paintRemoteImage(graphics2D, commodityBannerUrl, 170, 50, 400, 400);
            // 画二维码
            PainterUtil.paintRemoteImage(graphics2D, qrCodeUrl, 190, 750, 360, 360);
            for (int i = 0; i <= line; i++) {
                System.out.println(titles[i]);
                System.out.println(500 + 40 * line);
                PainterUtil.paintText(graphics2D, titles[i], PainterUtil.CUSTOMER_NAME,
                        PainterUtil.CUSTOMER_NAME_COLOR, 1F, 50, 500 + 40 * i);
            }
            // 画价格
            PainterUtil.paintText(graphics2D, money, PainterUtil.CUSTOMER_WECHAT_NUMBER,
                    PainterUtil.CUSTOMER_WECHAT_NUMBER_COLOR, 1F, 50, 700);
            // 画底部文字
//            PainterUtil.paintText(graphics2D, bottom, PainterUtil.CUSTOMER_PHONE_NUMBER,
//                    PainterUtil.CUSTOMER_PHONE_NUMBER_COLOR, 1F, 330, 200);

            // 写出到字节流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PainterUtil.write(graphics2D, image, out);
            byte[] bytes = out.toByteArray();

            // 写出到七牛
            String url = qiNiuService.uploadByByte(bytes);

            return url;
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return "";
    }
}
