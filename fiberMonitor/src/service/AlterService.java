package service;

import java.io.Serializable;
import java.util.Map;

import domain.Alarm;
import domain.AlarmBank;
import domain.Areas;
import domain.Cabinets;
import domain.Curves;
import domain.Distributing_frames;
import domain.Duty_operator;
import domain.Duty_schedule;
import domain.Event_curves;
import domain.Fiber_cores;
import domain.Frame_ports;
import domain.Jumper_backups;
import domain.Jumper_frames;
import domain.Jumper_routes;
import domain.Landmarks;
import domain.Log;
import domain.Optical_cables;
import domain.Optimize_parameters;
import domain.Period_parameters;
import domain.Permissions;
import domain.Protect_groups;
import domain.Racks;
import domain.Role;
import domain.Routes;
import domain.Rtu_ports;
import domain.Rtus;
import domain.SerialCode;
import domain.Stations;
import domain.SystemInfo;
import domain.Threshold;
import domain.User;
import domain.Verify_codes;

public interface AlterService 
{
	
/**修改区域**/
//修改区域(Areas)
	boolean alterArea(Areas area);
//根据字段修改区域
	boolean updateArea(Serializable id,Map<String , Object> parameterMap);
/************/
	
/**修改局站**/
//修改局站(Stations)
	boolean alterStation(Stations station);
//根据字段修改局站
	boolean updateStation(Serializable id,Map<String , Object> parameterMap);	
/***********/
	
/**修改地标**/
//修改地标(Landmarks)
	boolean alterLandmark(Landmarks landmark);
//根据字段修改地标
	boolean updateLandmark(Serializable id,Map<String , Object> parameterMap);
/*************/
	
/**修改机架**/
//修改机柜(Cabinets)
	boolean alterCabinet(Cabinets cabinet);
//根据字段修改机柜
	boolean updateCabinet(Serializable id,Map<String , Object> parameterMap);	
/***********/
	
/**修改机架**/
//修改机架(Racks)
	boolean alterRack(Racks rack);
//根据字段修改机架
	boolean updateRack(Serializable id,Map<String , Object> parameterMap);		
/***********/
	
/**修改配线架**/
//修改配线架(Distributing_frames)
	boolean alterDistributingFrame(Distributing_frames distributing_frame);
//根据字段修改配线架
	boolean updateDistributingFrame(Serializable id,Map<String , Object> parameterMap);
/**************/
	
/**修改配线架端口**/
//修改配线架端口(Frame_ports)
	boolean alterFramePort(Frame_ports frame_port);
//根据字段修改配线架端口
	boolean updateFramePort(Serializable id,Map<String , Object> parameterMap);
/*****************/
	
/**修改RTU**/
//修改RTU(Rtus)
	boolean alterRtu(Rtus rtu);
//根据字段修改RTU
	boolean updateRtu(Serializable id,Map<String , Object> parameterMap);

/**************/	
/**修改RTU端口**/
//修改RTU端口(Rtu_ports)
	boolean alterRtuPort(Rtu_ports rtu_port);
//根据字段修改RTU端口
	boolean updateRtuPort(Serializable id,Map<String , Object> parameterMap);
/**************/
	
/**修改光缆**/
//修改光缆(Optical_cables)
	boolean alterOpticalCable(Optical_cables optical_cable);
//根据字段修改光缆
	boolean updateOpticalCable(Serializable id,Map<String , Object> parameterMap);	
/*************/

/**修改纤芯**/
//修改纤芯(Fiber_cores)
	boolean alterFiberCore(Fiber_cores fiber_core);
//根据字段修改光缆
	boolean updateFiberCore(Serializable id,Map<String , Object> parameterMap);	
/************/

/**修改配线架跳纤**/
//修改配线架跳纤(Jumper_frames)
	boolean alterJumperFrame(Jumper_frames jumper_frame);
//根据字段修改配线架跳纤(Jumper_frames)
	boolean updateJumperFrame(Serializable id,Map<String , Object> parameterMap);
/*****************/
	
/**修改光路跳纤**/
//修改光路跳纤(Jumper_routes)
	boolean alterJumperRoute(Jumper_routes jumper_route);
//根据字段修改光路跳纤(Jumper_routes)
	boolean updateJumperRoute(Serializable id,Map<String , Object> parameterMap);
	/***修改备纤光源跳纤**/
	public boolean alterJumperBackup(Jumper_backups jumper_backup);	
/*************/
	
/**修改光路**/
//修改光路(Routes)
	boolean alterRoute(Routes route);
//根据字段修改光路(Routes route)
	boolean updateRoute(Serializable id,Map<String , Object> parameterMap);
/***********/
	
/**修改优化参数**/
//修改优化参数(Optimize_parameters)
	boolean alterOptimizeParameter(Optimize_parameters optimize_parameter);
//根据字段修改优化参数
	boolean updateOptimizeParameter(Serializable id,Map<String , Object> parameterMap);
/****************/

/**修改周期测试参数据**/
//修改周期测试参数(Period_parametrs)
	boolean alterPeriodParameter(Period_parameters period_parameter);
//根据字段修改优化参数
	boolean updatePeriodParameter(Serializable id,Map<String , Object> parameterMap);
/*******************/
	
/**修改回传事件**/
//修改回传事件(Event_curves)
	boolean alterEventCurve(Event_curves event_curve);
//根据字段修改回传事件
	boolean updateEventCurve(Serializable id,Map<String , Object> parameterMap);
/*****************/
	
/**修改回传曲线**/
//修改回传曲线(Curves)
	boolean alterCurve(Curves curve);
//根据字段修改回传曲线
	boolean updateCurve(Serializable id,Map<String , Object> parameterMap);
	
/*****************/
	/**修改告警组暂存记录**/	
	boolean updateTempGroupInfo(Serializable id,Map<String , Object> parameterMap);
	
/*****************/
/**修改告警组**/	
boolean updatePriorities(Serializable id,Map<String , Object> parameterMap);	

/*****************/
/**修改告警记录**/	
public  boolean updateAlarm(Serializable id,Map<String , Object> parameterMap);	
public boolean alterAlarm(Alarm alert);	
	
/****/
//修改角色
public boolean alterRole(Role role);
	
/****/
//修改权限
public boolean alterPermission(Permissions permission);
/****/
//修改用户
public boolean alterUser(User user);	
	
//修改门限
public boolean alterThreshold(Threshold threshold);	
	
//修改值班表	
public boolean alterDutySchedule(Duty_schedule duty_schedule);
	
//修改值班人员
public boolean alterDutyOperator(Duty_operator duty_operator);		
//修改验证码
public boolean alterVerifyCode(Verify_codes verifyCode);
/****/
//修改日志
public boolean alterLog(Log log);	
/**修改光保护组**/

public boolean alterProtectGroup(Protect_groups protectGroup);	
	
public boolean alterSystemInfo(SystemInfo systemInfo);	
	
/**修改告警经验信息**/
public boolean alterAlarmBank(AlarmBank alarmBank);	
//修改序列号
public boolean alterSNCode(SerialCode sNCode);	
}
