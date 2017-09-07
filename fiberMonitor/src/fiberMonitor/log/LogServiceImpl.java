package fiberMonitor.log;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;

import domain.Log;
import fiberMonitor.bean.NumConv;
import service.AddService;
import service.DeleteService;
import service.FindService;


public class LogServiceImpl implements ILogService {
	private FindService findServiceImpl;
	private AddService addServiceImpl;
	public void setFindService(FindService findServiceImpl)
	{
		this.findServiceImpl = findServiceImpl;
	}
	public void setAddService(AddService addServiceImpl)
	{
		this.addServiceImpl  = addServiceImpl;
	}
	public void setDeleteService(DeleteService deleteServiceImpl)
	{
	}
	
	
	//给切换光路添加日志的方法
    public void switchLog(JoinPoint point) {
        if(point.getSignature().getName().equals("alterProtectGroups"))
        {
        	String name = "切换光路";
        	String type = "切换光路";
        	String operateDetail="切换光路";
        	Log log = new Log();
        	log.setDate(NumConv.currentTime());
        	Subject currentUser = SecurityUtils.getSubject();//获取当前用户
         	String Account=currentUser.getPrincipal().toString();//当前用户的账号
         	log.setUser(Account);
        	log.setResourceName(name);
        	log.setResourceType(type);
        	log.setOperateDetail(operateDetail);
        	addServiceImpl.addLog(log);
        }
    }
    
    //给删除资源添加日志的方法
    public void deleteResLog(JoinPoint point) {
    	String name="";
    	String type="";
    	String operateDetail="";
    	long deleteId =Long.parseLong(point.getArgs()[0].toString());
    	if(point.getSignature().getName().equals("deleteArea"))
    	{
    		name = findServiceImpl.findArea(deleteId).getArea_name();
    		type = "区域";
    	}
    	else if(point.getSignature().getName().equals("deleteStation"))
    	{
    		try{
    			name = findServiceImpl.findStation(deleteId).getStation_name();
        		type = "局站";
    		}catch(Exception e){
    			
    		}
    		
    	}
    	else if(point.getSignature().getName().equals("deleteLandmark"))
    	{
    		name = findServiceImpl.findLandmark(deleteId).getName();
    		type = "地标";
    	}
    	else if(point.getSignature().getName().equals("deleteCabinet"))
    	{
    		name = findServiceImpl.findCabinet(deleteId).getCabinet_name();
    		type = "机柜";
    	}
    	else if(point.getSignature().getName().equals("deleteDistributingFrame"))
    	{
    		name = findServiceImpl.findDistributingFrame(deleteId).getFrame_name();
    		type = "配线架";
    	}
    	else if(point.getSignature().getName().equals("deleteRtu"))
    	{
    		name = findServiceImpl.findRtu(deleteId).getRtu_name();
    		type = "RTU";
    	}
    	else if(point.getSignature().getName().equals("deleteJumperFrame"))
    	{
    		name = findServiceImpl.findJumperFrame(deleteId).getJumper_frame_name();
    		type = "配线架跳纤";
    	}
    	else if(point.getSignature().getName().equals("deleteJumperRoute"))
    	{
    		name = findServiceImpl.findJumperRoute(deleteId).getJumper_route_name();
    		type = "光路(切换)跳纤";
    	}
    	else if(point.getSignature().getName().equals("deleteRoute"))
    	{
    		name = findServiceImpl.findRoute(deleteId).getRtu_name();
    		type = "光路";
    	}
    	else if(point.getSignature().getName().equals("addOpticalCable"))
    	{
    		name = findServiceImpl.findOpticalCable(deleteId).getOptical_cable_name();
    		type = "光缆";
    	}
    	else if(point.getSignature().getName().equals("deletePriority"))
    	{
    		name = findServiceImpl.findPriorityById(deleteId).getName();
    		type = "告警组";
    	}
    	else if(point.getSignature().getName().equals("deleteRole"))
    	{
    		name = findServiceImpl.findRoleById(deleteId).getName();
    		type = "角色";
    	}
    	else if(point.getSignature().getName().equals("deletePermission"))
    	{
    		name = findServiceImpl.findPermissionById(deleteId).getName();
    		type = "权限";
    	}
    	else if(point.getSignature().getName().equals("deleteUser"))
    	{
    		name = findServiceImpl.findUserById(deleteId).getAccount();
    		type = "用户";
    	}
    	else if(point.getSignature().getName().equals("deleteDutyOperator"))
    	{
    		name = findServiceImpl.findDutyOperatorById(deleteId).getAccount();
    		type = "值班人员";
    	}
    	if(!name.equals("")&&!type.equals("")){
    		operateDetail="删除"+type+"\""+name+"\"";
        	Log log = new Log();
        	log.setDate(NumConv.currentTime());
        	Subject currentUser = SecurityUtils.getSubject();//获取当前用户
         	String Account=currentUser.getPrincipal().toString();//当前用户的账号
         	log.setUser(Account);
        	log.setResourceName(name);
        	log.setResourceType(type);
        	log.setOperateDetail(operateDetail);
        	addServiceImpl.addLog(log);
    	}
    	
    }

