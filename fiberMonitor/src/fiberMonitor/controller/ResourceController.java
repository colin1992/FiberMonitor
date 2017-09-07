package fiberMonitor.controller;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.http.HttpException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.dom4j.DocumentException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import domain.Alarm_ways;
import domain.Areas;
import domain.Distributing_frames;
import domain.Fiber_cores;
import domain.Frame_ports;
import domain.Jumper_backups;
import domain.Jumper_frames;
import domain.Jumper_routes;
import domain.Landmarks;
import domain.Optical_cables;
import domain.Protect_groups;
import domain.Routes;
import domain.Rtu_ports;
import domain.Rtus;
import domain.Stations;
import domain.SystemInfo;
import fiberMonitor.bean.HttpClientUtil;
import fiberMonitor.bean.MessageUtil;
import fiberMonitor.bean.NumConv;
import fiberMonitor.bean.XmlCodeCreater;
import fiberMonitor.bean.XmlDom;
import fiberMonitor.bean.root;
import jxl.Sheet;
import jxl.Workbook;
import net.sf.json.JSONArray;
import service.AddService;
import service.AlterService;
import service.DeleteService;
import service.FindService;

@Controller
public class ResourceController {
	@Resource(name="findService")
	private FindService findService;
    @Resource(name="addService") 
	private  AddService addService;
    @Resource(name="alterService") 
   	private   AlterService alterService;
    @Resource(name="deleteService") 
   	private  DeleteService delService;
    
