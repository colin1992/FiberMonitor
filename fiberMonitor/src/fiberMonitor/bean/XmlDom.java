package fiberMonitor.bean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import domain.Alarm;
import domain.Log;
import domain.Optical_powers;
import domain.Protect_groups;
import domain.Routes;
import domain.Rtus;
import domain.Threshold;
import fiberMonitor.paraReturn.DataPointReturn;
import fiberMonitor.paraReturn.TestParaReturn;
import service.AddService;
import service.AlterService;
import service.FindService;

public class XmlDom {
	private static  FindService findService;
    private static AddService addService;
    private static AlterService alterService;
    public  Map<String, Object> responseData=new LinkedHashMap<String, Object>();
    public static void setFindService(FindService findService) {
    	XmlDom.findService = findService;
	}
    public  void setAlterService(AlterService alterService) {
    	XmlDom.alterService = alterService;
	}
    public  void setAddService(AddService addService) {
    	XmlDom.addService = addService;
	}
	
	public static void analysisXml(String fileUrl) throws DocumentException{
		  SAXReader reader = new SAXReader();
		  File file = new File(fileUrl);  
		  Document document = reader.read(file);
		  Element root = document.getRootElement();//获取根节点
		  getNodes(root); 
	 }
    @SuppressWarnings("unchecked")
	public static void getNodes(Element node){  
		   //当前节点的名称、文本内容和属性  
    	if(!node.getName().equals("TstDat")){
    		// System.out.println(node.getName()+":"+node.getTextTrim());//当前节点名称   
    		 //List<Attribute> listAttr=node.attributes();//当前节点的所有属性的list  
 		    //for(Attribute attr:listAttr){//遍历当前节点的所有属性  
 		      //  String name=attr.getName();//属性名称  
 		      //  String value=attr.getValue();//属性的值  
 		       // System.out.println("Attribute:"+name+"="+value);  
 		   // }  
    	}
		//递归遍历当前节点所有的子节点  
	    List<Element> listElement=node.elements();//所有一级子节点的list  
		for(Element e:listElement){//遍历所有一级子节点  
		      getNodes(e);//递归  
		   }  
		}  
	 
