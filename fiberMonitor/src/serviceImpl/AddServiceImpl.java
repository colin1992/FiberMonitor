

/**业务逻辑层：添加实体**/


package serviceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dao.*;
import domain.*;
import service.AddService;
/**
 * 数据库资源增加实现类接口类
 * **/
public class AddServiceImpl implements AddService
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
	private		TopologicRouteDao		topologicRouteDao;		//23.注入对光路拓扑图-线的操作
	private		TopologicPointDao		topologicPointDao;		//24.注入对光路拓扑图-站点的操作
	private		PriorityDao		        priorityDao;		    //25.注入对告警组的操作
	private		TempGroupInfoDao	    tempGroupInfoDao;	    //26.注入对暂存告警组的操作
	private		AlarmWayDao	            alarmWayDao;	        //27.注入对告警方法的操作
	private     ProtectGroupDao         protectGroupDao;        //28.注入对配对组的操作
	private     OpticalPowerDao         opticalPowerDao;        //29.注入对光功率值的操作 
	private     AlarmDao                alarmDao;               //30.注入对告警信息的操作 
	private     RoleDao           		roleDao;          		//31.注入对角色的操作 
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
//新增区域(Areas)
	public Serializable addArea(Areas area)
	{
		return areaDao.addEntity(area);
	}
	
//新增局站(Stations)
	public Serializable addStation(Stations station)
	{
		return stationDao.addEntity(station);
	}
	
//新增机柜(Cabinets)
	public Serializable addCabinet(Cabinets cabinet)
	{
		Serializable id = cabinetDao.addEntity(cabinet);
		/**新增机柜，同时自动新增机架(Racks)**/
		int rackNumber = cabinet.getRack_number();	//获取机架数量
		for(int i=0; i<rackNumber; i++)
		{
			Racks rack = new Racks();
			rack.setRack_order(i+1);	//设置机架在该机柜上的序号,从1开始
			rack.setCabinet(cabinet);	//设置机架所属机柜
			addRack(rack);
		}
		return id;
	}
	
//新增机架(Racks)
	private Serializable addRack(Racks rack)
	{
		return rackDao.addEntity(rack);
	}
	
//新增配线架(Distributing_frames)
	public Serializable addDistributingFrame(Distributing_frames frame)
	{
		Serializable id = frameDao.addEntity(frame);
		/**新增配线架，同时自动新增配线架端口(Frame_ports)**/
		int framePortNumber = frame.getPort_number();	//获取配线架端口数量
		for(int i=0; i<framePortNumber; i++)
		{
			Frame_ports frame_port = new Frame_ports();
			frame_port.setPort_order(i+1);	//设置配线架端口在该配线架的序号
			frame_port.setFrame(frame);	//设置配线架端口所属配线架
			addFramePort(frame_port);
		}
		return id;
	}
	
//新增配线架端口(Frame_ports)
	private Serializable addFramePort(Frame_ports frame_port)
	{
		return framePortDao.addEntity(frame_port);
	}
	
//新增RTU(Rtus)
	public Serializable addRtu(Rtus rtu) //FIXME
	{
		String[] order={"A","B","C","D"};
		Serializable id = rtuDao.addEntity(rtu);
		if(rtu.getType().equals("普通rtu")){
			String rtuInstallType = rtu.getInstallInfo();
			for(int i =0;i<rtuInstallType.length();i++){  //固定为8，因为硬件上一共就只有8个模块
				if(!rtuInstallType.substring(i, i+1).equals("0")){//为连接的模块增加端口
					String port_type = "OTDR_PORT";
					for(int j = 0; j < 8; j++){
							Rtu_ports rtu_port = new Rtu_ports();
							rtu_port.setPort_type(port_type);//设置端口类型
							rtu_port.setPort_order(j+1);//设置端口序号
							String portName="M"+(i+1)+"-"+order[j%4]+(j/4+1);
							rtu_port.setName(portName);
							rtu_port.setStatus(false);//设置端口状态
							rtu_port.setRtu(rtu);//设置该端口所属RTU
							rtu_port.setModuleOrder(i+1);
							addRtuPort(rtu_port);
						}
				}
				
			}		
		}
		else {//备纤光源RTU
			for(int j = 0; j < rtu.getPortNum(); j++){
				String port_type = "IN_PORT";//将备纤光源RTU的端口设置为接入端口
				Rtu_ports rtu_port = new Rtu_ports();
				rtu_port.setPort_type(port_type);//设置端口类型
				rtu_port.setPort_order(j+1);//设置端口序号
				String portName="端口"+(j+1);
				rtu_port.setName(portName);
				rtu_port.setStatus(false);//设置端口状态
				rtu_port.setRtu(rtu);//设置该端口所属RTU
				rtu_port.setModuleOrder(1);
				addRtuPort(rtu_port);
			}
		}
		return id;
	}
	
