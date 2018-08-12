package top.xuguoliang.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.xuguoliang.common.utils.WeChatUtil;
import top.xuguoliang.models.user.PregnancyTypeEnum;
import top.xuguoliang.models.user.User;
import top.xuguoliang.models.user.UserDao;
import top.xuguoliang.service.RedisKeyPrefix;
import top.xuguoliang.service.user.web.AuthorizeVO;
import top.xuguoliang.service.user.web.UserSetPregnancyVO;
import top.xuguoliang.service.user.web.WeChatUser;

import javax.annotation.Resource;
import java.util.Date;
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
        User user = userDao.findOne(1);

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
     * @param userId        用户id
     * @param userSetPregnancyVO 设置信息
     * @return 成功与否
     */
    public Boolean setPregnancyType(Integer userId, UserSetPregnancyVO userSetPregnancyVO) {
        PregnancyTypeEnum pregnancyType = userSetPregnancyVO.getPregnancyType();
        User user = userDao.findOne(userId);
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
}
