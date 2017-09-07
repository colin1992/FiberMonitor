package daoImpl;

import java.util.List;

import dao.TopologicRouteDao;
import domain.Topologic_routes;

public class TopologicRouteDaoImpl extends BaseDaoImpl<Topologic_routes> implements TopologicRouteDao
{
   public List<Topologic_routes> findByRouteId(Long id){
	   String sql = "select topologic_routes from Topologic_routes as topologic_routes where topologic_routes.route.id = ?0";
		return findMulti(sql,id);
   }
}
