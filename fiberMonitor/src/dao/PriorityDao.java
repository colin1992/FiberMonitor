package dao;

import java.io.Serializable;
import java.util.List;


import domain.Priorities;

public interface PriorityDao extends BaseDao<Priorities>
{
	//根据Rtu-Id找到其对应的所有优先级
		List<Priorities> findAllByRtuId(Serializable id);

}
