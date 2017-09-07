package daoImpl;

import java.io.Serializable;
import java.util.List;

import dao.LandmarkDao;
import domain.Landmarks;

public class LandmarkDaoImpl extends BaseDaoImpl<Landmarks> implements LandmarkDao
{
//根据光缆id获取该光缆的全部光缆地标
	public List<Landmarks> findAllByOpticalCableId(Serializable id)
	{
		String sql = " select landmarks "
					+ " from Landmarks as landmarks "
					+ " where landmarks.optical_cable.id = ?0";
		return findMulti(sql,id);
	}
}
