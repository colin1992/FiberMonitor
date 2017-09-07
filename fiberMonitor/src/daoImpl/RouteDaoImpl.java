package daoImpl;

import java.io.Serializable;
import java.util.List;
import dao.RouteDao;
import domain.Routes;

public class RouteDaoImpl extends BaseDaoImpl<Routes> implements RouteDao
{
//根据光端口序号以及RTU编号查询光路
	public Routes findByRtuOrderAndID(Serializable id, int port_order)
	{
		String sql = " select route from Routes as route "
				+ " where route.rtu_id = ?0 and route.rtu_port_order = ?1 ";
		return findOne(sql,id,port_order);
	}
//根据切换端口序号以及切换RTU编号查询光路
	public Routes findBySwitchRtuOrderAndID(Serializable id, int port_order)
	{
		String sql = " select route from Routes as route "
				+ " where route.switch_rtu_id = ?0 and route.switch_rtu_order = ?1 ";
		return findOne(sql,id,port_order);
	}		
//根据Rtu Id查询所有光路
	public List<Routes> findAllByRtuId(Serializable id)
	{
		String sql = " select routes "
				+ " from Routes as routes "
				+ " where routes.rtu_id = ?0";
		return findMulti(sql,id);
	}
	
//根据priotity_id查询所有光路
	public	List<Routes> findAllByPriotityId(Serializable id){
		String sql = " select routes "
				+ " from Routes as routes "
				+ " where routes.priotity_id = ?0";
		return findMulti(sql,id);
	}
	
}
