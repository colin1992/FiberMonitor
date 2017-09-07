package fiberMonitor.bean;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import domain.Alarm;
import domain.Curves;
import domain.Duty_operator;
import domain.Duty_schedule;
import domain.Event_curves;
import domain.Parameter_curves;
import domain.Routes;
import fiberMonitor.paraReturn.DataPointReturn;
import fiberMonitor.paraReturn.EventPointReturn;
import fiberMonitor.paraReturn.TestParaReturn;
import service.AddService;
import service.AlterService;
import service.FindService;
/***
 * 数据分析类
 * */
public class AnalysisData {
	private static FindService findService;
    private static AddService addService;
    private static AlterService alterService;
    public static void setFindService(FindService findService) {
    	AnalysisData.findService = findService;
	}
    public static void setAlterService(AlterService alterService) {
    	AnalysisData.alterService = alterService;
	}
    public static void setAddService(AddService addService) {
    	AnalysisData.addService = addService;
	}
	/**
	 *读取测试条件信息
	 *入口参数： byte[] dataSource接收到的反馈数据
	 *出口参数：Map<String, Number>：利用HashMap存放的一些列键值对
	 * 采用LinkedHashMap可以保证独处的顺序和插入的顺序一致*/
   public static  TestParaReturn getTestPara(Map<String,String>retTestPara,byte[] dataSource,int pos){
		 Map<String, Object> testPara=new LinkedHashMap<String, Object>();
		 testPara.put("sampFreq",AnalysisData.getIntData(dataSource,pos, 4));
		 testPara.put("testRange",AnalysisData.getIntData(dataSource,pos+4, 4));
		 testPara.put("testPulWidth",AnalysisData.getIntData(dataSource,pos+8, 4));
		 testPara.put("testWavelen",AnalysisData.getIntData(dataSource,pos+12, 4));
		 testPara.put("testCostTime",AnalysisData.getIntData(dataSource,pos+16, 4));
		     /**群折射率*/
		 testPara.put("GRI",AnalysisData.getFloatData(dataSource,pos+20, 4));
		    /**光纤链长*/
		 testPara.put("FCL",AnalysisData.getFloatData(dataSource,pos+24, 4));
		 /**链损失*/
		 testPara.put("chainLoss",AnalysisData.getFloatData(dataSource,pos+28, 4));
		 /**链衰减*/
		 testPara.put("chainAtten",AnalysisData.getFloatData(dataSource,pos+32, 4));
		/**非反射门限*/
		 testPara.put("NRL",AnalysisData.getFloatData(dataSource,pos+36, 4));
		 /**结束门限*/
		 testPara.put("endLimit",AnalysisData.getFloatData(dataSource,pos+40, 4));
		 String testMode="平均";
		 if(AnalysisData.getIntData(dataSource,pos+44,4)==2){
			  testMode="实时";
		 }
		 testPara.put("testMode",testMode);
		 String testWay="自动";
		 if(AnalysisData.getIntData(dataSource,pos+48,4)==1){
			 testWay="手动";
		 }
		 testPara.put("testWay",testWay);
		 int endPos=pos+52;
		 /**新建参数返回对象*/
		 TestParaReturn testP=new TestParaReturn(); 
		 /**存储返回参数到对象*/
		 testP.setEndPos(endPos);
		 testP.setTestPara(testPara);
		 return testP;
		/**注：遍历HashMap的方法：
	      *  Map<?, ?> testPara=analysisData.getTestPara(recData);
	      Set<String> get = (Set<String>) testPara.keySet(); 
	        for (String test:get) {
	         //System.out.println(test+":"+testPara.get(test));
	        }*/	
		
	 }
	  /**解析数据点数据，返回数据均为float32类型
	   * */

