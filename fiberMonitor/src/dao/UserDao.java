package dao;

import java.util.List;

import domain.User;

public interface UserDao extends BaseDao<User> {
//通过角色查找用户	
   public List<User> findUsersByRoleId(Long id);
//通过用户名查找用户
   public User getUserByAccount(String account);
}
