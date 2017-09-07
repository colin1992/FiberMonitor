package serviceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import dao.*;
import domain.*;
import service.DeleteService;

public class DeleteServiceImpl implements DeleteService
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
	@SuppressWarnings("unused")
	private		EventCurveDao			eventCurveDao;			//22.注入对回传事件的操作
	private		TopologicRouteDao		topologicRouteDao;		//23.注入对光路拓扑图-线的操作
	@SuppressWarnings("unused")
	private		TopologicPointDao		topologicPointDao;		//24.注入对光路拓扑图-站点的操作
	private		PriorityDao		        priorityDao;		    //25.注入对告警组的操作
	@SuppressWarnings("unused")
	private		TempGroupInfoDao	    tempGroupInfoDao;	   //26.注入对暂存告警组的操作
	@SuppressWarnings("unused")
	private		AlarmWayDao	            alarmWayDao;	        //27.注入对告警方法的操作
	private     ProtectGroupDao         protectGroupDao;        //28.注入对配对组的操作
	private     OpticalPowerDao         opticalPowerDao;        //29.注入对光功率值得操作
	private     AlarmDao                alarmDao;               //30.注入对告警信息的操作 
	private     RoleDao           		roleDao;          		//31.注入对角色的操作 
	private     PermissionDao           permissionDao;          //32.注入对权限的操作
	private     UserDao           		userDao;          		//33.注入对用户的操作 
	private     ThresholdDao           	thresholdDao;          	//34.注入对门限的操作 
	private     DutyScheduleDao         dutyScheduleDao;        //35.注入对值班表的操作
	private     DutyOperatorDao         dutyOperatorDao;        //36.注入对值班人员的操作
	private     VerifyCodeDao           verifyCodeDao;          //37.注入对验证码的操作   
	private     LogDao                  logDao;                 //38.注入对日志的操作
	@SuppressWarnings("unused")
	private     SystemDao               systemDao;              //39.注入对系统的操作
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
	public void setSystemDao(SystemDao systemdao ){
		this.systemDao=systemdao;
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
//删除区域(Areas)
	public void deleteArea(Serializable id){
		List<Stations> stations = stationDao.findAllByAreaId(id);
		for(int k = 0 ;k<stations.size();k++){
			List<Jumper_routes> jumperRoutes = jumperRouteDao.findAllByStationId(stations.get(k).getId());
			for(int i=0;i<jumperRoutes.size();i++){
				deleteJumperRoute(jumperRoutes.get(i).getId());
			}
			List<Optical_cables> cables = opticalCableDao.findAllByStationId(stations.get(k).getId());
			for(int i = 0 ;i<cables.size();i++){
				deleteOpticalCable(cables.get(i).getId()); 
			}
		}
		areaDao.deleteEntity(Areas.class, id);
	}
//删除局站(Stations)
	public void deleteStation(Serializable id) {
		//删除备纤光源跳纤
		List<Jumper_backups> jumperBackups = jumperBackupDao.findAllByStationId(id);
		for(Jumper_backups jumperBackup: jumperBackups){
			deleteJumperBackup(jumperBackup.getId());
		}
		//删光路跳纤
		List<Jumper_routes> jumperRoutes = jumperRouteDao.findAllByStationId(id);
		for(int i=0;i<jumperRoutes.size();i++){
			deleteJumperRoute(jumperRoutes.get(i).getId());
		}
		//删配线架跳纤
		List<Jumper_frames> jumperFrame = jumperFrameDao.findAllByStationId(id);
		for(int i=0;i<jumperFrame.size();i++){
			deleteJumperFrame(jumperFrame.get(i).getId());
		}
		//删光纤
		List<Optical_cables> cables=opticalCableDao.findAllByStationId(id);
		if(cables.size()>0){
			for(int i = 0 ;i<cables.size();i++){
				deleteOpticalCable(cables.get(i).getId());
			}
		}
		try{
			stationDao.deleteEntity(Stations.class, id);
		}
		catch(Exception e){
			
		}
		
	}
	
//删除地标(Landmarks)
	public void deleteLandmark(Serializable id){
		landmarkDao.deleteEntity(Landmarks.class, id);
	}
	
//删除机柜(Cabinets)
	public void deleteCabinet(Serializable id){
		/**删除机柜，自动检测并删除该机柜上的RTU和配线架
		 * 同时，检测删除RTU和配线架后两端置空的光路跳纤和配线架跳纤，
		 * 将其一并删除**/
		Cabinets cabinet = cabinetDao.findEntity(Cabinets.class, id);
		List<Racks> racks =cabinet.getRacks();
		for(Racks rack:racks){
			if(rack.getThing_type() != null){
				switch (rack.getThing_type()){
					case "RTU":{
						deleteRtu(rack.getThing_id());
						break;
					}
					case "配线架":{
						deleteDistributingFrame(rack.getThing_id());
						break;
					}
					default :
						break;		
				}
			}
		}
		cabinetDao.deleteEntity(Cabinets.class, id);
		//检测光路跳纤和配线架跳纤
		List<Jumper_frames> jumper_frames = jumperFrameDao.findAllNullByStationId(cabinet.getStation().getId());
		if(jumper_frames != null){
			for(Jumper_frames jumper_frame : jumper_frames){
				jumperFrameDao.deleteEntity(Jumper_frames.class,jumper_frame.getId());
			}
		}
		List<Jumper_routes> jumper_routes = jumperRouteDao.findAllNullByStationId(cabinet.getStation().getId());
		if(jumper_routes != null){
			for(Jumper_routes jumper_route : jumper_routes){
				jumperRouteDao.deleteEntity(Jumper_routes.class, jumper_route.getId());
			}
		}
	}
	
//删除机架(Rakcs)
	@SuppressWarnings("unused")
	private void deleteRack(Serializable id){
		rackDao.deleteEntity(Racks.class, id);
	}
	
//删除配线架(Distributing_frames)
	public void deleteDistributingFrame(Serializable id){
		/**删除配线架，自动将连接在配线架端口上的所有
		 * 光路跳纤、配线架跳纤、光纤对应端置空
		 * 同时判断该配线架上是否设置有光路，如果有则将对应光路删除**/
		//检测被占用端口
		List<Frame_ports> frame_ports = framePortDao.findAllByFrameId(id);
		for(Frame_ports frame_port : frame_ports){
			if(frame_port.getStatus()){//检测到该端口被占用
				String connected_type = frame_port.getConnection_type();
				switch (connected_type){
					case "配线架跳纤":{
						//查询该端口连接的配线架跳纤
						Jumper_frames jumper_frame = jumperFrameDao.findEntity(Jumper_frames.class, frame_port.getConnection_id());
						//判断配线架跳纤是否挂载光路
						if(jumper_frame != null){
							if(jumper_frame.getRoute_id() !=null)//挂载了光路
								deleteRoute(jumper_frame.getRoute_id());
							if(jumper_frame.getFrame_a_id()==id){
								//根据配线架id和端口序号查询预备光路，如果找到预备光路
								Preparatory_routes preparatory_route = preparatoryRouteDao.findByEndFrameIdAndPortOrder(id, jumper_frame.getPort_order_a());
								if(preparatory_route != null){
									//删除光路跳纤
									deleteJumperRoute(preparatory_route.getJumper_route().getId());
								}
								jumper_frame.setFrame_a_id(null);//置该配线架跳纤A端连接配线架id为空
								jumper_frame.setFrame_a_name(null);//置该配线架跳纤A端连接配线架名称为空
								jumper_frame.setPort_order_a(0);//置该配线架跳纤A端连接配线架端口序号为0
							}
							else{
								//根据配线架id和端口序号查询预备光路，如果找到预备光路
								Preparatory_routes preparatory_route = preparatoryRouteDao.findByEndFrameIdAndPortOrder(id, jumper_frame.getPort_order_z());
								if(preparatory_route != null){
									//删除光路跳纤
									deleteJumperRoute(preparatory_route.getJumper_route().getId());
								}
								jumper_frame.setFrame_z_id(null);//置该配线架跳纤Z端连接配线架id为空
								jumper_frame.setFrame_z_name(null);//置该配线架跳纤Z端连接配线架名称为空
								jumper_frame.setPort_order_z(0);//置该配线架跳纤Z端连接配线架端口序号为0
							}
							//更新配线架跳纤信息
							jumperFrameDao.alterEntity(jumper_frame);
							break;
						}
					}
					case "光路跳纤":
					case "切换跳纤":{
					    //查询该端口连接的光路跳纤，并将其删除,此时将级联删除预备光路
						Jumper_routes jumper_route = jumperRouteDao.findEntity(Jumper_routes.class, frame_port.getConnection_id());
						if(jumper_route != null){
							deleteJumperRoute(jumper_route.getId());
							break;
						}
					}
					default :
						break;
				}
			}
			/**查询该端口对应的光纤，判断该光纤是否是光路起始端光纤，
			 * 如果是，则要删除光路跳纤，级联删除预备光路**/
			Fiber_cores fiber_core = fiberCoreDao.findByFrameIdandPortOrder(id, frame_port.getPort_order());
			if(fiber_core != null){
				fiber_core.setStatus(false);
				//将连接的光纤状态设置为未使用
				if(fiber_core.getRoute_id() != null)
					deleteRoute(fiber_core.getRoute_id());
				//如果该光纤为预备光路上的光纤，则删除光路跳纤
				Preparatory_routes preparatory_route = preparatoryRouteDao.findByFiberCoreId(fiber_core.getId());
				if(preparatory_route != null){
					deleteJumperRoute(preparatory_route.getJumper_route().getId());
				}
				if(fiber_core.getFrame_a_id()== id){//判断该光纤A端是否与该端口相连
					fiber_core.setFrame_a_id(null);
					fiber_core.setPort_order_a(0);
					fiber_core.setFrame_a_name(null);
				}
				else{
					fiber_core.setFrame_z_id(null);
					fiber_core.setPort_order_z(0);
					fiber_core.setFrame_z_name(null);
				}
				fiberCoreDao.alterEntity(fiber_core);
			}
		}
		frameDao.deleteEntity(Distributing_frames.class, id);
	}
	
//删除配线架端口(Frame_ports)
	@SuppressWarnings("unused")
	private void deleteFramePort(Serializable id){
		framePortDao.deleteEntity(Frame_ports.class, id);
	}
	
//删除RTU(Rtus)
	public void deleteRtu(Serializable id){
		/**删除RTU，检测是否有端口占用,若占用，则将连接的光路跳纤删除，将对应光路删除,
		 * 删除连接RTU的跳纤
		 * 同时删除该RTU连接的所有预备光路**/
		Rtus rtu=rtuDao.findEntity(Rtus.class,id);
		if(rtu.getType().contains("普通")){//普通RTU
			List<Rtu_ports> rtu_ports = rtuPortDao.findAllByRtuId(id);//获取该RTU下的所有端口
			for(Rtu_ports rtu_port : rtu_ports){
				if(rtu_port.getStatus()){//检测端口是否占用
					//如果端口被占用，则删除对应的光路
					Routes route = routeDao.findByRtuOrderAndID(id, (rtu_port.getModuleOrder()-1)*8+rtu_port.getPort_order());
					if(route!= null){//查询到光路，则删除光路，自动删除光路关联的参数
						deleteRoute(route.getId());
					}
					//将连接的光路跳纤删除，同时将配线架端口置空
					Jumper_routes jumper_route = jumperRouteDao.findByRtuIdAndPortMoudleOrder(id,rtu_port.getPort_order(),rtu_port.getModuleOrder());
					try{
						Frame_ports frame_port = framePortDao.findByFrameIdAndPortOrder(jumper_route.getFrame_id(), jumper_route.getFrame_port_order());
						jumperRouteDao.deleteEntity(Jumper_routes.class, jumper_route.getId());
						frame_port.setConnection_type(null);
						frame_port.setConnection_id(null);
						frame_port.setStatus(false);
						framePortDao.alterEntity(frame_port);
						//将该端口下的预备光路删除
						Preparatory_routes preparatory_route = preparatoryRouteDao.findByRtuIdAndPortOrder(id, rtu_port.getPort_order());
						if(preparatory_route != null){
							preparatoryRouteDao.deleteEntity(Preparatory_routes.class, preparatory_route.getId());
						}
					}
					catch(Exception e){
						e.printStackTrace();
					}
				 }
			}
		} 
		else if(rtu.getType().contains("切换")){//切换RTU  删除切换跳纤
			List<Rtu_ports> rtu_ports=rtuPortDao.findSwitchRtuPortsByRtuId((Long) id);//获取该RTU下的所有端口
			if(rtu_ports!=null){
				for(Rtu_ports port:rtu_ports){
					if(port.getJumper_route_id()!=null){//删除切换跳纤
						deleteJumperRoute(port.getJumper_route_id());
					}
					rtuPortDao.deleteEntity(Rtu_ports.class, port.getId());	
				}
			}
		}
		else if(rtu.getType().contains("备纤光源")){//备纤光源RTU
			List<Rtu_ports> rtu_ports=rtuPortDao.findRtuInPortsByRtuId((Long)id);//获取该RTU下的所有IN端口
			if(rtu_ports!=null){
				for(Rtu_ports port:rtu_ports){
					if(port.getJumper_route_id()!=null){//删除备纤光源跳纤
						deleteJumperBackup(port.getJumper_route_id());
					}
					rtuPortDao.deleteEntity(Rtu_ports.class, port.getId());	
				}
			}
		}
		rtuDao.deleteEntity(Rtus.class, id);
	}	
	
//删除RTU端口(Rtu_ports)
	public void deleteRtuPort(Serializable id){
		rtuPortDao.deleteEntity(Rtu_ports.class, id);
	}
	//删除光缆(Optival_cables)
	public void deleteOpticalCable(Serializable id){
		/**删除光缆前，检测该光缆里是否存在光纤上挂载光路**/
		//删除光缆地标
		List<Landmarks> landmark=landmarkDao.findAllByOpticalCableId(id);//获取光缆的地标
        if(landmark!=null){
        	for(int i=0;i<landmark.size();i++){
        		deleteLandmark(landmark.get(i).getId());
        	}
        }
		//首先查询所有光纤
		List<Fiber_cores> fiber_cores = fiberCoreDao.findAllByOpticalCableId(id);
		/**删除光缆,自动删除所有光纤**/
		for(Fiber_cores fiber_core : fiber_cores){
			//检测光纤是否挂载光路
			if(fiber_core.getRoute_id() != null)//如果挂载光路，则删除光路，并修改该光路连接线上的信息
				deleteRoute(fiber_core.getRoute_id());
			
			//查询该光纤是否属于预备光路中的光纤，如果是，则需要将对应的预备光路删除
			Preparatory_routes preparatory_route = preparatoryRouteDao.findByFiberCoreId(fiber_core.getId());//////////////////////////
			if(preparatory_route != null){//如果该光纤属于预备光路中的光纤,删除光路跳纤,级联删除预备光路
				Jumper_routes jumper_route = jumperRouteDao.findEntity(Jumper_routes.class, preparatory_route.getJumper_route().getId());
				if(jumper_route.getRoute_id() != null)//如果该光路跳纤上挂载光路，则删除该光路，并更新相关信息
					deleteRoute(jumper_route.getRoute_id());
				jumperRouteDao.deleteEntity(Jumper_routes.class, preparatory_route.getJumper_route().getId());
				/**删除光路跳纤，更新配线架跳纤连接的配线架端口
				 * 同时，级联删除该跳纤对应的预备光路
				 * 注意，此时的预备光路已经被级联删除**/
				//查询光路跳纤连接的配线架端口
				Frame_ports frame_port = framePortDao.findByJumperRouteAndConnectionId(preparatory_route.getJumper_route().getId());
				if(frame_port != null){
					frame_port.setConnection_id(null);
					frame_port.setConnection_type(null);
					frame_port.setStatus(false);
					framePortDao.alterEntity(frame_port);
				}
				//查询光路跳纤连接的RTU端口
				Rtu_ports rtu_port = rtuPortDao.findByJumperRouteId(preparatory_route.getJumper_route().getId());
				if(rtu_port != null){
					rtu_port.setJumper_route_id(null);
					rtu_port.setStatus(false);
					rtuPortDao.alterEntity(rtu_port);
				}
			}
			//查询纤芯对应的配线架端口
			List<Frame_ports> frame_ports=new ArrayList<>();
			if(fiber_core.getPort_order_a()!=0){
				Frame_ports port=framePortDao.findByFrameIdAndPortOrder( fiber_core.getFrame_a_id(),fiber_core.getPort_order_a());
				if(port!=null){
					frame_ports.add(port);
				}
			}
			if(fiber_core.getPort_order_z()!=0){
				Frame_ports port=framePortDao.findByFrameIdAndPortOrder( fiber_core.getFrame_z_id(),fiber_core.getPort_order_z());
				if(port!=null){
					frame_ports.add(port);
				}
			}
			if(frame_ports.size()>0){
				for(Frame_ports frame_port : frame_ports){
					frame_port.setHas_fiber(false);//将连接fiber设置为空
					framePortDao.alterEntity(frame_port);//更新
				}
			}
		}
		opticalCableDao.deleteEntity(Optical_cables.class, id);
	}

	
//删除纤芯(Fiber_cores)	
	@SuppressWarnings("unused")
	private void deleteFiberCore(Serializable id){
		fiberCoreDao.deleteEntity(Fiber_cores.class, id);
	}
	
//删除配线架跳纤(Jumper_frames)
	public void deleteJumperFrame(Serializable id){
		/**删除配线架跳纤前，判断是否挂载光路**/
		Jumper_frames jumper_frame = jumperFrameDao.findEntity(Jumper_frames.class, id);
		if(jumper_frame.getRoute_id() != null)
			deleteRoute(jumper_frame.getRoute_id());

		jumperFrameDao.deleteEntity(Jumper_frames.class, id);
		/**删除配线架跳纤，更新配线架跳纤连接的配线架端口**/
		//查询配线架跳纤两端的连接配线架端口
		List<Frame_ports> frame_ports = framePortDao.findByJumperFrameAndConnectionId(id);
		if(frame_ports != null){
			for(Frame_ports framePort : frame_ports){
				framePort.setConnection_id(null);
				framePort.setConnection_type(null);
				framePort.setStatus(false);
			}
		}
	}

//删除光路跳纤(Jumper_routes)
	public void deleteJumperRoute(Serializable id){
		/**删除光路跳纤前，判断是否挂载光路**/
		Jumper_routes jumper_route = jumperRouteDao.findEntity(Jumper_routes.class, id);
		Rtu_ports rtu_port=null;
		//如果是光路跳纤则删除该光路，并更新相关信息
		//如果是切换跳纤，将光路的切换跳纤置空，解除切换端口的占用
		if(jumper_route!=null){
			 if(!jumper_route.getIsSwitch()){//光路跳纤
				    if(jumper_route.getRoute_id()!=null){//如果该光路跳纤上挂载光路
					  deleteRoute(jumper_route.getRoute_id());
				    }
					/**删除光路跳纤，更新配线架跳纤连接的配线架端口
					 * 同时，级联删除该跳纤对应的预备光路
					 * 注意，此时的预备光路已经被级联删除**/
					//查询光路跳纤连接的RTU端口
					 rtu_port = rtuPortDao.findByJumperRouteId(id);
			}
			    /**
			     * 切换跳纤,设置光路的切换信息为空
			     * 解除切换RTU和配线架端口的占用
			     * **/
			  else{
			    	if(jumper_route.getRoute_id()!=null){
			    		Routes route=routeDao.findEntity(Routes.class, jumper_route.getRoute_id());
				    	if(route!=null){
				    		route.setSwitchJumperId(null);
					    	route.setSwitchRtuId(null);
					    	route.setSwitchRtuOrder(null);
					    	routeDao.alterEntity(route);
					    }
			    	}
			    	rtu_port=rtuPortDao.findByRtuIdAndModelOrderAndPortOrder(jumper_route.getRtu_id(),jumper_route.getModelOrder(), jumper_route.getOtdr_port_order());
			    }
			//查询光路跳纤连接的配线架端口
			    Frame_ports framePort = framePortDao.findByFrameIdAndPortId(jumper_route.getFrame_id(), jumper_route.getFrame_port_order());
				if(framePort!=null){//解除配线架端口占用
						framePort.setConnection_type(null);
						framePort.setConnection_id(null);
						framePort.setStatus(false);
						framePortDao.alterEntity(framePort);
				}
				if(rtu_port!=null){//解除切换RTU端口占用
					rtu_port.setJumper_route_id(null);
					rtu_port.setStatus(false);
					rtuPortDao.alterEntity(rtu_port);
				}
			try{
				jumperRouteDao.deleteEntity(Jumper_routes.class, id);
			}
			catch(Exception e){
				
			}
			
		  }
	}
	
//删除备纤光源跳纤
	public void deleteJumperBackup(Serializable id){
		/**删除跳纤前，判断是否挂载光路**/
		Jumper_backups jumper_backup= jumperBackupDao.findEntity(Jumper_backups.class, id);
		if(jumper_backup!=null){
			if(jumper_backup.getRoute_id()!=null){
	    		Routes route=routeDao.findEntity(Routes.class, jumper_backup.getRoute_id());
		    	if(route!=null){
		    		route.setSwitchJumperId(null);
			    	route.setBackupRtuId(null);
			    	route.setBackupRtuOrder(null);
			    	routeDao.alterEntity(route);
			    }
	    	}
		}
		Rtu_ports rtu_port=rtuPortDao.findInPortByRtuIdAndOrder(jumper_backup.getRtu_id(), jumper_backup.getIn_port_order());
		//查询光路跳纤连接的配线架端口
		Frame_ports framePort = framePortDao.findByFrameIdAndPortId(jumper_backup.getFrame_id(), jumper_backup.getFrame_port_order());
		if(framePort!=null){//解除配线架端口占用
				framePort.setConnection_type(null);
				framePort.setConnection_id(null);
				framePort.setStatus(false);
				framePortDao.alterEntity(framePort);
		}
		if(rtu_port!=null){//解除切换RTU端口占用
			rtu_port.setJumper_route_id(null);
			rtu_port.setStatus(false);
			rtuPortDao.alterEntity(rtu_port);
		}
		jumperBackupDao.deleteEntity(Jumper_backups.class, id);
	}
//删除告警
	public boolean deleteAlarm(Serializable id){
		boolean status=true;
		try{
		  alarmDao.deleteEntity(Alarm.class, id);
		}catch(Exception e){
			status=false;
		}
		return status;
	}
//删除光路(Routes)
	public boolean deleteRoute(Serializable id){
		
		/**删除光路，自动删除优化参数和周期测试参数，以及曲线
		 * 同时，更改该光路对应预备光路的状态
		 * 删除告警，删除光功率值,删除光功率门限
		 * 如果存在切换RTU，应该解除切换RTU的端口占用
		 * 如果是备纤，连接了检测光功率的RTU，解除端口占用
		 * 
		 * **/ 
		Routes route = routeDao.findEntity(Routes.class,id);
		if(route.getSwitchRtuId()!=null){//连接有切换RTU或备纤光源RTU
			Jumper_routes jumper=jumperRouteDao.findEntity(Jumper_routes.class, route.getSwitchRtuId());
			if(jumper!=null){
				/**切换跳纤:解除切换RTU端口占用**/
				if(jumper.getIsSwitch()){
					Rtu_ports rtu_port=rtuPortDao.findByRtuIdAndModelOrderAndPortOrder(route.getSwitchRtuId(), jumper.getModelOrder(), jumper.getOtdr_port_order());
					rtu_port.setJumper_route_id(null);
					rtu_port.setStatus(false);
					rtuPortDao.alterEntity(rtu_port);
				}
				else{//备纤光源RTU
					Rtu_ports rtu_port=rtuPortDao.findInPortByRtuIdAndOrder(route.getSwitchRtuId(), route.getRtu_port_order());
					if(rtu_port!=null){//解除切换RTU端口占用
						rtu_port.setJumper_route_id(null);
						rtu_port.setStatus(false);
						rtuPortDao.alterEntity(rtu_port);
					}
				}
			} 
		}
		List<Topologic_routes> topologic_routes = route.getTopologic_routes();
		if(topologic_routes.size()==0)
			topologic_routes=topologicRouteDao.findByRouteId((Long)id);
		for(int i=0; i<topologic_routes.size(); i++){//修改该光路拓扑图上的所有连接线的信息
			switch (topologic_routes.get(i).getTopologic_type()){
				case "光路跳纤":{
					Jumper_routes jumper_route = jumperRouteDao.findEntity(Jumper_routes.class, topologic_routes.get(i).getTopologic_id());
					jumper_route.setRoute_id(null);
					jumperRouteDao.alterEntity(jumper_route);
					break;
				}
				case "光纤":{
					Fiber_cores fiber_core = fiberCoreDao.findEntity(Fiber_cores.class, topologic_routes.get(i).getTopologic_id());
					fiber_core.setRoute_id(null);
					fiberCoreDao.alterEntity(fiber_core);
					break;
				}
				case "配线架跳纤":{
					Jumper_frames jumper_frame_other = jumperFrameDao.findEntity(Jumper_frames.class, topologic_routes.get(i).getTopologic_id());
					jumper_frame_other.setRoute_id(null);
					jumperFrameDao.alterEntity(jumper_frame_other);
					break;
				}
				default:
					break;
			}
		}
		//删除告警
		List<Alarm> alarm=alarmDao.findByRouteId((long)id);
		if(alarm!=null){
			if(alarm.size()>0){
				for(int index=0;index<alarm.size();index++){
					alarmDao.deleteEntity(Alarm.class, alarm.get(index).getId());
				}
			}
		}
		//删除光功率值
		List<Optical_powers> opticalPowers=opticalPowerDao.findOpticalPowersByRouteId((long)id);
		if(opticalPowers!=null){
			if(opticalPowers.size()>0){
				for(int index=0;index<opticalPowers.size();index++){
					opticalPowerDao.deleteEntity(Optical_powers.class, opticalPowers.get(index).getId());
				}
			}
		}
		//删除光功率门限
		Threshold thres=thresholdDao.findByRouteId(id);
		if(thres!=null){
			thresholdDao.deleteEntity(Threshold.class, thres.getId());
		}
		//删除保护组
		if(route.getIsProtect()){
			Protect_groups group= protectGroupDao.findProtectGroupsByRouteId(id);
			if(group!=null){
				protectGroupDao.deleteEntity(Protect_groups.class,group.getId());
			}
		}
		//更新预备光路状态
		Preparatory_routes preparatory_route = preparatoryRouteDao.findEntity(Preparatory_routes.class, route.getPreparatoty_id());
		if(preparatory_route != null){
			preparatory_route.setStatus(false);
			preparatoryRouteDao.alterEntity(preparatory_route);
		}
        routeDao.deleteEntity(Routes.class, id);
		boolean status=true;
		if(routeDao.findEntity(Routes.class, id)!=null)
			status=false;
		return status;
	}
	
//删除优化参数(Optimize_parameters)
	public void deleteOptimizeParameter(Serializable id){
		optimizeParameterDao.deleteEntity(Optimize_parameters.class, id);
	}
	
//删除周期测试参数(Period_parameters)
	public void deletePeriodParameter(Serializable id){
		periodParameterDao.deleteEntity(Period_parameters.class, id);
	}
	
//删除回传曲线(Curves)
	/**删除回传曲线，系统自动删除该曲线对应的回传测试参数和回传事件**/
	public void deleteCurve(Serializable id){
		curveDao.deleteEntity(Curves.class, id);
	}
//删除告警组
	public void deletePriority(Serializable id){
		 priorityDao.deleteEntity(Priorities.class, id);
	}
//删除配对组
	public void deleteProtectGroup(Serializable id){
		protectGroupDao.deleteEntity(Protect_groups.class, id);
	}
/**删除角色
 * 删除前先删除该角色下的用户
 * **/
	public void deleteRole(Serializable id){
		roleDao.deleteEntity(Role.class, id);
	}
/*****/
//删除权限
	public void deletePermission(Serializable id){
		permissionDao.deleteEntity(Permissions.class, id);
	}
/****/	
//删除用户
	public void deleteUser(Serializable id){
		userDao.deleteEntity(User.class, id);
	}
	
//删除门限
	public void deleteThreshold(Serializable id){
		thresholdDao.deleteEntity(Threshold.class, id);
	}	
/***/	
//删除值班表
	public void deleteDutySchedule(Serializable id){
		dutyScheduleDao.deleteEntity(Duty_schedule.class, id);
	}	
/****/	
//删除值班人员
	public void deleteDutyOperator(Serializable id){
			
	   dutyOperatorDao.deleteEntity(Duty_operator.class, id);
	}
//删除验证码记录
	public void deleteVerifyCode(Serializable id){
		verifyCodeDao.deleteEntity(Verify_codes.class, id);
	}
	/****/	
	//删除日志
	public void deleteLog(Serializable id){
				
		 logDao.deleteEntity(Log.class, id);
	}
/**删除告警经验**/	
	public void deleteAlarmBank(Serializable id){
		alarmBankDao.deleteEntity(AlarmBank.class, id);
	}
//删除序列号
    public void deleteSNCode(Long id){
    	serialCodeDao.deleteEntity(SerialCode.class, id);
	}	
}
