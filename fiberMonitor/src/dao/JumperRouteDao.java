package dao;

import java.io.Serializable;
import java.util.List;
import domain.Jumper_routes;

public interface JumperRouteDao extends BaseDao<Jumper_routes>
{
//根据局站id获取当前局站的所有光路跳纤
	List<Jumper_routes> findAllByStationId(Serializable id);
	
//根据RTU-id和RTU端口序号查询光路跳纤
	Jumper_routes findByRtuIdAndPortMoudleOrder(Serializable id,int port_order,int moudleOrder);
	
//根据局站Id查询该局站下两端置空的光路跳纤
	List<Jumper_routes> findAllNullByStationId(Serializable id);
//根据局站Id查询该局站下两端置空的光路跳纤
	List<Jumper_routes> findByRouteId(Serializable id);	
}
