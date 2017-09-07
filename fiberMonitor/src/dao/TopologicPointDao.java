package dao;

import java.util.List;

import domain.Topologic_points;

public interface TopologicPointDao extends BaseDao<Topologic_points>
{
	List<Topologic_points> findTopologic_pointsByRouteId(Long id);
}
