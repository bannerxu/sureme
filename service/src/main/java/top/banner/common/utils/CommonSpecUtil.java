package top.banner.common.utils;

import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by jdq on 2017/8/8.
 */
@Component
public class CommonSpecUtil<T> {

    /**
     * 精确匹配(equal)
     *
     * @param srcName        字段名
     * @param targetProperty 匹配内容
     * @return
     */
    public Specification<T> equal(String srcName, Object targetProperty) {
        if (targetProperty == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get(srcName), targetProperty);
    }

    /**
     * 精确匹配(notEqual)
     *
     * @param srcName        字段名
     * @param targetProperty 匹配内容
     * @return
     */
    public Specification<T> notEqual(String srcName, Object targetProperty) {
        if (targetProperty == null) {
            return null;
        }
        return (root, query, cb) -> cb.notEqual(root.get(srcName), targetProperty);
    }

    /**
     * 模糊匹配(like)
     *
     * @param srcName        字段名
     * @param targetProperty 匹配内容
     * @return
     */
    public Specification<T> like(String srcName, String targetProperty) {
        if (StringUtils.isEmpty(targetProperty)) {
            return null;
        }
        return (root, query, cb) -> cb.like(root.get(srcName), "%" + targetProperty + "%");
    }

    /**
     * 日期范围匹配(timeBetween)
     *
     * @param srcName      字段名
     * @param startTimeStr 开始时间
     * @param endTimeStr   结束时间
     * @return
     */
    public Specification<T> timeBetween(String srcName, String startTimeStr, String endTimeStr) {
        Date startTime, endTime;
        if (StringUtils.isEmpty(startTimeStr)) {
            startTime = DateTime.parse("1970-01-01").toDate();
        } else {
            startTime = DateTime.parse(startTimeStr).toDate();
        }

        if (StringUtils.isEmpty(endTimeStr)) {
            endTime = new Date();
        } else {
            endTime = DateTime.parse(endTimeStr).plusDays(1).minusSeconds(1).toDate();
        }
        return (root, query, cb) -> cb.between(root.get(srcName), startTime, endTime);
    }

    /**
     * 日期范围匹配(timeBetween)
     *
     * @param srcName   字段名
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public Specification<T> timeBetween(String srcName, Date startTime, Date endTime) {
        if (StringUtils.isEmpty(startTime)) {
            return null;
        }
        if (StringUtils.isEmpty(endTime)) {
            return null;
        }
        return (root, query, cb) -> cb.between(root.get(srcName), startTime, endTime);
    }

    /**
     * 数值范围匹配(between)
     *
     * @param srcName 字段名
     * @param start   开始
     * @param end     结束
     * @return
     */
    public Specification<T> between(String srcName, Integer start, Integer end) {
        if (StringUtils.isEmpty(start)) {
            return null;
        }
        if (StringUtils.isEmpty(end)) {
            return null;
        }
        return (root, query, cb) -> cb.between(root.get(srcName), start, end);
    }

    /**
     * 大于等于(greaterThanOrEqualTo)
     *
     * @param srcName 字段名
     * @param value   数值
     * @return
     */
    public Specification<T> greaterThanOrEqualTo(String srcName, Integer value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(srcName), value);
    }

    /**
     * 小于等于(lessThanOrEqualTo)
     *
     * @param srcName 字段名
     * @param value   数值
     * @return
     */
    public Specification<T> lessThanOrEqualTo(String srcName, Integer value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(srcName), value);
    }

    /**
     * in条件帅选(in)
     *
     * @param srcName 字段名
     * @param list    集合
     * @return
     */
    public Specification<T> in(String srcName, List<Integer> list) {
        if (list.isEmpty()) {
            return null;
        }
        return (root, query, cb) -> cb.and(root.get(srcName).in(list));
    }

    /**
     * 字段不为空(isNotNull)
     *
     * @param srcName 字段名
     * @return
     */
    public Specification<T> isNotNull(String srcName) {
        return (root, query, cb) -> cb.isNotNull(root.get(srcName));
    }

    /**
     * 字段为空(isNull)
     *
     * @param srcName 字段名
     * @return
     */
    public Specification<T> isNull(String srcName) {
        return (root, query, cb) -> cb.isNull(root.get(srcName));
    }

    /**
     * 倒序(desc)
     *
     * @param srcName 字段名
     * @return
     */
    public Specification<T> desc(String srcName) {
        return (root, query, cb) -> query.orderBy(cb.desc(root.get(srcName).as(Integer.class))).getRestriction();
    }

    /**
     * 升序(asc)
     *
     * @param srcName 字段名
     * @return
     */
    public Specification<T> asc(String srcName) {
        return (root, query, cb) -> query.orderBy(cb.asc(root.get(srcName).as(Integer.class))).getRestriction();
    }

}
