package com.funny.admin.common.dao;

import java.util.List;

import com.funny.admin.common.domain.BaseEntity;
import com.funny.admin.common.domain.PageCondition;

/**
 * Created by fangli@autohome.com.cn on 2016/6/29.
 */
public interface BaseMapper<T extends BaseEntity> {
    /**
     * 新增
     *
     * @param t
     * @return
     */
    int insert(T t);

    /**
     * 根据主键修改，为空的字段不修改
     *
     * @param t
     * @return
     */
    int updateByIdSelected(T t);

    /**
     * 根据主键修改，无值的字段将会被改成null
     *
     * @param t
     * @return
     */
    int updateById(T t);

    /**
     * 根据主键id查找
     *
     * @param id
     * @return
     */
    T findById(Long id);

    /**
     * 根据条件查找
     *
     * @param pageCondition
     * @return
     */
    List<T> findByCondition(PageCondition pageCondition);
}
