package fiberMonitor.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import domain.Areas;
import domain.Cabinets;
import domain.Distributing_frames;
import domain.Duty_operator;
import domain.Duty_schedule;
import domain.Frame_ports;
import domain.Jumper_routes;
import domain.Log;
import domain.Optical_powers;
import domain.Protect_groups;
import domain.Racks;
import domain.Role;
import domain.Routes;
import domain.Rtu_ports;
import domain.Rtus;
import domain.Stations;
import domain.Threshold;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import service.AddService;
import service.AlterService;
import service.DeleteService;
import service.FindService;
import fiberMonitor.bean.AreaJson;
import fiberMonitor.bean.CabinetJson;
import fiberMonitor.bean.HttpClientUtil;
import fiberMonitor.bean.NumConv;
import fiberMonitor.bean.TCPComndUtil;
import fiberMonitor.bean.TcpClient;
import fiberMonitor.bean.XmlCodeCreater;
import fiberMonitor.bean.XmlDom;
import fiberMonitor.bean.distributeFrameJson;
import fiberMonitor.bean.root;
 

@Controller
public class resController {
@Resource(name="findService")
private FindService findService;
@Resource(name="addService") 
private AddService addService;
@Resource(name="alterService") 
private AlterService alterService;
@Resource(name="deleteService")
private DeleteService delService;

private final String[] weeks={
								"星期一",
								"星期二",
								"星期三",
								"星期四",
								"星期五",
								"星期六",
								"星期天"
		                      };

/*****************罗列出区域信息**********************/
   @RequestMapping(value="ResourceListArea")	
	public  void listArea (HttpServletRequest request,HttpServletResponse response) throws IOException
	{
	   List<Areas> areas =findService.findAllAreas();
	   
		List<AreaJson> areaJson=new ArrayList<AreaJson >(areas.size());
		for(int i = 0 ; i < areas.size(); i++){  
			AreaJson aaa = new AreaJson();  
			areaJson.add(aaa);  
        }  
		for(int i=0;i<areaJson.size();i++)
		{
			areaJson.get(i).setArea_id(areas.get(i).getId());
			areaJson.get(i).setArea_name(areas.get(i).getArea_name());
			areaJson.get(i).setDescription(areas.get(i).getDescription());
			areaJson.get(i).setCreate_date(areas.get(i).getCreate_date());
			areaJson.get(i).setAlter_date(areas.get(i).getAlter_date());
		}
		JSONArray areasJson=JSONArray.fromObject(areaJson);	
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(areasJson.toString());			
		out.flush();
		out.close();
	}
   
/******************增加区域*************************/   
   @RequestMapping(value="area/addArea")	
	public  void addAreas (HttpServletRequest request,HttpServletResponse response) throws IOException
	{
	   boolean status=false;
	   String area_name=request.getParameter("areaName");
	   String description=request.getParameter("areaDescription");
	   Areas area=new Areas();
	   area.setArea_name(area_name);
	   area.setDescription(description);
	   Date date = new Date();
	   DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	   String time = format.format(date);
	   area.setCreate_date(time);
	   LinkedHashMap<String, Boolean> responseData=new LinkedHashMap<String, Boolean>();
	   Serializable  areaId=addService.addArea(area);
	   if(areaId!=null)
	    {
		   status=true;
		}
	     responseData.put("status", status);
         JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
         response.setContentType("text/xml");
		 response.setCharacterEncoding("utf-8");
		 PrintWriter out=response.getWriter();
		 out.println(responseJson);
		 out.flush();
		 out.close();
	}
	
   /****************删除区域******************/   
   @RequestMapping(value="area/delArea")	
	public  void delArea (HttpServletRequest request,HttpServletResponse response) throws IOException{
	   boolean status=true;
	   LinkedHashMap<String, Object> responseData=new LinkedHashMap<String, Object>();
	   long areaId=Long.parseLong(request.getParameter("areaID"));
	   List<Stations> stations=findService.findStationsByAreaId(areaId);
	   String err="";
	   /**清除RTU模式的线程**/
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
				String rtuUrl="http://"+rtu.getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
				 //向RTU下发指令并解析回复
				 try{
					    String responseFile=NumConv.createVerifyCode(20)+"clearRtuMode.xml";
						String testDecode=NumConv.createVerifyCode(20)+"testDecode.txt";
						HttpClientUtil.Post(rtuUrl,xml,responseFile,2000,3000); 
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
							  status=false;
				    }
			}
		}
	   if(stations!=null){
		   for(Stations station:stations){
			   List<Rtus>rtus= station.getRtus();
			   if(rtus!=null){
				   List<DelRtu> deRtus=new ArrayList<DelRtu>();
				   for(Rtus rtu:rtus){
					   if(rtu.getType().contains("普通")){
						   DelRtu delRtu=new DelRtu(rtu);
						   deRtus.add(delRtu);
						   delRtu.start();
					   }
				   }
				   for(DelRtu delRtu:deRtus){
					   try {
						   delRtu.join();
					   } catch (InterruptedException e) {
						   break;
					   }
				   }
				   for(DelRtu delRtu:deRtus){
					   status&=delRtu.getStatus();
				   }
			   }
		   }
	   }
	   if(!status){
		   err="当前区域下的局站存在RTU，在解除RTU模式时与RTU通信故障，请检查RTU状态是否正常";
	   }else{
		   try {
				 delService.deleteArea(areaId);
				} 
			   catch (Exception e) {
				   status = false;
				   err="数据库操作异常";
			   }
	   }
	   responseData.put("status", status);
	   responseData.put("err", err);
	   JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
	   response.setContentType("text/xml");
	   response.setCharacterEncoding("utf-8");
	   PrintWriter out=response.getWriter();
	   out.println(responseJson);
	   out.flush();
	   out.close();
	}
/*******************修改区域********************/   
   @RequestMapping(value="area/modifyArea")	
	public  void alterArea (HttpServletRequest request,HttpServletResponse response) throws IOException{	 
	   //从前端获取参数
	   long areaId=Long.parseLong(request.getParameter("areaID"));
	   String new_area_name=request.getParameter("alterareaName");
	   String new_description=request.getParameter("areaDescription");
	   boolean status=true;
	   LinkedHashMap<String, Boolean> responseData=new LinkedHashMap<String, Boolean>(); 
	   try {
			   //对数据库做修改操作
			   Areas area = findService.findArea(areaId);
			   area.setArea_name(new_area_name);
			   area.setDescription(new_description);
			   Date date=new Date();
			   DateFormat format= new SimpleDateFormat("yyyy-MM-dd");
			   String time = format.format(date);
			   area.setAlter_date(time);
			   alterService.alterArea(area);
		} catch (Exception e) {
				status=false;
		}finally {
			   /*将反馈信息传到前端*/
			 responseData.put("status", status);
		     JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
	         response.setContentType("text/xml");
			 response.setCharacterEncoding("utf-8");
			 PrintWriter out=response.getWriter();
			 out.println(responseJson);
			 out.flush();
			 out.close();
		}
	}

/*******************点击局站，罗列rtu********************/   
   @RequestMapping(value="ResourceListRtu")	//FIXME
  	public  void listRtu (HttpServletRequest request,HttpServletResponse response) throws IOException{	   
  	 long stationId=Long.parseLong(request.getParameter("stationID"));
  	 Stations station = findService.findStation(stationId);
  	 List<Rtus> rtus=station.getRtus();
  	 List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
  	 Map<String , Object> mapStatus = new LinkedHashMap<>();
  	 if(rtus.size()==0)
  	 {
  		mapStatus.put("status", false);
  		mapList.add(mapStatus);
  	 }  		 
  	 else
  	 {
  		mapStatus.put("status", true);
  		mapList.add(mapStatus);
  		for(int i = 0;i<rtus.size();i++)
  	  	 {
  	  		 Map<String , Object> map = new LinkedHashMap<String , Object>();
  	  		 map.put("rtuId", rtus.get(i).getId());
  	  		 map.put("rtuName",rtus.get(i).getRtu_name());
  	  		 Long alarmId=rtus.get(i).getAlarm_wayId();
  	  		 String alarm="本地可视";
  	  		 if(alarmId!=null){
  	  			 alarm=findService.findAlarmWayById(alarmId).getAlarm_name();
  	  		 }
  	  		 map.put("tackOrder", rtus.get(i).getRack().getRack_order());
  	  		 map.put("alarmWay",alarm);
  	  		 map.put("rtuUrl", rtus.get(i).getRtu_url());
  	  		 map.put("stationId", rtus.get(i).getStation().getId());
  	  		 map.put("stationName", rtus.get(i).getStation().getStation_name());
  	  		 map.put("description", rtus.get(i).getDescription());
  	  		 map.put("createDate", rtus.get(i).getCreate_date());
  	  		 map.put("createUser", rtus.get(i).getCreate_user());
  	  		 map.put("alterDate", rtus.get(i).getAlter_date());
  	  		 map.put("alterUser", rtus.get(i).getAlter_user());
  	  		 map.put("installInfo", rtus.get(i).getInstallInfo());
  	  		 map.put("type", rtus.get(i).getType());
  	  		 mapList.add(map);
  	  	 }
  	 }  	 
	 JSONArray rtusJson=JSONArray.fromObject(mapList);
	 response.setContentType("text/xml");
	 response.setCharacterEncoding("utf-8");
	 PrintWriter out = response.getWriter();
	 out.print(rtusJson.toString());			
	 out.flush();
	 out.close();
  	}
   /*******************查询特定rtu的信息********************/   
   @RequestMapping(value="getRtuInfoById")	//FIXME
  	public  void getRtuInfoById (HttpServletRequest request,HttpServletResponse response) throws IOException
  	{	   
	   long rtuId =Long.parseLong(request.getParameter("rtuId"));
	   Rtus rtu = findService.findRtu(rtuId);
	   Map<String , Object> map = new LinkedHashMap<>();
	   map.put("installInfo", rtu.getInstallInfo());
	   JSONArray rtusJson=JSONArray.fromObject(map);
	   response.setContentType("text/xml");
	   response.setCharacterEncoding("utf-8");
	   PrintWriter out = response.getWriter();
	   out.print(rtusJson.toString());			
	   out.flush();
	   out.close();
  	}
   
 
/***************增加RTU
 * RTU包括五种类型的模块
 *    模块名称          模块类型号		OTDR测试                光功率相关测试                                                                 备注 
 *    在线模块		1			    具有	                      不具有									业务纤
 *    备纤模块		2			    具有                           具有                                            				备用纤
 *    在线OPM模块	4                          具有                           具有                                            				带光功率测试的业务纤
 *    保护模块-主		3                          具有                    在线纤具有，备纤不具有                    保护模块的接收机端的模块，保护光路在该模块所在RTU端生成 
 *    保护模块-从		5                        不具有                        不具有                                         保护模块的发射机端模块，只用于控制切换，不生成光路
 * @throws IOException ***********************/   
 
