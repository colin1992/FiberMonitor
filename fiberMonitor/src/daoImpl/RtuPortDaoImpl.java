package daoImpl;

import java.io.Serializable;
import java.util.List;

import dao.RtuPortDao;
import domain.Rtu_ports;

public class RtuPortDaoImpl extends BaseDaoImpl<Rtu_ports> implements RtuPortDao
{
//根据RTU标识(RTU-ID)查询对应的OTDR端口数量
	public int findCountByRtuId(Serializable id)
	{
		String sql = " select count(*) "
					+ " from Rtu_ports as rtu_port "
					+ " where rtu_port.port_type = 'OTDR_PORT' "
					+ " and rtu_port.rtu.id = ?0";
		return countAmounts(sql,id);
	}
	
//根据RTU-ID查询该RTU上的所有OTDR端口
	public List<Rtu_ports> findAllByRtuId(Serializable id)
	{
		String sql = " select rtuPorts from Rtu_ports as rtuPorts "
				+ " where rtuPorts.port_type = 'OTDR_PORT' "
				+ " and rtuPorts.rtu.id = ?0 ";
		return findMulti(sql,id);
	}
	
//根据RTU-ID和模块号和端口序号查询OTDR端口
	public Rtu_ports findByRtuIdAndModelOrderAndPortOrder(Serializable id,int modelOrder,int port_order)
	{
		String sql = " select rtuPort from Rtu_ports as rtuPort "
				+ " where rtuPort.port_type = 'OTDR_PORT' "
				+ " and rtuPort.rtu.id = ?0 "
				+ " and rtuPort.module_order = ?1"
				+ " and rtuPort.port_order = ?2";
		return findOne(sql,id,modelOrder,port_order);
	}
	
//根据光路跳纤id查询端口
	public Rtu_ports findByJumperRouteId(Serializable id)
	{
		String sql = " select rtuPort from Rtu_ports as rtuPort "
				+ " where rtuPort.port_type = 'OTDR_PORT' and rtuPort.jumper_route_id = ?0 ";
		return findOne(sql,id);
	}
//根据RTU id和端口序号查找切换端口
	public Rtu_ports findSwitchPortByRtuIdAndOrder(Long id,int portOrder)
	{
		String sql = "select rtuPort from Rtu_ports as rtuPort "
				+ " where rtuPort.port_type = 'SWITCH_PORT' "
				+ "and rtuPort.rtu.id = ?0 "+
				"and rtuPort.port_order=?1";
		return findOne(sql,id,portOrder);
	}
//根据切换RTU id查找全部端口
	public List<Rtu_ports> findSwitchRtuPortsByRtuId(Long id)
	{
		String sql = "select rtuPort from Rtu_ports as rtuPort  where  rtuPort.rtu.id = ?0 ";
		return findMulti(sql,id);
	}
//根据RTU id和端口序号查找备纤光源RTU的IN端口
	public Rtu_ports findInPortByRtuIdAndOrder(Long id, int inRtuOrder){
		String sql = "select rtuPort from Rtu_ports as rtuPort "
				+ " where rtuPort.port_type = 'IN_PORT' "
				+ "and rtuPort.rtu.id = ?0 "+
				"and rtuPort.port_order=?1";
		return findOne(sql,id,inRtuOrder);
	}	
//	根据备纤光源RTU id查找全部端口
	public List<Rtu_ports> findRtuInPortsByRtuId(Long id)
	{
		String sql = "select rtuPort from Rtu_ports as rtuPort  where  rtuPort.rtu.id = ?0 ";
		return findMulti(sql,id);
	}
//根据模块序号查找当前模块上未使用的端口	
	public List<Rtu_ports> findUnsedRtuPortsByRtuIdAndMoudleOrder(Long id,int order)
	{
		String sql = " select rtuPort from Rtu_ports as rtuPort "
				+ " where rtuPort.port_type = 'OTDR_PORT' "
				+ " and rtuPort.rtu.id = ?0 "
				+ " and rtuPort.module_order = ?1"
				+ " and rtuPort.status =false";
		return findMulti(sql,id,order);
	}
}
