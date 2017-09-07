package dao;

import java.io.Serializable;
import java.util.List;

import domain.Preparatory_routes;

public interface PreparatoryRouteDao extends BaseDao<Preparatory_routes>
{
//根据起始局站ID和终止局站ID查询备选状态的预备光路
	List<Preparatory_routes> findByStationIds(Serializable startId,Serializable endId);
	
//根据RTU-id和端口序号查询预备光路
	Preparatory_routes findByRtuIdAndPortOrder(Serializable id, int port_order);
	
//根据配线架id和端口序号查询预备光路
	Preparatory_routes findByStartFrameIdAndPortOrder(Serializable id,int port_order);
	
//根据起始配线架id和端口序号查询预备光路
	Preparatory_routes findByEndFrameIdAndPortOrder(Serializable id,int port_order);
	
//根据光路跳纤Id查询预备光路
	Preparatory_routes findByJumperRouteId(Serializable id);
	
//根据光纤Id查询预备光路
	Preparatory_routes findByFiberCoreId(Serializable id);
	
}