    public static void main(String args[]){
	
	}
public static void printAllRequestPara(HttpServletRequest request){
	Enumeration<?> paramNames = request.getParameterNames();  
    while (paramNames.hasMoreElements()) {  
    String paramName = (String) paramNames.nextElement();  
    String[] paramValues = request.getParameterValues(paramName);  
    if (paramValues.length == 1) {  
      String paramValue = paramValues[0];  
      if (paramValue.length() != 0) {  
        //System.out.println("参数：" + paramName + ":" + paramValue);  
       
      }  
    }  
   }  
}	
	/**----------------------------根据局站的信息查找局站---------------------------------*/
	@SuppressWarnings("unchecked")
	@RequestMapping("searchStation")
	public void searchStation(HttpServletRequest request,HttpServletResponse response) throws IOException{
		long areaId=Integer.parseInt(request.getParameter("areaId"));
		Map<String, Object> stationPara=new LinkedHashMap<String, Object>();
		String stringStationId=request.getParameter("stationId");
		long stationId=0;
		if((stringStationId!="")&&(stringStationId!=null)){
			stationId=Long.parseLong(stringStationId);
			stationPara.put("id", stationId);
		}
		String stationName=request.getParameter("stationName");
		if((stationName!="")&&(stationName!=null)){
			stationPara.put("station_name", stationName);
		}
		boolean status=false;
		List <Stations> stations= findService.findStationsByMulti(stationPara);
		if(stations.size()>0)
		{   
			if(areaId!=0)//非全局查询
			{
				for(int stCount=0;stCount<stations.size();stCount++){
					if(stations.get(stCount).getArea().getId()!=areaId){
						stations.remove(stCount);
						stCount-=1;
					}
				}
			}
			if(stations.size()>0){
				status=true;
			}
			 
		}
		
		@SuppressWarnings("rawtypes")
		LinkedHashMap[] responseD=new LinkedHashMap[stations.size()+1];
		responseD[0]=new LinkedHashMap<Object, Object>();
		if(status){
			for(int count=0;count<stations.size();count++){
			responseD[count+1]=new LinkedHashMap<Object, Object>();
			String stationLatitude="";//纬度
			String stationLongitude="";//经度
			stationLatitude+=stations.get(count).getLatitude();
			stationLongitude+=stations.get(count).getLongitude();
		    responseD[count+1]=new LinkedHashMap<Object, Object>();//Map数组的每个Map都应该先初始化
			responseD[count+1].put("stationId", stations.get(count).getId());
			responseD[count+1].put("stationName", stations.get(count).getStation_name());
			responseD[count+1].put("areaId",stations.get(count).getArea().getId());  
			responseD[count+1].put("areaName",stations.get(count).getArea().getArea_name());  
            responseD[count+1].put("stationLongitude",stationLongitude );//经度
            responseD[count+1].put("stationLatitude",stationLatitude );//纬度
            responseD[count+1].put("stationDescription", stations.get(count).getDescription());
			responseD[count+1].put("stationCreteTime", stations.get(count).getCreate_date());
			responseD[count+1].put("stationCreteUser", stations.get(count).getCreate_user());//
			responseD[count+1].put("stationAlterTime", stations.get(count).getAlter_date());
			responseD[count+1].put("stationAlterUser", stations.get(count).getAlter_user());//
		}
	}  
		responseD[0].put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseD);//生成json格式
		////System.out.println("json:"+responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
	}
	/**------------------------通过区域获取局站------------------------------------*/
    @SuppressWarnings("unchecked")
	@RequestMapping("getStation")//通过区域获取局站
    public void getStation(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	long areaId=Integer.parseInt(request.getParameter("areaId"));
    	Areas area=findService.findArea(areaId);
		List<Stations> stations = findService.findStationsByAreaId(areaId);
		@SuppressWarnings("rawtypes")
		LinkedHashMap[] responseD=new LinkedHashMap[stations.size()];
		for(int count=0;count<stations.size();count++){
			String stationLatitude="";//纬度
			String stationLongitude="";//经度
			stationLatitude+=stations.get(count).getLatitude();
            stationLongitude+=stations.get(count).getLongitude();
            responseD[count]=new LinkedHashMap<Object, Object>();//Map数组的每个Map都应该先初始化
			responseD[count].put("stationId", stations.get(count).getId());
			responseD[count].put("stationName", stations.get(count).getStation_name());
			responseD[count].put("areaId", area.getId());  
			responseD[count].put("areaName", area.getArea_name());  
            responseD[count].put("stationLongitude",stationLongitude );//经度
            responseD[count].put("stationLatitude",stationLatitude );//纬度
            responseD[count].put("stationDescription", stations.get(count).getDescription());
			responseD[count].put("stationCreteTime", stations.get(count).getCreate_date());
			responseD[count].put("stationCreteUser", stations.get(count).getCreate_user());//
			responseD[count].put("stationAlterTime", stations.get(count).getAlter_date());
			responseD[count].put("stationAlterUser",stations.get(count).getAlter_user());//
			
		}
		JSONArray responseJson=JSONArray.fromObject(responseD);//生成json格式
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
    }   
    /*-------------------------修改局站--------------------------*/
	@RequestMapping("station/modifyStation")//
    public void alterStation(HttpServletRequest request,HttpServletResponse response) throws IOException{
		long stationId=Integer.parseInt(request.getParameter("stationId"));
		String stationLongitude=request.getParameter("stationLongitude");
		String stationLatitude=request.getParameter("stationLatitude");
		String station_name=request.getParameter("stationName");
		String description=request.getParameter("description");
		Map <String,Object>stationPara=new LinkedHashMap<String,Object>();
		stationPara.put("station_name", station_name);
		stationPara.put("description", description);
		stationPara.put("latitude", stationLatitude);
		stationPara.put("longitude", stationLongitude);
		stationPara.put("alter_date", NumConv.currentTime());//设置修改时间为当前系统时间
		stationPara.put("alter_user", "admin");//设置修改用户
		boolean status= alterService.updateStation(stationId, stationPara);
		Map<String, Boolean> responseData=new LinkedHashMap<String, Boolean>();
		responseData.put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
    	
    }  
	/**------------------删除局站---------------------------------*/
	@RequestMapping("station/delStation")
    public void deleteStation(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Long stationId=Long.parseLong(request.getParameter("stationId"));
		boolean status=true;
		String err="";
		class DelRtu extends Thread{
			private Rtus rtu;
			private boolean status;
			public  DelRtu(Rtus rtu){
				this.rtu=rtu;
			}
			public boolean getStatus(){
				return status;
			}
			public void run(){
				Map<String,Object> para=new LinkedHashMap<String,Object>();
			    para.put("CM", rtu.getId());
				para.put("CLP", rtu.getStation().getId());
			    String xml=XmlCodeCreater.cancelRtuMode(para);
				status=true;
			     //System.out.println("Time:"+System.currentTimeMillis());
			     //System.out.println("xml:"+xml);
				 String rtuUrl="http://"+rtu.getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
				 //向RTU下发指令并解析回复
				 try{
					    String responseFile=NumConv.createVerifyCode(20)+"clearRtuMode.xml";
						String testDecode=NumConv.createVerifyCode(20)+"testDecode.txt";
						HttpClientUtil.Post(rtuUrl,xml,responseFile,3500,5000); 
						XmlDom XmlDom=new XmlDom();
						Map<String, Object> response= XmlDom.AnalyseRespondse(responseFile,testDecode);
						/**-------------如果回复码为0,说明成功下发--------------------------------*/
					    String StatusCode=(String) response.get("StatusCode");
					    if(StatusCode.equals("0")){//状态码为0，说明RTU端接收正常
					    	status=true;
					    }
					    else{
					        status=false;
					    }
				   }catch(IOException | HttpException | DocumentException e){
							 e.printStackTrace();  //输入出错误原因
							  status=false;
				    }
			}
		}
		List<Rtus> rtus=findService.findRtusByStationId(stationId);
		if(rtus!=null){
			List<DelRtu> delList=new ArrayList<DelRtu>();
			for(Rtus rtu:rtus){
				if(rtu.getType().contains("普通")){
					DelRtu delRtu=new DelRtu(rtu);
					delList.add(delRtu);
					delRtu.start();
				}
			}
			for(DelRtu delRtu:delList){
				try {
					delRtu.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for(DelRtu delRtu:delList){
				status&=delRtu.getStatus();
			}
		}
		if(!status){
			err="当前局站下存在RTU，在清除RTU模式时与RTU通信故障，请检查RTU状态是否正常";
		}
		else{
			try {
				delService.deleteStation(stationId);
			} catch (Exception e) {
				status = false;
				err="数据库操作异常";
				e.printStackTrace();
			}
		}
		
		Map<String, Object> responseData=new LinkedHashMap<String, Object>();
		responseData.put("status", status);
		responseData.put("err", err);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
	}  
	/**---------------------增加局站---------------------------------*/
	@RequestMapping("station/addStation")//
    public void addStation(HttpServletRequest request,HttpServletResponse response) throws IOException{
		////System.out.println("receice ID:"+request.getParameter("areaId"));
		Areas area= findService.findArea(Long.parseLong(request.getParameter("areaId")));
		
		boolean status=false;
		Stations station=new Stations();
		station.setArea(area);
		Subject currentUser = SecurityUtils.getSubject();//获取当前用户
    	String Account=currentUser.getPrincipal().toString();//当前用户的账号
        String lng=request.getParameter("stationLongitude");
        String lat=request.getParameter("stationLatitude");
		station.setDescription( request.getParameter("description"));
		station.setLatitude(lat);
		station.setLongitude(lng);
		station.setStation_name(request.getParameter("stationName"));
		station.setCreate_date(NumConv.currentTime());//创建时间为当前时间
		station.setCreate_user(Account);//创建用户
		Serializable stationId =addService.addStation(station);
		if(stationId!=null){
			status=true;
		}
		Map<String, Boolean> responseData=new LinkedHashMap<String, Boolean>();
		responseData.put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		////System.out.println("JsonData:"+responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
    	
    }   
	
	
	/**--------通过局站id获取其相关的cable，起点---------*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("getCablesByStationId")
    public void getCablesByStationId(HttpServletRequest request,HttpServletResponse response) throws IOException{
		long stationId=Long.parseLong(request.getParameter("stationId"));
		List<Optical_cables> Cable=findService.findOpticalCablesByStationId(stationId);//用于承接查询到的光缆
        LinkedHashMap[] responseData=new LinkedHashMap[Cable.size()+1];
		boolean status=false;
		responseData[0]=new LinkedHashMap<>();
		           /*遍历获取存放每条光缆的信息*/
		if(responseData.length>1)
			{
			    status=true;
				for(int cableCount=0;cableCount<Cable.size();cableCount++){
					responseData[cableCount+1]=new LinkedHashMap<Object, Object>();//对于map数组的每个map都必须实例化
					responseData[cableCount+1].put("id",Cable.get(cableCount).getId());//获取存放id
					responseData[cableCount+1].put("name",Cable.get(cableCount).getOptical_cable_name() );//
					responseData[cableCount+1].put("Length",Cable.get(cableCount).getLength());//
					responseData[cableCount+1].put("laidWay",Cable.get(cableCount).getLaidWay());//
					responseData[cableCount+1].put("networkLevel",Cable.get(cableCount).geCableLevel());
					responseData[cableCount+1].put("coreNumber",Cable.get(cableCount).getCoreNumber());
					responseData[cableCount+1].put("coreStrct",Cable.get(cableCount).getCore_strct());
					responseData[cableCount+1].put("createUser",Cable.get(cableCount).getCreateUser());
					responseData[cableCount+1].put("alterUser",Cable.get(cableCount).getAlterUser());
					responseData[cableCount+1].put("description",Cable.get(cableCount).getDescription());
					responseData[cableCount+1].put("aRemainLength",Cable.get(cableCount).getPort_a_remain());
					responseData[cableCount+1].put("zRemainLength",Cable.get(cableCount).getPort_z_remain());
					responseData[cableCount+1].put("aPortName",Cable.get(cableCount).getStation_a_name());
					responseData[cableCount+1].put("zPortName",Cable.get(cableCount).getStation_z_name());
					responseData[cableCount+1].put("alterTime",Cable.get(cableCount).getAlterTime());
					responseData[cableCount+1].put("createTime",Cable.get(cableCount).getCreateTime());
					responseData[cableCount+1].put("refractiveIndex",String.valueOf(Cable.get(cableCount).getRefractive_index()));
					
				}
			}
		responseData[0].put("status",status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
    	
    }  
	
	/**------------------通过cableName laidWay或newworkLevel获取其相关的cable-----------------*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("searchCable")
    public void searchCable(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String cableName=request.getParameter("cableName");
		String networkLevel=request.getParameter("networkLevel");
		Map<String, Object> cablePara=new LinkedHashMap<String, Object>();
		if((cableName!="")&&(cableName!=null)){
			cablePara.put("name", cableName);
		}
		if((networkLevel!=null)){
			if(!(networkLevel.equals("index")))
			cablePara.put("cable_level", networkLevel);
		}
		List<Optical_cables> Cable=findService.findOpticalCablesByMulti(cablePara);
		boolean status=false;
		LinkedHashMap[] responseData=new LinkedHashMap[Cable.size()+1];
		responseData[0]=new LinkedHashMap<Object, Object>();//回传数据
		
		/*有符合条件的地标，获取存放cable的信息*/
		if(Cable.size()!=0){
		    status=true;
		    for(int cableCount=0;cableCount<Cable.size();cableCount++){
		    	responseData[cableCount+1]=new LinkedHashMap<Object, Object>();//对于map数组的每个map都必须实例化
				responseData[cableCount+1].put("id",Cable.get(cableCount).getId());//获取存放id
				responseData[cableCount+1].put("name",Cable.get(cableCount).getOptical_cable_name() );//
				responseData[cableCount+1].put("Length",Cable.get(cableCount).getLength());//
				responseData[cableCount+1].put("laidWay",Cable.get(cableCount).getLaidWay());//
				responseData[cableCount+1].put("networkLevel",Cable.get(cableCount).geCableLevel());
				responseData[cableCount+1].put("coreNumber",Cable.get(cableCount).getCoreNumber());
				responseData[cableCount+1].put("coreStrct",Cable.get(cableCount).getCore_strct());
				responseData[cableCount+1].put("createUser",Cable.get(cableCount).getCreateUser());
				responseData[cableCount+1].put("alterUser",Cable.get(cableCount).getAlterUser());
				responseData[cableCount+1].put("description",Cable.get(cableCount).getDescription());
				responseData[cableCount+1].put("aRemainLength",Cable.get(cableCount).getPort_a_remain());
				responseData[cableCount+1].put("zRemainLength",Cable.get(cableCount).getPort_z_remain());
				responseData[cableCount+1].put("aPortName",Cable.get(cableCount).getStation_a_name());
				responseData[cableCount+1].put("zPortName",Cable.get(cableCount).getStation_z_name());
				responseData[cableCount+1].put("alterTime",Cable.get(cableCount).getAlterTime());
				responseData[cableCount+1].put("createTime",Cable.get(cableCount).getCreateTime());
				responseData[cableCount+1].put("refractiveIndex",Cable.get(cableCount).getRefractive_index());
		    }
		}
		responseData[0].put("status",status);//写入状态
		JSONArray responseJson=JSONArray.fromObject(responseData);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
    	
    } 
	
	
	/**-------------------------------增加光缆---------------------*/
	@RequestMapping("cable/addCable")
    public void addCable(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Enumeration<?> paramNames = request.getParameterNames();  
        while (paramNames.hasMoreElements()) {  
         String paramName = (String) paramNames.nextElement();  
         String[] paramValues = request.getParameterValues(paramName);  
         if (paramValues.length == 1) {  
           String paramValue = paramValues[0];  
           if (paramValue.length() != 0) {  
             ////System.out.println("参数：" + paramName + ":" + paramValue);  
            
           }  
         }  
       }  
		Optical_cables cable=new Optical_cables();
		cable.setCableLevel(request.getParameter("newworkLevel"));
		cable.setCoreNumber(Integer.parseInt(request.getParameter("cableCoreCount")));
		cable.setLaidWay(request.getParameter("laidWay"));
		cable.setLength(request.getParameter("cableLength"));
		cable.setOptical_cable_name(request.getParameter("cableName"));
		cable.setPort_a_remain(request.getParameter("ARemainLen"));
		cable.setPort_z_remain(request.getParameter("ZRemainLen"));
		cable.setRefractive_index(request.getParameter("refractiveIndex"));
		cable.setStation_a_id(Long.parseLong(request.getParameter("AStationId")));
		cable.setStation_a_name(request.getParameter("AStationName"));
		cable.setStation_z_id(Long.parseLong(request.getParameter("ZStationId")));
		cable.setStation_z_name(request.getParameter("ZStationName"));
		cable.setDescription(request.getParameter("cableName"));
		cable.setCore_strct(request.getParameter("cableCore"));
		cable.setCreateUser("admin");
		cable.setCreateTime(NumConv.currentTime());
		Serializable cableId= addService.addOpticalCable(cable);
		boolean status=false;
		if((long)cableId!=0){
			status=true;
		}
		Map<String, Boolean> responseData=new LinkedHashMap<String, Boolean>();
		responseData.put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		////System.out.println("JsonData:"+responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
    	
    }  
	/**-------------------------------修改光缆---------------------*/
	@RequestMapping("cable/modifyCable")
    public void alterCable(HttpServletRequest request,HttpServletResponse response) throws IOException{
		printAllRequestPara(request);
		Long cableId=Long.parseLong(request.getParameter("cableId"));
        Optical_cables cable=findService.findOpticalCable(cableId);
        cable.setAlterTime(NumConv.currentTime());
        Subject currentUser = SecurityUtils.getSubject();//获取当前用户
    	String Account=currentUser.getPrincipal().toString();//当前用户的账号
    	cable.setAlterUser(Account);
    	cable.setCableLevel(request.getParameter("networkLevel"));
    	cable.setCore_strct(request.getParameter("cableCore"));
    	cable.setOptical_cable_name(request.getParameter("cableName"));
    	cable.setCoreNumber(Integer.parseInt(request.getParameter("cableCoreCount")));
    	cable.setDescription(request.getParameter("description"));
    	cable.setLaidWay(request.getParameter("laidWay"));
    	cable.setRefractive_index(request.getParameter("refractiveIndex"));
    	cable.setPort_a_remain(request.getParameter("ARemainLen"));
    	cable.setPort_z_remain(request.getParameter("ZRemainLen"));
    	cable.setLength(request.getParameter("cableLength"));
		boolean status=alterService.alterOpticalCable(cable);
		Map<String, Boolean> responseData=new LinkedHashMap<String, Boolean>();
		responseData.put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
    	
    }  
	
	/**-------------------------------删除光缆---------------------*/
	@RequestMapping("cable/delCable")
    public void deleteCableConditionB(HttpServletRequest request,HttpServletResponse response) throws IOException{
		long cableId=Long.parseLong(request.getParameter("cableId"));
		Optical_cables cable=findService.findOpticalCable(cableId);
		List<Integer> ports=new ArrayList<Integer>();//占用端口的端口序号
    	List<Long> CM=new ArrayList<Long>();//占用端口的RTU ID
    	List<Map<String,Object>>CMAndPorts =new ArrayList<Map<String,Object>>();//用于存放RTU Id和其下的端口
		if(cable!=null){
			List<Fiber_cores> fibers=cable.getFiber_cores();
			if(fibers!=null){
				for(Fiber_cores fiber:fibers){
					if(fiber.getRoute_id()!=null){//挂载有光路
						Routes route=findService.findRoute(fiber.getRoute_id());
						CM.add(route.getRtu_id());
						ports.add(route.getRtu_port_order());
					}
				}
			}
		}
		/**遍历查找位于同一RTU的端口*/
    	for(int i=0;i<ports.size();i++){
    		Long thisCM=CM.get(i);
    		List<Integer>groupPorts=new ArrayList<Integer>();//用于存放在相同RTU的端口号
    		Map<String,Object> group=new LinkedHashMap<String,Object>();//存放一组的Map
    		groupPorts.add(ports.get(i));
    		for(int j=i+1;j<ports.size();j++){
    			if(CM.get(j)==thisCM){//该端口与遍历端口在同一个RTU上
    				groupPorts.add(ports.get(j));//在端口组中增加该端口
    				ports.remove(j);
    				CM.remove(j);
    				j--;
    			}
    			//i-=(groupPorts.size()-1);
    		}
    		//存放一条记录
    		group.put("CM", thisCM);
    		group.put("ports", groupPorts);
    		CMAndPorts.add(group);
    	}
    	
		/**解除端口占用的线程**/
		class ClearRtuPort extends Thread{
			private Long CM;
			private List<Integer> ports;
			private boolean status=false;
			/**构造函数**/
			public  ClearRtuPort(Long CM,List<Integer> ports){
				this.CM=CM;
				this.ports=ports;
			}
			public boolean getStatus(){
				return this.status;
			}
			public void run(){
		    	 LinkedHashMap<String,Object> para=new LinkedHashMap<String,Object>();//用于生成解除端口占用XML的Map
		   		 para.put("CM",CM);
		   		 para.put("CLP",findService.findRtu(CM).getStation().getId());
		   		 para.put("ports", ports);
		   		 String xml=XmlCodeCreater.cancelPortOccupy(para);
		   		 //System.out.println("current time:"+System.currentTimeMillis()+"\nsend:"+xml);
		   		 String rtuUrl="http://"+findService.findRtu(CM).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
		   		 String fileName=NumConv.createVerifyCode(20);
				 String responseFile="delRtuPortResponse"+fileName+".xml";
				 String testDecode="testDecode"+fileName+".txt";
		   		 try {
					    HttpClientUtil.Post(rtuUrl, xml, responseFile, 3500, 4000);
					    XmlDom XmlDom=new XmlDom();
					    Map<String, Object> responseMsg=XmlDom.AnalyseRespondse(responseFile,testDecode);
			  	    	String StatusCode=(String) responseMsg.get("StatusCode");
			            if(StatusCode.equals("0")){//
			  	    		 status =true;
			  	    	}
			  	    	 
				} catch (HttpException| DocumentException | IOException e) {
					// TODO Auto-generated catch block
					 e.printStackTrace();  //输入出错误原因
					 status=false; 
			    }	
			}
			
		}
		boolean status = true;  
		/**给各RTU发送解除端口占用指令**/
		List<ClearRtuPort> threads=new ArrayList<ClearRtuPort>();
		for(int index=0;index<CMAndPorts.size();index++){
			long cm=(long)CMAndPorts.get(index).get("CM");
			@SuppressWarnings("unchecked")
			List<Integer> port=(List<Integer>)CMAndPorts.get(index).get("ports");
			ClearRtuPort clearPort=new ClearRtuPort(cm,port);
			threads.add(clearPort);
			clearPort.start();
		}
		/**主线程等待所有次线程执行完后再执行
		 * 删除的结果是每个线程的执行结果的与
		 * **/
		for(ClearRtuPort clearRtuPort:threads){
			try {
				clearRtuPort.join();
				status&=clearRtuPort.getStatus();//状态为每个进程的执行状态与
			 } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				status=false;
			 }
        }
		for(ClearRtuPort clearRtuPort:threads){
			status&=clearRtuPort.getStatus();//状态为每个进程的执行状态与
			
        }
		/**如果解除端口指令下发成功，删除配线架**/
		String err="";
		if(status){
			try {
				delService.deleteOpticalCable(cableId);
				status = true;
			} catch (Exception e) {
				e.printStackTrace();
				status = false;
				err="数据库操作异常";
			}
		}
		else{
			err="当前光缆的纤芯挂载有光路，在向RTU下发解除端口状态时与RTU通信异常，请检查RTU状态是否正常。";
		}
		Map<String, Object> responseData=new LinkedHashMap<String, Object>();
		responseData.put("status", status);
		responseData.put("err", err);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
		  	
    } 
	/**------------------通过cableId获取其相关的landmark-----------------*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("getLandmarkByCableId")
    public void getLandmarkByCableId(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Long cableId=Long.parseLong(request.getParameter("cableId"));
		List<Landmarks> Landmark=findService.findLandmarksByOpticalCableId(cableId);//用于承接查询到的光缆
		
		/*-------  - - - -根据cableId查询landmark，未完成，待数据库 -- ---- - - - -----*/
		boolean status=false;
		LinkedHashMap[] responseData=new LinkedHashMap[Landmark.size()+1];
		responseData[0]=new LinkedHashMap<Object, Object>();
		           /*遍历获取存放个landmark的信息*/
		if(Landmark.size()>0){
			status=true;
			for(int Count=0;Count<Landmark.size();Count++){
				responseData[Count+1]=new LinkedHashMap<Object, Object>();//对于map数组的每个map都必须实例化
				responseData[Count+1].put("id",Landmark.get(Count).getId());//获取存放id
				responseData[Count+1].put("name",Landmark.get(Count).getName() );//获取存放id
				responseData[Count+1].put("cableName",Landmark.get(Count).getOptical_cable().getOptical_cable_name() );//获取存放id
				responseData[Count+1].put("Latitude",Landmark.get(Count).getLatitude() );//获取存放id
				responseData[Count+1].put("Longitude",Landmark.get(Count).getLongitude());
				responseData[Count+1].put("description",Landmark.get(Count).getDescription());
				responseData[Count+1].put("createUser",Landmark.get(Count).getCreateUser());
				responseData[Count+1].put("createTime",Landmark.get(Count).getCreateTime());
				responseData[Count+1].put("alterUser",Landmark.get(Count).getAlterUser());
				responseData[Count+1].put("alterTime",Landmark.get(Count).getAlterTime());
				
			}
		}
		
		responseData[0].put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
    	
    } 
	
	/**------------------通过landmark id或name获取其相关的landmark-----------------*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("searchLandmark")
    public void searchLandmark(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String, Object> landmarkPara=new LinkedHashMap<String, Object>();
		String landmarkName=request.getParameter("landmarkName");
		if(landmarkName.length()>0){
			landmarkPara.put("landmark_name", landmarkName);
		}
		boolean status=false;
		List<Landmarks> Landmark=findService.findLandmarksByMulti(landmarkPara);
		LinkedHashMap[] responseData=new LinkedHashMap[Landmark.size()+1];
		responseData[0]=new LinkedHashMap<Object, Object>();
		           /*遍历获取存放个landmark的信息*/
		if(Landmark.size()>0){
			status=true;
			for(int Count=0;Count<Landmark.size();Count++){
				responseData[Count+1]=new LinkedHashMap<Object, Object>();//对于map数组的每个map都必须实例化
				responseData[Count+1].put("id",Landmark.get(Count).getId());//获取存放id
				responseData[Count+1].put("name",Landmark.get(Count).getName() );//获取存放id
				responseData[Count+1].put("cableName",Landmark.get(Count).getOptical_cable().getOptical_cable_name() );//获取存放id
				responseData[Count+1].put("Latitude",Landmark.get(Count).getLatitude() );//获取存放id
				responseData[Count+1].put("Longitude",Landmark.get(Count).getLongitude());
				responseData[Count+1].put("description",Landmark.get(Count).getDescription());
				responseData[Count+1].put("createUser",Landmark.get(Count).getCreateUser());
				responseData[Count+1].put("createTime",Landmark.get(Count).getCreateTime());
				responseData[Count+1].put("alterUser",Landmark.get(Count).getAlterUser());
				responseData[Count+1].put("alterTime",Landmark.get(Count).getAlterTime());
				/*-----------------------获取其他landmark信息，等待数据库----*/
			}
		}
		
		responseData[0].put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
    	
    } 
	/**--------------------增加landmark-----------------------*/
	@RequestMapping("landmark/addLandmark")
	  public void addLandmark(HttpServletRequest request,HttpServletResponse response) throws IOException{
			Subject currentUser = SecurityUtils.getSubject();//获取当前用户
	    	String Account=currentUser.getPrincipal().toString();//当前用户的账号
	        Optical_cables cable=findService.findOpticalCable(Long.parseLong(request.getParameter("cableId")));
	        String lng=request.getParameter("Longtitude");
	        String lat=request.getParameter("Latitude");
	        Landmarks landmark=new Landmarks();
	        landmark.setOptical_cable(cable);
	        landmark.setName(request.getParameter("landmarkName"));
	        landmark.setLatitude(lat);
	        landmark.setLongitude(lng);
	        landmark.setDescription(request.getParameter("description"));
	        landmark.setCreateTime(NumConv.currentTime());
	        landmark.setCreateUser(Account);
	        boolean status=false;
	        Serializable id=addService.addLandmark(landmark);
	        if(id!=null){
	        	status=true;
	        }
			Map<String, Boolean> responseData=new LinkedHashMap<String, Boolean>();
			responseData.put("status", status);
			JSONArray responseJson=JSONArray.fromObject(responseData);
			PrintWriter out=response.getWriter();
			out.println(responseJson);
			out.flush();
			out.close();
	    	
	    }  
	/**--------------------修改landmark-----------------------*/
	@RequestMapping("landmark/modifyLandmark")
	  public void alterLandmark(HttpServletRequest request,HttpServletResponse response) throws IOException{
//			Enumeration<?> paramNames = request.getParameterNames();  
//	        while (paramNames.hasMoreElements()) {  
//	         String paramName = (String) paramNames.nextElement();  
//	         String[] paramValues = request.getParameterValues(paramName);  
//	         if (paramValues.length == 1) {  
//	           String paramValue = paramValues[0];  
//	           if (paramValue.length()!= 0) {  
//	             ////System.out.println("参数：" + paramName + "=" + paramValue);  
//	            
//	           }  
//	         }  
//	       }  
	      	Subject currentUser = SecurityUtils.getSubject();//获取当前用户
	    	String Account=currentUser.getPrincipal().toString();//当前用户的账号
	        Map <String,Object>cablePara=new LinkedHashMap<String,Object>();
	        cablePara.put("landmark_name", request.getParameter("landmarkName"));
	        cablePara.put("latitude", request.getParameter("Latitude"));
	        cablePara.put("longitude", request.getParameter("Longtitude"));
	        cablePara.put("description", request.getParameter("description"));
	        cablePara.put("alter_time", NumConv.currentTime());
	        cablePara.put("alter_user", Account);
	        Long landmarkId=Long.parseLong(request.getParameter("LandmarkId"));
	        Long cableId=findService.findLandmark(landmarkId).getOptical_cable().getId();
			boolean status=alterService.updateLandmark(landmarkId, cablePara);
			Map<String, Object> responseData=new LinkedHashMap<String, Object>();
			responseData.put("status", status);
			responseData.put("cableId", cableId);
			JSONArray responseJson=JSONArray.fromObject(responseData);
			PrintWriter out=response.getWriter();
			out.println(responseJson);
			out.flush();
			out.close();
	    	
	    }
	/**--------------------删除landmark-----------------------*/
	@RequestMapping("landmark/delLandmark")
	  public void deleteLandmark(HttpServletRequest request,HttpServletResponse response) throws IOException{
			/*Enumeration<?> paramNames = request.getParameterNames();  
	        while (paramNames.hasMoreElements()) {  
	         String paramName = (String) paramNames.nextElement();  
	         String[] paramValues = request.getParameterValues(paramName);  
	         if (paramValues.length == 1) {  
	           String paramValue = paramValues[0];  
	           if (paramValue.length() != 0) {  
	             ////System.out.println("参数：" + paramName + "=" + paramValue);  
	            
	           }  
	         }  
	       } */ 
			Long landmarkId = (long)Integer.parseInt(request.getParameter("landmarkId"));
			boolean status= true;
			Long cableId=findService.findLandmark(landmarkId).getOptical_cable().getId();
			try {
				delService.deleteLandmark(landmarkId);
				status = true;
			} 
			catch (Exception e) {
				status = false;
			}
			finally {
				Map<String, Object> responseData=new LinkedHashMap<String, Object>();
				responseData.put("status", status);
				responseData.put("cableId", cableId);
				JSONArray responseJson=JSONArray.fromObject(responseData);
				PrintWriter out=response.getWriter();
				out.println(responseJson);
				out.flush();
				out.close();
			}	    	
	    }
	
	
	/**---------------------通过局站id获取类跳纤---------------------*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("getJumperByStationId")
	  public void getJumperByStationId(HttpServletRequest request,HttpServletResponse response) throws IOException{
			long stationId=Long.parseLong(request.getParameter("stationId"));
			List <Jumper_frames>frameJumper=findService.findJumperFramesByStationId(stationId);
			List <Jumper_routes>routeJumper=findService.findJumperRoutesByStationId(stationId);
			List <Jumper_backups>backupJumper=findService.findJumperBackupByStationId(stationId);
			LinkedHashMap[] responseData=new LinkedHashMap[frameJumper.size()+routeJumper.size()+backupJumper.size()+1];
			responseData[0]=new LinkedHashMap<Object, Object>();
			boolean status=false;
			if(responseData.length>1){
				status=true;
				for(int JCount=1;JCount<responseData.length;JCount++){
					responseData[JCount]=new LinkedHashMap<Object, Object>();
					if((JCount-1)<frameJumper.size()){//存配线架跳纤
						responseData[JCount].put("id",frameJumper.get(JCount-1).getId());
						responseData[JCount].put("name",frameJumper.get(JCount-1).getJumper_frame_name());
						responseData[JCount].put("length",frameJumper.get(JCount-1).getLength());
						responseData[JCount].put("type","配线架跳纤");
						responseData[JCount].put("equipmentAId",frameJumper.get(JCount-1).getFrame_a_id());
						responseData[JCount].put("equipmentAName",frameJumper.get(JCount-1).getFrame_a_name());
						responseData[JCount].put("equipmentAPort",frameJumper.get(JCount-1).getPort_order_a());
						responseData[JCount].put("equipmentZId",frameJumper.get(JCount-1).getFrame_z_id());
						responseData[JCount].put("equipmentZName",frameJumper.get(JCount-1).getFrame_z_name());//
						responseData[JCount].put("equipmentZPort",frameJumper.get(JCount-1).getPort_order_z());
						responseData[JCount].put("stationName",frameJumper.get(JCount-1).getStation().getStation_name());
						responseData[JCount].put("stationId",frameJumper.get(JCount-1).getStation().getId());
						responseData[JCount].put("description",frameJumper.get(JCount-1).getDescription());
						responseData[JCount].put("createTime",frameJumper.get(JCount-1).getCreateTime());//
						responseData[JCount].put("alterTime",frameJumper.get(JCount-1).getAlterTime());//
						
					}
					else if((JCount-1)<frameJumper.size()+routeJumper.size()){//存光路跳纤
						
						responseData[JCount]=new LinkedHashMap<Object, Object>();
						responseData[JCount].put("id",routeJumper.get(JCount-frameJumper.size()-1).getId());
						responseData[JCount].put("name",routeJumper.get(JCount-frameJumper.size()-1).getJumper_route_name());
						responseData[JCount].put("length",routeJumper.get(JCount-frameJumper.size()-1).getLength());
						String type="光路跳纤";
						if(routeJumper.get(JCount-frameJumper.size()-1).getIsSwitch())
							type="切换跳纤";
						responseData[JCount].put("type",type);
						responseData[JCount].put("equipmentAId",routeJumper.get(JCount-frameJumper.size()-1).getRtu_id());
						responseData[JCount].put("equipmentAName",routeJumper.get(JCount-frameJumper.size()-1).getRtu_name());//
						String order=RouteController.getPortOrder(routeJumper.get(JCount-frameJumper.size()-1).getOtdr_port_order(),
								      routeJumper.get(JCount-frameJumper.size()-1).getModelOrder());
						responseData[JCount].put("equipmentAPort",order);
						responseData[JCount].put("equipmentZId",routeJumper.get(JCount-frameJumper.size()-1).getFrame_id());
						responseData[JCount].put("equipmentZName",routeJumper.get(JCount-frameJumper.size()-1).getFrame_name());
						responseData[JCount].put("equipmentZPort",routeJumper.get(JCount-frameJumper.size()-1).getFrame_port_order());
						responseData[JCount].put("stationName",routeJumper.get(JCount-frameJumper.size()-1).getStation().getStation_name());
						responseData[JCount].put("stationId",routeJumper.get(JCount-frameJumper.size()-1).getStation().getId());
						responseData[JCount].put("description",routeJumper.get(JCount-frameJumper.size()-1).getDescription());
						responseData[JCount].put("createTime",routeJumper.get(JCount-frameJumper.size()-1).getCreateTime());//
						responseData[JCount].put("alterTime",routeJumper.get(JCount-frameJumper.size()-1  ).getAlterTime());//
					}
					else{//备纤光源条纤
						responseData[JCount]=new LinkedHashMap<Object, Object>();
						responseData[JCount].put("id",backupJumper.get(JCount-frameJumper.size()-routeJumper.size()-1).getId());
						responseData[JCount].put("name",backupJumper.get(JCount-frameJumper.size()-routeJumper.size()-1).getJumperName());
						responseData[JCount].put("length",backupJumper.get(JCount-frameJumper.size()-routeJumper.size()-1).getLength());
						responseData[JCount].put("type","备纤光源跳纤");
						responseData[JCount].put("equipmentAId",backupJumper.get(JCount-frameJumper.size()-routeJumper.size()-1).getRtu_id());
						responseData[JCount].put("equipmentAName",backupJumper.get(JCount-frameJumper.size()-routeJumper.size()-1).getRtu_name());//
						responseData[JCount].put("equipmentAPort",backupJumper.get(JCount-frameJumper.size()-routeJumper.size()-1).getIn_port_order());
						responseData[JCount].put("equipmentZId",backupJumper.get(JCount-frameJumper.size()-routeJumper.size()-1).getFrame_id());
						responseData[JCount].put("equipmentZName",backupJumper.get(JCount-frameJumper.size()-routeJumper.size()-1).getFrame_name());
						responseData[JCount].put("equipmentZPort",backupJumper.get(JCount-frameJumper.size()-routeJumper.size()-1).getFrame_port_order());
						responseData[JCount].put("stationName",backupJumper.get(JCount-frameJumper.size()-routeJumper.size()-1).getStation().getStation_name());
						responseData[JCount].put("stationId",backupJumper.get(JCount-frameJumper.size()-routeJumper.size()-1).getStation().getId());
						responseData[JCount].put("description",backupJumper.get(JCount-frameJumper.size()-routeJumper.size()-1).getDescription());
						responseData[JCount].put("createTime",backupJumper.get(JCount-frameJumper.size()-routeJumper.size()-1).getCreateTime());//
						responseData[JCount].put("alterTime",backupJumper.get(JCount-frameJumper.size()-routeJumper.size()-1  ).getAlterTime());//
					}
				}
			}
			responseData[0].put("status", status);
			JSONArray responseJson=JSONArray.fromObject(responseData);
			//////System.out.println("JsonData:"+responseJson);
			PrintWriter out=response.getWriter();
			out.println(responseJson);
			out.flush();
			out.close();
	    	
	    }
	/**--------------------根据局站id获取rtu，用于增加跳纤时显示-----------------------*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("getRtuByStationId")
		  public void getRtuByStationId(HttpServletRequest request,HttpServletResponse response) throws IOException{
			    long stationId=Long.parseLong(request.getParameter("stationId"));
			    String jumperType = request.getParameter("jumperType");
			    List<Rtus> rtu=findService.findRtusByStationId(stationId);
			    boolean status=false;
				LinkedHashMap[] responseDataPre=new LinkedHashMap[rtu.size()+1];
				responseDataPre[0]=new LinkedHashMap<Object, Object>();
				int needRtuNum = 1;
				if(rtu.size()>0){//有rtu
					if(jumperType.equals("光路跳纤")){
						for(int rCount=0;rCount<rtu.size();rCount++){
							String rtuType = rtu.get(rCount).getType();
							if(rtuType.equals("普通rtu")){   
								status=true;
								responseDataPre[needRtuNum]=new LinkedHashMap<Object, Object>();
								responseDataPre[needRtuNum].put("id",rtu.get(rCount).getId());
								responseDataPre[needRtuNum].put("name",rtu.get(rCount).getRtu_name());
								needRtuNum++;
							}
						}
					}
					/**切换跳纤所连接的RTU必须具有保护-从模块
					 * 模块类型为5**/
					else if(jumperType.equals("切换跳纤")){
						for(int rCount=0;rCount<rtu.size();rCount++){
							if(rtu.get(rCount).getType().equals("普通rtu")){  
								String info = rtu.get(rCount).getInstallInfo();
								if(info.contains("5")){//具有切换模块
								    status=true;
									responseDataPre[needRtuNum]=new LinkedHashMap<Object, Object>();
									responseDataPre[needRtuNum].put("id",rtu.get(rCount).getId());
									responseDataPre[needRtuNum].put("name",rtu.get(rCount).getRtu_name());
									needRtuNum++;
								}
							}
							else{
								continue;
							}
							
						}
					}
					else{//备纤光源跳纤
						for(int rCount=0;rCount<rtu.size();rCount++){
							String rtuType = rtu.get(rCount).getType();
							if(rtuType.equals("备纤光源rtu")){   
								status=true;
								responseDataPre[needRtuNum]=new LinkedHashMap<Object, Object>();
								responseDataPre[needRtuNum].put("id",rtu.get(rCount).getId());
								responseDataPre[needRtuNum].put("name",rtu.get(rCount).getRtu_name());
								needRtuNum++;
							}
						}
					}
					
				}
				responseDataPre[0].put("status", status);
				LinkedHashMap[] responseData=new LinkedHashMap[needRtuNum];
				responseData[0]=new LinkedHashMap<Object, Object>();
				responseData[0]=responseDataPre[0];
				for(int i =1;i<needRtuNum;i++)
				{
					responseData[i]=new LinkedHashMap<Object, Object>();
					responseData[i].put("id",responseDataPre[i].get("id"));
					responseData[i].put("name",responseDataPre[i].get("name"));
				}
				JSONArray responseJson=JSONArray.fromObject(responseData);
				//System.out.println("rerurn:"+responseJson);
				PrintWriter out=response.getWriter();
				out.println(responseJson);
				out.flush();
				out.close();	   
	}
	  /**--------------------根据rtuId获取module名字  用于新建跳纤时选择模块-----------------------*/
	@RequestMapping("getRtuModelNameByRtuId")
	public void getRtuModelNameByRtuId(HttpServletRequest request,HttpServletResponse response) throws IOException{
	   	 long rtuId =Long.parseLong(request.getParameter("rtuId"));
	   	 String addJumperType=request.getParameter("addJumperType");
	   	 String rtuInstallInfo = findService.findRtu(rtuId).getInstallInfo();
	   	 List<String> moudleName = new ArrayList<String>();
	   	 List<Integer> moudleOrder = new ArrayList<Integer>();
	   	 for(int i = 0;i<rtuInstallInfo.length();i++){
	   		/**切换跳纤只选择保护-从模块**/
	   		 if(addJumperType.equals("切换跳纤")){
	   			if(rtuInstallInfo.substring(i, i+1).equals("5")){
	   				moudleName.add("模块"+(i+1)+"(保护-从)"); 
	   				moudleOrder.add(i+1);
	   			}
	   		 }
	   		 /**新建光路跳纤时不显示保护-从模块的端口
	   		  * 其模块类型号为5**/
	   		 else{
	   			    
		   			switch(rtuInstallInfo.substring(i, i+1)){
			   		 	case"1":
			   		 		moudleName.add("模块"+(i+1)+"(在线)");
			   		 	    moudleOrder.add(i+1);
			   		 		break;
			   		 	case"2":
			   		 		moudleName.add("模块"+(i+1)+"(备纤)");
			   		 	    moudleOrder.add(i+1);
			   		 		break;
			   		    case"3":
			   		    	moudleName.add("模块"+(i+1)+"(保护-主)");
			   		 	    moudleOrder.add(i+1);
			   		    	break;
					 	case"4":
					 		moudleName.add("模块"+(i+1)+"(在纤OPM)");
			   		 	    moudleOrder.add(i+1);
					 		break;
					 	default:
					 		break;
		   		   }
	   		 }
	   		 
	   	 }	 
	   	Map<String,Object> map = new LinkedHashMap<>();
	   	map.put("modelName", moudleName);
		map.put("modelOrder", moudleOrder);
	    JSONArray responseData=JSONArray.fromObject(map);
	    //System.out.println("response:"+responseData);
	   	response.setContentType("text/xml");
	   	response.setCharacterEncoding("utf-8");
	   	PrintWriter out = response.getWriter();
	   	out.print(responseData);			
	   	out.flush();
	   	out.close();
	}  

