package fiberMonitor.controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.http.HttpException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import domain.Fiber_cores;
import domain.Frame_ports;
import domain.Jumper_backups;
import domain.Jumper_routes;
import domain.Log;
import domain.Optimize_parameters;
import domain.Period_parameters;
import domain.Preparatory_routes;
import domain.Protect_groups;
import domain.Routes;
import domain.Rtus;
import fiberMonitor.bean.AnalysisData;
import fiberMonitor.bean.HttpClientUtil;
import fiberMonitor.bean.NumConv;
import fiberMonitor.bean.TCPComndUtil;
import fiberMonitor.bean.TcpClient;
import fiberMonitor.bean.XmlDom;
import fiberMonitor.bean.XmlCodeCreater;
import net.sf.json.JSONArray;
import service.AddService;
import service.AlterService;
import service.DeleteService;
import service.FindService;

@Controller
/**接收前端请求，封装参数并下发到RTU**/
public class DilverCodeController {
	@Resource(name="findService")
	private FindService findService;
    @Resource(name="addService") 
	private AddService addService;
    @Resource(name="alterService") 
   	private AlterService alterService;
    @Resource(name="deleteService") 
   	private DeleteService delService;
    
    /**
     * @throws IOException下发局站选择的光路，生成监测光路
     * @throws IOException 
     * @throws  
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping("route/addRoute")  
     public void addRoute(HttpServletRequest request,HttpServletResponse response) throws IOException  {
   	      /**------------前端下发的数据为三维数组
   	       * 参数结构：  allRoute[i][0]为一维数组，内容是用户设定的光路参数    
   		   *  allRoute[i][1]为二位数组， allRoute[i][1][0]topologicalRoute
   		   *  allRoute[i][1][1]topologicalPoint----------*/
    	  ResourceController.printAllRequestPara( request);
   		  JSONArray jsonArray = JSONArray.fromObject(request.getParameter("routePara"));//前端发送的为数组,将其装转换为JSONArray
   		  JSONArray prepId = JSONArray.fromObject(request.getParameter("prepId"));//前端发送预备光路数组
   		  JSONArray frameOrderArray=JSONArray.fromObject(request.getParameter("frameOrder"));//Z端配线架端口号
   		  JSONArray crossStation = JSONArray.fromObject(request.getParameter("crossStation")).getJSONArray(0);//经过局站的Id
   		  Long[] prepareId=new Long[prepId.size()];
   		  Long[] stationIds=new Long[crossStation.size()];
   		  Long rtuId=null;
   		  int[] frameOrder=new int[frameOrderArray.size()];
   		  /**---------Z端配线架端口号-----------*/
   		  for(int mainCount=0;mainCount<prepId.size();mainCount++){  //
   			  frameOrder[mainCount]=Integer.parseInt(frameOrderArray.getString(mainCount));
   		  }
   		  /**---------获取预备光路id-----------*/
   		  for(int mainCount=0;mainCount<prepId.size();mainCount++){  //
   			  prepareId[mainCount]=Long.parseLong(prepId.getString(mainCount));
   		  }
   		  /**---------获取station id-----------*/
   		  for(int mainCount=0;mainCount<crossStation.size();mainCount++){  //
   			  stationIds[mainCount]=Long.parseLong(crossStation.getString(mainCount));
   		  }
   		  List<Map<String,Object>>AllRouteParaList=new ArrayList<Map<String,Object>>();//定义最外层的List
   		  for(int mainCount=0;mainCount<jsonArray.size();mainCount++){  //最外层
   			  JSONArray firstSubArr =jsonArray.getJSONArray(mainCount);//一条光路的信息
   			  Map<String,Object> oneRoutePara=new LinkedHashMap<String,Object>();
   	            for(int subCount = 0 ; subCount < firstSubArr.size() ; subCount++){
   	            	JSONArray secondSubArr =firstSubArr.getJSONArray(subCount);//获取一条光路的setedPara以及拓扑信息
   	            	/**--------获取setedPara------*/
   	                if(subCount==0){
   	                	List<String> setPara=new ArrayList<String>();
   	                	for(int count=0;count<secondSubArr.size();count++){
   	                		setPara.add(secondSubArr.getString(count));//获取一条光路的一条setPara
   	                	}
   	                   oneRoutePara.put("setedPara", setPara);//存放一条光路的setedPara
   	                }
   	                /**-----------获取Topologic------------------*/
   	                if(subCount==1){
   	                	
   	                	JSONArray Topologic_routeJ=secondSubArr.getJSONArray(0);
   	                	JSONArray Topologic_pointJ=secondSubArr.getJSONArray(1);
   	                	List<Serializable> topologicalRoute= new ArrayList<Serializable>();
   	                	List<Serializable> topologicalPoint= new ArrayList<Serializable>();
   	                	Map<String,Object>topologies=new LinkedHashMap<String,Object>();
                           for(int count=0;count<Topologic_routeJ.size();count++){
                           	topologicalRoute.add(Long.parseLong(Topologic_routeJ.getString(count)));
   	                	}
                           for(int count=0;count<Topologic_pointJ.size();count++){
                           	topologicalPoint.add(Long.parseLong(Topologic_pointJ.getString(count)));
   	                	}
                           topologies.put("topologicalRoute",topologicalRoute);//存放一条光路的Topologic_routes
                           topologies.put("topologicalPoint",topologicalPoint);//存放一条光路的Topologic_points
                           oneRoutePara.put("topologies", topologies);
   	                }
   	               
   	            }
   	            AllRouteParaList.add(oneRoutePara);//存放一条光路信息  
   	      }
   		  Map<String,Object> responseData=new LinkedHashMap<String,Object>();
   		  boolean status=false;
   		  List<Long> CMs=new ArrayList<Long>();//用于存储RTU id，可能出现一个局站有多个RTU的情况
   		  List<List<Integer>> allRtuPorts=new ArrayList<List<Integer>>();//存放所有RTU下个光路的端口信息
   		  List<List<String>> allTypes=new ArrayList<List<String>>();//存放光路的类型 0：备纤 1：在线
   		  /**发射机端RTU的端口信息**/
   		  List<Long> switchCMs=new ArrayList<Long>();//用于存储RTU id，可能出现一个局站有多个RTU的情况
  		  List<List<Integer>> switchRtuPorts=new ArrayList<List<Integer>>();//存放所有RTU下个光路的端口信息
  		  List<List<String>> switchTypes=new ArrayList<List<String>>();//存放光路的类型 0：备纤 1：在线
   		  for(int count=0;count<AllRouteParaList.size();count++){
   			  Routes route=new Routes();
   			  Map<String,Object> routePara=AllRouteParaList.get(count);
   			  List setPara=(List) routePara.get("setedPara");//用户设置的参数  目前setPara(0):name  setPara(1) 描述 setPara(2) isOnline  setPara(3) isUplink  setPara(4)跨段数目
   			  Map<String,Object>topologies=(Map<String, Object>) routePara.get("topologies");
   			  List<Serializable> topologicalRoute=(List<Serializable>) topologies.get("topologicalPoint");
   			               /**拓扑信息  
   			                 * 从第一位开始为 a端RTU id，
   			                 * A端frame id 
   			                 * 不跨段:末端frameId
   			                 * 跨段:经过的局站1的frame a Id，frame b id,
   			                 *     经过的局站2的frame a Id，frame b id
   			                 *     .........
   			                 *     末端frame Id
   			                 * */
   			  Long stationZId=stationIds[stationIds.length-1];
   			  route.setRoute_name((String) setPara.get(0));
   			  route.setDescription((String) setPara.get(1));
   			  route.setIs_online(Boolean.parseBoolean((String) setPara.get(2)));
   			  route.setIsUplink(Boolean.parseBoolean((String) setPara.get(3)));
   			  route.setCross_number(Integer.parseInt((String) setPara.get(4)));
   			  route.setPreparatoty_id(prepareId[count]);
   			  Preparatory_routes PreParatory_route=findService.findPreparatoryRoute(prepareId[count]);
   			  route.setCreate_date(NumConv.currentTime());
   			  Subject currentUser = SecurityUtils.getSubject();//获取当前用户
   			  String account=currentUser.getPrincipal().toString();//当前用户的账号
   			  route.setCreate_user(account);
   			  route.setFrame_a_id(PreParatory_route.getFrame_a_id());//frame_a_id
   			  route.setFrame_a_order(PreParatory_route.getFrame_a_port_order());//frame_a_order
   			  Long frameZId=(Long) topologicalRoute.get(topologicalRoute.size()-1);
   			  route.setFrame_z_id(frameZId);//frame_z_id
   			  Frame_ports framePort=findService.findFramePortByFrameIdAndPortId(frameZId, frameOrder[count]);//查找配线架端口
   			  /**检查是否连接有发射机RTU**/
   			  int switchRtuPort=0;
   			  Long switchRtuId=null;
   			  if(framePort.getConnection_type()!=null){
   				  /**连接了切换模块**/
   				  if(framePort.getConnection_type().equals("切换跳纤")){
   					  Long switchJumperId=framePort.getConnection_id();
   					  route.setSwitchJumperId(switchJumperId);//该光路为混合模式下的光路，存储切换跳纤ID
   					  Jumper_routes jumperSwitch=findService.findJumperRoute(switchJumperId);
   					  switchRtuId=jumperSwitch.getRtu_id();
   					  route.setSwitchRtuId(switchRtuId);//切换RTU id
   					  /**切换RTU端口序号，将之转换为1-64的编号**/
   					  switchRtuPort=jumperSwitch.getOtdr_port_order()+(jumperSwitch.getModelOrder()-1)*8;
   					  route.setSwitchRtuOrder(switchRtuPort);
   				  }
   				  /**备纤光源跳纤**/
   				  else if(framePort.getConnection_type().equals("备纤光源跳纤")){
   					  Long backUpJumperId=framePort.getConnection_id();
  					  Jumper_backups jumperBackup=findService.findJumperBackup(backUpJumperId);
  					  route.setBackupJumperId(backUpJumperId);
  					  route.setBackupRtuId(jumperBackup.getRtu_id());//切换RTU id
  					  route.setSwitchRtuOrder(jumperBackup.getIn_port_order());
   				  }
   			  }
   			  route.setFrame_z_order(frameOrder[count]);//frame_z_order
   			  route.setRtu_id(PreParatory_route.getRtu_id());
   			  route.setRtu_name(findService.findRtu(PreParatory_route.getRtu_id()).getRtu_name());
   			  route.setRtu_model_order(PreParatory_route.getJumper_route().getModelOrder());
   			  route.setRtu_port_order(PreParatory_route.getRtu_port_order());
   			  route.setStation_a_id(PreParatory_route.getStation_a_id());
   			  route.setStation_a_name(findService.findStation(PreParatory_route.getStation_a_id()).getStation_name());
   			  route.setStation_z_id(stationZId);
   			  route.setStation_z_name(findService.findStation(stationZId).getStation_name());
   			  rtuId=PreParatory_route.getRtu_id();
   			  Serializable routeId=addService.addRoute(route,topologies);
   			  List<Fiber_cores> fibers=findService.findFiberCoresByRouteId(routeId);
   			  float len=0;//存储光路长度
			  for(int i=0;i<fibers.size();i++){
				  len+=Float.parseFloat(fibers.get(i).getLength());
			  }
			  route=findService.findRoute(routeId);
			  route.setLength(String.valueOf(len));
			  alterService.alterRoute(route);
   			  if(routeId!=null){//数据库存储成功  记录端口信息
   				 status=true;
   				 /**记录发射机端口信息
   				  * 这种时候一定在混合模块，而且混合模块的端口C1、D1或C2、D2端口
   				  * switchRtuPort-1)%4=2||3
   				  * */
   				 if((switchRtuPort-1)%4>1){
   					if(switchCMs.contains(switchRtuId)){
   						int index=switchCMs.indexOf(switchRtuId);
   						List<Integer> renewPorts=switchRtuPorts.get(index);//
  						renewPorts.add(switchRtuPort);
  					    switchRtuPorts.set(index,renewPorts);//在发射机RTU下添加端口信息
  						String isOn="0";
  						List<String> temType=switchTypes.get(index);
						if(Boolean.parseBoolean((String) setPara.get(2))){
							isOn="1";
						}
						temType.add(isOn);
						switchTypes.set(index,temType); 
   					 }
   					 else{//属于新的发射机RTU
   	   					    List<Integer> ports=new ArrayList<Integer>();//一个RTU下占用端口的端口序号
   	   					    switchRtuPorts.add(ports);//存放端口占用信息
   	 					    List<String> type=new ArrayList<String>();
   	 					    switchTypes.add(type);
   	 					    switchCMs.add(switchRtuId);
   							List<Integer> temPort=switchRtuPorts.get(switchRtuPorts.size()-1);
   							temPort.add(switchRtuPort);//修改存放当前光路占用RTU的端口号
   							switchRtuPorts.set(switchRtuPorts.size()-1, temPort);
   							List<String> temType=switchTypes.get(switchTypes.size()-1);
   							String isOn="0";
   							if(Boolean.parseBoolean((String) setPara.get(2))){
   								isOn="1";
   							}
   							temType.add(isOn);
   							switchTypes.set(switchTypes.size()-1,temType);
   				    }
   				 }
   				 /**记录接收机端口信息**/
   				if(CMs.contains(rtuId)){
   					int index=CMs.indexOf(rtuId);
   					List<Integer> renewPorts=allRtuPorts.get(index);//
					renewPorts.add(PreParatory_route.getRtu_port_order());
					allRtuPorts.set(index,renewPorts);//在RTU下添加端口信息
					String isOn="0";
					List<String> temType=allTypes.get(index);
					if(Boolean.parseBoolean((String) setPara.get(2))){
						isOn="1";
					}
					temType.add(isOn);
					allTypes.set(index,temType);
   				}
   				 else{//属于新的RTU
   					  List<Integer> ports=new ArrayList<Integer>();//一个RTU下占用端口的端口序号
 					  allRtuPorts.add(ports);//存放端口占用信息
 					  List<String> type=new ArrayList<String>();
 					  allTypes.add(type);
					  CMs.add(rtuId);
					  List<Integer> temPort=allRtuPorts.get(allRtuPorts.size()-1);
					  temPort.add(PreParatory_route.getRtu_port_order());//修改存放当前光路占用RTU的端口号
					  allRtuPorts.set(allRtuPorts.size()-1, temPort);
					  List<String> temType=allTypes.get(allTypes.size()-1);
					  String isOn="0";
					  if(Boolean.parseBoolean((String) setPara.get(2))){
						 isOn="1";
					  }
					  temType.add(isOn);
					  allTypes.set(allTypes.size()-1,temType);
			    }
   			}
   		 }
   		  //如果存储成功,向各RTU发送端口占用指令
   		  String err="";
   		  /**发射机发送失败的端口信息,需要撤销接收端的端口占用**/
   		  List<Map<String,Object>> switchFailPort=new ArrayList<Map<String,Object>>();
   		  /**记录发送失败的光路**/
   		  List<Long> failRoute=new ArrayList<Long>();
   		  if(status){
   			      class SetRtuPort extends Thread{
   			    	  private Map<String,Object> portPara;
   			    	  private boolean statu=true;
   			    	  private String err="";
   			    	  public Map<String,Object>getPortPara(){
 			    		   return this.portPara;
 			    	  }
   			    	  public boolean getStatus(){
 			    		  return this.statu;
 			    	  }
   			    	  public String getErr(){
  			    		  return this.err;
  			    	  }
   			    	  public SetRtuPort(Map<String,Object> para){
   			    		this.portPara=para;
   			    	  }
   			    	  public void run(){
   			    		  Rtus rtu=findService.findRtu((long)portPara.get("CM"));
	   			    	  portPara.put("CLP",rtu.getStation().getId());
	   	   		   	      String xml=XmlCodeCreater.setPortOccupy(portPara);
	   		   			  String rtuUrl="http://"+rtu.getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
	   		   			  String fileName=NumConv.createVerifyCode(20);
	   					  String responseFile="setRtuPortResponse"+fileName+".xml";
	   					  String testDecode="testDecode"+fileName+".xml";
	   		  			    //向RTU下发指令并解析回复
	   		  				  try{
	   		  					  HttpClientUtil.Post(rtuUrl,xml,responseFile,3500,4000); 
	   		  					  XmlDom XmlDom=new XmlDom();
	   		  					  Map<String,Object> responseMsg = XmlDom.AnalyseRespondse(responseFile,testDecode);
	   							 /**-------------如果回复码为0,说明成功下发--------------------------------*/
	   				 	    	   String StatusCode=(String) responseMsg.get("StatusCode");
	   				   	    	  if(!StatusCode.equals("0")){//状态码不为0，说明RTU端接收故障，应将先前存储的光路删除
	   				   	    		  statu=false;
	   				   	    		  err="RTU回复异常，可能由于下发参数非法引起，请核对后重试。";
	   				   	    		  if(StatusCode.equals("15")){
	   				   	    		      err="由于意外情况，您所下发光路连接端口类型中包含实际硬件未配置的模块，请核对硬件连接状态后重试。";
	   				   	    		  }
	   				   	    	}
	   		  				  }catch(ConnectTimeoutException | HttpException e){
	   		  					  //e.printStackTrace();  //输入出错误原因
	   		  					  statu=false;
	   			   	    		  err="RTU通信故障,与RTU的连接建立超时，请检查RTU的状态是否正常。";
	   		  				  }
	   		  				  catch(NullPointerException | DocumentException | IOException  e){
	   		  				      statu=false;
				   	    		  err="RTU回复异常，请稍后重试，若一直出现该故障，请核对RTU是否正常。";
	   		  				  }
	   			    	  }
   			      }
   			      /***线程队列**/
   			      List<SetRtuPort> threadList=new ArrayList<SetRtuPort>();
   			     /**下发端口占用**/
   			     for(int pIndex=0;pIndex<CMs.size();pIndex++){
			    	  Map<String,Object> para=new LinkedHashMap<String,Object>();//用于生成端口占用XML的Map
	   		   		  para.put("CM", CMs.get(pIndex));
	   		   		  para.put("ports",allRtuPorts.get(pIndex));
	   		          para.put("types",allTypes.get(pIndex));
	   		          SetRtuPort setRtuPort=new SetRtuPort(para);
	   		          setRtuPort.start();
	   		          threadList.add(setRtuPort);
				}
   			    /***等待线程队列执行完成**/
   			    for(SetRtuPort setRtuPort:threadList){
   			    	try {
						setRtuPort.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
   			    }
   			    /**读取发送结果**/
   			   for(SetRtuPort setRtuPort:threadList){
			    	boolean statu=setRtuPort.getStatus();
					status&=statu;
					/**读取错误信息**/
					if(!statu){
						err=setRtuPort.getErr();
						long CM=(long)setRtuPort.getPortPara().get("CM");
						List<Integer> ports=(List<Integer>) setRtuPort.getPortPara().get("ports");
						for(int order:ports){
							long SN=findService.findRouteByRtuOrderAndID(CM,order).getId();
							if(!failRoute.contains(SN)){
									failRoute.add(SN);
							}
						}
						
					}
			    }
		  }
   		  else{
   			   status=false;
   			   err="数据库存储故障";
   		  }
   		  if(!status){//下发失败，删除光路
   			 class ThreadDelete extends Thread  /**数据库级联删除问题，新建后立即删除如果是存在级联表的情况删除始终无法删除级联表  **/
   			 {                                  /**但是新建线程在线程中删除能够成功，原因不确定**/
   				 private List<Long> routeIds=new ArrayList<Long>();
   				 public ThreadDelete(List<Long> ids){
   					 this.routeIds=ids;
   				 }
   				 public void run()
   				 {
   		   			 for(long routeIds:routeIds){
   						delService.deleteRoute(routeIds); 
   					 }
   				 }
   			 }
   			/**有发送失败的，需要删除光路**/
   			 if(!failRoute.isEmpty()){
   				ThreadDelete thread=new ThreadDelete(failRoute);
      			 thread.start();
      			/**等待删除完成**/
       			 try {
    				thread.join();
    			 } catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			 }
   			 }
   			 class CancelPortOccupy extends Thread{
   				  private Map<String,Object> portPara;
		    	  public CancelPortOccupy(Map<String,Object> para){
		    		this.portPara=para;
		    	  }
		    	  public void run(){
		    		 Rtus rtu=findService.findRtu((long)portPara.get("CM"));
			    	  portPara.put("CLP",rtu.getStation().getId());
	   		   	      String xml=XmlCodeCreater.cancelPortOccupy(portPara);
	   		   	      ////System.out.println("下发指令:\n"+xml);
		   			  String rtuUrl="http://"+rtu.getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
		   			  String fileName=NumConv.createVerifyCode(20);
					  String  responseFile="cancelPortOccupyResponse"+fileName+".xml";
					  String testDecode="testDecode"+fileName+".xml";
		  			    //向RTU下发指令并解析回复
		  				  try{
		  					  HttpClientUtil.Post(rtuUrl,xml,responseFile,3500,4000); 
		  					  XmlDom XmlDom=new XmlDom();
		  					  Map<String,Object> responseMsg = XmlDom.AnalyseRespondse(responseFile,testDecode);
							 /**-------------如果回复码为0,说明成功下发--------------------------------*/
				 	    	   String StatusCode=(String) responseMsg.get("StatusCode");
				   	    	  if(!StatusCode.equals("0")){//状态码不为0，说明RTU端接收故障，应将先前存储的光路删除
				   	    		
				   	    	}
		  				  }catch(HttpException|NullPointerException | DocumentException | IOException  e){
		  					  
		  				  }
			    	  }
   			 }
   			 /**有发射机发送失败的，需要解除接收机的端口占用**/
   			 if(!switchFailPort.isEmpty()){
   				 for(Map<String,Object> para:switchFailPort){
   					new CancelPortOccupy(para).start();
   				 }
   				
   			 }
   		 }
   		  responseData.put("err", err);
   		  responseData.put("status", status);
   		  responseData.put("rtuId", rtuId);
   		  JSONArray responseJson=JSONArray.fromObject(responseData);
   		  ////System.out.println("JsonData:"+responseJson);
   		  PrintWriter out;
		  out=response.getWriter();
		  out.print(responseJson);
	   	  out.flush();
	   	  out.close();  
	 }
    /**----------------------修改光路------------------------------*/
    @RequestMapping("route/modifyRoute")
     public void alterRoute(HttpServletRequest request,HttpServletResponse response) throws IOException{
   	 long routeId = Long.parseLong(request.getParameter("routeId"));
   	 String routeName = request.getParameter("routeName");
   	 String routeDescription = request.getParameter("routeDescription");
   	 boolean newStatus = Boolean.parseBoolean(request.getParameter("routeStatus"));
   	 boolean isUplink=Boolean.parseBoolean(request.getParameter("linkStatus"));
   	 boolean status = true;	 
 	 Map<String, Object> responseData=new LinkedHashMap<String, Object>();
 	 String err="";
   	 Routes route = findService.findRoute(routeId);
   	 Long rtuId=findService.findRoute(routeId).getRtu_id();
   	 route.setRoute_name(routeName);
   	 route.setIsUplink(isUplink);
   	 route.setDescription(routeDescription);
   	 /**如果修改光路的在线离线状态，向RTU下发端口状态指令**/
   	 if(newStatus!=route.getIs_online()){
   		      LinkedHashMap<String,Object> para=new LinkedHashMap<String,Object>();//用于生成端口占用XML的Map
	   		  para.put("CM",route.getRtu_id());
	   		  List<Integer> port=new ArrayList<>();
	   		  port.add(route.getRtu_port_order());
	   		  List<String> type=new ArrayList<>();
	   		  String typ=newStatus ?"1":"0";
	   		  type.add(typ);
	   		  para.put("ports",port);
	          para.put("types",type);
	   	      Rtus rtu=findService.findRtu(route.getRtu_id());
	   		  para.put("CLP",rtu.getStation().getId());
	   	      String xml=XmlCodeCreater.setPortOccupy(para);
	   	      ////System.out.println("下发指令:\n"+xml);
			  String rtuUrl="http://"+rtu.getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
			  String fileName=NumConv.createVerifyCode(20);
			  String responseFile="modifyRtuPortResponse"+fileName+".xml";
			  String testDecode="testDecode"+fileName+".xml";
			    //向RTU下发指令并解析回复
			  try{
					 HttpClientUtil.Post(rtuUrl,xml,responseFile,3500,4000); 
					 XmlDom XmlDom=new XmlDom();
					 responseData = XmlDom.AnalyseRespondse(responseFile,testDecode);
				 /**-------------如果回复码为0,说明成功下发--------------------------------*/
	 	    	   String StatusCode=(String) responseData.get("StatusCode");
	   	    	  if(!StatusCode.equals("0")){//状态码不为0，说明RTU端接收故障，应将先前存储的光路删除
	   	    		  status=false;
	   	    		  err="RTU回复异常，可能由下发参数异常引起，请核对后重试。";
	   	           }
			}catch(ConnectTimeoutException | HttpException e){
					  status=false;
					  err="RTU通信故障,与RTU的连接建立超时，请检查RTU的状态是否正常。";
			}
			catch(IOException|DocumentException|NullPointerException e){
				  status=false;
				  err="RTU通信故障,RTU回复异常，请检查RTU的状态是否正常。";
			}
		   
   	}
   	 if(status){
   		/**记录日志**/
   		Log log=new Log();
   		log.setDate(NumConv.currentTime());
   		log.setOperateDetail("修改光路");
   		log.setResourceType("光路");
   		log.setResourceType(route.getRoute_name());
   	    Subject currentUser = SecurityUtils.getSubject();//获取当前用户
    	String Account=currentUser.getPrincipal().toString();//当前用户的账号
   		log.setUser(Account);
   		addService.addLog(log);
   		/**修改光路**/
   	   	route.setIs_online(newStatus);
   	   	route.setAlter_date(NumConv.currentTime());
   	   	status=alterService.alterRoute(route);
   	   	if(!status){
   	   		err="数据库操作异常。";
   	   	}
   	 }
   	
    responseData.put("err",err);
    responseData.put("status", status);
   	responseData.put("rtuId", rtuId);
   	JSONArray responseJson=JSONArray.fromObject(responseData);
   	PrintWriter out=response.getWriter();
   	out.println(responseJson);
   	out.flush();
   	out.close();		 	 
    }
    /**删除光路
     * 先向RTU发送解除端口占用指令，如果有发射机，应该同时解除发射机的端口占用，然后删除数据库中的光路
     * @throws IOException 
     * @throws DocumentException */
    @RequestMapping("route/delRoute")
     public void deleteRoute(HttpServletRequest request,HttpServletResponse response) throws IOException{
		   	 long routeId=Long.parseLong(request.getParameter("routeId"));
		   	 boolean status=true;
		   	 String err="";
		   	 Routes route=findService.findRoute(routeId);
		   	 int moudleOrder=route.getRtu_model_order();
		   	 int moudleType=Integer.parseInt(findService.findRtu(route.getRtu_id()).getInstallInfo().substring(moudleOrder-1, moudleOrder));
		   	 List<long[]>idAndOrders=new ArrayList<long[]>();
		   	 Long switchRtuId=route.getSwitchRtuId();
		   	 Long rtuId=route.getRtu_id();
		   	 long[]idAOrder={rtuId,route.getRtu_port_order()};
		   	 idAndOrders.add(idAOrder);
		   	 /**发射机切换端口：
		   	  *解除发射机端口占用的情形只适合模块为混合模块的情形
		   	  * **/
		   	 if(switchRtuId!=null&&moudleType==3){
		   		 long[]idAndOrder={switchRtuId,route.getRtu_port_order()};
		   		 idAndOrders.add(idAndOrder);
		   	 }
		   	 class CancelPortOcpy extends Thread{
		   		 private long CM=0;
		   		 private int order=0;
		   		 private String err="";
		   		 private boolean statu=true;
		   		 public String getErr(){
		   			 return this.err;
		   		 }
		   		 public boolean getStatus(){
		   			 return this.statu;
		   		 }
		   		 
		   		 public CancelPortOcpy(long rtuId,int order){
		   			 this.CM=rtuId;
		   			 this.order=order;
		   		 }
		   		 public void run(){
		   			 LinkedHashMap<String,Object> para=new LinkedHashMap<String,Object>();//用于生成解除端口占用XML的Map
		   			 para.put("CM", CM);
		   			 Rtus rtu=findService.findRtu(CM);
			   		 para.put("CLP",rtu.getStation().getId());
			   		 List<Integer> ports=new ArrayList<Integer>();//占用端口的端口序号
			   		 ports.add(order);
			   		 para.put("ports", ports);
			   		 String xml=XmlCodeCreater.cancelPortOccupy(para);
			   		 //System.out.println("send:"+xml);
			   		 String rtuUrl="http://"+rtu.getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
			   		 String fileName=NumConv.createVerifyCode(20);
					 String responseFile="delRtuPortResponse"+fileName+".xml";
					 String testDecode="testDecode"+fileName+".xml";
			   		 try {
						    HttpClientUtil.Post(rtuUrl, xml, responseFile, 3500, 4000);
						    XmlDom XmlDom=new XmlDom();
						    Map<String, Object> responseMsg=XmlDom.AnalyseRespondse(responseFile,testDecode);
				  	    	String StatusCode=(String) responseMsg.get("StatusCode");
				            if(StatusCode.equals("0")){//
				            	statu=true;
				  	    	 }
				  	    	 else{
				  	    		 statu=false; 
				  	    		 err="RTU回复异常，请稍后重试，若一直出现该故障请检查RTU的状态是否正常。";
				  	    	 }
					} catch (HttpException |IOException e) {
						// TODO Auto-generated catch block
						 e.printStackTrace();  //输入出错误原因
						 statu=false; 
		 	    		 err="RTU通信异常,与RTU建立连接超时，若一直出现该故障请检查RTU的状态是否正常。";
		 	    	}
			   		catch (NullPointerException| DocumentException e) {
						// TODO Auto-generated catch block
						 e.printStackTrace();  //输入出错误原因
						 statu=false; 
		 	    		 err="RTU回复异常，请稍后重试，若一直出现该故障请检查RTU的状态是否正常。";
		 	    	}
		   		 }
		   	 } 
	   		/****
	   		 * 由于RTU的设计模式是接收机管理端口，从而只要保证接收机的端口占用被解除就可以删除光路
	   		 * */
		   	 CancelPortOcpy cancelPortOcpy=new CancelPortOcpy(idAndOrders.get(0)[0],(int)idAndOrders.get(0)[1]);
		   	 cancelPortOcpy.start();
		   	 /**等待线程执行完成**/
		   	 try {
					cancelPortOcpy.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			 }
		   	 /**获取执行结果**/
		   	status&=cancelPortOcpy.getStatus();
		    if(status){//接收机发送成功，向发送机发送解除端口占用
		   		if(idAndOrders.size()==2){//有发射机 
		   		   new CancelPortOcpy(idAndOrders.get(1)[0],(int)idAndOrders.get(1)[1]).start();
		   	   }
		   	}
		    else{
		    	err=cancelPortOcpy.getErr();
		    }
	   		 if(status){
	   			 status =delService.deleteRoute(routeId);
  	    		 if(!status){
  	    			 err="数据库操作异常。";
  	    		 }
	   		 }
  			 Map<String,Object> responseData=new LinkedHashMap<String, Object>();
		   	 responseData.put("status", status);
		   	 responseData.put("rtuId", rtuId);
		   	 responseData.put("err", err);
		   	 JSONArray responseJson=JSONArray.fromObject(responseData);
		   	 PrintWriter out=response.getWriter();
		   	 out.println(responseJson);
		   	 out.flush();
   			 out.close();
   		 	 
    }
    
    /**--------------------点名测试-----------------------------*/
	@RequestMapping("curve/callTest")
    public void SetNameTest(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String, String> testPara=new LinkedHashMap<String, String>();
		Long routeId=Long.parseLong(request.getParameter("routeId"));
		Routes route=findService.findRoute(routeId);
		long rtuId=route.getRtu_id();
    	testPara.put("CM", String.valueOf(rtuId));
    	testPara.put("CLP",  String.valueOf(findService.findRtu(rtuId).getStation().getId()));
    	testPara.put("PS", request.getParameter("PS"));
    	testPara.put("SNo",String.valueOf(route.getRtu_port_order()));
    	testPara.put("P11", request.getParameter("P11")+"000");
    	testPara.put("P12", request.getParameter("P12"));
    	testPara.put("P13", request.getParameter("P13"));
    	testPara.put("P14", request.getParameter("P14"));
    	testPara.put("P15", request.getParameter("P15"));
    	testPara.put("P16", request.getParameter("P16"));
    	testPara.put("P17", request.getParameter("P17"));
 	   /**向RTU下发点名测试命令
		    * 请求延时设为5ms
		    * 等待延时设为20秒
		    * */
    	String setNamedCode=XmlCodeCreater.setNameTestCode(testPara);
    	////System.out.println("点名测试XML："+setNamedCode);
    	String rtuUrl="http://"+findService.findRtu(rtuId).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
		Map<String,Object> responseData=new LinkedHashMap<String, Object>();
    	boolean status=false;
    	String err="";
    	String fileName=NumConv.createVerifyCode(20);
		String responseFile="callTestResponse"+fileName+".xml";
		String testDecode="testDecode"+fileName+".xml";
		System.out.println("==========下发请求========接收文件名为："+responseFile);
    	try {
    		int timeOut=Integer.parseInt(request.getParameter("P14"))*1500+10*1000;
			HttpClientUtil.Post(rtuUrl, setNamedCode, responseFile, 5000,timeOut);
			 /**接收数据解析返回的需要回传的参数*/
			XmlDom XmlDom=new XmlDom();
	    	responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);
	    	String StatusCode=(String) responseData.get("StatusCode");//获取装态码
	      	 status=false;
	      	if(StatusCode.equals("10")){
	      		status=true;
	      	}
	      	else{
	      		status=false;
	      		err="RTU回复异常，请稍后重试，若一直出现该故障，请检查RTU状态是否正常。";
	      	}
		} catch (SocketTimeoutException |ConnectTimeoutException |HttpException  e) {
			// TODO Auto-generated catch block
			status=false;
			err="与RTU连接超时，请稍后重试，若一直出现该故障，请检查RTU状态是否正常";
			e.printStackTrace();
		}
		 catch (IOException |DocumentException |NullPointerException e) {
			// TODO Auto-generated catch block
			 status=false;
			 err="与RTU通信故障，请检查RTU状态是否正常";
			 e.printStackTrace();
		} 
    	Log log=new Log();
		log.setDate(NumConv.currentTime());
		Subject currentUser = SecurityUtils.getSubject();//获取当前用户
	    String Account=currentUser.getPrincipal().toString();//当前用户的账号
	    log.setUser(Account);
	    log.setResourceName(route.getRoute_name());
	    log.setResourceType("曲线测试");
		String detail="光路"+route.getRoute_name()+"进行点名测试，测试结果：";
		if(status){
			detail+="成功。";
		 }
		 else{
			detail+="失败，失败原因："+"RTU通信异常，建立连接超时。";
		 }
		 log.setOperateDetail(detail);
		 addService.addLog(log);
    	 responseData.put("status", status);
    	 responseData.put("err",err);
    	 responseData.put("rtuId",rtuId);
    	 response.setContentType("text/html");
    	 response.setCharacterEncoding("utf-8");
    	 PrintWriter out = response.getWriter();
         /*将回传数据转为Json格式*/
    	 JSONArray responseD =JSONArray.fromObject(responseData);
    	 /*将数据返回到前端*/
    	 out.print(responseD);// 
    	 out.flush();
    	 out.close();
    	
    }
	/**---------------------查询数据库判断是否存在优化测试参数-------------------*/
	@RequestMapping(value="checkOptimize")	
	public  void checkOptimize (HttpServletRequest request,HttpServletResponse response) throws IOException{
		long CM = Long.parseLong(request.getParameter("CM"));
		Integer SNo=Integer.parseInt(request.getParameter("SNo"));
		Routes route = findService.findRouteByRtuOrderAndID(CM, SNo);
		String result;
		if(route.getOptimize_parameter() == null)
		{
			result="null";
		}
		else 
			result="ok";
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(result);			
		out.flush();
		out.close();	
	}
	/***
	 * 通过光路ID获取rtuId
	 * 
	 * **/
	@RequestMapping("route/getRtuIdByRouteId")
    public void getRtuIdByRouteId(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String, Object> responseData=new LinkedHashMap<String, Object>();
		boolean status=false;
		Long routeId=Long.parseLong(request.getParameter("routeId"));
		Routes route=findService.findRoute(routeId);
		if(route!=null){
			status=true;
			responseData.put("rtuId",route.getRtu_id());
		}
		responseData.put("status",status);
    	PrintWriter out=response.getWriter();
    	JSONArray responseD =JSONArray.fromObject(responseData);
    	/*将数据返回到前端*/
    	out.print(responseD);// 
    	out.flush();
    	out.close();
	}
	/**-------------------------------设置优化参数------------------------------------*/
	@RequestMapping("route/optiRoute")
    public void SetOptiPara(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String, Object> indexPara=new LinkedHashMap<String, Object>();
		boolean status=true;
		ResourceController.printAllRequestPara(request);
		Long CM=Long.parseLong(request.getParameter("CM"));
		indexPara.put("CM", request.getParameter("CM"));//获取存放RTU编号
		indexPara.put("CLP", findService.findRtu(CM).getStation().getId());//获取存放局站代码
		int changeCount=Integer.parseInt(request.getParameter("setCount"));//获取修改优化参数的光路数
		indexPara.put("SN",changeCount);//获取存放OTDR端口路数
		String[][] optiPara=new String[changeCount][8];//各条光路的优化参数，每组包含九个参数 分别为SNo，P11-P17
		/**各组的优化参数 由前端下发  下发时分别封装成数组*/
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("optiPara"));//前端发送的为多维数组,形式为N*8，将其装转换为JSONArray
		for(int mainCount=0;mainCount<jsonArray.size();mainCount++){  //遍历获取每个一维数组
            JSONArray subArr =jsonArray.getJSONArray(mainCount);
            for(int subCount = 0 ; subCount < subArr.size() ; subCount++){
            	optiPara[mainCount][subCount] =subArr.getString(subCount);//遍历获取每个一维数组的元素，赋值二维数组
            }
        }
		String optiParaXML=XmlCodeCreater.setOptiParaCode(indexPara,optiPara);//生成xml格式优化参数命令
		Map<String, Object> responseData=new LinkedHashMap<String, Object>();
		String err="";
		/**-------------下发命令到RTU----------------------------------*/
		String rtuUrl="http://"+findService.findRtu(CM).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
		try {
			String fileName=NumConv.createVerifyCode(20);
			String responseFile="setOptiParaResponse"+fileName+".xml";
			String testDecode="testDecode"+fileName+".xml";
			HttpClientUtil.Post(rtuUrl, optiParaXML, responseFile, 2000, 3000);
			XmlDom XmlDom=new XmlDom();
			responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);
			/**-------------如果回复码为0，存放优化参数到数据库--------------------------------*/
	    	String StatusCode=(String) responseData.get("StatusCode");
	    	if(StatusCode.equals("0")){
	    		status=true;
	    		/**---获取对光路的操作，用于查询当前光路是否已经存在优化参数，如果存在则修改，否者覆盖-------*/
	    		for(int count=0;count<optiPara.length;count++){
	    			Routes route=findService.findRouteByRtuOrderAndID(Long.parseLong(request.getParameter("CM")),Integer.parseInt(optiPara[count][0]));
	    			if(route.getOptimize_parameter()==null){//不存在优化参数，直接存储优化参数
	    		    	 Optimize_parameters optimizeParameter=new Optimize_parameters();
	    				 optimizeParameter.setRoute(route);
	    				 optimizeParameter.setMax_range(Integer.parseInt(optiPara[count][1]));
	    				 optimizeParameter.setPulse_width(Integer.parseInt(optiPara[count][2]));
	    				 optimizeParameter.setWave_length(Integer.parseInt(optiPara[count][3]));
	    				 optimizeParameter.setAverage_times(Integer.parseInt(optiPara[count][4]));
	    				 optimizeParameter.setRefractive_index(optiPara[count][5]);
	    				 optimizeParameter.setNon_refractive_threshold(optiPara[count][6]);
	    				 optimizeParameter.setEnd_threshold(optiPara[count][7]);
	    				 Serializable optiId= addService.addOptimizeParameter(optimizeParameter);
	    				 status=status&&(optiId!=null);
	    		 }
	    		  else{    //存在优化参数，进行修改
	    			    Map<String,Object> OptimizePara=new LinkedHashMap<String,Object>();
	    			    OptimizePara.put("max_range",Integer.parseInt(optiPara[count][1]));
	    			    OptimizePara.put("pulse_width",Integer.parseInt(optiPara[count][2]));
	    			    OptimizePara.put("wave_length",Integer.parseInt(optiPara[count][3]));
	    			    OptimizePara.put("average_times",Integer.parseInt(optiPara[count][4]));
	    			    OptimizePara.put("refractive_index",optiPara[count][5]);
	    			    OptimizePara.put("non_refractive_threshold",optiPara[count][6]);
	    			    OptimizePara.put("end_threshold",optiPara[count][7]);
	    			    boolean stat=alterService.updateOptimizeParameter(route.getOptimize_parameter().getId(), OptimizePara);
	    			    status=status&&stat;
	    		}
	    		if(!status)//数据库中存参数失败
	    	    	  {
	    	    		responseData.replace("StatusCode", "1");
	    	    		responseData.replace("err", "数据库存储参数失败");
	    	    		
	    	    	  } 
	    	    }
	    	}
	    	else{
	    		 status=false;
				 err="与RTU通信故障，RTU回复异常，请稍后重试，若一直出现该故障，请检查RTU状态是否正常。";
				 responseData.put("err",err);
	    	}
		} catch (HttpException|SocketTimeoutException|ConnectTimeoutException e) {
			// TODO Auto-generated catch block
			status=false;
			err="与RTU连接超时，请稍后重试，若一直出现该故障，请检查RTU状态是否正常。";
			responseData.put("err",err);
			e.printStackTrace();
		} catch (IOException |DocumentException |NullPointerException e) {
			// TODO Auto-generated catch block
			 status=false;
			 err="与RTU通信故障，RTU回复异常，请稍后重试，若一直出现该故障，请检查RTU状态是否正常。";
			 responseData.put("err",err);
			e.printStackTrace();
		}   //
		responseData.put("status",status);
    	PrintWriter out=response.getWriter();
    	JSONArray responseD =JSONArray.fromObject(responseData);
    	/*将数据返回到前端*/
    	out.print(responseD);// 
    	out.flush();
    	out.close();
	} 
	
	/**-----------------------------更新光路的时间表----------------------------*/
	@RequestMapping("curve/periodTest/refreshCycPara")
    public void refreshCycPara(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String, String> indexPara=new LinkedHashMap<String, String>();
		indexPara.put("CM", request.getParameter("CM"));//获取存放RTU编号
		indexPara.put("CLP", request.getParameter("CLP"));//获取存放局站代码
		indexPara.put("SN","1");//获取存放OTDR端口路数
		String[][] timeTable=new String[1][5];//各条光路的优化参数，每组包含5个参数 分别为SNo，T1,T2 IP01 IP02
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("timeTable"));//前端发送的为多维数组,形式为N*5，将其装转换为JSONArray
		for(int mainCount=0;mainCount<jsonArray.size();mainCount++){  //遍历获取每个一维数组
			 String para= jsonArray.getString(mainCount);//遍历获取每个一维数组的元素
			 if(mainCount==1){
				 try {//将开始时间转换为时间戳下发
					para=NumConv.dateToUnixStamp(para);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 timeTable[0][mainCount] =para;
	     }
		String cycParaXML=XmlCodeCreater.setCycParaCode(indexPara,timeTable);//生成xml格式优化参数命令
		//////System.out.println("周期测试参数表:"+cycParaXML);
		Long CM=Long.parseLong(request.getParameter("CM"));
		String rtuUrl="http://"+findService.findRtu(CM).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
		Map<String, Object> responseData=new LinkedHashMap<String, Object>();
		boolean status=false;
		String err="";
		try {
			String fileName=NumConv.createVerifyCode(20);
			String responseFile="modifyPeriodTimeResponse"+fileName+".xml";
			String testDecode="testDecode"+fileName+".xml";
			HttpClientUtil.Post(rtuUrl, cycParaXML, responseFile, 2000,3000);
			 /*解析Rtu服务器端的回复码*/
			XmlDom XmlDom=new XmlDom();
	      	responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);
	      	String StatusCode=(String) responseData.get("StatusCode");//获取装态码
	      	if(StatusCode.equals("0")){
	      		status=true;
	      	}
	      	else{
	      		status=false;
	      		err="RTU回复异常，请稍后重试，若一直出现该故障，请检查RTU状态是否正常。";
	      	}
		} catch (SocketTimeoutException |ConnectTimeoutException e) {
			// TODO Auto-generated catch block
			status=false;
			err="与RTU连接超时，RTU繁忙请稍后重试，若一直出现该故障，请检查RTU状态是否正常";
			responseData.put("err",err);
			e.printStackTrace();
		} catch (HttpException|IOException|DocumentException |NullPointerException e) {
			// TODO Auto-generated catch block
			 status=false;
			 err="与RTU通信故障，请检查RTU状态是否正常";
			 responseData.put("err",err);
			e.printStackTrace();
		}
    	 if(status){//下发成功,存储参数
        	Routes route=findService.findRouteByRtuOrderAndID(Long.parseLong(request.getParameter("CM")),Integer.parseInt(timeTable[0][0]));
        	if(route.getPeriod_parameters()!=null){//存在，进行修改
 		    	Map<String, Object> timePara=new LinkedHashMap<String, Object>();
 		    	timePara.put("start_time", timeTable[0][1]);
 		    	timePara.put("interval_time", timeTable[0][2]);
 		    	timePara.put("return_ip_1", timeTable[0][3]);
 		    	timePara.put("return_ip_2", timeTable[0][4]);
 		    	alterService.updatePeriodParameter(route.getPeriod_parameters().getId(), timePara);
 		    }
        	else{
        		Period_parameters periodPara=new Period_parameters();
            	periodPara.setStart_time(timeTable[0][1]);
            	periodPara.setInterval_time(timeTable[0][2]);
            	periodPara.setReturn_ip_1(timeTable[0][3]);
            	periodPara.setReturn_ip_2(timeTable[0][4]);
            	periodPara.setRoute(route);
            	addService.addPeriodParameter(periodPara);//存储时间表
        	}
         }
    	responseData.put("status", status);
     	PrintWriter out=response.getWriter();
     	JSONArray responseD =JSONArray.fromObject(responseData);
     	/*将数据返回到前端*/
     	out.print(responseD);// 
     	out.flush();
     	out.close();
    }
	/**-----------------------------------设置周期测试时间表--------------------------------*/
	@RequestMapping("curve/periodTest/setPeriodTest")
    public void SetCycPara(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String, String> indexPara=new LinkedHashMap<String, String>();
		indexPara.put("CM", request.getParameter("CM"));//获取存放RTU编号
		indexPara.put("CLP", request.getParameter("CLP"));//获取存放局站代码
		int tableCount=Integer.parseInt(request.getParameter("tableCount"));//获取时间表路数
		indexPara.put("SN",String.valueOf(tableCount));//获取存放OTDR端口路数
		String[][] timeTable=new String[tableCount][5];//各条光路的优化参数，每组包含5个参数 分别为SNo，T1,T2 IP01 IP02
	    JSONArray jsonArray = JSONArray.fromObject(request.getParameter("timeTable"));//前端发送的为多维数组,形式为N*5，将其装转换为JSONArray
		for(int mainCount=0;mainCount<jsonArray.size();mainCount++){  //遍历获取每个一维数组
	            JSONArray subArr =jsonArray.getJSONArray(mainCount);
	            for(int subCount = 0 ; subCount < subArr.size() ; subCount++){
	            	String para=subArr.getString(subCount);//遍历获取每个一维数组的元素，赋值二维数组
	            	 if(subCount==1){
	    				 try {//将开始时间转换为时间戳下发
	    					para=NumConv.dateToUnixStamp(para);
	    				} catch (ParseException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
	    			 }
	            	timeTable[mainCount][subCount] =para;
	            }
			}  
        String cycParaXML=XmlCodeCreater.setCycParaCode(indexPara,timeTable);//生成xml格式优化参数命令
		////System.out.println("周期测试参数表:"+cycParaXML);
    	Map<String, Object> responseData=new LinkedHashMap<String, Object>();
    	boolean status=false;
    	String err="";
		Long CM=Long.parseLong(request.getParameter("CM"));
		String rtuUrl="http://"+findService.findRtu(CM).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
		try {
			String fileName=NumConv.createVerifyCode(20);
			String responseFile="setPeriodTimeResponse"+fileName+".xml";
			String testDecode="testDecode"+fileName+".xml";
			HttpClientUtil.Post(rtuUrl, cycParaXML, responseFile, 2000,5000);
			  /*解析Rtu服务器端的回复码*/
			XmlDom XmlDom=new XmlDom();
			responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);
	      	String StatusCode=(String) responseData.get("StatusCode");//获取装态码
	      	if(StatusCode.equals("0")){
	      		status=true;
	      	}
	      	else{
	     		err="RTU回复异常，请稍后重试，若一直出现该故障，请检查RTU状态是否正常";
				responseData.put("err",err);
	     	}
		} catch (SocketTimeoutException|ConnectTimeoutException e) {
			// TODO Auto-generated catch block
			status=false;
			err="与RTU连接超时，RTU繁忙请稍后重试，若一直出现该故障，请检查RTU状态是否正常";
			responseData.put("err",err);
			e.printStackTrace();
		} catch (HttpException|IOException|DocumentException|NullPointerException e) {
			// TODO Auto-generated catch block
			 status=false;
			 err="与RTU通信故障，请检查RTU状态是否正常";
			 responseData.put("err",err);
			e.printStackTrace();
		}
    	
    	/**---------------------如果回复码为0，存放时间表到数据库,将周期测试的标志位置位-----------------------------------------*/
    	
    	if(status){   //回复码为0，将数据库中该光路的测试标志位置位
    		    String operate="";
    			for(int setIndex=0;setIndex<timeTable.length;setIndex++){
    	 		    Routes route=findService.findRouteByRtuOrderAndID(Long.parseLong(request.getParameter("CM")),Integer.parseInt(timeTable[setIndex][0]));
    	 		    operate+=route.getRoute_name()+",";
    	 		    if(route.getPeriod_parameters()!=null){//存在，进行修改
    	 		    	Map<String, Object> timePara=new LinkedHashMap<String, Object>();
    	 		    	timePara.put("start_time", timeTable[setIndex][1]);
    	 		    	timePara.put("interval_time", timeTable[setIndex][2]);
    	 		    	timePara.put("return_ip_1", timeTable[setIndex][3]);
    	 		    	timePara.put("return_ip_2", timeTable[setIndex][4]);
    	 		    	alterService.updatePeriodParameter(route.getPeriod_parameters().getId(), timePara);
    	 		    }
    	 		    else{//不存在时间表，增加即可
    	 		    	Period_parameters periodPara=new Period_parameters();
        	        	periodPara.setStart_time(timeTable[setIndex][1]);
        	        	periodPara.setInterval_time(timeTable[setIndex][2]);
        	        	periodPara.setReturn_ip_1(timeTable[setIndex][3]);
        	        	periodPara.setReturn_ip_2(timeTable[setIndex][4]);
        	        	periodPara.setRoute(route);
        	        	addService.addPeriodParameter(periodPara);//存储时间表
    	 		    }
    	 		   route.setIs_period(true);
    	           alterService.alterRoute(route);//修改周期测试状态
    	 		 }
    			Log log=new Log();
    			log.setDate(NumConv.currentTime());
    			Subject currentUser = SecurityUtils.getSubject();//获取当前用户
             	String Account=currentUser.getPrincipal().toString();//当前用户的账号
             	log.setUser(Account);
    			log.setOperateDetail("启动周期测试");
    			log.setResourceName(operate);
    			log.setResourceType("曲线测试");
    			addService.addLog(log);
    	}

   	   
      	responseData.put("status", status);
    	PrintWriter out=response.getWriter();
    	JSONArray responseD =JSONArray.fromObject(responseData);
    	/*将数据返回到前端*/
    	out.print(responseD);// 
    	out.flush();
    	out.close();	
    }
	/**--------------------取消周期测试------------------------*/
	@RequestMapping("curve/periodTest/cancelPeriodTest")
    public void CancelCycTest(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Map<String, String> indexPara=new LinkedHashMap<String, String>();
		indexPara.put("CM", request.getParameter("CM"));//获取存放RTU编号
		indexPara.put("CLP", request.getParameter("CLP"));//获取存放局站代码
		int canCount=Integer.parseInt(request.getParameter("canCount"));//获取取消周期测试
		indexPara.put("SN", String.valueOf(canCount));//获取存放OTDR端口路数
		String [] cancelIndex=new String[canCount];
		/**---------------------------将字符串形式的一维数组还原--------------------------------------------------*/
		JSONArray jsonArray = JSONArray.fromObject(request.getParameter("canIndex"));//以Json格式获取前端传下来的数组（字符串形式）
		for(int index=0;index<jsonArray.size();index++){
			cancelIndex[index]=jsonArray.getString(index);//读取每个元素，存放到数组中
		}
		String  cancelCycXML=XmlCodeCreater.cancelCycTestCode(indexPara,cancelIndex);//生成xml格式优化参数命令
		Long CM=Long.parseLong(request.getParameter("CM"));
		String rtuUrl="http://"+findService.findRtu(CM).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
		Map<String, Object> responseData=new LinkedHashMap<String, Object>();
		boolean status=false;
		String err="";
		try {
			String fileName=NumConv.createVerifyCode(20);
			String responseFile="cancelPeriodResponse"+fileName+".xml";
			String testDecode="testDecode"+fileName+".xml";
			HttpClientUtil.Post(rtuUrl, cancelCycXML, responseFile, 3000, 5000);
			 //解析Rtu服务器端的回复码
			 XmlDom XmlDom=new XmlDom();
	     	responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);//调用文件解析函数，获取回传数据
	     	if(responseData.get("StatusCode").equals("0")){
	     		status=true;
	     	}
	     	else{
	     		err="RTU回复异常，请稍后重试，若一直出现该故障，请检查RTU状态是否正常";
				responseData.put("err",err);
	     	}
		} catch (SocketTimeoutException|ConnectTimeoutException e) {
			// TODO Auto-generated catch block
			status=false;
			err="与RTU连接超时，RTU繁忙请稍后重试，若一直出现该故障，请检查RTU状态是否正常";
			responseData.put("err",err);
			e.printStackTrace();
		} catch (HttpException|IOException|DocumentException|NullPointerException e) {
			// TODO Auto-generated catch block
			 status=false;
			 err="与RTU通信故障，请检查RTU状态是否正常";
			 responseData.put("err",err);
			e.printStackTrace();
		}
    	if(status){//下发取消命令成功，更改该光路的字段
    		String operate="";
    		for(int canIndex=0;canIndex<cancelIndex.length;canIndex++){
 		       Routes route=findService.findRouteByRtuOrderAndID(Long.parseLong(request.getParameter("CM")),Integer.parseInt(cancelIndex[canIndex])); //将取消周期测试光路的周期测试状态标识设为false
 		       operate+=route.getRoute_name()+",";
 		       route.setIs_period(false);
 		       alterService.alterRoute(route);//修改周期测试状态
 			  /**------------此处应该删除数据库中的时间表-------------*/
 		 }
 		    Log log=new Log();
			log.setDate(NumConv.currentTime());
			log.setOperateDetail("取消周期测试");
			Subject currentUser = SecurityUtils.getSubject();//获取当前用户
         	String Account=currentUser.getPrincipal().toString();//当前用户的账号
         	log.setUser(Account);
			log.setResourceName(operate);
			log.setResourceType("曲线测试");
			addService.addLog(log);

 	   }
    	responseData.put("status", status);
 		PrintWriter out=response.getWriter();
     	JSONArray responseD =JSONArray.fromObject(responseData);//将回传数据封装为JSON格式
     	/*将数据返回到前端*/
     	//////System.out.println("回复码："+responseData.get("StatusCode"));
     	out.print(responseD);// 
     	out.flush();
     	out.close();
	}
	
	/**--------------------障碍告警配置-----------------------*/
	@RequestMapping("curve/obstacleTest/setObstacle")
    public void setObstacleTesting(HttpServletRequest request,HttpServletResponse response) throws IOException{
		ResourceController.printAllRequestPara(request);
		Map<String, Object> alarmPara =new LinkedHashMap<String, Object>();
		Long CM=Long.parseLong(request.getParameter("CM"));
		Long CLP=findService.findRtu(CM).getStation().getId();//station Id
		JSONArray groupParaArray=JSONArray.fromObject(request.getParameter("threshodList"));
		List<Integer> SNos=new ArrayList<Integer>();//光端口号
		String[] keys={
				         "ASNo","AT01","AT02","AT03","AT04","AT05",
				         "AT06","TYPE","IP01","IP02","IP03","IP04",
				         "IP05", "IP06","T3","T4","PS","P21","P22",
				         "P23","P24","P25","P26","P27"
				       };
		alarmPara.put("CM", CM);
		alarmPara.put("CLP", CLP);
		alarmPara.put("AN", 1);
		String ANo=groupParaArray.getString(0);//ANo
		if(ANo.equals("")){
			ANo="*";
		}
		JSONArray groupRoutePara=groupParaArray.getJSONArray(1);//获取一个告警组光路的全部信息
		List<Map<String,Object>>threshodsList=new ArrayList<Map<String,Object>>();////一个告警组下所有光路的门限
		for(int routeIndex=0;routeIndex<groupRoutePara.size();routeIndex++){
			Map<String, Object>threshodsMap=new LinkedHashMap<String, Object>();//一条光路的门限
			JSONArray routePara=groupRoutePara.getJSONArray(routeIndex);//获取一条光路的参数信息
			SNos.add(Integer.parseInt(routePara.getString(0)));//存放光端口号
			for(int paraIndex=0;paraIndex<routePara.size();paraIndex++){//获取各参数
				threshodsMap.put(keys[paraIndex], routePara.getString(paraIndex));
			}
			threshodsList.add(threshodsMap);//放入一条光路的门限到列表中
		}
		String err="";
		Map<String,Object> groupMap=new LinkedHashMap<String, Object>();//一个告警组的参数
		groupMap.put("ANo", ANo);
		groupMap.put("ASN", threshodsList.size());
		groupMap.put("threshodsList", threshodsList);
		List<Object> groupList=new ArrayList<Object>();//所有告警组的参数
		groupList.add(groupMap);
		alarmPara.put("groupList", groupList);
		String  setAlarmTestCode=XmlCodeCreater.setAlarmTestCode(alarmPara);//生成xml格式优化参数命令
		////System.out.println("XML文件为：\n"+setAlarmTestCode);
		String rtuUrl="http://"+findService.findRtu(CM).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
		boolean status=false;
		Map<String, Object> responseData=new LinkedHashMap<String,Object>();
		try {
			String fileName=NumConv.createVerifyCode(20);
			String responseFile="obstacleResponse"+fileName+".xml";
			String testDecode="testDecode"+fileName+".xml";	
			HttpClientUtil.Post(rtuUrl, setAlarmTestCode, responseFile, 2500, 2500);
			XmlDom XmlDom=new XmlDom();	
			responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);//调用文件解析函数，获取回传数据
			if(responseData.get("StatusCode").equals("0")){//下发取消命令成功，更改该光路的字段
	    		status=true;
	    		String operate="";
	    		for(int SNoIndex=0;SNoIndex<SNos.size();SNoIndex++){//修改数据库的字段
	 		    	Routes route=findService.findRouteByRtuOrderAndID(CM, SNos.get(SNoIndex));
	 		    	operate+=route.getRoute_name()+",";
	 		    	route.setIsObstalce(true);
	 		    	alterService.alterRoute(route);
	 		    }
	    		Log log=new Log();
	 			log.setDate(NumConv.currentTime());
	 			log.setOperateDetail("设置障碍告警测试");
	 			Subject currentUser = SecurityUtils.getSubject();//获取当前用户
	          	String Account=currentUser.getPrincipal().toString();//当前用户的账号
	          	log.setUser(Account);
	 			log.setResourceName(operate);
	 			log.setResourceType("曲线测试");
	 			addService.addLog(log);

	 	   }
	       else if(responseData.get("StatusCode").equals("14")){
	    		  status=false;
	    		  if(responseData.get("err")==null){
	    			  err="RTU回复异常，请稍后重试，若一直出现该故障，请检查RTU状态是否正常。";
	    			  responseData.put("err",err);
	    		  }
	      }
	       else{
	    	      status=false;
	    		  err="RTU回复异常，请稍后重试，若一直出现该故障，请检查RTU状态是否正常。";
	    		  responseData.put("err",err);
	    		  
	       }
		}catch (SocketTimeoutException|ConnectTimeoutException e) {
			// TODO Auto-generated catch block
			status=false;
			err="与RTU连接超时，若一直出现该故障，请检查RTU状态是否正常";
			responseData.put("err",err);
			e.printStackTrace();
		} catch (HttpException|IOException|DocumentException|NullPointerException e) {
			// TODO Auto-generated catch block
			 status=false;
			 err="与RTU通信故障，请检查RTU状态是否正常";
			 responseData.put("err",err);
			e.printStackTrace();
		}
    	 //解析Rtu服务器端的回复码
     	PrintWriter out=response.getWriter();
    	responseData.put("status", status);
    	JSONArray responseD =JSONArray.fromObject(responseData);//将回传数据封装为JSON格式
    	/*将数据返回到前端*/
    	out.print(responseD);// 
    	out.flush();
    	out.close();
    	
	}
	
	/**--------------------取消障碍告警-----------------------
	 * @throws IOException */
	@RequestMapping("curve/obstacleTest/cancelObstacle")
    public void cancelObstacleTesting(HttpServletRequest request,HttpServletResponse response) throws IOException {
		ResourceController.printAllRequestPara(request);
		Long CM=Long.parseLong(request.getParameter("CM"));
		Long CLP=findService.findRtu(CM).getStation().getId();//station Id
		JSONArray SNoAndIdArray=JSONArray.fromObject(request.getParameter("SNoAndId"));
		List<Long> curveIds=new ArrayList<Long>(); 
		String[] SNos=new String[SNoAndIdArray.size()];
		for(int index=0;index<SNoAndIdArray.size();index++){
			JSONArray SNoAndId=SNoAndIdArray.getJSONArray(index);
			SNos[index]=SNoAndId.getString(0);
			curveIds.add(Long.parseLong(SNoAndId.getString(1)));
		}
		Map<String, Object> cancelPara =new LinkedHashMap<String, Object>();
		cancelPara.put("CM", CM);
		cancelPara.put("CLP", CLP);
		cancelPara.put("SNos", SNos);
	    String  cancelObstacle=XmlCodeCreater.cancelObstacle(cancelPara);//生成xml格式优化参数命令
		//////System.out.println("XML文件为：\n"+cancelObstacle);
	    String rtuUrl="http://"+findService.findRtu(CM).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
	    String fileName=NumConv.createVerifyCode(20);
	    String responseFile="cancelObstacleResponse"+fileName+".xml";
	    String testDecode="testDecode"+fileName+".xml";
		boolean status=false;
		Map<String, Object> responseData=new LinkedHashMap<String,Object>();
		try {
				HttpClientUtil.Post(rtuUrl, cancelObstacle, responseFile, 2500,2500);
				XmlDom XmlDom=new XmlDom();	
				responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);
				if(responseData.get("StatusCode").equals("0")){//下发取消命令成功，更改该光路的字段
				status=true;
		    	String operate="";
		    	for(int SNoIndex=0;SNoIndex<curveIds.size();SNoIndex++){//修改数据库的字段
		 		    	Routes route=findService.findRoute(curveIds.get(SNoIndex));
		 		    	route.setIsObstalce(false);
		 		    	operate+=route.getRoute_name()+",";
		 		    	alterService.alterRoute(route);
		 		    }
		 		    Log log=new Log();
					log.setDate(NumConv.currentTime());
					log.setOperateDetail("取消障碍告警测试");
					Subject currentUser = SecurityUtils.getSubject();//获取当前用户
		         	String Account=currentUser.getPrincipal().toString();//当前用户的账号
		         	log.setUser(Account);
		            log.setResourceName(operate);
					log.setResourceType("曲线测试");
					addService.addLog(log);
		 	   }else{
		 		  status=false;
	    		  String err="RTU回复异常，请稍后重试，若一直出现该故障，请检查RTU状态是否正常。";
	    		  responseData.put("err",err);
	    		}
			}catch(HttpException e){
				  status=false;
	    		  String err="RTU连接异常，请稍后重试，若一直出现该故障，请检查RTU状态是否正常。";
	    		  responseData.put("err",err);
			} 
		   catch (DocumentException |IOException e) {
			      status=false;
	    		  String err="RTU回复异常，请稍后重试，若一直出现该故障，请检查RTU状态是否正常。";
	    		  responseData.put("err",err);
		}
    	responseData.put("status", status);
    	PrintWriter out=response.getWriter();
    	JSONArray responseD =JSONArray.fromObject(responseData);//将回传数据封装为JSON格式
    	/*将数据返回到前端*/
    	out.print(responseD);// 
    	out.flush();
    	out.close();
	}
	
	/**--------------------设置告警提示方式-----------------------
	 * @throws IOException */
	@RequestMapping("setAlarmAlert")
    public void setAlarmAlert(HttpServletRequest request,HttpServletResponse response) throws IOException{
		ResourceController.printAllRequestPara(request);
		Long CM=Long.parseLong(request.getParameter("CM"));
		String alarmId=request.getParameter("alarmId");
		Long CLP=findService.findRtu(CM).getStation().getId();//station Id
		Map<String, Object> alarmPara =new LinkedHashMap<String, Object>();
		alarmPara.put("CM", CM);
		alarmPara.put("CLP", CLP);
		alarmPara.put("AI",alarmId);
	    String  setAlarmCode=XmlCodeCreater.setAlarmAlertWay(alarmPara);//生成xml格式优化参数命令
		//////System.out.println("XML文件为：\n"+setAlarmCode);
	    String rtuUrl="http://"+findService.findRtu(CM).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
	    String fileName=NumConv.createVerifyCode(20);
	    String responseFile="setAlarmWayResponse"+fileName+".xml";
	    String testDecode="testDecode"+fileName+".xml";
   	    boolean status=false;
	    Map<String, Object> responseData=new LinkedHashMap<String, Object>();
	    String err="";
		try {
			  HttpClientUtil.Post(rtuUrl, setAlarmCode, responseFile, 3500, 4000);
			  XmlDom XmlDom=new XmlDom();
		      responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);//调用文件解析函数，获取回传数据
		      if(responseData.get("StatusCode").equals("0")){//下发命令成功，更改该光路的字段
	    		status=true;
	    		Rtus rtu=findService.findRtu(CM);
	    		rtu.setAlarm_wayId(Long.parseLong(alarmId));//设置RTU告警方式
	    		alterService.alterRtu(rtu);
	    	}else{
	    		err="RTU回复异常，请稍后重试，若一直出现该故障，请检查RTU状态是否正常。";
	    	  }
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			err="与RTU连接超时，请稍后重试，若一直出现该故障，请检查RTU状态是否正常。";
		}
		catch(IOException |DocumentException e){
			err="RTU回复异常，请稍后重试，若一直出现该故障，请检查RTU状态是否正常。";
		} 
		responseData.put("status", status);
		responseData.put("err", err);
    	JSONArray responseD =JSONArray.fromObject(responseData);//将回传数据封装为JSON格式
    	/*将数据返回到前端*/
    	 PrintWriter out=response.getWriter();
    	out.print(responseD);// 
    	out.flush();
    	out.close();
	}
	
	/**--------------------	取消告警提示-----------------------*/
	@RequestMapping("cancelAlarmAlert")
    public void cancelAlarmAlert(HttpServletRequest request,HttpServletResponse response) throws HttpException, IOException, DocumentException{
		ResourceController.printAllRequestPara(request);
		Long CM=Long.parseLong(request.getParameter("CM"));
		Long CLP=findService.findRtu(CM).getStation().getId();//station Id
		Map<String, Object> alarmPara =new LinkedHashMap<String, Object>();
		alarmPara.put("CM", CM);
		alarmPara.put("CLP", CLP);
		String  cancelAlarmCode=XmlCodeCreater.cancelAlarmAlert(alarmPara);//生成xml格式优化参数命令
		//////System.out.println("XML文件为：\n"+cancelAlarmCode);
		String rtuUrl="http://"+findService.findRtu(CM).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
		String fileName=NumConv.createVerifyCode(20);
		String responseFile="cancelAlarmResponse"+fileName+".xml";
		String testDecode="testDecode"+fileName+".xml";
		HttpClientUtil.Post(rtuUrl, cancelAlarmCode, responseFile, 1500, 1500);
		XmlDom XmlDom=new XmlDom();
		Map<String, Object> responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);//调用文件解析函数，获取回传数据
     	boolean status=false;
		PrintWriter out=response.getWriter();
    	if(responseData.get("StatusCode").equals("0")){//下发取消命令成功，更改该光路的字段
    		status=true;
    		Rtus rtu=findService.findRtu(CM);
    		rtu.setAlarm_wayId(null);//设置RTU告警方式
    	}
		responseData.put("status", status);
    	JSONArray responseD =JSONArray.fromObject(responseData);//将回传数据封装为JSON格式
    	/*将数据返回到前端*/
    	out.print(responseD);// 
    	out.flush();
    	out.close();
	}
	/**----------删除告警组----------
	 * @throws IOException */
	@RequestMapping("deleteAlarmGroup")
	public void deleteAlarmGroup(HttpServletRequest request,HttpServletResponse response) throws IOException{
		ResourceController.printAllRequestPara(request);
		Long groupId=Long.parseLong(request.getParameter("id"));
		Long CM=Long.parseLong(request.getParameter("CM"));
		Long CLP=findService.findRtu(CM).getStation().getId();
		List<Routes> routes=findService.findRoutesByPriotityId(groupId);
		Map<String, Object> responseData=new LinkedHashMap<>();
	    String[] SNos=new String[routes.size()];
	    List<Long> ids=new ArrayList<Long>();
		Map<String, Object> cancelPara =new LinkedHashMap<String, Object>();
		boolean status=true;
		delService.deletePriority(groupId);//删除告警组
		/*当前组中存在光路，
		 * 除删除组外还要想Rtu下发取消障碍告警测试命令，
		 * 同时将当前组的光路的分组信息删除*/
		if(routes.size()>0){
			for(int index=0;index<routes.size();index++){
				ids.add(routes.get(index).getId());
				SNos[index]=String.valueOf(routes.get(index).getRtu_port_order());
			}
			cancelPara.put("CM", CM);
			cancelPara.put("CLP", CLP);
			cancelPara.put("SNos", SNos);
		    String  cancelObstacle=XmlCodeCreater.cancelObstacle(cancelPara);//生成xml格式优化参数命令
			String rtuUrl="http://"+findService.findRtu(CM).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
			String fileName=NumConv.createVerifyCode(20);
			String responseFile="delAlarmResponse"+fileName+".xml";
			String testDecode="testDecode"+fileName+".xml";
			try {
				 HttpClientUtil.Post(rtuUrl, cancelObstacle, responseFile, 3500, 4000);
				 XmlDom XmlDom=new XmlDom();	
				 responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);//调用文件解析函数，获取回传数据
				    if(responseData.get("StatusCode").equals("0")){//下发取消命令成功，更改该光路的字段
						Map<String, Object> routePara=new LinkedHashMap<String, Object>();
			    		routePara.put("priotity_id", null);
			    		routePara.put("priotity_name", " ");
			 		    for(int SNoIndex=0;SNoIndex<ids.size();SNoIndex++){//修改数据库的字段
			 		    	status=alterService.updateRoute(ids.get(SNoIndex), routePara);
			 		    }
			    	}
			} catch (HttpException | IOException|NullPointerException | DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
		}
    	responseData.put("status", status);
    	JSONArray responseD =JSONArray.fromObject(responseData);//将回传数据封装为JSON格式
    	PrintWriter out=response.getWriter();
    	/*将数据返回到前端*/
    	out.print(responseD);// 
    	out.flush();
    	out.close();
	}
	
	/**功能：设置配对组
	 * 前端下发的信息包括三个参数
	 * rtuId：当前设置配对组的保护-主模块所在的RTU的Id
	 * idUplink：上行配对组的光路Id数组，为一个二维数组。每个元素为一个长度为2的一维数组，表示一个保护组的上行配对组  在线光路在前，备纤在后
	 * idDownlink：下行配对组的光路Id数组，为一个二维数组。每个元素为一个长度为2的一维数组，表示一个保护组的下行配对组 在线光路在前，备纤在后
	 * @throws IOException 
	 * */
	@RequestMapping("switch/setSwitchGroup")
	 public void setProtectGroup(HttpServletRequest request,HttpServletResponse response) throws IOException{
		    ResourceController.printAllRequestPara(request);
		    JSONArray idUplinkArray=JSONArray.fromObject(request.getParameter("idUplink"));
		    JSONArray idDownlinkArray=JSONArray.fromObject(request.getParameter("idDownlink"));
			/**存储所有保护组的上行配对Id组**/
			List<Long[]> idUplinks=new ArrayList<Long[]>();
			/**存储所有保护组的下行配对Id组**/
			List<Long[]> idDownlinks=new ArrayList<Long[]>();
			for(int index=0;index<idUplinkArray.size();index++){
				/**获取一个保护组的上行配对Id组**/
				JSONArray idUplinkMatch=idUplinkArray.getJSONArray(index);
				/**获取一个保护组的下行配对Id组**/
				JSONArray idDownMatch=idDownlinkArray.getJSONArray(index);
				//读取一个上行链路组
				Long[] upLink={
								Long.parseLong(idUplinkMatch.getString(0)),
								Long.parseLong(idUplinkMatch.getString(1))
							  };
				//读取一个下行链路组
				Long[] downLink={
								Long.parseLong(idDownMatch.getString(0)),
								Long.parseLong(idDownMatch.getString(1))
							  };
				idUplinks.add(upLink);
				idDownlinks.add(downLink);
			}
			long rtuId = Long.parseLong(request.getParameter("rtuId"));
			Map <String,Object> protectGroupsPara=new LinkedHashMap<String, Object>();//保护组参数
			List <Map<String,Object>>groupList=new ArrayList<Map<String,Object>>();//保护组信息
			List<Protect_groups>protectGroups=new ArrayList<Protect_groups>();//暂存新增的保护组，等下发成功后存入数据库
			boolean status=false;
			Subject currentUser = SecurityUtils.getSubject();//获取当前用户
			String account=currentUser.getPrincipal().toString();
			/**封装下发生成保护组的数据*/
			for(int i=0;i<idUplinks.size();i++){
				Map <String,Object> oneGroupPara=new LinkedHashMap<String, Object>();//一条保护组参数
				Map <String,Object> uplink=new LinkedHashMap<String, Object>();//上行配对组参数
				Map <String,Object> downlink=new LinkedHashMap<String, Object>();//下行配对组参数
				Long slaveRtuId=findService.findRoute(idUplinks.get(i)[0]).getSwitchRtuId();//保护模块-从所在RTU id
				Protect_groups protectGroup = new Protect_groups();
				protectGroup.setCreateUser(account);
				protectGroup.setCreateDate(NumConv.currentTime());
				Routes routeUpOn=findService.findRoute(idUplinks.get(i)[0]);
				int upOnOrder=routeUpOn.getRtu_port_order();//上行在线链路端口序号
				/**光开关状态：
				 *   16表示1-3相连  即为A2,B2,C2,D2为在线  端口序号为5-8
				 *   96表示1-2相连  即为A1,B1,C1,D1为在线  端口序号为1-4
				 *   **/
				String upSwitchCode="16";//光开关连接状态
				String connectPos="0";//主-从连接模式 0表示A-A B-B；1表示A-C B-D
				if((upOnOrder-1)%8<4){//端口序号为1-4在线，96
					if(routeUpOn.getIs_online()){
						upSwitchCode="96";
					}
					if((routeUpOn.getSwitchRtuOrder()-1)%8>3){//A-C B-D相连模式
						connectPos="1";
					}
				}
				else{//端口号为5-8
					if(!routeUpOn.getIs_online()){//端口序号为1-4在线，96
						upSwitchCode="96";
					}
					if((routeUpOn.getSwitchRtuOrder()-1)%8<4){//C-A D-B相连模式
						connectPos="1";
					}
				}
				int upOffOrder=findService.findRoute(idUplinks.get(i)[1]).getRtu_port_order();//上行在备纤链路端口序号
				int upPNo=Math.min(upOffOrder, upOnOrder);//上行配对组序号
				upPNo=(routeUpOn.getRtu_model_order()-1)*4+(upPNo-1)%8+1;//组号为1-32
				uplink.put("PNo", upPNo);
				uplink.put("SNoA",upOnOrder);
				uplink.put("SNoB",upOffOrder);
				uplink.put("SwitchPos",upSwitchCode);//上行配对组光开关状态
				//下行在线光路
				Routes routeDownOn=findService.findRoute(idDownlinks.get(i)[0]);
				int downOnOrder=routeDownOn.getRtu_port_order();//上行在线链路端口序号
				/**光开关状态：
				 *   16表示1-3相连  即为A2,B2,C2,D2为在线  端口序号为5-8
				 *   96表示1-2相连  即为A1,B1,C1,D1为在线  端口序号为1-4
				 *   **/
				String downSwitchCode="16";//光开关连接状态
				if((downOnOrder-1)%8<4){//端口序号为1-4在线，96
					if(routeDownOn.getIs_online()){
						downSwitchCode="96";
					}
				}
				else{//端口号为5-8
					if(!routeDownOn.getIs_online()){//端口序号为1-4在线，96
						downSwitchCode="96";
					}
				}
				int downOffOrder=findService.findRoute(idDownlinks.get(i)[1]).getRtu_port_order();//上行在备纤链路端口序号
				int downPNo=Math.min(downOffOrder, downOnOrder);//下行配对组序号
				downPNo=(routeDownOn.getRtu_model_order()-1)*4+(downPNo-1)%8+1;//组号为1-32
				downlink.put("PNo", downPNo);
				downlink.put("SNoA",downOnOrder);
				downlink.put("SNoB",downOffOrder);
				downlink.put("SwitchPos",downSwitchCode);//下行配对组光开关状态
				protectGroup.setRouteUpOnId(idUplinks.get(i)[0]);
				protectGroup.setRouteUpOnName(routeUpOn.getRoute_name());
				protectGroup.setRouteUpOffId(idUplinks.get(i)[1]);
				protectGroup.setRouteUpOffName(findService.findRoute(idUplinks.get(i)[1]).getRoute_name());
				protectGroup.setRouteDownOnId(idDownlinks.get(i)[0]);
				protectGroup.setRouteDownOnName(routeDownOn.getRoute_name());
				protectGroup.setRouteDownOffId(idDownlinks.get(i)[1]);
				protectGroup.setRouteDownOffName(findService.findRoute(idDownlinks.get(i)[1]).getRoute_name());
				protectGroup.setRtuMasterId(rtuId);
				protectGroup.setRtuSlaveId(slaveRtuId);
				protectGroup.setRtuMasterName(routeUpOn.getRtu_name());
				protectGroup.setRtuSlaveName(findService.findRtu(slaveRtuId).getRtu_name());
				protectGroup.setDownSwitchPos(downSwitchCode);
				protectGroup.setUpSwitchPos(upSwitchCode);
				protectGroups.add(protectGroup);//暂存保护组
				oneGroupPara.put("Recv", downlink);//存放下行组
				oneGroupPara.put("Send", uplink);//存放上行组
				oneGroupPara.put("IP", findService.findRtu(routeUpOn.getSwitchRtuId()).getRtu_url());//存放保护-从模块的所在RTU的IP
				oneGroupPara.put("ModNo", (routeUpOn.getSwitchRtuOrder()-1)/8+1);//存放保护-从模块的模块序号
				oneGroupPara.put("ConnectPos", connectPos);//主-从模块连接状态
				groupList.add(oneGroupPara);//存放一条记录
			}
			protectGroupsPara.put("CLP", findService.findRtu(rtuId).getStation().getId());
			protectGroupsPara.put("CM",rtuId);
			protectGroupsPara.put("PN",groupList.size());
			protectGroupsPara.put("groupList",groupList);
			String setProtectCode=XmlCodeCreater.setProtectGroup(protectGroupsPara);
			Map<String,Object> responseData=new LinkedHashMap<String, Object>();
			String rtuUrl="http://"+findService.findRtu(rtuId).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
			try {
				String fileName=NumConv.createVerifyCode(20);
				String responseFile="setGroupResponse"+fileName+".xml";
				String testDecode="testDecode"+fileName+".xml";
				HttpClientUtil.Post(rtuUrl, setProtectCode, responseFile, 1500, 3000);
				XmlDom XmlDom=new XmlDom();
				responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);//调用文件解析函数，获取回传数据
				 if(responseData.get("StatusCode").equals("0")){
					 status=true;
				 }
				 else{
					 responseData.put("err", "RTU回复异常，请稍后重试。若一直出现该故障，请检查RTU状态是否正常。");
					 status=false;
				 }
			}  catch (HttpException | IOException e) {
				// TODO Auto-generated catch block
				status=false;
				 responseData.put("err", "与RTU连接超时，请稍后重试。若一直出现该故障，请检查RTU状态是否正常。");
				e.printStackTrace();
			}
			catch(DocumentException|NullPointerException e){
				status=false;
				 responseData.put("err", "RTU回复异常，请稍后重试。若一直出现该故障，请检查RTU状态是否正常。");
			}
			if(status){//下发成功，存储保护组
				for(Protect_groups group:protectGroups){
					Serializable id=addService.addProtectGroup(group);
					if(id==null){
						status=false;
						break;
					}
				}
			}
		    if(status){//存放成功，更改该光路的字段
		    	//将对应的光路的被保护的字段设置为true
		    	String operate="";
				for(int index=0;index<idUplinks.size();index++){
					Routes routeAOn = findService.findRoute(idUplinks.get(index)[0]);
					routeAOn.setIsProtect(true);
					status=alterService.alterRoute(routeAOn);
					Routes routeAOff = findService.findRoute(idUplinks.get(index)[1]);
					routeAOff.setIsProtect(true);
					status=alterService.alterRoute(routeAOff);
					Routes routeBOn=findService.findRoute(idDownlinks.get(index)[0]);
					routeBOn.setIsProtect(true);
					status=alterService.alterRoute(routeBOn);
					Routes routeBOff=findService.findRoute(idDownlinks.get(index)[1]);
					routeBOff.setIsProtect(true);
					status=alterService.alterRoute(routeBOff);
					operate+="上行："+routeAOn.getRoute_name()+"—"+routeAOff.getRoute_name()+""+
							 "下行："+routeBOn.getRoute_name()+"—"+routeBOff.getRoute_name();
					Log log=new Log();
					log.setDate(NumConv.currentTime());
					log.setOperateDetail("设置保护组，"+operate);
			        log.setUser(account);
			        log.setResourceName(operate);
				    log.setResourceType("业务切换");
				    addService.addLog(log);
				}
			}
		    response.setContentType("text/xml");
			response.setCharacterEncoding("utf-8");
			responseData.put("status", status);
	    	JSONArray responseD =JSONArray.fromObject(responseData);//将回传数据封装为JSON格式
			PrintWriter out = response.getWriter();
			//将数据返回到前端
		    out.print(responseD); 
		    out.flush();
		    out.close();
	}
	/**保护组的保护参数设置，包含一个保护组四条光路的测试参数和门限参数
	 * 另外：为了使得启动保护组，在下发参数后下发一条配置保护组的命令
	 * **/
	@RequestMapping("switch/setGroupPara")
    public void setGroupPara(HttpServletRequest request,HttpServletResponse response) throws IOException{
		ResourceController.printAllRequestPara(request);
		Map<String, Object> alarmPara =new LinkedHashMap<String, Object>();
		JSONArray thresholdArray=JSONArray.fromObject(request.getParameter("threshold"));
		JSONArray testParaArray=JSONArray.fromObject(request.getParameter("testPara"));
		////System.out.println("thresholdArray size:"+thresholdArray.size());
		String err="";
		Map<String,Object> responseData=new LinkedHashMap<String, Object>();
		boolean status=false;
		if(thresholdArray.size()>0){
			List<Long> routeIds=new ArrayList<Long>();//光端口号
			String[] threKeys={
					         "ASNo","TYPE","AT01","AT02","AT03","AT04",
					         "AT05","AT06","T3","T4",
					       };
			String[] testKeys={
					           "PS","P21","P22","P23","P24","P25","P26",
					           "P27","IP01","IP02","IP03","IP04","IP05", "IP06",
							 };
			List<Map<String,Object>>threshodsList=new ArrayList<Map<String,Object>>();////一个告警组下所有光路的门限
			for(int i=0;i<thresholdArray.size();i++){
				Map<String, Object>paraMap=new LinkedHashMap<String, Object>();//一条光路的参数
				JSONArray threPara=thresholdArray.getJSONArray(i);//获取一条光路的门限参数信息
				JSONArray testPara=testParaArray.getJSONArray(i);//获取一条光路的测试参数信息
				Long routeId=Long.parseLong(threPara.getString(0));//光路ID
				routeIds.add(routeId);//存放光路ID
				paraMap.put(threKeys[0],findService.findRoute(routeId).getRtu_port_order());//存放光端口号
				/**存放门限参数信息**/
				for(int threIndex=1;threIndex<threPara.size();threIndex++){//获取各参数
					paraMap.put(threKeys[threIndex], threPara.getString(threIndex));
				}
				/***存放测试参数信息*/
				for(int index=0;index<testKeys.length;index++){//获取各参数
					if(index<=9){
						paraMap.put(testKeys[index], testPara.getString(index));
					}
					else{//IP3-IP6填充*
						paraMap.put(testKeys[index],"*");
					}
				}
				threshodsList.add(paraMap);//放入一条光路的门限到列表中
			}
			Map<String,Object> groupMap=new LinkedHashMap<String, Object>();//一个告警组的参数
			groupMap.put("ANo",1);
			groupMap.put("ASN", threshodsList.size());
			groupMap.put("threshodsList", threshodsList);
			List<Object> groupList=new ArrayList<Object>();//所有告警组的参数
			groupList.add(groupMap);
			alarmPara.put("groupList", groupList);
			Routes route=findService.findRoute(routeIds.get(0));
			Rtus rtu=findService.findRtu(route.getRtu_id());
			alarmPara.put("CM",rtu.getId());
			alarmPara.put("CLP", rtu.getStation().getId());
			alarmPara.put("AN", 1);
			String  setAlarmTestCode=XmlCodeCreater.setAlarmTestCode(alarmPara);//生成xml格式优化参数命令
			////System.out.println("XML文件为：\n"+setAlarmTestCode);
			String rtuUrl="http://"+rtu.getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
			status=false;
			try {
				String fileName=NumConv.createVerifyCode(20);
				String responseFile="obstacleResponse"+fileName+".xml";
				String testDecode="testDecode"+fileName+".xml";	
				HttpClientUtil.Post(rtuUrl, setAlarmTestCode, responseFile, 2500, 2500);
				XmlDom XmlDom=new XmlDom();
				responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);//调用文件解析函数，获取回传数据
				if(responseData.get("StatusCode").equals("0")){//下发取消命令成功，更改该光路的字段
					/***为了使得保护组成功启动，在配置参数后再发送一次配置保护组的命令**/
					List<Map<String,Object>>protectList=new ArrayList<Map<String,Object>>();
					Map<String,Object>protectGroupsPara=new LinkedHashMap<String,Object>();
					Map <String,Object> oneGroupPara=new LinkedHashMap<String, Object>();//一条保护组参数
					Map <String,Object> uplink=new LinkedHashMap<String, Object>();//上行配对组参数
					Map <String,Object> downlink=new LinkedHashMap<String, Object>();//下行配对组参数
					Protect_groups protectGroup=findService.findProtectGroupsByRouteId(route.getId());
					Routes routeDownOn=findService.findRoute(protectGroup.getRouteDownOnId());
					int downOnOrder=routeDownOn.getRtu_port_order();
					int downOffOrder=findService.findRoute(protectGroup.getRouteDownOffId()).getRtu_port_order();
					Routes routeUpOn=findService.findRoute(protectGroup.getRouteUpOnId());
					int upOnOrder=routeUpOn.getRtu_port_order();
					int upOffOrder=findService.findRoute(protectGroup.getRouteUpOffId()).getRtu_port_order();
					int downPNo=Math.min(downOffOrder, downOnOrder);//下行配对组序号
					downPNo=(routeDownOn.getRtu_model_order()-1)*4+(downPNo-1)%8+1;//组号为1-32
					downlink.put("PNo", downPNo);
					downlink.put("SNoA",downOnOrder);
					downlink.put("SNoB",downOffOrder);
					downlink.put("SwitchPos",protectGroup.getDownSwitchPos());//下行配对组光开关状态
					int upPNo=Math.min(upOffOrder, upOnOrder);//上行配对组序号
					upPNo=(routeUpOn.getRtu_model_order()-1)*4+(upPNo-1)%8+1;//组号为1-32
					uplink.put("PNo", upPNo);
					uplink.put("SNoA",upOnOrder);
					uplink.put("SNoB",upOffOrder);
					uplink.put("SwitchPos",protectGroup.getUpSwitchPos());//上行配对组光开关状态
					oneGroupPara.put("Recv", downlink);//存放下行组
					oneGroupPara.put("Send", uplink);//存放上行组
					oneGroupPara.put("IP", findService.findRtu(route.getSwitchRtuId()).getRtu_url());//存放保护-从模块的所在RTU的IP
					oneGroupPara.put("ModNo", (routeUpOn.getSwitchRtuOrder()-1)/8+1);//存放保护-从模块的模块序号
					String connectPos="0";//主-从连接模式 0表示A-A B-B；1表示A-C B-D
					if((upOnOrder-1)%8<4){//端口序号为1-4在线，96
						if((routeUpOn.getSwitchRtuOrder()-1)%8>3){//A-C B-D相连模式
							connectPos="1";
						}
					}
					else{//端口号为5-8
						if((routeUpOn.getSwitchRtuOrder()-1)%8<4){//C-A D-B相连模式
							connectPos="1";
						}
					}
					oneGroupPara.put("ConnectPos", connectPos);//主-从模块连接状态
					protectList.add(oneGroupPara);//存放一条记录
					protectGroupsPara.put("CLP", rtu.getStation().getId());
					protectGroupsPara.put("CM",rtu.getId());
					protectGroupsPara.put("PN",1);
					protectGroupsPara.put("groupList",protectList);
					String setProtectCode=XmlCodeCreater.setProtectGroup(protectGroupsPara);
					////System.out.println("==========setProtectCode========："+setProtectCode);
					rtuUrl="http://"+findService.findRtu(rtu.getId()).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
					try {
						fileName=NumConv.createVerifyCode(20);
						responseFile="setGroupResponse"+fileName+".xml";
						HttpClientUtil.Post(rtuUrl, setProtectCode, responseFile, 2000, 3000);
						responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);//调用文件解析函数，获取回传数据
						 if(responseData.get("StatusCode").equals("0")){
							 status=true;
						 }
						 else{
							 responseData.put("err", "RTU回复异常，请稍后重试。若一直出现该故障，请检查RTU状态是否正常。");
							 status=false;
						 }
					}  catch (HttpException | IOException e) {
						// TODO Auto-generated catch block
						status=false;
						 responseData.put("err", "与RTU连接超时，请稍后重试。若一直出现该故障，请检查RTU状态是否正常。");
						e.printStackTrace();
					}
					catch(DocumentException|NullPointerException e){
						status=false;
						 responseData.put("err", "RTU回复异常，请稍后重试。若一直出现该故障，请检查RTU状态是否正常。");
					}
		    		if(status){
		    			String operate="";
			    		for(int i=0;i<routeIds.size();i++){//修改数据库的字段
			 		    	route=findService.findRoute(routeIds.get(i));
			 		    	route.setIsObstalce(true);
			 		    	operate+=route.getRoute_name()+",";
			 		    	alterService.alterRoute(route);
			 		    }
			    		Log log=new Log();
			 			log.setDate(NumConv.currentTime());
			 			log.setOperateDetail("保护组配置保护参数");
			 			Subject currentUser = SecurityUtils.getSubject();//获取当前用户
			          	String Account=currentUser.getPrincipal().toString();//当前用户的账号
			          	log.setUser(Account);
			            log.setResourceName(operate);
			 			log.setResourceType("业务切换");
			 			addService.addLog(log);
		    		}
		      }else{
		    	   status=false;
		    	   err="RTU回复异常，请稍后重试，若一直出现该故障，请检查RTU状态是否正常。";
		    	   responseData.put("err",err);
		       }
			}catch (SocketTimeoutException|ConnectTimeoutException e) {
				// TODO Auto-generated catch block
				status=false;
				err="与RTU连接超时，若一直出现该故障，请检查RTU状态是否正常";
				responseData.put("err",err);
				e.printStackTrace();
			} catch (HttpException|IOException|DocumentException|NullPointerException e) {
				// TODO Auto-generated catch block
				 status=false;
				 err="与RTU通信故障，请检查RTU状态是否正常";
				 responseData.put("err",err);
				e.printStackTrace();
			}
		}
		 //解析Rtu服务器端的回复码
     	PrintWriter out=response.getWriter();
    	responseData.put("status", status);
    	JSONArray responseD =JSONArray.fromObject(responseData);//将回传数据封装为JSON格式
    	/*将数据返回到前端*/
    	out.print(responseD);// 
    	out.flush();
    	out.close();
	}	
	/**
	* 切换光路
	* @throws IOException **/
	@RequestMapping("switch/fiberSwitch")
	public void fiberSwitch(HttpServletRequest request,HttpServletResponse response) throws IOException{
			Long groupId=Long.parseLong(request.getParameter("groupId"));
			Protect_groups group =findService.findProtectGroupById(groupId);
			Map<String,Object> responseData=new LinkedHashMap<String, Object>();
			String switchStatus=group.getDownSwitchPos().equals("96")?"16":"96";//要切换到的状态 
			List<Object> groupList=new ArrayList<>();
			boolean status=false;
			Map<String,Object> groupPara=new LinkedHashMap<String, Object>();
			Map<String,Object> groupMap=new LinkedHashMap<String,Object>();
			Routes routeDownOn=findService.findRoute(group.getRouteDownOnId());
			Routes routeDownOff=findService.findRoute(group.getRouteDownOffId());
			groupMap.put("SwitchPos",switchStatus);
			int orderA=routeDownOn.getRtu_port_order();
			int orderB=routeDownOff.getRtu_port_order();
			int PNo=Math.min(orderA,orderB);//配对组序号
			PNo=(PNo-1)%8+1;//组号为1-4 即为单向配对组中小的端口序号(1-8编号)
			groupMap.put("PNo",PNo);
			groupMap.put("SNoA",orderA);
			groupMap.put("SNoB",orderB);
			Long rtuId=routeDownOn.getRtu_id();
			groupList.add(groupMap);
			groupPara.put("CM", rtuId);
			groupPara.put("PN", groupList.size());
			groupPara.put("CLP",findService.findRtu(rtuId).getStation().getId());
			groupPara.put("groupList", groupList);
			String requestProtectCode=XmlCodeCreater.requestRouteProtect(groupPara);
			String rtuUrl="http://"+findService.findRtu(rtuId).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
			try {  
					String fileName=NumConv.createVerifyCode(20);
					String responseFile="routeSwitchResponse"+fileName+".xml";
					String testDecode="testDecode"+fileName+".xml";
					HttpClientUtil.Post(rtuUrl, requestProtectCode,responseFile,2000,3000);
					XmlDom XmlDom=new XmlDom();	
					responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);//调用文件解析函数，获取回传数据
					if(responseData.get("StatusCode").equals("0")){//发送成功
						status=true;
					}
					else{
						  status=false;
						  responseData.put("err","RTU回复异常。");
					}
			} catch (HttpException|IOException | DocumentException e) {
					responseData.put("err","RTU连接超时，请稍后重试。若一直出现该故障，请检查RTU状态是否正常。");
					e.printStackTrace();
			}
			/**记录切换日志**/
			Log log=new Log();
			log.setDate(NumConv.currentTime());
			Subject currentUser = SecurityUtils.getSubject();//获取当前用户
	        String Account=currentUser.getPrincipal().toString();//当前用户的账号
	        log.setUser(Account);
	        log.setResourceType("业务切换");
	        String detail="";
			String operate="";
			detail+="下行："+routeDownOn.getRoute_name()+"由在线业务纤切换为备纤，"+routeDownOff.getRoute_name()+"由备纤切换为业务纤;"+
			        "上行："+group.getRouteDownOnName()+"由在线业务纤切换为备纤，"+group.getRouteDownOffName()+"由备纤切换为业务纤;";
			operate+=routeDownOn.getRoute_name()+","+routeDownOff.getRoute_name();
	        String groupStatus=status?"切换成功":"切换失败。"; 
			detail+="切换结果：切换成功，光开关状态为："+groupStatus;
			log.setOperateDetail("手动切换保护组光纤:"+detail);
			log.setResourceName(operate);
			addService.addLog(log);
			 /**切换成功,更改光路状态
			   * 和保护组状态    平行交叉互换
			   * 在线纤 备纤互换*/
			if(status){
				String switchPos=group.getDownSwitchPos().equals("16")?"96":"16";
				group.setDownSwitchPos(switchPos);
				Long downOff=group.getRouteDownOffId();
				Long downOn=group.getRouteDownOnId();
				Long upOff=group.getRouteUpOffId();
				Long upOn=group.getRouteUpOnId();
				group.setRouteDownOffId(downOn);
				group.setRouteDownOnId(downOff);
				group.setRouteUpOffId(upOn);
				group.setRouteUpOnId(upOff);
				String upOnName=group.getRouteUpOnName();
				String upOffName=group.getRouteUpOffName();
				group.setRouteDownOffName(routeDownOn.getRoute_name());
				group.setRouteDownOnName(routeDownOff.getRoute_name());
				group.setRouteUpOffName(upOnName);
				group.setRouteUpOnName(upOffName);
				alterService.alterProtectGroup(group);//修改保护组光开关状态
				routeDownOn.setIs_online(false);
				alterService.alterRoute(routeDownOn);
				routeDownOff.setIs_online(true);
				alterService.alterRoute(routeDownOff);//修改光路状态
				Routes routeUpOn=findService.findRoute(upOn);
				Routes routeUpOff=findService.findRoute(upOff);
				routeUpOn.setIs_online(false);
				routeUpOff.setIs_online(true);
				alterService.alterRoute(routeUpOn);//修改光路状态
				alterService.alterRoute(routeUpOff);//修改光路状态
			}
			responseData.put("status", status);
			response.setContentType("text/xml");
			response.setCharacterEncoding("utf-8");
			JSONArray responseD =JSONArray.fromObject(responseData);//将回传数据封装为JSON格式
	    	PrintWriter out=response.getWriter();
	    	/**将数据返回到前端*/
	    	out.print(responseD);// 
	    	out.flush();
	    	out.close();
 }
	/**删除配对组*/
	@RequestMapping("switch/delSwitchGroup")
	 public void routeMatchDelete(HttpServletRequest request,HttpServletResponse response) throws IOException{
			    JSONArray groupIds=JSONArray.fromObject(request.getParameter("selectMatchId"));
			    Long rtuId=Long.parseLong(request.getParameter("rtuId"));
				Map<String,Object> responseData=new LinkedHashMap<String, Object>();
			    boolean status=false;
				List<Integer> QNs=new ArrayList<Integer>();
				for(int index=0;index<groupIds.size();index++){
					 Protect_groups group=findService.findProtectGroupById(Long.parseLong(groupIds.getString(index)));
					 Routes routeDownOn=findService.findRoute(group.getRouteDownOnId());
					 int downOnOrder=routeDownOn.getRtu_port_order();
					 int downOffOrder=findService.findRoute(group.getRouteDownOffId()).getRtu_port_order();
					 int PNo=Math.min(downOnOrder,downOffOrder);//配对组序号
					 PNo=(routeDownOn.getRtu_model_order()-1)*4+(PNo-1)%8+1;//组号为1-32
					 QNs.add(PNo);
				}
				Map<String,Object> CancelPara=new LinkedHashMap<String,Object>();
				CancelPara.put("CM", rtuId);
				CancelPara.put("CLP", findService.findRtu(rtuId).getStation().getId());
				CancelPara.put("QNs", QNs);
				String cancelCode=XmlCodeCreater.cancelProtectGroup(CancelPara);
				////System.out.println("-------------XmlCodeCreater--------:\n"+cancelCode);
				String rtuUrl="http://"+findService.findRtu(rtuId).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
				String fileName=NumConv.createVerifyCode(20);
				String responseFile="cancelGroupResponse"+fileName+".xml";
				String testDecode="testDecode"+fileName+".xml";
				try {
					 HttpClientUtil.Post(rtuUrl, cancelCode, responseFile,3500,4000);
					 XmlDom XmlDom=new XmlDom(); 
					 responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);//调用文件解析函数，获取回传数据
					 if(responseData.get("StatusCode").equals("0")){
						 status=true;
					 }
					 else{
						 status=false;
					 }
				}  catch (HttpException | IOException e) {
					// TODO Auto-generated catch block
					 status=false;
					 responseData.put("err", "与RTU连接超时，请稍后重试。若一直出现该故障，请检查RTU状态是否正常。");
					e.printStackTrace();
				}
				catch(DocumentException|NullPointerException e){
					status=false;
					 responseData.put("err", "RTU回复异常，请稍后重试。若一直出现该故障，请检查RTU状态是否正常。");
				}
				if(status){//下发成功，删除配对
					Log log=new Log();
					log.setDate(NumConv.currentTime());
					Subject currentUser = SecurityUtils.getSubject();//获取当前用户
			        String Account=currentUser.getPrincipal().toString();//当前用户的账号
			        log.setUser(Account);
			        String operate="";
			        for(int i = 0; i<groupIds.size();i++){
			        	    Long groupId=Long.parseLong(groupIds.getString(i));
			        	    Protect_groups group=findService.findProtectGroupById(groupId);
							//还原光路是否配对的字段
							Routes routeUpOn = findService.findRoute(group.getRouteUpOnId());
							routeUpOn.setIsProtect(false);
							alterService.alterRoute(routeUpOn);
							Routes routeUpOff = findService.findRoute(group.getRouteUpOffId());
							routeUpOff.setIsProtect(false);
							alterService.alterRoute(routeUpOff);
							Routes routeDownOn = findService.findRoute(group.getRouteDownOnId());
							routeDownOn.setIsProtect(false);
							alterService.alterRoute(routeDownOn);
							Routes routeDownOff = findService.findRoute(group.getRouteDownOffId());
							routeDownOff.setIsProtect(false);
							alterService.alterRoute(routeDownOff);
							operate+="上行:"+routeUpOn.getRoute_name()+"—"+routeUpOff.getRoute_name()+";"+
									 "下行："+routeDownOn.getRoute_name()+"—"+routeDownOff.getRoute_name()+";";
							delService.deleteProtectGroup(groupId);//删除配对
					}
			       log.setResourceName(operate);
				   log.setResourceType("业务切换");
				   log.setOperateDetail("删除配对组。");
				   addService.addLog(log);
				}
			    response.setContentType("text/xml");
				response.setCharacterEncoding("utf-8");
				responseData.put("status", status);
		    	JSONArray responseD =JSONArray.fromObject(responseData);//将回传数据封装为JSON格式
		    	PrintWriter out=response.getWriter();
				/*将数据返回到前端*/
			    out.print(responseD);// 
			    out.flush();
			    out.close();
	}
	/**-------------实时获取光功率值------------*/
	@RequestMapping("degradation/checkPowerValue")
	public void getOpticalPower(HttpServletRequest request,HttpServletResponse response) throws IOException{
		    Long routeId=Long.parseLong(request.getParameter("routeId"));
		    Routes route=findService.findRoute(routeId);
			Long CM=route.getRtu_id();
			int SNo=route.getRtu_port_order();
			Long CLP=findService.findRtu(CM).getStation().getId();
			Map<String,Object> paraMap=new LinkedHashMap<String,Object>();
			paraMap.put("CLP",CLP);
			paraMap.put("CM",CM);
			paraMap.put("SNo",SNo); 
			String xmlCode=XmlCodeCreater.getOpticalValue(paraMap);
			String fileName=NumConv.createVerifyCode(20);
			String responseFile="rightTimePwResponse"+fileName+".xml";
			String testDecode="testDecode"+fileName+".xml";
			String rtuUrl="http://"+findService.findRtu(CM).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
			String err="";
			Map<String,Object> responseData=new LinkedHashMap<String,Object>();
			boolean status=false;
			try {
				XmlDom XmlDom=new XmlDom();
				HttpClientUtil.Post(rtuUrl, xmlCode, responseFile, 2000, 1500);
				/**------
				 * responseData中存在的键值对为
				 * StatusCode
				 * err
				 * testTime:测试时间(StatusCode为0时才存在)
				 * opticalPower：测得的光功率值(StatusCode为0时才存在)
				 * ------*/ 
				 responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);//调用文件解析函数，获取回传数据
				 if(responseData.get("StatusCode").equals("10")){//下发测试命令成功且有数据反馈
						status=true;
				 }
				 else{
					 status=false;
					 err="当前光路无法进行光功率采集";
				 }
			  } catch (SocketTimeoutException|ConnectTimeoutException|HttpException e) {
				// TODO Auto-generated catch block
				status=false;
				err="RTU通信故障，与RTU建立连接超时，请检查RTU的状态是否正常";
				e.printStackTrace();
			} catch (IOException |DocumentException|NullPointerException e) {
				status=false;
				err="RTU回复数据异常，请检查RTU的状态是否正常";
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    responseData.put("status", status);
		    responseData.put("err", err);
		    response.setContentType("text/xml");
			response.setCharacterEncoding("utf-8");
			responseData.put("status", status);
	    	JSONArray responseD =JSONArray.fromObject(responseData);//将回传数据封装为JSON格式
	    	PrintWriter out=response.getWriter();
	    	/*将数据返回到前端*/
	    	out.print(responseD);// 
	    	out.flush();
	    	out.close();
	}
	/**-------------获取RTU时间------------
	 * @throws IOException */
	@RequestMapping("masterCon/getRtuTime")
	public void getRtuTime(HttpServletRequest request,HttpServletResponse response) throws IOException{
		    Long CM=Long.parseLong(request.getParameter("rtuId"));
		    ////System.out.println("CM:"+CM);
			Long CLP=findService.findRtu(CM).getStation().getId();
			Map<String,Object> paraMap=new LinkedHashMap<String,Object>();
			paraMap.put("CLP",CLP);
			paraMap.put("CM",CM);
			String xmlCode=XmlCodeCreater.getRtuTime(paraMap);
			////System.out.println("XML:\n"+xmlCode);
			Map<String,Object> responseData=new LinkedHashMap<String, Object>();
			boolean status=false;
			String rtuUrl="http://"+findService.findRtu(CM).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
			try {
				String fileName=NumConv.createVerifyCode(20);
				String responseFile="getRtuTimeResponse"+fileName+".xml";
				String testDecode="testDecode"+fileName+".xml";
				HttpClientUtil.Post(rtuUrl,xmlCode,responseFile,1500,2000);
				XmlDom XmlDom=new XmlDom();
				responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);//调用文件解析函数，获取回传数据
				if(responseData.get("StatusCode").equals("11")){//下发测试命令成功且有数据反馈
					status=true;
				}
				else{
					String err="RTU回复异常，请稍后重试。";
					responseData.put("err",err);
				}
			 } catch (HttpException | DocumentException | IOException|NullPointerException e) {
				status=false;
				String err="RTU通信异常";
				responseData.put("err",err);
				// TODO Auto-generated catch block
				e.printStackTrace();//打印错误信息
			}
			responseData.put("status", status);
		    response.setContentType("text/xml");
			response.setCharacterEncoding("utf-8");
			responseData.put("status", status);
	    	JSONArray responseD =JSONArray.fromObject(responseData);//将回传数据封装为JSON格式
	    	PrintWriter out=response.getWriter();
	    	/*将数据返回到前端*/
	    	out.print(responseD);// 
	    	out.flush();
	    	out.close();
	}
	
	/**-------------设置RTU时间------------
	 * @throws IOException 
	 * @throws ParseException */
	@RequestMapping("masterCon/setRtuTime")
	public void setRtuTime(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
		    Long CM=Long.parseLong(request.getParameter("rtuId"));
		    ////System.out.println("CM:"+CM);
			Long CLP=findService.findRtu(CM).getStation().getId();
			Map<String,Object> paraMap=new LinkedHashMap<String,Object>();
			String T8=NumConv.dateToUnixStamp(request.getParameter("time"));
			paraMap.put("CLP",CLP);
			paraMap.put("CM",CM);
			paraMap.put("T8",T8);
			////System.out.println("time:"+request.getParameter("time")+"\t stamp:"+NumConv.stampToDate(T8));
			String xmlCode=XmlCodeCreater.adjustRtuTime(paraMap);
			////System.out.println("XML:\n"+xmlCode);
			Map<String, Object> responseData=new LinkedHashMap<String, Object>();
			boolean status=false;
			Rtus rtu=findService.findRtu(CM);
			String rtuUrl="http://"+rtu.getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
			String fileName=NumConv.createVerifyCode(20);
			String responseFile="setRtuTimeResponse"+fileName+".xml";
			String testDecode="testDecode"+fileName+".xml";
			try {
				HttpClientUtil.Post(rtuUrl,xmlCode,responseFile,2000,2000);
				XmlDom XmlDom=new XmlDom();
				responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);//调用文件解析函数，获取回传数据
				if(responseData.get("StatusCode").equals("0")){//下发成功
					status=true;
				}
				else{
					String err="RTU回复异常，请稍后重试。若一直出现该故障，请检查RTU状态是否正常。";
					responseData.put("err",err);
				}
			 } catch (HttpException |IOException e) {
				status=false;
				String err="RTU通信异常,与RTU建立连接超时，请检查RTU状态是否正常。";
				responseData.put("err",err);
				// TODO Auto-generated catch block
				e.printStackTrace();//打印错误信息
			}catch( DocumentException | NullPointerException e){
				status=false;
				String err="RTU回复异常，请检查RTU状态是否正常。";
				responseData.put("err",err);
			}
			Log log=new Log();
			log.setDate(NumConv.currentTime());
			Subject currentUser = SecurityUtils.getSubject();//获取当前用户
	        String Account=currentUser.getPrincipal().toString();//当前用户的账号
	        log.setUser(Account);
	        log.setResourceName(rtu.getRtu_name());
			log.setResourceType("RTU");
			String detail="刷新RTU的时间，完成定时同步。操作结果：";
			if(status){
				detail+="操作成功。";
			}
			else{
				detail+="操作失败，失败原因："+"RTU通信异常，与RTU建立连接超时。";
			}
			log.setOperateDetail(detail);
			addService.addLog(log);
			responseData.put("status", status);
		    response.setContentType("text/xml");
			response.setCharacterEncoding("utf-8");
	    	JSONArray responseD =JSONArray.fromObject(responseData);//将回传数据封装为JSON格式
	    	PrintWriter out=response.getWriter();
	    	/*将数据返回到前端*/
	    	out.print(responseD);// 
	    	out.flush();
	    	out.close();
	}
	
	/**-------------获取RTU IP地址------------
	 * @throws IOException 
	 * @throws ParseException */
	@RequestMapping("masterCon/getRtuIp")
	public void getRtuIp(HttpServletRequest request,HttpServletResponse response) throws IOException{
		    Long CM=Long.parseLong(request.getParameter("rtuId"));
		    boolean status=false;
		    Map<String,Object> responseData=new LinkedHashMap<String,Object>();
		    String err="";
		    Rtus rtu=findService.findRtu(CM);
		    if(rtu!=null){
		    	if(rtu.getType().contains("普通")){//普通RTU
		    		responseData.put("type",1);//RTU类型 1为切换RTU 0为普通
		    		Long CLP=findService.findRtu(CM).getStation().getId();
					Map<String,Object> paraMap=new LinkedHashMap<String,Object>();
					paraMap.put("CLP",CLP);
					paraMap.put("CM",CM);
					String xmlCode=XmlCodeCreater.getRtuNetwork(paraMap);
					String rtuUrl="http://"+findService.findRtu(CM).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
					String fileName=NumConv.createVerifyCode(20);
					String responseFile="getRtuIpResponse"+fileName+".xml";
					String testDecode="testDecode"+fileName+".xml";
					try {
							HttpClientUtil.Post(rtuUrl,xmlCode,responseFile,2000,3000);
							status=true;
					} 
					catch(ConnectTimeoutException | HttpException e){
							// TODO Auto-generated catch block
							status=false;
						    err="RTU通信异常,与RTU建立连接超时，请检查RTU的状态是否正常。";
							// TODO Auto-generated catch block
							e.printStackTrace();//打印错误信息
					}
					if(status){//通信正常再解析文件
						try {
							XmlDom XmlDom=new XmlDom();
							responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);
							//调用文件解析函数，获取回传数据
							if(responseData.get("StatusCode").equals("12")){//获取成功
								status=true;
							/**回传数据包括IP，netMask,gateway**/
							}
							else{
								status=false;
								err="RTU回复异常，请稍后重试。若一直出现该故障，请检查RTU状态是否正常。";
							}
						} catch (DocumentException | HttpException|NullPointerException e) {
							// TODO Auto-generated catch block
							status=false;
							err="与RTU通信过程中RTU回复数据异常，请检查RTU的状态是否正常。";
							e.printStackTrace();
						}
					 
					}
				}
		    	else{//切换RTU
		    		status=true;
		    		String ip=rtu.getRtu_url();
		    		responseData.put("IP",ip);
		    		responseData.put("netMask", "255.255.255.0");
		    		/**拼接网关地址**/
		    		String[] ips=ip.split("\\.");
		    		String gateway="";
		    		for(int i=0;i<ips.length-1;i++){
		    			gateway+=ips[i]+".";
		    		}
		    		gateway+="0";
		    		responseData.put("gateway",gateway);
		    		responseData.put("type",1);//RTU类型 1为切换RTU
		    	}
		    }
			
			responseData.put("status", status);
			responseData.put("err",err);
			response.setContentType("text/xml");
			response.setCharacterEncoding("utf-8");
	    	JSONArray responseD =JSONArray.fromObject(responseData);//将回传数据封装为JSON格式
	    	////System.out.println("response:"+responseD);
	    	PrintWriter out=response.getWriter();
	    	/*将数据返回到前端*/
	    	out.print(responseD);// 
	    	out.flush();
	    	out.close();
	}
	
	/**-------------设置RTU IP地址------------
	 * @throws IOException 
	 * @throws ParseException */
	@RequestMapping("masterCon/setRtuIp")
	public void setRtuIp(HttpServletRequest request,HttpServletResponse response) throws IOException{
		    Long CM=Long.parseLong(request.getParameter("rtuId"));
		    Rtus rtu=findService.findRtu(CM);
		    Long CLP=rtu.getStation().getId();
			Map<String,Object> responseData=new LinkedHashMap<String, Object>();
		    boolean status=false;//
			String IP=request.getParameter("ipv4");
			String submask=request.getParameter("submask");
			String gateWay=request.getParameter("gateWay");
			if(rtu.getType().contains("普通")){//普通RTU
				Map<String,Object> paraMap=new LinkedHashMap<String,Object>();
				paraMap.put("CLP",CLP);
				paraMap.put("CM",CM);
				paraMap.put("IP",IP);
				paraMap.put("Gateway",gateWay);
				paraMap.put("subnetMask",submask);
				String xmlCode=XmlCodeCreater.setRtuNetwork(paraMap);
				////System.out.println("XML:\n"+xmlCode);
				responseData=new LinkedHashMap<String, Object>();
				String rtuUrl="http://"+findService.findRtu(CM).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
				String fileName=NumConv.createVerifyCode(20);
				String responseFile="setRtuIpResponse"+fileName+".xml";
				String testDecode="testDecode"+fileName+".xml";	
				XmlDom XmlDom=new XmlDom();
				try {
					  HttpClientUtil.Post(rtuUrl,xmlCode,responseFile,3500,2500);
					  responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);//调用文件解析函数，获取回传数据
					  if(responseData.get("StatusCode").equals("0")){//发送成功
						  /**进行IP获取测试，如果测试通过表明修改完成，修改数据库中的IP地址**/
						  rtuUrl="http://"+IP+":5000/cgi-bin/BoaCom.cgi";
						  xmlCode=XmlCodeCreater.getRtuNetwork(paraMap);
						  ////System.out.println("code:"+xmlCode);
						  try{
							  fileName=NumConv.createVerifyCode(20);
							  responseFile="getRtuNetwork"+fileName+".xml";
							  HttpClientUtil.Post(rtuUrl,xmlCode,responseFile,2500,1500);
							  responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);//调用文件解析函数，获取回传数据
							  if(responseData.get("StatusCode").equals("12")){//获取IP成功
									rtu.setRtu_url(IP);
									status=alterService.alterRtu(rtu);
							  }
							  else{
								  String err="RTU回复异常,请稍后重试。若一直出现该故障，请检查RTU状态。";
								   responseData.put("err",err);
							  }
						    }catch (HttpException|IOException|DocumentException|NullPointerException e) {//
								   e.printStackTrace();
								   status=false;
								   String err="RTU通信异常,与RTU建立连接超时或RTU回复数据异常，请检查RTU状态。";
								   responseData.put("err",err);
						}
					 }
				 } catch (HttpException|IOException|DocumentException e) {//修改IP地址后一定会出现read time超时，因为此时IP已经修改
					     // e.printStackTrace();
					       /**进行IP获取测试，如果测试通过表明修改完成，修改数据库中的IP地址**/
						  rtuUrl="http://"+IP+":5000/cgi-bin/BoaCom.cgi";
						  fileName=NumConv.createVerifyCode(20);
						  responseFile="getRtuNetwork"+fileName+".xml";
						  xmlCode=XmlCodeCreater.getRtuNetwork(paraMap);
						  ////System.out.println("code:"+xmlCode);
						  try{
							  HttpClientUtil.Post(rtuUrl,xmlCode,responseFile,3500,3000);
							  responseData=XmlDom.AnalyseRespondse(responseFile,testDecode);//调用文件解析函数，获取回传数据
							  if(responseData.get("StatusCode").equals("12")){//获取IP成功
									status=true;
							  }
						    }catch (HttpException|IOException|DocumentException e1) {//修改IP地址后一定会出现read time超时，因为此时IP已经修改
								   e1.printStackTrace();
								   status=false;
								   String err="RTU通信异常";
								   responseData.put("err",err);
						}
				}
			}
			else{//切换RTU
				/**更新IP的命令**/
				long SN=(long) 0;
				byte[] code=TCPComndUtil.comndGetSN();
				TcpClient client=new TcpClient(rtu.getRtu_url(),8088,3000);
				while(!client.getIsReady());//等待执行完
				status=client.getStatus();
				if(status){
					client.sendData(code);
					byte []rec=client.recData();//接收数据
					client.closeConn();
					Map<String,Object> para=new LinkedHashMap<String,Object>();
					if((boolean) para.get("status")){
						SN=(long)para.get("SN");
						code=TCPComndUtil.comdSetIP(IP,8088,submask);
						client=new TcpClient(rtu.getRtu_url(),8088,3000);
						while(!client.getIsReady());//等待执行完
						status=client.getStatus();
						if(status){//连接成功
							status=client.sendData(code);
							if(status){
								 rec=client.recData();//接收数据
								 client.closeConn();//发送后释放连接
					    		 ////System.out.println("server response："+NumConv.byteArrayToHex(rec));
					    		 Map<String,Object>recData=TCPComndUtil.anlyzeRecData(rec);
					    		 status=(boolean) recData.get("status");
					    		 ////System.out.println("======="+status);
					    		 /***==================此处采用新的IP地址进行连接测试===================***/
					    		 if(status){//指令下发成功
					    			 /***重启RTU，重启后RTU刷新IP**/
					    			code=TCPComndUtil.comdSetServiceOff(); //重启的指令
					    			client=new TcpClient(rtu.getRtu_url(),8088,3000);
									while(!client.getIsReady());//等待执行完
									status=client.getStatus();
									if(status){//建立连接成功，发送重启指令
										status=client.sendData(code);
										client.closeConn();//发送重启指令不会有回复，直接断线
										try {//等待5s 5s后重新连接
											Thread.sleep(5500);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
						 				 for(int i=1;i<4;i++){//等待5s后进行三次重发连接   如果连接失败 视为IP修改失败
						 					 code=TCPComndUtil.comndGetSN();//测试指令
						 					 client=new TcpClient(IP,8088,3000);//新的IP地址
						 					 while(!client.getIsReady());
							 				 status=client.sendData(code);
							 				 if(status){
							 					 rec=client.recData();//接收数据
												 client.closeConn();//发送后释放连接
												 status=(boolean) recData.get("status");
									    		 if(status){//连接测试成功，读取序列号看是否为开始的序列号
									    			 long SNRe=(long)recData.get("SN");
									    			 if(SNRe==SN){//
									    				 status=true;
									    			 }
									    			 else{
									    				status=false; 
									    				responseData.put("err", "刷新IP后进行重连测试RTU连接失败，已恢复原来IP。");
									    			 }
									    			 break;
									    		 }
							 				 }
							 			 }
									}
					    			
					    		 }
					    		 else{//发送失败
					    			 responseData.put("err", "向RTU发送数据无响应，请检查RTU的状态是否正常。");
					    		 }
							}
						}
						else{//连接失败
							responseData.put("err", "与RTU连接超时，请检查RTU的状态是否正常。");
						}
					}
				}
				else{
					responseData.put("err", "与RTU连接超时，请检查RTU的状态是否正常。");
				}
			}
			Log log=new Log();
			log.setDate(NumConv.currentTime());
			Subject currentUser = SecurityUtils.getSubject();//获取当前用户
	        String Account=currentUser.getPrincipal().toString();//当前用户的账号
	        log.setUser(Account);
	        log.setResourceName(rtu.getRtu_name());
			log.setResourceType("RTU");
			String detail="修改RTU的IP地址。修改结果：";
			/**修改成功**/
			if(status){
				detail+="修改成功。";
				rtu.setRtu_url(IP);
				status=alterService.alterRtu(rtu);
			}
			else{
				detail+="修改失败，失败原因："+"RTU通信异常，与RTU建立连接超时。";
			}
			log.setOperateDetail(detail);
			addService.addLog(log);
			responseData.put("status", status);
		    response.setContentType("text/xml");
			response.setCharacterEncoding("utf-8");
	    	JSONArray responseD =JSONArray.fromObject(responseData);//将回传数据封装为JSON格式
	    	PrintWriter out=response.getWriter();
	    	/*将数据返回到前端*/
	    	out.print(responseD);// 
	    	out.flush();
	    	out.close();
	}
