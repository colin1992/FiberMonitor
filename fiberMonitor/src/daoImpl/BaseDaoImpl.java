package daoImpl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import dao.BaseDao;

public class BaseDaoImpl<T> implements BaseDao<T>
{
	private SessionFactory sessionFactory;
	//注入sessionFactory
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}
	public SessionFactory getSessionFactory()
	{
		return this.sessionFactory;
	}

/**CRUD操作**/
	
//增加实体
	public Serializable addEntity(T entity)
	{
		return getSessionFactory().getCurrentSession().save(entity);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
	}
	
//根据Id删除实体
	@SuppressWarnings("unchecked")
	public void deleteEntity(Class<T> entityClass, Serializable id)
	{
		T entityT = (T) getSessionFactory().getCurrentSession().load(entityClass, id);
		getSessionFactory().getCurrentSession().delete(entityT);
	}
	
//修改实体
	public void alterEntity(T entity)
	{
		getSessionFactory().getCurrentSession().update(entity);
	}
	
//根据实体ID查询实体
	@SuppressWarnings("unchecked")
	public T findEntity(Class<T> entityClass, Serializable id)
	{
		return (T)getSessionFactory().getCurrentSession().get(entityClass, id);
	}

//查询所有实体
	@SuppressWarnings("unchecked")
	public List<T> findAllEntities(Class<T> entityClass)
	{
		String sql = " select entities from "+entityClass.getSimpleName()+" as entities";
		return (List<T>)getSessionFactory().getCurrentSession().createQuery(sql).list();
	}
	
//获取实体记录数量
	public long countAmounts(Class<T> entityClass)
	{
		String sql = " select count(*) from " + entityClass.getSimpleName();
		List<?> number = getSessionFactory().getCurrentSession().createQuery(sql).list();
		if(null != number && 1 == number.size())
		{
			return (long)number.get(0);
		}
		return 0;
	}
	
//根据条件获取实体记录数量
	public int countAmounts(String sql,Object...parameters)
	{
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		for(int i = 0; i < parameters.length; i++)
		{
			query.setParameter(String.valueOf(i), parameters[i]);
		}
		List<?> number = query.list();
		if(null != number && 1 == number.size())
		{
			return (int) number.get(0);
		}
		return 0;
	}
	
//多条件查询实体
	@SuppressWarnings("unchecked")
	public List<T> findEntities(Class<T> entityClass, Map<String,Object> para) 
			throws InstantiationException, IllegalAccessException
	{
		List<T> entitiesT = null;
		
		StringBuffer sql = new StringBuffer(" select entityT from "+ entityClass.getName() +" as entityT ");
		if(para.size() != 0)
		{
			sql.append(" where entityT.");
			Set<Entry<String,Object>> set = para.entrySet();
			Iterator<Entry<String,Object>> iterator = set.iterator();
			boolean firstPara = true;//第一个参数不用加and
			while(iterator.hasNext())
			{
				Map.Entry<String, Object> mapEntry = (Entry<String,Object>) iterator.next();
				if( firstPara )//如果是第一个查询参数
				{
					if( (mapEntry.getValue() instanceof String) || (mapEntry.getValue() instanceof Character))//模糊查询
						sql.append(mapEntry.getKey()+" like "+"\'%"+mapEntry.getValue()+"%\'");
					else			//精确查询
						sql.append(mapEntry.getKey()+" = "+mapEntry.getValue());
					firstPara = false;
				}
				else
					if( (mapEntry.getValue() instanceof String) || (mapEntry.getValue() instanceof Character))//模糊查询
						sql.append(" or entityT."+mapEntry.getKey()+" like "+"\'%"+mapEntry.getValue()+"%\'");
					else											//精确查询
						sql.append(" or entityT."+mapEntry.getKey()+" = "+mapEntry.getValue());
			}
		}
		entitiesT = (List<T>) getSessionFactory().getCurrentSession().createQuery(sql.toString()).list();
		return entitiesT;
	}
	
//根据指定字段更新实体
	public void updateEntity(Class<T> entityClass, Serializable id, Map<String , Object> parameterMap)
	{

		StringBuffer sql = new StringBuffer(" update "+entityClass.getSimpleName()+" as entity set ");
		if(parameterMap.size() != 0)
		{
			Set<Entry<String,Object>> set = parameterMap.entrySet();
			Iterator<Entry<String, Object>> iterator = set.iterator();
			boolean firstPara = true;
			int i = 0;
			while(iterator.hasNext())
			{
				
				Map.Entry<String, Object> mapEntry = (Entry<String,Object>) iterator.next();
				if( firstPara )//如果是第一个查询参数
				{
					sql.append(" entity."+mapEntry.getKey()+" = "+"?"+String.valueOf(i));
					firstPara = false;
				}
				else		
					sql.append(" , entity."+mapEntry.getKey()+" = "+"?"+String.valueOf(i));
				i++;
			}
			sql.append(" where entity.id = "+"?"+String.valueOf(parameterMap.size()));
			Query query = getSessionFactory().getCurrentSession().createQuery(sql.toString());
			
			Iterator<Entry<String, Object>> iteratorParameter = set.iterator();
			int j = 0;
			while(iteratorParameter.hasNext())
			{
				Map.Entry<String, Object> mapEntryParameter = (Entry<String,Object>) iteratorParameter.next();
				query.setParameter(String.valueOf(j), mapEntryParameter.getValue());
				j++;
			}
			query.setParameter(String.valueOf(j), id);
			query.executeUpdate();
		}
	}
	
//根据HQL语句查询实体
	@SuppressWarnings("unchecked")
	protected List<T> find(String sql)
	{
		return (List<T>)getSessionFactory().getCurrentSession().createQuery(sql).list();
	}

//带参数HQL语句查询多个实体
	@SuppressWarnings("unchecked")
	protected List<T> findMulti(String sql,Object...parameters)
	{
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		for(int i = 0; i < parameters.length; i++)
		{
			query.setParameter(String.valueOf(i), parameters[i]);
		}
		return (List<T>)query.list();
	}
/**
 * 分页查询
 * @param sql 查询语句
 * @param page 当前页码
 * @param perCount 每一页显示的条目数
***/
	@SuppressWarnings("unchecked")
	protected List<T> findPagination(String sql,int page,int perCount )
	{
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		int firstRow=perCount*(page-1);
		//当前页的起始位置
		query.setFirstResult(firstRow);
		//读取的最大个数，将个数设置为每页的显示个数
		query.setMaxResults(perCount);
	    return (List<T>)query.list();
	}
//带参数HQL语句查询单个实体
	@SuppressWarnings("unchecked")
	protected T findOne(String sql,Object...parameters)
	{
		Query query = getSessionFactory().getCurrentSession().createQuery(sql);
		for(int i = 0; i < parameters.length; i++)
		{
			query.setParameter(String.valueOf(i), parameters[i]);
		}
		return (T)query.uniqueResult();
	}
	
}
