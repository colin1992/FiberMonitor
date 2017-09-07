package dao;

import java.io.Serializable;
import java.util.List;

import domain.Protect_groups;
public interface ProtectGroupDao  extends BaseDao<Protect_groups> {
	//通过rtu_Id查找配对组	
     public List<Protect_groups> findProtectGroupsByRtuId(Serializable id);
     
    //通过routeId查找
     public Protect_groups findProtectGroupsByRouteId(Serializable id);
}
