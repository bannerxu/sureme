package top.banner.models.relation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author jinguoguo
 */
public interface RelationUserGroupBuyingDao extends JpaSpecificationExecutor<RelationUserGroupBuying>,
        JpaRepository<RelationUserGroupBuying, Integer> {

    /**
     * 通过用户id查找关系
     *
     * @param userId 用户id
     * @return 关系
     */
    List<RelationUserGroupBuying> findByUserIdIs(Integer userId);

    /**
     * 通过拼团id查找关系
     *
     * @param groupBuyingId 拼团id
     * @return 关系
     */
    List<RelationUserGroupBuying> findByGroupBuyingIdIs(Integer groupBuyingId);

    /**
     * 通过用户id和拼团id查找关系
     *
     * @param userId        用户id
     * @param groupBuyingId 拼团id
     * @return 关系
     */
    List<RelationUserGroupBuying> findByUserIdIsAndGroupBuyingIdIs(Integer userId, Integer groupBuyingId);
}
