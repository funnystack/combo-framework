package cn.com.autohome.mall.dao;

import java.util.List;

import cn.com.autohome.mall.po.BaseEntity;

/**
 * Define BaseMapper.
 * <p>Created with IntelliJ IDEA on 3/14/2016 3:13 PM.</p>
 *
 * @author fangli@autohome.com.cn
 * @version 1.0
 */
public interface BaseMapper<Entity extends BaseEntity, ID> {

    /**
     * 有选择性的新增对象.
     *
     * @param entity 对象实例.
     * @return 受影响行数.
     */
    int insert(Entity entity);

    /**
     * 根据ID删除对象.
     *
     * @param id 对象ID.
     * @return 受影响行数.
     */
    int deleteById(ID id);

    /**
     * 有选择性的更新对象.
     *
     * @param entity 对象实例.
     * @return 受影响行数.
     */
    int updateById(Entity entity);
    /**
     * 有选择性的更新对象.
     *
     * @param entity 对象实例.
     * @return 受影响行数.
     */
    int updateByIdSelected(Entity entity);

    /**
     * 根据ID获取对象.
     *
     * @param id 对象ID.
     * @return 对象实例.
     */
    Entity findById(ID id);

    /**
     * 获取所有对象.
     *
     * @return 对象集合.
     */
    List<Entity> findAll();
}
