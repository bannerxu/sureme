package top.xuguoliang.models.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface UserDao extends JpaRepository<User,Integer>,JpaSpecificationExecutor<User> {

    User findByOpenId(String openId);
}
