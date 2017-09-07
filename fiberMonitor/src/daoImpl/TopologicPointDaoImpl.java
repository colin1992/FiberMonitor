package daoImpl;

import java.util.List;

import dao.TopologicPointDao;
import domain.Topologic_points;

public class TopologicPointDaoImpl extends BaseDaoImpl<Topologic_points> implements TopologicPointDao
{
	public List<Topologic_points> findTopologic_pointsByRouteId(Long id){
		String sql = "select topologic_points from Topologic_points as topologic_points where topologic_points.route.id = ?0";
		return findMulti(sql,id);
	}
}
