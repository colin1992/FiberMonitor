package fiberMonitor.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import domain.Permissions;
import domain.Role;
import domain.SerialCode;
import domain.User;
import domain.Verify_codes;
import fiberMonitor.bean.MessageUtil;
import fiberMonitor.bean.NumConv;
import net.sf.json.JSONArray;
import service.AddService;
import service.AlterService;
import service.DeleteService;
import service.FindService;

@Controller
/**
 * 控制器
 * 主要负责用户、角色等与权限相关的操作
 * **/
public class ShiroController {
	@Resource(name="findService")
	private FindService findService;
    @Resource(name="addService") 
	private  AddService addService;
    @Resource(name="alterService") 
   	private   AlterService alterService;
    @Resource(name="deleteService") 
   	private  DeleteService delService;
   /**读取注册码中的serverIp
    * 注册时使用
    * **/
    @RequestMapping("valid/getServerMac")
    public void getServerIp(HttpServletRequest request,HttpServletResponse response) throws IOException{
         SerialCode serialCode=findService.findSNCodeById((long)1);
         String serverMAC="3C-46-D8-CC-80-2B";
         if(serialCode!=null){
        	 serverMAC=serialCode.getMAC();
         }
         LinkedHashMap<String, Object> responseData=new LinkedHashMap<String, Object>();
         responseData.put("serverIp", serverMAC);
         JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
         response.setContentType("text/xml");
		 response.setCharacterEncoding("utf-8");
		 PrintWriter out=response.getWriter();
		 out.println(responseJson);
		 out.flush();
		 out.close();
    }
    /**
     * 注册系统
     * **/
     @RequestMapping("valid/validate")
     public void validate(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	  String SN=request.getParameter("serialCode");
    	  boolean status=false;
          SerialCode serialCode=findService.findSNCodeById((long)1);
          if(serialCode!=null){
         	if(serialCode.getSerialCode().equals(NumConv.passwordMD5(SN))){
	         	status=true;
	         	serialCode.setValidation(SN);
	         	alterService.alterSNCode(serialCode);
         	}
          }
          LinkedHashMap<String, Object> responseData=new LinkedHashMap<String, Object>();
          responseData.put("status", status);
          JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
          response.setContentType("text/xml");
 		  response.setCharacterEncoding("utf-8");
 		  PrintWriter out=response.getWriter();
 		  out.println(responseJson);
 		  out.flush();
 		  out.close();
     }
    /**
     * 判断当前用户是否有角色管理的权限**/
     @RequestMapping("isPermittedRole")
    public void isSuperAdmin(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	 Subject currentUser = SecurityUtils.getSubject();//获取当前用户
//    	 String Account=currentUser.getPrincipal().toString();//当前用户的账号
         boolean isSuperAdmin=currentUser.isPermitted("role");//判断是否为超级管理员
         LinkedHashMap<String, Boolean> responseData=new LinkedHashMap<String, Boolean>();
         responseData.put("status", isSuperAdmin);
         JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
         response.setContentType("text/xml");
		 response.setCharacterEncoding("utf-8");
		 PrintWriter out=response.getWriter();
		 out.println(responseJson);
		 out.flush();
		 out.close();
    }
     /**
      * 判断用户是否具有设置光路的权限**/
     @RequestMapping("isPerToSetRoute")
     public void isPerToSetRoute(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	 Subject currentUser = SecurityUtils.getSubject();//获取当前用户
         boolean isSuperAdmin=currentUser.isPermitted("addRoute");//判断是否为超级管理员
         LinkedHashMap<String, Boolean> responseData=new LinkedHashMap<String, Boolean>();
         responseData.put("status", isSuperAdmin);
         JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
         response.setContentType("text/xml");
		 response.setCharacterEncoding("utf-8");
		 PrintWriter out=response.getWriter();
		 out.println(responseJson);
		 out.flush();
		 out.close();
    }
     /**
      * 判断用户是否具有设置光路的权限**/
     @RequestMapping("isPerCallTest")
     public void isPerCallTest(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	 Subject currentUser = SecurityUtils.getSubject();//获取当前用户
         boolean isSuperAdmin=currentUser.isPermitted("callTest");//判断是否为超级管理员
         LinkedHashMap<String, Boolean> responseData=new LinkedHashMap<String, Boolean>();
         responseData.put("status", isSuperAdmin);
         JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
         response.setContentType("text/xml");
		 response.setCharacterEncoding("utf-8");
		 PrintWriter out=response.getWriter();
		 out.println(responseJson);
		 out.flush();
		 out.close();
    }
     /**
      * 判断用户是否具有查看曲线的权限**/
     @RequestMapping("isPerCheckCurve")
     public void isPerCheckCurve(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	 Subject currentUser = SecurityUtils.getSubject();//获取当前用户
         boolean isSuperAdmin=currentUser.isPermitted("checkCurve");//判断是否为超级管理员
         LinkedHashMap<String, Boolean> responseData=new LinkedHashMap<String, Boolean>();
         responseData.put("status", isSuperAdmin);
         JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
         response.setContentType("text/xml");
		 response.setCharacterEncoding("utf-8");
		 PrintWriter out=response.getWriter();
		 out.println(responseJson);
		 out.flush();
		 out.close();
    }
     /**
      * 获取当前登录的用户信息**/
   @RequestMapping("getAccountInfo")
   public void addRoleTest(HttpServletRequest request,HttpServletResponse response) throws IOException{
	   Subject currentUser = SecurityUtils.getSubject();//获取当前用户
	   String account=currentUser.getPrincipal().toString();//当前用户的账号
	   //System.out.println("this session:"+currentUser.getSession().getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY));
	   User user=findService.findUserByAccount(account);
	   LinkedHashMap<String, Object> responseData=new LinkedHashMap<String, Object>();
	   boolean status=false;
	   if(user!=null){
		   status=true;
		   responseData.put("phone", user.getPhone());
		   responseData.put("email", user.getEmail());
		   responseData.put("account",account);
	   }
       responseData.put("status", status);
       JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
       response.setContentType("text/xml");
	   response.setCharacterEncoding("utf-8");
	   PrintWriter out=response.getWriter();
	   out.println(responseJson);
	   out.flush();
	   out.close();
   }
   /**
    * 注销**/
 @RequestMapping("logout")
 public void logout(HttpServletRequest request,HttpServletResponse response) throws IOException{
	   Subject currentUser = SecurityUtils.getSubject();//获取当前用户
	   currentUser.logout();
	   LinkedHashMap<String, Object> responseData=new LinkedHashMap<String, Object>();
	   responseData.put("status", true);
       JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
       response.setContentType("text/xml");
	   response.setCharacterEncoding("utf-8");
	   PrintWriter out=response.getWriter();
	   out.println(responseJson);
	   out.flush();
	   out.close();
 }
 /**修改密码**/
 @RequestMapping("account/modifyPword")
 public void modifyPword(HttpServletRequest request,HttpServletResponse response) throws IOException{
	   Subject currentUser = SecurityUtils.getSubject();//获取当前用户
	   User user=findService.findUserByAccount(currentUser.getPrincipal().toString());
	   String oldPword=NumConv.passwordMD5(request.getParameter("oldPword"));
	   String newPword=NumConv.passwordMD5(request.getParameter("newPword"));
	   boolean status=false;
	   String err="";
	   if(oldPword.equals(user.getPassword())){//验证输入的用户密码是否与数据库的密码一致
		   user.setPassword(newPword);
		   status=alterService.alterUser(user);
		   if(!status){
			   err="存储故障";
		   }
	   }
	   else{
		   err="验证失败，您输入的密码有误";
	   }
	   LinkedHashMap<String, Object> responseData=new LinkedHashMap<String, Object>();
	   responseData.put("status", status);
	   responseData.put("err", err);
       JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
       response.setContentType("text/xml");
	   response.setCharacterEncoding("utf-8");
	   PrintWriter out=response.getWriter();
	   out.println(responseJson);
	   out.flush();
	   out.close();
 }
 /**修改邮箱**/
 @RequestMapping("account/modifyInfo/modifyEmail")
 public void modifyEmail(HttpServletRequest request,HttpServletResponse response) throws IOException{
	   Subject currentUser = SecurityUtils.getSubject();//获取当前用户
	   User user=findService.findUserByAccount(currentUser.getPrincipal().toString());
	   String pword=NumConv.passwordMD5(request.getParameter("pword"));
	   String newEmail=request.getParameter("email");
	   boolean status=false;
	   String err="";
	   if(pword.equals(user.getPassword())){//验证输入的用户密码是否与数据库的密码一致
		   user.setEmail(newEmail);
		   status=alterService.alterUser(user);
		   if(!status){
			   err="存储故障";
		   }
	   }
	   else{
		   err="验证失败，您输入的密码有误";
	   }
	   LinkedHashMap<String, Object> responseData=new LinkedHashMap<String, Object>();
	   responseData.put("status", status);
	   responseData.put("err", err);
       JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
       response.setContentType("text/xml");
	   response.setCharacterEncoding("utf-8");
	   PrintWriter out=response.getWriter();
	   out.println(responseJson);
	   out.flush();
	   out.close();
 }
 /**修改手机号**/
 @RequestMapping("account/modifyInfo/modifyPhone")
 public void modifyPhone(HttpServletRequest request,HttpServletResponse response) throws IOException{
	   Subject currentUser = SecurityUtils.getSubject();//获取当前用户
	   User user=findService.findUserByAccount(currentUser.getPrincipal().toString());
	   String pword=NumConv.passwordMD5(request.getParameter("pword"));
	   String newPhone=request.getParameter("phone");
	   boolean status=false;
	   String err="";
	   if(pword.equals(user.getPassword())){//验证输入的用户密码是否与数据库的密码一致
		   user.setPhone(newPhone);
		   status=alterService.alterUser(user);
		   if(!status){
			   err="存储故障";
		   }
	   }
	   else{
		   err="验证失败，您输入的密码有误";
	   }
	   LinkedHashMap<String, Object> responseData=new LinkedHashMap<String, Object>();
	   responseData.put("status", status);
	   responseData.put("err", err);
       JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
       response.setContentType("text/xml");
	   response.setCharacterEncoding("utf-8");
	   PrintWriter out=response.getWriter();
	   out.println(responseJson);
	   out.flush();
	   out.close();
 }
 /**
  * 检查角色是否已经被使用**/
 @RequestMapping("isRoleExit")
 public void isRoleExit(HttpServletRequest request,HttpServletResponse response) throws IOException{
 	String roleName=request.getParameter("role");
 	Map<String,Object> responseData=new LinkedHashMap<String,Object>();
		boolean status=false;
		Role role=findService.findRoleByName(roleName);
		if(role!=null){
 		status=true;
 	}
 	responseData.put("status", status);
 	JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
 }
    /**
     * 增加角色
     * **/
    @RequestMapping("role/addRole")
    public void addRole(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	String roleName=request.getParameter("name");
    	String description=request.getParameter("description");
    	JSONArray pmssId=JSONArray.fromObject(request.getParameter("pmss"));//读取前端下发的当前角色权限ID
    	List<Permissions> pmss=new ArrayList<Permissions>();//当前角色对应的权限
    	WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();    
    	SessionFactory sessionFactory=(SessionFactory)wac.getBean("sessionFactory");
    	Session session=sessionFactory.openSession();
    	Transaction tx=session.beginTransaction();
    	for(int count=0;count<pmssId.size();count++){
    		pmss.add((Permissions) session.get(Permissions.class, Long.parseLong(pmssId.getString(count))));//利用session获取实体  当获取完成后释放session
        }
    	//提交事务
		tx.commit();
		session.close();
    	Role role=new Role();
    	Map<String,Object> responseData=new LinkedHashMap<String,Object>();
		boolean status=false;
		role.setName(roleName);
		role.setPmss(pmss);
		role.setDescription(description);
		Subject currentUser = SecurityUtils.getSubject();//获取当前用户
     	String Account=currentUser.getPrincipal().toString();//当前用户的账号
     	role.setAddTime(NumConv.currentTime());
     	role.setAddUser(Account);
		Serializable roleId=addService.addRole(role);
    	if(roleId!=null){
    		status=true;
    	}
    	responseData.put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
	}
    /**通过角色名获取角色的权限**/
    @RequestMapping("getPermsByRoleName")
    public void getPermsByRoleName(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	String roleName=request.getParameter("name");
    	Role role=findService.findRoleByName(roleName);
    	boolean status=false;
    	Map<String,Object> responseData=new LinkedHashMap<String,Object>();
    	if(role!=null){
    		List<Permissions>perms=role.getPmss();
    		if(perms!=null){
    			status=true;
    			List<Long> permIds=new ArrayList<Long>();
    			for(Permissions perm:perms){
    				permIds.add(perm.getId());
    			}
    			responseData.put("perms", permIds);
    		}
    	}
    	responseData.put("status", status);
    	JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
	}
    /**
     * 修改角色
     * 
     * 建立session后不释放连接 当获取完成所有实体厚释放session，从而节约时间
     * **/
    @RequestMapping("role/modifyRole")
    public void modifyRole(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	String roleName=request.getParameter("name");
    	String description=request.getParameter("description");
    	Role role=findService.findRoleByName(roleName);
    	JSONArray pmssId=JSONArray.fromObject(request.getParameter("pmss"));//读取前端下发的当前角色权限ID
    	List<Permissions> pmss=new ArrayList<Permissions>();//当前角色对应的权限
    	WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();    
    	SessionFactory sessionFactory=(SessionFactory)wac.getBean("sessionFactory");
    	Session session=sessionFactory.openSession();
    	Transaction tx=session.beginTransaction();
    	for(int count=0;count<pmssId.size();count++){
    		pmss.add((Permissions) session.get(Permissions.class, Long.parseLong(pmssId.getString(count))));//利用session获取实体  当获取完成后释放session
        }
    	//提交事务
		tx.commit();
		session.close();
    	Subject currentUser = SecurityUtils.getSubject();//获取当前用户
     	String Account=currentUser.getPrincipal().toString();//当前用户的账号
     	role.setAlterTime(NumConv.currentTime());
     	role.setAlterUser(Account);
    	Map<String,Object> responseData=new LinkedHashMap<String,Object>();
		role.setName(roleName);
		role.setPmss(pmss);
		role.setDescription(description);
		boolean status=alterService.alterRole(role);
    	responseData.put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
	}
    /**
     * 删除角色
     * **/
    @RequestMapping("role/delRole")
    public void deleteRole(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	Map<String,Object> responseData=new LinkedHashMap<String,Object>();
    	Long roleId=Long.parseLong(request.getParameter("roleId"));
    	delService.deleteRole(roleId);
		boolean status=true;
		if(findService.findRoleById(roleId)!=null){
			status=false;
		}
		responseData.put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
	}
    /**
     * 检查账号是否已经被使用**/
    @RequestMapping("isAccountExit")
    public void isAccountExit(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	String account=request.getParameter("account");
    	Map<String,Object> responseData=new LinkedHashMap<String,Object>();
		boolean status=false;
		User user=findService.findUserByAccount(account);
		if(user!=null){
    		status=true;
    		responseData.put("email", user.getEmail());
    		responseData.put("phone", user.getPhone());
    	}
    	responseData.put("status", status);
    	JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
    }
    /**
     * 增加用户**/
    @RequestMapping("user/addUser")
    public void addUser(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	//System.out.println("addUser");
    	String userName=request.getParameter("name");
    	String password=NumConv.passwordMD5(request.getParameter("password"));//将用户密码进行MD5加密
    	String phone=request.getParameter("phone");
    	String email=request.getParameter("email");
    	String description=request.getParameter("description");
    	Map<String,Object> responseData=new LinkedHashMap<String,Object>();
		boolean status=false;
    	Long roleId=Long.parseLong(request.getParameter("roleId"));
    	User user=new User();
    	user.setAccount(userName);
    	user.setPassword(password);
    	user.setPhone(phone);
    	user.setEmail(email);
    	user.setDescription(description);
    	user.setRole(findService.findRoleById(roleId));
    	Serializable userId=addService.addUser(user);
    	if(userId!=null){
    		status=true;
    	}
    	responseData.put("status", status);
    	JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
	} 
    /**
     * 修改用户**/
    @RequestMapping("user/modifyUser")
    public void modifyUser(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	String account=request.getParameter("account");
    	Long roleId=Long.parseLong(request.getParameter("roleId"));
    	String phone=request.getParameter("phone");
    	String email=request.getParameter("email");
    	String description=request.getParameter("description");
    	Map<String,Object> responseData=new LinkedHashMap<String,Object>();
		boolean status=false;
    	User user=findService.findUserByAccount(account);
    	user.setPhone(phone);
    	user.setEmail(email);
    	user.setDescription(description);
    	user.setRole(findService.findRoleById(roleId));
    	status=alterService.alterUser(user);
    	responseData.put("status", status);
    	JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
	} 
    /**
     * 删除用户**/
    @RequestMapping("user/delUser")
    public void delUser(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	Map<String,Object> responseData=new LinkedHashMap<String,Object>();
		boolean status=true;
    	Long userId=Long.parseLong(request.getParameter("userId"));
    	//System.out.println("userId:"+userId);
    	delService.deleteUser(userId);
    	if(findService.findUserById(userId)!=null){
    		status=false;
    	}
    	responseData.put("status", status);
    	JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
	} 
    /**通过账号查找用户信息**/
    @RequestMapping("user/searchByAccount")
    public void searchByAccount(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	Map<String,Object> responseData=new LinkedHashMap<String,Object>();
		boolean status=false;
    	String account=request.getParameter("account");
    	User user=findService.findUserByAccount(account);
    	if(user!=null){
    		status=true;
    		responseData.put("roleId", user.getRole().getId());
    		responseData.put("roleName", user.getRole().getName());
    		responseData.put("id", user.getId());
    		responseData.put("account", user.getAccount());
    		responseData.put("email", user.getEmail());
    		responseData.put("phone", user.getPhone());
    		responseData.put("description", user.getDescription());
    	}
    	responseData.put("status", status);
    	JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.println(responseJson.get(0));
		out.flush();
		out.close();
	} 
  /**通过账号获取用户信息**/
    @RequestMapping("user/getByAccount")
    public void getByAccount(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	Map<String,Object> responseData=new LinkedHashMap<String,Object>();
		boolean status=false;
    	String account=request.getParameter("account");
    	User user=findService.findUserByAccount(account);
    	if(user!=null){
    		status=true;
    		responseData.put("role", user.getRole().getId());
    		responseData.put("email", user.getEmail());
    		responseData.put("phone", user.getPhone());
    		responseData.put("description", user.getDescription());
    	}
    	responseData.put("status", status);
    	JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
	} 
   /**找回密码-发送验证码**/ 
    @RequestMapping("account/getVerifyCode")
    public void getVerifyCode(HttpServletRequest request,HttpServletResponse response){
    	Map<String,Object> responseData=new LinkedHashMap<String,Object>();
    	boolean status=false;
    	String verifyWay=request.getParameter("verifyWay");
    	String desAddress="";
    	String account=request.getParameter("account");
    	User user=findService.findUserByAccount(account);
    	String code=NumConv.createVerifyCode(4);
    	String context="您正在尝试找回账号"+account+"的密码，本次验证码为："+code+"<br/>\n"+
    	               "请勿泄露验证码，如果不是您本人操作请忽略该信息";
    	String info="";
    	if(verifyWay.equals("email")){//发送验证码至邮箱
    		desAddress=user.getEmail();
    		List<String> emails=new ArrayList<String>();
    		emails.add(desAddress);
    		status=MessageUtil.sendEmail(emails, "邮箱验证找回账号密码",context);
    		if(!status){//发送失败重发
    			status=MessageUtil.sendEmail(emails, "邮箱验证找回账号密码",context);
    			if(status){
    				info="验证消息已成功发送到您指定邮箱，请注意查收";
    			}
    			else{
    				info="发送失败，请确认您指定邮箱是否开启垃圾邮箱拦截";
    			}
    		}
    		else{
    			info="验证消息已成功发送到您指定邮箱，请注意查收";
    		}
    	}
    	else{//短信找回
    		List<String> phones=new ArrayList<String>();
    		phones.add(desAddress);
    		status=MessageUtil.sendMessage(phones, context);
    		desAddress=user.getPhone();
    		if(!status){//发送失败重发
    			status=MessageUtil.sendMessage(phones, context);
    			if(status){
    				info="验证消息已成功发送到您手机，请注意查收";
    			}
    			else{
    				info="发送失败，请确认短信猫或您账户是否正常";
    			}
    		}
    		else{
    			info="验证消息已成功发送到您手机，请注意查收";
    		}
    	}
    	if(status){
    		Verify_codes verifyCode=findService.findVerifyCodeByAccount(account);
    		if(verifyCode!=null){//存在当前账户的则修改
    			verifyCode.setVerifyCode(code);
    			status=alterService.alterVerifyCode(verifyCode);
    		}
    		else{
    			verifyCode=new Verify_codes();
    			verifyCode.setAccount(account);
    			verifyCode.setVerifyCode(code);
    			addService.addVerifyCode(verifyCode);
    		}
    	}
    	responseData.put("status", status);
    	responseData.put("info", info);
    	JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
    	response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		
		try {
			PrintWriter out=response.getWriter();
			out.println(responseJson);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} 
    /**找回密码-发送验证码
     * 核对验证码是否正确
     * **/ 
    @RequestMapping("account/checkVerifyCode")
    public void checkVerifyCode(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	Map<String,Object> responseData=new LinkedHashMap<String,Object>();
		boolean status=false;
    	String account=request.getParameter("account");
    	String code=request.getParameter("verifyCode");
    	//System.out.println("account:"+account+"\t code:"+code);
    	Verify_codes verifyCode=findService.findVerifyCodeByAccount(account);
    	if(verifyCode!=null){
    		if(verifyCode.getVerifyCode().equals(code)){
    			status=true;
    			delService.deleteVerifyCode(verifyCode.getId());
    		}
    	}
    	responseData.put("status", status);
        JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		//System.out.println("response:"+responseJson);
		out.flush();
		out.close();
	}  
    /**验证重设密码**/
    @RequestMapping("account/reSetPword")
    public void reSetPword(HttpServletRequest request,HttpServletResponse response) throws IOException{
   	   User user=findService.findUserByAccount(request.getParameter("account"));
   	   String pword=NumConv.passwordMD5(request.getParameter("pword"));
   	   boolean status=false;
   	   String err="";
   	   user.setPassword(pword);
       status=alterService.alterUser(user);
   		   if(!status){
   			   err="存储故障";
   		   }
   	   LinkedHashMap<String, Object> responseData=new LinkedHashMap<String, Object>();
   	   responseData.put("status", status);
   	   responseData.put("err", err);
       JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
       response.setContentType("text/xml");
   	   response.setCharacterEncoding("utf-8");
   	   PrintWriter out=response.getWriter();
   	   out.println(responseJson);
   	   out.flush();
   	   out.close();
    }
    /**验证session是否有效**/
    @RequestMapping("checkSession")
   public void checkSession(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	  Subject currentUser = SecurityUtils.getSubject();//获取当前用户
    	  LinkedHashMap<String, Object> responseData=new LinkedHashMap<String, Object>();
     	  responseData.put("status", currentUser.isAuthenticated());
     	  JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
          response.setContentType("text/xml");
     	  response.setCharacterEncoding("utf-8");
     	  PrintWriter out=response.getWriter();
     	  out.println(responseJson);
     	  out.flush();
     	  out.close();
    }
   
}
