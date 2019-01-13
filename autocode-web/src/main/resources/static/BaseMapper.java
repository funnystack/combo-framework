package cn.com.autohome.mall.base;




import java.util.List;

public interface BaseMapper<T extends BaseEntity> {
    /**
     * 新增对象
     *
     * @param entity 对象实例
     * @return 受影响行数
     */
    int insert(T entity);

    /**
     * 更新对象<为空的字段不更新,需要字段更新为空的需要自写sql>
     *
     * @param entity 对象实例
     * @return 受影响行数
     */
    int updateById(T entity);

    /**
     * 根据主键ID获取对象
     *
     * @param id 对象ID
     * @return 对象实例
     */
    T findById(Long id);

    /**
     * 根据查询条件返回条数
     *
     * @param entity 查询条件
     * @return 数量
     */
    List<T> findList(T entity);

    /**
     * 根据查询条件返回列表
     *
     * @param entity 查询条件
     * @return List<对象>
     */
    int count(T entity);
}