//新增RTU端口(Rtu_ports)
	public Serializable addRtuPort(Rtu_ports rtu_port)
	{
		return rtuPortDao.addEntity(rtu_port);
	}
	
//新增光缆(Optical_cables)
	public Serializable addOpticalCable(Optical_cables optical_cable)
	{
		Serializable id = opticalCableDao.addEntity(optical_cable);
		/**新增光缆，自动新增纤芯(Fiber_cores)**/
		int fiberCoreNumber = optical_cable.getCoreNumber();//获取纤芯数量
		for(int i=0; i<fiberCoreNumber; i++)
		{
			Fiber_cores fiber_core = new Fiber_cores();
			fiber_core.setCore_order(i+1);//设置纤芯在该光缆的序号
			fiber_core.setOptival_cable(optical_cable);//设置该纤芯所属的光缆
			fiber_core.setStation_a_id(optical_cable.getStation_a_id());//设置光纤a端局站
			fiber_core.setStation_z_id(optical_cable.getStation_z_id());//设置光纤z端局站
			addFiberCore(fiber_core);
		}
		return id;
	}
	
//新增纤芯(Fiber_cores)
	private Serializable addFiberCore(Fiber_cores fiber_core)
	{
		return fiberCoreDao.addEntity(fiber_core);
	}
	
//新增配线架跳纤(Jumper_frames)
	public Serializable addJumperFrame(Jumper_frames jumper_frame)
	{
		Serializable id = jumperFrameDao.addEntity(jumper_frame);
		/**新增配线架跳纤，自动修改配线架端口状态信息**/
		//查询a端连接的配线架端口，并更新端口信息
		Frame_ports frame_port_a = framePortDao.findByFrameIdAndPortId(jumper_frame.getFrame_a_id(), jumper_frame.getPort_order_a());
		frame_port_a.setConnection_id((long)id);//设置连接的配线架跳纤id
		frame_port_a.setConnection_type("配线架跳纤");
		frame_port_a.setStatus(true);
		framePortDao.alterEntity(frame_port_a);//更新
		//查询z端连接的配线架端口，并更新端口信息
		Frame_ports frame_port_z = framePortDao.findByFrameIdAndPortId(jumper_frame.getFrame_z_id(), jumper_frame.getPort_order_z());
		frame_port_z.setConnection_id((long)id);//设置连接的配线架跳纤id
		frame_port_z.setConnection_type("配线架跳纤");
		frame_port_z.setStatus(true);
		framePortDao.alterEntity(frame_port_z);//更新
		return id;
	} 
	
