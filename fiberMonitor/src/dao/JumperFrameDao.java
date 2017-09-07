package dao;

import java.io.Serializable;
import java.util.List;

import domain.Jumper_frames;

public interface JumperFrameDao extends BaseDao<Jumper_frames>
{
//根据配线架Id和端口序号找配线架跳纤
	Jumper_frames findByFrameIdandPortId(Serializable id, int port_order);
//根据局站id获取当前局站的所有配线架跳纤
	List<Jumper_frames> findAllByStationId(Serializable id);
	
//根据局站Id查询两端均置空的配线架跳纤
	List<Jumper_frames> findAllNullByStationId(Serializable id);
//根据光路Id查询两端均置空的配线架跳纤
	List<Jumper_frames> findByRouteId(Serializable id);
}