	public static  DataPointReturn getDataPoint(Map<String, Object> testPara,Map<String, String>retTestPara,byte[] dataSource,int pos,String curveFrom) throws FileNotFoundException{
		/**数据点个数*/
		int dataPointSize=getIntData(dataSource,pos,4);
		float[] dataPoint=new float[dataPointSize];//新建float数组，用于存放每个数据点的数据
		//File fp=new File("DataPoint.txt");
		//PrintWriter pfp= new PrintWriter(fp);
		for(int size=0;size<dataPointSize;size++){
			dataPoint[size]=(float) (getIntData(dataSource,pos+4+size*2,2)/1000.0-5.0);//还原数据
           // pfp.print( dataPoint[size]);//存放数据到本地
			//pfp.print("\r\n");
		}
		//pfp.close();
		DataPointReturn dataPointRe=new DataPointReturn();
		/**每个数据点占用四个字节
		 * 加上数据点长度的2个字节*/
		int endPos=pos+4+dataPointSize*2;
		dataPointRe.setEndPos(endPos);
		dataPointRe.setDataPoint(dataPoint);
		/**将曲线数据存入数据库
		 * 
		 * 注：route_id
		 *  curve_type
		 *  referring
		   待完善*/   
		
		 /**将二进制格式的数据点存放到数据库*/
		
		 byte[] dataPointBinary=new byte[dataPointSize*2+4];
		 System.arraycopy(dataSource, pos, dataPointBinary, 0,dataPointSize*2+4);//读取事件点，此时为二进制数据,将长度也存放
		/**
		 * 存储事件点
		 * 数据*/
		 /**光路*/        /**光路类型*/          /**测试时间*/     /**数据点*/
		/**---------通过RTU id和光端口号获取光路-------*/
		Routes route=findService.findRouteByRtuOrderAndID(Long.parseLong(retTestPara.get("CM")), Integer.parseInt(retTestPara.get("SNo")));
		Curves curve=new Curves();
		curve.setCurve_type(curveFrom);
		curve.setTest_date(retTestPara.get("T9"));
		curve.setCurve_data(dataPointBinary);
	    curve.setRoute(route);
		long curveId= (long) addService.addCurve(curve);
		dataPointRe.setCurveId(curveId);
		/**将测试参数存放到数据库,参数是基于曲线的，从而应该先存放曲线数据在存放测试参数
		  * */
		Curves thisCurve=findService.findCurve(curveId);
		Parameter_curves paraCurve=new Parameter_curves();
		paraCurve.setCurve(thisCurve);
		paraCurve.setPulse_width((int)testPara.get("testPulWidth"));//脉宽
	    paraCurve.setEnd_threshold(String.valueOf(testPara.get("endLimit")));
		paraCurve.setMax_range((int)testPara.get("testRange"));
		paraCurve.setRefractive_index(String.valueOf(testPara.get("GRI")));
		paraCurve.setWave_length((int)testPara.get("testPulWidth"));
		paraCurve.setPath_length(String.valueOf((testPara.get("FCL"))));
		paraCurve.setPath_loss(String.valueOf(testPara.get("chainLoss")));
		paraCurve.setPath_attenuation(String.valueOf(testPara.get("chainAtten")));
		paraCurve.setNon_refractive_threshold(String.valueOf(testPara.get("NRL")));
		paraCurve.setPath_loss(String.valueOf(testPara.get("chainLoss")));
		paraCurve.setSample_fre((int)testPara.get("sampFreq"));
		paraCurve.setTime_length(String.valueOf(testPara.get("testCostTime")));
		paraCurve.setAverage_times(Integer.parseInt(retTestPara.get("P14")));
		paraCurve.setOptimize(Boolean.parseBoolean(retTestPara.get("PS")));
		paraCurve.setTestMode((String)testPara.get("testMode"));
		paraCurve.setTestWay((String)testPara.get("testWay"));
	    addService.addParameterCurve(paraCurve);
		return dataPointRe;
		
    }
	/***
	 * 发送短信和邮件
	 * 通过当前为礼拜几查询值班表，然后给当天的值班人员发送告警信息
	 * 
	 * ***/
	public static boolean sendMessage(String title,String info){
			 List<String> email=new ArrayList<String>();
			// System.out.println(NumConv.todayWeek());
			 Duty_schedule schedule=findService.findDutyScheduleByDutyDate(NumConv.todayWeek());
			 List<Duty_operator> operaters=schedule.getOperators();
			 List<String> phone=new ArrayList<String>();
			 boolean status=true;
			 if(operaters!=null){
				 for(Duty_operator operater:operaters){
					  email.add(operater.getEmail());
					  phone.add(operater.getPhoneNumber());
				 }
			 }
			class SendMessage extends Thread{
				 private boolean statusCode;
				 private boolean isEmail;
				 public boolean getStatusCode(){
					 return this.statusCode;
				 }
				 public  SendMessage(boolean isEmail){
					 this.isEmail=isEmail;
				 }
				 public void run(){
					 statusCode=isEmail?MessageUtil.sendEmail(email,title,info):MessageUtil.sendMessage(phone, info);//发送告警
					 if(!statusCode){
						 String titl="告警邮件发送异常情况反馈";
						 String context="系统在发送告警邮件时发送失败，可能原因：\n"
						 		     + "1.邮件服务器地址不正确;\n"
						 		     + "2.值班人员邮件地址填写错误;\n"
						 		     + "3.值班人员邮箱设置了垃圾邮件标注，告警邮件被作为垃圾邮件拦截。\n"
						 		     + "为有效发送告警信息，请及时核对信息排除异常！";
						 if(!isEmail){
							 titl="告警短信发送异常情况反馈";
							 context="系统在发送告警短信时发送失败，可能原因:\n"
							 		+ "1.服务器端尚未正确接入短信模块;\n"
							 		+ "2.短信模块中的SIM状态异常，处于欠费状态或信号微弱状态;\n"
							 		+ "3.值班人员手机号填写错误或处于欠费状态。\n"
							 		+ "为有效发送告警信息，请及时核对信息排除异常！";
						 }
						 MessageUtil.broadcastMessage(titl, context);
					 }
				 }
			 }
			List<SendMessage> threads=new ArrayList<SendMessage>();
			if(!email.isEmpty()){
				 SendMessage mail=new SendMessage(true);
				 mail.start();
				 threads.add(mail);
			 }
			 if(!phone.isEmpty()){
				 SendMessage phones=new SendMessage(false);
				 phones.start();
				 threads.add(phones);
			 }
			 //等待完成
			 for(SendMessage thread:threads){
				 try {
					thread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 //读取结果
			 for(SendMessage thread:threads){
				 status|=thread.getStatusCode();
				
			 }
			//System.out.println("============发送告警信息=========="+(status?"发送成功":"发送失败"));
			return status;
		 
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static  EventPointReturn getEventPoint(long curve_id,Map<String,String>retTestPara,byte[] dataSource,int pos) throws FileNotFoundException{
		/**事件点个数*/
		int eventPointSize=getIntData(dataSource,pos,4);
		/**创建HashMap类型的数组
		 * 每个Map对应一个事件点*/
		LinkedHashMap[] eventPointMap=new LinkedHashMap[eventPointSize];
		Curves thisCurve=findService.findCurve(curve_id);//当前曲线
        /**判断告警
		 * 判断方法：首先检查光路是否存在参考曲线
		 *          如果存在参考曲线：第一步判断链长差别是否超过门限(暂时设置为2000m)，如果超过门限，说明发生了断纤事件
		 *                          接下来读取事件点，最后一个事件点的位置即为断纤的位置
		 *                          第二步：判断总损耗与光功率总损耗之差是否超过门限(暂设为2dB),超过则说明存在异常
		 *                          遍历检查各个事件点的插入损耗和累计损耗，如果超过门限（插入损耗暂定为1.5dB，累计损耗定为2dB）则说明该位置发生了故障
		 *     如果不存在  则读取数据库中的光路长度进行对比，如果长度差超过门限2000m，说明发生了断纤   
		 * **/
		Routes route=thisCurve.getRoute();
		Parameter_curves Parameter_curve=findService.findParameterCurveByCurveId(curve_id);
		Curves refreCuere=findService.findReferenceCurveByRouteId(route.getId());
		boolean isCut=false;
		boolean isBroken=false;
		if(refreCuere!=null){//有参考曲线的情况
			//参考测试的链长
			Float pathLeng=Float.parseFloat(refreCuere.getTest_parameter().getPath_length());
			//参考曲线总损耗
			Float totalLoss=Float.parseFloat(refreCuere.getTest_parameter().getPath_loss());
			if(Math.abs(Float.parseFloat(Parameter_curve.getPath_length())-pathLeng)>2000){
				isCut=true;
			}
			else if(Math.abs(Float.parseFloat(Parameter_curve.getPath_loss())-totalLoss)>2){
				isBroken=true;
			}
		}
		else{//无参考曲线的情况
			//断纤
			if(Math.abs(Float.parseFloat(Parameter_curve.getPath_length())-Float.parseFloat(route.getLength())*1000)>2000){
				isCut=true;
			}
			//产生非正常事件
			else if(Math.abs(Float.parseFloat(Parameter_curve.getPath_loss()))>3){
				isBroken=true;
			}
		}
		//System.out.println("==============is breken=======:"+isBroken+"\n isCut:"+isCut);
		/**告警处理线程**/
			class alarmHandle extends Thread{
				private boolean isCut;
				private boolean isBroken;
				public alarmHandle(boolean isCut,boolean isBroken){
					this.isCut=isCut;
					this.isBroken=isBroken;
				}
				public void run(){
					DecimalFormat format2=new DecimalFormat("#0.00"); 
				    float reIndex=Float.parseFloat(retTestPara.get("reIndex"));
				    int sample=Integer.parseInt(retTestPara.get("sample"));
				    String info=retTestPara.get("T9")+
			    		       findService.findRtu(route.getRtu_id()).getStation().getStation_name()
			    		       +"下的光路："+route.getRoute_name()+
			       			   "产生了OTDR告警。\n"+
			    		       "所属RTU:"+"RTU"+route.getRtu_name()+";\n"+
			       			    "告警来源："+retTestPara.get("curveResource")+";\n";//告警内容
					/**发生断纤事件*/
				    if(isCut){//断纤
				    	//System.out.println("=========================断纤==========================");
						   int index=getIntData(dataSource,pos+24*(eventPointSize-1)+4,4);//断纤的位置是最后一个事件点的位置
						   String distance=format2.format((index)*2.9979*Math.pow(10,8)/(2*reIndex*sample));//告警点离RTU的距离
					       Alarm alarm=new Alarm();
					       alarm.setAlarm_source(retTestPara.get("curveResource"));//获取曲线来源，周期测试 障碍告警测试 点名测试
					       alarm.setAlarm_time(retTestPara.get("T9"));//告警时间为测试时间
					       alarm.setAlarm_type("OTDR告警");
					       alarm.setAlarm_level("断纤");//暂时将DTDR告警的级别设置为事件类型
					       alarm.setDistance(distance);
					       alarm.setIs_handle(false);
					       alarm.setRoute(route);
					       alarm.setRtu_id(route.getRtu_id());
					       addService.addAlarm(alarm);
					       info+="告警原因：断纤;\n"+
				       			 "故障位置：距离RTU"+distance+"m";
					       if(!route.getIs_online()){
					    	  info+= "\n处理建议：备纤断纤，请及时更换。";
					       }
					       String title=info.split("下的光路")[0]+"产生告警";
						   MessageUtil.broadcastMessage(title,info);//向前端推送告警信息
					       /**如果当前光路为在线纤
					        * 检测到断纤后自动切换光路
					        * 障碍告警测试不切换
					        * **/
					       if(!retTestPara.get("curveResource").equals("障碍告警测试")&&route.getIs_online()&&route.getIsProtect()){
					    	  try {
					    		   XmlDom XmlDom=new XmlDom();
								   XmlDom.requestSwitchRoute(route.getRtu_id(),route.getId());
							    } catch (Exception e) {
								//System.out.println("=======切换故障=======");
								e.printStackTrace();
							   }
					       } 
					       if(!retTestPara.get("curveResource").equals("点名测试")){//点名测试不发送告警信息，切换光路
					    	   /**发送告警邮件和短信
						        * 告警信息模板
						        * XX时间XX局站下XXRTU检测的XX光路产生了XX告警
						        * 告警位置：XX
						        * 告警级别：
						        * 告警类型：
						        * **/
					    	   sendMessage("光路OTDR测试断纤告警",info);//发送告警
						   }
					      //将纤芯状态设置为损坏
					       route.setIsBroken(true);  
					       alterService.alterRoute(route);
					}
				    //非断纤故障
					else if(isBroken){
						for(int eventCount=0;eventCount<eventPointSize;eventCount++){//遍历事件点 获取事件点的插入损耗和总损耗，获取故障位置
							float inserLoss=getFloatData(dataSource,pos+24*eventCount+16,4);//插入损耗
							float totalLos=getFloatData(dataSource,pos+24*eventCount+24,4);//累计损耗
							if(inserLoss>1.5&&(inserLoss<100)||(totalLos>2&&(totalLos<100))){
								//插入损耗在1.5-100区间,或总损耗在2-100dB(设置上限是为了排除起始事件和结束事件的干扰)说明该点故障
								int index=getIntData(dataSource,pos+24*eventCount+4,4);//事件点序号
								String site=format2.format((index)*2.9979*Math.pow(10,8)/(2*reIndex*sample));//告警点离RTU的距离
								String reason="反射事件";
								if(getIntData(dataSource,pos+24*eventCount+8,4)==2)
									reason="非反射事件";
								info+="告警原因："+reason+";\n"+
									  "故障位置："+"距RTU"+site+"m;";
								 String title=info.split("下的光路")[0]+"产生告警";
								 MessageUtil.broadcastMessage(title,info);//向前端推送光功率告警信息
								 if(!retTestPara.get("curveResource").equals("点名测试")){//点名测试不发送告警信息
									 sendMessage("光路OTDR告警",info);//发送告警
								}
							}
						}
					}
				    //既未断纤又未损坏，将光路状态设置为正常
					else{
						 //将纤芯状态设置为正常
					       route.setIsBroken(false);  
					       alterService.alterRoute(route);
					}
				}
			}
		 new alarmHandle(isCut,isBroken).start();
		 for(int eventCount=0;eventCount<eventPointSize;eventCount++){
			/**注：使用HashMap数组时，必须对每个HashMap分别实例化
			 * 每个事件点的信息占用24个字节*/
			eventPointMap[eventCount]=new LinkedHashMap();
			eventPointMap[eventCount].put("eventSite",getIntData(dataSource,pos+24*eventCount+4,4));
			eventPointMap[eventCount].put("returnLoss",getFloatData(dataSource,pos+24*eventCount+12,4));
			eventPointMap[eventCount].put("insertLoss",getFloatData(dataSource,pos+24*eventCount+16,4));
			eventPointMap[eventCount].put("ELBP",getFloatData(dataSource,pos+24*eventCount+20,4));
			 /*累计损耗*/
			eventPointMap[eventCount].put("totalLoss",getFloatData(dataSource,pos+24*eventCount+24,4));
			String eventType="";
			switch (getIntData(dataSource,pos+24*eventCount+8,4)){
				case 0:eventType="起始事件"; 
				break;
				case 1:eventType="反射事件"; //对比反射事件的插入损耗，判断是否发生异常
				       
				break;
				case 2:eventType="非反射事件";//
				break;
				case 3:eventType="结束事件"; 
				break;
			    default:
			    break;
			}
			eventPointMap[eventCount].put("eventType",eventType);
			/*存放数据*/
			
			Event_curves event=new Event_curves();
			event.setCurves(thisCurve);
			event.setPotition(String.valueOf(getIntData(dataSource,pos+24*eventCount+4,4)));
			event.setEvent_type(eventType);
			event.setReturn_loss(String.valueOf(getFloatData(dataSource,pos+24*eventCount+12,4)));
			event.setInsertion_loss(String.valueOf(getFloatData(dataSource,pos+24*eventCount+16,4)));
			event.setAttenuation_coeff(String.valueOf(getFloatData(dataSource,pos+24*eventCount+20,4)));
			event.setTotal_loss(String.valueOf(getFloatData(dataSource,pos+24*eventCount+24,4)));
			addService.addEventCurve(event);
	    }
		
		/**for(int i=0;i<eventPointSize;i++){
			int count=i+1;
         	//System.out.println("事件点"+count+":");
         	Set<String> get = (Set<String>) eventPointMap[i].keySet(); 
	       for (String str:get) {
	         //System.out.println(str+":"+eventPointMap[i].get(str));
	        }
         	
		}*/
		/**计算结束位置
		 * 每个事件点占用24个字节，事件点长度占用4个字节*/
		int endPos=pos+eventPointSize*24+4;
		EventPointReturn eventPointRe=new EventPointReturn() ;
		eventPointRe.setEndPos(endPos);
		eventPointRe.setEventPoint(eventPointMap);
		
		return eventPointRe;
    }
	public void sendAlarm(){
		
	}
	/**获取整数型测试数据长度
	 * 入口参数：
	 *        byte[] dataSource：数据源
	 *        int Pos：起始偏移位置
	 *        int BytesLen：欲读取的长度（字节数）*/
	public static int getIntData(byte[] dataSource,int pos,int BytesLen){
		
		byte[] dataLenBuf=new byte[BytesLen];
		System.arraycopy(dataSource, pos, dataLenBuf, 0, BytesLen);
		/**4字节，转为32位Uint*/
		if(BytesLen==4)
		    return NumConv.bytesToUint32(dataLenBuf);
		else /**2字节，转为16位Uint*/
			return NumConv.bytesToUint16(dataLenBuf);
	}
	public static float getFloatData(byte[] dataSource,int Pos,int BytesLen){
		byte[] dataLenBuf=new byte[BytesLen];
		System.arraycopy(dataSource, Pos, dataLenBuf, 0, BytesLen);
		return NumConv.bytesToFloat(dataLenBuf);
	}

}