//新增光路跳纤(Jumper_routes)
	public Serializable addJumperRoute(Jumper_routes jumper_route)
	{
		Serializable id =  jumperRouteDao.addEntity(jumper_route);
		
		/**新增光路跳纤，自动修改配线架端口和RTU端口状态信息**/
		//查询连接的RTU端口，并更新端口信息
		Rtu_ports rtu_port = rtuPortDao.findByRtuIdAndModelOrderAndPortOrder(jumper_route.getRtu_id(),jumper_route.getModelOrder(), jumper_route.getOtdr_port_order());
		rtu_port.setJumper_route_id((long)id);//设置光路跳纤id
		rtu_port.setStatus(true);//设置端口状态
		rtuPortDao.alterEntity(rtu_port);//更新端口信息
		//查询连接的配线架端口，并更新端口信息
		Frame_ports frame_port = framePortDao.findByFrameIdAndPortId(jumper_route.getFrame_id(), jumper_route.getFrame_port_order());
		frame_port.setConnection_id((long)id);//设置连接的配线架跳纤id
		frame_port.setConnection_type("光路跳纤");
		frame_port.setStatus(true);
		framePortDao.alterEntity(frame_port);//更新
		/**新增光路跳纤，自动新增预备光路**/
		Fiber_cores fiber_core = fiberCoreDao.findByFrameIdandPortOrder(jumper_route.getFrame_id(), 
				jumper_route.getFrame_port_order());//查找连接在该配线架端口上的光纤
		if(fiber_core != null)
		{
			Preparatory_routes preparatory_route = new Preparatory_routes();
			preparatory_route.setJumper_route(jumper_route);//设置该预备光路对应的光路跳纤
			preparatory_route.setStation_a_id(jumper_route.getStation().getId());//指定起点局站标识(A)
			preparatory_route.setRtu_id(jumper_route.getRtu_id());//指定RTU标识
			/**指定预备光路的RTU端口，转换为1-64**/
			preparatory_route.setRtu_port_order(jumper_route.getOtdr_port_order()+(jumper_route.getModelOrder()-1)*8);
			preparatory_route.setFrame_a_id(jumper_route.getFrame_id());//指定配线架标识(A)
			preparatory_route.setFrame_a_port_order(jumper_route.getFrame_port_order());//指定配线架端口序号
			preparatory_route.setFiber_core_id(fiber_core.getId());
			if(jumper_route.getFrame_id() == fiber_core.getFrame_a_id())//设置配线架标识(Z)和配线架端口序号
			{
				preparatory_route.setStation_z_id(fiber_core.getStation_z_id());
				preparatory_route.setFrame_z_port_order(fiber_core.getPort_order_z());
				preparatory_route.setFrame_z_id(fiber_core.getFrame_z_id());
			}
			else
			{
				preparatory_route.setStation_z_id(fiber_core.getStation_a_id());
				preparatory_route.setFrame_z_port_order(fiber_core.getPort_order_a());
				preparatory_route.setFrame_z_id(fiber_core.getFrame_a_id());
			}
			addPreparatoryRoute(preparatory_route);
		}
		return id;
	}
//新增切换跳纤(Jumper_routes)
	public Serializable addJumperSwitch(Jumper_routes jumper_route)
	{
		Serializable id =  jumperRouteDao.addEntity(jumper_route);
		//查询连接的RTU端口，并更新端口信息
		//jumper_route.get
		Rtu_ports rtu_port =rtuPortDao.findByRtuIdAndModelOrderAndPortOrder(jumper_route.getRtu_id(), jumper_route.getModelOrder(),jumper_route.getOtdr_port_order());//
		rtu_port.setJumper_route_id((long)id);//设置光路跳纤id
		rtu_port.setStatus(true);//设置端口状态
		rtuPortDao.alterEntity(rtu_port);//更新端口信息
		//查询连接的配线架端口，并更新端口信息
		Frame_ports frame_port = framePortDao.findByFrameIdAndPortId(jumper_route.getFrame_id(),jumper_route.getFrame_port_order());
		frame_port.setConnection_id((long)id);//设置连接的配线架跳纤id
		frame_port.setConnection_type("切换跳纤");
		frame_port.setStatus(true);
		framePortDao.alterEntity(frame_port);//更新
		return id;
	}
//新增备纤光源跳纤
	public Serializable addJumperBackup(Jumper_backups jumper_backup)
	{
		Serializable id=jumperBackupDao.addEntity(jumper_backup);
		/**新增光路跳纤，自动修改配线架端口和RTU端口状态信息**/
		//查询连接的RTU端口，并更新端口信息
		Rtu_ports rtu_port =rtuPortDao.findInPortByRtuIdAndOrder(jumper_backup.getRtu_id(), jumper_backup.getIn_port_order());//
		rtu_port.setJumper_route_id((long)id);//设置光路跳纤id
		rtu_port.setStatus(true);//设置端口状态
		rtuPortDao.alterEntity(rtu_port);//更新端口信息
		//查询连接的配线架端口，并更新端口信息
		Frame_ports frame_port = framePortDao.findByFrameIdAndPortId(jumper_backup.getFrame_id(), jumper_backup.getFrame_port_order());
		frame_port.setConnection_id((long)id);//设置连接的配线架跳纤id
		frame_port.setConnection_type("备纤光源跳纤");
		frame_port.setStatus(true);
		framePortDao.alterEntity(frame_port);//更新
		return id;
	}
