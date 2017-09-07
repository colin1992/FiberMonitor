package daoImpl;

import java.io.Serializable;
import java.util.List;

import dao.FiberCoreDao;
import domain.Fiber_cores;

public class FiberCoreDaoImpl extends BaseDaoImpl<Fiber_cores> implements FiberCoreDao
{
//根据配线架ID以及配线架端口序号Order查询光纤
	public Fiber_cores findByFrameIdandPortOrder(Serializable id, int port_order)
	{
		String sql = " select fiberCore from Fiber_cores as fiberCore "
						+ "	where (fiberCore.frame_a_id = ?0 and fiberCore.port_order_a = ?1) "
						+ " or (fiberCore.frame_z_id = ?0 and fiberCore.port_order_z = ?1)";
		return findOne(sql,id,port_order);
	}
	
//根据局站ID(Stations-ID)查询光纤
	public List<Fiber_cores> findAllByStationId(Serializable id)
	{
		String sql = " select fiberCores from Fiber_cores as fiberCores"
				+ " where fiberCores.station_a_id = ?0 "
				+ " or fiberCores.station_z_id = ?0 ";
		return findMulti(sql,id);
	}
	
//根据光缆Id查询所有光纤
	public List<Fiber_cores> findAllByOpticalCableId(Serializable id)
	{
		String sql = " select fiberCores from Fiber_cores as fiberCores "
					+ " where fiberCores.optical_cable.id = ?0 ";
		return findMulti(sql,id);
	}
//根据光路id获取光纤	
	public List<Fiber_cores> findAllByRouteId(Serializable id)
	{
		String sql = " select fiberCores from Fiber_cores as fiberCores "
					+ " where fiberCores.route_id = ?0 ";
		return findMulti(sql,id);
	}
}
