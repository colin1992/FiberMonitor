package dao;

import domain.Role;

public interface RoleDao extends BaseDao<Role> {
   public Role getByName(String role);
}
