package dao;

import java.io.Serializable;
import java.util.List;

import domain.Frame_ports;

public interface FramePortDao extends BaseDao<Frame_ports>
{
//根据配线架ID查询该配线架上的所有端口
	List<Frame_ports> findAllByFrameId(Serializable id);
	
//根据配线架ID和端口序号查询该配线架上的端口
	Frame_ports findByFrameIdAndPortId(Serializable id,int port_order);
	
//根据配线架端口连接物类型:配线架跳纤和连接物Id查询配线架端口
	List<Frame_ports> findByJumperFrameAndConnectionId(Serializable connectionId);
	
//根据配线架端口连接物类型:光路跳纤和连接物Id查询配线架端口
	Frame_ports findByJumperRouteAndConnectionId(Serializable connectionId);
	
//根据配线架端口连接物类型：纤芯和连接物Id查询配线架端口
   Frame_ports findByFrameIdAndPortOrder(Long frameId,int portOrder);
}
