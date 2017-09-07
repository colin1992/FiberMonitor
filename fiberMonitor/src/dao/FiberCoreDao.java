package dao;

import java.io.Serializable;
import java.util.List;

import domain.Fiber_cores;

public interface FiberCoreDao extends BaseDao<Fiber_cores>
{
//根据配线架ID以及配线架端口序号Order查询光纤
	Fiber_cores findByFrameIdandPortOrder(Serializable id, int port_order);
	
//根据局站ID(Stations-ID)查询光纤
	List<Fiber_cores> findAllByStationId(Serializable id);
	
//根据光缆Id查询所有光纤
	List<Fiber_cores> findAllByOpticalCableId(Serializable id);
//根据光路id	
	public List<Fiber_cores> findAllByRouteId(Serializable id);
}