//新增预备光路(Preparatory_routes)
	private Serializable addPreparatoryRoute(Preparatory_routes preparatory_route)
	{
		return preparatoryRouteDao.addEntity(preparatory_route);
	}
	
//新增光路拓扑图(Routes,传入光路拓扑图：局站id)
	public List<Map<String,Object>> addTopology(List<Serializable> ids)
	{   
		List<Map<String,Object>> routeCandidates = new ArrayList<Map<String,Object>>();
		/**首先判断是否为有效光路拓扑图**/
		int stationNumber = ids.size();//获取局站数量，跨段数=局站数-1
		int remainStation = stationNumber - 2;//剩余局站数
		//提取所有满足起始局站为第一局站和终止局站为第二局站的预备光路
		List<Preparatory_routes> preparatoryRoutes = preparatoryRouteDao.findByStationIds(ids.get(0), ids.get(1));
		if(preparatoryRoutes.size() == 0)//未找到这样的预备光路，返回null
			return null;
		Iterator<Preparatory_routes> iterator = preparatoryRoutes.iterator();

		while(iterator.hasNext())
		{
			//记录经过的所有跳纤、光纤,按序存放ID编号
			List<Serializable> routeMap = new ArrayList<Serializable>();
			//记录经过的所有站点：RTU、配线架,按序存放ID编号
			List<Serializable> pointMap = new ArrayList<Serializable>();
			int i = 0; //局站所在ids中编号
			//获取一条预备光路作为搜索起点
			Preparatory_routes preparatory_route = iterator.next();
			Long preparatory_id = preparatory_route.getId();//设置光路的preparatory_id用
			pointMap.add(preparatory_route.getRtu_id());//将RTU-ID作为第一个站点，之后每添加一种纤，将其尾端配线架设置为下一个站点
			routeMap.add(preparatory_route.getJumper_route().getId());//设置第一个路由线路：光路跳纤
			Jumper_routes jumper_route = jumperRouteDao.findEntity(Jumper_routes.class, preparatory_route.getJumper_route().getId());//获取该光路跳纤
			pointMap.add(jumper_route.getFrame_id());//设置第二个站点，光路跳纤连接的配线架ID
			//获取预备光路上的光纤
			Fiber_cores fiber_core = fiberCoreDao.findEntity(Fiber_cores.class, preparatory_route.getFiber_core_id());
			routeMap.add(fiber_core.getId());//设置第二个路由线路：光纤
			
			//如果该光纤A端和光路跳纤同时连接在同一个配线架上，则应该将光纤Z端配线架加入下一个站点
			if(fiber_core.getFrame_a_id() == jumper_route.getFrame_id())
				pointMap.add(fiber_core.getFrame_z_id());//设置下一个站点：光纤尾端配线架
			else
				pointMap.add(fiber_core.getFrame_a_id());
			
			//循环到最后一个局站
			while(remainStation+1 > 0)
			{
				if(remainStation>0) {
					i++;
				}
				if(fiber_core.getStation_a_id() == ids.get(i))//判断光纤A端是否属于第下个局站
				{
					//寻找连接着fiber_core光纤的配线架跳纤
					Jumper_frames jumper_frame = jumperFrameDao.findByFrameIdandPortId(fiber_core.getFrame_a_id(), fiber_core.getPort_order_a());	
					if(jumper_frame == null)//如果未找到对应配线架跳纤，则退出局站循环，回到下一个预备光路
						break;
					else//如果找到了对应的配线架跳纤
					{	
						routeMap.add(jumper_frame.getId());//设置下一个路由路线：配线架跳纤(说明光路为跨段属性)
						if(fiber_core.getFrame_a_id() == jumper_frame.getFrame_a_id())//光纤A端对应配线架跳纤A端
						{
							//如果光纤a端对应配线架跳纤a端,则将配线架跳纤z端连接配线架加入下一个站点
							pointMap.add(jumper_frame.getFrame_z_id());//加入配线架跳纤Z端对应配线架
							//如果光纤a端对应配线架跳纤a端，则用配线架z端参数去寻找下一个光纤，并将其复制给fiber_core
							fiber_core = fiberCoreDao.findByFrameIdandPortOrder(jumper_frame.getFrame_z_id(), jumper_frame.getPort_order_z());
							if(fiber_core == null)//如果未找到这样的光纤，说明未连通，退出局站循环，回到下一个预备光路
								break;
							else//如果找到了这样的光纤，则进行相应的记录
							{
								routeMap.add(fiber_core.getId());//设置下一个路由路线：光纤
								
								//如果该光纤A端对应配线架z端，则将该光纤z端连接配线架设置为下一个站点
								if(fiber_core.getFrame_a_id() == jumper_frame.getFrame_z_id())
									pointMap.add(fiber_core.getFrame_z_id());
								else
									pointMap.add(fiber_core.getFrame_a_id());
							}
						}
						else//光纤A端对应配线架跳纤Z端
						{
							//如果光纤a端对应配线架跳纤z端,则将配线架跳纤z端连接配线架加入下一个站点
							pointMap.add(jumper_frame.getFrame_a_id());//加入配线架跳纤a端对应配线架
							
							//如果光纤a端对应配线架跳纤z端，则用配线架a端参数去寻找下一个光纤，并将其复制给fiber_core
							fiber_core = fiberCoreDao.findByFrameIdandPortOrder(jumper_frame.getFrame_a_id(), jumper_frame.getPort_order_a());
							if(fiber_core == null)//如果未找到这样的光纤，说明未连通，退出局站循环，回到下一个预备光路
								break;
							else//如果找到了这样的光纤，则进行相应的记录
							{
								routeMap.add(fiber_core.getId());//设置下一个路由路线：光纤
								
								//如果该光纤A端对应配线架a端，则将该光纤z端连接配线架设置为下一个站点
								if(fiber_core.getFrame_a_id() == jumper_frame.getFrame_a_id())
									pointMap.add(fiber_core.getFrame_z_id());
								else
									pointMap.add(fiber_core.getFrame_a_id());
								
							}//第四层if-else
						}//第三层if-else
					}//第二层if-else
				}//第一层if-else
				else//否则光纤Z端属于下个局站
				{
					//寻找连接着fiber_core光纤的配线架跳纤
					Jumper_frames jumper_frame = jumperFrameDao.findByFrameIdandPortId(fiber_core.getFrame_z_id(), fiber_core.getPort_order_z());
					if(jumper_frame == null)//如果未找到对应配线架跳纤，则返回局站循环，回到下一个预备光路
						break;
					else
					{
						routeMap.add(jumper_frame.getId());//设置下一个路由路线：配线架跳纤(说明光路为跨段属性)
						if(fiber_core.getFrame_z_id() == jumper_frame.getFrame_a_id())//光纤Z端对应配线架跳纤A端
						{
							//光纤Z端对应配线架跳纤A端,则将配线架z端配线架加入下一个站点
							pointMap.add(jumper_frame.getFrame_z_id());
							
							//如果光纤z端对应配线架跳纤a端，则用配线架z端参数去寻找下一个光纤，并将其复制给fiber_core
							fiber_core = fiberCoreDao.findByFrameIdandPortOrder(jumper_frame.getFrame_z_id(), jumper_frame.getPort_order_z());
							if(fiber_core == null)//如果未找到这样的光纤，说明未连通，退出局站循环，回到下一个预备光路
								break;
							else//如果找到了这样的光纤，则进行相应的记录
							{
								routeMap.add(fiber_core.getId());//设置下一个路由路线：光纤
								
								//如果该光纤A端对应配线架z端，则将该光纤z端连接配线架设置为下一个站点
								if(fiber_core.getFrame_a_id() == jumper_frame.getFrame_z_id())
									pointMap.add(fiber_core.getFrame_z_id());
								else
									pointMap.add(fiber_core.getFrame_a_id());
							}
						}
						else//光纤Z端对应配线架跳纤Z端
						{
							//如果光纤z端对应配线架跳纤z端,则将配线架跳纤a端连接配线架加入下一个站点
							pointMap.add(jumper_frame.getFrame_a_id());//加入配线架跳纤a端对应配线架
							
							//如果光纤z端对应配线架跳纤z端，则用配线架a端参数去寻找下一个光纤，并将其复制给fiber_core
							fiber_core = fiberCoreDao.findByFrameIdandPortOrder(jumper_frame.getFrame_a_id(), jumper_frame.getPort_order_a());
							if(fiber_core == null)//如果未找到这样的光纤，说明未连通，退出局站循环，回到下一个预备光路
								break;
							else//如果找到了这样的光纤，则进行相应的记录
							{
								routeMap.add(fiber_core.getId());//设置下一个路由路线：光纤
								
								//如果该光纤A端对应配线架a端，则将该光纤z端连接配线架设置为下一个站点
								if(fiber_core.getFrame_a_id() == jumper_frame.getFrame_a_id())
									pointMap.add(fiber_core.getFrame_z_id());
								else
									pointMap.add(fiber_core.getFrame_a_id());
								
							}//第四层if-else
						}//第三层if-else
					}//第二层if-else
				}//第一层if-else
			}//内while
			if(routeMap.size() == 2*(stationNumber-1))//局站是否遍历，如果是，则表明找到了符合条件的一条光路拓扑图
			{
				//获取该光路起点的RTU名称
				String rtuName = rtuDao.findEntity(Rtus.class, pointMap.get(0)).getRtu_name();
				/**获取该光路起点的RTU端口序号
				 * 用1-64的标号
				 * **/
				Jumper_routes jumper=jumperRouteDao.findEntity(Jumper_routes.class, routeMap.get(0));
				int rtuPortOrder = jumper.getOtdr_port_order()+(jumper.getModelOrder()-1)*8;//端口序号，1-64
				//获取该拓扑图的最后一个元素：光纤，从而指定其终点端的配线架信息和端口信息
				Fiber_cores topologicalFiberCore = fiberCoreDao.findEntity(Fiber_cores.class, routeMap.get(routeMap.size()-1));
				String frameName = "";
				int framePortOrder = 0;
				if(topologicalFiberCore.getStation_a_id() == ids.get(stationNumber-1))//判断光纤A端是否为尾端
				{
					//获取该光路终点的配线架名称
					frameName = frameDao.findEntity(Distributing_frames.class, topologicalFiberCore.getFrame_a_id()).getFrame_name();
					// 获取该该光路终点的配线架端口序号
					framePortOrder = topologicalFiberCore.getPort_order_a();
				}
				else//光纤Z端为尾端
				{
					//获取该光路终点的配线架名称
					frameName = frameDao.findEntity(Distributing_frames.class, topologicalFiberCore.getFrame_z_id()).getFrame_name();
					// 获取该该光路终点的配线架端口序号
					framePortOrder = topologicalFiberCore.getPort_order_z();
				}

				
				//用容器将其封装
				Map<String,Object> topologicalMessage = new LinkedHashMap<String,Object>();
				topologicalMessage.put("rtuName", rtuName);//封装rtu名字,String
				topologicalMessage.put("rtuPortOrder", rtuPortOrder);//封装rtu端口序号,int 1-64
				topologicalMessage.put("frameName", frameName);//封装配线架名字,String
				topologicalMessage.put("framePortOrder", framePortOrder);//封装配线架端口序号,int
				topologicalMessage.put("preparatoryId", preparatory_id);//封装该拓扑图对应的预备光路id
				topologicalMessage.put("topologicalRoute", routeMap);//封装光路拓扑图,List<Serializable>
				topologicalMessage.put("topologicalPoint", pointMap);//封装站点拓扑图,List<Serializable>
				topologicalMessage.put("topologicalStation",ids);//封装局站拓扑图,List<Serializable>
				
				//装入返回结果中
				routeCandidates.add(topologicalMessage);
			}
		}//外while	
		return routeCandidates;
	}//public
	