/**--------------------根据局站id获取frame-----------------------*/
@SuppressWarnings({ "unchecked", "rawtypes" })
@RequestMapping("getFrameByStationId")
  public void getFrameByStationId(HttpServletRequest request,HttpServletResponse response) throws IOException{
	    long stationId=Long.parseLong(request.getParameter("stationId"));
	    List<Distributing_frames> frame=findService.findDistributingFramesByStationId(stationId);
	    boolean status=false;
		LinkedHashMap[] responseData=new LinkedHashMap[frame.size()+1];
		responseData[0]=new LinkedHashMap<Object, Object>();
		if(frame.size()>0){//有rtu
			status=true;
			for(int rCount=0;rCount<frame.size();rCount++){
				responseData[rCount+1]=new LinkedHashMap<Object, Object>();
				responseData[rCount+1].put("id",frame.get(rCount).getId());
				responseData[rCount+1].put("name",frame.get(rCount).getFrame_name());
			}
		}
		responseData[0].put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		//System.out.println("======================JsonData:"+responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
    	
    }

/**根据frame id获取frame port-
 * 
 * 用于增加跳纤或设置纤芯连接关系
 * */
@SuppressWarnings({ "unchecked", "rawtypes" })
@RequestMapping("getPortByFrameId")
public void getPortByFrameId(HttpServletRequest request,HttpServletResponse response) throws IOException{
	    long frameId=Long.parseLong(request.getParameter("frameId"));
	    String fiber=request.getParameter("fiber");//判断是否是fiber在查询，若为fiber，利用has_fiber来筛选端口
	    String portType=request.getParameter("portType");
	    if(portType==null){
	    	portType="all";
	    }
	    List<Frame_ports>framePort=findService.findFramePortsAllByFrameId(frameId);
	    boolean status=false;
	    for(int rCount=0;rCount<framePort.size();rCount++){
	    	if(fiber!=null){  //fiber 查询
	    		   if(framePort.get(rCount).getHas_fiber()){
		    		  framePort.remove(rCount);
		    		  rCount-=1;
		    	  }
	    	}
	    	else{ //跳纤查询
	    		  /**配线架跳纤只剔除已经使用的端口**/
	    		  if(framePort.get(rCount).getStatus()){
		    		framePort.remove(rCount);
		    		rCount-=1;
	    	      }
	    		  /**未使用，但是根据新建跳纤的类型来决定**/
	    		  else {
	    			  boolean keep=false;
	    			  /**其他类型的跳纤要根据条件筛选**/
	    			  if(framePort.get(rCount).getHas_fiber()){
	    				/**光路跳纤，只要连接了纤芯即可**/
	    				if(portType.equals("all")||portType.equals("jumperRoute")){
	    					keep=true;
	 	 		    	}
	 	    			/**切换跳纤:剔除不是连接保护-从模块的端口
	 		    		*备纤光源跳纤：剔除不是连接备纤模块的端口
	 		    		* **/
	    				else{
		 	    			    Fiber_cores fiberCore=findService.findFiberCoreByFrameIDandPortOrder(framePort.get(rCount).getFrame().getId(), framePort.get(rCount).getPort_order());
		 	    			    //未生成光路，只是存在物理连接
	 	 	    			    int order=fiberCore.getPort_order_a();
	 	 	    			    long frameRec=fiberCore.getFrame_a_id();
	 	 	    				//找到连接纤芯的接收端的配线架端口序号
	 	 	    				if(framePort.get(rCount).getFrame().getId()==frameRec){
	 	 	    					order=fiberCore.getPort_order_z();
		 	 	    				frameRec=fiberCore.getFrame_z_id();
	 	 	    				}
	 	 	    				Frame_ports portRec=findService.findFramePortByFrameIdAndPortOrder(frameRec,order);
	 	 	    				if(portRec.getConnection_id()!=null&&portRec.getConnection_type().equals("光路跳纤")){//连接有光路跳纤
	 	 	    					Jumper_routes jumperRoute=findService.findJumperRoute(portRec.getConnection_id());
	 	 	    					int moudleOrder=jumperRoute.getModelOrder();
	 	 	    					String moudleInfo=findService.findRtu(jumperRoute.getRtu_id()).getInstallInfo();
	 	 	    					String moudleType=moudleInfo.substring(moudleOrder-1,moudleOrder);
	 	 	    					/**光路所在的模块类型：
	 	 	    					 * 1为在线 
	 	 	    					 * 2为备纤 
	 	 	    					 * 3为保护-主 
	 	 	    					 * 4为在线OPM
	 	 	    					 * 5为保护-从
	 	 	    					 * 只有连有保护-主模块端口的配线架端口可以连接切换跳纤*/
	 	 	    					if((moudleType.equals("3")&&portType.equals("jumperSwitch"))||(portType.equals("jumperBackup")&&moudleType.equals("2"))){
				    				    keep=true;
				    				  }
	 	 	    				}
	 	 	    		} 
	 		    	}
	    			if(!keep){
	    				framePort.remove(rCount);
			    		rCount-=1;
	    			} 
	    		 }
	    	}
	    }
		LinkedHashMap[] responseData=new LinkedHashMap[framePort.size()+1];
		responseData[0]=new LinkedHashMap<Object, Object>();
		if(framePort.size()>0){//
			status=true;
			for(int rCount=0;rCount<framePort.size();rCount++){
				responseData[rCount+1]=new LinkedHashMap<Object, Object>();
				responseData[rCount+1].put("order",framePort.get(rCount).getPort_order());
				if((framePort.get(rCount).getName()==null)||(framePort.get(rCount).getName().equals(""))){
					responseData[rCount+1].put("name","端口"+framePort.get(rCount).getPort_order());
				}
				else{
					 responseData[rCount+1].put("name",framePort.get(rCount).getName());
				}
			 } 
		}
		responseData[0].put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
  	
  }
