package daoImpl;

import java.io.Serializable;
import java.util.List;

import dao.RtuDao;
import domain.Rtus;

public class RtuDaoImpl extends BaseDaoImpl<Rtus> implements RtuDao
{
//根据局站Id查询RTU
	public List<Rtus> findAllByStationId(Serializable id)
	{
		String sql = " select rtus from Rtus as rtus where rtus.station.id = ?0 ";
		return findMulti(sql,id);
	}
}
