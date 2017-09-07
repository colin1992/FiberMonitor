package daoImpl;

import java.io.Serializable;
import java.util.List;

import dao.FramePortDao;
import domain.Frame_ports;

public class FramePortDaoImpl extends BaseDaoImpl<Frame_ports> implements FramePortDao
{
//根据配线架ID查询该配线架上的所有端口
	public List<Frame_ports> findAllByFrameId(Serializable id)
	{
		String sql = " select framePorts from Frame_ports as framePorts "
				+ " where framePorts.frame.id = ?0 ";
		return findMulti(sql,id);
	}
	
//根据配线架ID和端口序号查询该配线架上的端口
	public Frame_ports findByFrameIdAndPortId(Serializable id,int port_order)
	{
		String sql = " select framePort from Frame_ports as framePort "
				+ " where framePort.frame.id = ?0 "
				+ " and framePort.port_order = ?1";
		return findOne(sql,id,port_order);
	}

//根据配线架端口连接物类型:配线架跳纤和连接物Id查询配线架端口
	public List<Frame_ports> findByJumperFrameAndConnectionId(Serializable connectionId)
	{
		String sql = " select framePort from Frame_ports as framePort "
				+ " where framePort.connection_type = '配线架跳纤' "
				+ " and framePort.connection_id = ?0 ";
		return findMulti(sql,connectionId);
	}
	
//根据配线架端口连接物类型:光路跳纤和连接物Id查询配线架端口
	public Frame_ports findByJumperRouteAndConnectionId(Serializable connectionId)
	{
		String sql = " select framePort from Frame_ports as framePort "
				+ " where framePort.connection_type = '光路跳纤' "
				+ " and framePort.connection_id = ?0 ";
		return findOne(sql,connectionId);
	}
	
//根据配线架端口连接物类型：纤芯和连接物Id查询配线架端口
	public Frame_ports findByFrameIdAndPortOrder(Long frameId,int portOrder)
	{
		String sql = " select frame_ports from Frame_ports as frame_ports "
				+ " where frame_ports.frame.id = "+frameId
				+ " and frame_ports.port_order = "+portOrder;
		return findOne(sql);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
