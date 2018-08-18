package top.xuguoliang.service.second;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.xuguoliang.common.exception.MessageCodes;
import top.xuguoliang.common.exception.ValidationException;
import top.xuguoliang.common.utils.BeanUtils;
import top.xuguoliang.models.second.Second;
import top.xuguoliang.models.second.SecondDao;
import top.xuguoliang.service.RedisKeyPrefix;
import top.xuguoliang.service.second.web.SecondWebDetailVO;
import top.xuguoliang.service.second.web.SecondWebPageResultVO;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Service
public class SecondWebService {

    private static final Logger logger = LoggerFactory.getLogger(SecondWebService.class);

    @Resource
    private SecondDao secondDao;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 分页查询秒杀
     *
     * @param pageable 分页信息
     * @return 分页秒杀
     */
    public Page<SecondWebPageResultVO> findPage(Pageable pageable) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        // 先查询缓存中是否有，没有再查数据库
        String seconds = valueOperations.get(RedisKeyPrefix.secondFindPage());
        return null;
    }

    /**
     * 查询秒杀详情
     *
     * @param secondId 秒杀id
     * @return 秒杀详情
     */
    public SecondWebDetailVO getDetail(Integer secondId) {
        Second second = secondDao.findOne(secondId);
        if (ObjectUtils.isEmpty(second) || second.getDeleted()) {
            logger.error("查询秒杀详情失败：秒杀{} 不存在", secondId);
            throw new ValidationException(MessageCodes.WEB_SECOND_NOT_EXIST);
        }
        SecondWebDetailVO detailVO = new SecondWebDetailVO();
        BeanUtils.copyNonNullProperties(second, detailVO);

        return detailVO;
    }
}
