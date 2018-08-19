package top.xuguoliang.models.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface UserDao extends JpaRepository<User,Integer>,JpaSpecificationExecutor<User> {

    User findByOpenId(String openId);

    List<User> findByNickNameLike(String nickName);
}
