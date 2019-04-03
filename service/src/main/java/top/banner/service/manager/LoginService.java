package top.banner.service.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import top.banner.common.exception.MessageCodes;
import top.banner.common.exception.ValidationException;
import top.banner.models.manager.Manager;
import top.banner.models.manager.ManagerDao;
import top.banner.service.RedisKeyPrefix;
import top.banner.service.manager.cms.ManagerLoginVO;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author jinguoguo
 */
@Service
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ManagerDao managerDao;

    public Manager login(ManagerLoginVO loginVO) {
        logger.debug("调用接口：管理员登录");
        ValueOperations<String, String> redis = stringRedisTemplate.opsForValue();

        //判断账号密码是否正确
        String md5Password = DigestUtils.md5DigestAsHex((loginVO.getPassword()).getBytes());
        Manager manager = managerDao.findByAccountAndPasswordAndDeletedIsFalse(loginVO.getAccount(), md5Password);
        if (Objects.nonNull(manager)) {
            int managerId = manager.getManagerId();
            int role = manager.getRole();

            String token = redis.get(RedisKeyPrefix.cmsAuthIdToToken(managerId));
            this.stringRedisTemplate.delete(RedisKeyPrefix.cmsAuthTokenToId(token));
            this.stringRedisTemplate.delete(RedisKeyPrefix.cmsAuthIdToToken(managerId));
            this.stringRedisTemplate.delete(RedisKeyPrefix.cmsAuthIdToRole(managerId));
            String uuid = UUID.randomUUID().toString();
            String newToken = StringUtils.deleteAny(uuid, "-") + Long.toString(System.currentTimeMillis(), Character.MAX_RADIX);
            redis.set(RedisKeyPrefix.cmsAuthTokenToId(newToken), managerId + "", 7, TimeUnit.DAYS);
            redis.set(RedisKeyPrefix.cmsAuthIdToToken(managerId), newToken, 7, TimeUnit.DAYS);
            redis.set(RedisKeyPrefix.cmsAuthIdToRole(managerId), role + "", 7, TimeUnit.DAYS);
            manager.setToken(newToken);
            return manager;
        } else {
            //密码错误或账户不存在
            throw new ValidationException(MessageCodes.AUTH_ACCOUNT_PASSWORD_WRONG);
        }
    }

}