//新增光路(Routes)
	@SuppressWarnings("unchecked")
	public Serializable addRoute(Routes route,Map<String,Object> topologies)
	{
		Serializable id = routeDao.addEntity(route);
		//将该光路对应的预备光路状态设置为激活状态
		Preparatory_routes preparatory_route = preparatoryRouteDao.findEntity(Preparatory_routes.class, route.getPreparatoty_id());
		preparatory_route.setStatus(true);
		preparatoryRouteDao.alterEntity(preparatory_route);//更新预备光路状态
		
		/**新增光路，自动为该光路添加光路拓扑图：线和站点**/
		//增加光路拓扑图：线
		List<Serializable> topologicRoutes = (List<Serializable>) topologies.get("topologicalRoute");
		for(int i=0; i<topologicRoutes.size(); i++)
		{
			Topologic_routes topologic_route = new Topologic_routes();
			topologic_route.setRoute(route);
			topologic_route.setTopologic_order(i+1);
			topologic_route.setTopologic_id((Long)topologicRoutes.get(i));
			if(0 == i)
			{
				topologic_route.setTopologic_type("光路跳纤");
				Jumper_routes jumper_route = jumperRouteDao.findEntity(Jumper_routes.class, topologicRoutes.get(i));
				jumper_route.setRoute_id((Long)id);
				jumperRouteDao.alterEntity(jumper_route);//跟新光路跳纤的光路ID字段信息
			}
			else if(1 == i%2)
			{
				topologic_route.setTopologic_type("光纤");
				Fiber_cores fiber_core = fiberCoreDao.findEntity(Fiber_cores.class, topologicRoutes.get(i));
				fiber_core.setRoute_id((Long)id);
				fiberCoreDao.alterEntity(fiber_core);//跟新光纤的光路ID字段信息
			}
			else
			{
				topologic_route.setTopologic_type("配线架跳纤");
				Jumper_frames jumper_frame = jumperFrameDao.findEntity(Jumper_frames.class, topologicRoutes.get(i));
				jumper_frame.setRoute_id((Long)id);
				jumperFrameDao.alterEntity(jumper_frame);//跟新配线架跳纤的光路ID字段信息
			}
			topologicRouteDao.addEntity(topologic_route);
		}
		//增加光路拓扑图：站点
		List<Serializable> topologicPoints = (List<Serializable>) topologies.get("topologicalPoint");
		for(int i=0; i<topologicPoints.size(); i++)
		{
			Topologic_points topologic_point = new Topologic_points();
			topologic_point.setRoute(route);
			if(0 == i)
				topologic_point.setTopologic_type("RTU");
			else 
				topologic_point.setTopologic_type("配线架");
			topologic_point.setTopologic_order(i+1);
			topologic_point.setTopologic_id((Long)topologicPoints.get(i));
			topologicPointDao.addEntity(topologic_point);
		}
		return id;
	}
	
