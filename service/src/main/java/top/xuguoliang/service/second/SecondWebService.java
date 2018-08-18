package top.xuguoliang.service.second;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import top.xuguoliang.models.second.SecondDao;
import top.xuguoliang.service.RedisKeyPrefix;
import top.xuguoliang.service.second.web.SecondWebPageResultVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Service
public class SecondWebService {

    @Resource
    private SecondDao secondDao;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 分页查询秒杀
     * @param pageable 分页信息
     * @return 分页秒杀
     */
    public Page<SecondWebPageResultVO> findPage(Pageable pageable) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        // 先查询缓存中是否有，没有再查数据库
        String seconds = valueOperations.get(RedisKeyPrefix.secondFindPage());
        return null;
    }
}