  /**对根名的分析
   * 针对不同的根名将其作为不同的类型的xml文件进行解析
   * 点名测试阶段先做AsynPush和RespondMessage中的基本类型
   * 待后续完善
   * */
 public  Map<String, Object> AnalyseRespondse(String xmlFile,String TstDataFileName ) throws DocumentException, IOException, HttpException {
	  SAXReader reader = new SAXReader();
	  File file = new File(xmlFile);
	  Document document = reader.read(file);
	  Element root = document.getRootElement();
	  getNodes(root);
	  String rootName=root.getName();//获取根名（根名标识了数据类别）
	  switch (rootName){
	  	 case "AsynPush":/*RTU推送文件*/
			 AsynPushAnalyse(document,root,TstDataFileName);//解析文件
			 break;
			 
		 case "RespondMessage":/*向RTU下发请求后的回复*/
			ResponseAnalyse(document,root,TstDataFileName);//解析文件
		   break;
		 default:
			  break;
	 }
	  deleteFile(xmlFile);//解析完成后删除文件，防止干扰
      deleteFile(TstDataFileName);//解析完成后删除文件，防止干扰  
	  return responseData;//回传解析后需要反馈给页面的数据信息
	  
 }
	/**
	 * 
	 * 创建时间2016/7/1*/
	private  void ResponseAnalyse(Document document,Element root,String TstDataFileName) throws IOException, DocumentException{
		  String RespondCode=root.elementText("RespondCode");
		  /**
		   * 获取到回复码后
		   * 根据不同的情况作出相应的处理，
		   * 点名测试阶段先处理0,1,3,10回复码的情形
		   * 
		   * 后续情形待完善*/
		  switch(RespondCode){
		  case "0":/*成功处理下发命令,回传参数 只含有确认信息或者是其他简单的信息
		                      不包含测试数据信息，适用于下发配置命令的情形
		           */
			  responseData.put("StatusCode","0");//成功处理下发命令
			  break;
		  case "1":/*设置测试参数非法*/
			  responseData.put("StatusCode","1");//故障，状态码返回1
			  responseData.put("err","错误，您设置的测试参数非法，请核对后重新发送!");//设置参数非法，将回复码返回到服务器进行分析处理
			  break;
          case "2":/*设置门限参数非法*/
        	  responseData.put("StatusCode","2");//故障，状态码返回1
			  responseData.put("err","错误，您设置的门限参数非法，请核对后重新发送!");//设置参数非法，将回复码返回到服务器进行分析处理
			  break;
          case "3":/*命令无效*/
        	  responseData.put("StatusCode","3");//故障，状态码返回1
			  responseData.put("err","错误，下发命令无效，请重新发送!");//设置参数非法，将回复码返回到服务器进行分析处理
			  break;
          case "4":/*设通信置参数非法*/
        	  responseData.put("StatusCode","4");//故障，状态码返回1
			  responseData.put("err","错误，您设置的回传地址有误，请核对后重新发送!");//设置参数非法，将回复码返回到服务器进行分析处理
			  break;
          case "10":/**回传数据中包含测试的数据文件 *解析数据文件*/
          case "11":
          case "12":
        	  responseData.put("StatusCode",RespondCode);//
              ResponseDataClassAnalyse(document,root,TstDataFileName);
			  break;
          case "14":/**服务器和RTU数据库不同步**/	 
        	       responseData.put("StatusCode",RespondCode);//
        	       String errSN=root.element("Data").elementText("ErrorSN");//读取错误节点
        	       if(errSN!=null){//
        	    	   if(root.element("Data").elementText("SNo")!=null){//端口形式返回配置错误
        	    		   responseData.put("errType","route");//错误类型为光路
        	    		   String SNo=root.element("Data").elementText("SNo");
        	    		   SNo=SNo.substring(0,Integer.parseInt(errSN));
        	    		   String err="光端口号为"+SNo+"的光路在RTU处不存在占用信息，请重新配置光路。";
        	    		   responseData.put("err",err);
        	    	   }
        	    	   else{
        	    		   responseData.put("err",root.element("Data").elementText("Infor").split(","));
        	    	   }
        	    	}
 	              
        	       break;
          case "13"://保护切换失败
        	      responseData.put("StatusCode",RespondCode);//
	              responseData.put("err","当前配对组中两条光路的类型相同，请更改光路状态后重试");//
	              break;
          /**增加RTU模块时包含实际不存在的模块*/
          case"15": responseData.put("StatusCode",RespondCode);//
                  break;
		  default: responseData.put("StatusCode",RespondCode);//
		           responseData.put("err","未知错误");//
		           break;
		  }
		 
     }
	
	/**当有回复数据时分析数据类型
	 * 针对RespondCode为10的情形
	 * 读取<Data>下的第一个子节点名称，判断是何种数据
	 *  TsetData:普通测试数据
	 *  OpticPowerData：光功率数据
	 *  RTUStatus：RTU 状态数据
	 *  .....
	 *  点名测试阶段先解析读取<TsetData>类型数据
	 * */
    