//新增地标(Landmarks)
	public Serializable addLandmark(Landmarks landmark)
	{
		return landmarkDao.addEntity(landmark);
	}
	
//新增优化测试参数(Optimize_parameters)
	public Serializable addOptimizeParameter(Optimize_parameters optimize_parameter)
	{
		return optimizeParameterDao.addEntity(optimize_parameter);
	}
	
//新增周期测试参数(Period_parameters)
	public Serializable addPeriodParameter(Period_parameters period_parameter)
	{
		return periodParameterDao.addEntity(period_parameter);
	}
	
//新增回传曲线(Curves)
	public Serializable addCurve(Curves curve)
	{
		return curveDao.addEntity(curve);
	}
	
//新增回传测试参数(Parameter_curves)
	public Serializable addParameterCurve(Parameter_curves parameter_curve)
	{
		return parameterCurveDao.addEntity(parameter_curve);
	}
	
//新增回传事件(Event_curves)
	public Serializable addEventCurve(Event_curves event_curve)
	{
		return eventCurveDao.addEntity(event_curve);
	}
	
//新增纤芯A端和配线架端口的连接关系
	public boolean addConnectionTerminalAFiberCoreAndFramePort(List<Serializable> core_ids,Serializable frame_id,List<Integer> frame_int)
	{
		//判断传入参数是否配对
		if(core_ids.size() == frame_int.size())
		{
			for(int i=0; i<frame_int.size(); i++)
			{
				Serializable core_id = core_ids.get(i);
				int frame_port_order = frame_int.get(i);
				//查询光纤并修改光纤A端信息
				Fiber_cores fiberCore = fiberCoreDao.findEntity(Fiber_cores.class, core_id);
				fiberCore.setFrame_a_id((long)frame_id);
				fiberCore.setPort_order_a(frame_port_order);
				fiberCoreDao.alterEntity(fiberCore);
				//查询配线架端口并修改端口信息
				Frame_ports framePort = framePortDao.findByFrameIdAndPortId(frame_id, frame_port_order);
				framePort.setConnection_type("光纤");
				framePort.setConnection_id((long)core_id);
				framePort.setStatus(true);
			}
			return true;
		}
		return false;
	}
	
