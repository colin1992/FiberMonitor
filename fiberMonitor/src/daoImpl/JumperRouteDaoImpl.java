package daoImpl;

import java.io.Serializable;
import java.util.List;

import dao.JumperRouteDao;
import domain.Jumper_routes;

public class JumperRouteDaoImpl extends BaseDaoImpl<Jumper_routes> implements JumperRouteDao
{
//根据局站id获取当前局站的所有光路跳纤
	public List<Jumper_routes> findAllByStationId(Serializable id)
	{
		String sql = " select jumperRoutes"
				+ " from Jumper_routes as jumperRoutes "
				+ " where jumperRoutes.station.id = ?0 ";
		return findMulti(sql,id);
	}
	
//根据RTU-id和RTU端口序号以及模块序号查询光路跳纤
	public Jumper_routes findByRtuIdAndPortMoudleOrder(Serializable id,int port_order,int moudleOrder)
	{
		String sql = "select jumperRoute from Jumper_routes as jumperRoute "
				+ " where jumperRoute.rtu_id = ?0 and jumperRoute.otdr_port_order = ?1 and jumperRoute.model_order=?2";
		return findOne(sql,id,port_order,moudleOrder);
	}
	
//根据局站Id查询该局站下两端置空的光路跳纤
	public List<Jumper_routes> findAllNullByStationId(Serializable id)
	{
		String sql = " select jumperRoutes from Jumper_routes as jumperRoutes "
				+ " where jumperRoutes.otdr_port_order = 0 and jumperRoutes.frame_port_order = 0 "
				+ " and jumperRoutes.station.id = ?0 ";
		return findMulti(sql,id);
	}
//根据局站Id查询该局站下两端置空的光路跳纤
	public List<Jumper_routes> findByRouteId(Serializable id){
		String sql = " select jumperRoutes from Jumper_routes as jumperRoutes "
				+ " where jumperRoutes.route_id = ?0";
		return findMulti(sql,id);
	}	
}
