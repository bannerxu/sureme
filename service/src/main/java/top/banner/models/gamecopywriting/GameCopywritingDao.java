package top.banner.models.gamecopywriting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @description: dao
 * @author: XGL
 * @create: 2018-08-29 10:37
 **/
public interface GameCopywritingDao extends JpaRepository<GameCopywriting, Integer> {
    List<GameCopywriting> findByMoodTypeAndDeletedIsFalse(MoodType key);
}
