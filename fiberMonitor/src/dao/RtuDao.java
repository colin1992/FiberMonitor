package dao;

import java.io.Serializable;
import java.util.List;

import domain.Rtus;

public interface RtuDao  extends BaseDao<Rtus>
{
//根据局站Id查询RTU
	List<Rtus> findAllByStationId(Serializable id);
}