    //给增加资源添加日志的方法
    public void addResLog(JoinPoint point, Object returnObj) {
        //此方法返回的是一个数组，数组中包括request以及ActionCofig等类对象
    	String name = "";
    	String type = "";
    	String operateDetail = "";
    	long id = Long.parseLong(returnObj.toString());
    	if(point.getSignature().getName().equals("addArea"))
    	{
    		name = findServiceImpl.findArea(id).getArea_name();
    		type = "区域";
    	}
    	else if(point.getSignature().getName().equals("addStation"))
    	{
    		name = findServiceImpl.findStation(id).getStation_name();
    		type = "局站";
    	}
    	else if(point.getSignature().getName().equals("addCabinet"))
    	{
    		name = findServiceImpl.findCabinet(id).getCabinet_name();
    		type = "机柜";
    	}
    	else if(point.getSignature().getName().equals("addDistributingFrame"))
    	{
    		name = findServiceImpl.findDistributingFrame(id).getFrame_name();
    		type = "配线架";
    	}
    	else if(point.getSignature().getName().equals("addRtu"))
    	{
    		name = findServiceImpl.findRtu(id).getRtu_name();
    		type = "RTU";
    	}
    	else if(point.getSignature().getName().equals("addOpticalCable"))
    	{
    		name = findServiceImpl.findOpticalCable(id).getOptical_cable_name();
    		type = "光缆";
    	}
    	else if(point.getSignature().getName().equals("addJumperFrame"))
    	{
    		name = findServiceImpl.findJumperFrame(id).getJumper_frame_name();
    		type = "配线架跳纤";
    	}
    	else if(point.getSignature().getName().equals("addJumperRoute"))
    	{
    		name = findServiceImpl.findJumperRoute(id).getJumper_route_name();
    		type = "光路跳纤";
    	}
    	else if(point.getSignature().getName().equals("addJumperSwitch"))
    	{
    		name = findServiceImpl.findJumperRoute(id).getJumper_route_name();
    		type = "切换跳纤";
    	}
 
    	else if(point.getSignature().getName().equals("addRoute"))
    	{
    		name = findServiceImpl.findRoute(id).getRoute_name();
    		type = "光路";
    	}
    	else if(point.getSignature().getName().equals("addOptimizeParameter"))
    	{
    		name = findServiceImpl.findOptimizeParameter(id).getRoute().getRoute_name()+"光路";
    		type = "优化测试参数";
    	}
//    	else if(point.getSignature().getName().equals("addCurve"))
//    	{         
//    		name = findServiceImpl.findCurve(id).getRoute().getRoute_name()+"光路"+"进行"+findServiceImpl.findCurve(id).getCurve_type() ;
//    		type = "曲线测试";
//    	}
    	else if(point.getSignature().getName().equals("addPriorities"))
    	{
    		name = findServiceImpl.findPriorityById(id).getName();
    		type = "告警组";
    	}
    	else if(point.getSignature().getName().equals("addTempGroupInfo"))
    	{
    		name = findServiceImpl.findTempGroupById(id).getRouteIds()+"光路";
    		type = "暂存告警记录";
    	}
    	else if(point.getSignature().getName().equals("addAlarm"))
    	{
    		name = findServiceImpl.findAlarmById(id).getRoute().getRoute_name()+"光路";
    		type = "告警信息";
    	}
    	else if(point.getSignature().getName().equals("addRole"))
    	{
    		if(id>1){
    			name = findServiceImpl.findRoleById(id).getName();
        		type = "角色";
    		} 
    	}
    	else if(point.getSignature().getName().equals("addUser"))
    	{
    		if(id>1){
    			name = findServiceImpl.findUserById(id).getAccount();
        		type = "用户";
    		}
    		
    	}
    	else if(point.getSignature().getName().equals("addDutyOperator"))
    	{
    		name = findServiceImpl.findDutyOperatorById(id).getAccount();
    		type = "值班人员";
    	}
    	if(!name.equals("")&&!type.equals("")){
    		operateDetail="增加"+type+"\""+name+"\"";
        	Log log = new Log();
        	log.setDate(NumConv.currentTime());
        	String Account="";
        	try{
        		Subject currentUser = SecurityUtils.getSubject();//获取当前用户
             	 Account=currentUser.getPrincipal().toString();//当前用户的账号
        	}catch(Exception e){
        		
        	}
        	log.setUser(Account);
        	log.setResourceName(name);
        	log.setResourceType(type);
        	log.setOperateDetail(operateDetail);
        	addServiceImpl.addLog(log);
    	}
    	
    }
}
