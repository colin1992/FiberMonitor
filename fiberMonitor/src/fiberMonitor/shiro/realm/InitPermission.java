package fiberMonitor.shiro.realm;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import domain.Alarm_ways;
import domain.Duty_schedule;
import domain.Permissions;
import domain.Priorities;
import domain.Role;
import domain.SerialCode;
import domain.SystemInfo;
import domain.User;
import fiberMonitor.bean.NumConv;
import fiberMonitor.bean.SerialPortUtil;
import service.AddService;
import service.AlterService;
import service.FindService;

public class InitPermission extends HttpServlet {  
	  
    /**  
     */  
    private static final long serialVersionUID = 1L;  
    private AddService addServiceImpl;
    @Override  
    public void init(ServletConfig config) {  
	        try {  
	            super.init();  
	        } catch (ServletException e) {  
	            e.printStackTrace();  
	        } 
	        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();    
	        addServiceImpl=(AddService)wac.getBean("addService");
	        FindService findServiceImpl=(FindService)wac.getBean("findService");
	        AlterService alterServiceImpl=(AlterService)wac.getBean("alterService");
	        List<Permissions> permIn=findServiceImpl.findAllPermissions();
	        System.out.println("================>[Servlet]系统初始化自动加载建立角色权限开始.");  
            List<Map<String,Object>>permss=new ArrayList<Map<String,Object>>();//所有的权限
            /**
             * 账户管理
             * **/
            LinkedHashMap <String,Object>account=new LinkedHashMap<String,Object>();
            account.put("name", "账户管理");
            account.put("permission","account");
            account.put("description","账户管理");
            account.put("parent",null);
    	    String accountChilds [][]={	
    	    							{"修改密码","modifyPword","修改密码"},
    	    							{"修改个人信息","modifyInfo","修改个人信息"},				
    	    						  };
    	    account.put("children",accountChilds);
    	    account.put("grandChildren",null);
        	permss.add(account);
        	/**
        	 * 角色管理模块
        	 * **/
        	LinkedHashMap <String,Object>roles=new LinkedHashMap<String,Object>();
        	roles.put("name", "角色管理");
        	roles.put("permission","role");
        	roles.put("description","角色管理");
        	roles.put("parent",null);
    	    String roleChilds [][]={	
    	    							{"增加角色","addRole","增加角色"},
    	    							{"删除角色","delRole","删除角色"},				
    	    							{"修改角色","modifyRole","修改角色"},
    	    						};
        	roles.put("children",roleChilds);
         	roles.put("grandChildren",null);
        	permss.add(roles);
        	/**
        	 * 用户管理模块
        	 * **/
        	LinkedHashMap <String,Object>users=new LinkedHashMap<String,Object>();//用户管理部分
        	users.put("name", "用户管理");
        	users.put("permission","user");
        	users.put("description", "用户管理");
        	users.put("parent", null);
    	    String userChilds [][]={   
    	    							{"增加用户","addUser","增加用户"},
    									{"删除用户","delUser","删除用户"},				
    									{"修改用户","modifyUser","修改用户"},
    								};
    	    users.put("children",userChilds);
       	    users.put("grandChildren",null);
    	    permss.add(users);
    	    /**
    	     *资源管理模块 
    	     * **/
        	LinkedHashMap <String,Object>resource=new LinkedHashMap<String,Object>();//资源管理部分
        	resource.put("name", "资源管理");
        	resource.put("permission","resource");
        	resource.put("description", "资源管理");
        	resource.put("parent", null);
    	    String resourceChilds [][]={   
    		    							{"区域管理","area","区域管理"},
    		    							{"局站管理","station","局站管理"},
    		    							{"光缆管理","cable","光缆管理"},
    		    							{"光缆地标管理","landmark","光缆地标管理"},
    		    							{"纤芯管理","fiber","纤芯管理"},
    		    							{"跳纤管理","jumper","跳纤管理"},
    		    							{"光路管理","route","光路管理"},
    		    							{"RTU管理",   "rtu",     "RTU管理"},
    										{"配线架管理","frame","配线架管理"},
    										{"机柜管理","cabinet","机柜管理"},
    								};
    	    String resGrandChilds[][][] ={
    	    								{
    	    									{"增加区域","addArea","增加区域"},
    	    									{"删除区域","delArea","删除区域"},
    	    									{"修改区域","modifyArea","修改区域"},
    	    								},
    	    								{
    	    									{"增加局站","addStation","增加局站"},
    	    									{"删除局站","delStation","删除局站"},
    	    									{"修改局站","modifyStation","修改局站"},
    	    								},
    	    								{
    	    									{"增加光缆","addCable","增加光缆"},
    	    									{"删除光缆","delCable","删除光缆"},
    	    									{"修改光缆","modifyCable","修改光缆"},
    	    								},
    	    								{
    	    									{"增加地标","addLandmark","增加光缆地标"},
    	    									{"删除地标","delLandmark","删除光缆地标"},
    	    									{"修改地标","modifyLandmark","修改光缆地标"},
    	    								},
    	    								{
    	    									{"修改纤芯","modifyFiber","修改纤芯"},
    	    								},
    	    								{
    	    									{"增加跳纤","addJumper","增加跳纤"},
    	    									{"删除跳纤","delJumper","删除跳纤"},
    	    									{"修改跳纤","modifyJumper","修改跳纤"},
    	    								},
    	    								{
    	    									{"增加光路","addRoute","增加光路"},
    	    									{"删除光路","delRoute","删除光路"},
    	    									{"修改光路","modifyRoute","修改光路"},
    	    									{"设置优化参数","optiRoute","设置优化参数"},
    	    									{"设置光功率门限","setThreshold","设置光功率门限"},
    	    								},
    	    								{
    	    									{"增加RTU","addRtu","增加RTU"},
    	    									{"删除RTU","delRtu","删除RTU"},
    	    									{"修改RTU","modifyRtu","修改RTU"},
    	    								},
    	    								{
    	    									{"增加配线架","addFrame","增加配线架"},
    	    									{"删除配线架","delFrame","删除配线架"},
    	    									{"修改配线架","modifyFrame","修改配线架"},
    	    								},
    	    								{
    	    									{"增加机柜","addCabinet","增加机柜"},
    	    									{"删除机柜","delCabinet","删除机柜"},
    	    									{"修改机柜","modifyCabinet","修改机柜"},
    	    								},
    	    								
    	    							 };
    	    resource.put("children",resourceChilds);
    	    resource.put("grandChildren",resGrandChilds);
    	    permss.add(resource);
        	/**
        	 * 曲线管理模块
        	 * **/
        	LinkedHashMap <String,Object>curve=new LinkedHashMap<String,Object>();
        	curve.put("name", "曲线管理");
        	curve.put("permission","curve");
        	curve.put("description","曲线管理");
        	curve.put("parent",null);
    	    String curveChilds [][]={	
    	    							{"删除曲线","delCurve","删除曲线"},
    	    							{"曲线查看","checkCurve","曲线查看"},
    	    							{"点名测试","callTest","点名测试"},
    	    							{"周期测试","periodTest","周期测试"},				
    	    							{"障碍告警测试","obstacleTest","障碍告警测试"},
    	    							
    	    						};
    	    String curveGrandChilds[][][] ={
    	    									{
    	    										
    	    									},
    	    									{
    	    										{"设置参考曲线","setRefrence","设置参考曲线"},
    	    										{"增加事件点","addEvent","增加事件点"},
    	    									},
    	    									{
    	    										
    	    									},
    	    									{
    	    										{"设置周期测试","setPeriodTest","设置周期测试"},
    	    										{"取消周期测试","cancelPeriodTest","取消周期测试"},
        											{"刷新时间表", "refreshCycPara","刷新时间表"},

    	    									},
    	    									{
    	    										{"分组配置","obstacleGroup","分组配置"},
    	    										{"设置障碍告警","setObstacle","设置障碍告警"},
    	    										{"取消障碍告警测试","cancelObstacle","取消测试"},
    	    									}
    	    								};
    	    curve.put("children",curveChilds);
    	    curve.put("grandChildren",curveGrandChilds);
    	    permss.add(curve);
        	/**
        	 * 劣化分析模块
        	 * **/
    	    LinkedHashMap <String,Object>degradation=new LinkedHashMap<String,Object>();
        	degradation.put("name", "劣化分析");
        	degradation.put("permission","degradation");
        	degradation.put("description","劣化分析");
        	degradation.put("parent", null);
    	    String degradationChilds[][]={
    							    		{"光功率实时查阅","checkPowerValue","光功率实时查阅"},
    							    		{"光功率历史数据查阅","checkHisPowerValue","光功率历史数据查阅"},
    										{"光功率劣化分析","degradationPowerValue","光功率劣化分析"},				
    										{"劣化参数配置","degradationParaSet","劣化参数配置"},
    									 };
    	    degradation.put("children", degradationChilds);
    	    degradation.put("grandChildren",null);
    	    permss.add(degradation);
    	    /**
    	     * 告警管理
    	     * **/
    	    LinkedHashMap <String,Object>alarm=new LinkedHashMap<String,Object>();
    	    alarm.put("name", "告警管理");
    	    alarm.put("permission","alarm");
    	    alarm.put("description","告警管理");
    	    alarm.put("parent", null);
    	    String alarmChilds[][]={
    							    		{"处理告警","handleAlarm","处理告警"},
    							    		{"删除告警","delAlarm","删除告警"},
    							    		{"新增告警经验","addAlarmBank","新增告警经验"},
    										{"修改告警经验","modifyAlarmBank","修改告警经验"},				
    										{"删除告警经验","delAlarmBank","删除告警经验"},
    									 };
    	    alarm.put("children", alarmChilds);
    	    alarm.put("grandChildren",null);
    	    permss.add(alarm);
        	/**
        	 * 业务切换模块
        	 * **/
    	    LinkedHashMap <String,Object> switchs=new LinkedHashMap<String,Object>();
    	    switchs.put("name", "业务切换");
    	    switchs.put("permission","switch");
    	    switchs.put("description","业务切换");
    	    switchs.put("parent", null);
    	    String switchChilds[][]={
    							    	{"光纤切换","fiberSwitch","光纤切换"},
    							    	{"增加保护组","setSwitchGroup","增加保护组"},
    							    	{"删除保护组","delSwitchGroup","删除保护组"},
    							    	{"配置保护参数","setGroupPara","配置保护参数"},
    								};
    	 
    	    switchs.put("children", switchChilds);
    	    switchs.put("grandChildren",null);
    	    permss.add(switchs);
    	    /**主控设置**/
    	    LinkedHashMap <String,Object> masterCon=new LinkedHashMap<String,Object>();
    	    masterCon.put("name", "主控设置");
    	    masterCon.put("permission","masterCon");
    	    masterCon.put("description","主控设置");
    	    masterCon.put("parent", null);
    	    String masterChilds[][]={
    	    							{"重启RTU","restartRtu","重启RTU"},
    							    	{"获取RTU时间","getRtuTime","获取RTU时间"},
    							    	{"更新RTU时间","setRtuTime","更新RTU时间"},
    							    	{"获取RTU IP","getRtuIp","获取RTU IP"},
    							    	{"更新RTU IP","setRtuIp","更新RTU IP"},
    							    	{"设置邮件服务器地址","setEmail","设置邮件服务器地址"},
    								};
    	 
    	    masterCon.put("children", masterChilds);
    	    masterCon.put("grandChildren",null);
    	    permss.add(masterCon);
    	    if(permIn.size()<1){//只有数据库未建立权限记录时才新建权限
    	    	for(int pCount=0;pCount<permss.size();pCount++){
            		Permissions permission=new Permissions();
                	permission.setName((String) permss.get(pCount).get("name"));
                	permission.setDescription((String) permss.get(pCount).get("description"));
                	permission.setParent((Permissions) permss.get(pCount).get("parent"));
                	permission.setPermission((String) permss.get(pCount).get("permission"));
                	Serializable permId=addServiceImpl.addPermission(permission);
                	if(permId!=null){
                		String children[][]=(String[][])permss.get(pCount).get("children");
                		String [][][] grandChildren=(String [][][] )permss.get(pCount).get("grandChildren");
                		for(int cCount=0;cCount<children.length;cCount++){
                			Permissions childPerm=new Permissions();
                			childPerm.setName(children[cCount][0]);
                			childPerm.setPermission(children[cCount][1]);
                			childPerm.setDescription(children[cCount][2]);
                			childPerm.setParent(permission);
                			addServiceImpl.addPermission(childPerm);
                			if(grandChildren!=null){
                				for(int gCount=0;gCount<grandChildren[cCount].length;gCount++){
                					    Permissions gChildPerm=new Permissions();
                    					gChildPerm.setName(grandChildren[cCount][gCount][0]);
                    					gChildPerm.setPermission(grandChildren[cCount][gCount][1]);
                    					gChildPerm.setDescription(grandChildren[cCount][gCount][2]);
                    					gChildPerm.setParent(childPerm);
                            			addServiceImpl.addPermission(gChildPerm);
                				}
                			}
                		}
                	}
               }
    	    /**新建超级管理员**/	
    	    	Role superAdmin=new Role();
    	    	superAdmin.setName("超级管理员");
    	    	superAdmin.setDescription("系统初始时自带角色，拥有最高权限，请勿删除");
    	    	superAdmin.setPmss(findServiceImpl.findAllPermissions());
    	    	Serializable roleId=addServiceImpl.addRole(superAdmin);
    	    	if(roleId!=null){
    	    		User admin=new User();
        	    	admin.setAccount("admin");
        	    	admin.setPassword(NumConv.passwordMD5("123456"));
        	    	admin.setDescription("超级管理员");
        	    	admin.setEmail("");
        	    	admin.setPhone("");
        	    	admin.setRole(superAdmin);
        	    	addServiceImpl.addUser(admin);
    	    	}
    	  }
    	  //新建值班表
          if(findServiceImpl.findAllDutySchedule().size()<1){
        	  for(int i=1;i<8;i++){
        		  Duty_schedule duty=new Duty_schedule();
        		  duty.setDutyWeek(i);
        		  addServiceImpl.addDutySchedule(duty);
        	  }
          }
    	  if(findServiceImpl.findAllAlarmWay().size()<1){
    		  String priorityPara[][]={
  		    		{"普通支路","普通支路","0"},
  		    		{"关键支路","关键支路","0"},
  					{"普通干线","普通干线","0"},				
  					{"关键干线","关键干线","0"},
  					};
  			for(int index=0;index<priorityPara.length;index++){
  			Priorities priority=new Priorities();
  			priority.setName(priorityPara[index][0]);
  			priority.setDescription(priorityPara[index][1]);
  			priority.setRtuId(Long.parseLong(priorityPara[index][2]));
  			addServiceImpl.addPriorities(priority);
  			}
  			String alarmPara[][]={
  					{"本地可视可闻","浏览器端实现视图声音告警"},
  					{"远端可视可闻","RTU实现视图声音告警"},
  					{"本地远端可视可闻","浏览器和RTU端均实行视图声音告警"},
  					{"本地可视","浏览器端实现视图告警"},
  					{"远端可视图","RTU端实现视图告警"},
  					{"无告警提示","不产生告警提示"},
  			};
  			for(int i=0;i<alarmPara.length;i++){
				Alarm_ways alarmWay=new Alarm_ways();
				alarmWay.setAlarm_name(alarmPara[i][0]);
				alarmWay.setDescription(alarmPara[i][1]);
				addServiceImpl.addAlarmWay(alarmWay);
			}
  			System.out.println("================>[Servlet]自动加载建立角色权限结束.");   
    	}
    	SerialCode serialCode=findServiceImpl.findSNCodeById((long)1);
    	//序列号用md5加密后存储
    	String SN=NumConv.passwordMD5(NumConv.createSNCode(false));
    	String MAC=NumConv.getMACAddress();
    	if(serialCode==null){
    		serialCode=new SerialCode();
    		serialCode.setSerialCode(SN);
    		serialCode.setMAC(MAC);
    		addServiceImpl.addSNCode(serialCode);
    	}
    	else{
    		serialCode.setSerialCode(SN);
    		//serialCode.setValidation("");
    		serialCode.setMAC(MAC);
    		alterServiceImpl.alterSNCode(serialCode);
    	}
    	System.out.println("================>[Servlet]产生注册码.");   
    	SystemInfo systemInfo=findServiceImpl.findSystemInfoById((long)1);
    	 String ip="";
    	 try {
			 InetAddress addr = InetAddress.getLocalHost();
			 ip=addr.getHostAddress().toString();//获得本机IP
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(systemInfo==null){
			systemInfo=new SystemInfo();
			 systemInfo.setIp(ip);
			 /**获取连接GSM模块的串口**/
			 SerialPortUtil serial=new SerialPortUtil(9600);
		     String GSMPort=serial.GetGsmPort();
		     systemInfo.setSmsPort(GSMPort);
			 addServiceImpl.addSystem(systemInfo);
		 }
		 else{
			 systemInfo.setIp(ip);
			 alterServiceImpl.alterSystemInfo(systemInfo);
		 }

    	
}  
}
