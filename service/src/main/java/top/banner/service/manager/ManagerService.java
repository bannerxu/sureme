package top.banner.service.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import top.banner.common.constants.RoleConstant;
import top.banner.common.exception.ValidationException;
import top.banner.common.utils.CommonSpecUtil;
import top.banner.models.manager.Manager;
import top.banner.models.manager.ManagerDao;
import top.banner.service.manager.cms.ManagerAddVO;
import top.banner.service.manager.cms.ManagerEditVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Service
public class ManagerService {

    private static final Logger logger = LoggerFactory.getLogger(ManagerService.class);

    @Resource
    private ManagerDao managerDao;

    @Resource
    private CommonSpecUtil<Manager> specUtil;

    /**
     * 分页查找管理员
     *
     * @param pageable
     * @return
     */
    public Page<Manager> findPage(Pageable pageable) {
        logger.debug("调用接口：分页查找管理员");
        Specifications<Manager> spec = Specifications.where(specUtil.equal("role", RoleConstant.ADMIN))
                .and(specUtil.equal("deleted",false));
        return managerDao.findAll(spec,pageable);
    }

    /**
     * 添加
     *
     * @param addVO
     * @return
     */
    public Manager add(ManagerAddVO addVO) {
        logger.debug("调用接口：添加管理员");
        Manager one = managerDao.findByAccountAndDeletedIsFalse(addVO.getAccount());
        if (one != null) {
            throw new ValidationException("用户名已存在");
        }
        Manager manager = new Manager();
        BeanUtils.copyProperties(addVO, manager);
        manager.setPassword(DigestUtils.md5DigestAsHex((addVO.getPassword()).getBytes()));
        return managerDao.save(manager);
    }

    /**
     * 修改
     *
     * @param editVO 管理员信息
     * @return 管理员信息
     */
    public Manager update(ManagerEditVO editVO) {
        logger.debug("调用接口：修改管理员");
        Integer managerId = editVO.getManagerId();
        Manager manager = managerDao.getOne(managerId);
        BeanUtils.copyProperties(editVO, manager);
        return managerDao.save(manager);
    }

    /**
     * 删除
     *
     * @param managerId 管理员id
     */
    public void delete(int managerId) {
        logger.debug("调用接口：删除管理员");
        Manager manager = managerDao.getOne(managerId);
        manager.setDeleted(true);
        managerDao.save(manager);
    }
}
