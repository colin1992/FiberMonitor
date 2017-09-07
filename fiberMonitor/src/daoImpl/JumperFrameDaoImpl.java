package daoImpl;

import java.io.Serializable;
import java.util.List;

import dao.JumperFrameDao;
import domain.Jumper_frames;

public class JumperFrameDaoImpl extends BaseDaoImpl<Jumper_frames> implements JumperFrameDao
{
//根据配线架Id和端口序号找配线架跳纤
	public Jumper_frames findByFrameIdandPortId(Serializable id, int port_order)
	{
		String sql = " select jumperFrame "
						+ " from Jumper_frames as jumperFrame "
						+ " where (jumperFrame.frame_a_id = ?0 and jumperFrame.port_order_a = ?1) "
						+ " or (jumperFrame.frame_z_id = ?0 and jumperFrame.port_order_z = ?1) ";
		return findOne(sql,id,port_order);
	}
	
//根据局站id获取当前局站的所有配线架跳纤
	public List<Jumper_frames> findAllByStationId(Serializable id)
	{
		String sql = " select jumperFrames"
				+ " from Jumper_frames as jumperFrames "
				+ " where jumperFrames.station.id = ?0 ";
		return findMulti(sql,id);
	}
	
//根据局站Id查询两端均置空的配线架跳纤
	public List<Jumper_frames> findAllNullByStationId(Serializable id)
	{
		String sql = " select jumperFrames from Jumper_frames as jumperFrames "
				+ " where jumperFrames.port_order_a = 0 and jumperFrames.port_order_z = 0 "
				+ " and jumperFrames.station.id = ?0 ";
		return findMulti(sql,id);
	}
//根据光路Id查询两端均置空的配线架跳纤
	public List<Jumper_frames> findByRouteId(Serializable id){
		String sql = " select jumperFrames from Jumper_frames as jumperFrames "
				+ " where jumperFrames.route_id= ?0 ";
		return findMulti(sql,id);
	}	
}
