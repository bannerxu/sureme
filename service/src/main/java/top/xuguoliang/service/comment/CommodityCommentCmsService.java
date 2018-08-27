package top.xuguoliang.service.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.common.utils.CommonSpecUtil;
import top.xuguoliang.models.comment.CommodityComment;
import top.xuguoliang.models.comment.CommodityCommentDao;
import top.xuguoliang.models.user.User;
import top.xuguoliang.models.user.UserDao;
import top.xuguoliang.service.comment.cms.CmsReplyCommentParamVO;
import top.xuguoliang.service.comment.cms.CommodityCommentCmsResultVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Service
public class CommodityCommentCmsService {

    @Resource
    private CommodityCommentDao commodityCommentDao;

    @Resource
    private UserDao userDao;

    @Resource
    private CommonSpecUtil<CommodityComment> commonSpecUtil;

    /**
     * 分页查询商品评论
     *
     * @param commodityId 商品id，如果为空查询所有
     * @param pageable    分页信息
     * @return 分页商品评论
     */
    public Page<CommodityCommentCmsResultVO> findPage(Integer commodityId, Pageable pageable) {
        Specification<CommodityComment> deleted = commonSpecUtil.equal("deleted", false);
        Page<CommodityCommentCmsResultVO> resultVOS;
        if (ObjectUtils.isEmpty(commodityId)) {
            resultVOS = commodityCommentDao.findAll(deleted, pageable).map(this::convertCommodityCommentToVO);
        } else {
            Specification<CommodityComment> specification = commonSpecUtil.equal("commodityId", commodityId);
            Specifications<CommodityComment> specifications = Specifications.where(deleted).and(specification);
            resultVOS = commodityCommentDao.findAll(specifications, pageable).map(this::convertCommodityCommentToVO);
        }
        return resultVOS;
    }

    /**
     * 商品评论转VO
     *
     * @param commodityComment 商品评论
     * @return VO
     */
    private CommodityCommentCmsResultVO convertCommodityCommentToVO(CommodityComment commodityComment) {
        CommodityCommentCmsResultVO vo = new CommodityCommentCmsResultVO();
        BeanUtils.copyNonNullProperties(commodityComment, vo);
        Integer userId = commodityComment.getUserId();
        User user = userDao.findOne(userId);
        if (!ObjectUtils.isEmpty(user)) {
            vo.setNickname(user.getNickName());
        }
        return vo;
    }

    /**
     * 删除商品评论
     *
     * @param commodityCommentId 商品评论id
     */
    public void deleteComment(Integer commodityCommentId) {
        CommodityComment commodityComment = commodityCommentDao.findOne(commodityCommentId);
        if (!ObjectUtils.isEmpty(commodityComment)) {
            commodityComment.setDeleted(true);
            commodityCommentDao.saveAndFlush(commodityComment);
        }
    }

    /**
     * 回复评论
     *
     * @param commodityCommentId 评论id
     */
    public void replyComment(Integer commodityCommentId, CmsReplyCommentParamVO cmsReplyCommentParamVO) {
        CommodityComment commodityComment = commodityCommentDao.findOne(commodityCommentId);
        commodityComment.setCommentReply(cmsReplyCommentParamVO.getCommentReply());
        commodityCommentDao.saveAndFlush(commodityComment);
    }

}
