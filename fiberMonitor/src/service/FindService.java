package service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import domain.Alarm_ways;
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
import domain.Optical_powers;
import domain.Optimize_parameters;
import domain.Parameter_curves;
import domain.Period_parameters;
import domain.Permissions;
import domain.Preparatory_routes;
import domain.Priorities;
import domain.Protect_groups;
import domain.Racks;
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

public interface FindService {
/**查询区域**/	
//根据区域标识(Areas-ID)查询区域
	Areas findArea(Serializable id);
//查询所有的区域
	public List<Areas> findAllAreas();
/************/
	
/**查询局站**/
//根据局站标识(Stations-ID)查询局站
	Stations findStation(Serializable id);
//根据区域(Areas)ID查询局站
	List<Stations> findStationsByAreaId(Serializable id);
//多条件查询局站
	List<Stations> findStationsByMulti(Map<String,Object> parameters); 
//查询所有局站
	List<Stations> findAllStations();
/************/

/**查询地标**/
//根据地标标识(Landmarks-ID)查询地标
	Landmarks findLandmark(Serializable id);
//根据光缆id获取该光缆的全部光缆地标
	List<Landmarks> findLandmarksByOpticalCableId(Serializable id);
//多条件查询
	List<Landmarks> findLandmarksByMulti(Map<String,Object> parameters);
//查询所有地标
	List<Landmarks> findAllLandmarks();
/**********/

/**查询机柜**/
//根据机柜标识(Cabinets-ID)查询机柜
	Cabinets findCabinet(Serializable id);
//多条件查询
	List<Cabinets> findCabinetsByMulti(Map<String,Object> parameters);
//查询所有机柜
	List<Cabinets> findAllCabinets();
//根据配线架Id查询机柜
	Cabinets findCabinetByFrameId(Serializable id);
/***********/
	
/**查询机架**/
//根据机架标识(Racks-ID)查询机架
	Racks findRack(Serializable id);
//根据机柜Id和机架序号查询机架
	Racks findRackByCabinetIdAndRackOrder(Serializable id, int rack_order);
//根据局站Id(Stations-ID)查询机架
	List<Racks> findRacksByStationId(Serializable id);
/**************/
	
/**查询配线架**/
//根据配线架标识(Distributing_frames-ID)查询配线架
	Distributing_frames findDistributingFrame(Serializable id);
//根据局站Id获取该局站下的配线架
	List<Distributing_frames> findDistributingFramesByStationId(Serializable id);
//查找所有frame
	List<Distributing_frames> findAllFrames();	
//多条件查询
		List<Distributing_frames> findDistributingFramesByMulti(Map<String,Object> parameters);
/*************/
	
/**查询配线架端口**/
//根据配线架端口标识(Frame_ports-ID)查询配线架端口
	Frame_ports findFramePort(Serializable id);
//根据配线架ID查询该配线架上的所有端口
	List<Frame_ports> findFramePortsAllByFrameId(Serializable id);
//根据配线架ID和端口序号查询该配线架上的端口
	Frame_ports findFramePortByFrameIdAndPortId(Serializable id,int port_order);
//
	public Frame_ports findFramePortByFrameIdAndPortOrder(Serializable id,int port_order);
//多条件查询配线架端口
	List<Frame_ports> findFramePortByMulti(Map<String,Object> parameters);
/****************/

/**查询RTU**/
//根据RTU标识(Rtus-ID)查询RTU
	Rtus findRtu(Serializable id);
//查询所有RTU
	List<Rtus> findAllRtus();
//根据局站Id查询RTU
	List<Rtus> findRtusByStationId(Serializable id);
//多条件查询
	List<Rtus> findRtusByMulti(Map<String,Object> parameters);
	
