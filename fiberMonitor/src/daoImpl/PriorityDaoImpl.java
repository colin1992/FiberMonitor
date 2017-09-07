package daoImpl;

import java.io.Serializable;
import java.util.List;
import dao.PriorityDao;
import domain.Priorities;


public class PriorityDaoImpl extends BaseDaoImpl<Priorities> implements PriorityDao
{
//根据Rtu-Id找到其对应的所有优先级
	public List<Priorities> findAllByRtuId(Serializable id)
	{
		String sql = " select priorities from Priorities as priorities where priorities.rtu_id = ?0";
		return findMulti(sql,id);
	}

}