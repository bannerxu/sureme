package top.xuguoliang.service.moneywater;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.xuguoliang.models.moneywater.MoneyWater;
import top.xuguoliang.models.moneywater.MoneyWaterDao;

import javax.annotation.Resource;

/**
 * @author jinguoguo
 */
@Service
public class MoneyWaterService {

    @Resource
    private MoneyWaterDao moneyWaterDao;

    public Page<MoneyWater> findAll(Pageable pageable) {
        return moneyWaterDao.findAll(pageable);
    }
}