/**--------------------根据rtu id获取rtu port-----------------------*/
@SuppressWarnings({ "unchecked", "rawtypes" })
@RequestMapping("getPortByRtuId")
public void getPortByRtuId(HttpServletRequest request,HttpServletResponse response) throws IOException{
	    long rtuId=Long.parseLong(request.getParameter("rtuId"));
	    List<Rtu_ports> rtuPorts=findService.findRtuPortsAllByRtuId(rtuId);
	    for(int i=0;i<rtuPorts.size();i++){
	    	if(rtuPorts.get(i).getStatus()){
	    		rtuPorts.remove(i);
	    		i-=1;
	    	}
	    }
	    boolean status=false;
		LinkedHashMap[] responseData=new LinkedHashMap[rtuPorts.size()+1];
		responseData[0]=new LinkedHashMap<Object, Object>();
		if(rtuPorts.size()>0){//有
			status=true;
			for(int rCount=0;rCount<rtuPorts.size();rCount++){
				responseData[rCount+1]=new LinkedHashMap<Object, Object>();
				responseData[rCount+1].put("order",rtuPorts.get(rCount).getPort_order());
				String name=rtuPorts.get(rCount).getName();
				if((name==null)||(name.equals(""))){
					name="端口"+rtuPorts.get(rCount).getPort_order();
				}
				responseData[rCount+1].put("name",name);
			}
		}
		responseData[0].put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
  	
  }

/**根据rtuId获取module  port  
 * 用于增加跳纤时的端口选择
 * 光路跳纤：保护-主模块 类型为3
 * 切换跳纤：保护-从模块 类型为5
 * */
@RequestMapping("getPortByRtuIdAndModelName") 
public void getPortByRtuIdAndModelName(HttpServletRequest request,HttpServletResponse response) throws IOException{
	String addJumperType = request.getParameter("addJumperType");
	List<String> portName=new ArrayList<String>();
	List<Integer> portOrder=new ArrayList<Integer>();
	int modelOrder = Integer.parseInt(request.getParameter("modelOrder"));	
	long rtuId = Long.parseLong(request.getParameter("rtuId"));
	if(addJumperType.equals("光路跳纤")){ //光路跳纤的情况
		List<Rtu_ports> modulePorts = findService.findRtuPortsByRtuIdAndMoudleOrder(rtuId,modelOrder);
		//System.out.println(rtuId+"\nmodelOrder:"+modelOrder+"\nsize:"+modulePorts.size());
		String info=findService.findRtu(rtuId).getInstallInfo();
		int moudleType=Integer.parseInt(info.substring(modelOrder-1, modelOrder));
		for(int i=0;i<modulePorts.size();i++){
			/**保护-从模块不可连接光路跳纤，不可设置光路**/
			if(!modulePorts.get(i).getStatus()&&(moudleType!=5)){
				portName.add(modulePorts.get(i).getName());
				 portOrder.add(modulePorts.get(i).getPort_order());
			}
		}
   }
	else if(addJumperType.equals("切换跳纤")) {   /**切换跳纤的情况
	        RTU 必须具有具有保护-从模块**/
		String info=findService.findRtu(rtuId).getInstallInfo();
		int moudleType=Integer.parseInt(info.substring(modelOrder-1, modelOrder));
        List<Rtu_ports> allPorts = findService.findRtuPortsByRtuIdAndMoudleOrder(rtuId, modelOrder);
        for(int i=0;i<allPorts.size();i++){  
        	if((moudleType==5)&&!allPorts.get(i).getStatus()){
				 portName.add(allPorts.get(i).getName());
				 portOrder.add(allPorts.get(i).getPort_order());
        	}
		 }
	 }
	/**备纤光源跳纤**/
	else{
		List<Rtu_ports> allPorts = findService.findRtu(rtuId).getRtu_ports();
        for(int i=0;i<allPorts.size();i++){ 
        	if(!allPorts.get(i).getStatus()){
				 portName.add(allPorts.get(i).getName());
				 portOrder.add(allPorts.get(i).getPort_order());
        	}
		 }
	}
	 Map<String,Object> map = new LinkedHashMap<>();
	 map.put("portName", portName);
	 map.put("portOrder",portOrder );
	 JSONArray responseData=JSONArray.fromObject(map);
	 response.setContentType("text/xml");
	 response.setCharacterEncoding("utf-8");
	 PrintWriter out = response.getWriter();
	 out.print(responseData);			
	 out.flush();
	 out.close();
}


/**--------------------------增加跳纤-------------------------*/
@RequestMapping("jumper/addJumper") //FIXME
public void addJumper(HttpServletRequest request,HttpServletResponse response) throws IOException{
	    printAllRequestPara(request);
	    String jumperType=request.getParameter("type");
	    boolean status=false;
	    String err="";
	    Stations station=findService.findStation(Long.parseLong(request.getParameter("stationId")));
	    Serializable jumperId=null;
	    if(jumperType.equals("配线架跳纤")){
	    	Jumper_frames jumperFrame=new Jumper_frames();
	    	jumperFrame.setLength(request.getParameter("jumperLength"));
	    	jumperFrame.setJumper_frame_name(request.getParameter("jumperName"));
	    	jumperFrame.setFrame_a_id(Long.parseLong(request.getParameter("aEquipmentId")));
	    	jumperFrame.setFrame_a_name(request.getParameter("aEquipmentName"));
	    	jumperFrame.setPort_order_a(Integer.parseInt(request.getParameter("aPortOrder")));
	    	jumperFrame.setFrame_z_id(Long.parseLong(request.getParameter("zEquipmentId")));
	    	jumperFrame.setFrame_z_name(request.getParameter("zEquipmentName"));
	    	jumperFrame.setPort_order_z(Integer.parseInt(request.getParameter("zPortOrder")));
	    	jumperFrame.setDescription(request.getParameter("description"));
	    	jumperFrame.setStation(station);
	    	jumperFrame.setCreateTime(NumConv.currentTime());
	    	jumperId=addService.addJumperFrame(jumperFrame);
	    }
	    if(jumperType.equals("光路跳纤")){
	    	Jumper_routes jumperRoute=new Jumper_routes();
	    	jumperRoute.setIsSwitch(false);
	    	int modelOrder = Integer.parseInt(request.getParameter("modelOrder"));
	    	jumperRoute.setModelOrder(modelOrder);
	    	jumperRoute.setLength(request.getParameter("jumperLength"));
	    	jumperRoute.setJumper_route_name(request.getParameter("jumperName"));
	    	jumperRoute.setRtu_id(Long.parseLong(request.getParameter("aEquipmentId")));
	    	jumperRoute.setRtu_name(request.getParameter("aEquipmentName"));
	    	int portOrder = Integer.parseInt(request.getParameter("aPortOrder"));
	    	/**端口序号用每个模块的序号
	    	 * 值为1-8**/
	    	jumperRoute.setOtdr_port_order(portOrder);
	    	jumperRoute.setFrame_id(Long.parseLong(request.getParameter("zEquipmentId")));
	    	jumperRoute.setFrame_name(request.getParameter("zEquipmentName"));
	    	jumperRoute.setFrame_port_order(Integer.parseInt(request.getParameter("zPortOrder")));
	    	jumperRoute.setDescription(request.getParameter("description"));
	    	jumperRoute.setCreateTime(NumConv.currentTime());
	    	jumperRoute.setStation(station);
	    	jumperId=addService.addJumperRoute(jumperRoute);
	    	if(jumperId==null){
	    		err="数据库存储异常";
    		}
	    }
	    /**新建切换跳纤时，要向发射机发送端口占用指令**/
	    if(jumperType.equals("切换跳纤")){
	    	Jumper_routes jumperRoute=new Jumper_routes();
	    	jumperRoute.setIsSwitch(true);
	    	jumperRoute.setLength(request.getParameter("jumperLength"));
	    	jumperRoute.setJumper_route_name(request.getParameter("jumperName"));
	    	Long switchRtuId=Long.parseLong(request.getParameter("aEquipmentId"));
	    	jumperRoute.setRtu_id(switchRtuId);
	    	jumperRoute.setRtu_name(request.getParameter("aEquipmentName"));
	    	int modelOrder = Integer.parseInt(request.getParameter("modelOrder"));
	    	jumperRoute.setModelOrder(modelOrder);
	    	int aPortOrder = Integer.parseInt(request.getParameter("aPortOrder"));
	    	jumperRoute.setOtdr_port_order(aPortOrder);
	    	long frameId=Long.parseLong(request.getParameter("zEquipmentId"));//配线架ID
	    	int portOrder=Integer.parseInt(request.getParameter("zPortOrder"));//配线架端口
	    	jumperRoute.setFrame_id(frameId);
	    	jumperRoute.setFrame_name(request.getParameter("zEquipmentName"));
	    	jumperRoute.setFrame_port_order(portOrder);
	    	jumperRoute.setDescription(request.getParameter("description"));
	    	jumperRoute.setCreateTime(NumConv.currentTime());
	    	jumperRoute.setStation(station);
	    	Fiber_cores fiber=findService.findFiberCoreByFrameIDandPortOrder(frameId, portOrder);
	    	if(fiber!=null){//如果切换跳纤所连配线架的端口连有纤芯，查找该纤芯是否被光路使用
	    		//如果纤芯挂载在光路上，应该讲切换跳纤与光路关联
	    		Long routeId=findService.findFiberCoreByFrameIDandPortOrder(frameId, portOrder).getRoute_id();
	    		if(routeId!=null){//挂载了光路
	    			LinkedHashMap<String,Object> para=new LinkedHashMap<String,Object>();//用于生成端口占用XML的Map
	    		   	para.put("CM",switchRtuId);
	    		   	List<Integer> rtuPort=new ArrayList<Integer>();
	    			List<String> types=new ArrayList<String>();
	    		   	/**此处的端口序号应为1-64**/
	    		   	rtuPort.add((modelOrder-1)*8+aPortOrder);
	    		   	para.put("ports",rtuPort);
	    		   	Routes route=findService.findRoute(routeId);
	    		   	String portType=route.getIs_online()?"1":"0";
	    		   	types.add(portType);
	    		    para.put("types",types);
	    		   	Rtus rtu=findService.findRtu(switchRtuId);
	    		   	para.put("CLP",rtu.getStation().getId());
	    		   	String xml=XmlCodeCreater.setPortOccupy(para);
	    		   	//System.out.println("下发指令:\n"+xml);
	     			String rtuUrl="http://"+rtu.getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
	     			String fileName=NumConv.createVerifyCode(20);
	    			String responseFile="setRtuPortResponse"+fileName+".xml";
	    			String testDecode="testDecode"+fileName+".txt";
	    			//向RTU下发指令并解析回复
	    			try{
	    				HttpClientUtil.Post(rtuUrl,xml,responseFile,1500,2500); 
	    				XmlDom XmlDom=new XmlDom();
	    				Map<String,Object> responseMsg = XmlDom.AnalyseRespondse(responseFile,testDecode);
	    				/**-------------如果回复码为0,说明成功下发--------------------------------*/
	    		 	    String StatusCode=(String) responseMsg.get("StatusCode");
	    		   	    if(!StatusCode.equals("0")){//状态码不为0，说明RTU端接收故障，应将先前存储的光路删除
	    		   	    		status=false;
	    		   	    		err="下发参数异常，请核对后重试。";
	    		   	   }
	    		   	    else{
	    		   	    	status=true;
	    		   	    }
	    			}catch(ConnectTimeoutException|HttpException e){
	    					  e.printStackTrace();  //输入出错误原因
	    					  status=false;
	    	   	    		  err="设置端口占用时与RTU通信故障,与RTU的连接建立超时，请检查RTU的状态是否正常。";
	    			}
	    			catch(NullPointerException | DocumentException e){
	    				  e.printStackTrace();  //输入出错误原因
	    				  status=false;
	     	    		  err="设置端口占用时与RTU通信故障,RTU回复异常，请检查RTU的状态是否正常。";
	    		   }
	    			/**下发端口占用指令成功
	    			 * 存储**/
	    			if(status){
	    				 jumperId=addService.addJumperSwitch(jumperRoute);
	    				 jumperRoute.setRoute_id(routeId);
	 	    			 alterService.alterJumperRoute(jumperRoute);
	 	    			 route.setSwitchJumperId((Long) jumperId);
	 	    			 route.setSwitchRtuId(switchRtuId);
	 	    			 route.setSwitchRtuOrder((modelOrder-1)*8+aPortOrder);
	 	    			 route.setAlter_date(NumConv.currentTime());
	 	    			 Subject currentUser = SecurityUtils.getSubject();//获取当前用户
	 	    			 route.setAlter_user(currentUser.getPrincipal().toString());
	 	    			 status=alterService.alterRoute(route);
	    			}
	    		}
	    		/**未挂载光路，直接存储**/
	    		else{
	    			jumperId=addService.addJumperSwitch(jumperRoute);
	    			if(jumperId==null){
	    	    		err="数据库存储异常";
	    	    	}
		    	}
	    	}
	    	/**未连接纤芯，直接存储**/
	    	else{
	    		jumperId=addService.addJumperSwitch(jumperRoute);
	    		if(jumperId==null){
		    		err="数据库存储异常";
		    	}
	    	}
	    	
	    }
	    if(jumperType.equals("备纤光源跳纤")){
	    	Jumper_backups jumperBackup=new Jumper_backups();
	    	jumperBackup.setLength(request.getParameter("jumperLength"));
	    	jumperBackup.setJumperName(request.getParameter("jumperName"));
	    	Long backUpRtuId=Long.parseLong(request.getParameter("aEquipmentId"));
	    	jumperBackup.setRtu_id(backUpRtuId); 
	    	jumperBackup.setRtu_name(request.getParameter("aEquipmentName"));
	    	int aPortOrder = Integer.parseInt(request.getParameter("aPortOrder"));
	    	jumperBackup.setIn_port_order(aPortOrder);
	    	long frameId=Long.parseLong(request.getParameter("zEquipmentId"));//配线架ID
	    	int zPortOrder=Integer.parseInt(request.getParameter("zPortOrder"));//配线架端口
	    	jumperBackup.setFrame_id(frameId);
	    	jumperBackup.setFrame_name(request.getParameter("zEquipmentName"));
	    	jumperBackup.setFrame_port_order(zPortOrder);
	    	jumperBackup.setDescription(request.getParameter("description"));
	    	jumperBackup.setCreateTime(NumConv.currentTime());
	    	jumperBackup.setStation(station);
	    	jumperId=addService.addJumperBackup(jumperBackup);
	    	Fiber_cores fiber=findService.findFiberCoreByFrameIDandPortOrder(frameId, zPortOrder);
	    	if(fiber!=null&&(jumperId!=null)){//如果备纤光源跳纤所连配线架的端口连有纤芯，查找该纤芯是否被光路使用
	    		//如果纤芯挂载在光路上，应该将备纤光源跳纤与光路关联
	    		Long routeId=findService.findFiberCoreByFrameIDandPortOrder(frameId, zPortOrder).getRoute_id();
	    		if(routeId!=null){//挂载了光路
	    			jumperBackup=findService.findJumperBackup(jumperId);
	    			jumperBackup.setRoute_id(routeId);
	    			Routes route=findService.findRoute(routeId);
	    			route.setBackupJumperId((Long)jumperId);
	    			route.setBackupRtuId(backUpRtuId);
	    			route.setBackupRtuOrder(aPortOrder);
	    			route.setAlter_date(NumConv.currentTime());
	    			Subject currentUser=SecurityUtils.getSubject();//获取当前用户
	    			route.setAlter_user(currentUser.getPrincipal().toString());
	    			status=alterService.alterRoute(route);
	    			if(!status){
	    				err="数据库存储异常";
	    			}
	    		}
	    	}
	    }
		if(jumperId!=null){
			status=true;
		}
		Map<String, Object> responseData=new LinkedHashMap<String, Object>();
		responseData.put("status", status);
		responseData.put("err",err);
		JSONArray responseJson=JSONArray.fromObject(responseData);
	//	////System.out.println("JsonData:"+responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
  	
  }
/*--------------------------修改跳纤-------------------------*/
@RequestMapping("jumper/modifyJumper")
public void alterJumper(HttpServletRequest request,HttpServletResponse response) throws IOException{
	   Enumeration<?> paramNames = request.getParameterNames();  
       while (paramNames.hasMoreElements()) {  
       String paramName = (String) paramNames.nextElement();  
       String[] paramValues = request.getParameterValues(paramName);  
       if (paramValues.length == 1) {  
         String paramValue = paramValues[0];  
         if (paramValue.length() != 0) {  
           //System.out.println("参数：" + paramName + "=" + paramValue);  
          
         }  
       }  
      } 
        String type=request.getParameter("type");
        long jumperId=Long.parseLong(request.getParameter("jumperId"));
        boolean status=false;
        Map<String, Object> jumperPara=new LinkedHashMap<String, Object>();
        jumperPara.put("length",request.getParameter("jumperLength"));
        jumperPara.put("description",request.getParameter("description"));
        jumperPara.put("alter_time", NumConv.currentTime());
        if(type.equals("光路跳纤")||type.equals("切换跳纤")){
        	jumperPara.put("jumper_route_name",request.getParameter("jumperName"));
            status=alterService.updateJumperRoute(jumperId, jumperPara);
        }
        if(type.equals("配线架跳纤")){
        	jumperPara.put("jumper_frame_name",request.getParameter("jumperName"));
            status=alterService.updateJumperFrame(jumperId, jumperPara);
        }
        else{//备纤光源跳纤
        	Jumper_backups jumperBackup=findService.findJumperBackup(jumperId);
        	if(jumperBackup!=null){
        		jumperBackup.setLength(request.getParameter("jumperLength"));
        		jumperBackup.setDescription(request.getParameter("description"));
        		jumperBackup.setAlterTime( NumConv.currentTime());
        		jumperBackup.setJumperName(request.getParameter("jumperName"));
        		status=alterService.alterJumperBackup(jumperBackup);
        	}
        	else{
        		status=false;
        	}
        	
        }
		Map<String, Boolean> responseData=new LinkedHashMap<String, Boolean>();
		responseData.put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		//////System.out.println("JsonData:"+responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
  	
  }
