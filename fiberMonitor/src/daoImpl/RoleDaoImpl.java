package daoImpl;

import dao.RoleDao;
import domain.Role;

public class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDao{
	 public Role getByName(String role){
		   String sql = " select role from Role as role where role.name = ?0 ";
		return findOne(sql,role);
	  }
}
