package daoImpl;

import java.util.List;

import dao.UserDao;
import domain.User;

public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {
	//通过角色查找用户	
	   public List<User> findUsersByRoleId(Long id){
		   String sql=" select user from User as user where user.role.id = ?0 ";
		   return findMulti(sql,id);
	   }
	 //通过用户名查找用户
	   public User getUserByAccount(String account){
		   String sql="select user from User as user where user.account = ?0 ";
		   return findOne(sql,account);
	   }
}
