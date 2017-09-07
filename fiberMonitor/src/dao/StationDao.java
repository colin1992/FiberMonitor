package dao;

import java.io.Serializable;
import java.util.List;

import domain.Stations;

public interface StationDao extends BaseDao<Stations>
{
//根据区域(Areas)ID查询局站
	List<Stations> findAllByAreaId(Serializable id);
}
