package top.xuguoliang.service.shareitem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.models.shareitem.ShareItem;
import top.xuguoliang.models.shareitem.ShareItemDao;
import top.xuguoliang.models.user.User;
import top.xuguoliang.models.user.UserDao;
import top.xuguoliang.service.shareitem.web.ShareItemAddVO;
import top.xuguoliang.service.shareitem.web.ShareItemVO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShareItemWebService {

    @Resource
    private ShareItemDao shareItemDao;
    @Resource
    private UserDao userDao;

    /**
     * 通过分享成为下线
     *
     * @param addVO
     * @return
     */
    public ShareItem addShareItem(ShareItemAddVO addVO) {
        ShareItem shareItem = shareItemDao.findByShareUserIdAndBeShareUserId(addVO.getShareUserId(), addVO.getBeShareUserId());
        ShareItem beShareItem = shareItemDao.findByShareUserIdAndBeShareUserId(addVO.getBeShareUserId(), addVO.getShareUserId());
        if (shareItem != null && beShareItem != null) {
            throw new ValidationException(MessageCodes.SHAREITEM_IS_EXIST);
        }
        //判断当前用户是否是其他人的下线
        ShareItem share = shareItemDao.findByBeShareUserId(addVO.getBeShareUserId());
        if (share != null) {
            throw new ValidationException(MessageCodes.SHAREITEM_IS_EXIST);
        }
        //添加关系
        share = new ShareItem();
        BeanUtils.copyNonNullProperties(addVO, share);
        return shareItemDao.save(share);

    }


    /**
     * 我的下线
     *
     * @param userId
     * @param type
     * @param pageable
     * @return
     */
    public Page<ShareItemVO> findShareItemPage(Integer userId, Integer type, Pageable pageable) {
        if (type == 1) {
            //获取一级下线
            return shareItemDao.findByShareUserId(userId, pageable)
                    .map(this::toShareItemVO);
        } else if (type == 2) {
            //获取一级下线，通过一级下线获取二级下线
            List<Integer> oneIdList = shareItemDao.findByShareUserId(userId).stream()
                    .map(ShareItem::getBeShareUserId)
                    .collect(Collectors.toList());
            if (oneIdList.size() == 0) {
                return new PageImpl<>(new ArrayList<>(1), pageable, 0);
            } else {
                return shareItemDao.findByShareUserIdIn(oneIdList, pageable).map(this::toShareItemVO);
            }

        }
        return null;
    }

    /**
     * shareItem --> shareItemVO
     *
     * @param shareItem
     * @return
     */
    private ShareItemVO toShareItemVO(ShareItem shareItem) {
        ShareItemVO shareItemVO = new ShareItemVO();
        User user = userDao.findOne(shareItem.getBeShareUserId());
        BeanUtils.copyNonNullProperties(user, shareItemVO);
        return shareItemVO;
    }
}