	@SuppressWarnings("unchecked")
	private  void ResponseDataClassAnalyse(Document document,Element root,String TstDataFileName) throws IOException{
		 /**获取<Data>节点下的子节点*/
		 Element DataNode=root.element("Data");
		 List<Element> listElement=DataNode.elements();//所有一级子节点的list  
		 String DataType=listElement.get(0).getName();
		 /**根据不同的数据类型调用相应的解析方式
		  * 点名测试阶段只做TestData类型：普通测试数据
		  * */
		 switch(DataType){
		 	case "TestData":
		 		//测试数据
		 		responseData.clear();
		 		responseData.put("StatusCode","10");
		 		Element TestData=root.element("Data").element("TestData");
		 		  /**定义HashMap，用于存放接收到的相关参数*/
			      Map<String, String> retTestPara=new LinkedHashMap<String, String>();
			     /**提取参数，放入MAP中
			       * 不存放CMDcode和R
			       * 含有从参数表中SNo至T9的12组键值对
			     /*读取TestData下的所有子节点*/
				  List<Element> listTestDataElement=TestData.elements();
			      for(int len=2;len<listTestDataElement.size();len++){
			    	  retTestPara.put(listTestDataElement.get(len).getName(),listTestDataElement.get(len).getText());
			      }
			      retTestPara.replace("T9",NumConv.currentTime());
			      /**读取测试数据*/  
			      String TstDat=TestData.elementText("TstDat");
		 		  ResponseTestDataAnalyse(retTestPara,TstDat,TstDataFileName,"点名测试");
		 		break;
		 	case"OpticalPowerData":
		 		//光功率数据
		 		responseData.clear();
		 		responseData.put("StatusCode","10");
		 		Element OpticaPowerData=root.element("Data").element("OpticalPowerData");
		 		if(OpticaPowerData.elementText("CMDcode").equals("511"))//判断指令码是否为511
		 		{
		 			Long CM=Long.parseLong(OpticaPowerData.elementText("CM"));//获取CM
		 		    int SNo=Integer.parseInt(OpticaPowerData.elementText("SNo"));//获取SNo
		 		    String powerValue=OpticaPowerData.elementText("PowerValue");//获取光功率值
		 		   // System.out.println("CM:"+CM+"\nSNO:"+SNo+"\nfindServiceImpl:"+findService);
		 		    Routes route=findService.findRouteByRtuOrderAndID(CM,SNo);
		 		    Long routeId=route.getId();
		 		    Optical_powers opticalPower=new Optical_powers();
		 		    opticalPower.setPowerValue(powerValue);
		 		    opticalPower.setTestTime( NumConv.currentTime());
		 		    opticalPower.setRouteId(routeId);
		 		    addService.addOpticalPower(opticalPower);
		 		    responseData.put("opticalPower", opticalPower);
                    responseData.put("testTime", NumConv.currentTime());
               }
		 		break;
		 	case "ReferenceTime":       //时间参数
		 		   Element ReferenceTime=root.element("Data").element("ReferenceTime");
		 		   if(ReferenceTime.elementText("CMDcode").equals("514"))//核对指令码
		 			   {
		 			     String T8=ReferenceTime.elementText("T8")+"000";//获取时间，时间信息为时间戳 RTU时间戳格式为10位 Java为13位
		 			     //System.out.println("stamp:"+T8);
		 			     T8=NumConv.stampToDate(T8);//将时间戳转换为时间
		 			    //System.out.println("time:"+T8);
		 			     responseData.put("T8",T8);
		 			   }
		 		  break;
		 	case "NetworkSegment"://网络参数
		 		  Element NetworkSegment=root.element("Data").element("NetworkSegment");
		 		  if(NetworkSegment.elementText("CMDcode").equals("513"))//核对指令码
		 			   {
		 			     String IP=NetworkSegment.elementText("IP");//获取IP
		 			     String Netmask=NetworkSegment.elementText("Netmask");//获取netMask
		 			     String Gateway=NetworkSegment.elementText("Gateway");//获取Gateway
		 			     responseData.put("IP",IP);
		 			     responseData.put("netMask",Netmask);
		 			     responseData.put("gateway",Gateway);
		 			   }
		 		 
		 		  break;
		 	default:
		 		 
		 		 break;	
		 }
     }
     
