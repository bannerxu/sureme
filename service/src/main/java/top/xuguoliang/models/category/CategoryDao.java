package top.xuguoliang.models.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author jinguoguo
 */
public interface CategoryDao extends JpaSpecificationExecutor<Category>, JpaRepository<Category, Integer> {
}