//新增纤芯Z端和配线架端口的连接关系
	public boolean addConnectionTerminalZFiberCoreAndFramePort(List<Serializable> core_ids,Serializable frame_id,List<Integer> frame_int)
	{
		//判断传入参数是否配对
		if(core_ids.size() == frame_int.size())
		{
			for(int i=0; i<frame_int.size(); i++)
			{
				Serializable core_id = core_ids.get(i);
				int frame_port_order = frame_int.get(i);
				//查询光纤并修改光纤Z端信息
				Fiber_cores fiberCore = fiberCoreDao.findEntity(Fiber_cores.class, core_id);
				fiberCore.setFrame_z_id((long)frame_id);
				fiberCore.setPort_order_z(frame_port_order);
				fiberCoreDao.alterEntity(fiberCore);
				//查询配线架端口并修改端口信息
				Frame_ports framePort = framePortDao.findByFrameIdAndPortId(frame_id, frame_port_order);
				framePort.setConnection_type("光纤");
				framePort.setConnection_id((long)core_id);
				framePort.setStatus(true);
			}
			return true;
		}
		return false;
	}
//	新增告警组
	public Serializable addPriorities(Priorities priority){
		return priorityDao.addEntity(priority);
	}
//新增暂存告警记录
	public Serializable addTempGroupInfo(TempGroupInfo tempGroupInfo){
		return tempGroupInfoDao.addEntity(tempGroupInfo);
	}
