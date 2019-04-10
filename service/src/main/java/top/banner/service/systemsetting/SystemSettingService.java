package top.banner.service.systemsetting;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.banner.common.utils.BeanUtils;
import top.banner.models.systemsetting.SystemSetting;
import top.banner.models.systemsetting.SystemSettingDao;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Service
public class SystemSettingService {

    @Resource
    private SystemSettingDao systemSettingDao;

    /**
     * 获取系统设置
     *
     * @return 系统设置
     */
    public SystemSetting getSystemSetting() {
        SystemSetting systemSetting = systemSettingDao.getOne(1);
        if (ObjectUtils.isEmpty(systemSetting)) {
            return initSystemSetting();
        } else {
            return systemSetting;
        }
    }

    /**
     * 初始化系统设置
     *
     * @return 系统设置
     */
    private SystemSetting initSystemSetting() {
        SystemSetting systemSetting = new SystemSetting();
        systemSetting.setFirstScale(20.0);
        systemSetting.setSecondScale(10.0);
        systemSetting.setThirdScale(5.0);
        return systemSettingDao.saveAndFlush(systemSetting);
    }

    /**
     * 修改系统设置
     * @param systemSetting 系统设置
     * @return 系统设置
     */
    public SystemSetting modifySystemSetting(SystemSetting systemSetting) {
        SystemSetting one = systemSettingDao.getOne(1);
        BeanUtils.copyNonNullProperties(systemSetting, one);
        one.setSystemSettingId(1);
        return systemSettingDao.saveAndFlush(one);
    }
}
