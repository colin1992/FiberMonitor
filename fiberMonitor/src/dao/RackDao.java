package dao;

import java.io.Serializable;

import domain.Racks;

public interface RackDao extends BaseDao<Racks>
{
//根据机柜Id和机架序号查询机架
	Racks findByCabinetIdAndRackOrder(Serializable id, int rack_order);
	
//根据配线架id得到所属机架
	Racks findByFrameId(Serializable id);

}
