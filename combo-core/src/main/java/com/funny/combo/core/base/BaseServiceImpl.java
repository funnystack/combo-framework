package com.funny.combo.core.base;


import java.util.List;

/**
 * 基础Service接口实现<br>
 * 处理公用的CRUD、分页等
 *
 * @author fangli 2014-7-28
 */
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {


    protected abstract BaseMapper<T> baseMapper();

    /**
     * 保存实体
     *
     * @throws Exception
     */
    @Override
    public int insertSelective(T entity) throws Exception {
        return baseMapper().insertSelective(entity);
    }


    /**
     * 更新实体(根据主键ID)
     *
     * @throws Exception
     */
    @Override
    public int updateSelectiveById(T entity) throws Exception {
        return baseMapper().updateSelectiveById(entity);
    }

    /**
     * 返回实体
     *
     * @return
     * @throws Exception
     */
    @Override
    public T findEntityById(Long id) throws Exception {
        return baseMapper().findEntityById(id);
    }

    /**
     * 返回实体List
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<T> listByCondition(T entity) throws Exception {
        return baseMapper().listByCondition(entity);
    }

    @Override
    public int count(T entity) throws Exception {
        return baseMapper().count(entity);
    }

}
