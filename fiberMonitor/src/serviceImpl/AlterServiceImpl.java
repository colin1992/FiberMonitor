package serviceImpl;

import java.io.Serializable;
import java.util.Map;

import dao.*;
import domain.*;
import service.AlterService;

public class AlterServiceImpl implements AlterService
{
	private 	AreaDao 				areaDao;				//01.注入对区域的操作
	private 	StationDao 				stationDao;				//02.注入对局站的操作
	private 	CabinetDao 				cabinetDao;				//03.注入对机柜的操作
	private 	RackDao 				rackDao;				//04.注入对机架的操作
	private     FrameDao				frameDao;				//05.注入对配线架的操作
	private		FramePortDao			framePortDao;			//06.注入对配线架端口的操作
	private 	RtuDao					rtuDao;					//07.注入对RTU的操作
	private		RtuPortDao				rtuPortDao;				//08.注入对RTU端口的操作
	private     OpticalCableDao 		opticalCableDao;		//11.注入对光缆的操作
	private		FiberCoreDao			fiberCoreDao;			//12.注入对纤芯的操作
	private     JumperFrameDao     		jumperFrameDao;			//13.注入对配线架跳纤的操作
	private     JumperRouteDao     	 	jumperRouteDao;			//14.注入对光路跳纤的操作
	private     PreparatoryRouteDao 	preparatoryRouteDao;	//15.注入对预备光路的操作
	private		LandmarkDao				landmarkDao;			//16.注入对地标的操作
	private 	RouteDao				routeDao;				//17.注入对光路的操作
	private 	OptimizeParameterDao 	optimizeParameterDao;	//18.注入对优化参数的操作
	private		PeriodParameterDao		periodParameterDao;		//19.注入对周期测试参数的操作
	private     CurveDao				curveDao;				//20.注入对回传曲线的操作
	@SuppressWarnings("unused")
	private 	ParameterCurveDao		parameterCurveDao;		//21.注入对回传测试参数的操作
	private		EventCurveDao			eventCurveDao;			//22.注入对回传事件的操作
	@SuppressWarnings("unused")
	private		TopologicRouteDao		topologicRouteDao;		//23.注入对光路拓扑图-线的操作
	@SuppressWarnings("unused")
	private		TopologicPointDao		topologicPointDao;		//24.注入对光路拓扑图-站点的操作
	private		PriorityDao		        priorityDao;		    //25.注入对告警组的操作
	private		TempGroupInfoDao	    tempGroupInfoDao;	    //26.注入对暂存告警组的操作
	@SuppressWarnings("unused")
	private		AlarmWayDao	            alarmWayDao;	        //27.注入对告警方法的操作
	private		ProtectGroupDao	        protectGroupDao;	    //28.注入对保护组的操作
	@SuppressWarnings("unused")
	private     OpticalPowerDao         opticalPowerDao;        //29.注入对光功率数据的操作
	private     AlarmDao                alarmDao;               //30.注入对告警信息的操作 
	private     RoleDao                 roleDao;                //31.注入对角色的操作      
	private     PermissionDao           permissionDao;          //32.注入对权限的操作
	private     UserDao           		userDao;          		//33.注入对用户的操作 
	private     ThresholdDao           	thresholdDao;          	//34.注入对门限的操作 
	private     DutyScheduleDao         dutyScheduleDao;        //35.注入对值班表的操作
	private     DutyOperatorDao         dutyOperatorDao;        //36.注入对值班人员的操作
	private     VerifyCodeDao           verifyCodeDao;          //37.注入对验证码的操作  
	private     LogDao                  logDao;                 //38.注入对日志的操作
	private     SystemDao               systemDao;              //39.注入对系统信息的操作
	private     JumperBackupDao     	jumperBackupDao;		//40.注入对备纤光源跳纤的操作
	private     AlarmBankDao     	    alarmBankDao;		    //41.注入对告警经验库的操作
	private     SerialCodeDao     	    serialCodeDao;		    //42.注入对注册码的操作

