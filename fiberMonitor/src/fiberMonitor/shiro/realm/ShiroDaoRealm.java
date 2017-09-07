package fiberMonitor.shiro.realm;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import domain.Permissions;
import domain.User;
import service.FindService;
public class ShiroDaoRealm extends AuthorizingRealm{
	private FindService findServiceImpl;
    public void setFindService(FindService findServiceImpl){
    	this.findServiceImpl=findServiceImpl;
    }
   /**用于鉴定权限**/
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		//获取当前登录的用户名
		String account = (String) super.getAvailablePrincipal(principals);
		//用户的角色
		List<String> roles=new ArrayList<String>();  
		//角色所拥有的权限
		List<String> permissions = new ArrayList<String>();
		User user = findServiceImpl.findUserByAccount(account);
		if(user != null){
			if (user.getRole() != null) {
				roles.add(user.getRole().getName());
				 Subject currentUser = SecurityUtils.getSubject();//获取当前用户
				 currentUser.getSession().setTimeout(28800000); //设置登录有效时间为八小时
				if (user.getRole().getPmss()!= null &&user.getRole().getPmss().size() > 0) {
						for (Permissions pmss : user.getRole().getPmss()) {
							if(!StringUtils.isEmpty(pmss.getPermission())){
								permissions.add(pmss.getPermission());
							}
					}
				}
			}
		}else{
			throw new AuthorizationException();
		}
		//给当前用户设置角色
		info.addRoles(roles);
		//给当前用户设置权限
        info.addStringPermissions(permissions); 
		return info;
		
	}

	/**
	 *  认证回调函数,登录时调用.
	 *  获取用户信息
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		/**通过表单获取的用户信息**/
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		User user=findServiceImpl.findUserByAccount(token.getUsername());
		
		if (user!=null) {
			System.out.println(ShiroSecurityHelper.getSessionByUsername(user.getAccount()));
			return new SimpleAuthenticationInfo(user.getAccount(),user.getPassword()," ");
		} else {
			return null;
		}
	}	
}