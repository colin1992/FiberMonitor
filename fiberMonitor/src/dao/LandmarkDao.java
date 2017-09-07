package dao;

import java.io.Serializable;
import java.util.List;

import domain.Landmarks;

public interface LandmarkDao extends BaseDao<Landmarks>
{
//根据光缆id获取该光缆的全部光缆地标
	List<Landmarks> findAllByOpticalCableId(Serializable id);
}