/**重启RTU
 * @throws IOException **/	
	@RequestMapping("masterCon/restart")
	public void restart(HttpServletRequest request,HttpServletResponse response) throws IOException{
		    Long CM=Long.parseLong(request.getParameter("rtuId"));
		    boolean status=false;
		    String err="";
		    Map<String,Object> responseData=new LinkedHashMap<String,Object>();
		    Rtus rtu=findService.findRtu(CM);
		    if(rtu!=null){
		    	if(rtu.getType().contains("普通")){//普通RTU
		    		Long CLP=findService.findRtu(CM).getStation().getId();
					Map<String,Object> paraMap=new LinkedHashMap<String,Object>();
					paraMap.put("CLP",CLP);
					paraMap.put("CM",CM);
					String time="000005";
					paraMap.put("Time",time);
					String xmlCode=XmlCodeCreater.restartRtu(paraMap);
					////System.out.println("XML:\n"+xmlCode);
					String rtuUrl="http://"+findService.findRtu(CM).getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
					String fileName=NumConv.createVerifyCode(20);
					String responseFile="restartRtu"+fileName+".xml";
					String testDecode="testDecode"+fileName+".xml";
					try {
							HttpClientUtil.Post(rtuUrl,xmlCode,responseFile,1500,3000);
							XmlDom XmlDom=new XmlDom();
							Map<String,Object> responseDa=XmlDom.AnalyseRespondse(responseFile,testDecode);
							//调用文件解析函数，获取回传数据
							if(responseDa.get("StatusCode").equals("0")){//成功
								status=true;
							}
							else{
								status=false;
								err="RTU回复异常，请稍后重试，若一直出现该故障，请检查RTU状态是否正常。";
								
							}
					} 
					catch(HttpException  e){
							// TODO Auto-generated catch block
							status=false;
							err="RTU通信异常,与RTU建立连接超时，请检查RTU的状态是否正常。";
							// TODO Auto-generated catch block
							e.printStackTrace();//打印错误信息
					}
					catch (DocumentException | NullPointerException| IOException e) {
							// TODO Auto-generated catch block
							status=false;
							err="与RTU通信过程中RTU回复数据异常，请检查RTU的状态是否正常。";
							e.printStackTrace();
					}
				}
		    	else{//备纤光源RTU
		    		/**重启命令**/
					byte[] code=TCPComndUtil.comdSetServiceOff();
					TcpClient client=new TcpClient(rtu.getRtu_url(),8088,2000);
					while(!client.getIsReady());//等待执行完
					status=client.getStatus();
					if(status){//连接成功
						status=client.sendData(code);
						if(status){
							 client.closeConn();//发送后释放连接
						}
					}
					else{//连接失败
						err="与RTU连接超时，请检查RTU的状态是否正常。";
						
					}
				}
		    }
		    /**记录日志**/
		    Log log=new Log();
			log.setDate(NumConv.currentTime());
			Subject currentUser = SecurityUtils.getSubject();//获取当前用户
	        String Account=currentUser.getPrincipal().toString();//当前用户的账号
	        log.setUser(Account);
	        log.setResourceName(rtu.getRtu_name());
			log.setResourceType("RTU");
			String detail="重启RTU。重启结果：";
			if(status){
				detail+="重启成功.";
			}
			else{
				detail+="重启失败。失败原因："+"RTU通信异常，与RTU建立连接超时。";
			}
			log.setOperateDetail(detail);
			addService.addLog(log);
			responseData.put("status", status);
			responseData.put("err", err);
			response.setContentType("text/xml");
			response.setCharacterEncoding("utf-8");
	    	JSONArray responseD =JSONArray.fromObject(responseData);//将回传数据封装为JSON格式
	    	////System.out.println("response:"+responseD);
	    	PrintWriter out=response.getWriter();
	    	/*将数据返回到前端*/
	    	out.print(responseD);// 
	    	out.flush();
	    	out.close();
	}
	@RequestMapping("forTest")
	public void forTest(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		  AnalysisData.sendMessage("玩儿","测试玩儿");
		   
	    	PrintWriter out=response.getWriter();
	    	/*将数据返回到前端*/
	    	out.print("测试");// 
	    	out.flush();
	    	out.close();
	}
}
