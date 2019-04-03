package top.banner.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.banner.service.RedisKeyPrefix;


import javax.annotation.Resource;

/**
 * @author luwei
 */
@Component
public class RedisRealm extends AuthorizingRealm {

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> redis;

    @Override
    public String getName() {
        return "redisRealm";
    }
    private Logger logger = LoggerFactory.getLogger(RedisRealm.class);

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && Token.class.isAssignableFrom(token.getClass());
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        final Token Token = (Token) authenticationToken;
        final String tokenString = Token.getToken();
        final String companyId = redis.get(RedisKeyPrefix.webAuthTokenToId(tokenString));
        logger.info("------userId:{}------------tokenString {}",companyId,tokenString);
        if (!StringUtils.isEmpty(companyId)) {
            return new SimpleAuthenticationInfo(companyId, tokenString, getName());
        }
        return null;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

}