	public void setAreaDao(AreaDao areaDao)
	{
		this.areaDao = areaDao;
	}
	public void setStationDao(StationDao stationDao)
	{
		this.stationDao = stationDao;
	}
	public void setCabinetDao(CabinetDao cabinetDao)
	{
		this.cabinetDao = cabinetDao;
	}
	public void setRackDao(RackDao rackDao)
	{
		this.rackDao = rackDao;
	}
	public void setFrameDao(FrameDao frameDao)
	{
		this.frameDao = frameDao;
	}
	public void setFramePortDao(FramePortDao framePortDao)
	{
		this.framePortDao = framePortDao;
	}
	public void setRtuDao(RtuDao rtuDao)
	{
		this.rtuDao = rtuDao;
	}
	public void setRtuPortDao(RtuPortDao rtuPortDao)
	{
		this.rtuPortDao = rtuPortDao;
	}
	public void setOpticalCableDao(OpticalCableDao opticalCableDao)
	{
		this.opticalCableDao = opticalCableDao;
	}
	public void setFiberCoreDao(FiberCoreDao fiberCoreDao)
	{
		this.fiberCoreDao = fiberCoreDao;
	}
	public void setJumperFrameDao(JumperFrameDao jumperFrameDao)
	{
		this.jumperFrameDao = jumperFrameDao;
	}
	public void setJumperRouteDao(JumperRouteDao jumperRouteDao)
	{
		this.jumperRouteDao = jumperRouteDao;
	}
	public void setPreparatoryRouteDao(PreparatoryRouteDao preparatoryRouteDao)
	{
		this.preparatoryRouteDao = preparatoryRouteDao;
	}
	public void setLandmarkDao(LandmarkDao landmarkDao)
	{
		this.landmarkDao = landmarkDao;
	}
	public void setRouteDao(RouteDao routeDao)
	{
		this.routeDao = routeDao;
	}
	public void setOptimizeParameterDao(OptimizeParameterDao optimizeParameterDao)
	{
		this.optimizeParameterDao = optimizeParameterDao;
	}
	public void setPeriodParameterDao(PeriodParameterDao periodParameterDao)
	{
		this.periodParameterDao = periodParameterDao;
	}
	public void setCurveDao(CurveDao curveDao)
	{
		this.curveDao = curveDao;
	}
	public void setParameterCurveDao(ParameterCurveDao parameterCurveDao)
	{
		this.parameterCurveDao = parameterCurveDao;
	}
	public void setEventCurveDao(EventCurveDao eventCurveDao)
	{
		this.eventCurveDao = eventCurveDao;
	}
	public void setTopologicRouteDao(TopologicRouteDao topologicRouteDao)
	{
		this.topologicRouteDao = topologicRouteDao;
	}
	public void setTopologicPointDao(TopologicPointDao topologicPointDao)
	{
		this.topologicPointDao = topologicPointDao;
	}
	public void setPriorityDao(PriorityDao priorityDao)
	{
		this.priorityDao = priorityDao;
	}
	public void setTempGroupInfoDao(TempGroupInfoDao tempGroupInfoDao)
	{
		this.tempGroupInfoDao = tempGroupInfoDao;
	}
	public void setAlarmWayDao(AlarmWayDao alarmWayDao)
	{
		this.alarmWayDao = alarmWayDao;
	}
	public void setProtectGroupDao(ProtectGroupDao protectGroupDao){
		this.protectGroupDao=protectGroupDao;
	}
	public void setOpticalPowerDao(OpticalPowerDao opticalPowerDao){
		this.opticalPowerDao=opticalPowerDao;
	}
	public void setAlarmDao(AlarmDao alarmDao){
		this.alarmDao=alarmDao;
	}
	public void setRoleDao(RoleDao roleDao){
		this.roleDao=roleDao;
	}
	public void setPermissionDao(PermissionDao permissionDao){
		this.permissionDao=permissionDao;
	}
	public void setUserDao(UserDao userDao){
		this.userDao=userDao;
	}
	public void setThresholdDao(ThresholdDao thresholdDao){
		this.thresholdDao=thresholdDao;
	}
	public void setDutyScheduleDao(DutyScheduleDao dutySchedule){
		this.dutyScheduleDao=dutySchedule;
	}
	public void setDutyOperatorDao(DutyOperatorDao dutyOperatorDao){
		this.dutyOperatorDao=dutyOperatorDao;
	}
	public void setVerifyCodeDao(VerifyCodeDao verifyCodeDao){
		this.verifyCodeDao=verifyCodeDao;
	}  
	public void setLogDao(LogDao logDao){
		this.logDao = logDao;
	}
	public void setSystemDao(SystemDao systemDao){
		this.systemDao = systemDao;
	}
	public void setJumperBackupDao(JumperBackupDao jumperBackupDao){
		this.jumperBackupDao=jumperBackupDao;
	}
	public void setAlarmBankDao(AlarmBankDao alarmBankDao){
		this.alarmBankDao=alarmBankDao;
	}
	public void setSerialCodeDao(SerialCodeDao serialCodeDao){
		this.serialCodeDao=serialCodeDao;
	}
/**修改区域**/
//修改区域(Areas)
	public boolean alterArea(Areas area){
		try{
			areaDao.alterEntity(area);
			return true;
		}
		catch(Exception e){
			return false;
		}

	}
//根据字段修改区域(Areas)
	public boolean updateArea(Serializable id,Map<String , Object> parameterMap)
	{
		try
		{
			areaDao.updateEntity(Areas.class, id, parameterMap);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
/*************************/
/**修改局站**/
//修改局站(Stations)
	public boolean alterStation(Stations station)
	{
		try
		{
			stationDao.alterEntity(station);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
//根据字段修改局站(Stations)
	public boolean updateStation(Serializable id,Map<String , Object> parameterMap)
	{
		try
		{
			stationDao.updateEntity(Stations.class, id, parameterMap);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
/*************************/	
/**修改地标**/
//修改地标(Landmarks)
	public boolean alterLandmark(Landmarks landmark)
	{
		try
		{
			landmarkDao.alterEntity(landmark);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
//根据字段修改地标(Landmarks)
	public boolean updateLandmark(Serializable id,Map<String , Object> parameterMap)
	{
		try
		{
			landmarkDao.updateEntity(Landmarks.class, id, parameterMap);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}	
/*************************/	
/**修改机柜**/
//修改机柜(Cabinets)
	public boolean alterCabinet(Cabinets cabinet)
	{
		try
		{
			cabinetDao.alterEntity(cabinet);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
//根据字段修改机柜(Cabinets)
	public boolean updateCabinet(Serializable id,Map<String , Object> parameterMap)
	{
		try
		{
			cabinetDao.updateEntity(Cabinets.class, id, parameterMap);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}	
/***********/	
/**修改机架**/
//修改机架(Racks)
	public boolean alterRack(Racks rack)
	{
		try
		{
			rackDao.alterEntity(rack);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
//根据字段修改机架(Racks)
	public boolean updateRack(Serializable id,Map<String , Object> parameterMap)
	{
		try
		{
			rackDao.updateEntity(Racks.class, id, parameterMap);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}		
/******************/
/**修改配线架**/
//修改配线架(Distributing_frames)
	public boolean alterDistributingFrame(Distributing_frames distributing_frame)
	{
		try
		{
			frameDao.alterEntity(distributing_frame);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
//根据字段修改配线架(Distributing_frames)
	public boolean updateDistributingFrame(Serializable id,Map<String , Object> parameterMap)
	{
		try
		{
			frameDao.updateEntity(Distributing_frames.class, id, parameterMap);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}		
/**************/
/**修改配线架端口**/
//修改配线架端口(Frame_ports)
	public boolean alterFramePort(Frame_ports frame_port)
	{
		try
		{
			framePortDao.alterEntity(frame_port);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
//根据字段修改配线架端口(Frame_ports)
	public boolean updateFramePort(Serializable id,Map<String , Object> parameterMap)
	{
		try
		{
			framePortDao.updateEntity(Frame_ports.class, id, parameterMap);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
/*****************/
/**修改RTU**/
//修改RTU(Rtus)
	public boolean alterRtu(Rtus rtu)
	{
		try
		{
			rtuDao.alterEntity(rtu);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
//根据字段修改RTU(Rtus)
	public boolean updateRtu(Serializable id,Map<String , Object> parameterMap)
	{
		try
		{
			rtuDao.updateEntity(Rtus.class, id, parameterMap);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

/**************/
	
/**修改RTU端口**/
//修改RTU端口(Rtu_ports)
	public boolean alterRtuPort(Rtu_ports rtu_port)
	{
		try
		{
			rtuPortDao.alterEntity(rtu_port);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
//根据字段修改RTU端口(Rtu_ports)
	public boolean updateRtuPort(Serializable id,Map<String , Object> parameterMap)
	{
		try
		{
			rtuPortDao.updateEntity(Rtu_ports.class, id, parameterMap);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
/*****************/
	
/**修改光缆**/	
//修改光缆(Optical_cables)
	public boolean alterOpticalCable(Optical_cables optical_cable)
	{
		try
		{
			opticalCableDao.alterEntity(optical_cable);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
//根据字段修改光缆(Optical_cables)
	public boolean updateOpticalCable(Serializable id,Map<String , Object> parameterMap)
	{
		try
		{
			opticalCableDao.updateEntity(Optical_cables.class, id, parameterMap);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
/***********/
	
/**修改纤芯**/
//修改纤芯(Fiber_cores)
	public boolean alterFiberCore(Fiber_cores fiber_core)
	{
		try
		{
			fiberCoreDao.alterEntity(fiber_core);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
//根据字段修改纤芯(Fiber_cores)
	public boolean updateFiberCore(Serializable id,Map<String , Object> parameterMap)
	{
		try
		{
			fiberCoreDao.updateEntity(Fiber_cores.class, id, parameterMap);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
/***************/
	
/**修改配线架跳纤**/
//修改配线架跳纤(Jumper_frames)
	public boolean alterJumperFrame(Jumper_frames jumper_frame)
	{
		try
		{
			jumperFrameDao.alterEntity(jumper_frame);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
//根据字段修改配线架跳纤(Jumper_frames)
	public boolean updateJumperFrame(Serializable id,Map<String , Object> parameterMap)
	{
		try
		{
			jumperFrameDao.updateEntity(Jumper_frames.class, id, parameterMap);
			
			//如果该配线架跳纤重新指定了配线架a端口，则更新a端口状态
			String frame_a_name = (String) parameterMap.get("frame_a_name");
			if(frame_a_name != null)
			{
				Long frame_a_id = (Long) parameterMap.get("frame_a_id");
				int port_order_a = (int) parameterMap.get("port_order_a");
				Frame_ports frame_port = framePortDao.findByFrameIdAndPortId(frame_a_id, port_order_a);
				frame_port.setConnection_type("配线架跳纤");
				frame_port.setConnection_id(frame_a_id);
				frame_port.setStatus(true);
				framePortDao.alterEntity(frame_port);
			}
			//如果该配线架跳纤重新指定了配线架z端口，则更新z端口状态
			String frame_z_name = (String) parameterMap.get("frame_z_name");
			if(frame_z_name != null)
			{
				Long frame_z_id = (Long) parameterMap.get("frame_z_id");
				int port_order_z = (int) parameterMap.get("port_order_z");
				Frame_ports frame_port = framePortDao.findByFrameIdAndPortId(frame_z_id, port_order_z);
				frame_port.setConnection_type("配线架跳纤");
				frame_port.setConnection_id(frame_z_id);
				frame_port.setStatus(true);
				framePortDao.alterEntity(frame_port);
			}
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
/*************/
	
/**修改光路跳纤**/
//修改光路跳纤(Jumper_routes)
	public boolean alterJumperRoute(Jumper_routes jumper_route)
	{
		try
		{
			jumperRouteDao.alterEntity(jumper_route);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
//根据字段修改光路跳纤(Jumper_routes)
	public boolean updateJumperRoute(Serializable id,Map<String , Object> parameterMap)
	{
		try
		{
			jumperRouteDao.updateEntity(Jumper_routes.class, id, parameterMap);
			//如果为该光路跳纤重新指定了RTU端口(应该是一个全新的RTU的端口)，则需要更新RTU端口状态
			String rtu_name = (String) parameterMap.get("rtu_name");
			if(rtu_name != null)
			{
				Long rtu_id = (Long) parameterMap.get("rtu_id");
				int otdr_port_order = (int) parameterMap.get("otdr_port_order");
				//找到该新RTU的端口
				Rtu_ports rtu_port = rtuPortDao.findByRtuIdAndModelOrderAndPortOrder(rtu_id,otdr_port_order/8+1, (otdr_port_order-1)%8+1);
				rtu_port.setJumper_route_id((Long)id);//更新连接的光路跳纤标识
				rtu_port.setStatus(true);//更新端口状态，占用
				rtuPortDao.alterEntity(rtu_port);//更新端口
			}
			//如果为该光路跳纤重新指定了配线架端口，则需要更新配线架端口状态
			String frame_name = (String) parameterMap.get("frame_name");
			if(frame_name != null)
			{
				Long frame_id = (Long) parameterMap.get("frame_id");
				int frame_port_order = (int) parameterMap.get("frame_port_order");
				//找到该新配线架的端口
				Frame_ports frame_port = framePortDao.findByFrameIdAndPortId(frame_id, frame_port_order);
				frame_port.setConnection_type("光路跳纤");
				frame_port.setConnection_id((Long)id);
				frame_port.setStatus(true);
				framePortDao.alterEntity(frame_port);
			}
			//判断该光路跳纤是否有对应的预备光路,如果有，则说明只是简单修改光路跳纤的基本信息
			@SuppressWarnings("unused")
			Preparatory_routes preparatory_route = preparatoryRouteDao.findByJumperRouteId(id);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
/***修改备纤光源跳纤**/
	public boolean alterJumperBackup(Jumper_backups jumper_backup)
	{
		try
		{
			jumperBackupDao.alterEntity(jumper_backup);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
/********************/
	
/**修改光路**/	
//修改光路
	public boolean alterRoute(Routes route)
	{
		try
		{
			routeDao.alterEntity(route);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
//根据字段修改光路(Routes)
	public boolean updateRoute(Serializable id,Map<String , Object> parameterMap)
	{
		try
		{
			routeDao.updateEntity(Routes.class, id, parameterMap);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
/***************/
	
/**修改优化参数**/
//修改优化参数(Optimize_parameters)
	public boolean alterOptimizeParameter(Optimize_parameters optimize_parameter)
	{
		try
		{
			optimizeParameterDao.alterEntity(optimize_parameter);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
//根据字段修改优化参数(Optimize_parameters)
	public boolean updateOptimizeParameter(Serializable id,Map<String , Object> parameterMap)
	{
		try
		{
			optimizeParameterDao.updateEntity(Optimize_parameters.class, id, parameterMap);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
/*************/

/**修改周期测试参数**/
//修改周期测试参数(Period_parametrs)
	public boolean alterPeriodParameter(Period_parameters period_parameter)
	{
		try
		{
			periodParameterDao.alterEntity(period_parameter);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
//根据字段修改周期参数(Period_parameters)
	public boolean updatePeriodParameter(Serializable id,Map<String , Object> parameterMap)
	{
		try
		{
			periodParameterDao.updateEntity(Period_parameters.class, id, parameterMap);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
/********************/
	
/**修改回传事件**/
//修改回传事件(Event_curves)
	public boolean alterEventCurve(Event_curves event_curve)
	{
		try
		{
			eventCurveDao.alterEntity(event_curve);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
//根据字段修改回传事件(Event_curves)
	public boolean updateEventCurve(Serializable id,Map<String , Object> parameterMap)
	{
		try
		{
			eventCurveDao.updateEntity(Event_curves.class, id, parameterMap);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
/*****************/
	
/**修改回传曲线**/	
//修改回传曲线(Curves)
	public boolean alterCurve(Curves curve)
	{
		try
		{
			curveDao.alterEntity(curve);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
//根据字段修改回传曲线(Curves)
	public boolean updateCurve(Serializable id,Map<String , Object> parameterMap)
	{
		try
		{
			curveDao.updateEntity(Curves.class, id, parameterMap);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
/**修改告警组暂存记录**/	
public boolean updateTempGroupInfo(Serializable id,Map<String , Object> parameterMap){
	try
	{
		tempGroupInfoDao.updateEntity(TempGroupInfo.class, id, parameterMap);
		return true;
	}
	catch(Exception e)
	{
		return false;
	}
}	

/*****************/
/**修改告警组**/	
public  boolean updatePriorities(Serializable id,Map<String , Object> parameterMap){
	try
	{
		priorityDao.updateEntity(Priorities.class, id, parameterMap);
		return true;
	}
	catch(Exception e)
	{
		return false;
	}
}
/***************/
/*-----修改告警记录---*/
public  boolean updateAlarm(Serializable id,Map<String , Object> parameterMap){
	try
	{
		alarmDao.updateEntity(Alarm.class, id, parameterMap);
		return true;
	}
	catch(Exception e)
	{
		return false;
	}
}
public boolean alterAlarm(Alarm alert)
{
	try
	{
		alarmDao.alterEntity(alert);
		return true;
	}
	catch(Exception e)
	{
		return false;
	}
}
/**修改角色**/
public boolean alterRole(Role role)
{
	try
	{
		roleDao.alterEntity(role);
		return true;
	}
	catch(Exception e)
	{
		return false;
	}
}
/*********/
//修改权限
public boolean alterPermission(Permissions permission)
{
	try
	{
		permissionDao.alterEntity(permission);
		return true;
	}
	catch(Exception e)
	{
		return false;
	}
}
/****/
//修改用户
public boolean alterUser(User user)
{
	try
	{
		userDao.alterEntity(user);
		return true;
	}
	catch(Exception e)
	{
		return false;
	}
}
/****/
//修改门限
public boolean alterThreshold(Threshold threshold)
{
	try
	{
		thresholdDao.alterEntity(threshold);
		return true;
	}
	catch(Exception e)
	{
		return false;
	}
}
/***/
//修改值班表
public boolean alterDutySchedule(Duty_schedule duty_schedule)
{
	try
	{
		dutyScheduleDao.alterEntity(duty_schedule);
		return true;
	}
	catch(Exception e)
	{
		return false;
	}
}
/****/
//修改值班人员
public boolean alterDutyOperator(Duty_operator duty_operator)
{
	try
	{
		dutyOperatorDao.alterEntity(duty_operator);
		return true;
	}
	catch(Exception e)
	{
		return false;
	}
}
/**验证码**/
public boolean alterVerifyCode(Verify_codes verifyCode)
{
	try
	{
		verifyCodeDao.alterEntity(verifyCode);
		return true;
	}
	catch(Exception e)
	{   
		 e.printStackTrace();
		return false;
	}
}
/****/
//修改日志
public boolean alterLog(Log log)
{
	try
	{
		logDao.alterEntity(log);
		return true;
	}
	catch(Exception e)
	{
		return false;
	}
}
/**修改光保护组**/

public boolean alterProtectGroup(Protect_groups protectGroup)
{
	try
	{
		protectGroupDao.alterEntity(protectGroup);
		return true;
	}
	catch(Exception e)
	{
		return false;
	}
}
/**修改系统信息**/
public boolean alterSystemInfo(SystemInfo systemInfo)
{
	try
	{
		systemDao.alterEntity(systemInfo);
		return true;
	}
	catch(Exception e)
	{
		return false;
	}
}
/**修改告警经验信息**/
public boolean alterAlarmBank(AlarmBank alarmBank)
{
	try
	{
		alarmBankDao.alterEntity(alarmBank);
		return true;
	}
	catch(Exception e)
	{
		return false;
	}
}
//修改序列号
public boolean alterSNCode(SerialCode sNCode){
	try{
		serialCodeDao.alterEntity(sNCode);
		return true;
	}
	catch(Exception e){
		return false;
	}	   
  }
}