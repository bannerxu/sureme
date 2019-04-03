package top.banner.models.withdraw;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author 19355
 */
public interface WithdrawDao extends JpaRepository<Withdraw,Integer>,JpaSpecificationExecutor<Withdraw> {
    List<Withdraw> findByUserId(Integer userId);
}
