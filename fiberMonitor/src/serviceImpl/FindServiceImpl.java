

/**业务逻辑层：查询实体**/


package serviceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.*;
import domain.*;
import service.FindService;

public class FindServiceImpl implements FindService
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
	private 	ParameterCurveDao		parameterCurveDao;		//21.注入对回传测试参数的操作
	private		EventCurveDao			eventCurveDao;			//22.注入对回传事件的操作
	@SuppressWarnings("unused")
	private		TopologicRouteDao		topologicRouteDao;		//23.注入对光路拓扑图-线的操作
	@SuppressWarnings("unused")
	private		TopologicPointDao		topologicPointDao;		//24.注入对光路拓扑图-站点的操作
	private		PriorityDao		        priorityDao;		    //25.注入对告警组的操作
	private		TempGroupInfoDao	    tempGroupInfoDao;	    //26.注入对暂存告警组的操作
	private		AlarmWayDao	            alarmWayDao;	        //27.注入对告警方法的操作
	private		ProtectGroupDao	        protectGroupDao;	    //28.注入对告警方法的操作
    private     OpticalPowerDao         opticalPowerDao;        //29.注入对光功率数据的操作
    private     AlarmDao                alarmDao;          		//30.注入对告警信息的操作 
    private     RoleDao                 roleDao;          		//31.注入对角色的操作 
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
	private     SerialCodeDao     	    serialCodeDao;		    //42.注入对序列号的操作

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
/**查询区域**/
//根据区域标识(Areas-ID)查询区域
	public Areas findArea(Serializable id)
	{
		return areaDao.findEntity(Areas.class, id);
	}
//查询所有的区域
	public List<Areas> findAllAreas()
	{
		return areaDao.findAllEntities(Areas.class);
	}
/************/

/**查询局站**/
//根据局站标识(Stations-ID)查询局站
	public Stations findStation(Serializable id)
	{
		return stationDao.findEntity(Stations.class, id);
	}
//根据区域(Areas)ID查询局站
	public List<Stations> findStationsByAreaId(Serializable id)
	{
		return stationDao.findAllByAreaId(id);
	}
//多条件查询局站
	public List<Stations> findStationsByMulti(Map<String,Object> parameters) 
	{
		try
		{
			return stationDao.findEntities(Stations.class, parameters);
		}
		catch(Exception e)
		{
			return null;
		}
	}
//查询所有局站
	public List<Stations> findAllStations()
	{
		return stationDao.findAllEntities(Stations.class);
	}
/***********/

/**查询地标**/
//根据地标标识(Landmarks-ID)查询地标
	public Landmarks findLandmark(Serializable id)
	{
		return landmarkDao.findEntity(Landmarks.class, id);
	}
//根据光缆id获取该光缆的全部光缆地标
	public List<Landmarks> findLandmarksByOpticalCableId(Serializable id)
	{
		return landmarkDao.findAllByOpticalCableId(id);
	}
//多条件查询
	public List<Landmarks> findLandmarksByMulti(Map<String,Object> parameters)
	{
		try
		{
			return landmarkDao.findEntities(Landmarks.class, parameters);
		}
		catch(Exception e)
		{
			return null;
		}
	}
//查询所有地标
	public List<Landmarks> findAllLandmarks()
	{
		return landmarkDao.findAllEntities(Landmarks.class);
	}
/***********/
	
/**查询机柜**/
//根据机柜标识(Cabinets-ID)查询机柜
	public Cabinets findCabinet(Serializable id)
	{
		return cabinetDao.findEntity(Cabinets.class, id);
	}
//多条件查询
	public List<Cabinets> findCabinetsByMulti(Map<String,Object> parameters)
	{
		try
		{
			return cabinetDao.findEntities(Cabinets.class, parameters);
			
		}
		catch(Exception e)
		{
			return null;
		}
	}
//查询所有机柜
	public List<Cabinets> findAllCabinets()
	{
		return cabinetDao.findAllEntities(Cabinets.class);
	}
