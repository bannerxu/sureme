package top.xuguoliang.service.groupbuying;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.models.groupbuying.GroupBuying;
import top.xuguoliang.models.groupbuying.GroupBuyingDao;
import top.xuguoliang.service.groupbuying.cms.GroupBuyingCmsAddParamVO;
import top.xuguoliang.service.groupbuying.cms.GroupBuyingCmsResultVO;
import top.xuguoliang.service.groupbuying.cms.GroupBuyingCmsUpdateParamVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Service
public class GroupBuyingCmsService {

    private static final Logger logger = LoggerFactory.getLogger(GroupBuyingCmsService.class);

    @Resource
    private GroupBuyingDao groupBuyingDao;

    /**
     * 分页查询
     *
     * @param pageable 分页信息
     * @return 分页的结果
     */
    public Page<GroupBuyingCmsResultVO> findPage(Pageable pageable) {
        return groupBuyingDao.findAll(pageable).map(groupBuying -> {
            GroupBuyingCmsResultVO vo = new GroupBuyingCmsResultVO();
            BeanUtils.copyNonNullProperties(groupBuying, vo);
            return vo;
        });
    }

    /**
     * 单个查询
     *
     * @param groupBuyingId id
     * @return 单个结果
     */
    public GroupBuyingCmsResultVO getGroupBuying(Integer groupBuyingId) {
        GroupBuyingCmsResultVO resultVO = new GroupBuyingCmsResultVO();
        GroupBuying groupBuying = groupBuyingDao.findOne(groupBuyingId);
        BeanUtils.copyNonNullProperties(groupBuying, resultVO);
        // todo 执行业务

        return resultVO;
    }

    /**
     * 添加
     *
     * @param addVO 添加的信息
     * @return 添加成功后的实体信息
     */
    public GroupBuyingCmsResultVO addGroupBuying(GroupBuyingCmsAddParamVO addVO) {
        // 保存
        GroupBuying groupBuying = new GroupBuying();
        BeanUtils.copyNonNullProperties(addVO, groupBuying);
        GroupBuying groupBuyingSave = groupBuyingDao.saveAndFlush(groupBuying);
        // 返回值
        GroupBuyingCmsResultVO resultVO = new GroupBuyingCmsResultVO();
        BeanUtils.copyNonNullProperties(groupBuyingSave, resultVO);

        return resultVO;
    }

    /**
     * 修改
     *
     * @param updateVO 修改的信息
     * @return 修改成功后的实体信息
     */
    public GroupBuyingCmsResultVO updateGroupBuying(Integer groupBuyingId, GroupBuyingCmsUpdateParamVO updateVO) {
        // 修改
        GroupBuying groupBuying = groupBuyingDao.findOne(groupBuyingId);
        if (ObjectUtils.isEmpty(groupBuying) || groupBuying.getDeleted()) {
            // 实体已被删除
            throw new ValidationException(MessageCodes.CMS_GROUP_BUYING_NOT_EXIST);
        }
        BeanUtils.copyNonNullProperties(updateVO, groupBuying);
        GroupBuying groupBuyingSave = groupBuyingDao.saveAndFlush(groupBuying);

        // 返回值
        GroupBuyingCmsResultVO resultVO = new GroupBuyingCmsResultVO();
        BeanUtils.copyNonNullProperties(groupBuyingSave, resultVO);

        return resultVO;
    }

    /**
     * 删除
     *
     * @param groupBuyingId 实体id
     */
    public void deleteGroupBuying(Integer groupBuyingId) {
        GroupBuying groupBuying = groupBuyingDao.findOne(groupBuyingId);
        if (ObjectUtils.isEmpty(groupBuying)) {
            logger.warn("实体不存在");
            return ;
        }
        groupBuying.setDeleted(true);
        groupBuyingDao.saveAndFlush(groupBuying);
    }
}
