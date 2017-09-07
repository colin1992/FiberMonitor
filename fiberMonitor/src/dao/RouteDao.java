package dao;

import java.io.Serializable;
import java.util.List;
import domain.Routes;

public interface RouteDao extends BaseDao<Routes>
{
//根据光端口序号以及RTU编号查询光路
	Routes findByRtuOrderAndID(Serializable id, int port_order);
//根据切换端口序号以及切换RTU编号查询光路
	public Routes findBySwitchRtuOrderAndID(Serializable id, int port_order);
//根据Rtu Id查询所有光路
	List<Routes> findAllByRtuId(Serializable id);
	
//根据priotity_id查询所有光路
	List<Routes> findAllByPriotityId(Serializable id);	
	
}
