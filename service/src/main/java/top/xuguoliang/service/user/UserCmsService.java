package top.xuguoliang.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import top.xuguoliang.common.utils.CommonSpecUtil;
import top.xuguoliang.models.user.User;
import top.xuguoliang.models.user.UserDao;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Service
public class UserCmsService {

    @Resource
    private Logger logger = LoggerFactory.getLogger(UserCmsService.class);

    @Resource
    private UserDao userDao;

    @Resource
    private CommonSpecUtil<User> specUtil;

    /**
     * 分页获取用户
     *
     * @param nickName 用户昵称
     * @param pageable 分页信息
     * @return 分页用户信息
     */
    public Page<User> findPage(String nickName, Pageable pageable) {
        logger.debug("调用接口：分页获取用户信息，传入的昵称[{}]", nickName);
        Specifications<User> spec = Specifications.where(specUtil.like("nickName", nickName));
        return userDao.findAll(spec, pageable);
    }
}