//根据配线架Id查询机柜
	public Cabinets findCabinetByFrameId(Serializable id)
	{
		//根据配线架Id查询机架
		Racks rack = rackDao.findByFrameId(id);
		//根据机架id查询机柜
		return cabinetDao.findByRackId(rack.getId());
	}

/************/

/**查询机架**/
//根据机架标识(Racks-ID)查询机架
	public Racks findRack(Serializable id)
	{
		return rackDao.findEntity(Racks.class, id);
	}
//根据机柜Id和机架序号查询机架
	public Racks findRackByCabinetIdAndRackOrder(Serializable id, int rack_order)
	{
		return rackDao.findByCabinetIdAndRackOrder(id, rack_order);
	}
//根据局站Id(Stations-ID)查询机架
	public List<Racks> findRacksByStationId(Serializable id)
	{
		//查询局站下的所有机柜
		List<Cabinets> cabinets = cabinetDao.findByStationId(id);
		//查询机柜下的所有机架
		List<Racks> racks = new ArrayList<Racks>();
		for(Cabinets cabinet : cabinets)
			racks.addAll(cabinet.getRacks());
		return racks;
	}
/************/
	
/**查询配线架**/
//根据配线架标识(Distributing_frames-ID)查询配线架
	public Distributing_frames findDistributingFrame(Serializable id)
	{
		return frameDao.findEntity(Distributing_frames.class, id);
	}
//根据局站Id获取该局站下的配线架
	public List<Distributing_frames> findDistributingFramesByStationId(Serializable id)
	{
		return frameDao.findAllByStationId(id);
	}
//	查询所有配线架
	public List<Distributing_frames> findAllFrames() {
			
		return frameDao.findAllEntities(Distributing_frames.class);
	}
//多条件查询
	public List<Distributing_frames> findDistributingFramesByMulti(Map<String,Object> parameters)
	{
		try
		{
			return frameDao.findEntities(Distributing_frames.class,parameters);
		} 
		catch (Exception e) 
		{
			return null;
		}

	}
/*************/
	
/**查询配线架端口**/
//根据配线架端口标识(Frame_ports-ID)查询配线架端口
	public Frame_ports findFramePort(Serializable id)
	{
		return framePortDao.findEntity(Frame_ports.class, id);
	}
//根据配线架ID查询该配线架上的所有端口
	public List<Frame_ports> findFramePortsAllByFrameId(Serializable id)
	{
		return framePortDao.findAllByFrameId(id);
	}
//根据配线架ID和端口Id查询该配线架上的端口
	public Frame_ports findFramePortByFrameIdAndPortId(Serializable id,int port_order)
	{
		return framePortDao.findByFrameIdAndPortId(id, port_order);
	}
//根据配线架ID和端口序号查询该配线架上的端口
	public Frame_ports findFramePortByFrameIdAndPortOrder(Serializable id,int port_order)
	{
		return framePortDao.findByFrameIdAndPortOrder((Long) id, port_order);
	}
//多条件查询配线架端口
	public List<Frame_ports> findFramePortByMulti(Map<String,Object> parameters) 
	{
		try
		{
			return framePortDao.findEntities(Frame_ports.class, parameters);
		}
		catch(Exception e)
		{
			return null;
		}
	}	

/****************/
/**查询RTU**/
//根据RTU标识(Rtus-ID)查询RTU
	public Rtus findRtu(Serializable id)
	{
		return rtuDao.findEntity(Rtus.class, id);
	}
//查询所有RTU
	public List<Rtus> findAllRtus()
	{
		return rtuDao.findAllEntities(Rtus.class);
	}
//根据局站Id查询RTU
	public List<Rtus> findRtusByStationId(Serializable id)
	{
		return rtuDao.findAllByStationId(id);
	}
