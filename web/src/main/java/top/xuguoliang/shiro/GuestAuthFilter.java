package top.xuguoliang.shiro;

import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import top.xuguoliang.common.exception.MessageCodes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author luwei
 */
public class GuestAuthFilter extends StatelessAuthFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        String token = getToken(request);
        if (StringUtils.isEmpty(token)) {
            final boolean canAccess = Objects.equals(((HttpServletRequest) request).getMethod(), HttpMethod.GET.name());
            if (!canAccess) {
                printUnauthorized(MessageCodes.AUTH_TOKEN, WebUtils.toHttp(response));
                return false;
            }
        } else {
            boolean loginSuccess = login(new Token(token));
            if (!loginSuccess) {
                printUnauthorized(MessageCodes.AUTH_TOKEN, WebUtils.toHttp(response));
                return false;
            }
        }
        return true;
    }
}
