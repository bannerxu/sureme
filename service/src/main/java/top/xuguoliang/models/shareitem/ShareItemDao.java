package top.xuguoliang.models.shareitem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * @author jinguoguo
 */
public interface ShareItemDao extends JpaRepository<ShareItem, Integer> {
    /**
     * 通过上线用户id和下线id获取分销记录
     *
     * @param shareUserId   上线用户id
     * @param beShareUserId 下线用户id
     * @return 分销记录
     */
    ShareItem findByShareUserIdAndBeShareUserId(Integer shareUserId, Integer beShareUserId);

    /**
     * 通过下线用户id查找分销记录
     *
     * @param beShareUserId 下线用户id
     * @return 分销记录
     */
    ShareItem findByBeShareUserId(Integer beShareUserId);

    /**
     * 通过上线用户id分页查找分销记录
     *
     * @param userId   用户id
     * @param pageable 分页信息
     * @return 分页分销记录
     */
    Page<ShareItem> findByShareUserId(Integer userId, Pageable pageable);

    /**
     * 通过下线用户id查找分销记录
     * @param userId 下线用户id
     * @return 分销记录列表
     */
    List<ShareItem> findByShareUserId(Integer userId);

    /**
     * 查找下线id在id列表中的分销记录
     * @param oneIdList id列表
     * @param pageable 分页信息
     * @return 分页分销记录
     */
    Page<ShareItem> findByShareUserIdIn(List<Integer> oneIdList, Pageable pageable);
}
