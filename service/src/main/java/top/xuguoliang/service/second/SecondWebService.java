package top.xuguoliang.service.second;

import org.springframework.stereotype.Service;
import top.xuguoliang.models.second.SecondDao;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Service
public class SecondWebService {

    @Resource
    private SecondDao secondDao;

}
