package top.xuguoliang.shiro;

import top.xuguoliang.models.manager.Manager;
import top.xuguoliang.models.manager.ManagerDao;
import top.xuguoliang.service.RedisKeyPrefix;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author medical
 */
@Component
public class CmsRealm extends AuthorizingRealm {
    @Resource
    private ManagerDao managerDao;

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> redis;

    private static final Logger logger = LoggerFactory.getLogger(AuthorizingRealm.class);

    @Override
    public String getName() {
        return "redisRealm";
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && Token.class.isAssignableFrom(token.getClass());
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        Token token = (Token) authenticationToken;
        String tokenString = token.getToken();
        String userId = this.redis.get(RedisKeyPrefix.cmsAuthTokenToId(tokenString));
        this.logger.info("--------------------userId：{} ; tokenString：{}", userId, tokenString);
        return !StringUtils.isEmpty(userId) ? new SimpleAuthenticationInfo(userId, tokenString, this.getName()) : null;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String userId = (String) principals.getPrimaryPrincipal();
        if (!StringUtils.isEmpty(userId)) {
            String role = this.redis.get(RedisKeyPrefix.cmsAuthIdToRole(Integer.valueOf(userId)));
            this.logger.info("--------------------userId：{} ; role：{}", userId, role);
            if (!StringUtils.isEmpty(role)) {
                SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
                info.addRole(role);
                return info;
            }
        }
        return null;
    }


}