@RequestMapping(value="rtu/addRtu")
 	public  void AddRtu (HttpServletRequest request,HttpServletResponse response) throws IOException
 	{ 	  
	   ResourceController.printAllRequestPara( request);
 	   /*获取从前端传来的数据*/
	   long rackId = Long.parseLong(request.getParameter("rackId"));
 	   String rtuNameAdd=request.getParameter("rtuNameAdd");
 	   long stationIdAdd=Long.parseLong(request.getParameter("stationIdAdd"));
 	   String rtuKeyAdd=request.getParameter("rtuKeyAdd");
 	   String rtuUrlAdd=request.getParameter("rtuUrlAdd");
	   String descriptionAdd=request.getParameter("descriptionAdd");
	   String installInfo = request.getParameter("installInfo");
	   String type = request.getParameter("type");
	   boolean status=false;
	   Map<String, Object> responseData =new LinkedHashMap<>();
	   String err="";
	   if(type.contains("普通")){//普通RTU下发占用
		   /**向RTU发送模块模式指令**/
	       String modeType=installInfo;//读取每个模块的类型
	       Map<String, Object> para=new LinkedHashMap<String, Object>();
	       para.put("CM","*");
	       para.put("CLP", stationIdAdd);
	       List<Map<String,String>> modules=new ArrayList<Map<String,String>>();
	       for(int i=0; i<modeType.length();i++){
	    	   Map<String,String> moudle=new LinkedHashMap<String, String>();
	    	   moudle.put("KNo", String.valueOf(1+i));
	    	   moudle.put("Type",String.valueOf(modeType.charAt(i)));
	    	   modules.add(moudle);
	       }
	       para.put("modules", modules);
	       String xml=XmlCodeCreater.setRtuMode(para);
	       //System.out.println("文件为："+xml);
	       String rtuUrl="http://"+rtuUrlAdd+":5000/cgi-bin/BoaCom.cgi";
	       class SetRtuTime extends Thread{
	    	   private String url;
	    	   public SetRtuTime(String urlRtu){
	    		  this.url=urlRtu;
	    	   }
	    	   public void run(){
	    		    Map<String, Object> para=new LinkedHashMap<String, Object>();
	   	            para.put("CM","*");
	   	            para.put("CLP", stationIdAdd);
	   	            try {
						para.put("T8", NumConv.dateToUnixStamp(NumConv.currentTime()));
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    		String xmlCode=XmlCodeCreater.adjustRtuTime(para);
		  			//System.out.println("======setRtu Time========code:"+xmlCode);
		  			String responseFile=NumConv.createVerifyCode(20)+"setRtuTime.xml";
				    try {
						HttpClientUtil.Post(url,xmlCode,responseFile,2000,3000);
					} catch (HttpException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	    	  }
	       }
	       //向RTU下发指令并解析回复
			  try{
				  
				  String responseFile=NumConv.createVerifyCode(20)+"setRtuMode.xml";
				  String testDecode=NumConv.createVerifyCode(20)+"testDecode.txt";
				  HttpClientUtil.Post(rtuUrl,xml,responseFile,3500,4000); 
				  XmlDom XmlDom=new XmlDom();
				  responseData = XmlDom.AnalyseRespondse(responseFile,testDecode);
				 /**-------------如果回复码为0,说明成功下发--------------------------------*/
		    	  String StatusCode=(String) responseData.get("StatusCode");
		    	  if(StatusCode.equals("0")){//状态码为0，说明RTU端接收正常
		    		  status=true;
		    		  new SetRtuTime(rtuUrl).start();
		    	   }
		    	  /**回复码为15，说明前端下发指定的模块中有物理上不存在的模块**/
		    	  else if(StatusCode.equals("15")){//
		    		  status=false;
		    		  err="您指定新建的模块中包含实际硬件未连接的模块，请核对硬件连接状态后重试。";
		    	   }
		    	  else{
		    		  status=false;
		    		  err="RTU回复异常，请稍后重试，若一直出现该故障，请检查RTU状态是否正常。";
		    	  }
			  }catch(DocumentException | IOException|NullPointerException e){
	    		 status=false;
	    		  err="RTU通信故障,RTU回复异常，请稍后重试，若一直出现该故障请核对RTU状态是否正常。";
	    	 }
			  catch(HttpException e){
				  e.printStackTrace();  //输入出错误原因
				  status=false;
	    		  err="RTU通信故障,与RTU建立连接失败，请核对RTU是否正常工作及RTU地址是否正确。";
	    	 }
	   }
	   else{//备纤光源RTU不设置端口占用，直接新建
		   status=true;
	   }
	   /**发送指令成功，将数据存入数据库*/
		  if(status){
			   Rtus rtu = new Rtus();
			   rtu.setRtu_name(rtuNameAdd);
			   Stations station=findService.findStation(stationIdAdd);
			   rtu.setStation(station);
			   rtu.setRtu_key(rtuKeyAdd);
			   rtu.setRtu_url(rtuUrlAdd);
			   rtu.setDescription(descriptionAdd);
			   Subject currentUser = SecurityUtils.getSubject();//获取当前用户
			   String account=currentUser.getPrincipal().toString();//当前用户的账号
			   rtu.setCreate_user(account);
			   rtu.setCreate_date(NumConv.currentTime());
			   if(type.equals("普通rtu")){
				   rtu.setInstallInfo(installInfo);
			   }
			   else{//备纤光源RTU设置分光路数
				   int portNum = Integer.parseInt(request.getParameter("portNum"));
				   int dividNum= Integer.parseInt(request.getParameter("dividNum"));
				   rtu.setDividNum(dividNum);
				   rtu.setPortNum(portNum);
			   }
			   rtu.setType(type);
			   Racks rack = findService.findRack(rackId);
			   rtu.setRack(rack);
			   rtu.setCreate_date(NumConv.currentTime());
			   Serializable id = addService.addRtu(rtu);
			   /**发射端RTU获取发射机光功率值的线程
			    * 读取十次
			    * **/
               class GetRtuPth extends Thread{
            	   private Long rtuId;
            	   public GetRtuPth(long id){
            		   this.rtuId=id;
            	   }
				   public void run(){
					   Rtus rtu=findService.findRtu(rtuId);
					   if(rtu!=null){
						   float meanPVal=0;
						   for(int i=0;i<10;i++){
							   TcpClient tcpConn =new TcpClient(rtu.getRtu_url(),8088,2000);
							   while(!tcpConn.getIsReady());//等待完成
							   byte[] code=TCPComndUtil.comndGetPowerVal();
					   	 	   boolean status=tcpConn.sendData(code);
					    	   if(status){
					        		 byte []rec=tcpConn.recData();//接收数据
					        		 tcpConn.closeConn();//发送后释放连接
					        		 Map<String,Object>reD=TCPComndUtil.anlyzeRecData(rec);
					        		 status=(boolean)reD.get("status");
					        		 if(status){
					        			float pVal=(float)reD.get("pwVal");
					        			pVal=pVal/rtu.getDividNum();
					        			pVal=pVal/rtu.getDividNum();
					        			meanPVal=(float) ((pVal+meanPVal)/2);
					        		 }
					         }
					    	 else{
					    		   tcpConn =new TcpClient(rtu.getRtu_url(),8088,2000);
								   while(!tcpConn.getIsReady());//等待完成
								   status=tcpConn.sendData(code);
						    	   if(status){ 
						    		   byte []rec=tcpConn.recData();//接收数据
					        		   tcpConn.closeConn();//发送后释放连接
					        		   //System.out.println("server response："+NumConv.byteArrayToHex(rec));
					        		   Map<String,Object>reD=TCPComndUtil.anlyzeRecData(rec);
					        		   status=(boolean)reD.get("status");
					        		  if(status){
					        			float pVal=(float)reD.get("pwVal");
					        			pVal=pVal/rtu.getDividNum();
					        			meanPVal=(float) ((pVal+meanPVal)/2.0);
					        		  }
					        	 } 
					    	  }
						   }
						   DecimalFormat format2=new DecimalFormat("#0.00"); 
						   rtu.setPVal(format2.format(meanPVal));
		        		   alterService.alterRtu(rtu);
						}
						
					}
			   }
			   if(id!=null){
				 status=true;  
				 rack.setThing_id((Long)id);
				 rack.setThingName(rtuNameAdd);
				 rack.setThing_type(type);
				 alterService.alterRack(rack);
				 /**采集发射端光功率值**/
				 if(!type.equals("普通rtu")){
					 new GetRtuPth((long) id).start(); 
				 }
				}
			  
			   else{
				   err="数据存储故障";
			   }
		}
	   responseData.put("err",err);
	   responseData.put("status", status);
       JSONArray responseJson=JSONArray.fromObject(responseData);
	   response.setContentType("text/xml");
 	   response.setCharacterEncoding("utf-8");
 	   PrintWriter out = response.getWriter();
 	   out.print(responseJson);			
 	   out.flush();
 	   out.close();
 	} 
 /****************删除RTU
 * @throws IOException ******************/   
   @RequestMapping(value="rtu/delRtu")	
	public  void delRtu (HttpServletRequest request,HttpServletResponse response) throws IOException 
	{
	   long selectRtuId=Long.parseLong(request.getParameter("rtuId"));
	   boolean status=false;
	   String err="";
	   Map<String, Object> responseData =new LinkedHashMap<>();
	   Rtus rtu=findService.findRtu(selectRtuId);
	   if(rtu.getType().contains("普通")){//普通RTU，下发解除指令
		   Map<String,Object> para=new LinkedHashMap<String,Object>();
		   para.put("CM", selectRtuId);
		   para.put("CLP", rtu.getStation().getId());
		   String xml=XmlCodeCreater.cancelRtuMode(para);
		   String rtuUrl="http://"+rtu.getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
		  //向RTU下发指令并解析回复
			  try{
				  String responseFile=NumConv.createVerifyCode(20)+"clearRtuMode.xml";
				  String testDecode=NumConv.createVerifyCode(20)+"testDecode.txt";
				  HttpClientUtil.Post(rtuUrl,xml,responseFile,3500,4000); 
				  XmlDom XmlDom=new XmlDom();
				  responseData = XmlDom.AnalyseRespondse(responseFile,testDecode);
				 /**-------------如果回复码为0,说明成功下发--------------------------------*/
		    	  String StatusCode=(String) responseData.get("StatusCode");
		    	  if(StatusCode.equals("0")){//状态码为0，说明RTU端接收正常
		    		  status=true;
		    	   }
		    	  else{
		    		  status=false;
		    		  err="RTU通信异常。";
		    	  }
			  }catch(IOException | HttpException | DocumentException e){
				  e.printStackTrace();  //输入出错误原因
				  status=false;
	    		  err="RTU通信故障，与RTU建立连接超时，请检查RTU状态是否正常。";
	    	 }
	   }
	   else{//备纤光源RTU 
		   status=true;
	   }
	  
	  if(status){
		   try {
			   Racks rack = findService.findRtu(selectRtuId).getRack();
			   rack.setThing_id(null);
			   rack.setThing_type(null);
			   alterService.alterRack(rack);
			   delService.deleteRtu(selectRtuId);
		  } 
		  catch (Exception e) {
			  e.printStackTrace();
			status=false;
			err="数据库操作异常";
		  }
	    }
		responseData.put("status", status);
		responseData.put("err", err);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out= response.getWriter();
		out.print(responseJson);			
		out.flush();
		out.close();
	}
   /*******************修改Rtu********************/   
   @RequestMapping(value="rtu/modifyRtu")	 //FIXME
	public  void alterRtu (HttpServletRequest request,HttpServletResponse response) throws IOException
	{	
	   ResourceController.printAllRequestPara(request);
	   /*获取前端的数据*/
	   long alterRtuId= Long.parseLong(request.getParameter("alterRtuId"));
	   String alterRtuName=request.getParameter("alterRtuName");
	   String alterRtuDescription=request.getParameter("alterRtuDescription");
	   String newInstallInfo=request.getParameter("installInfo");
	   String rtuType=request.getParameter("rtuType");
	   Map<String,Object> responseData=new LinkedHashMap<String,Object>();
	   boolean status=false;
	   String err="";
	   /*修改数据库*/
	   Rtus rtu=findService.findRtu(alterRtuId);
	   if(rtu.getType().contains("普通")&&(!newInstallInfo.equals(rtu.getInstallInfo()))){//普通RTU,模块和之前不同，下发
		   /**向RTU发送模块模式指令**/
	       String modeType=newInstallInfo;//.contains("0")?(newInstallInfo.split("0")[0]):newInstallInfo;//读取每个模块的类型
	       Map<String, Object> para=new LinkedHashMap<String, Object>();
	       para.put("CM", rtu.getId());
	       para.put("CLP", rtu.getStation().getId());
	       List<Map<String,String>> modules=new ArrayList<Map<String,String>>();
	       for(int i=0; i<modeType.length();i++){
	    	   Map<String,String> moudle=new LinkedHashMap<String, String>();
	    	   moudle.put("KNo", String.valueOf(1+i));
	    	   moudle.put("Type",String.valueOf(modeType.charAt(i)));
	    	   modules.add(moudle);
	       }
	       para.put("modules", modules);
	       String xml=XmlCodeCreater.setRtuMode(para);
	       //System.out.println("文件为："+xml);
	       String rtuUrl="http://"+rtu.getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
	       //向RTU下发指令并解析回复
			  try{
				  String responseFile=NumConv.createVerifyCode(20)+"modifyRtu.xml";
				  String testDecode=NumConv.createVerifyCode(20)+"testDecode.txt";
				  HttpClientUtil.Post(rtuUrl,xml,responseFile,3500,4000);
				  XmlDom XmlDom=new XmlDom();
				  responseData = XmlDom.AnalyseRespondse(responseFile,testDecode);
				 /**-------------如果回复码为0,说明成功下发--------------------------------*/
		    	  String StatusCode=(String) responseData.get("StatusCode");
		    	  if(StatusCode.equals("0")){//状态码为0，说明RTU端接收正常
		    		  status=true;
		    	   }
		    	  /**回复码为15，说明前端下发指定的模块中有物理上不存在的模块**/
		    	  else if(StatusCode.equals("15")){//
		    		  status=false;
		    		  err="您指定新建的模块中包含实际硬件未连接的模块，请核对硬件连接状态后重试。";
		    	   }
		    	  else{
		    		  status=false;
		    		  err="RTU回复异常，请稍后重试，若一直出现该故障，请检查RTU状态是否正常。";
		    	  }
			  }catch(HttpException|DocumentException |IOException e){
				  e.printStackTrace();  //输入出错误原因
				  status=false;
	    		  err="RTU通信故障,与RTU建立连接超时，请检查RTU的状态是否正常。";
	    	 }
	   }
	   else{//切换RTU不设置端口占用，直接新建
		   status=true;
	   }
	   //for test
	   status=true;
	   /**下发成功**/
	   if(status){
		   if(rtuType.equals("普通rtu")){
			    String oldInstallInfo = rtu.getInstallInfo();
			      /**遍历对比RTU的新旧模块，如果模块类型相同则不做处理，或者新建端口**/
				for(int i =0;i<newInstallInfo.length();i++){
					 if(!newInstallInfo.substring(i, i+1).equals(oldInstallInfo.substring(i,i+1))){//和以前的模块类型不一样则进行处理，否则不处理
					      /***重新建立模块下的端口***/
						  String port_type = "";
						  String[] order={"A","B","C","D"};
						  for(int j = 0; j < 8; j++){
								Rtu_ports rtu_port = new Rtu_ports();
								rtu_port.setPort_type(port_type);//设置端口类型
								rtu_port.setPort_order(j+1);//设置端口序号
								String portName="M"+(i+1)+"-"+order[j%4]+(j/4+1);
								rtu_port.setName(portName);
								rtu_port.setPort_type("OTDR_PORT");
								rtu_port.setStatus(false);//设置端口状态
								rtu_port.setRtu(rtu);//设置该端口所属RTU
								rtu_port.setModuleOrder(i+1);
								addService.addRtuPort(rtu_port);
						}
					 }
			   }
			   rtu.setInstallInfo(newInstallInfo);	
			   rtu.setRtu_name(alterRtuName);
			   rtu.setDescription(alterRtuDescription);
			   rtu.setAlter_date(NumConv.currentTime());
			   Subject currentUser = SecurityUtils.getSubject();//获取当前用户
			   String account=currentUser.getPrincipal().toString();//当前用户的账号
			   rtu.setAlter_user(account);
			   status=alterService.alterRtu(rtu);
			   if(!status){
				   err="数据库操作异常";
			   }
			}	   	  
	   }
	   responseData.put("status", status);
	   responseData.put("err", err);
	   response.setContentType("text/xml");
	   response.setCharacterEncoding("utf-8");
	   PrintWriter out=response.getWriter();
	   JSONArray responseJson=JSONArray.fromObject(responseData);
	   //System.out.println("回传数据:"+responseJson);
	   out.println(responseJson); 
	   out.flush();
	   out.close();
	}
   /****************搜索RTU******************/   
   @RequestMapping(value="ResourceSearchRTUAll")	//FIXME
	public  void ResourceSearchRTU (HttpServletRequest request,HttpServletResponse response) throws IOException
	{
        String RtuName=request.getParameter("RtuNAME");
		LinkedHashMap<String , Object> para= new LinkedHashMap<String,Object>();
		para.put("rtu_name", RtuName);
		List<Rtus> rtus = findService.findRtusByMulti(para);
		List<Map<String,Object>> Rtus=new ArrayList<Map<String,Object>>();
		for(int i = 0;i<rtus.size();i++)
 	  	 {
 	  		 Map<String , Object> map = new LinkedHashMap<String , Object>();
 	  		 map.put("rtuId", rtus.get(i).getId());
 	  		 map.put("rtuName",rtus.get(i).getRtu_name());
 	  		 Long alarmId=rtus.get(i).getAlarm_wayId();
 	  		 String alarm="本地可视";
 	  		 if(alarmId!=null){
 	  			 alarm=findService.findAlarmWayById(alarmId).getAlarm_name();
 	  		 }
 	  		 map.put("tackOrder", rtus.get(i).getRack().getRack_order());
 	  		 map.put("alarmWay",alarm);
 	  		 map.put("rtuUrl", rtus.get(i).getRtu_url());
 	  		 map.put("stationId", rtus.get(i).getStation().getId());
 	  		 map.put("stationName", rtus.get(i).getStation().getStation_name());
 	  		 map.put("description", rtus.get(i).getDescription());
 	  		 map.put("createDate", rtus.get(i).getCreate_date());
 	  		 map.put("createUser", rtus.get(i).getCreate_user());
 	  		 map.put("alterDate", rtus.get(i).getAlter_date());
 	  		 map.put("alterUser", rtus.get(i).getAlter_user());
 	  		 map.put("installInfo", rtus.get(i).getInstallInfo());
 	  		 map.put("type", rtus.get(i).getType());
 	  		 Rtus.add(map);
 	  	 }
  		JSONArray rtusJson=JSONArray.fromObject(Rtus);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(rtusJson);	
		out.flush();
		out.close();
	}

 


/************点击局站罗列机柜信息************/
@RequestMapping(value="ResourceListCabinete")	
public  void ResourceListCabinete (HttpServletRequest request,HttpServletResponse response) throws IOException
{ 
	/*获取前端的局站ID*/
	long stationId = (long) Integer.parseInt(request.getParameter("stationId"));
	List<Cabinets> cabinets = findService.findStation(stationId).getCabinets();	
	List<CabinetJson> cabinetJsons = new ArrayList<CabinetJson >(cabinets.size());
		for(int i = 0 ; i < cabinets.size(); i++){  
			CabinetJson aaa = new CabinetJson();  
			cabinetJsons.add(aaa);  
        }  
		for(int i=0;i<cabinetJsons.size();i++)
		{
			cabinetJsons.get(i).setAlter_date(cabinets.get(i).getAlter_date());
			cabinetJsons.get(i).setCabinet_name(cabinets.get(i).getCabinet_name());
			cabinetJsons.get(i).setCreate_date(cabinets.get(i).getCreate_date());
			cabinetJsons.get(i).setDescription(cabinets.get(i).getDescription());
			cabinetJsons.get(i).setId(cabinets.get(i).getId());
			cabinetJsons.get(i).setRack_number(cabinets.get(i).getRack_number());
			cabinetJsons.get(i).setStationId(cabinets.get(i).getStation().getId());
		}
		JSONArray cabinetsJsons=JSONArray.fromObject(cabinetJsons);	
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(cabinetsJsons.toString());			
		out.flush();
		out.close();
}

/************增加机柜************/	
@RequestMapping(value="cabinet/addCabinet")	
public  void addCabinet (HttpServletRequest request,HttpServletResponse response) throws IOException
{ 
	/*获取前端的局站ID*/
		Long stationId = Long.parseLong(request.getParameter("stationId"));
		String cabinetName = request.getParameter("cabinetName");
		String description = request.getParameter("cabinetDescription");
		int rackNumber = Integer.parseInt(request.getParameter("rackNum"));
		
		/*开始存入数据库*/
		Cabinets cabinet = new Cabinets();
		cabinet.setCabinet_name(cabinetName);
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time = format.format(date);
		cabinet.setCreate_date(time);
		cabinet.setDescription(description);
		cabinet.setRack_number(rackNumber);
		Stations station = findService.findStation(stationId);
		cabinet.setStation(station);
		Serializable cabinetId=addService.addCabinet(cabinet);
		boolean status=false;
		if(cabinetId!=null)
		{
			status=true;
		}
		LinkedHashMap<String,Object> responseData=new LinkedHashMap<String,Object>();
		responseData.put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();   
}


/***********修改机柜************/	
@RequestMapping(value="cabinet/modifyCabinet")	
public  void modifyCabinet (HttpServletRequest request,HttpServletResponse response) throws IOException
{ 
	/*接收前端传来的数据*/
		long cabinetId=(long)Integer.parseInt(request.getParameter("alterCabinetId"));
		String cabinetName = request.getParameter("alterCabinetName");
		String cabinetDescription = request.getParameter("alterCabinetDescription");
		Cabinets cabinet = findService.findCabinet(cabinetId);
		cabinet.setCabinet_name(cabinetName);
		cabinet.setDescription(cabinetDescription);
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time = format.format(date);
		cabinet.setAlter_date(time);
		boolean status =alterService.alterCabinet(cabinet);
		LinkedHashMap<String,Object> responseData=new LinkedHashMap<String,Object>();
		responseData.put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();   
}

/***********删除机柜************/	
@RequestMapping(value="cabinet/delCabinet")	
public  void ResourceDeleteCabinetConditionB (HttpServletRequest request,HttpServletResponse response) throws IOException
{ 
	Long cabinetId = Long.parseLong(request.getParameter("cabinetId"));
	Long stationId=findService.findCabinet(cabinetId).getStation().getId();
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
			 String rtuUrl="http://"+rtu.getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
			 //向RTU下发指令并解析回复
			 try{
				    String responseFile=NumConv.createVerifyCode(20)+"clearRtuMode.xml";
					String testDecode=NumConv.createVerifyCode(20)+"testDecode.txt";
					HttpClientUtil.Post(rtuUrl,xml,responseFile,2000,3000); 
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
						 // e.printStackTrace();  //输入出错误原因
						  status=false;
			    }
		}
	}
	Cabinets cabniet=findService.findCabinet(cabinetId);
	if(cabniet!=null){
		List<Racks> racks=cabniet.getRacks();
		List<DelRtu> delRtus=new ArrayList<DelRtu>();
		for(Racks rack :racks){
			if(rack.getThing_type()!=null){
				if(rack.getThing_type().equals("普通rtu")){//如果机架上放置的是普通RTU，下发清除RTU模式指令
					Rtus rtu=findService.findRtu(rack.getThing_id());
					DelRtu delRtu=new DelRtu(rtu);
					delRtus.add(delRtu);
					delRtu.start();
				}
			}
		}
		for(DelRtu delRtu:delRtus){
			try {
				delRtu.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(DelRtu delRtu:delRtus){
			status&=delRtu.getStatus();
		}
	}
	if(!status){
		err="当前局站下存在RTU，在清除RTU模式时与RTU通信故障，请检查RTU状态是否正常。";
	}
	else{
		try {
			delService.deleteCabinet(cabinetId);
		} 
		catch (Exception e) {
			e.printStackTrace();
			status=false;
			err="数据库操作异常。";
		}
	}
	LinkedHashMap<String,Object> responseData=new LinkedHashMap<String,Object>();
	responseData.put("status", status);
	responseData.put("err", err);
	responseData.put("stationId", stationId);
  	JSONArray responseJson=JSONArray.fromObject(responseData);
  	response.setContentType("text/xml");
	response.setCharacterEncoding("utf-8");
  	PrintWriter out=response.getWriter();
  	out.println(responseJson);
  	out.flush();
  	out.close();  
	
}

    /************点击局站罗列机架信息************/
 	@RequestMapping(value="ResourceListRack")	
 	public  void ResourceListrack (HttpServletRequest request,HttpServletResponse response) throws IOException
 	{ 
 		/**接收前端传来的数据*/
 		long stationId =Long.parseLong(request.getParameter("stationId"));
 		/**搜索数据库*/
 		List<Racks> racks = findService.findRacksByStationId(stationId);
 		@SuppressWarnings("unchecked")
		Map<String,Object>[] rack=new LinkedHashMap[racks.size()];
 		for(int i=0;i<racks.size();i++){
 			rack[i]=new LinkedHashMap<String,Object>();
 			rack[i].put("id",racks.get(i).getId());
 			rack[i].put("cabineteId",racks.get(i).getCabinets().getId());
 			rack[i].put("order",racks.get(i).getRack_order());
 			rack[i].put("cabineteName",racks.get(i).getCabinets().getCabinet_name());
 			if(racks.get(i).getThing_type()==null){ 
 				rack[i].put("isUsed","未使用");
 				rack[i].put("thingType","");
 				rack[i].put("thingName","");
 			}
 			else{
 				String type=racks.get(i).getThing_type();
 				long id=racks.get(i).getThing_id();
 				String name="";
 				switch(type){
 					case"普通rtu":
 					case"备纤光源rtu":
 						name=findService.findRtu(id).getRtu_name();
 						break;
 					case"配线架":
 						name=findService.findDistributingFrame(id).getFrame_name();
 						break;
 					default:
 						break;
 				}
 				rack[i].put("isUsed","已使用");
 				rack[i].put("thingType",racks.get(i).getThing_type());
 				rack[i].put("thingName",name);
 			}
 		}
 		JSONArray racksJsons=JSONArray.fromObject(rack);	
 		response.setContentType("text/xml");
 		response.setCharacterEncoding("utf-8");
 		PrintWriter out = response.getWriter();
 		out.print(racksJsons.toString());			
 		out.flush();
 		out.close();
 	}
      /************点击局站更新机柜的下拉框************/	
 	 	@RequestMapping(value="Resourceupdatecabinete")	
 	 	public  void Resourceupdatecabinete (HttpServletRequest request,HttpServletResponse response) throws IOException
 	 	{ 
 	 		/*接收前端传来的数据*/
 	 		long stationId =Long.parseLong(request.getParameter("stationId"));
 	 		/*搜索数据库*/
 	 		List<Cabinets> cabinetes = findService.findStation(stationId).getCabinets();
 	 		String[] cabinetesName = new String[cabinetes.size()];
 	 		for(int i=0;i<cabinetes.size();i++){
 	 			cabinetesName[i] = cabinetes.get(i).getCabinet_name();
 	 		}
 	         	
 	 		Map<String, Object> map = new LinkedHashMap<>();
 	 		map.put("cabinetesName", cabinetesName);
 	 		JSONArray data=JSONArray.fromObject(map);
 	 		//System.out.println(data);
 	 		response.setContentType("text/xml");
 	 		response.setCharacterEncoding("utf-8");
 	 		PrintWriter out = response.getWriter();
 	 		out.print(data.toString());			
 	 		out.flush();
 	 		out.close();
 	 	}
 	
    /************罗列配线架信息************/
 	@RequestMapping(value="ResourceListDistributeFrame")	
 	public  void ResourceListDistributeFrame (HttpServletRequest request,HttpServletResponse response) throws IOException
 	{ 		
 		/*****新建list赋值并且转json******/
 		long stationId=(long)Integer.parseInt(request.getParameter("stationId"));
 		Stations station=findService.findStation(stationId); 		
 		List<Distributing_frames> frames=station.getDistributing_frames(); 	
 		List<distributeFrameJson> frameJson=new ArrayList<distributeFrameJson >(frames.size());
 		for(int i = 0 ; i < frames.size(); i++){  
 			distributeFrameJson aaa = new distributeFrameJson();  
 			frameJson.add(aaa);  
         }  
 		for(int i=0;i<frameJson.size();i++)
 		{
 			frameJson.get(i).setFrame_id(frames.get(i).getId());
 			frameJson.get(i).setFrame_name(frames.get(i).getFrame_name());
 			frameJson.get(i).setPort_number(frames.get(i).getPort_number());
 			frameJson.get(i).setDescription(frames.get(i).getDescription());
 			frameJson.get(i).setStationId(frames.get(i).getStation().getId());
 			//frameJson.get(i).setCabinetId(findService.findCabinetByFrameId(frames.get(i).getId()).getId());  //配线架所属机柜
 			frameJson.get(i).setCabinetId(frames.get(i).getRack().getCabinets().getId());
 			frameJson.get(i).setRackOrder(frames.get(i).getRack().getRack_order());
 			frameJson.get(i).setCreate_time(frames.get(i).getCreate_date());
 		}
 		JSONArray framesJson=JSONArray.fromObject(frameJson);	
 		response.setContentType("text/xml");
 		response.setCharacterEncoding("utf-8");
 		PrintWriter out = response.getWriter();
 		out.print(framesJson.toString());			
 		out.flush();
 		out.close();		
 	}
 	  /***************增加配线架 提供机柜ID和名字***********************/   
	  @RequestMapping(value="ResourceAddFrameUnderStationListCabinetes")		
	   	public  void ResourceAddFrameUnderStationListCabinetes (HttpServletRequest request,HttpServletResponse response) throws IOException
	   	{
		    /*获取局站的ID*/
		    long stationId =Long.parseLong(request.getParameter("stationId"));
	  		/*找到该局站下面的所有的机柜*/
	  	    List<Cabinets> cabinets = findService.findStation(stationId).getCabinets();
	  	    
	  	    String[] cabinetName = new String[cabinets.size()];
	  	    String[] cabinetId = new String[cabinets.size()];
	  	    
	  	    for(int i=0;i<cabinets.size();i++){
	  	    	cabinetId[i] = String.valueOf(cabinets.get(i).getId());
	  	    	cabinetName[i] = cabinets.get(i).getCabinet_name();
	  	    }
		    
	        Map<String,Object> map = new LinkedHashMap<>();
	        map.put("cabinetName", cabinetName);
	        map.put("cabinetId", cabinetId);
	        
	        JSONArray responseData=JSONArray.fromObject(map);
	   		response.setContentType("text/xml");
	   		response.setCharacterEncoding("utf-8");
	   		PrintWriter out = response.getWriter();
	   		out.print(responseData);			
	   		out.flush();
	   		out.close();
	   	}
	  
	  /***************增加配线架 提供机架ID和序号***********************/   
	  @RequestMapping(value="ResourceAddFrameUnderStationListracks")		
	   	public  void ResourceAddFrameUnderStationListracks (HttpServletRequest request,HttpServletResponse response) throws IOException
	   	{
		    /*获取机柜的ID*/
		    long cabineteId =Long.parseLong(request.getParameter("cabineteId"));
	  		/*找到该机柜下面的所有的机架*/
	  	    List<Racks> racks = findService.findCabinet(cabineteId).getRacks();
	  	    String[] racksOrder = new String[racks.size()];
	  	    String[] rackstId = new String[racks.size()];
	  	    int j=0;
	  	    for(int i=0;i<racks.size();i++){
	  	    	if(racks.get(i).getThing_type()==null) 
	  	    	{
	  	  	    	rackstId[j] = String.valueOf(racks.get(i).getId());
	  	  	    	racksOrder[j] = String.valueOf(racks.get(i).getRack_order());
	  	  	    	j++;
	  	    	}
	  	    }
	  	    
	  	    String[] racksOrder1 = new String[j];
		    String[] rackstId1 = new String[j];
		    for(int m=0;m<j;m++){
		    	racksOrder1[m]=racksOrder[m];
		    	rackstId1[m] = rackstId[m];
		    }
		    
	        Map<String,Object> map = new LinkedHashMap<>();
	        map.put("racksId", rackstId1);
	        map.put("racksOrder", racksOrder1);
	        JSONArray responseData=JSONArray.fromObject(map);
	   		response.setContentType("text/xml");
	   		response.setCharacterEncoding("utf-8");
	   		PrintWriter out = response.getWriter();
	   		out.print(responseData);			
	   		out.flush();
	   		out.close();
	   	}
/***************增加配线架***********************/   
  @RequestMapping(value="frame/addFrame")	
	public  void ResourceAddFrameTop (HttpServletRequest request,HttpServletResponse response) throws IOException{
	   /*获取从前端传来的数据*/
	   String frameName=request.getParameter("frameName");
	   int framePortNum=Integer.parseInt(request.getParameter("framePortNum"));
	   String frameDescription = request.getParameter("frameDescription");
	   Long rackId = Long.parseLong(request.getParameter("rackId")); 
	   
		/*将数据存入数据库*/
        Distributing_frames frame = new Distributing_frames();
        frame.setFrame_name(frameName);
        frame.setPort_number(framePortNum);
        frame.setDescription(frameDescription);
	    Date date = new Date();
	    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    String time = format.format(date);
	    frame.setCreate_date(time);
	    Racks rack = findService.findRack(rackId);
	    rack.setThing_type("配线架");
	    rack.setThingName(frameName);
        frame.setRack(rack);
        frame.setStation(rack.getCabinets().getStation());
		long frameId = (long)addService.addDistributingFrame(frame); 
	    rack.setThing_id(frameId);
	    Long stationId=rack.getCabinets().getStation().getId();
	    boolean status=alterService.alterRack(rack);
	    LinkedHashMap<String,Object> responseData=new LinkedHashMap<String,Object>();
		responseData.put("status", status);
		responseData.put("stationId", stationId);
  		JSONArray responseJson=JSONArray.fromObject(responseData);
  		//System.out.println("JsonData:"+responseJson);
  		PrintWriter out=response.getWriter();
  		out.println(responseJson);
  		out.flush();
  		out.close();  
	  
	}
    
    /*******************修改配线架********************/   
    @RequestMapping(value="frame/modifyFrame")	
 	public  void modifyFrame (HttpServletRequest request,HttpServletResponse response) throws IOException
 	{	
 	   /*获取前端的数据*/
 	   long alterFrameId= Long.parseLong(request.getParameter("frameIdAlter"));
 	   String alterFrameName=request.getParameter("frameNameAlter");
 	   String alterFrameDescription=request.getParameter("frameDescriptionAlter");
 	   boolean status = true; 
 	   Long stationId=null;
	   try {
			/*修改数据库*/
		 	   Distributing_frames frames = findService.findDistributingFrame(alterFrameId);
		 	   stationId=frames.getRack().getCabinets().getStation().getId();
		 	   Date date = new Date();
		 	   DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		 	   String time = format.format(date);
		 	   frames.setAlter_date(time);
		 	   frames.setDescription(alterFrameDescription);
		 	   frames.setFrame_name(alterFrameName);
		 	   alterService.alterDistributingFrame(frames);
		} 
		catch (Exception e) {
			status=false;
		}
		finally {
			
			LinkedHashMap<String,Object> responseData=new LinkedHashMap<String,Object>();
			responseData.put("status", status);
			responseData.put("stationId", stationId);
	  		JSONArray responseJson=JSONArray.fromObject(responseData);
	  		//System.out.println("JsonData:"+responseJson);
	  		PrintWriter out=response.getWriter();
	  		out.println(responseJson);
	  		out.flush();
	  		out.close();  
		}
 	}
    /*******************删除配线架********************/   
    @RequestMapping(value="frame/delFrame")	
 	public  void deleteFrame (HttpServletRequest request,HttpServletResponse response) throws IOException
 	{	
    	/*获取前端的数据*/
    	long frameId = Long.parseLong(request.getParameter("frameId"));
    	List<Frame_ports> frame_ports = findService.findFramePortsAllByFrameId(frameId);
    	/**如果配线架上挂载了光路，应该先向RTU下发解除端口占用的指令**/
    	List<Integer> ports=new ArrayList<Integer>();//占用端口的端口序号
    	List<Long> CM=new ArrayList<Long>();//占用端口的RTU ID
    	List<Map<String,Object>>CMAndPorts =new ArrayList<Map<String,Object>>();//用于存放RTU Id和其下的端口
    	for(Frame_ports frame_port : frame_ports)
		{
			if(frame_port.getStatus())//检测到该端口被占用
			{
				String connected_type = frame_port.getConnection_type();
				if(connected_type.equals("光路跳纤")){
					Jumper_routes jumper_route=findService.findJumperRoute(frame_port.getConnection_id());
					if(jumper_route.getRoute_id()!=null){//存在光路
						Routes route=findService.findRoute(jumper_route.getRoute_id());
						ports.add(route.getRtu_port_order());//添加端口序号
						CM.add(route.getRtu_id());//添加CM
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
				Racks rack =findService.findDistributingFrame(frameId).getRack();
		    	rack.setThing_id(null);
		    	rack.setThing_type(null);
		    	alterService.alterRack(rack);
		    	delService.deleteDistributingFrame(frameId);
			} 
			catch (Exception e) {
				status = false;
				err="数据库操作异常";
			}
		}
		else{
			err="当前配线架上的端口挂载有光路，在向RTU下发解除端口状态时与RTU通信异常，请检查RTU状态是否正常。";
		}
    	/**向前端回复数据**/
		LinkedHashMap<String,Object> responseData=new LinkedHashMap<String,Object>();
		responseData.put("err", err);
		responseData.put("status", status);
	  	JSONArray responseJson=JSONArray.fromObject(responseData);
	  	//System.out.println("JsonData:"+responseJson);
	  	PrintWriter out=response.getWriter();
	  	out.println(responseJson);
	  	out.flush();
	  	out.close();  
	    
 	}
    /*******************搜索配线架********************/   
    @RequestMapping(value="ResourceSearchFrame")	
 	public  void ResourceSearchFrame (HttpServletRequest request,HttpServletResponse response) throws IOException
 	{
    	/*接收前端传来的参数*/
    	String frameName = request.getParameter("frameNAME");
    	Map<String , Object> map =new LinkedHashMap<>();
    	map.put("frame_name", frameName);
    	List<Distributing_frames> frames = findService .findDistributingFramesByMulti(map);
    	List<distributeFrameJson> frameJson=new ArrayList<distributeFrameJson >(frames.size());
 		for(int i = 0 ; i < frames.size(); i++){  
 			distributeFrameJson aaa = new distributeFrameJson();  
 			frameJson.add(aaa);  
         }  
 		for(int i=0;i<frameJson.size();i++)
 		{
 			frameJson.get(i).setFrame_id(frames.get(i).getId());
 			frameJson.get(i).setFrame_name(frames.get(i).getFrame_name());
 			frameJson.get(i).setPort_number(frames.get(i).getPort_number());
 			frameJson.get(i).setDescription(frames.get(i).getDescription());
 			frameJson.get(i).setStationId(frames.get(i).getStation().getId());
 			frameJson.get(i).setCabinetId(frames.get(i).getRack().getCabinets().getId());
 			frameJson.get(i).setRackOrder(frames.get(i).getRack().getRack_order());
 			frameJson.get(i).setCreate_time(frames.get(i).getCreate_date());
 		}
 		JSONArray framesJson=JSONArray.fromObject(frameJson);	
 		response.setContentType("text/xml");
 		response.setCharacterEncoding("utf-8");
 		PrintWriter out = response.getWriter();
 		out.print(framesJson.toString());			
 		out.flush();
 		out.close();	
 	}
    
 
    /**----------------------从光路配对表中获得所有的匹配信息-------------------------------*/
    @SuppressWarnings({ "rawtypes", "unchecked" })
  @RequestMapping("getAllRouteMatchByRtuId")
     public void getMixRouteByRtuId(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	Long rtuId=Long.parseLong(request.getParameter("rtuId"));
    	List<Protect_groups> protect_groups = findService.findProtectGroupsByRtuId(rtuId);
    	LinkedHashMap[] responseData=new LinkedHashMap[protect_groups.size()+1];
    	responseData[0]=new LinkedHashMap<Object, Object>(); 
    	boolean status=false;
    	if(protect_groups.size()>0){
    		status=true;
    		for(int i = 0;i<protect_groups.size();i++){
        		responseData[i+1] = new LinkedHashMap<Object, Object>();
        		responseData[i+1].put("id", protect_groups.get(i).getId());
        		responseData[i+1].put("upOnName",protect_groups.get(i).getRouteUpOnName());
        		responseData[i+1].put("upOffName",protect_groups.get(i).getRouteUpOffName());
        		responseData[i+1].put("downOnName",protect_groups.get(i).getRouteDownOnName());
        		responseData[i+1].put("downOffName",protect_groups.get(i).getRouteDownOffName());
        		responseData[i+1].put("stationAName", findService.findRtu(protect_groups.get(i).getRtuMasterId()).getStation().getStation_name());
        		responseData[i+1].put("masterRtuName", protect_groups.get(i).getRtuMasterName());
        		responseData[i+1].put("stationZName", findService.findRtu(protect_groups.get(i).getRtuSlaveId()).getStation().getStation_name());
        		responseData[i+1].put("slaveRtuName", protect_groups.get(i).getRtuSlaveName());
        		responseData[i+1].put("createUser", protect_groups.get(i).getCreateUser());
        		responseData[i+1].put("createTime",protect_groups.get(i).getCreateDate());
        	}
    	}
    	
    	responseData[0].put("status", status);
  		JSONArray responseJson=JSONArray.fromObject(responseData);
  		//System.out.println("JsonData:"+responseJson);
  		PrintWriter out=response.getWriter();
  		out.println(responseJson);
  		out.flush();
  		out.close();  
    }
    /**通过RTU id获取光路（连接到混合模块的光路）(用来显示在pickLIst的左侧)
     * 构成保护组的前提是必须具有双向的配对组
     * 首先遍历每条光路，查找是否有位于同一个模块的单向配对组，然后遍历单向配对组，查找是否有双向保护组
     * 保护-主：3
     * 保护-从：5
     *规定：模块的连接关系：
     *	1. A-B为一个收发组  端口序号为1-2（针对1-8编号而言）
     *  2. C-D为一个收发组  端口序号为3-4（针对1-8编号而言）
     *  3. A1-A2,B1-B2,C1-C2,D1-D2构成一个单向的保护组   端口号为1-5；2-6；3-7；4-8(对于1-8的编号而言)
     * */
     @RequestMapping("switch/getPrepareGroup")
     public void getPrepareGroup(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	    long id = Long.parseLong(request.getParameter("id"));
    	    int step=-1;
    	    int maxStep=-1;
    	    boolean status=true;
    	    List<Routes> routes=findService.findRoutesByRtuId(id);
    	    //System.out.println("route count:"+routes.size());
		    Map<String, Object> responseData=new LinkedHashMap<String, Object>();
		    /**记录所有的单向保护组的信息
		     * 然后通过单向配对组来查找是否有双向保护组
		     * 单向配对组的条件：位于保护模块上，A端模块类型为保护-住，模块号3；Z端为保护-从，模块类型为5
		     * 端口号为1-5；2-6；3-7；4-8(对于1-8的编号而言)，两条光路为一备一在
		     * 
		     * **/
		    /**配对组信息**/
		    List<Map<String, Object>> matchGroups=new ArrayList<Map<String, Object>>();
		    /**保护组信息**/
		    Map<String,Object> protectGroups=new LinkedHashMap<String,Object>();
		  	int num = 0;
		  	if(routes!=null){
		  	  for(int i = 0;i<routes.size();i++){  
		  		    step=0;//说明有光路
		  		    maxStep=(step>maxStep)?step:maxStep;
	      	    	Rtus rtu = findService.findRtu(routes.get(i).getRtu_id());
		    		int portOrderA = routes.get(i).getRtu_port_order();
		    		/**潜在光路必须是未处于保护状态而且位于混合模块**/
		    		String moudleType=rtu.getInstallInfo().substring(routes.get(i).getRtu_model_order()-1, routes.get(i).getRtu_model_order());
		    		/**A端为混合-主模块**/
	      	    	if((!routes.get(i).getIsProtect())&&moudleType.equals("3")) {
	      	    		for(int j =i+1;j<routes.size();j++){ //依次和其他光路进行比对
	      	    			if(!routes.get(j).getIsProtect()&&routes.get(i).getRtu_model_order()==routes.get(j).getRtu_model_order()){ //判断这两个光路是不是在一个模块
	          	    			step=1;//能到第一步，在同一个模块
	          	    		    maxStep=Math.max(step, maxStep);
	          	    			int portOrderB = routes.get(j).getRtu_port_order();
	          	    		    /**1-5,2-6,3-7,4-8形成一组，端口号差4**/
	          	    			if(Math.abs(portOrderA-portOrderB)==4){
	          	    				//表明这两个光路是配对关系，且没有设置为保护状态
	          	    				step=2;//能到第二步，位于配对位置
	          	    				maxStep=Math.max(step, maxStep);
	          	    				//判断这两个光路有没有连接切换跳纤和切换RTU
	          	    				if(routes.get(i).getSwitchRtuOrder()!=null&&routes.get(j).getSwitchRtuOrder()!=null){
	          	    						step=3;//能到第三步，具有切换跳纤和切换RTU
	          	    						maxStep=Math.max(step, maxStep);
	          	    						/**读取切换端口所在模块的序号**/
	          	    						int switchMoudleA=(routes.get(i).getSwitchRtuOrder()-1)/8+1;
	          	    						int switchMoudleB=(routes.get(j).getSwitchRtuOrder()-1)/8+1;
	          	    						/**读取切换rtuId**/
	          	    						long switchRtuA=routes.get(i).getSwitchRtuId();
	          	    						long switchRtuB=routes.get(j).getSwitchRtuId();
	          	    						/**两条光路在同一个切换RTU的同一个保护模块**/
	          	    						if(switchRtuA==switchRtuB&&switchMoudleA==switchMoudleB){
	          	    							step=4;//能到第四步，在同一个保护-从模块
	          	    							maxStep=Math.max(step, maxStep);
		          	    						 /**
		          	    						  * 判断是否位于保护-从的配对位置
		          	    						  * A1-A2 B1-B2  C1-C2 D1-D2
		          	    						  * 1-5,2-6,3-7,4-8形成一组，端口号差4**/
		        	          	    			if(Math.abs(routes.get(i).getSwitchRtuOrder()-routes.get(j).getSwitchRtuOrder())==4){
		        	          	    				step=5;//能到第五步，位于保护-从模块的配对位置
		        	          	    				maxStep=Math.max(step, maxStep);
			          	    						if(routes.get(i).getIsUplink()==routes.get(j).getIsUplink()){//配对光路的链路方向必须相同
			          	    							step=6;//能到第六步，链路方向相同
			          	    							maxStep=Math.max(step, maxStep);
			          	    							if(routes.get(i).getIs_online()!=routes.get(j).getIs_online()){//配对的光路类型必须为一个在线一个备纤
			          	    								step=7;//能到第七步，存在配对组
			          	    								maxStep=Math.max(step, maxStep);
					          	    						Routes routeOnline=routes.get(i);
					          	    						Routes routeOffline=routes.get(j);
					          	    						/**将在线纤的信息放在前**/
					          	    						if(!routeOnline.getIs_online()){
					          	    							routeOnline=routes.get(j);
					          	    							routeOffline=routes.get(i);
					          	    						}
				          	    							Map<String,Object> groupPara=new LinkedHashMap<String, Object>();	  
				          	    							groupPara.put("id", routeOnline.getId()+"-"+routeOffline.getId());
				          	    							int[]rtuOrder={routeOnline.getRtu_port_order(),routeOffline.getRtu_port_order()};
				          	    							groupPara.put("name",routeOnline.getRoute_name()+"—"+routeOffline.getRoute_name());
				          	    							groupPara.put("rtuOrder",rtuOrder);
				          	    							groupPara.put("switchRtuId", routeOnline.getSwitchRtuId()+"—"+routeOffline.getSwitchRtuId());
				          	    							int[]swRtuOrder={routeOnline.getSwitchRtuOrder(),routeOffline.getSwitchRtuOrder()};
				          	    							groupPara.put("switchRtuOrder",swRtuOrder);				          	    							
				          	    							groupPara.put("isUplink",routeOnline.getIsUplink());
				          	    							matchGroups.add(groupPara);//记录配对组信息
				          	    				   	  	}
				                     	      	  	}
		        	          	    			}
	          	    						}
	          	    				  }
	          	    			}
	          	    		}
	          	    	}
	      	    	}
	      	    }
		  	}
		  	/***读取配对组，查找保护组
		  	 * 一个收发配对组构成一个保护组
		  	 * **/
		  	if(!matchGroups.isEmpty()){
		  		for(int i=0;i<matchGroups.size();i++){
		  			for(int j=i+1;j<matchGroups.size();j++){
		  				/***保护组必须为一个收发组**/
		  				if((boolean)matchGroups.get(i).get("isUplink")!=(boolean)matchGroups.get(j).get("isUplink")){
		  					step=8;//能到第八步，存在上下行双向的配对组
		  					maxStep=Math.max(step, maxStep);
		  					/**保护-主端口序号**/
		  					int[] portA=(int[])matchGroups.get(i).get("rtuOrder");
		  					int[] portB=(int[])matchGroups.get(j).get("rtuOrder");
		  					/**保护-主模块序号**/
		  					int modOrderA=(portA[0]-1)/8+1;
		  					int modOrderB=(portB[0]-1)/8+1;
		  					String swA=(String)matchGroups.get(i).get("switchRtuId");
		  					int[] swPortA=(int[])matchGroups.get(i).get("switchRtuOrder");
		  					int[] swPortB=(int[])matchGroups.get(j).get("switchRtuOrder");
		  					/**保护-从模块序号**/
		  					int swModOrderA=(swPortA[0]-1)/8+1;
		  					int swModOrderB=(swPortB[0]-1)/8+1;
		  					/**同一个保护-主，保护-从模块**/
		  					if(modOrderA==modOrderB&&swA.equals((String)matchGroups.get(j).get("switchRtuId"))&&swModOrderA==swModOrderB){
		  						/**同向的两条光路端口为A1-B1或C1-D1或A2-B2或C2-D2即为端口号为1-2,3-4,5-6,7-8**/
		  						int smallOrder=(portA[0]<portB[0])?portA[0]:portB[0];
		  						int bigOrder=(portA[0]>portB[0])?portA[0]:portB[0];
		  						/**满足收发连接关系**/
		  						if(smallOrder%2==1&&(bigOrder==smallOrder+1)){
		  							Map<String,Object> routePara=new LinkedHashMap<String, Object>();
		  							int uplinkIndex=i;
		  							int downlinkIndex=j;
		  							if((boolean)matchGroups.get(j).get("isUplink")){
		  								uplinkIndex=j;
		  								downlinkIndex=i;
		  							}
		  							routePara.put("id", matchGroups.get(uplinkIndex).get("id")+","+ matchGroups.get(downlinkIndex).get("id"));
		  							String matchInfo="上行:("+matchGroups.get(uplinkIndex).get("name")+")"+
		  				   	  						";下行:("+matchGroups.get(downlinkIndex).get("name")+")";
		  				   	  	    routePara.put("text", matchInfo);
		  				   	  		protectGroups.put(String.valueOf(num), routePara);
		  				   	  		num++;
		  				   	  		  
		  						}
		  					}
		  				}
		  			}
		  			
		  		}
		  	}
		  	if(protectGroups.isEmpty()){//没有保护组
      	    	String err="无可用保护组。";
      	    	status=false;
      	    	switch(maxStep){
      	    	    case -1://不存在光路
      	    	    	err+="当前RTU下不存在可用光路,请先设置光路。";
      	    	    	break;
      	        	case 0://有光路，但不存在在同一个模块上的光路
      	        		err+="当前RTU下不存在位于同一保护模块的光路,请建立满足条件的光路后重试。";
      	        		break;
      	        	case 1://存在位于同一保护模块的光路，但是未处于保护位置
      	        		err+="当前RTU上存在位于保护模块的两条光路，但两条光路未处于保护-主模块的保护配对位置，请核对连接关系后重试。";
      	        		break;
      	        	case 2://
      	        		err+="当前RTU上存在位于保护-主模块配对位置的两条光路，但光路未连接保护-从模块，请先通过切换跳纤连接可用的保护模块-从模块后重试。";
      	        		break;
      	        	case 3://具有连接切换跳纤的配对光路，但是两条光路不在同一个保护-从模块
      	        		err+="存在位于保护-主模块配对位置且连接保护-从模块的两条光路，但它们不在同一个保护-从模块上，请核对连接关系后重试。";
      	        		break;
      	        	case 4://从模块不在配对位置上
      	        		err+="存在位于保护-主模块配对位置且连接保护-从模块的两条光路，但是它们未处于保护-从模块的保护配对位置，请核对连接关系后重试。";
      	        		break;
      	        	case 5://具有连接切换跳纤的配对光路，但是配对光路链路方向不同
      	        		err+="存在位于保护-主模块和保护-从模块配对位置的两条光路，但其链路方向不同。配对光路链路方向必须相同，请修改光路状态后重试。";
      	        		break;
      	        	case 6://具有连接切换跳纤的配对光路，但是配对光路的类型相同
      	        		err+="存在位于保护-主模块和保护-从模块配对位置的两条光路，但其光路类型相同。配对光路必须满足一条为在线纤另一条为备纤，请修改光路状态后重试。";
      	        		break;
      	        	case 7://只存在单向配对组
      	        		err+="只存在上行或下行链路的配对光路，一个保护组必须具有上下行双向配对组，请配置完成后重试。";
      	        		break;
      	        	case 8://有双向配对组保护组，但是不在同一个保护配对位置
      	        		err+="存在上行和下行方向的配对组，但两个链路方向的配对组光路未处于匹配的保护位置，请配置完成后重试。";
      	        		break;
      	        	default:
      	        		break;
      	    	}
      	    	responseData.put("err", err);
      	    }
      	    else{//存在可用配对组
      	    	 status=true;
      	    	 responseData.put("group", protectGroups);
      	    }
      	    responseData.put("status", status);
            JSONArray responseJson=JSONArray.fromObject(responseData);
      		PrintWriter out=response.getWriter();
      		//System.out.println("回传："+responseJson);
      		out.println(responseJson);
      		out.flush();
      		out.close(); 
    }
  /*****通过配对组ID获取配对的光路名称，Id和状态信息
   * 用于保护组设置参数时显示配对光路信息
   * 回传的数据为key-value,包括：
   *     status:boolean   
   *     upLink:{  
   *              names:String[2],
   *              ids:long[2],
   *              status:boolean[2]
   *             }
   *      downLink:{  
   *              names:String[2],
   *              ids:long[2],
   *              status:boolean[2]
   *             }
   *         
   *     upLink和downLink分别为保护组中上行和下行配对组的信息  
   *     外层的status为反馈状态标识
   *     names：两条光路的名称
   *     ids:两条光路的Id
   *     status:两条光路的线路状态
   *        
   * **/
  @RequestMapping("switch/getGroupRoute") 
  public void getGroupRoute(HttpServletRequest request,HttpServletResponse response) throws IOException{
	  	long id =Long.parseLong(request.getParameter("groupId"));
	  	Protect_groups group = findService.findProtectGroupById(id);
	  	Map<String,Object>responseData = new LinkedHashMap<String,Object>();
	  	boolean status=false;
	  	if(group!=null){
	  		status=true;
	  		Map<String,Object>upLink = new LinkedHashMap<String,Object>();
	  		Map<String,Object>downLink = new LinkedHashMap<String,Object>();
	  		Long[] upLinkIds={group.getRouteUpOnId(),group.getRouteUpOffId()};
	  		boolean[] linkStatus={true,false};
	  		String[] upLinkNames={group.getRouteUpOnName(),group.getRouteUpOffName()};
	  		upLink.put("ids",upLinkIds);
	  		upLink.put("names",upLinkNames);
	  		upLink.put("status",linkStatus);
	  		Long[] downLinkIds={group.getRouteDownOnId(),group.getRouteDownOffId()};
	  		String[] downLinkNames={group.getRouteDownOnName(),group.getRouteDownOffName()};
	  		downLink.put("ids",downLinkIds);
	  		downLink.put("names",downLinkNames);
	  		downLink.put("status",linkStatus);
	  		responseData.put("upLink", upLink);
	  		responseData.put("downLink", downLink);
	  	}
	  	responseData.put("status", status);
	    JSONArray responseJson=JSONArray.fromObject(responseData);
	    PrintWriter out=response.getWriter();
		//System.out.println("回传："+responseJson);
		out.println(responseJson);
		out.flush();
		out.close(); 
  }
    /**----------------------通过RTU id获取配对信息，用来显示在pickLIst的左侧（删除配对信息的时候）-------------------------------*/
    @SuppressWarnings("unchecked")
    @RequestMapping("getRouteMatchByRtuIdFillPick")
     public void getRouteMatchByRtuIdFillPick(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	long id =Long.parseLong(request.getParameter("id"));
    	List<Protect_groups> protect_groups = findService.findProtectGroupsByRtuId(id);
    	LinkedHashMap<String,Object>[] responseData = new LinkedHashMap[2];
    	responseData[0] = new LinkedHashMap<>();
    	responseData[1] = new LinkedHashMap<>();
    	try {
    		for(int i = 0;i<protect_groups.size();i++){
        		Map<String, Object> matchPara = new LinkedHashMap<>();
        		String text = "上行:"+protect_groups.get(i).getRouteUpOnName()+"—"+protect_groups.get(i).getRouteUpOffName()+";"+
        					  "下行："+protect_groups.get(i).getRouteDownOnName()+"—"+protect_groups.get(i).getRouteDownOffName();
        		matchPara.put("id", protect_groups.get(i).getId());
        		matchPara.put("text", text );
        		responseData[1].put(String.valueOf(i), matchPara);
        	}
    		responseData[0].put("status", true);
		} 
    	catch (Exception e) {
			responseData[0].put("status", false);
		}
    	finally {
    		JSONArray responseJson=JSONArray.fromObject(responseData);
    		//System.out.println("-----------------------------"+responseJson);
      		PrintWriter out=response.getWriter();
      		out.println(responseJson);
      		out.flush();
      		out.close(); 
		}
    }
    /**光功率树的生成，此处要列出RTU下能进行光功率采集的光率的光路
     * 备纤均可获取光功率
     * 在线纤模块类型为3和4的可以获取光功率
     * */
    @RequestMapping("lightTree") 
    public void portTree(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	/*****获取区域，局站，rtu表中的所有的信息*******/
    	List<Areas> areas = findService.findAllAreas();
    	List<Stations> stations=findService.findAllStations();
    	List<Rtus> rtus=findService.findAllRtus();
    	for(int i = 0;i<rtus.size();i++)
		{
			if(rtus.get(i).getType().equals("切换rtu"))
				rtus.remove(i--);
		}
    	List<Routes> routes = findService.findAllRoutes();
    	for(int i=0;i<routes.size();i++){
    		Rtus rtu=findService.findRtu(routes.get(i).getRtu_id());
    		int order=routes.get(i).getRtu_model_order();
			String moudleType=rtu.getInstallInfo().substring(order-1,order);
			  /**光路所在的模块类型：1为在线 2为备纤 3为保护-主  4为在线OPM
			          备纤均可以测光功率，在线纤只有3和4可以  */
			if(routes.get(i).getIs_online()&&(!(moudleType.equals("4")||moudleType.equals("3")))){
	    		routes.remove(i--);
	    	}
    	}
    	List<root> roots=new ArrayList<root >(1);
    	root portRoot = new root();
    	roots.add(portRoot);
    	//***********新建一个list，将三个对象的list进行拼接，并生成.jon文件，方便之后生成左侧的树形图***********//*
    	List<Object>  list=new  ArrayList<Object>(); 
    	list.addAll(areas);
    	list.addAll(stations);
    	list.addAll(rtus);
        list.addAll(routes);
    	list.addAll(roots);
    	response.setContentType("text/xml");
    	response.setCharacterEncoding("utf-8");
    	PrintWriter out = response.getWriter();
    	out.print(list);	
    	out.flush();
    	out.close();    	
    } 
    /**------------------得到历史的光功率值---------------------*/
	@RequestMapping("degradation/checkHisPowerValue")
    public void getHistoryPowerData(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
    	String startTime1 = request.getParameter("startTime");
    	String endTime1 = request.getParameter("endTime");
    	long routeId =Long.parseLong(request.getParameter("routeId"));
    	List<Optical_powers> optical_powers = findService.findOpticalPowersByRouteId(routeId);
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date startTime = format.parse(startTime1);
    	Date endTime = format.parse(endTime1);
    	long startTimeTimeStamp = startTime.getTime();
    	long endTimeTimeStamp = endTime.getTime();
    	JSONObject responseData = new JSONObject();
    	Threshold threHold=findService.findThresholdByRouteId(routeId);
    	JSONObject threHolds=new JSONObject();
    	if(threHold!=null){
    		threHolds.put("statusCode", true);
    		threHolds.put("thre1", threHold.getThre1());
    		threHolds.put("thre2", threHold.getThre2());
    		threHolds.put("thre3", threHold.getThre3());
    		threHolds.put("thre4", threHold.getThre4());
    	}
    	else{
    		threHolds.put("statusCode", false);
    	}
    	responseData.put("threHolds", threHolds);
    	boolean status = false;
    	if(optical_powers!=null&&optical_powers.size()>0){
    		JSONArray pValues=new JSONArray();
    		for(int i=0; i<optical_powers.size();i++){
        		Date dataTime = format.parse(optical_powers.get(i).getTestTime());
        		long dataTimeTimeStamp = dataTime.getTime();
        		if(dataTimeTimeStamp>=startTimeTimeStamp&&dataTimeTimeStamp<=endTimeTimeStamp){
        			JSONObject pValue=new JSONObject();
        			pValue.put("pValue",optical_powers.get(i).getPowerValue());
        			pValue.put("time",optical_powers.get(i).getTestTime());
        			pValues.add(pValue);
        		}
        	}
    		status=(!pValues.isEmpty());
    		responseData.put("pValues", pValues);
    	}
    	responseData.put("status", status);
		PrintWriter out=response.getWriter();
		out.println(responseData);
		out.flush();
		out.close();  
    }
    
    /**------------------光功率劣化分析，线性拟合---------------------*/
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("opticalLinearFit")
    public void opticalLinearFit(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
    	String degradationStartTime = request.getParameter("sampleStartTime");
    	String degradationEndTime = request.getParameter("sampleEndTime");
    	long routeId =Long.parseLong(request.getParameter("routeId"));
    	long futureIntervalTime =Long.parseLong((request.getParameter("futureIntervalTime")));
    	String timeIntervalUnit = request.getParameter("timeIntervalUnit");
    	String futureEndTime = request.getParameter("futureEndTime");
    	
    	List<Optical_powers>  optical_powers= findService.findOpticalPowersByRouteId(routeId);
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date startTime = format.parse(degradationStartTime);
    	Date endTime = format.parse(degradationEndTime);
    	long startTimeTimeStamp = startTime.getTime();
    	long endTimeTimeStamp = endTime.getTime();
    	List<Object> xList = new ArrayList<>();
    	List<Object> yList = new ArrayList<>();
    	for(int i = 0;i<optical_powers.size();i++){
    		Date dataTime = format.parse(optical_powers.get(i).getTestTime());
    		long dataTimeTimeStamp = dataTime.getTime();
    		if(dataTimeTimeStamp>=startTimeTimeStamp&&dataTimeTimeStamp<=endTimeTimeStamp){
    			yList.add(optical_powers.get(i).getPowerValue());
    			xList.add(format.parse(optical_powers.get(i).getTestTime()).getTime());
    		}
    	}
    	Long[] xArray = (Long[])xList.toArray(new Long[xList.size()]);
    	String[] yArray = (String[])yList.toArray(new String[yList.size()]);
    	/////////////////////////////////////////////////////
    	//线性拟合模型计算(X = k*Y +R  其中x表示时间戳，Y表示光功率值)//
    	/////////////////////////////////////////////////////
    	double xySum = 0;
    	for(int i=0;i<xArray.length;i++){
    		xySum=xySum+xArray[i]*Double.parseDouble(yArray[i]);
    	}
    	double xSum = 0;
    	for(int i=0;i<xList.size();i++){
    		xSum = xSum + xArray[i];
    	}
    	double ySum = 0;
    	for(int i=0;i<yList.size();i++){
    		ySum = ySum + Double.parseDouble(yArray[i]);
    	}
    	double yySum = 0;
    	for(int i= 0;i<yArray.length;i++){
    		yySum = yySum + Double.parseDouble(yArray[i])*Double.parseDouble(yArray[i]);
    	}
    	int N =xArray.length;
    	double K =(xySum - xSum*ySum/N)/(yySum-ySum*ySum/N);
    	double R = xSum/N - K*ySum/N;
    	/*double yValue =(testDate.getTime()-R)/K;*/
    	
    	//将劣化分析的参数准备好
    	Date testDate = format.parse(futureEndTime);
    	long futureEndTimeVal = testDate.getTime();
    	   	
    	if(timeIntervalUnit.equals("天")){
    		futureIntervalTime = futureIntervalTime*24*60*60*1000;
    	}
    		
    	else
    	{
    		futureIntervalTime = futureIntervalTime*60*60*1000; 
    	}
    		
    	Date now = new Date();
    	long nowStamp = now.getTime();
    	long degradePointStamp = nowStamp;
    	//开始进行线性拟合
    	LinkedHashMap[] timeAndData = new LinkedHashMap[1];
    	timeAndData[0] = new LinkedHashMap<Object, Object>();
    	List<Object> dataList = new ArrayList<>();
    	List<Object> timeList = new ArrayList<>();
    	while(degradePointStamp<=futureEndTimeVal){
    		double yValue =(degradePointStamp-R)/K;
    		dataList.add(yValue);
    		Date date = new Date(degradePointStamp);
    		String xValue = format.format(date);
    		timeList.add(xValue);
    		degradePointStamp = degradePointStamp + futureIntervalTime ;
    	}
    	timeAndData[0].put("timeList", timeList);
    	timeAndData[0].put("dataList", dataList);
    	JSONArray responseJson=JSONArray.fromObject(timeAndData); 
    	//System.out.println(responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush(); 
		out.close(); 
    }
   
  
    /**------------------获取权限树---------------------*/
	@RequestMapping("authorityTree")
    public void authorityTree(HttpServletRequest request,HttpServletResponse response) throws IOException{
		List<Map<String,Object>> maplist = new ArrayList<>();
		//根节点
		Map<String,Object> map1 = new LinkedHashMap<String, Object>();
		map1.put("id", 0);
		map1.put("pid",-1);
		map1.put("name", "权限配置");
		maplist.add(map1);
		
		List<domain.Permissions> allPermissions = findService.findAllPermissions();
		for(int i = 0;i<allPermissions.size();i++){
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("id",allPermissions.get(i).getId());
			map.put("name", allPermissions.get(i).getName());
			if(allPermissions.get(i).getParent()==null) 
				map.put("pid", 0);
			else
				map.put("pid", allPermissions.get(i).getParent().getId());
			maplist.add(map);
		}
		JSONArray responseJson=JSONArray.fromObject(maplist);//生成json格式
		//System.out.println(responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
	}
  /**------------------得到所有的角色---------------------*/
	@RequestMapping("getRole")
    public void getRole(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException, MessagingException, GeneralSecurityException{
		//System.out.println("-------------------");
		List<Role> allroles = findService.findAllRoles();
        List<Map<String,Object>> roleList = new ArrayList<Map<String, Object>>();
        for(int i = 0; i<allroles.size();i++)
        {   
        	if(!allroles.get(i).getName().equals("超级管理员")&&(allroles.get(i).getId()!=1)){
            Map<String,Object> map = new LinkedHashMap<String, Object>();
        	map.put("id", allroles.get(i).getId());
        	map.put("name", allroles.get(i).getName());
        	map.put("addTime", allroles.get(i).getAddTime());
        	map.put("addUser", allroles.get(i).getAddUser());
        	map.put("alterTime", allroles.get(i).getAlterTime());
        	map.put("alterUser", allroles.get(i).getAlterUser());
        	map.put("description", allroles.get(i).getDescription());
        	roleList.add(map);
          }
        }
        JSONArray responseJson=JSONArray.fromObject(roleList);//生成json格式
		//System.out.println(responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
	}
	
	/**------------------得到角色人员树---------------------*/
	@RequestMapping("roleUserTree")
    public void roleUserTree(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException, MessagingException, GeneralSecurityException{
		List<Map<String,Object>> treeList = new ArrayList<Map<String, Object>>();
		Map<String,Object> rootMap = new LinkedHashMap<String, Object>();
        rootMap.put("id", 0);
        rootMap.put("pid", -1);
        rootMap.put("name", "人员管理");
        treeList.add(rootMap);
        List<Role> allroles = findService.findAllRoles(); 
        for(int i = 0; i<allroles.size();i++)
        {
      	  Map<String,Object> map = new LinkedHashMap<String, Object>();
      	if(!allroles.get(i).getName().equals("超级管理员")&&(allroles.get(i).getId()!=1)){
      		map.put("id", allroles.get(i).getId());
        	  map.put("pid", 0);
        	  map.put("name", allroles.get(i).getName());
        	  treeList.add(map); 
      	  }
      	}
        JSONArray responseJson=JSONArray.fromObject(treeList);//生成json格式
		//System.out.println(responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
	}
	/**------------------通过角色ID获得人员LIST---------------------*/
	@RequestMapping("getUserByRole")
    public void getUserByRole(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException, MessagingException, GeneralSecurityException{
		long roleId =Long.parseLong(request.getParameter("roleId"));
		List<domain.User> users = findService.findUsersByRoleId(roleId);
		List<Map<String,Object>> userList = new ArrayList<Map<String, Object>>();
        for(int i = 0; i<users.size();i++)
        {
      	  Map<String,Object> map = new LinkedHashMap<String, Object>();
      	  map.put("id", users.get(i).getId());
      	  map.put("account", users.get(i).getAccount());
      	  map.put("roleId", users.get(i).getRole().getId());
      	  map.put("roleName", users.get(i).getRole().getName());
      	  map.put("phone", users.get(i).getPhone());
      	  map.put("email", users.get(i).getEmail());
      	  map.put("description",users.get(i).getDescription());
      	  userList.add(map);
        }
        JSONArray responseJson=JSONArray.fromObject(userList);//生成json格式
		//System.out.println(responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
        
	}
	/**------------------设置值班表 左侧人员的罗列---------------------*/
	
	@RequestMapping("getAllOperators")
    public void getAllOperators(HttpServletRequest request,HttpServletResponse response) throws IOException {
		int week =Integer.parseInt(request.getParameter("week"));
		//首先找出全体值班人员的信息
		List<Duty_operator> operators = findService.findAllDutyOperator();
		boolean status=false;
		//然后找到已经被安排到当天值班的人员信息
		Map<String , Object>responseData = new LinkedHashMap<>();
		if(operators.size()>0){
			status=true;
			//在当天值班的人
			List<Duty_operator> dutyOperators = findService.findDutyScheduleByDutyDate(week).getOperators(); 
			for(int i=0;i<operators.size();i++){
				for(Duty_operator ope:dutyOperators){
					if(operators.get(i).getId()==(ope.getId())){
						operators.remove(i--);
						break;
					}
				}
			}
			//准备未被选中的值班人员的信息
			Map<String , Object> unDutyInfo = new LinkedHashMap<String,Object>();
			for(int i = 0;i<operators.size();i++){
				Map<String , Object> userInfo = new LinkedHashMap<String , Object>();
				userInfo.put("id", operators.get(i).getId());
				userInfo.put("text", operators.get(i).getAccount()+"_"+operators.get(i).getPhoneNumber());
				unDutyInfo.put(String.valueOf(i),userInfo);
			}
			//准备已经被选中的值班人员的信息
			Map<String , Object> dutyInfo = new LinkedHashMap<String , Object>();
			for(int i = 0;i<dutyOperators.size();i++){
				Map<String , Object> userInfo = new LinkedHashMap<String , Object>();
				userInfo.put("id", dutyOperators.get(i).getId());
				userInfo.put("text", dutyOperators.get(i).getAccount()+"_"+dutyOperators.get(i).getPhoneNumber());
				dutyInfo.put(String.valueOf(i),userInfo);
			}
			responseData.put("unDuty", unDutyInfo);
			responseData.put("duty", dutyInfo);
		}
		responseData.put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
		System.out.println(responseJson.get(0));
		PrintWriter out=response.getWriter();
		out.println(responseJson.get(0));
		out.flush();
		out.close();
	}
	/**------------------点击日期查看值班人员---------------------*/
	@RequestMapping("getDudyOperators")
    public void getDutyPeopleByDate(HttpServletRequest request,HttpServletResponse response) throws IOException {
	    int week=Integer.parseInt(request.getParameter("week"));
	    boolean status=false;
		Duty_schedule schedule = findService.findDutyScheduleByDutyDate(week);
		Map<String,Object> responseData=new LinkedHashMap<String,Object>();
		if(schedule!=null){
			List<Duty_operator> operators=schedule.getOperators();
			if(!operators.isEmpty()){
				status=true;
				List<Map<String , Object>> schedules = new ArrayList<Map<String,Object>>();
				for(Duty_operator operator:operators){
					Map<String,Object> schedul = new LinkedHashMap<>();
					schedul.put("duty", weeks[week-1]);
					schedul.put("account", operator.getAccount());
					schedul.put("email",operator.getEmail());
					schedul.put("phoneNumber",operator.getPhoneNumber());
					schedules.add(schedul);
				}
				responseData.put("schedules", schedules);
			}
		}
		responseData.put("status", status);
		JSONArray jsonArray=JSONArray.fromObject(responseData);
		System.out.println(jsonArray.get(0));
		PrintWriter out=response.getWriter();
		out.println(jsonArray.get(0));
		out.flush();
		out.close();
	}
	/**------------------设置值班表---------------------*/
	@RequestMapping("setDutySchedule")
    public void setDutySchedule(HttpServletRequest request,HttpServletResponse response) throws IOException {
		int week=Integer.parseInt(request.getParameter("week"));
		JSONArray idArray=JSONArray.fromObject(request.getParameter("idArray"));
		Duty_schedule schedule=findService.findDutyScheduleByDutyDate(week);
		boolean status=false;
		if(schedule!=null){
			for(int i=0;i<idArray.size();i++){
				Duty_operator operator=findService.findDutyOperatorById(idArray.getLong(i));
				List<Duty_schedule> schedules=operator.getScedule();
				if(!schedules.contains(schedule)){
					schedules.add(schedule);
					operator.setScedule(schedules);
					status=alterService.alterDutyOperator(operator);
				}else{
					status=true;
				}
			}
		}
        Map<String,Object>responseData=new LinkedHashMap<>();
        responseData.put("status",status);
        JSONArray json=JSONArray.fromObject(responseData);
		PrintWriter out=response.getWriter();
		out.println(json.get(0));
		out.flush();
		out.close();
	}
	/**------------------根据工作人员姓名搜索他的值班信息---------------------*/
	@RequestMapping("searchDutySchduleByName")
    public void searchDutySchduleByName(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String name = request.getParameter("name");
		Map<String , Object> responseData = new LinkedHashMap<>();
		List<Duty_operator> operators=findService.findDutyOperatorByName(name);
		boolean status=false;
		if(operators.size()>0){
			status=true;
			List<Map<String,Object>> scheds=new ArrayList<>();
			for(Duty_operator operator:operators){
				List<Duty_schedule> schedules = operator.getScedule();
				for(Duty_schedule schedul:schedules){
					Map<String,Object> schedule = new LinkedHashMap<>();
					schedule.put("week", weeks[schedul.getDutyWeek()-1]);
					schedule.put("account", operator.getAccount());
					schedule.put("email", operator.getEmail());
					schedule.put("phoneNumber", operator.getPhoneNumber());
					scheds.add(schedule);
				}
			}
			responseData.put("schedules", scheds);
		}
		responseData.put("status", status);
		JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
		System.out.println(responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson.get(0));
		out.flush();
		out.close();
	}
	
	/**------------------得到所有的值班人员的信息---------------------*/
	@RequestMapping("getAllDutyOperator")
    public void getAllDutyOperator(HttpServletRequest request,HttpServletResponse response) throws IOException {
		List<Duty_operator> operators = findService.findAllDutyOperator();
		List<Map<String , Object>> mapList = new ArrayList<>();
		Map<String ,Object> status = new LinkedHashMap<>();
		status.put("status", true);
		mapList.add(status);
		for(int i = 0;i<operators.size();i++){
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("id", operators.get(i).getId());
			map.put("account", operators.get(i).getAccount());
			map.put("email", operators.get(i).getEmail());
			map.put("telephone", operators.get(i).getPhoneNumber());
			mapList.add(map);
		}
		JSONArray responseJson=JSONArray.fromObject(mapList);//生成json格式
		////System.out.println(responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
	}
	/**------------------增加值班人员信息---------------------*/
	@RequestMapping("operatorAdd")
    public void operatorAdd(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String operatorName = request.getParameter("operatorName");
		String operatorEmail = request.getParameter("operatorEmail");
		String operatorPhone = request.getParameter("operatorPhone");
		
		Duty_operator operator = new Duty_operator();
		operator.setAccount(operatorName);
		operator.setEmail(operatorEmail);
		operator.setPhoneNumber(operatorPhone);
		boolean status=true;
		try{
			addService.addDutyOperator(operator);
		}catch(Exception e){
			status=false;
		}
		Map<String,Object>responseData=new LinkedHashMap<String,Object>();
		responseData.put("status", status);
		JSONArray json= JSONArray.fromObject(responseData);
		PrintWriter out=response.getWriter();
		out.println(json.get(0));
		out.flush();
		out.close();
	}
	/**------------------删除值班人员信息---------------------*/
	@RequestMapping("operatorDelete")
    public void operatorDelete(HttpServletRequest request,HttpServletResponse response) throws IOException {
		long deleteId =Long.parseLong(request.getParameter("deleteId"));	
		boolean status = false;
		try{
			delService.deleteDutyOperator(deleteId);
			status = true ;
		}
		catch(Exception e){
			status = false;
		}
		finally{
			PrintWriter out=response.getWriter();
			out.println(status);
			out.flush();
			out.close();
		}
	}
	/**------------------修改值班人员信息---------------------*/
	@RequestMapping("operatorAlter")
    public void operatorAlter(HttpServletRequest request,HttpServletResponse response) throws IOException {	
		boolean status = false;
		try{
			long operatorId =Long.parseLong(request.getParameter("operatorId"));
			String alterOperatorEmail = request.getParameter("alterOperatorEmail");
			String alterOperatorTelephone = request.getParameter("alterOperatorTelephone");
			Duty_operator operator = findService.findDutyOperatorById(operatorId);
			operator.setEmail(alterOperatorEmail);
			operator.setPhoneNumber(alterOperatorTelephone);
			alterService.alterDutyOperator(operator);
			status = true;
		}
		catch(Exception e){
			status = false;
		}
		finally{
			PrintWriter out=response.getWriter();
			out.println(status);
			out.flush();
			out.close();
		}
	}
	/**------------------根据名字搜索值班人员信息---------------------*/
	@RequestMapping("searchOperatorByName")
    public void searchOperatorByName(HttpServletRequest request,HttpServletResponse response) throws IOException {	
		String operatorName = request.getParameter("operatorName");
		Map<String ,Object> nameMap = new LinkedHashMap<>();
		nameMap.put("account", operatorName);
		List<Duty_operator> operators = findService.findDutyOperatorByMulti(nameMap);
		List<Map<String , Object>> mapList = new ArrayList<Map<String , Object>>();
		Map<String ,Object> status = new LinkedHashMap<>();
		status.put("status", true);
		mapList.add(status);
		for(int i = 0;i<operators.size();i++){
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("id", operators.get(i).getId());
			map.put("account", operators.get(i).getAccount());
			map.put("email", operators.get(i).getEmail());
			map.put("telephone", operators.get(i).getPhoneNumber());
			mapList.add(map);
		}
		JSONArray responseJson=JSONArray.fromObject(mapList);//生成json格式
		////System.out.println(responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
	}
	/***设置值班表***/
	@RequestMapping("operator/setDuty")
    public void setDuty(HttpServletRequest request,HttpServletResponse response) throws IOException {	
		Long operatorId = Long.parseLong(request.getParameter("operatorId"));
		Duty_operator operator = findService.findDutyOperatorById(operatorId);
		JSONArray dutys=JSONArray.fromObject(request.getParameter("dutys"));
		List<Duty_schedule> schedule= new ArrayList<>();
		//获取值班
		for(int i=0;i<dutys.size();i++){
			schedule.add(findService.findDutyScheduleByDutyDate(dutys.getInt(i)));
		}
		operator.setScedule(schedule);
		boolean status=alterService.alterDutyOperator(operator);
		Map<String,Object> responseData=new LinkedHashMap<String,Object>();
		responseData.put("status", status);
		JSONArray json= JSONArray.fromObject(responseData);
		PrintWriter out=response.getWriter();
		out.println(json.get(0));
		out.flush();
		out.close();
	}
	
    /**告警处理发送邮件的时候值班人员的列举**/
	
	@RequestMapping("getAllOperatorsEmail")
    public void getAllOperatorsEmail(HttpServletRequest request,HttpServletResponse response) throws IOException {
		List<Duty_operator> operators = findService.findAllDutyOperator();
		String [] operatorName = new String[operators.size()];
		String [] operatorEmail = new String[operators.size()];
		for(int i = 0;i<operators.size();i++){
			operatorName[i] = operators.get(i).getAccount();
			operatorEmail[i] = operators.get(i).getEmail();
		}
		List<Object> operatorInfo = new ArrayList<>();
		operatorInfo.add(operatorName);
		operatorInfo.add(operatorEmail);
		
		JSONArray responseJson=JSONArray.fromObject(operatorInfo);//生成json格式
		//System.out.println(responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
	}
/**告警处理发送短信的时候值班人员的列举**/
	@RequestMapping("getAllOperatorsSms")
    public void getAllOperatorsSms(HttpServletRequest request,HttpServletResponse response) throws IOException {
		List<Duty_operator> operators = findService.findAllDutyOperator();
		String [] operatorName = new String[operators.size()];
		String [] operatorSms = new String[operators.size()];
		for(int i = 0;i<operators.size();i++){
			operatorName[i] = operators.get(i).getAccount();
			operatorSms[i] = operators.get(i).getPhoneNumber();
		}
		List<Object> operatorInfo = new ArrayList<>();
		operatorInfo.add(operatorName);
		operatorInfo.add(operatorSms);
		
		JSONArray responseJson=JSONArray.fromObject(operatorInfo);//生成json格式
		//System.out.println(responseJson);
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close();
	}
/****机架那个页面，罗列机柜的下拉框****/	
	  @RequestMapping(value="listCabinetesInRack")		
	   	public  void ResourceAddFrameTopListCabinetes (HttpServletRequest request,HttpServletResponse response) throws IOException
	   	{
		    /*获取局站的ID*/
		    long stationId =Long.parseLong(request.getParameter("stationId"));
	  		/*找到该局站下面的所有的机柜*/
	  	    List<Cabinets> cabinets = findService.findStation(stationId).getCabinets();
	  	    
	  	    String[] cabinetName = new String[cabinets.size()];
	  	    String[] cabinetId = new String[cabinets.size()];
	  	    
	  	    for(int i=0;i<cabinets.size();i++){
	  	    	cabinetId[i] = String.valueOf(cabinets.get(i).getId());
	  	    	cabinetName[i] = cabinets.get(i).getCabinet_name();
	  	    }
		    
	        Map<String,Object> map = new LinkedHashMap<>();
	        map.put("cabinetName", cabinetName);
	        map.put("cabinetId", cabinetId);
	        
	        JSONArray responseData=JSONArray.fromObject(map);
	   		response.setContentType("text/xml");
	   		response.setCharacterEncoding("utf-8");
	   		PrintWriter out = response.getWriter();
	   		out.print(responseData);			
	   		out.flush();
	   		out.close();
	   	}
	  /**根据类型获取总的日志，实现分页**/
	  @RequestMapping(value="getLogs")		
	   	public  void getLogs (HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
		  String logType = request.getParameter("logType");
		  int perCount = Integer.parseInt(request.getParameter("perCount"));
		  List<Log> resLogs = findService.findAllLog(logType);
		  Map<String,Object> responseData=new LinkedHashMap<String,Object>();
		  List<Map<String,Object>> logs = new ArrayList<>();
		  for(int i=0;i<resLogs.size();i++){
			  if(logs.size()>=perCount){
				  break;
			  }
			  Map<String,Object> logMap = new LinkedHashMap<>();
			  logMap.put("id", resLogs.get(i).getId());
			  logMap.put("resourceName", resLogs.get(i).getResourceName());
			  logMap.put("resourceType", resLogs.get(i).getResourceType());
			  logMap.put("operateDetail", resLogs.get(i).getOperateDetail());
			  logMap.put("user", resLogs.get(i).getUser());
			  logMap.put("date", resLogs.get(i).getDate());
			  logs.add(logMap);
		  }
		  int pageCount=(int) Math.ceil(resLogs.size()*1.0/perCount);
		  pageCount=pageCount==0?1:pageCount;
		  responseData.put("pageCount",pageCount);
		  responseData.put("logs",logs);
		  JSONArray responseDat=JSONArray.fromObject(responseData);
		  response.setContentType("text/xml");
	   	  response.setCharacterEncoding("utf-8");
	   	  PrintWriter out = response.getWriter();
	   	  out.print(responseDat);			
	   	  out.flush();
	      out.close();
	   	}
	  
	  /**分页查询日志**/
	  @RequestMapping(value="getLogsByPage")		
	   	public  void getLogsByPage(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
		  int perCount = Integer.parseInt(request.getParameter("perCount"));
		  String logType = request.getParameter("logType");
		  int page = Integer.parseInt(request.getParameter("page"));
		  List<Log> resLogs = findService.findLogByPage(logType,page,perCount);
		  Map<String,Object> responseData=new LinkedHashMap<String,Object>();
		  List<Map<String,Object>> logs = new ArrayList<>();
		  for(int i=0;i<resLogs.size();i++){
			  Map<String,Object> logMap = new LinkedHashMap<>();
			  logMap.put("id", resLogs.get(i).getId());
			  logMap.put("resourceName", resLogs.get(i).getResourceName());
			  logMap.put("resourceType", resLogs.get(i).getResourceType());
			  logMap.put("operateDetail", resLogs.get(i).getOperateDetail());
			  logMap.put("user", resLogs.get(i).getUser());
			  logMap.put("date", resLogs.get(i).getDate());
			  logs.add(logMap);
		  }
		  responseData.put("logs",logs);
		  JSONArray responseDat=JSONArray.fromObject(responseData);
		  response.setContentType("text/xml");
	   	  response.setCharacterEncoding("utf-8");
	   	  PrintWriter out = response.getWriter();
	   	  out.print(responseDat);			
	   	  out.flush();
	      out.close();
	   	}
	  /**列出日志表格**/
	  @RequestMapping(value="listResourceLog")		
	   	public  void listResourceLog (HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
		  String logType = request.getParameter("logType");
		  int perCount = Integer.parseInt(request.getParameter("perCount"));
		  String startTimeStr = request.getParameter("startTime");
		  String endTimeStr = request.getParameter("endTime");
		  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		  Date startTime = format.parse(startTimeStr);
          Date endTime = format.parse(endTimeStr);
          List<Log> resLogs = findService.findAllLog(logType);
		  Map<String,Object> responseData=new LinkedHashMap<String,Object>();
		  boolean status=false;
		  List<Map<String,Object>> logs = new ArrayList<>();
		  for(int i=0;i<resLogs.size();i++){
			  Date logTime = format.parse(resLogs.get(i).getDate());
			  if(logTime.getTime()<startTime.getTime()||logTime.getTime()>endTime.getTime()){
				  resLogs.remove(i--);
			  }
		  }
		  if(resLogs.size()>0){
			  status=true;
			  for(int i=0;i<resLogs.size();i++){
				  Map<String,Object> logMap = new LinkedHashMap<>();
				  logMap.put("id", resLogs.get(i).getId());
				  logMap.put("resourceName", resLogs.get(i).getResourceName());
				  logMap.put("resourceType", resLogs.get(i).getResourceType());
				  logMap.put("operateDetail", resLogs.get(i).getOperateDetail());
				  logMap.put("user", resLogs.get(i).getUser());
				  logMap.put("date", resLogs.get(i).getDate());
				  logs.add(logMap);
			  }
		  }
		  int pageCount=(int) Math.ceil(resLogs.size()*1.0/perCount);
		  pageCount=pageCount==0?1:pageCount;
		  responseData.put("pageCount",pageCount);
		  responseData.put("logs",logs);
		  responseData.put("status",status);
		  //System.out.println("记录数："+logs.size());
		  JSONArray responseDa=JSONArray.fromObject(responseData);
		  response.setContentType("text/xml");
	   	  response.setCharacterEncoding("utf-8");
	   	  PrintWriter out = response.getWriter();
	   	  out.print(responseDa);			
	   	  out.flush();
	      out.close();
	   	}
} 
