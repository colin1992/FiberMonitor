package daoImpl;

import java.io.Serializable;
import java.util.List;

import dao.FrameDao;
import domain.Distributing_frames;

public class FrameDaoImpl extends BaseDaoImpl<Distributing_frames> implements FrameDao
{
//根据局站Id获取该局站下的配线架
	public List<Distributing_frames> findAllByStationId(Serializable id)
	{
		String sql = " select distributingFrames "
				+ " from Distributing_frames as distributingFrames "
				+ " where distributingFrames.station.id = ?0";
		return findMulti(sql,id);
	}
}
