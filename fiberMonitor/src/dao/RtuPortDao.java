package dao;

import java.io.Serializable;
import java.util.List;

import domain.Rtu_ports;

public interface RtuPortDao extends BaseDao<Rtu_ports>
{
//根据RTU标识(RTU-ID)查询对应的OTDR端口数量
	int findCountByRtuId(Serializable id);

//根据RTU-ID查询该RTU上的所有OTDR端口
	List<Rtu_ports> findAllByRtuId(Serializable id);
	
//根据RTU-ID和模块号和端口序号查询OTDR端口
	Rtu_ports findByRtuIdAndModelOrderAndPortOrder(Serializable id,int modelOrder,int port_order);
	
//根据光路跳纤id查询端口
	Rtu_ports findByJumperRouteId(Serializable id);
//根据RTU id和端口序号查找切换端口
	Rtu_ports findSwitchPortByRtuIdAndOrder(Long switchRtuId, int switchRtuOrder);
//根据RTU id和端口序号查找备纤光源RTU的IN端口
	Rtu_ports findInPortByRtuIdAndOrder(Long backupRtuId, int inRtuOrder);
//根据切换RTU id查找全部端口	
	public List<Rtu_ports> findSwitchRtuPortsByRtuId(Long id);
//	根据备纤光源RTU id查找全部端口
	public List<Rtu_ports> findRtuInPortsByRtuId(Long id);
//根据模块序号查找当前模块上未使用的端口	
	public List<Rtu_ports> findUnsedRtuPortsByRtuIdAndMoudleOrder(Long id,int order);
}
