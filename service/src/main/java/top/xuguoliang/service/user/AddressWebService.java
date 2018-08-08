package top.xuguoliang.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.models.user.Address;
import top.xuguoliang.models.user.AddressDao;
import top.xuguoliang.models.user.User;
import top.xuguoliang.models.user.UserDao;
import top.xuguoliang.service.user.web.AddressWebAddParamVO;
import top.xuguoliang.service.user.web.AddressWebUpdateParamVO;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author jinguoguo
 */
@Service
public class AddressWebService {

    private static final Logger logger = LoggerFactory.getLogger(AddressWebService.class);

    @Resource
    private AddressDao addressDao;

    @Resource
    private UserDao userDao;


    /**
     * 添加收货地址
     *
     * @param addressWebAddParamVO 收货地址信息
     * @return 添加成功后收货地址的信息
     */
    public Address addAddress(AddressWebAddParamVO addressWebAddParamVO) {
        Date date = new Date();
        Address address = new Address();

        // 判断传入的用户id对应的用户是否存在
        Integer userId = addressWebAddParamVO.getUserId();
        User user = userDao.findOne(userId);
        if (ObjectUtils.isEmpty(user) || user.getDeleted()) {
            logger.error("调用添加地址业务错误：对应的用户不存在");
            throw new ValidationException(MessageCodes.WEB_USER_NOT_EXIST, "该用户不存在");
        }

        BeanUtils.copyNonNullProperties(addressWebAddParamVO, address);

        address.setCreateTime(date);
        address.setCreateTime(date);

        return addressDao.saveAndFlush(address);
    }

    /**
     * 分页查询收货地址
     *
     * @param pageable 分页信息
     * @return 分页收货地址
     */
    public Page<Address> findPage(Pageable pageable) {
        return addressDao.findAll(pageable);
    }

    /**
     * 查询单个收货地址
     *
     * @param addressId 地址id
     * @return 收货地址
     */
    public Address getAddress(Integer addressId) {
        return addressDao.findOne(addressId);
    }

    /**
     * 修改收货地址
     *
     * @param vo 地址信息
     * @return 修改完成后的地址信息
     */
    public Address updateAddress(Integer addressId, AddressWebUpdateParamVO vo) {
        Address address = addressDao.findOne(addressId);
        if (ObjectUtils.isEmpty(address) || address.getDeleted()) {
            logger.error("调用修改地址业务错误：原地址为空");
            throw new ValidationException(MessageCodes.WEB_ADDRESS_NOT_EXIST, "修改出错，查不到原地址");
        }

        BeanUtils.copyNonNullProperties(vo, address);
        address.setUpdateTime(new Date());
        return addressDao.saveAndFlush(address);
    }

    /**
     * 删除收货地址
     *
     * @param addressId 地址id
     */
    public void deleteAddress(Integer addressId) {
        Address address = addressDao.findOne(addressId);
        if (!ObjectUtils.isEmpty(address)) {
            address.setDeleted(true);
            addressDao.saveAndFlush(address);
        }
    }

    /**
     * 设置默认地址
     *
     * @param userId 用户id
     * @param addressId 地址id
     */
    public void setDefaultAddress(Integer userId, Integer addressId) {
        Address address = addressDao.findOne(addressId);
        if (ObjectUtils.isEmpty(address) || address.getDeleted()) {
            logger.error("设置默认地址失败：地址不存在");
            throw new ValidationException(MessageCodes.WEB_ADDRESS_NOT_EXIST);
        }
        List<Address> addresses = addressDao.findByUserIdIsAndDeletedIsFalse(userId);
        addresses.forEach(adr -> {
            if (adr.getAddressId().equals(addressId)) {
                adr.setIsDefault(true);
            } else {
                adr.setIsDefault(false);
            }
        });

        addressDao.save(addresses);
    }

}