/**--------------------------删除配线架跳纤-------------------------*/
@RequestMapping("jumper/delJumperFrame")
public void delJumperFrame(HttpServletRequest request,HttpServletResponse response) throws IOException{
	    long jumperId=Long.parseLong(request.getParameter("jumperId"));
		boolean status=true;
		try {
			delService.deleteJumperFrame(jumperId);
		} 
		catch (Exception e) {
			status = false;
		}
		finally {
			Map<String, Boolean> responseData=new LinkedHashMap<String, Boolean>();
			responseData.put("status", status);
			JSONArray responseJson=JSONArray.fromObject(responseData);
			PrintWriter out=response.getWriter();
			out.println(responseJson);
			out.flush();
			out.close();
		}
		
  }
/**--------------------------删除备纤光源跳纤-------------------------*/
@RequestMapping("jumper/delJumperBackup")
public void delJumperBackup(HttpServletRequest request,HttpServletResponse response) throws IOException{
	    long jumperId=Long.parseLong(request.getParameter("jumperId"));
		boolean status=true;
		try {
			delService.deleteJumperBackup(jumperId);
		} 
		catch (Exception e) {
			status = false;
		}
		finally {
			Map<String, Boolean> responseData=new LinkedHashMap<String, Boolean>();
			responseData.put("status", status);
			JSONArray responseJson=JSONArray.fromObject(responseData);
			PrintWriter out=response.getWriter();
			out.println(responseJson);
			out.flush();
			out.close();
		}
		
  }

/***--------------------------删除光路跳纤-------------------------*/
@RequestMapping("jumper/delJumperRoute")
public void delJumperRoute(HttpServletRequest request,HttpServletResponse response) throws IOException{
	    long jumperId=Long.parseLong(request.getParameter("jumperId"));
		boolean status=true;
		String err="";
		Jumper_routes jumper=findService.findJumperRoute(jumperId);
		Routes route=null;
		Protect_groups group=null;
		if(jumper.getRoute_id()!=null){
			 route=findService.findRoute(jumper.getRoute_id());
			 if(route!=null){
				 group=findService.findProtectGroupsByRouteId(route.getId());
			 }
		}
		Map<String, Object> responseData=new LinkedHashMap<String, Object>();
		if(group!=null){//存在保护组，删除保护组
			     List<Integer> QNs=new ArrayList<Integer>();
			     Routes routeA=findService.findRoute(group.getRouteDownOnId());
			     Routes routeB=findService.findRoute(group.getRouteDownOffId());
			     int orderA=routeA.getRtu_port_order();
				 int orderB=routeB.getRtu_port_order();
				 int PNo=Math.min(orderA,orderB);//配对组序号
				 PNo=(PNo-1)%8+1;//组号为1-4 即为单向配对组中小的端口序号(1-8编号)
				 QNs.add(PNo);
				 Map<String,Object> CancelPara=new LinkedHashMap<String,Object>();
				 CancelPara.put("CM", routeA.getRtu_id());
				 CancelPara.put("CLP", findService.findRtu(routeA.getRtu_id()).getStation().getId());
				 CancelPara.put("QNs", QNs);
				 String cancelCode=XmlCodeCreater.cancelProtectGroup(CancelPara);
				 //System.out.println("-------------XmlCodeCreater--------:\n"+cancelCode);
				 String rtuUrl="http://"+findService.findRtu(routeA.getRtu_id()).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
				 String fileName=NumConv.createVerifyCode(20);
				 String responseFile="cancelGroupResponse"+fileName+".xml";
				 String testDecode="cancelGrouptestDecode"+fileName+".txt";
				 try {
						 HttpClientUtil.Post(rtuUrl, cancelCode, responseFile, 3500, 4000);
						 XmlDom XmlDom=new XmlDom();
						 responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);//调用文件解析函数，获取回传数据
						 if(responseData.get("StatusCode").equals("0")){
							 status=true;
						 }
					} catch (HttpException | IOException | DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						err="当前跳纤挂载的光路存在保护组，在删除保护组时与RTU通信异常，请检查RTU的状态是否正常。";
					} 
					if(status){//下发成功，删除配对
				    			delService.deleteProtectGroup(group.getId());//删除配对
								//还原光路是否配对的字段
								routeA.setIsProtect(false);
								alterService.alterRoute(routeA);
								routeB.setIsProtect(false);
								alterService.alterRoute(routeB);
					}
			}
		/**切换跳纤，删除保护组后可以直接删除实体**/
		if(jumper.getIsSwitch()){
			status=true;
		}
		else{//光路跳纤，如果挂载了光路应该先向RTU下发解除端口占用指令
			if(route!=null){
				 Long rtuId=route.getRtu_id();
				 Map<String,Object>para=new LinkedHashMap<String,Object>();
			   	 para.put("CM", rtuId);
		   		 para.put("CLP",findService.findRtu(rtuId).getStation().getId());
		   		 List<Integer> ports=new ArrayList<Integer>();//占用端口的端口序号
		   		 ports.add(route.getRtu_port_order());
		   		 para.put("ports", ports);
		   		 String xml=XmlCodeCreater.cancelPortOccupy(para);
		   		 //System.out.println("send:"+xml);
		   		 String rtuUrl="http://"+findService.findRtu(rtuId).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
		   		 String fileName=NumConv.createVerifyCode(20);
				 String responseFile="cancelPort"+fileName+".xml";
				 String testDecode="cancelPortDecode"+fileName+".txt";
		   		 try {
					    HttpClientUtil.Post(rtuUrl, xml, responseFile, 3500, 4000);
					    XmlDom XmlDom=new XmlDom();
					    Map<String, Object> responseMsg=XmlDom.AnalyseRespondse(responseFile,testDecode);
			  	    	String StatusCode=(String) responseMsg.get("StatusCode");
			            if(StatusCode.equals("0")){//
			  	    		 status =delService.deleteRoute(route.getId());
			  	    		 if(!status){
			  	    			 err="数据库操作异常";
			  	    		 }
			  	    	 }
			  	    	 else{
			  	    		 status=false; 
			  	    		 err="在解除当前跳纤挂载端口时与RTU通信异常，请检查RTU的状态是否正常。";
			  	    	 }
				} catch (ConnectTimeoutException|HttpException| DocumentException e) {
					// TODO Auto-generated catch block
					 e.printStackTrace();  //输入出错误原因
					 status=false; 
					 err="在解除当前跳纤挂载端口时与RTU通信异常，请检查RTU的状态是否正常。";
	 	    	}
			}
		}
		if(status){//删除实体
			try {
				delService.deleteJumperRoute(jumperId);
			} 
			catch (Exception e) {
				status = false;
				err="数据库操作异常";
				e.printStackTrace();
			}
		}
		responseData.put("status", status);
		responseData.put("err", err);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
 }
/**------------------通过 id或name获取其相关的jumper-----------------*/
@SuppressWarnings("unchecked")
@RequestMapping("searchJumper")
public void searchJumper(HttpServletRequest request,HttpServletResponse response) throws IOException{
	Map<String, Object> jumperPara=new LinkedHashMap<String, Object>();
	int size=0;
	String type=request.getParameter("type");
	String name=request.getParameter("name");
	List<Jumper_frames> frameJumper=new ArrayList<Jumper_frames>();
	List<Jumper_routes> routeJumper=new ArrayList<Jumper_routes>();
	List <Jumper_backups> backupJumper=new ArrayList<Jumper_backups>();
	if(type.equals("光路跳纤")){
		if(name.length()>0)
			jumperPara.put("jumper_route_name", name);	
			routeJumper=findService.findJumperRoutesByMulti(jumperPara);
			size=routeJumper.size();
	}
	else if(type.equals("配线架跳纤")){
			if(name.length()>0)
			jumperPara.put("jumper_frame_name", name);
			frameJumper=findService.findJumperFramesByMulti(jumperPara);
			size=frameJumper.size();
	}
	else if(type.equals("备纤光源跳纤")){
		if(name.length()>0){
			backupJumper=findService.findJumperBackupByName(name);
			size=backupJumper.size();
		}
	}
	else if(type.equals("全部")){
		if(name.length()>0){
			jumperPara.put("jumper_frame_name", name);
		    frameJumper=findService.findJumperFramesByMulti(jumperPara);
		    jumperPara.clear();
			jumperPara.put("jumper_route_name", name);	
			routeJumper=findService.findJumperRoutesByMulti(jumperPara);
			backupJumper=findService.findJumperBackupByName(name);
			size=frameJumper.size()+routeJumper.size()+backupJumper.size();
		}
			
	}
	boolean status=false;
	LinkedHashMap<String,Object>[] responseData=new LinkedHashMap[size+1];
	responseData[0]=new LinkedHashMap<String, Object>();
	if(responseData.length>1){
			status=true;
			for(int JCount=0;JCount<frameJumper.size();JCount++){
				    responseData[JCount+1]=new LinkedHashMap<String, Object>();
				    responseData[JCount+1].put("id",frameJumper.get(JCount).getId());
					responseData[JCount+1].put("name",frameJumper.get(JCount).getJumper_frame_name());
					responseData[JCount+1].put("length",frameJumper.get(JCount).getLength());
					responseData[JCount+1].put("type","配线架跳纤");
					responseData[JCount+1].put("equipmentAId",frameJumper.get(JCount).getFrame_a_id());
					responseData[JCount+1].put("equipmentAName",frameJumper.get(JCount).getFrame_a_name());
					responseData[JCount+1].put("equipmentAPort",frameJumper.get(JCount).getPort_order_a());
					responseData[JCount+1].put("equipmentZId",frameJumper.get(JCount).getFrame_z_id());
					responseData[JCount+1].put("equipmentZName",frameJumper.get(JCount).getFrame_z_name());//
					responseData[JCount+1].put("equipmentZPort",frameJumper.get(JCount).getPort_order_z());
					responseData[JCount+1].put("stationName",frameJumper.get(JCount).getStation().getStation_name());
					responseData[JCount+1].put("stationId",frameJumper.get(JCount).getStation().getId());
					responseData[JCount+1].put("description",frameJumper.get(JCount).getDescription());
					responseData[JCount+1].put("createTime",frameJumper.get(JCount).getCreateTime());//
					responseData[JCount+1].put("alterTime",frameJumper.get(JCount).getAlterTime());//
					
				}
				int fSize=frameJumper.size();
				for(int jcount=0;jcount<routeJumper.size();jcount++){
					responseData[jcount+fSize+1]=new LinkedHashMap<String, Object>();
					responseData[jcount+fSize+1].put("id",routeJumper.get(jcount).getId());
					responseData[jcount+fSize+1].put("name",routeJumper.get(jcount).getJumper_route_name());
					responseData[jcount+fSize+1].put("length",routeJumper.get(jcount).getLength());
					if(findService.findRtu(routeJumper.get(jcount).getRtu_id()).getType().equals("切换rtu"))//FIXME
					  responseData[jcount+fSize+1].put("type","切换跳纤");
					else
					responseData[jcount+fSize+1].put("type","光路跳纤");
					responseData[jcount+fSize+1].put("equipmentAId",routeJumper.get(jcount).getRtu_id());
					responseData[jcount+fSize+1].put("equipmentAName",routeJumper.get(jcount).getRtu_name());//
					String order=RouteController.getPortOrder(routeJumper.get(jcount+fSize+1).getOtdr_port_order(),
						      routeJumper.get(jcount+fSize+1).getModelOrder());				
					responseData[jcount].put("equipmentAPort",order);
					responseData[jcount+fSize+1].put("equipmentZId",routeJumper.get(jcount).getFrame_id());
					responseData[jcount+fSize+1].put("equipmentZName",routeJumper.get(jcount).getFrame_name());
					responseData[jcount+fSize+1].put("equipmentZPort",routeJumper.get(jcount).getFrame_port_order());
					responseData[jcount+fSize+1].put("stationName",routeJumper.get(jcount).getStation().getStation_name());
					responseData[jcount+fSize+1].put("stationId",routeJumper.get(jcount).getStation().getId());
					responseData[jcount+fSize+1].put("description",routeJumper.get(jcount).getDescription());
					responseData[jcount+fSize+1].put("createTime",routeJumper.get(jcount).getCreateTime());//
					responseData[jcount+fSize+1].put("alterTime",routeJumper.get(jcount).getAlterTime());//
				}
				int bSize=fSize+routeJumper.size();
				for(int jcount=0;jcount<backupJumper.size();jcount++){
					responseData[jcount+bSize+1]=new LinkedHashMap<String, Object>();
					responseData[jcount+bSize+1].put("id",backupJumper.get(jcount).getId());
					responseData[jcount+bSize+1].put("name",backupJumper.get(jcount).getJumperName());
					responseData[jcount+bSize+1].put("length",backupJumper.get(jcount).getLength());
					responseData[jcount+bSize+1].put("type","备纤光源跳纤");
					responseData[jcount+bSize+1].put("equipmentAId",backupJumper.get(jcount).getRtu_id());
					responseData[jcount+bSize+1].put("equipmentAName",backupJumper.get(jcount).getRtu_name());//
					responseData[jcount+bSize+1].put("equipmentAPort",backupJumper.get(jcount).getIn_port_order());
					responseData[jcount+bSize+1].put("equipmentZId",backupJumper.get(jcount).getFrame_id());
					responseData[jcount+bSize+1].put("equipmentZName",backupJumper.get(jcount).getFrame_name());
					responseData[jcount+bSize+1].put("equipmentZPort",backupJumper.get(jcount).getFrame_port_order());
					responseData[jcount+bSize+1].put("stationName",backupJumper.get(jcount).getStation().getStation_name());
					responseData[jcount+bSize+1].put("stationId",backupJumper.get(jcount).getStation().getId());
					responseData[jcount+bSize+1].put("description",backupJumper.get(jcount).getDescription());
					responseData[jcount+bSize+1].put("createTime",backupJumper.get(jcount).getCreateTime());//
					responseData[jcount+bSize+1].put("alterTime",backupJumper.get(jcount).getAlterTime());//
				}
				
	}
	responseData[0].put("status", status);
	JSONArray responseJson=JSONArray.fromObject(responseData);
	PrintWriter out=response.getWriter();
	out.println(responseJson);
	out.flush();
	out.close();
	
} 
/**------------------端口管理树的生成，此处要列出局站下的所有RTU和配线架---------------------*/
@RequestMapping("portTree")//FIXME
public void portTree(HttpServletRequest request,HttpServletResponse response) throws IOException{
	/*****获取区域，局站，rtu表中的所有的信息*******/
	List<Areas> areas = findService.findAllAreas();
	List<Stations> stations=findService.findAllStations();
	List<Rtus> rtus=findService.findAllRtus();
	List<Distributing_frames> frames=findService.findAllFrames();
	List<root> roots=new ArrayList<root >(1);
	root portRoot = new root();
	roots.add(portRoot);
	//***********新建一个list，将三个对象的list进行拼接，并生成.jon文件，方便之后生成左侧的树形图***********//*
	List<Object>  list=new  ArrayList<Object>();
	list.addAll(areas);
	list.addAll(stations);
	list.addAll(rtus);
	list.addAll(frames);
	list.addAll(roots);
	response.setContentType("text/xml");
	response.setCharacterEncoding("utf-8");
	PrintWriter out = response.getWriter();
	out.print(list);	
//	////System.out.println(list);
	out.flush();
	out.close();
	
}



/*--------------------根据frame id获取全部frame port-----------------------*/
@SuppressWarnings({ "unchecked", "rawtypes" })
@RequestMapping("getFramePortByFrameId")
public void getFramePortByFrameId(HttpServletRequest request,HttpServletResponse response) throws IOException{
	    long frameId=Long.parseLong(request.getParameter("id"));
	    List<Frame_ports>framePort=findService.findFramePortsAllByFrameId(frameId);
	    boolean status=false;
	    LinkedHashMap[] responseData=new LinkedHashMap[framePort.size()+1];
		responseData[0]=new LinkedHashMap<Object, Object>();
		if(framePort.size()>0){//
			status=true;
			for(int rCount=0;rCount<framePort.size();rCount++){
				String statu="未使用";
				String connectionType=framePort.get(rCount).getConnection_type();
				Long connectionId=framePort.get(rCount).getConnection_id();
			    String connectionName="";
				if(connectionType!=null){
					switch (connectionType){   //连接设备类型
					 case "配线架跳纤":
						  connectionName=findService.findJumperFrame(connectionId).getJumper_frame_name();
					 break;
					 case "光路跳纤":
						 connectionName=findService.findJumperRoute(connectionId).getJumper_route_name();
						 break;
						 /**-----待添加其他设备类型------*/
					default:
						break;
					}
				}
				
				responseData[rCount+1]=new LinkedHashMap<Object, Object>();
				responseData[rCount+1].put("id",framePort.get(rCount).getId());
				responseData[rCount+1].put("order",framePort.get(rCount).getPort_order());
				if((framePort.get(rCount).getName()==null)||(framePort.get(rCount).getName().equals("")))
				{
					responseData[rCount+1].put("name","端口"+framePort.get(rCount).getPort_order());
				}
				else{
					
					 responseData[rCount+1].put("name",framePort.get(rCount).getName());
				}
				if(framePort.get(rCount).getStatus())
				{
					statu="已使用";
				}
				responseData[rCount+1].put("status",statu);
				responseData[rCount+1].put("equiType","配线架");
				responseData[rCount+1].put("equiName",framePort.get(rCount).getFrame().getFrame_name());
				responseData[rCount+1].put("equiId",framePort.get(rCount).getFrame().getId());
				responseData[rCount+1].put("connectId",connectionId);
				responseData[rCount+1].put("connectType",connectionType);
				responseData[rCount+1].put("connectName",connectionName);
				responseData[rCount+1].put("description",framePort.get(rCount).getDescription());
				responseData[rCount+1].put("alterUser",framePort.get(rCount).getAlterUser());
				responseData[rCount+1].put("alterTime",framePort.get(rCount).getAlterTime());
				
			}
		}
		responseData[0].put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		//////System.out.println("JsonData:"+responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
  	
  }
