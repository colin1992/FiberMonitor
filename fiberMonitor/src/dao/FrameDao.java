package dao;

import java.io.Serializable;
import java.util.List;

import domain.Distributing_frames;

public interface FrameDao extends BaseDao<Distributing_frames> 
{
//根据局站Id获取该局站下的配线架
	List<Distributing_frames> findAllByStationId(Serializable id);
	
}
