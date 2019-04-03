package top.banner.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.banner.common.exception.MessageCodes;
import top.banner.common.exception.ValidationException;
import top.banner.common.utils.CommonSpecUtil;
import top.banner.models.user.User;
import top.banner.models.user.UserDao;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Service
public class UserCmsService {

    private static final Logger logger = LoggerFactory.getLogger(UserCmsService.class);

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
        Specifications<User> spec = Specifications.where(specUtil.like("nickName", nickName)).and(specUtil.equal("deleted", false));
        return userDao.findAll(spec, pageable);
    }

    public User updateUser(User user) {
        Integer userId = user.getUserId();
        if (!ObjectUtils.isEmpty(userId)) {
            User oldUser = userDao.getOne(userId);
            if (ObjectUtils.isEmpty(oldUser)) {
                throw new ValidationException(MessageCodes.OLD_USER_EMPTY, "原用户信息不存在");
            }
            BeanUtils.copyProperties(user, oldUser);
            return userDao.saveAndFlush(oldUser);
        } else {
            throw new ValidationException(MessageCodes.USER_ID_EMPTY, "用户id不能为空");
        }
    }

    /**
     * 删除用户
     *
     * @param userId 用户id
     */
    public void deleteUser(Integer userId) {
        if (!ObjectUtils.isEmpty(userId)) {
            userDao.deleteById(userId);
        }
    }
}