//多条件查询
	public List<Rtus> findRtusByMulti(Map<String,Object> parameters)
	{
		try
		{
			return rtuDao.findEntities(Rtus.class, parameters);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
/****************/
/**查询RTU**/
//根据rtu标识获取模块数量
	public int findModelNumByRtuId(Serializable id)
	{
		Rtus rtu = rtuDao.findEntity(Rtus.class, id);
		int modelNum = 0;
		String installInfo = rtu.getInstallInfo();
		for(int i = 0;i<installInfo.length();i++)
		{
			if(!installInfo.substring(i, i+1).equals("0"))
				modelNum++;
		}
		return modelNum;
	}

//根据rtu标识和模块号获取模块光端口的使用数量
	public int findModelUsedPortNumByRtuIdAndModelOrder(Serializable id, int modelOrder)
	{
		List<Rtu_ports> ports = rtuPortDao.findUnsedRtuPortsByRtuIdAndMoudleOrder((Long) id,modelOrder);
		return  8-ports.size();
	}
/************/
	
/**查询RTU端口**/	
//根据RTU端口标识(Rtu_ports-ID)查询RTU端口
	public Rtu_ports findRtuPort(Serializable id)
	{
		return rtuPortDao.findEntity(Rtu_ports.class, id);
	}
//根据RTU标识(RTU-ID)查询对应的OTDR端口数量	
	public int findOtdrAmountByRtuId(Serializable id)
	{
		return rtuPortDao.findCountByRtuId(id);
	}
//根据RTU-ID查询该RTU上的所有OTDR端口
	public List<Rtu_ports> findRtuPortsAllByRtuId(Serializable id)
	{
		return rtuPortDao.findAllByRtuId(id);
	}
//根据RTU-ID和model序号和端口序号查询OTDR端口
	public Rtu_ports findRtuPortByRtuIdAndModelOrderAndPortOrder(Serializable id,int model_order , int port_order)
	{
		return rtuPortDao.findByRtuIdAndModelOrderAndPortOrder(id, model_order,port_order);
	}
//多条件查询OTDR端口
	public List<Rtu_ports> findRtuPortByMulti(Map<String,Object> parameters) 
	{
		try
		{
			return rtuPortDao.findEntities(Rtu_ports.class, parameters);
		}
		catch(Exception e)
		{
			return null;
		}
	}
//根据RTU-ID和端口序号查询OTDR端口
	public Rtu_ports findRtuPortByJumperRouteId(Serializable id)
	{
		return rtuPortDao.findByJumperRouteId(id);
	}
//根据模块序号查找当前模块上未使用的端口	
	public List<Rtu_ports> findRtuPortsByRtuIdAndMoudleOrder(Long id,int order){
		return rtuPortDao.findUnsedRtuPortsByRtuIdAndMoudleOrder(id, order);
	}	
/*************/
	
/**查询光缆**/
//根据光缆标识(Optical_cables-ID)查询光缆
	public Optical_cables findOpticalCable(Serializable id)
	{
		return opticalCableDao.findEntity(Optical_cables.class, id);
	}
//根据局站id获取当前局站的所有光缆
	public List<Optical_cables> findOpticalCablesByStationId(Serializable id)
	{
		return opticalCableDao.findAllByStationId(id);
	}
//多条件查询
	public List<Optical_cables> findOpticalCablesByMulti(Map<String,Object> parameters)
	{
		try
		{
			return opticalCableDao.findEntities(Optical_cables.class,parameters);
		}
		catch(Exception e)
		{
			return null;
		}
	}
//查询所有光缆
	public List<Optical_cables> findAllCables()
	{
		return opticalCableDao.findAllEntities(Optical_cables.class);
	}
//根据AZ局站名称查找光缆	
	public List<Optical_cables> findByAZStationId(Long idA,Long idZ){
		return opticalCableDao.findByAZStationId(idA, idZ);
	}
/***********/
	
/**查询纤芯**/
//根据纤芯标识(Fiber_core-ID)查询纤芯
	public Fiber_cores findFiberCore(Serializable id)
	{
		return fiberCoreDao.findEntity(Fiber_cores.class, id);
	}
//根据局站ID(Stations-ID)查询光纤
	public List<Fiber_cores> findFiberCoresByStationId(Serializable id)
	{
		return fiberCoreDao.findAllByStationId(id);
	}
//根据配线架ID以及配线架端口序号Order查询光纤
	public Fiber_cores findFiberCoreByFrameIDandPortOrder(Serializable id, int port_order)
	{
		return fiberCoreDao.findByFrameIdandPortOrder(id, port_order);
	}
//根据光缆Id查询所有光纤
	public List<Fiber_cores> findFiberCoresByOpticalCableId(Serializable id)
	{
		return fiberCoreDao.findAllByOpticalCableId(id);
	}
//多条件查询光纤	
	public List<Fiber_cores> findFibersByMulti(Map<String,Object> parameters)
	{
		try {
			return fiberCoreDao.findEntities(Fiber_cores.class, parameters);
		} 
		catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
	}
//根据光路Id查找	
	public List<Fiber_cores> findFiberCoresByRouteId(Serializable id){
		return fiberCoreDao.findAllByRouteId(id);
	}
/***********/
	
/**查询光路跳纤**/
//根据光路跳纤标识(Jumper_routes-ID)查询光路跳纤
	public Jumper_routes findJumperRoute(Serializable id)
	{
		return jumperRouteDao.findEntity(Jumper_routes.class, id);
	}
//根据局站id获取当前局站的所有光路跳纤
	public List<Jumper_routes> findJumperRoutesByStationId(Serializable id)
	{
		return jumperRouteDao.findAllByStationId(id);
	}
//多条件查询
	public List<Jumper_routes> findJumperRoutesByMulti(Map<String,Object> parameters)
	{
		try
		{
			return jumperRouteDao.findEntities(Jumper_routes.class, parameters);
		}
		catch(Exception e)
		{
			return null;
		}
	}
 //	route id
	public List<Jumper_routes> findJumperRoutesByRouteId(Serializable id){
		return jumperRouteDao.findByRouteId(id);
	}
/**备纤光源跳纤**/	
//查询备纤光源跳纤
	public Jumper_backups findJumperBackup(Serializable id)
	{
		return jumperBackupDao.findEntity(Jumper_backups.class, id);
	}
//通过光路ID查找
	public Jumper_backups findJumperBackupByRouteId(Long id)
	{
		return jumperBackupDao.findEntity(Jumper_backups.class, id);
	}
//通过局站ID
	public List<Jumper_backups> findJumperBackupByStationId(Serializable id)
	{
		return jumperBackupDao.findAllByStationId(id);
	}
//name
	public List<Jumper_backups> findJumperBackupByName(String name)
	{
		return jumperBackupDao.findAllByName(name);
	}
/***************/

/**查询配线架跳纤**/
//根据配线架跳纤标识(Jumper_frames-ID)查询配线架跳纤
	public Jumper_frames findJumperFrame(Serializable id)
	{
		return jumperFrameDao.findEntity(Jumper_frames.class, id);
	}
//根据局站id获取当前局站的所有配线架跳纤
	public List<Jumper_frames> findJumperFramesByStationId(Serializable id)
	{
		return jumperFrameDao.findAllByStationId(id);
	}
//多条件查询
	public List<Jumper_frames> findJumperFramesByMulti(Map<String,Object> parameters)
	{
		try
		{
			return jumperFrameDao.findEntities(Jumper_frames.class, parameters);
		}
		catch(Exception e)
		{
			return null;
		}

	}
//	route id
    public List<Jumper_frames> findJumperFramesByRouteId(Serializable id){
    	return jumperFrameDao.findByRouteId(id);
    }
/*****************/
	
//根据预备光路标识(Preparatory_routes)查询预备光路
	public Preparatory_routes findPreparatoryRoute(Serializable id)
	{
		return preparatoryRouteDao.findEntity(Preparatory_routes.class, id);
	}

/**查询光路**/	
//根据光路标识(Routes-ID)查询光路
	public Routes findRoute(Serializable id)
	{
		return routeDao.findEntity(Routes.class, id);
	}
//根据光端口序号以及RTU编号查询光路
	public Routes findRouteByRtuOrderAndID(Serializable id, int port_order)
	{
		
		return routeDao.findByRtuOrderAndID(id, port_order);
	}
//根据切换RTU端口序号以及RTU编号查询光路
	public Routes findRouteBySwitchRtuOrderAndID(Serializable id, int port_order){
		return routeDao.findBySwitchRtuOrderAndID(id, port_order);
	}		
//根据Rtu Id查询所有光路
	public List<Routes> findRoutesByRtuId(Serializable id)
	{
		return routeDao.findAllByRtuId(id);
	}
//多条件查询光路
	public List<Routes> findRouteByMulti(Map<String,Object> parameters)
	{
		try
		{
			return routeDao.findEntities(Routes.class, parameters);
		}
		catch(Exception e)
		{
			return null;
		}
		
	}
//根据 priority_id 查找光路
	public List<Routes>findRoutesByPriotityId(Serializable id){
		return routeDao.findAllByPriotityId(id);
		
	}
//获取所有的光路
	public List<Routes> findAllRoutes(){
		return routeDao.findAllEntities(Routes.class);
	}
/***********/
	
/**查询优化参数**/
//根据优化参数标识(Optimize_parameters-ID)查询优化参数
	public Optimize_parameters findOptimizeParameter(Serializable id)
	{
		return optimizeParameterDao.findEntity(Optimize_parameters.class, id);
	}
//根据光路id获取优化测试参数
	public Optimize_parameters findOptimizeParameterByRouteId(Serializable id)
	{
		return optimizeParameterDao.findByRouteId(id);
	}
/*************/
	
/**回传曲线查询**/	
//根据回传曲线标识(Curves-ID)查询回传曲线
	public Curves findCurve(Serializable id)
	{
		return curveDao.findEntity(Curves.class, id);
	}	
//根据RTU标识(RTU-ID)查询回传曲线
	public List<Curves> findCurvesByRutId(Serializable id)
	{
		return curveDao.findAllByRtuId(id);
	}
//根据光路id获取曲线
	public List<Curves> findCurvesByRouteId(Serializable id)
	{
		return curveDao.findAllByRouteId(id);
	}
	/**
	 * 根据光路ID分页查询曲线
	 * @param sql 查询语句
	 * @param page 当前页码
	 * @param perCount 每一页显示的条目数
	***/
	public List<Curves> findPaginationCurvesByRouteId(Serializable id,int page,int perCount){
		return curveDao.findPaginationByRouteId(id, page, perCount);
	}
	//分页查找周期测试的曲线
	public List<Curves> findPaginationPeriodCurvesByRouteId(Serializable id,int page,int perCount){
		return curveDao.findPaginationPeriodByRouteId(id, page, perCount);
	}
	/**多条件查找曲线**/
	public List<Curves> findCurveByTypeAndRouteId(Serializable routeId,String curveType){
		return curveDao.findCurveByTypeAndRouteId(routeId, curveType);
		
	}
//根据光路id获取参考曲线
	public Curves findReferenceCurveByRouteId(Serializable id){
		return curveDao.findReferenceByRouteId(id);
	}
/***************/

//根据回传测试参数标识(Parameter_curves-ID)查询回传测试参数
	public Parameter_curves findParameterCurve(Serializable id)
	{
		return parameterCurveDao.findEntity(Parameter_curves.class, id);
	}
	public Parameter_curves findParameterCurveByCurveId(Serializable id){
		return parameterCurveDao.findByCurveId(id);
	}
/**查询事件**/
//根据回传事件标识(Event_curves-ID)查询回传事件
	public Event_curves findEventCurve(Serializable id)
	{
		return eventCurveDao.findEntity(Event_curves.class, id);
	}
//根据曲线Id查询该曲线的事件
	public List<Event_curves> findEventCurvesByCurveId(Serializable id)
	{
		return eventCurveDao.findByCurveId(id);
	}
/**********/

/**查询周期测试参数**/
//根据周期测试参数标识(Period_parameters-ID)查询周期测试参数
	 public Period_parameters findPeriodParameter(Serializable id)
	 {
		 return periodParameterDao.findEntity(Period_parameters.class, id);
	 }
//根据光路Id查询周期测试参数
	public Period_parameters findPeriodParameterByRouteId(Serializable id)
	{
		return periodParameterDao.findByRouteId(id);
	}
/*****************/
/**查询优先级组**/
//根据rtuId获取当前rtu下的所有告警组	
	public List<Priorities> findAllPrioritiesByRtuId(Serializable id)
	{
		return priorityDao.findAllByRtuId(id);
	}
//根据Id获取告警组	
	public Priorities findPriorityById(Serializable id)
	{
		return priorityDao.findEntity(Priorities.class, id);
	}
	/*****************/
/**查询暂存分组记录**/
//根据rtuId获取当前rtu下的暂存分组记录	
	public List<TempGroupInfo> findTempGroupByRtuId(Serializable id)
	{
		return tempGroupInfoDao.findAllByRtuId(id);
	}
//根据Id获取暂存暂存分组记录
	public TempGroupInfo findTempGroupById(Serializable id)
	{
		return tempGroupInfoDao.findEntity(TempGroupInfo.class, id);
	}
//通过rtuID和告警组Id查找暂存记录
	public TempGroupInfo findTempGroupByRtuIdAndGroupId(Serializable id, Long alarmGroupId){
		return tempGroupInfoDao.findByRtuIdAndalarmGroupId(id,alarmGroupId);
		
	}
/***************/
/**查询告警方式**/	
	public Alarm_ways findAlarmWayById(Long id){
		return alarmWayDao.findEntity(Alarm_ways.class, id);
	}
//查询所有的告警方式
	public List<Alarm_ways> findAllAlarmWay(){
		return alarmWayDao.findAllEntities(Alarm_ways.class);
	}	
/**查询配对组**/	
//根据id查询配对组
	public Protect_groups findProtectGroupById(Long id){
		return protectGroupDao.findEntity(Protect_groups.class, id);
	}	
//通过rtuId查找配对组	
	public List<Protect_groups> findProtectGroupsByRtuId(Long id){
		return protectGroupDao.findProtectGroupsByRtuId(id);
	}
	
//通过routeId查找配对组	
	public Protect_groups findProtectGroupsByRouteId(Long id){
		return protectGroupDao.findProtectGroupsByRouteId(id);
		
	}
//通过routeId查找光功率数据
	public List<Optical_powers> findOpticalPowersByRouteId(Long id){
		return opticalPowerDao.findOpticalPowersByRouteId(id);
		
	}
	
//通过RTU id获取所有的告警信息	
	public List<Alarm> findAlarmByRtuId(Long id){
		return alarmDao.findByRtuId(id);
	}
//分页获取告警
	public List<Alarm> findPaginationAlarmsByRouteId(Long routeId, int page, int perCount){
		return alarmDao.findPaginationByRouteId(routeId,page,perCount);
	}	
	
//通过光路id获取所有的告警信息	
	public List<Alarm> findAlarmByRouteId(Long id){
		return alarmDao.findByRouteId(id);
	}
//通过告警级别查找告警
	public List<Alarm> findAlarmByLevel(String level)
	{
		return alarmDao.findByLevel(level);
	}
//多条件查询告警信息
	public List<Alarm> findAlarmByMulti(Map<String,Object> parameters)
	{
		try
		{
			return alarmDao.findEntities(Alarm.class, parameters);
		}
		catch(Exception e)
		{
			return null;
		}
	}
//根据id查询告警
	public Alarm findAlarmById(Long id){
		return alarmDao.findEntity(Alarm.class, id);
	}
/********/
//查找所有的角色
	public List<Role> findAllRoles(){
		return roleDao.findAllEntities(Role.class);
	}
//根据id获取角色
	public Role findRoleById(Long id){
	   return roleDao.findEntity(Role.class, id);
	}
//通过角色名查找角色
	public Role findRoleByName(String name){
		return roleDao.getByName(name);
	}	
/****/
//查找所有权限
   public List<Permissions>  findAllPermissions(){
	  return permissionDao.findAllEntities(Permissions.class);
   }
//通过id查找
  public Permissions findPermissionById(Long id){
	  return permissionDao.findEntity(Permissions.class, id);
  }  
  /******/   
//通过角色查找用户    
  public List<User> findUsersByRoleId(Long id){
     return userDao.findUsersByRoleId(id);
   }
//通过id查找用户
  public User findUserById(Long id){
     return userDao.findEntity(User.class, id);
  }
//通过用户名查找用户    
  public User findUserByAccount(String account){
    	return userDao.getUserByAccount(account);
   } 
//通过光路Id查找门限  
  public Threshold findThresholdByRouteId(Long id){
	  return thresholdDao.findByRouteId(id);
  }
  /**值班表**/
	
//通过id查找值班信息
	public Duty_schedule findDutyScheduleById(Long id){
		return dutyScheduleDao.findEntity(Duty_schedule.class,id);
	}
//查询所有值班信息
	public List<Duty_schedule> findAllDutySchedule()
	{
		return dutyScheduleDao.findAllEntities(Duty_schedule.class);
	}
//根据日期查找	
	public Duty_schedule findDutyScheduleByDutyDate(int date)
	{
		return dutyScheduleDao.findByDate(date);
	}	
//根据值班人员Id查询值班信息
	public List<Duty_schedule> findDutyScheduleByDutyOperatorId(Serializable id)
	{
		return dutyScheduleDao.findAllByDutyOperatorId(id);
	}	
//多条件查询
	public List<Duty_schedule> findDutyScheduleByMulti(Map<String,Object> parameters)
	{
		try
		{
			return dutyScheduleDao.findEntities(Duty_schedule.class, parameters);
		}
		catch(Exception e)
		{
			return null;
		}
	}

/*值班人员*/
//通过id查找值班人员
	public Duty_operator findDutyOperatorById(Long id){
		return dutyOperatorDao.findEntity(Duty_operator.class, id);
	}
//通过name查找值班人员
	public List<Duty_operator> findDutyOperatorByName(String name){
		return dutyOperatorDao.findByName(name);
	}	
//查找所有的值班人员
	public List<Duty_operator> findAllDutyOperator(){
		return dutyOperatorDao.findAllEntities(Duty_operator.class);
	}
//多条件查询
	public List<Duty_operator> findDutyOperatorByMulti(Map<String,Object> parameters)
	{
		try
		{
			return dutyOperatorDao.findEntities(Duty_operator.class, parameters);
		}
		catch(Exception e)
		{
			return null;
		}
	}
/**验证码**/	
	public List<Verify_codes>findAllVerifyCode(Verify_codes verifyCode){
		return verifyCodeDao.findAllEntities(Verify_codes.class);
		
	}
	public Verify_codes findVerifyCodeById(Long id){
		return verifyCodeDao.findEntity(Verify_codes.class, id);
	}
	public Verify_codes findVerifyCodeByAccount(String account){
		return verifyCodeDao.findByAccount(account);
	}
	/*日志管理*/
	//通过id查找日志
	public Log findLogById(Long id){
		return logDao.findEntity(Log.class, id);
	}
	//查找所有日志
    public List<Log> findAllLog(String resourceType){
    	return logDao.findLogs(resourceType);
    }
   // 分页查找日志
    public List<Log> findLogByPage(String logType,int page,int perCount){
    	return logDao.findLogByPage(logType,page,perCount);
    }
	//多条件查询
	public List<Log> findLogByMulti(Map<String,Object> parameters)
	{
		try
		{
			return logDao.findEntities(Log.class, parameters);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	public SystemInfo findSystemInfoById(Long id){
		try
		{
			return systemDao.findEntity(SystemInfo.class, id);
		}
		catch(Exception e)
		{
			return null;
		}
	}
  public List<AlarmBank> findAllAlarmExperience(){
	  return alarmBankDao.findAllEntities(AlarmBank.class);
  }	
  public AlarmBank findAlarm(Long id){
	  return alarmBankDao.findEntity(AlarmBank.class, id);
 }
  public List<AlarmBank> findAlarmByMuyi(Map<String,Object> para){
	  try
	   {
			return alarmBankDao.findEntities(AlarmBank.class, para);
		}
		catch(Exception e)
		{
			return null;
		}
  }
  //查找序列号
  public SerialCode findSNCodeById(Long id){
	  return serialCodeDao.findEntity(SerialCode.class, id);
  }
}
