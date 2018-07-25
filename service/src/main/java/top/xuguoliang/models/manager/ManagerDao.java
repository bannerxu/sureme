package top.xuguoliang.models.manager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ManagerDao extends JpaRepository<Manager, Integer> ,JpaSpecificationExecutor<Manager>{

    Manager findByAccountAndPasswordAndDeletedIsFalse(String account, String md5Password);

    Manager findByAccountAndDeletedIsFalse(String account);
}