/*--------------------根据rtu id获取全部 rtu port-----------------------*/
@SuppressWarnings({ "unchecked", "rawtypes" })  //FIXME
@RequestMapping("getRtuPortByRtuId")
public void getRtuPortByRtuId(HttpServletRequest request,HttpServletResponse response) throws IOException{
	    long rtuId=Long.parseLong(request.getParameter("id"));
	    List<Rtu_ports> rtuPorts=findService.findRtu(rtuId).getRtu_ports();
	    boolean status=false;
		LinkedHashMap[] responseData=new LinkedHashMap[rtuPorts.size()+1];
		responseData[0]=new LinkedHashMap<Object, Object>();
		if(rtuPorts.size()>0){//有
			status=true;
			for(int rCount=0;rCount<rtuPorts.size();rCount++){
				String statu="未使用";
				Long connectId=rtuPorts.get(rCount).getJumper_route_id();
				responseData[rCount+1]=new LinkedHashMap<Object, Object>();
				responseData[rCount+1].put("id",rtuPorts.get(rCount).getId());
				responseData[rCount+1].put("order",rtuPorts.get(rCount).getPort_order());
				if((rtuPorts.get(rCount).getName()==null)||(rtuPorts.get(rCount).getName().equals("")))
				{
					responseData[rCount+1].put("name","端口"+rtuPorts.get(rCount).getPort_order());
				}
				else
				{
					responseData[rCount+1].put("name",rtuPorts.get(rCount).getName());
					
				}
				if(rtuPorts.get(rCount).getStatus())
				{
					statu="已使用";
				}
				responseData[rCount+1].put("status",statu);
				responseData[rCount+1].put("equiType","RTU");
				responseData[rCount+1].put("equiName",rtuPorts.get(rCount).getRtu().getRtu_name());
				responseData[rCount+1].put("equiId",rtuPorts.get(rCount).getRtu().getId());
				responseData[rCount+1].put("connectId",connectId);
				if(connectId!=null)
				 {
					responseData[rCount+1].put("connectName",findService.findJumperRoute(connectId).getJumper_route_name());  
				 }
				responseData[rCount+1].put("connectType",rtuPorts.get(rCount).getPort_type());
				responseData[rCount+1].put("description",rtuPorts.get(rCount).getDescription());
				responseData[rCount+1].put("alterUser",rtuPorts.get(rCount).getAlterUser());
				responseData[rCount+1].put("alterTime",rtuPorts.get(rCount).getAlterTime());
			}
		}
		responseData[0].put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
	//	////System.out.println("JsonData:"+responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();  	
  }
/*--------------------修改port----------------------*/
@RequestMapping("port/modifyPort")
  public void alterPort(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map <String,Object> portPara=new LinkedHashMap<String,Object>();
		String type=request.getParameter("type");
		boolean status=false;
		Long id=Long.parseLong(request.getParameter("id"));
		portPara.put("name", request.getParameter("name"));
		portPara.put("description", request.getParameter("description"));
		portPara.put("alter_time", NumConv.currentTime());
	   	 Subject currentUser = SecurityUtils.getSubject();//获取当前用户
    	 String Account=currentUser.getPrincipal().toString();//当前用户的账号
		portPara.put("alter_user", Account);
		if(type.equals("rtuPort")){ //rtuPort
			status=alterService.updateRtuPort(id, portPara);
		}
		if(type.equals("framePort")){
			status=alterService.updateFramePort(id, portPara);
		}
		Map<String, Boolean> responseData=new LinkedHashMap<String, Boolean>();
		responseData.put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		//////System.out.println("JsonData:"+responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
    	
    }

/**--------------------通过id查找port----------------------*/
@SuppressWarnings({ "unchecked", "rawtypes" })
@RequestMapping("port/checkPort")
  public void getPortById(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Long id=Long.parseLong(request.getParameter("id"));
		boolean status=false;
		Frame_ports framePort=findService.findFramePort(id);
		Rtu_ports rtuPort=findService.findRtuPort(id);
		LinkedHashMap[] responseData=new LinkedHashMap[2];
		responseData[0]=new LinkedHashMap();
		if(framePort!=null)
		{
			status=true;
			String statu="未使用";
			String connectionType=framePort.getConnection_type();
			Long connectionId=framePort.getConnection_id();
		    String connectionName="";
			if(connectionType!=null){
				switch (connectionType){   //连接设备类型
				 case "配线架跳纤":
					  connectionName=findService.findJumperFrame(connectionId).getJumper_frame_name();
				 break;
				 case "光路跳纤":
					 connectionName=findService.findJumperRoute(connectionId).getJumper_route_name();
					 break;
					 /**-----待添加其他设备类型------*/
				default:
					break;
				}
			}
			responseData[1]=new LinkedHashMap<Object, Object>();
			responseData[1].put("id",framePort.getId());
			responseData[1].put("order",framePort.getPort_order());
			if((framePort.getName()==null)||(framePort.getName().equals("")))
			  responseData[1].put("name","端口"+framePort.getPort_order());
			  
			else{
				responseData[1].put("name",framePort.getName());
			}
			if(framePort.getStatus())
			{
				statu="已使用";
			}
			responseData[1].put("status",statu);
			responseData[1].put("equiType","配线架");
			responseData[1].put("equiName",framePort.getFrame().getFrame_name());
			responseData[1].put("equiId",framePort.getFrame().getId());
			responseData[1].put("connectId",connectionId);
			responseData[1].put("connectType",connectionType);
			responseData[1].put("connectName",connectionName);
			responseData[1].put("description",framePort.getDescription());
			responseData[1].put("alterUser",framePort.getAlterUser());
			responseData[1].put("alterTime",framePort.getAlterTime());
			
		}
		else if(rtuPort!=null){
			   status=true;
			   String statu="未使用";
				Long connectId=rtuPort.getJumper_route_id();
				responseData[1]=new LinkedHashMap<Object, Object>();
				responseData[1].put("id",rtuPort.getId());
				responseData[1].put("order",rtuPort.getPort_order());
				if((rtuPort.getName()==null)||(rtuPort.getName().equals("")))
				  
			    	responseData[1].put("name","端口"+rtuPort.getPort_order());
				else{
					responseData[1].put("name",rtuPort.getName());
				}
				if(rtuPort.getStatus()){
					statu="已使用";
				}
				responseData[1].put("status",statu);
				responseData[1].put("equiType","RTU");
				responseData[1].put("equiName",rtuPort.getRtu().getRtu_name());
				responseData[1].put("equiId",rtuPort.getRtu().getId());
				responseData[1].put("connectId",connectId);
				String connectName="";
				String connectType="";
				if(connectId!=null){
					Jumper_routes jumper=findService.findJumperRoute(connectId);
					connectName=jumper.getJumper_route_name();
					connectType=jumper.getIsSwitch()?"切换跳纤":"光路跳纤";
				 }
				responseData[1].put("connectName",connectName);  
				responseData[1].put("connectType",connectType);
				responseData[1].put("description",rtuPort.getDescription());
				responseData[1].put("alterUser",rtuPort.getAlterUser());
				responseData[1].put("alterTime",rtuPort.getAlterTime());
			
		}
		
		responseData[0].put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		//////System.out.println("JsonData:"+responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
    	
    }

/*--------------------通过名称或标识多条件查找port----------------------*/
@SuppressWarnings({ "unchecked", "rawtypes" })
@RequestMapping("getPortByMulti")
  public void getPortByMulti(HttpServletRequest request,HttpServletResponse response) throws IOException{
	   Long id=null;
	   if(request.getParameter("id").length()>0){
		   id=Long.parseLong(request.getParameter("id"));
	   }
		String name=request.getParameter("name");
		Map<String,Object> portPara=new LinkedHashMap<String,Object>();
		if(id!=null){
			portPara.put("id", id);
		}
		if(!(name.equals(""))&&(name!=null)){
			portPara.put("name", name);
		}
		boolean status=false;
	    List <Frame_ports> framePort=findService.findFramePortByMulti(portPara);
	    List <Rtu_ports> rtuPort=findService.findRtuPortByMulti(portPara);
		LinkedHashMap[] responseData=new LinkedHashMap[rtuPort.size()+framePort.size()+1];
		responseData[0]=new LinkedHashMap();
		if(responseData.length>1)
		{
			status=true;
			String statu="未使用";
			for(int Count=1;Count<responseData.length;Count++){
				responseData[Count]=new LinkedHashMap<Object, Object>();
				if((Count-1)<framePort.size()){
						String connectionType=framePort.get(Count-1).getConnection_type();
						Long connectionId=framePort.get(Count-1).getConnection_id();
					    String connectionName="";
						if(connectionType!=null){
							switch (connectionType){   //连接设备类型
							 case "配线架跳纤":
								  connectionName=findService.findJumperFrame(connectionId).getJumper_frame_name();
							 break;
							 case "光路跳纤":
								 connectionName=findService.findJumperRoute(connectionId).getJumper_route_name();
								 break;
								 /**-----待添加其他设备类型------*/
							default:
								break;
							}
						}
						
						responseData[Count].put("id",framePort.get(Count-1).getId());
						responseData[Count].put("order",framePort.get(Count-1).getPort_order());
						if((framePort.get(Count-1).getName()!=null)&&(!framePort.get(Count-1).getName().equals("")))
						  responseData[Count].put("name",framePort.get(Count-1).getName());
						else{
							responseData[Count].put("name",framePort.get(Count-1).getPort_order());
						}
						if(framePort.get(Count-1).getStatus())
						{
							statu="已使用";
						}
						responseData[Count].put("status",statu);
						responseData[Count].put("equiType","配线架");
						responseData[Count].put("equiName",framePort.get(Count-1).getFrame().getFrame_name());
						responseData[Count].put("equiId",framePort.get(Count-1).getFrame().getId());
						responseData[Count].put("connectId",connectionId);
						responseData[Count].put("connectType",connectionType);
						responseData[Count].put("connectName",connectionName);
						responseData[Count].put("description",framePort.get(Count-1).getDescription());
						responseData[Count].put("alterUser",framePort.get(Count-1).getAlterUser());
						responseData[Count].put("alterTime",framePort.get(Count-1).getAlterTime());
				}
				else{
						Long connectId=rtuPort.get(Count-framePort.size()-1).getJumper_route_id();
						responseData[Count].put("id",rtuPort.get(Count-framePort.size()-1).getId());
						responseData[Count].put("order",rtuPort.get(Count-framePort.size()-1).getPort_order());
						if((rtuPort.get(Count-framePort.size()-1).getName()==null)||(rtuPort.get(Count-framePort.size()-1).getName().equals("")))
						{
							responseData[Count].put("name","端口"+rtuPort.get(Count-framePort.size()-1).getPort_order());
						} 
						else
						{
							responseData[Count].put("name",rtuPort.get(Count-framePort.size()-1).getName());
							
						}
						if(rtuPort.get(Count-framePort.size()-1).getStatus())
						{
							statu="已使用";
						}
						responseData[Count].put("status",statu);
						responseData[Count].put("equiType","RTU");
						responseData[Count].put("equiName",rtuPort.get(Count-framePort.size()-1).getRtu().getRtu_name());
						responseData[Count].put("equiId",rtuPort.get(Count-framePort.size()-1).getRtu().getId());
						responseData[Count].put("connectId",connectId);
						if(connectId!=null)
						 {
							responseData[Count].put("connectName",findService.findJumperRoute(connectId).getJumper_route_name());  
						 }
						responseData[Count].put("connectType",rtuPort.get(Count-framePort.size()-1).getPort_type());
						responseData[Count].put("description",rtuPort.get(Count-framePort.size()-1).getDescription());
						responseData[Count].put("alterUser",rtuPort.get(Count-framePort.size()-1).getAlterUser());
						responseData[Count].put("alterTime",rtuPort.get(Count-framePort.size()-1).getAlterTime());
				}
			}
		}
		
		responseData[0].put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		//////System.out.println("JsonData:"+responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
    	
    }
/*-----------------fiber管理树的生成，此处要列出局站下的所有RTU和配线架---------------------*/
@RequestMapping("fiberTree")  //FIXME
public void fiberTree(HttpServletRequest request,HttpServletResponse response) throws IOException{
	List<Areas> areas = findService.findAllAreas();
	List<Stations> stations=findService.findAllStations();
	List<Optical_cables> cables=findService.findAllCables();
	List<root> roots=new ArrayList<root >(1);
	root portRoot = new root();
	roots.add(portRoot);
	//***********新建一个list，将三个对象的list进行拼接，并生成.jon文件，方便之后生成左侧的树形图***********//*
	List<Object>  list=new  ArrayList<Object>();
	list.addAll(areas);
	list.addAll(stations);
	list.addAll(cables);
	list.addAll(roots);
	response.setContentType("text/xml");
	response.setCharacterEncoding("utf-8");
	PrintWriter out = response.getWriter();
	out.print(list);	
	//////System.out.println(list);
	out.flush();
	out.close();
	
}
@SuppressWarnings({ "unchecked", "rawtypes" })
@RequestMapping("getFiberByCableId")
public void getFiberByCableId(HttpServletRequest request,HttpServletResponse response) throws IOException{
	    Long id=Long.parseLong(request.getParameter("id"));
	    List<Fiber_cores>fiber=findService.findFiberCoresByOpticalCableId(id);
	    boolean status=false;
	    LinkedHashMap[] responseData=new LinkedHashMap[fiber.size()+1];
		responseData[0]=new LinkedHashMap<Object, Object>();
		if(fiber.size()>0){//
			status=true;
			for(int rCount=0;rCount<fiber.size();rCount++){
				String fiberStatus="未使用";
				if(fiber.get(rCount).getStatus()){
					fiberStatus="已使用";
				}
				String length=fiber.get(rCount).getLength();
				if((length==null)||(length.equals(""))){
					length=fiber.get(rCount).getOptical_cable().getLength();
				}
				String refractiveIndex=fiber.get(rCount).getRefractive_index();
				if((refractiveIndex==null)||(refractiveIndex.equals(""))){
					refractiveIndex=fiber.get(rCount).getOptical_cable().getRefractive_index();
				}
				String frameAOrder=String.valueOf(fiber.get(rCount).getPort_order_a());
				if(frameAOrder.equals("0")){
					frameAOrder="";
				}
				String frameZOrder=String.valueOf(fiber.get(rCount).getPort_order_z());
				if(frameZOrder.equals("0")){
					frameZOrder="";
				}
				responseData[rCount+1]=new LinkedHashMap<Object, Object>();
				responseData[rCount+1].put("id",fiber.get(rCount).getId());
				responseData[rCount+1].put("name",fiber.get(rCount).getName());
				responseData[rCount+1].put("length",length);
				responseData[rCount+1].put("refractiveIndex",refractiveIndex);
				responseData[rCount+1].put("cableId",fiber.get(rCount).getOptical_cable().getId());
				responseData[rCount+1].put("cableName",fiber.get(rCount).getOptical_cable().getOptical_cable_name());
				responseData[rCount+1].put("order",fiber.get(rCount).getCore_order());
				responseData[rCount+1].put("status",fiberStatus);
				responseData[rCount+1].put("stationAName",fiber.get(rCount).getOptical_cable().getStation_a_name());
				responseData[rCount+1].put("frameAId",fiber.get(rCount).getFrame_a_id());
				responseData[rCount+1].put("frameAName",fiber.get(rCount).getFrame_a_name());
				responseData[rCount+1].put("frameAOrder",frameAOrder);
				responseData[rCount+1].put("stationZName",fiber.get(rCount).getOptical_cable().getStation_z_name());
				responseData[rCount+1].put("frameZId",fiber.get(rCount).getFrame_z_id());
				responseData[rCount+1].put("frameZName",fiber.get(rCount).getFrame_z_name());
				responseData[rCount+1].put("frameZOrder",frameZOrder);
				responseData[rCount+1].put("description",fiber.get(rCount).getDescription());
				responseData[rCount+1].put("stationAId",fiber.get(rCount).getOptical_cable().getStation_a_id());
				responseData[rCount+1].put("stationZId",fiber.get(rCount).getOptical_cable().getStation_z_id());
			} 
		}
		responseData[0].put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		//////System.out.println("JsonData:"+responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
  	
  }
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping("getFiberByMulti")
public void getFiberByMulti(HttpServletRequest request,HttpServletResponse response) throws IOException{
	    String name=request.getParameter("name");
	    Map<String,Object> fiberPara=new LinkedHashMap<String,Object>();
	    if((name.length()!=0)&&(!name.equals(""))){
	    	fiberPara.put("name", name);
	    }
	    List<Fiber_cores>fiber=findService.findFibersByMulti(fiberPara);
	    boolean status=false;
	    LinkedHashMap[] responseData=new LinkedHashMap[fiber.size()+1];
		responseData[0]=new LinkedHashMap<Object, Object>();
		if(fiber.size()>0){//
			status=true;
			for(int rCount=0;rCount<fiber.size();rCount++){
				String fiberStatus="未使用";
				if(fiber.get(rCount).getStatus()){
					fiberStatus="已使用";
				}
				String length=fiber.get(rCount).getLength();
				if((length==null)||(length.equals(""))){
					length=fiber.get(rCount).getOptical_cable().getLength();
				}
				String refractiveIndex=fiber.get(rCount).getRefractive_index();
				if((refractiveIndex==null)||(refractiveIndex.equals(""))){
					refractiveIndex=fiber.get(rCount).getOptical_cable().getRefractive_index();
				}
				String frameAOrder=String.valueOf(fiber.get(rCount).getPort_order_a());
				if(frameAOrder.equals("0")){
					frameAOrder="";
				}
				String frameZOrder=String.valueOf(fiber.get(rCount).getPort_order_z());
				if(frameZOrder.equals("0")){
					frameZOrder="";
				}
				responseData[rCount+1]=new LinkedHashMap<Object, Object>();
				responseData[rCount+1].put("id",fiber.get(rCount).getId());
				responseData[rCount+1].put("name",fiber.get(rCount).getName());
				responseData[rCount+1].put("length",length);
				responseData[rCount+1].put("refractiveIndex",refractiveIndex);
				responseData[rCount+1].put("cableId",fiber.get(rCount).getOptical_cable().getId());
				responseData[rCount+1].put("cableName",fiber.get(rCount).getOptical_cable().getOptical_cable_name());
				responseData[rCount+1].put("order",fiber.get(rCount).getCore_order());
				responseData[rCount+1].put("status",fiberStatus);
				responseData[rCount+1].put("stationAName",fiber.get(rCount).getOptical_cable().getStation_a_name());
				responseData[rCount+1].put("frameAId",fiber.get(rCount).getFrame_a_id());
				responseData[rCount+1].put("frameAName",fiber.get(rCount).getFrame_a_name());
				responseData[rCount+1].put("frameAOrder",frameAOrder);
				responseData[rCount+1].put("stationZName",fiber.get(rCount).getOptical_cable().getStation_z_name());
				responseData[rCount+1].put("frameZId",fiber.get(rCount).getFrame_z_id());
				responseData[rCount+1].put("frameZName",fiber.get(rCount).getFrame_z_name());
				responseData[rCount+1].put("frameZOrder",frameZOrder);
				responseData[rCount+1].put("description",fiber.get(rCount).getDescription());
				responseData[rCount+1].put("stationAId",fiber.get(rCount).getOptical_cable().getStation_a_id());
				responseData[rCount+1].put("stationZId",fiber.get(rCount).getOptical_cable().getStation_z_id());
			} 
		}
		responseData[0].put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		//////System.out.println("JsonData:"+responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
  	
  }
