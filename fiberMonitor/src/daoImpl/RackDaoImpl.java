package daoImpl;

import java.io.Serializable;

import dao.RackDao;
import domain.Racks;

public class RackDaoImpl extends BaseDaoImpl<Racks> implements RackDao
{
//根据机柜Id和机架序号查询机架
	public Racks findByCabinetIdAndRackOrder(Serializable id, int rack_order)
	{
		String sql = " select rack from Racks as rack where rack.cabinet.id = ?0 and rack.rack_order = ?1 ";
		return findOne(sql,id,rack_order);
	}
	
//根据配线架id得到所属机架
	public Racks findByFrameId(Serializable id)
	{
		String sql = " select rack from Racks as rack where rack.thing_type = '配线架' "
				+ " and rack.thing_id = ?0 ";
		return findOne(sql,id);
	}

}