	/**普通测试数据
	 * 
	 * TestData的解析
	 * 入口参数为：
	 *        Element：<Data>节点下的子节点列表
	 *        String TstDataFileName:存放解析得到的测试数据的文件
	 *        此时的TstData为进行Base64编码后的二进制数据
	 *        
	 * */
	private  void ResponseTestDataAnalyse(Map<String, String> retTestPara,String TstDat,String TstDataFileName,String curveFrom) throws IOException{
	       /**对测试数据进行解码
	               解码后的数据用字节组
	       recData存储
	       	此时的数据为大端模式*/
	      byte[] recData=Base64Code.Base64DecodeString(TstDat, TstDataFileName);
	     /**获取返回数据中的测试参数信息*/
	        TestParaReturn TPR= AnalysisData.getTestPara(retTestPara,recData,8);
	        Map<String, Object> testPara=TPR.getTestPara();
           /**获取数据点信息和数据点数据*/
	       // 下发测试参数及RTU返回参数
			  DataPointReturn DPR=AnalysisData.getDataPoint(testPara,retTestPara,recData,TPR.getEndPos(),curveFrom);
			  responseData.put("curveId",DPR.getCurveId());//将点名测试获得的曲线Id回传，用于前端查询曲线
	         /**读取事件点*/
			 retTestPara.put("curveResource", curveFrom);//存入数据来源，用于保存报警信息时注明来源
			 retTestPara.put("reIndex", String.valueOf(testPara.get("GRI")));//存入折射率，用于计算告警的位置
			 retTestPara.put("sample", String.valueOf(testPara.get("sampFreq")));//存入采样频率，用于计算告警的位置
	         AnalysisData.getEventPoint(DPR.getCurveId(),retTestPara,recData,DPR.getEndPos()).getEventPoint();
	         deleteFile(TstDataFileName);
	}
	/**--------------RTU推送数据的情形------------------------*/
	@SuppressWarnings("unchecked")
	private  void AsynPushAnalyse(Document document,Element root,String TstDataFileName) throws IOException, DocumentException, HttpException{
		  List<Element> listElement=root.elements();//所有一级子节点的list  
		  String DataType=listElement.get(0).getName();
		   //分析推送数据的类型
		  switch(DataType){ 
			  //新的测试数据
			  case "NewOTDRData": 
				      AsynPushNewOTDRDataAnalyse(root,TstDataFileName);
				    break; 
			 //光功率告警
			  case "OpticPowerWarming":
				    AsynPushOpticPowerWarmingAnalyse(root,TstDataFileName);
				    break;
			 //RTU端控制切换的报告
			  case"SwitchReport":
				  GetSwitchReport(root,TstDataFileName);
				  break;
			  //保护组备纤在线纤均故障时推送故障报告
			  case"LineFaultWarming":
				  GetLineFaultWarm(root,TstDataFileName);
				  break;
			  default:
				   break;  
			  }
		  
     }
	