/**--------------------修改fiber----------------------*/
@RequestMapping("fiber/modifyFiber")
  public void alterFiber(HttpServletRequest request,HttpServletResponse response) throws IOException{
	    printAllRequestPara(request);
		Map <String,Object> fiberPara=new LinkedHashMap<String,Object>();
		boolean status=false;
		Long frameAId=null;
		Long frameZId=null;
		Integer portAOrder=0;
		Integer portZOrder=0;
		long oldFrameAId = 0;
		int oldFrameAPortOrder = 0;
		long oldFrameBId = 0;
		int oldFrameBPortOrder = 0;
		if(!request.getParameter("frameAId").equals("")&&!request.getParameter("frameAId").equals("index")){
			frameAId=Long.parseLong(request.getParameter("frameAId"));
		}
		if(!request.getParameter("frameZId").equals("")&&!request.getParameter("frameZId").equals("index")){
			frameZId=Long.parseLong(request.getParameter("frameZId"));
		}
		if(!request.getParameter("frameAOrder").equals("")&&!request.getParameter("frameAOrder").equals("index")){
			portAOrder=Integer.parseInt(request.getParameter("frameAOrder"));
		}
		if(!request.getParameter("frameZOrder").equals("")&&!request.getParameter("frameZOrder").equals("index")){
			portZOrder=Integer.parseInt(request.getParameter("frameZOrder"));
		}
		Long id=Long.parseLong(request.getParameter("id"));
		fiberPara.put("name", request.getParameter("name"));
		fiberPara.put("refractive_index", request.getParameter("refractive_index"));
		fiberPara.put("length", request.getParameter("length"));
		fiberPara.put("frame_a_id",frameAId);
		String frameAName=request.getParameter("frameAName").equals("请选择")?"":request.getParameter("frameAName");
		String frameZName=request.getParameter("frameZName").equals("请选择")?"":request.getParameter("frameZName");
		fiberPara.put("frame_a_name",frameAName);
		fiberPara.put("port_order_a",portAOrder);
		fiberPara.put("frame_z_id",frameZId);
		fiberPara.put("frame_z_name",frameZName);
		fiberPara.put("port_order_z",portZOrder);
		fiberPara.put("description",request.getParameter("description"));
		if((portAOrder!=0)&&(portZOrder!=0)){
			fiberPara.put("status", true);
		}
		status=alterService.updateFiberCore(id, fiberPara);
		if(status){
			if(frameAId!=null&&portAOrder!=null){
				Frame_ports frameAPort=findService.findFramePortByFrameIdAndPortId(frameAId, portAOrder);
				if(frameAPort!=null){
					frameAPort.setHas_fiber(true);
					alterService.alterFramePort(frameAPort);
				}
				Frame_ports frameZPort=findService.findFramePortByFrameIdAndPortId(frameZId, portZOrder);
				if(frameZPort!=null){
					frameZPort.setHas_fiber(true);
					alterService.alterFramePort(frameZPort);
				}	
			}
		}	
		if(!request.getParameter("oldFrameAId").equals("")){
			oldFrameAId = (long)Integer.parseInt(request.getParameter("oldFrameAId"));//////////////
		}
		if(!request.getParameter("oldFrameAPortOrder").equals("")){
			oldFrameAPortOrder = Integer.parseInt(request.getParameter("oldFrameAPortOrder"));///////////////
		}
		if(!request.getParameter("oldFrameBId").equals("")){
			oldFrameBId = (long)Integer.parseInt(request.getParameter("oldFrameBId"));//////////////////
		}
		if(!request.getParameter("oldFrameBPortOrder").equals("")){
			oldFrameBPortOrder = Integer.parseInt(request.getParameter("oldFrameBPortOrder"));/////////////
		}
		if(oldFrameAId!=0&&oldFrameAPortOrder!=0)
		{
			if(oldFrameAId!=frameAId||oldFrameAPortOrder!=portAOrder){  //////改变原来的端口搭载光钎的字段
				Frame_ports oldframeAPort=findService.findFramePortByFrameIdAndPortId(oldFrameAId, oldFrameAPortOrder);
				oldframeAPort.setHas_fiber(false);
				alterService.alterFramePort(oldframeAPort);
			}
		}
		if(oldFrameBId!=0&&oldFrameBPortOrder!=0)
		{
			if(oldFrameBId!=frameZId||oldFrameBPortOrder!=portZOrder){
				Frame_ports oldframeBPort=findService.findFramePortByFrameIdAndPortId(oldFrameBId, oldFrameBPortOrder);
				oldframeBPort.setHas_fiber(false);
				alterService.alterFramePort(oldframeBPort);//////////////////////////
			}
		}
		
		Map<String, Boolean> responseData=new LinkedHashMap<String, Boolean>();
		responseData.put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
    	
    }

 /**
  * 封装局站坐标信息到前端进行绘图
  * JSON格式的数组
  * 封装方式：每一个JSON对象包含
  * name:局站名称
  * lng:经度
  * lat:纬度
  ***/
 @RequestMapping("getMapPoints")
 public void getMapPoints(HttpServletRequest request,HttpServletResponse response ) throws IOException{
	 List<Stations> Stations=findService.findAllStations();
	 Map<String,Object> responseData=new LinkedHashMap<String,Object>();
	 List <Object>  Points=new ArrayList<Object>();
	 boolean status=false;
	 if(Stations.size()>0){
		 status=true;
		 String lng=null;
		 String lat=null;
		 for(int index=0;index<Stations.size();index++){
			 Map<String,Object> point=new LinkedHashMap<String,Object>();
			 point.put("name", Stations.get(index).getStation_name());
			 point.put("id", Stations.get(index).getId());
			 lng=Stations.get(index).getLongitude().split("经")[1];
			 lat=Stations.get(index).getLatitude().split("纬")[1];
			 point.put("lng",lng);
			 point.put("lat",lat);
			 Points.add(point);
		 }
	 }
	 responseData.put("status", status);
	 responseData.put("points", Points);
	 response.setContentType("text/xml");
	 response.setCharacterEncoding("utf-8");
	 PrintWriter out=response.getWriter();
	 JSONArray responseJson=JSONArray.fromObject(responseData);
	 //System.out.println("回传数据:"+responseJson);
	 out.println(responseJson); 
     out.flush();
	 out.close();
 }
 /**
  * 封装光缆地理位置信息用于在GIS上描绘
  * 封装格式：JSON数组
  * 每一个JSON对象的封装内容：
  * name:光缆名称
  * pointA 数组 局站A的经纬度
  * pointB 数组 局站B的经纬度
  * count 以A、B为起始站的光缆的条数
  * landMarks 光缆地标 
  * 每个值包含lng和lat
  * 
  * **/
 @RequestMapping("getCableLines")
 public void getCableLines(HttpServletRequest request,HttpServletResponse response ) throws IOException{
	 List<Optical_cables> Cables=findService.findAllCables();
	 Map<String,Object> responseData=new LinkedHashMap<String,Object>();
	 boolean status=false;
	 List <Object>  Lines=new ArrayList<Object>();
	 ////System.out.println("光缆数目："+Cables.size());
	 if(Cables.size()>0){
		 status=true;
		 for(int dex=0;dex<Cables.size();dex++){
			 for(int de=dex+1;de<Cables.size();de++){
				 if(Cables.get(dex).getStation_a_id()==Cables.get(de).getStation_a_id()&&Cables.get(dex).getStation_z_id().equals(Cables.get(de).getStation_z_id()))
					 {
					   Cables.remove(de);
					   de--;
					 }
			 }
		 }
		 for(int index=0;index<Cables.size();index++){
			 Map<String,Object> line=new LinkedHashMap<String,Object>();
			 List<Optical_cables>Cable=findService.findByAZStationId(Cables.get(index).getStation_a_id(),Cables.get(index).getStation_z_id());
			 int count=Cable.size();
			 List <String> names=new ArrayList<String>();
			 List<Long> ids=new ArrayList<Long>();
			 List<List<Map<String,Object> >> landmarks=new ArrayList<List<Map<String,Object> >>();
			 for(int cou=0;cou<Cable.size();cou++){
				 names.add(Cable.get(cou).getOptical_cable_name());
				 ids.add(Cable.get(cou).getId());
				 List<Landmarks> allLandmarks=Cable.get(cou).getLandmarks();//光缆地标
				 List<Map<String,Object>>marks=new ArrayList<Map<String,Object>>();
				 for(int i=0;i<allLandmarks.size();i++){
					 Map<String,Object> landmark=new LinkedHashMap<>();
					 landmark.put("name", allLandmarks.get(i).getName());
					 landmark.put("lat",allLandmarks.get(i).getLatitude().split("纬")[1]);
					 landmark.put("lng", allLandmarks.get(i).getLongitude().split("经")[1]);
					 marks.add(landmark);
				 }
				 landmarks.add(marks);//
			 }
			 String lngLogA[]=new String[2];
			 String lngLogB[]=new String[2];
			 lngLogA[0]=findService.findStation(Cables.get(index).getStation_a_id()).getLongitude().split("经")[1];
			 lngLogA[1]=findService.findStation(Cables.get(index).getStation_a_id()).getLatitude().split("纬")[1];
			 lngLogB[0]=findService.findStation(Cables.get(index).getStation_z_id()).getLongitude().split("经")[1];
			 lngLogB[1]=findService.findStation(Cables.get(index).getStation_z_id()).getLatitude().split("纬")[1];
			 line.put("pointA",lngLogA);
			 line.put("pointB",lngLogB);
			 line.put("landmarks", landmarks);
			 line.put("count",count);
			 line.put("id",ids);
			 line.put("name",names);
			 Lines.add(line);
		 }
	 }
	 responseData.put("status", status);
	 responseData.put("cables", Lines);
	 response.setContentType("text/xml");
	 response.setCharacterEncoding("utf-8");
	 PrintWriter out=response.getWriter();
	 JSONArray responseJson=JSONArray.fromObject(responseData);
	 ////System.out.println("回传数据:"+responseJson);
	 out.println(responseJson); 
     out.flush();
	 out.close();
	
 }
 /**获取所有告警提示方式**/
 @RequestMapping("getAllAlarmWay")
 public void getAllAlarmWay(HttpServletRequest request,HttpServletResponse response ) throws IOException{
	 List<Alarm_ways> alarm=findService.findAllAlarmWay();
	 Map<String,Object> responseData=new LinkedHashMap<String,Object>();
	 boolean status=false;
	 if(alarm!=null){
		 status=true;
		 List<Map<String,Object>> allWays=new ArrayList<Map<String,Object>>();
		 for(int i=0;i<alarm.size();i++){
			 Map<String,Object> alarmWay=new LinkedHashMap<String,Object>();
			 alarmWay.put("name",alarm.get(i).getAlarm_name());
			 alarmWay.put("id",alarm.get(i).getId());
			 allWays.add(alarmWay);
		 }
		 responseData.put("alarmWay",allWays);
	}
	 responseData.put("status", status);
	 responseData.put("status", status);
	 response.setContentType("text/xml");
	 response.setCharacterEncoding("utf-8");
	 PrintWriter out=response.getWriter();
	 JSONArray responseJson=JSONArray.fromObject(responseData);
	 ////System.out.println("回传数据:"+responseJson);
	 out.println(responseJson); 
     out.flush();
	 out.close();	
 }
 /**根据RTU Id获取告警方式**/
 @RequestMapping("getAlarmWay")
 public void getAlarmWay(HttpServletRequest request,HttpServletResponse response ) throws IOException{
	 Rtus rtu=findService.findRtu(Long.parseLong(request.getParameter("rtuId")));
	 Alarm_ways alarm=null;
	 if(rtu.getAlarm_wayId()!=null){
		 alarm=findService.findAlarmWayById(rtu.getAlarm_wayId());
	 }
	 boolean status=false;
	 Map<String,Object> responseData=new LinkedHashMap<String,Object>();
	 if(alarm!=null){
		 status=true;
		 String alarmWay=alarm.getAlarm_name();
		 responseData.put("alarmWay", alarmWay);
	 }
	 responseData.put("status", status);
	 response.setContentType("text/xml");
	 response.setCharacterEncoding("utf-8");
	 PrintWriter out=response.getWriter();
	 JSONArray responseJson=JSONArray.fromObject(responseData);
	 ////System.out.println("回传数据:"+responseJson);
	 out.println(responseJson); 
     out.flush();
	 out.close();	
 }
 /**根据RTU Id获取告模块信息**/
 @RequestMapping("masterCon/getMoudleInfo")
 public void getMoudleInfo(HttpServletRequest request,HttpServletResponse response ) throws IOException{
	 Long rtuId=Long.parseLong(request.getParameter("rtuId"));
	 Rtus rtu=findService.findRtu(rtuId);
	 boolean status=false;
	 Map<String,Object> responseData=new LinkedHashMap<String,Object>();
	 if(rtu!=null){
		 status=true;
		 int moudleNum=findService.findModelNumByRtuId(rtuId);//模块数量
		 List<Map<String,Object>> moudles=new ArrayList<Map<String,Object>>();
		 String info=rtu.getInstallInfo();
		 for(int i=1;i<=moudleNum;i++){
			 Map<String,Object> moudle=new LinkedHashMap<String,Object>();
			 moudle.put("order", i);
			 String type=info.substring(i-1, i);
			 switch(type){
			    	case"1":
			    		type="在线模块";
			    		break;
			    	case"2":
			    		type="备纤模块";
			    		break;
			    	case"3":
			    		type="保护-主模块";
			    		break;
			    	case"4":
			    		type="在线OPM模块";
			    		break;
			    	case"5":
			    		type="保护-从模块";
			    		break;
			    	default:
			    		type="未知";
			    		break;
			 }
			 moudle.put("type", type);
			 moudle.put("portNum",8);
			 int used=findService.findModelUsedPortNumByRtuIdAndModelOrder(rtuId, i);
			 String isUsed=(used>0)?("已使用"):("未使用");
			 moudle.put("status",isUsed);
			 moudle.put("useNum",used);//使用端口数
			 if(i!=moudleNum||(i==1)){
			    moudle.put("setTime",rtu.getCreate_date());//
			 }
			 else{
				 moudle.put("setTime",rtu.getAlter_date());// 
			 }
			 moudles.add(moudle);
		 }
		 responseData.put("moudles", moudles);
	 }
	 responseData.put("status", status);
	 response.setContentType("text/xml");
	 response.setCharacterEncoding("utf-8");
	 PrintWriter out=response.getWriter();
	 JSONArray responseJson=JSONArray.fromObject(responseData);
	 //System.out.println("回传数据:"+responseJson);
	 out.println(responseJson); 
     out.flush();
	 out.close();	
 }
 /**根据RTU Id获取RTU类型  用于主控模块**/
 @RequestMapping("masterCon/getRtuType")
 public void getRtuType(HttpServletRequest request,HttpServletResponse response ) throws IOException{
	 Long rtuId=Long.parseLong(request.getParameter("rtuId"));
	 Rtus rtu=findService.findRtu(rtuId);
	 boolean status=false;
	 Map<String,Object> responseData=new LinkedHashMap<String,Object>();
	 if(rtu!=null){
		 status=true;
		 int type=rtu.getType().contains("普通")?0:1;
		 responseData.put("type",type);
	 }
	 responseData.put("status", status);
	 response.setContentType("text/xml");
	 response.setCharacterEncoding("utf-8");
	 PrintWriter out=response.getWriter();
	 JSONArray responseJson=JSONArray.fromObject(responseData);
	 //System.out.println("回传数据:"+responseJson);
	 out.println(responseJson); 
     out.flush();
	 out.close();	
 }
 /**主控管理  列出RTU**/
 @RequestMapping(value="masterConTree")	//FIXME
	public void  createTree (HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		/*****获取区域，局站，rtu表中的所有的信息*******/
		List<Areas> areas = findService.findAllAreas();
		List<Stations> stations=findService.findAllStations();		
		List<Rtus> rtus=findService.findAllRtus();
		List<root> roots=new ArrayList<root >(1);
		root demo = new root();
		roots.add(demo);
		//***********新建一个list，将三个对象的list进行拼接，并生成.jon文件，方便之后生成左侧的树形图***********//*
		List<Object>  list=new  ArrayList<Object>();
		list.addAll(areas);
		list.addAll(stations); 
		list.addAll(rtus);
		list.addAll(roots);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(list);	
		//System.out.println(list);
		out.flush();
		out.close();
}
 /***上传excel文件批量导入地标
  * 经度在前，纬度在后
  * 
  * 
 * @throws IOException **/
 @RequestMapping("uploadLandmark")
 public void uploadLandmark(HttpServletRequest request,HttpServletResponse response) throws IOException{
	 /**获取sessionFactory**/
	 WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();    
	 SessionFactory sessionFactory=(SessionFactory)wac.getBean("sessionFactory");
	 Session session=sessionFactory.openSession();
	 Transaction tx=session.beginTransaction();
	 Optical_cables cable=null;
	 boolean status=true;
	 String err="";
	 Map<String,Object> responseData=new LinkedHashMap<String,Object>();
	 String currentPath=getClass().getResource("../").getFile().toString().substring(1).split("/")[0];//当前路径去掉/
	 String uploadPath =currentPath+"/uploadFile"; // 上传文件的目录  
	 File folder = new File(uploadPath);
     folder.mkdirs();//新建文件夹
	 File tempPathFile=new File(uploadPath);  
	 String sourceFile="";//接收到的文件名称 //System.out.print("upload succeed"); 
	 // Create a factory for disk-based file items  
     DiskFileItemFactory factory = new DiskFileItemFactory();  
     factory.setSizeThreshold(4096); // 设置缓冲区大小，这里是4kb  
     factory.setRepository(tempPathFile);// 设置缓冲区目录  
     ServletFileUpload upload = new ServletFileUpload(factory);  
     upload.setSizeMax(4194304); // 设置最大文件尺寸，这里是4MB  
     try {
		   @SuppressWarnings("unchecked")
		   List<FileItem> items= upload.parseRequest(request);
	       Iterator<FileItem> thisFile = items.iterator();  
           while (thisFile.hasNext()) {  
               FileItem fi = (FileItem) thisFile.next();  
               if (fi.isFormField()) {  
                   //如果是普通表单字段  
                   String name = fi.getFieldName();  
                   if(name.equals("cableId")){//前端下发的landmark
                	   cable=findService.findOpticalCable(Long.parseLong(fi.getString()));
                   }
               }
               else { //文件
	               String fileName = fi.getName();  
	               if (fileName != null) {  
	                   File fullFile = new File(new String(fi.getName().getBytes(), "utf-8")); // 解决文件名乱码问题  
	                   sourceFile= fullFile.getName();//.split("\\.")[0]+".xls";//记录文件路径  jxl只支持.xls格式的excel
	                   File savedFile = new File(uploadPath,sourceFile);  //存储文件
	                   sourceFile=uploadPath+"/"+sourceFile;
	                   fi.write(savedFile);  
	               }  
              }
           }
	} catch (Exception e) {
		status=false;
		err="文件格式不正确，请上传Excel 97-2003工作薄(后缀名为.xls)。";
		// TODO Auto-generated catch block
		e.printStackTrace();
	}// 得到所有的文件  
   try{ 
		   Workbook book = Workbook.getWorkbook(new File(sourceFile));
		   //0代表第一个工作表对象
		   Sheet sheet = book.getSheet(0);
		   int rows = sheet.getRows();
		   int cols = sheet.getColumns();
		   Map<String,Integer> colMap=new LinkedHashMap<String, Integer>();
		   /**筛选有用的信息列
		    * 地标经纬度项目必须包含关键字 经纬度
		    * 地标名称必须包含关键字 名称
		    * 描述必须包含关键字 描述
		    * 地标类型需包含关键字 类型
		    * 
		    * 经纬度条目有两种方式   30.6455，103.94492 或  30°38′43.8",103°56′41.712"
		    * **/
		   for(int i=0;i<cols;i++){
			   String colname = sheet.getCell(i, 0).getContents().trim();
			   if(colname.contains("类型")){
				   colMap.put("type", i);
				}
			   else if(colname.contains("名称")){
				   colMap.put("name", i);
			   }
			   else if(colname.contains("经纬度")){
				   colMap.put("lngAndLat", i);
			   }
			   else if(colname.contains("描述")){
				   colMap.put("description", i);
			   }
		   }
		  int count=0;
		   /**存储各地标，读取每列个各行**/
		   for (int index = 1; index < rows; index++) 
		   {
			   //0代表列数，z代表行数
			  if(colMap.get("lngAndLat")!=null){
				/**将符号统一规范**/
				   String lngAndLat[]=sheet.getCell((int)colMap.get("lngAndLat"), index).getContents().replaceAll(" ", "").replaceAll("，",",").replaceAll("\"", "″").split(",");//替换掉所有空格
				   //地标，名称
				   String name="";
				   if(colMap.get("name")!=null){
					  name=sheet.getCell((int)colMap.get("name"), index).getContents();
				   }
				  //地标类型
				   String type="";
				   if(colMap.get("type")!=null){
					  type=sheet.getCell((int)colMap.get("type"), index).getContents();
				   }
				  //描述
				   String description="";
				   if(colMap.get("type")!=null){
					   description=sheet.getCell((int)colMap.get("description"), index).getContents();//经度
				   }
				  Landmarks mark=new Landmarks();
		          mark.setCreateTime(NumConv.currentTime());
		          Subject currentUser = SecurityUtils.getSubject();//获取当前用户
	   			  String account=currentUser.getPrincipal().toString();//当前用户的账号
		          mark.setCreateUser(account);
		          mark.setDescription(description);
		          mark.setName(name);
		          String lng=lngAndLat[0].contains("°")?NumConv.lngAndLatConv(lngAndLat[0]):lngAndLat[0];
		          String lat=lngAndLat[1].contains("°")?NumConv.lngAndLatConv(lngAndLat[1]):lngAndLat[1];
		          /**数据库中的经纬度都是按小数的形式存储**/
		          try{
			          mark.setLatitude("北纬"+lat);
			          mark.setLongitude("东经"+lng);
		          }catch(Exception e){
		        	  status=false;
		        	  err="文件中经纬度格式不正确，请按\"30°3843.8,103°5641.712\"的格式录入经纬度。";
		      	      e.printStackTrace();
		          }
		          count++;
		          mark.setType(type);
		          mark.setOptical_cable(cable);
		          /**批量操作**/
		          session.save(mark);
		          if(count%20==0){//到20个后存储一次
		        	 session.flush();
		        	 session.clear();
		          }
		       }
	      }
		   //提交事务
		   tx.commit();
		   session.close();
		   book.close();//关闭文件 
	 }
      catch(Exception e){
    	status=false;
    	err="文件中含有非法条目或内容格式不正确，请根据规定的内容格式重新上传文件。";
	    e.printStackTrace();
      }
     responseData.put("status", status);
     responseData.put("err", err);
     response.setContentType("text/xml");
	 response.setCharacterEncoding("utf-8");
	 PrintWriter out=response.getWriter();
	 JSONArray responseJson=JSONArray.fromObject(responseData);
	 //System.out.println("回传数据:"+responseJson);
	 //System.out.println("response time:"+System.currentTimeMillis());
	 out.println(responseJson); 
     out.flush();
	 out.close();	 
   }
 /**测试邮件服务器可用与否
 * @throws IOException **/
 @RequestMapping("masterCon/checkEmail")
 public void testEmail(HttpServletRequest request,HttpServletResponse response) throws IOException{
	 printAllRequestPara(request);
	 String address=request.getParameter("address");
	 int port=Integer.parseInt(request.getParameter("port"));
	 String account=request.getParameter("account");
	 String pword=request.getParameter("pword");
	 Map<String,Object>emailPara=new LinkedHashMap<String,Object>();
	 emailPara.put("address", address);
	 emailPara.put("port", port);
	 emailPara.put("account", account);
	 emailPara.put("pword", pword);
	 boolean status=MessageUtil.checkServer(emailPara);
	 String err="";
	 Map<String,Object> responseData=new LinkedHashMap<String,Object>();
	 responseData.put("status", status);
     responseData.put("err", err);
     response.setContentType("text/xml");
	 response.setCharacterEncoding("utf-8");
	 PrintWriter out=response.getWriter();
	 JSONArray responseJson=JSONArray.fromObject(responseData);
	 //System.out.println("回传数据:"+responseJson);
	 //System.out.println("response time:"+System.currentTimeMillis());
	 out.println(responseJson); 
     out.flush();
	 out.close();	
 }
 /**添加邮件服务器信息
 * @throws IOException **/
 @RequestMapping("masterCon/setEmail")
 public void addEmail(HttpServletRequest request,HttpServletResponse response) throws IOException{
	 printAllRequestPara(request);
	 String address=request.getParameter("address");
	 String port=request.getParameter("port");
	 String account=request.getParameter("account");
	 String pword=request.getParameter("pword");
	 boolean status=true;
	 SystemInfo systemInfo=findService.findSystemInfoById((long)1);
	 if(systemInfo!=null){
		 systemInfo.setEmailAddr(address);
		 systemInfo.setEmailPort(port);
		 systemInfo.setEmailAccount(account);
		 systemInfo.setEmailPword(pword);
		 status=alterService.alterSystemInfo(systemInfo);
	 }
	 else{
		 systemInfo=new SystemInfo();
		 InetAddress addr = InetAddress.getLocalHost();
		 String ip=addr.getHostAddress().toString();//获得本机IP
		 systemInfo.setIp(ip);
		 systemInfo.setEmailAddr(address);
		 systemInfo.setEmailPort(port);
		 systemInfo.setEmailAccount(account);
		 systemInfo.setEmailPword(pword);
		 Serializable id=addService.addSystem(systemInfo);
		 if(id==null){
			status=false;
		 }
	 }
	 Map<String,Object> responseData=new LinkedHashMap<String,Object>();
	 if(!status){
		 responseData.put("err", "数据库操作异常，请稍后重试。");
	 }
	 responseData.put("status", status);
     response.setContentType("text/xml");
	 response.setCharacterEncoding("utf-8");
	 PrintWriter out=response.getWriter();
	 JSONArray responseJson=JSONArray.fromObject(responseData);
	 //System.out.println("回传数据:"+responseJson);
	 //System.out.println("response time:"+System.currentTimeMillis());
	 out.println(responseJson); 
     out.flush();
	 out.close();	
 }
 /**获取当前邮件服务器信息
  * @throws IOException **/
  @RequestMapping("getEmail")
  public void getEmail(HttpServletRequest request,HttpServletResponse response) throws IOException{
 	 boolean status=false;
 	 Map<String,Object> responseData=new LinkedHashMap<String,Object>();
 	 SystemInfo systemInfo=findService.findSystemInfoById((long)1);
 	 if(systemInfo!=null){
 		if(systemInfo.getEmailAccount()!=null){
 			status=true;
 			responseData.put("account", systemInfo.getEmailAccount());
 			responseData.put("port", systemInfo.getEmailPort());
 			responseData.put("address", systemInfo.getEmailAddr());
 		}
 	 }
 	 responseData.put("status", status);
     response.setContentType("text/xml");
 	 response.setCharacterEncoding("utf-8");
 	 PrintWriter out=response.getWriter();
 	 JSONArray responseJson=JSONArray.fromObject(responseData);
 	 //System.out.println("回传数据:"+responseJson);
 	 out.println(responseJson); 
     out.flush();
 	 out.close();	
  }
  /**获取服务器IP地址**/
  @RequestMapping("getServerIp")
  public void getServerIp(HttpServletRequest request,HttpServletResponse response) throws IOException{
	  boolean status=false;
	  Map<String,Object> responseData=new LinkedHashMap<String,Object>();
	  SystemInfo systemInfo=findService.findSystemInfoById((long)1);
	 	 if(systemInfo!=null){
	 		if(systemInfo.getIp()!=null){
	 			status=true;
	 			responseData.put("ip", systemInfo.getIp());
	 		}else{
	 			 InetAddress addr = InetAddress.getLocalHost();
	 			 String ip=addr.getHostAddress().toString();//获得本机IP
	 			 systemInfo.setIp(ip);
	 			 status=true;
	 			  responseData.put("ip", ip);
	 			 alterService.alterSystemInfo(systemInfo);
	 		}
	  }
	  else{
		  systemInfo=new SystemInfo(); 
		  InetAddress addr = InetAddress.getLocalHost();
		  String ip=addr.getHostAddress().toString();//获得本机IP
		  systemInfo.setIp(ip);
		  addService.addSystem(systemInfo);
		  status=true;
		  responseData.put("ip", ip);
	  }
	  responseData.put("status", status);
	  response.setContentType("text/xml");
	  response.setCharacterEncoding("utf-8");
	  PrintWriter out=response.getWriter();
	  JSONArray responseJson=JSONArray.fromObject(responseData);
	  //System.out.println("回传数据:"+responseJson);
	  out.println(responseJson); 
	  out.flush();
	  out.close();	
  }
  /**获取服务器IP地址**/
  @RequestMapping("test/forTest")
  public void forTest(HttpServletRequest request,HttpServletResponse response) throws IOException{
	  boolean status=false;
	  Map<String,Object> responseData=new LinkedHashMap<String,Object>();
	  SystemInfo systemInfo=findService.findSystemInfoById((long)1);
	 	 if(systemInfo!=null){
	 		if(systemInfo.getIp()!=null){
	 			status=true;
	 			responseData.put("ip", systemInfo.getIp());
	 		}else{
	 			 InetAddress addr = InetAddress.getLocalHost();
	 			 String ip=addr.getHostAddress().toString();//获得本机IP
	 			 systemInfo.setIp(ip);
	 			 status=true;
	 			  responseData.put("ip", ip);
	 			 alterService.alterSystemInfo(systemInfo);
	 		}
	  }
	  else{
		  systemInfo=new SystemInfo(); 
		  InetAddress addr = InetAddress.getLocalHost();
		  String ip=addr.getHostAddress().toString();//获得本机IP
		  systemInfo.setIp(ip);
		  addService.addSystem(systemInfo);
		  status=true;
		  responseData.put("ip", ip);
	  }
	  responseData.put("status", status);
	  responseData.put("time", NumConv.currentTime());
	  response.setContentType("text/xml");
	  response.setCharacterEncoding("utf-8");
	  PrintWriter out=response.getWriter();
	  JSONArray responseJson=JSONArray.fromObject(responseData);
	  //System.out.println("回传数据:"+responseJson);
	  out.println(responseJson); 
	  out.flush();
	  out.close();	
  } 
  /***获取服务器的时间
 * @throws ParseException **/
  @RequestMapping("getServerTime")
  public void getServerTime(HttpServletRequest request,HttpServletResponse response) throws IOException{
	  boolean status=true;
	  Map<String,Object> responseData=new LinkedHashMap<String,Object>();
	  responseData.put("status", status);
	  responseData.put("time", NumConv.currentTime());
	  String stamp="";
	  try {
		stamp = NumConv.dateToJavaStamp(NumConv.currentTime());
	  } catch (ParseException e) {
		  status=false;
		// TODO Auto-generated catch block
		e.printStackTrace();
	  }
	  responseData.put("stamp",stamp);
	  response.setContentType("text/xml");
	  response.setCharacterEncoding("utf-8");
	  PrintWriter out=response.getWriter();
	  JSONArray responseJson=JSONArray.fromObject(responseData);
	  //System.out.println("回传数据:"+responseJson);
	  out.println(responseJson); 
	  out.flush();
	  out.close();
	}	
}