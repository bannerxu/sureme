package top.xuguoliang.controllers;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;

/**
 * @author jinguoguo
 */
public class ManagerHelper {
    public static Integer getManagerId() {
        final Subject subject = SecurityUtils.getSubject();
        final Object managerId = subject.getPrincipal();
        if (managerId == null) {
            throw new AuthenticationException();
        }
        return Integer.parseInt(managerId.toString());
    }
}
