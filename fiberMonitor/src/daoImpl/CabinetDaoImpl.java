package daoImpl;

import java.io.Serializable;
import java.util.List;

import dao.CabinetDao;
import domain.Cabinets;

public class CabinetDaoImpl extends BaseDaoImpl<Cabinets> implements CabinetDao
{
//根据机架Id查询机柜
	public Cabinets findByRackId(Serializable id)
	{
		String sql = " select cabinet from Cabinets as cabinet where cabinet.racks.id = ?0 ";
		return findOne(sql,id);
	}

//根据局站Id查询机柜
	public List<Cabinets> findByStationId(Serializable id)
	{
		String sql = " select cabinets from Cabinets as cabinets where cabinets.station.id = ?0 ";
		return findMulti(sql,id);
	}
}
