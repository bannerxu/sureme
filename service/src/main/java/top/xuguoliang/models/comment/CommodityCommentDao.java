package top.xuguoliang.models.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface CommodityCommentDao extends JpaSpecificationExecutor<CommodityComment>, JpaRepository<CommodityComment, Integer> {
}
