package daoImpl;

import java.io.Serializable;
import java.util.List;

import dao.PreparatoryRouteDao;
import domain.Preparatory_routes;

public class PreparatoryRouteDaoImpl extends BaseDaoImpl<Preparatory_routes> implements PreparatoryRouteDao
{
//根据起始局站ID和终止局站ID查询备选状态的预备光路
	public List<Preparatory_routes> findByStationIds(Serializable startId,Serializable endId)
	{
		String sql= " select preparatoryRoutes "
					+ " from Preparatory_routes as preparatoryRoutes "
					+ " where preparatoryRoutes.station_a_id = ?0 "
					+ " and preparatoryRoutes.station_z_id = ?1 "
					+ " and preparatoryRoutes.status = false ";
		
		return findMulti(sql,startId,endId);
	}
	
//根据RTU-id和端口序号查询预备光路
	public Preparatory_routes findByRtuIdAndPortOrder(Serializable id, int port_order)
	{
		String sql = " select preparatoryRoute "
				+ " from Preparatory_routes as preparatoryRoute "
				+ " where preparatoryRoute.rtu_id = ?0 "
				+ " and preparatoryRoute.rtu_port_order = ?1 ";
		return findOne(sql,id,port_order);
	}
	
//根据起始配线架id和端口序号查询预备光路
	public Preparatory_routes findByStartFrameIdAndPortOrder(Serializable id,int port_order)
	{
		String sql = " select preparatoryRoute "
				+ " from Preparatory_routes as preparatoryRoute "
				+ " where preparatoryRoute.frame_a_id = ?0 "
				+ " and preparatoryRoute.frame_a_port_order = ?1 ";
		return findOne(sql,id,port_order);
	}
	
//根据起始配线架id和端口序号查询预备光路
	public Preparatory_routes findByEndFrameIdAndPortOrder(Serializable id,int port_order)
	{
		String sql = " select preparatoryRoute "
				+ " from Preparatory_routes as preparatoryRoute "
				+ " where preparatoryRoute.frame_z_id = ?0 "
				+ " and preparatoryRoute.frame_z_port_order = ?1 ";
		return findOne(sql,id,port_order);
	}
	
//根据光路跳纤Id查询预备光路
	public Preparatory_routes findByJumperRouteId(Serializable id)
	{
		String sql = " select preparatoryRoute "
				+ " from Preparatory_routes as preparatoryRoute "
				+ " where preparatoryRoute.jumper_route.id = ?0 ";
		return findOne(sql,id);
	}
	
//根据光纤Id查询预备光路
	public Preparatory_routes findByFiberCoreId(Serializable id)
	{
		String sql = " select preparatoryRoute "
				+ " from Preparatory_routes as preparatoryRoute "
				+ " where preparatoryRoute.fiber_core_id = ?0 ";
		return findOne(sql,id);
	}

}