//新增告警方式
	public Serializable addAlarmWay(Alarm_ways alarmWay){
		
		return alarmWayDao.addEntity(alarmWay);
	}
//新增配对组	
	public Serializable addProtectGroup(Protect_groups protect_group){
		return protectGroupDao.addEntity(protect_group);
	}
//新增光功率数据记录
	public Serializable addOpticalPower(Optical_powers optical_power ){
		return opticalPowerDao.addEntity(optical_power);
	}
//新增告警信息
	public Serializable addAlarm(Alarm alert)
	{
		return alarmDao.addEntity(alert);
	}
//新增角色
	public Serializable addRole(Role role){
		return roleDao.addEntity(role);
	}
// 新增权限
	public Serializable addPermission(Permissions permission){
		return permissionDao.addEntity(permission);
	}
//新增用户
	public Serializable addUser(User user){
		return userDao.addEntity(user);
	}
//新增门限
	public Serializable addThreshold(Threshold threshold){
		return thresholdDao.addEntity(threshold);
	}	
//新增值班表信息
	public Serializable addDutySchedule(Duty_schedule duty_schedule){
		return dutyScheduleDao.addEntity(duty_schedule);
	}
//新增值班人员
	public Serializable addDutyOperator(Duty_operator duty_operator){
		return dutyOperatorDao.addEntity(duty_operator);
	}
/****/
	public Serializable addVerifyCode(Verify_codes verifyCode){
		return verifyCodeDao.addEntity(verifyCode);
	}
	//新增日志
	public Serializable addLog(Log log){
		return logDao.addEntity(log);
	}
/**新增系统记录**/
	public Serializable addSystem(SystemInfo system){
		return systemDao.addEntity(system);
	}
/**新增告警经验**/	
	public Serializable addAlarmBank(AlarmBank alarmBank){
		return alarmBankDao.addEntity(alarmBank);
	}
//增加序列号
	public Serializable addSNCode(SerialCode sNCode){
		  return serialCodeDao.addEntity(sNCode);
	}	
}
