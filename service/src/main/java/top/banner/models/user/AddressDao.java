package top.banner.models.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author jinguoguo
 */
public interface AddressDao extends JpaSpecificationExecutor<Address>, JpaRepository<Address, Integer> {
    List<Address> findByUserIdIsAndDeletedIsFalse(Integer userId);

    Address findByAddressIdIsAndDeletedIsFalse(Integer addressId);

    Page<Address> findByDeletedIsFalse(Pageable pageable);
}
