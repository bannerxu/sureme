package top.xuguoliang.models.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface AddressDao extends JpaSpecificationExecutor<Address>, JpaRepository<Address, Integer> {
}
