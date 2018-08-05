package top.xuguoliang.models.shareitem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ShareItemDao extends JpaRepository<ShareItem, Integer> {
    ShareItem findByShareUserIdAndBeShareUserId(Integer shareUserId, Integer beShareUserId);

    ShareItem findByBeShareUserId(Integer beShareUserId);

    Page<ShareItem> findByShareUserId(Integer userId, Pageable pageable);

    List<ShareItem> findByShareUserId(Integer userId);

    Page<ShareItem> findByShareUserIdIn(List<Integer> oneIdList, Pageable pageable);
}
