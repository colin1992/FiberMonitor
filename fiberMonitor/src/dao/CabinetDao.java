package dao;

import java.io.Serializable;
import java.util.List;

import domain.Cabinets;

public interface CabinetDao extends BaseDao<Cabinets>
{
//根据机架Id查询机柜
	Cabinets findByRackId(Serializable id);
//根据局站Id查询机柜
	List<Cabinets> findByStationId(Serializable id);

}
