package top.xuguoliang.service.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.common.utils.CommonSpecUtil;
import top.xuguoliang.models.comment.ArticleComment;
import top.xuguoliang.models.comment.ArticleCommentDao;
import top.xuguoliang.service.comment.cms.ArticleCommentCmsResultVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Service
public class ArticleCommentCmsService {

    @Resource
    private ArticleCommentDao articleCommentDao;

    @Resource
    private CommonSpecUtil<ArticleComment> commonSpecUtil;

    /**
     * 分页查询文章评论
     *
     * @param articleId 文章id，如果为空查询所有
     * @param pageable  分页信息
     * @return 分页文章评论
     */
    public Page<ArticleCommentCmsResultVO> findPage(Integer articleId, Pageable pageable) {
        Page<ArticleCommentCmsResultVO> resultVOS;
        Specification<ArticleComment> deleted = commonSpecUtil.equal("deleted", false);
        if (ObjectUtils.isEmpty(articleId)) {
            resultVOS = articleCommentDao.findAll(deleted, pageable).map(this::convertArticleCommentToVO);
        } else {
            Specification<ArticleComment> specification = commonSpecUtil.equal("articleId", articleId);
            Specifications<ArticleComment> specifications = Specifications.where(deleted).and(specification);
            resultVOS = articleCommentDao.findAll(specifications, pageable).map(this::convertArticleCommentToVO);
        }
        return resultVOS;
    }

    /**
     * 文章评论转成VO
     *
     * @param articleComment 文章评论
     * @return VO
     */
    private ArticleCommentCmsResultVO convertArticleCommentToVO(ArticleComment articleComment) {
        ArticleCommentCmsResultVO vo = new ArticleCommentCmsResultVO();
        BeanUtils.copyNonNullProperties(articleComment, vo);
        return vo;
    }

    /**
     * 删除文章评论
     *
     * @param articleCommentId 文章评论
     */
    public void deleteComment(Integer articleCommentId) {
        ArticleComment articleComment = articleCommentDao.findOne(articleCommentId);
        if (!ObjectUtils.isEmpty(articleComment)) {
            articleComment.setDeleted(true);
            articleCommentDao.saveAndFlush(articleComment);
        }
    }

}
