package top.xuguoliang.models.brokerage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrokerageDao extends JpaRepository<Brokerage, Integer> {
    Page<Brokerage> findAllByUserId(Integer userId, Pageable pageable);
}
