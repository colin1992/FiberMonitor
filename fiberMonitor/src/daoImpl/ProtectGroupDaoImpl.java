package daoImpl;

import java.io.Serializable;
import java.util.List;

import dao.ProtectGroupDao;
import domain.Protect_groups;

public class ProtectGroupDaoImpl extends BaseDaoImpl<Protect_groups> implements ProtectGroupDao{
//通过rtu_Id查找配对组	
	public List<Protect_groups> findProtectGroupsByRtuId(Serializable id){
		String sql = " select protect_groups from Protect_groups as protect_groups where protect_groups.rtu_master_id = ?0";
		return findMulti(sql,id);
	}
//通过routeId查找
    public Protect_groups findProtectGroupsByRouteId(Serializable id){
    	String sql=" select protect_groups from Protect_groups as protect_groups where protect_groups.uplink_off_id = ?0"+
    			   " or protect_groups.uplink_on_id = ?0"+
    			   " or protect_groups.downlink_on_id = ?0"+ 
    			   " or protect_groups.downlink_off_id = ?0";
    	return findOne(sql,id);
    }
}
