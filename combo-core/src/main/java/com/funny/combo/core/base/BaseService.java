package com.funny.combo.core.base;



import java.util.List;

/**
 * 基础Service接口<br>
 * 处理公用的CRUD、分页等
 * 
 * @author fangli 2018-7-28
 * 
 * @param <T>
 */
public interface BaseService<T extends BaseEntity> {

	/**
	 * 保存实体
	 * 
	 * @throws Exception
	 */
	int insertSelective(T entity) throws Exception;

	/**
	 * 更新实体
	 * 
	 * @throws Exception
	 */
	int updateSelectiveById(T entity) throws Exception;

	/**
	 * 返回实体
	 * 
	 * @return
	 * @throws Exception
	 */
	T findEntityById(Long id) throws Exception;
	/**
	 * 返回实体List
	 * 
	 * @return
	 * @throws Exception
	 */
	List<T> listByCondition(T entity) throws Exception;

	int count(T entity) throws Exception;
}
