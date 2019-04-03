package top.banner.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.banner.common.utils.BeanUtils;
import top.banner.common.utils.WeChatUtil;
import top.banner.models.article.*;
import top.banner.models.user.PregnancyTypeEnum;
import top.banner.models.user.User;
import top.banner.models.user.UserDao;
import top.banner.service.RedisKeyPrefix;
import top.banner.service.user.web.ArticleStarResultVO;
import top.banner.service.user.web.AuthorizeVO;
import top.banner.service.user.web.UserSetPregnancyVO;
import top.banner.service.user.web.WeChatUser;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author jinguoguo
 */
@Service
public class UserWebService {

    private static final Logger logger = LoggerFactory.getLogger(UserWebService.class);

    @Resource
    private UserDao userDao;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private WeChatUtil weChatUtil;

    @Resource
    private ArticleDao articleDao;

    @Resource
    private ArticleBannerDao articleBannerDao;

    @Resource
    private ArticleStarDao articleStarDao;

    public User authorize(AuthorizeVO authorizeVO) {
        logger.debug("用户认证，传入VO：{}", authorizeVO);
        ValueOperations<String, String> redis = stringRedisTemplate.opsForValue();
        WeChatUser weChatUser = weChatUtil.authorize(authorizeVO);
        Assert.notNull(weChatUser, "授权失败");

        User user = userDao.findByOpenId(weChatUser.getOpenId());
        if (Objects.isNull(user)) {
            user = new User();
            BeanUtils.copyProperties(weChatUser, user);
            user.setAvatarUrl(weChatUser.getAvatar());
            user.setNickName(weChatUser.getNickname());
            user.setCreateTime(new Date());
            userDao.save(user);
        }
        Integer userId = user.getUserId();
        String token = redis.get(RedisKeyPrefix.webAuthIdToToken(userId));
        stringRedisTemplate.delete(RedisKeyPrefix.webAuthIdToToken(userId));
        stringRedisTemplate.delete(RedisKeyPrefix.webAuthTokenToId(token));

        String uuid = UUID.randomUUID().toString();
        String newToken = StringUtils.deleteAny(uuid, "-") + Long.toString(System.currentTimeMillis(), Character.MAX_RADIX);

        redis.set(RedisKeyPrefix.webAuthIdToToken(userId), newToken, 7, TimeUnit.DAYS);
        redis.set(RedisKeyPrefix.webAuthTokenToId(newToken), userId + "", 7, TimeUnit.DAYS);
        user.setToken(newToken);
        return user;
    }


    public User authorize() {
        User user = userDao.getOne(1);

        Integer userId = user.getUserId();

        ValueOperations<String, String> redis = stringRedisTemplate.opsForValue();
        String token = redis.get(RedisKeyPrefix.webAuthIdToToken(userId));
        stringRedisTemplate.delete(RedisKeyPrefix.webAuthIdToToken(userId));
        stringRedisTemplate.delete(RedisKeyPrefix.webAuthTokenToId(token));

        String uuid = UUID.randomUUID().toString();
        String newToken = StringUtils.deleteAny(uuid, "-") + Long.toString(System.currentTimeMillis(), Character.MAX_RADIX);

        redis.set(RedisKeyPrefix.webAuthIdToToken(userId), newToken, 7, TimeUnit.DAYS);
        redis.set(RedisKeyPrefix.webAuthTokenToId(newToken), userId + "", 7, TimeUnit.DAYS);
        user.setToken(newToken);
        return user;
    }

    /**
     * 设置孕期
     *
     * @param userId             用户id
     * @param userSetPregnancyVO 设置信息
     * @return 成功与否
     */
    public Boolean setPregnancyType(Integer userId, UserSetPregnancyVO userSetPregnancyVO) {
        PregnancyTypeEnum pregnancyType = userSetPregnancyVO.getPregnancyType();
        User user = userDao.getOne(userId);
        if (ObjectUtils.isEmpty(user)) {
            logger.error("设置孕期失败：用户不存在");
            return false;
        }
        user.setPregnancyType(pregnancyType);
        if (pregnancyType.equals(PregnancyTypeEnum.PREGNANT)) {
            user.setPregnantDate(userSetPregnancyVO.getPregnantDate());
        } else if (pregnancyType.equals(PregnancyTypeEnum.AFTER_PREGNANT)) {
            user.setBabyBirthday(userSetPregnancyVO.getBabyBirthday());
        }
        userDao.saveAndFlush(user);
        return true;
    }

    /**
     * 查询用户收藏
     *
     * @param userId 用户id
     */
    public Page<ArticleStarResultVO> findStar(Integer userId, Pageable pageable) {
        return articleStarDao.findByUserIdIs(userId, pageable).map(articleStar -> {
            if (ObjectUtils.isEmpty(articleStar)) {
                logger.warn("查询文章收藏错误：文章收藏不存在");
                return null;
            }
            ArticleStarResultVO vo = new ArticleStarResultVO();
            BeanUtils.copyNonNullProperties(articleStar, vo);

            Integer articleId = articleStar.getArticleId();
            Article article = articleDao.findByArticleIdIsAndDeletedIsFalse(articleId);
            if (ObjectUtils.isEmpty(article)) {
                logger.warn("查询文章收藏错误：文章不存在");
                return null;
            }
            BeanUtils.copyNonNullProperties(article, vo);

            List<ArticleBanner> banners = articleBannerDao.findByArticleIdIsAndDeletedIsFalseOrderByArticleBannerIdAsc(articleId);
            if (ObjectUtils.isEmpty(banners)) {
                logger.warn("查询文章收藏错误，文章图片不存在");
                return null;
            }
            vo.setArticleImage(banners.get(0).getArticleBannerUrl());
            return vo;
        });
    }

    /**
     * 删除用户收藏
     *
     * @param userId        用户id
     * @param articleStarId 文章收藏id
     */
    public void deleteStar(Integer userId, Integer articleStarId) {
        ArticleStar articleStar = articleStarDao.findByArticleStarIdIsAndUserIdIs(articleStarId, userId);
        if (!ObjectUtils.isEmpty(articleStar)) {
            articleStarDao.deleteById(articleStarId);
        }
    }


    public User getUser(Integer userId) {
        return userDao.getOne(userId);
    }
}
