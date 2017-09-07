package daoImpl;

import java.io.Serializable;
import java.util.List;

import dao.StationDao;
import domain.Stations;

public class StationDaoImpl extends BaseDaoImpl<Stations> 
		implements StationDao
{
//根据区域(Areas)ID查询局站
	public List<Stations> findAllByAreaId(Serializable id)
	{
		String sql = " select stations from Stations as stations where stations.area.id = ?0 ";
		return findMulti(sql,id);
	}
}
