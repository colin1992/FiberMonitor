package dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseDao<T> 
{
//保存实体
	 Serializable addEntity(T entity);
//根据ID删除实体
	 void deleteEntity(Class<T> entityClass, Serializable id);
//修改实体
	 void alterEntity(T entity);
//根据实体ID查询实体
	 T findEntity(Class<T> entityClass, Serializable id);
//查询所有实体
	 List<T> findAllEntities(Class<T> entityClass);
//获取实体记录数量
	 long countAmounts(Class<T> entityClass);
//根据条件获取实体记录数量
	 int countAmounts(String sql,Object...parameters);
//多条件查询实体
	 List<T> findEntities(Class<T> entityClass, Map<String,Object> para)
			throws InstantiationException, IllegalAccessException;	
//根据指定字段更新实体
	 void updateEntity(Class<T> entityClass, Serializable id,Map<String , Object> parameterMap);
}
