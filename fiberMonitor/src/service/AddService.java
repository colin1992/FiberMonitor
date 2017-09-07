package service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import domain.Alarm;
import domain.AlarmBank;
import domain.Alarm_ways;
import domain.Areas;
import domain.Cabinets;
import domain.Curves;
import domain.Distributing_frames;
import domain.Duty_operator;
import domain.Duty_schedule;
import domain.Event_curves;
import domain.Jumper_backups;
import domain.Jumper_frames;
import domain.Jumper_routes;
import domain.Landmarks;
import domain.Log;
import domain.Optical_cables;
import domain.Optical_powers;
import domain.Optimize_parameters;
import domain.Parameter_curves;
import domain.Period_parameters;
import domain.Permissions;
import domain.Priorities;
import domain.Protect_groups;
import domain.Role;
import domain.Routes;
import domain.Rtu_ports;
import domain.Rtus;
import domain.SerialCode;
import domain.Stations;
import domain.SystemInfo;
import domain.TempGroupInfo;
import domain.Threshold;
import domain.User;
import domain.Verify_codes;
/**
 * 数据库资源增加接口类
 * **/
public interface AddService
{
//新增区域(Areas)
	Serializable addArea(Areas area);
	
//新增局站(Stations)
	Serializable addStation(Stations station);
	
//新增机柜(Cabinets)
	Serializable addCabinet(Cabinets cabinet);
	
//新增配线架(Distributing_frames)
	Serializable addDistributingFrame(Distributing_frames frame);
	
//新增RTU(Rtus)
	Serializable addRtu(Rtus rtu);	

//新增rtu端口
	Serializable addRtuPort(Rtu_ports rtu_port);
	
//新增光缆(Optical_cables)
	Serializable addOpticalCable(Optical_cables optical_cable);
	
//新增配线架跳纤(Jumper_frames)
	Serializable addJumperFrame(Jumper_frames jumper_frame);
	
//新增光路跳纤(Jumper_routes)
	Serializable addJumperRoute(Jumper_routes jumper_route);
	
//新增切换跳纤
	Serializable addJumperSwitch(Jumper_routes jumper_route);
//新增备纤光源跳纤
	public Serializable addJumperBackup(Jumper_backups jumper_backup);
	
//新增光路拓扑图(Routes,传入光路拓扑图：局站id)
	List<Map<String,Object>> addTopology(List<Serializable> ids);
	
//新增光路(Routes)
	Serializable addRoute(Routes route,Map<String,Object> topologies);
	
//新增地标(Landmarks)
	Serializable addLandmark(Landmarks landmark);
	
//新增优化测试参数(Optimize_parameters)
	Serializable addOptimizeParameter(Optimize_parameters optimize_parameter);
	
//新增周期测试参数(Period_parameters)
	Serializable addPeriodParameter(Period_parameters period_parameter);
	
//新增回传曲线(Curves)
	Serializable addCurve(Curves curve);
	
//新增回传测试参数(Parameter_curves)
	Serializable addParameterCurve(Parameter_curves parameter_curve);
	
//新增回传事件(Event_curves)
	Serializable addEventCurve(Event_curves event_curve);
	
//新增纤芯A端和配线架端口的连接关系
	boolean addConnectionTerminalAFiberCoreAndFramePort(List<Serializable> core_,Serializable frame_id,List<Integer> frame_int);

//新增纤芯Z端和配线架端口的连接关系
	boolean addConnectionTerminalZFiberCoreAndFramePort(List<Serializable> core_,Serializable frame_id,List<Integer> frame_int);	
//新增告警组
	Serializable addPriorities(Priorities priority);
//新增暂存分组记录	
	Serializable addTempGroupInfo(TempGroupInfo tempGroupInfo);
//新增告警方式
	Serializable addAlarmWay(Alarm_ways alarmWay);
//新增配对组	
	Serializable addProtectGroup(Protect_groups protect_group);
//新增光功率数据记录
	public Serializable addOpticalPower(Optical_powers optical_power );
//新增告警记录
	public Serializable addAlarm(Alarm alert);
//新增角色
	public Serializable addRole(Role role);
//新增权限
	public Serializable addPermission(Permissions permission);
//新增用户	
	public Serializable addUser(User user);	
//新增门限
	public Serializable addThreshold(Threshold threshold);	
//新增值班信息
	public Serializable addDutySchedule(Duty_schedule duty_schedule);
//新增值班人员
	public Serializable addDutyOperator(Duty_operator duty_operator);
//新增验证码	
	public Serializable addVerifyCode(Verify_codes verifyCode);
//新增日志
   public Serializable addLog(Log log);
/**新增系统记录**/
   public Serializable addSystem(SystemInfo system);
/**新增告警经验**/	
   public Serializable addAlarmBank(AlarmBank alarmBank); 
//增加序列号
	public Serializable addSNCode(SerialCode sNCode);   
}