	 @SuppressWarnings("unchecked")
	 /**-----------------推送数据为新的OTDR测试数据-----------------*/
	public  void  AsynPushNewOTDRDataAnalyse(Element root,String TstDataFileName) throws IOException{
		  Element NewOtdrDataNode=root.element("NewOTDRData");//获取NewOtdrData节点
		  String Type=NewOtdrDataNode.elementText("Type");//获取数据类型，判断是障碍告警测试还是周期测试
		  Element Data= NewOtdrDataNode.element("Data");//获取测试数据节点
		  List<Element> listData=Data.elements();
		  Map<String,String>  retTestPara=new LinkedHashMap<String,String>();
		  retTestPara.put("CM",NewOtdrDataNode.elementText("CM"));//存放CM
		  retTestPara.put("SNo",NewOtdrDataNode.elementText("SNo"));//存放光端口序号
		  for(int len=0;len<listData.size()-1;len++){
	    	  retTestPara.put(listData.get(len).getName(),listData.get(len).getText());
	      }
		  retTestPara.replace("T9",  NumConv.currentTime());
		  String TstDat=Data.elementText("TstDat");//获取数据
		  switch(Type){
		  		/**周期测试数据*/
		  		case "1"://分析参数
		  			ResponseTestDataAnalyse(retTestPara,TstDat,TstDataFileName,"周期测试");
		  			break;
		  			/**障碍告警测试数据*/
		  		case "2":
		  			ResponseTestDataAnalyse(retTestPara,TstDat,TstDataFileName,"障碍告警测试");
		  			break;
		  		default:
		  			break;
		  }
		} 
       /**-----------------推送数据为光功率异常测试数据-----------------*/
		public  void  AsynPushOpticPowerWarmingAnalyse(Element root,String TstDataFileName) throws HttpException, IOException, DocumentException{
			  Element OpticPowerWarmingNode=root.element("OpticPowerWarming");//获取NewOtdrData节点
			  String CMDcode=OpticPowerWarmingNode.elementText("CMDcode");
			  if(CMDcode.equals("522")){//确认命令码
				  Long CM=Long.parseLong(OpticPowerWarmingNode.elementText("CM"));//获取CM
				  int SNo= Integer.parseInt(OpticPowerWarmingNode.elementText("SNo"));
				  Float PowerValue= Float.parseFloat(OpticPowerWarmingNode.elementText("PowerValue"));//
				  WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();    
			      addService=(AddService)wac.getBean("addService");
			      FindService findService=(FindService)wac.getBean("findService");
			      Routes route=findService.findRouteByRtuOrderAndID(CM, SNo);
				  Threshold thres=findService.findThresholdByRouteId(route.getId());
				  String WarmingLevel=""; //告警级别
				  if(thres!=null){//存在门限信息
					  //小于切换门限
					  if(PowerValue<Float.parseFloat(thres.getSwitchThre())){
						  WarmingLevel="一级";
						  if(route.getIs_online()&&route.getIsProtect()){//如果为在线纤，存在保护组，自动切换业务
							  /**
							   * 此处发送切换光路的指令
							   * **/
							  requestSwitchRoute(CM,route.getId());
						  }
					  } 
					  //小于一级门限
					  else if(PowerValue<Float.parseFloat(thres.getThre1())){
						  WarmingLevel="一级";
					  }
					//小于二级门限
					  else if(PowerValue<Float.parseFloat(thres.getThre2())){
						  WarmingLevel="二级";
					  }
					//小于三级门限
					  else if(PowerValue<Float.parseFloat(thres.getThre3())){
						  WarmingLevel="三级";
					  }
					//小于四级门限
					  else if(PowerValue<Float.parseFloat(thres.getThre4())){
						  WarmingLevel="四级";
					  }
				  }
				  else{//不存在告警门限
					  WarmingLevel="三级告警";
				  }
				  if(!WarmingLevel.equals("")){//存在告警，存放一条告警记录
					  Rtus rtu=findService.findRtu(CM);
					  String alertInfo=NumConv.currentTime()+
							           rtu.getStation().getStation_name()
							           +"下的光路："+route.getRoute_name()+
							           "产生"+WarmingLevel+"光功率告警。\n"+
					    		       "所属RTU:"+rtu.getRtu_name()+";\n"
				                       +"当前光功率值为："+PowerValue+"dB";
					  String title=alertInfo.split("下的光路")[0]+"产生告警";
					  MessageUtil.broadcastMessage(title,alertInfo);//向前端推送光功率告警信息
					  Alarm alarm=new Alarm(); 
					  alarm.setAlarm_level(WarmingLevel);
					  alarm.setAlarm_source("障碍告警测试");
					  alarm.setAlarm_type("光功率异常");
					  alarm.setDistance(null);
					  alarm.setIs_handle(false);
					  alarm.setAlarm_time(NumConv.currentTime());
					  alarm.setRoute(route);
					  alarm.setRtu_id(CM);
					  addService.addAlarm(alarm);//存储告警信息
					  AnalysisData.sendMessage("光功率告警",alertInfo);//发送告警
				  }
			  }
			  
	 }
    /**RTU推送的切换报告**/
	public  void GetSwitchReport(Element root,String TstDataFileName) throws HttpException, IOException, DocumentException{
		  Element switchReport=root.element("SwitchReport");//获取节点
		  String CMDcode=switchReport.elementText("CMDcode");
		  if(CMDcode.equals("523")){//确认命令码
			  Long CM=Long.parseLong(switchReport.elementText("CM"));//获取CM
			  //上报的切换后的状态16或96
			  String downSwitchPos=switchReport.elementText("StatusRecv");
			  String upSwitchPos=switchReport.elementText("StatusSend");
			  //切换后下行在线纤的端口序号
			  int downOffOrder=Integer.parseInt(switchReport.elementText("RSNo"));
			  //切换后上行在线纤的端口序号
			  int upOffOrder=Integer.parseInt(switchReport.elementText("SSNo"));
			  int moudleOrder=Integer.parseInt(switchReport.elementText("ModNo"));//模块序号
			//  System.out.println("==========推送切换报告===========");
			  //推送的端口序号为切换后的在线，也就是原来的备纤
			  Routes routeUpOff=findService.findRouteByRtuOrderAndID(CM, upOffOrder);
			  Routes routeDownOff=findService.findRouteByRtuOrderAndID(CM, downOffOrder);
			  if(routeUpOff.getRtu_model_order()==moudleOrder){//确认为该模块
				  Protect_groups group=findService.findProtectGroupsByRouteId(routeDownOff.getId());
				  if(group!=null){
					  //切换前的状态信息
					  Long downOff=group.getRouteDownOffId();
					  Long downOn=group.getRouteDownOnId();
					  Long upOff=group.getRouteUpOffId();
					  Long upOn=group.getRouteUpOnId();
					  //切换前的光路名信息
					  String upOnName=group.getRouteUpOnName();
					  String upOffName=group.getRouteUpOffName();
					  String downOnName=group.getRouteDownOnName();
					  String downOffName=group.getRouteDownOffName();
					  /**确保服务器数据库状态和RTU状态一致,如果数据库的状态与RTU不一致，以RTU为准,只有切换前状态一致才修改
					   * 此处的off指的是上一个状态，切换后实际应该是on
					   */
					  if(!routeUpOff.getIs_online()||!routeDownOff.getIs_online()){
						  /**调换切换后备纤在线纤光路ID**/
						  group.setRouteDownOffId(downOn);
						  group.setRouteDownOnId(downOff);
						  group.setRouteUpOffId(upOn);
						  group.setRouteUpOnId(upOff);
						  /**调换切换后备纤在线纤光路名**/
						  group.setRouteDownOffName(downOnName);
						  group.setRouteDownOnName(downOffName);
						  group.setRouteUpOffName(upOnName);
						  group.setRouteUpOnName(upOffName);
						  group.setDownSwitchPos(downSwitchPos);//修改切换状态
						  group.setUpSwitchPos(upSwitchPos);//修改切换状态
						  alterService.alterProtectGroup(group);//修改保护组光开关状态
						  Routes routeDownOn=findService.findRoute(downOn);
						  routeDownOff=findService.findRoute(downOff);
						  routeUpOff=findService.findRoute(upOff);
						  routeDownOn.setIs_online(false);
						  routeDownOn.setIsBroken(true);//原来的在线纤设置为损坏
						  routeDownOff.setIs_online(true);
						  Routes routeUpOn=findService.findRoute(upOn);
						  routeUpOn.setIs_online(false);
						  routeUpOn.setIsBroken(true);//原来的在线纤设置为损坏
						  routeUpOff.setIs_online(true);
						  alterService.alterRoute(routeDownOff);//修改光路状态
						  alterService.alterRoute(routeDownOn);
						  alterService.alterRoute(routeUpOn);//修改光路状态
						  alterService.alterRoute(routeUpOff);//修改光路状态
					  }
					  //服务器数据库和RTU端刚好状态相反，切换换后一致，所以不修改状态
					  else{
						   upOnName=group.getRouteUpOffName();
						   upOffName=group.getRouteUpOnName();
						   downOnName=group.getRouteDownOffName();
						   downOffName=group.getRouteDownOnName();
					  }
					  /**记录切换日志**/
					  Log log=new Log();
					  log.setDate(NumConv.currentTime());
					  log.setUser("系统自动");
				      log.setResourceType("业务切换");
				      String detail="";
					  String operate="";
					  detail+="下行："+downOnName+"由在线业务纤切换为备纤，"+downOffName+"由备纤切换为业务纤;"+
					          "上行："+upOnName+"由在线业务纤切换为备纤，"+upOffName+"由备纤切换为业务纤;";
					  operate+=group.getRouteDownOnName()+","+group.getRouteDownOffName();
					  String title=NumConv.currentTime()+"保护光路业务纤断纤自动切换情况反馈";
					  String info=NumConv.currentTime()+"检测到断纤，系统自动切换保护组光纤。\n切换详情:"+detail+"\n为保证保护组的正常功能，请及时修复更换损坏的光纤。";
					  MessageUtil.broadcastMessage(title,info);//向前端推送光功率告警信息
					  detail+="\n为保证保护组的正常功能，请及时修复更换损坏的光纤。";
					  log.setOperateDetail("检测到断纤，系统自动切换保护组光纤:"+detail);
					  log.setResourceName(operate);
					  addService.addLog(log);
					  AnalysisData.sendMessage("保护光路业务纤断纤自动切换情况反馈",info);//发送告警
				 }
			  }
		 }
			
	}
	/**推送消息为备纤与在线纤均损坏**/
	 /**RTU推送的损坏报告：首先将数据库中当前保护组的四条光路状态均设置为损坏
	  * 然后推送消息**/
	public  void GetLineFaultWarm(Element root,String TstDataFileName) throws HttpException, IOException, DocumentException{
			  Element switchReport=root.element("LineFaultWarming");//获取节点
			  String CMDcode=switchReport.elementText("CMDcode");
			  if(CMDcode.equals("521")){//确认命令码
				  Long CM=Long.parseLong(switchReport.elementText("CM"));//获取CM
				  //下行在线纤的端口序号
				  int downOffOrder=Integer.parseInt(switchReport.elementText("RSNo"));
				  //上行在线纤的端口序号
				  int upOffOrder=Integer.parseInt(switchReport.elementText("SSNo"));
				  int moudleOrder=Integer.parseInt(switchReport.elementText("ModNo"));//模块序号
				  //推送的端口序号为切换后的在线，也就是原来的备纤
				  Routes routeUpOff=findService.findRouteByRtuOrderAndID(CM, upOffOrder);
				  Routes routeDownOff=findService.findRouteByRtuOrderAndID(CM, downOffOrder);
				  if(routeUpOff.getRtu_model_order()==moudleOrder){//确认为该模块
					  Protect_groups group=findService.findProtectGroupsByRouteId(routeDownOff.getId());
					  if(group!=null){
						  //光路名信息
						  String upOnName=group.getRouteUpOnName();
						  String upOffName=group.getRouteUpOffName();
						  String downOnName=group.getRouteDownOnName();
						  String downOffName=group.getRouteDownOffName();
						  routeDownOff=findService.findRoute(group.getRouteDownOffId());
						  routeUpOff=findService.findRoute(group.getRouteUpOffId());
						  Routes routeDownOn=findService.findRoute(group.getRouteDownOnId());
						  Routes routeUpOn=findService.findRoute(group.getRouteUpOnId());
						  routeUpOn.setIsBroken(true);//原来的在线纤设置为损坏
						  routeUpOff.setIsBroken(true);
						  routeDownOn.setIsBroken(true);//原来的在线纤设置为损坏
						  routeDownOff.setIsBroken(true);
						  alterService.alterRoute(routeDownOn);
						  alterService.alterRoute(routeDownOff);//修改光路状态
						  alterService.alterRoute(routeUpOn);//修改光路状态
						  alterService.alterRoute(routeUpOff);//修改光路状态
						  String detail="";
						  detail+="下行：业务纤—"+downOnName+"，"+"备纤—"+downOffName+";"+
						          "上行：业务纤—"+upOnName+"，"+"备纤—"+upOffName+"。";
						  String title=NumConv.currentTime()+"保护光路业务纤与备纤均断纤";
						  String info=NumConv.currentTime()+"系统检测到保护组业务纤断纤，自动切换时发现备纤也断裂。\n保护组详情:"+detail+"\n请及时修复更换损坏的光纤。";
						  MessageUtil.broadcastMessage(title,info);//向前端推送光功率告警信息
						  AnalysisData.sendMessage("保护光路业务纤断纤自动切换情况反馈",info);//发送告警
					 }
				  }
			 }
	}
 	/**-----------------请求光保护切换-----------------*/	
    public  void  requestSwitchRoute(Long CM,Long routeId) throws HttpException, IOException, DocumentException{
    	Routes route=findService.findRoute(routeId);
    	if(route.getIsProtect()){
    		Protect_groups group =findService.findProtectGroupsByRouteId(routeId);
			responseData=new LinkedHashMap<String,Object>();
			String switchStatus=group.getDownSwitchPos().equals("96")?"16":"96";//要切换到的状态 
			List<Object> groupList=new ArrayList<>();
			boolean status=false;
			Map<String,Object> groupPara=new LinkedHashMap<String, Object>();
			Map<String,Object> groupMap=new LinkedHashMap<String,Object>();
			Routes routeDownOn=findService.findRoute(group.getRouteDownOnId());
			Routes routeDownOff=findService.findRoute(group.getRouteDownOffId());
			if(!routeDownOff.getIsBroken()&&!findService.findRoute(group.getRouteUpOffId()).getIsBroken()){//备纤光路必须未损坏才切换
				groupMap.put("SwitchPos",switchStatus);
				int orderA=routeDownOn.getRtu_port_order();
				int orderB=routeDownOff.getRtu_port_order();
				int PNo=Math.min(orderA,orderB);//配对组序号
				PNo=(routeDownOn.getRtu_model_order()-1)*4+(PNo-1)%8+1;//组号为1-32
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
						String testDecode="testDcode"+fileName+".txt";
						HttpClientUtil.Post(rtuUrl, requestProtectCode,responseFile,1500,2500);
						responseData=AnalyseRespondse(responseFile,testDecode);//调用文件解析函数，获取回传数据
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
				String Account="自动切换";
				try{
					Subject currentUser = SecurityUtils.getSubject();//获取当前用户
					Account=currentUser.getPrincipal().toString();//当前用户的账号
				}catch(Exception e){
					
				}
		        log.setUser(Account);
		        log.setResourceType("业务切换");
		        String detail="";
				String operate="";
				detail+="下行："+routeDownOn.getRoute_name()+"由在线业务纤切换为备纤，"+routeDownOff.getRoute_name()+"由备纤切换为业务纤;"+
				            "上行："+group.getRouteDownOnName()+"由在线业务纤切换为备纤，"+group.getRouteDownOffName()+"由备纤切换为业务纤;";
				operate+=routeDownOn.getRoute_name()+","+routeDownOff.getRoute_name();
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
					routeDownOn.setIsBroken(true);//原来的在线纤设置状态为损坏
					alterService.alterRoute(routeDownOn);
					routeDownOff.setIs_online(true);
					alterService.alterRoute(routeDownOff);//修改光路状态
					Routes routeUpOn=findService.findRoute(upOn);
					Routes routeUpOff=findService.findRoute(upOff);
					routeUpOn.setIs_online(false);
					routeUpOn.setIsBroken(true);//原来的在线纤设置状态为损坏
					routeUpOff.setIs_online(true);
					alterService.alterRoute(routeUpOn);//修改光路状态
					alterService.alterRoute(routeUpOff);//修改光路状态
				}
				/**发送切换报告**/
				 String title=NumConv.currentTime()+"保护光路业务纤断纤自动切换情况反馈";
				 String info=NumConv.currentTime()+"检测到断纤，系统自动切换保护组光纤。\n切换详情:"+detail;
				 MessageUtil.broadcastMessage(title,info);//向前端推送光功率告警信息
				 AnalysisData.sendMessage("保护光路业务纤断纤自动切换情况反馈",info);//发送告警
			}
    	}
    }
    /** 
     * 删除单个文件 
     * @param   sPath    被删除文件的文件名 
     * @return 单个文件删除成功返回true，否则返回false 
     */  
    public static boolean deleteFile(String sPath) {  
       boolean flag = false;  
        File file = new File(sPath);  
        // 路径为文件且不为空则进行删除  
        if (file.isFile() && file.exists()) {  
            file.delete();  
            flag = true;  
        } 
        return flag;  
    } 
    
}