	/****************/
	/**查询RTU**/
	//根据rtu标识获取模块数量
		public int findModelNumByRtuId(Serializable id);
	//根据rtu标识和模块号获取模块光端口的使用数量
		public int findModelUsedPortNumByRtuIdAndModelOrder(Serializable id, int modelOrder);

/***********/
	
/**查询RTU端口**/
//根据RTU端口标识(Rtu_ports-ID)查询RTU端口
	Rtu_ports findRtuPort(Serializable id);
//根据RTU标识(RTU-ID)查询对应的OTDR端口数量	
	int findOtdrAmountByRtuId(Serializable id);
//根据RTU-ID查询该RTU上的所有OTDR端口
	List<Rtu_ports> findRtuPortsAllByRtuId(Serializable id);
//根据RTU-ID和model序号和端口序号查询OTDR端口
	Rtu_ports findRtuPortByRtuIdAndModelOrderAndPortOrder(Serializable id,int model_order , int port_order);
//多条件查询RTU port
	List<Rtu_ports>findRtuPortByMulti(Map<String,Object> parameters) ;	
//根据光路跳纤Id
	public Rtu_ports findRtuPortByJumperRouteId(Serializable id);
//根据模块序号查找当前模块上未使用的端口	
	public List<Rtu_ports> findRtuPortsByRtuIdAndMoudleOrder(Long id,int order);	
/**************/
	
/**查询光缆**/
//根据光缆标识(Optical_cables-ID)查询光缆
	Optical_cables findOpticalCable(Serializable id);
//根据局站id获取当前局站的所有光缆
	List<Optical_cables> findOpticalCablesByStationId(Serializable id);	
//查询所有光缆
	List<Optical_cables> findAllCables();
//多条件查询
	List<Optical_cables> findOpticalCablesByMulti(Map<String,Object> parameters);
//根据AZ局站名称查找光缆	
   public List<Optical_cables> findByAZStationId(Long idA,Long idZ);	
/***********/
	
/**查询纤芯**/
//根据纤芯标识(Fiber_core-ID)查询纤芯
	Fiber_cores findFiberCore(Serializable id);
//根据局站ID(Stations-ID)查询光纤
	List<Fiber_cores> findFiberCoresByStationId(Serializable id);
//根据配线架ID以及配线架端口序号Order查询光纤
		Fiber_cores findFiberCoreByFrameIDandPortOrder(Serializable id, int port_order);
//根据光缆Id查询所有光纤
	List<Fiber_cores> findFiberCoresByOpticalCableId(Serializable id);
//多条件查询光纤
	List<Fiber_cores> findFibersByMulti(Map<String,Object> parameters);
	
//根据光路Id查找	
	List<Fiber_cores> findFiberCoresByRouteId(Serializable id);
/***********/
	
/**查询光路跳纤**/
//根据光路跳纤标识(Jumper_routes-ID)查询光路跳纤
	Jumper_routes findJumperRoute(Serializable id);
//根据局站id获取当前局站的所有光路跳纤
	List<Jumper_routes> findJumperRoutesByStationId(Serializable id);
//多条件查询
	List<Jumper_routes> findJumperRoutesByMulti(Map<String,Object> parameters);
//route id
	List<Jumper_routes> findJumperRoutesByRouteId(Serializable id);
/**************/
	
/**查询配线架跳纤**/
//根据配线架跳纤标识(Jumper_frames-ID)查询配线架跳纤
	Jumper_frames findJumperFrame(Serializable id);
//根据局站id获取当前局站的所有配线架跳纤
	List<Jumper_frames> findJumperFramesByStationId(Serializable id);	
//多条件查询
	List<Jumper_frames> findJumperFramesByMulti(Map<String,Object> parameters);
//route id
    List<Jumper_frames> findJumperFramesByRouteId(Serializable id);
  /**备纤光源跳纤**/	
  //查询备纤光源跳纤
  	public Jumper_backups findJumperBackup(Serializable id);
  //通过光路ID查找
  	public Jumper_backups findJumperBackupByRouteId(Long id);
  //通过局站ID
	public List<Jumper_backups> findJumperBackupByStationId(Serializable id);
//name
	public List<Jumper_backups> findJumperBackupByName(String name);
/*****************/
	
//根据预备光路标识(Preparatory_routes-ID)查询预备光路
	Preparatory_routes findPreparatoryRoute(Serializable id);
	
/**查询光路**/
//根据光路标识(Routes-ID)查询光路
	Routes findRoute(Serializable id);
//根据光端口序号以及RTU编号查询光路
	Routes findRouteByRtuOrderAndID(Serializable id, int port_order);
//根据切换RTU端口序号以及RTU编号查询光路
	Routes  findRouteBySwitchRtuOrderAndID(Serializable id, int port_order);		
//根据Rtu Id查询所有光路
	List<Routes> findRoutesByRtuId(Serializable id);
//根据告警组id priotity_id
	List<Routes> findRoutesByPriotityId(Serializable id);
//多条件查询
	List<Routes> findRouteByMulti(Map<String,Object> parameters);
//获取所有的光路
	List<Routes> findAllRoutes();
/***********/

/**查询优化参数**/
//根据优化参数标识(Optimize_parameters-ID)查询优化参数
	Optimize_parameters findOptimizeParameter(Serializable id);
//根据光路id获取优化测试参数
	Optimize_parameters findOptimizeParameterByRouteId(Serializable id);
/**************/
	
/**回传曲线查询**/	
//根据回传曲线标识(Curves-ID)查询回传曲线
	Curves findCurve(Serializable id);
//根据RTU标识(RTU-ID)查询回传曲线
	List<Curves> findCurvesByRutId(Serializable id);
//根据光路id获取曲线
	List<Curves> findCurvesByRouteId(Serializable id);
	/**
	 * 根据光路ID分页查询曲线
	 * @param sql 查询语句
	 * @param page 当前页码
	 * @param perCount 每一页显示的条目数
	***/
	List<Curves> findPaginationCurvesByRouteId(Serializable id,int page,int perCount);
//分页查找周期测试的曲线
	List<Curves> findPaginationPeriodCurvesByRouteId(Serializable id,int page,int perCount);
	//多条件查找曲线
	public List<Curves> findCurveByTypeAndRouteId(Serializable routeId,String curveType);
//根据光路id获取参考曲线
	public Curves findReferenceCurveByRouteId(Serializable id);
/***************/
	
//根据回传测试参数标识(Parameter_curves-ID)查询回传测试参数
	Parameter_curves findParameterCurve(Serializable id);
	Parameter_curves findParameterCurveByCurveId(Serializable id);

/**查询事件**/
//根据回传事件标识(Event_curves-ID)查询回传事件
	Event_curves findEventCurve(Serializable id);
//根据曲线Id查询该曲线的事件
	List<Event_curves> findEventCurvesByCurveId(Serializable id);
/***********/
	
/**查询周期测试参数**/
//根据周期测试参数标识(Period_parameters-ID)查询周期测试参数
	Period_parameters findPeriodParameter(Serializable id);
//根据光路Id查询周期测试参数
	Period_parameters findPeriodParameterByRouteId(Serializable id);
/******************/
/**查询告警组**/
//根据rtuId获取当前rtu下的所有告警组
	public List<Priorities> findAllPrioritiesByRtuId(Serializable id);
//根据Id获取告警组
	public Priorities findPriorityById(Serializable id);	
/***************/
/**查询暂存告警组记录**/
//根据rtuId获取暂存组
	public List<TempGroupInfo> findTempGroupByRtuId(Serializable id);
//根据Id获取暂存组
	public TempGroupInfo findTempGroupById(Serializable id);
//通过rtuID和告警组Id查找暂存记录
	public TempGroupInfo findTempGroupByRtuIdAndGroupId(Serializable id, Long alarmGroupId);	
	
/***************/
/**查询告警方式**/	
//查询所有的告警方式
	public List<Alarm_ways> findAllAlarmWay();
	public Alarm_ways findAlarmWayById(Long id);
/**查询配对组**/	
//根据id查询配对组
	public Protect_groups findProtectGroupById(Long id);
//通过rtuId查找配对组	
	public List<Protect_groups> findProtectGroupsByRtuId(Long id);
//通过routeId查找配对组	
	public Protect_groups findProtectGroupsByRouteId(Long id);	
	
/***************/	
/**查询光功率数据**/		
//通过routeId查找光功率数据
	public List<Optical_powers> findOpticalPowersByRouteId(Long id);
	
/************/
//通过RTU Id查找当前光路下的告警信息
	public List<Alarm> findAlarmByRtuId(Long id);	
	
//通过光路Id查找当前光路下的告警信息
	public List<Alarm> findAlarmByRouteId(Long id);
//根据告警级别查找告警
	public List<Alarm> findAlarmByLevel(String level);
	//多条件查询告警信息
	public List<Alarm> findAlarmByMulti(Map<String,Object> parameters);
	//根据id查询告警
	public Alarm findAlarmById(Long id);	
	//分页获取告警
	public List<Alarm> findPaginationAlarmsByRouteId(Long routeId, int page, int perCount);
/*******/
//查找所有角色
   public List<Role>  findAllRoles();
//通过id查找角色
   public Role findRoleById(Long id);
//通过角色名查找角色
   public Role findRoleByName(String name);
/****/
//查找所有权限
   public List<Permissions>  findAllPermissions();
 //通过id查找
    public Permissions findPermissionById(Long id);   
/******/   
//通过角色查找用户    
    public List<User> findUsersByRoleId(Long id);
//通过id查找用户
    public User findUserById(Long id); 
//通过用户名查找用户    
    public User findUserByAccount(String account); 
//通过光路Id查找门限    
    public Threshold findThresholdByRouteId(Long id); 
 /**/  	
 //通过id查找值班信息
    public Duty_schedule findDutyScheduleById(Long id);
 //根据值班日期查找
    public Duty_schedule  findDutyScheduleByDutyDate(int date);
 //查询所有值班信息
    public List<Duty_schedule> findAllDutySchedule();
  //根据用户Id查询值班信息
    public List<Duty_schedule> findDutyScheduleByDutyOperatorId(Serializable id);
    public List<Duty_operator> findDutyOperatorByName(String name);
  //多条件查询
    public List<Duty_schedule> findDutyScheduleByMulti(Map<String,Object> parameters);
  
  /****/
    public Duty_operator findDutyOperatorById(Long id);
    public List<Duty_operator> findAllDutyOperator();
    //多条件查询
    public List<Duty_operator> findDutyOperatorByMulti(Map<String,Object> parameters);
    /**验证码**/
    public List<Verify_codes>findAllVerifyCode(Verify_codes verifyCode);
    
    public Verify_codes findVerifyCodeById(Long id);
    public Verify_codes findVerifyCodeByAccount(String account);
    /***/
    //通过id查找日志
     public Log findLogById(Long id);
    //查找所有日志
     public List<Log> findAllLog(String resourceType);
     //分页查找日志
     public List<Log> findLogByPage(String logType,int page,int perCount);
     public List<Log> findLogByMulti(Map<String,Object> parameters);
     //查询系统记录
     public SystemInfo findSystemInfoById(Long id);
/****/
     //获取全部的告警经验库
     public List<AlarmBank> findAllAlarmExperience();
     public AlarmBank findAlarm(Long id);
     public List<AlarmBank> findAlarmByMuyi(Map<String,Object> para);
//查找序列号
     public SerialCode findSNCodeById(Long id);    
}