package dao;

import java.util.List;

import domain.Topologic_routes;

public interface TopologicRouteDao extends BaseDao<Topologic_routes>
{
	public List<Topologic_routes> findByRouteId(Long id);
}
