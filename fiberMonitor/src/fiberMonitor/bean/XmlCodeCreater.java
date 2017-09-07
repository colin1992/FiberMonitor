package fiberMonitor.bean;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpException;

/**类名： requestCodeCreater
 * 说明：该类用于生成向RTU下发指令的xml字符串
 * 创建时间：2016/6/30
 * 备注：当前方式入口参数过多，考虑后期直接将
 * request和response作为参数传入，在此处进行解析提取参数
 * 
 * 包含方法：
 * */
public class XmlCodeCreater {
	

public static void main(String[] args) throws HttpException, IOException{
}
/**---------------------点名测试-----------------------*/
   public static String setNameTestCode(Map<String, String> testPara){
	   String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		xml+="<SegmentCode>\n";
		xml+="<SetNamedTestSegment>\n";
		xml+="<CMDcode>110</CMDcode>\n";
		xml+="<R>*</R>\n";
		xml+="<CM>"+testPara.get("CM")+"</CM>\n";
		xml+="<CLP>"+testPara.get("CLP")+"</CLP>\n";
		xml+="<SNo>"+testPara.get("SNo")+"</SNo>\n";
		xml+="<PS>"+testPara.get("PS")+"</PS>\n";
		xml+="<P11>"+testPara.get("P11")+"</P11>\n";
		xml+="<P12>"+testPara.get("P12")+"</P12>\n";
		xml+="<P13>"+testPara.get("P13")+"</P13>\n";
		xml+="<P14>"+testPara.get("P14")+"</P14>\n";
		xml+="<P15>"+testPara.get("P15")+"</P15>\n";
		xml+="<P16>"+testPara.get("P16")+"</P16>\n";
		xml+="<P17>"+testPara.get("P17")+"</P17>\n";
		xml+="</SetNamedTestSegment>\n";
		xml+="</SegmentCode>";
	   return xml;
	   
   }
   
  
   /**优化参数设置方法
    * setOptiParaCode：用于生成设置优化参数的xml字符串
    */
   public static String setOptiParaCode(Map<String, Object> indexPara,String[][] optPara ){
	    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		xml+="<SegmentCode>\n";
		xml+="<SetDefaultTestSegment>\n";
		xml+="<CMDcode>100</CMDcode>\n";
		xml+="<R>*</R>\n";
		xml+="<CM>"+indexPara.get("CM")+"</CM>\n";
		xml+="<CLP>"+indexPara.get("CLP")+"</CLP>\n";
		xml+="<SN>"+indexPara.get("SN")+"</SN>\n";
		   /*输出A1-Asn*/
	
		for(int ACount=1;ACount<=(int)(indexPara.get("SN"));ACount++){
			xml+="<A"+ACount+">\n"; 
			
			/*分别给每个Ai的P01-P07写入值*/
			if(ACount<=optPara.length){ //需要修改优化参数的Ai输出参数，否者输出*
				xml+="<SNo>"+optPara[ACount-1][0]+"</SNo>\n";
				for(int PCount=1;PCount<=7;PCount++){  //给每个Ai添加P01-P07
					if(PCount==1){
						xml+="<P0"+PCount+">"+optPara[ACount-1][PCount]+"000"+"</P0"+PCount+">\n";//将量程由Km标识为m
					}
					else{
						xml+="<P0"+PCount+">"+optPara[ACount-1][PCount]+"</P0"+PCount+">\n";
					}
					
	            }
			}else{  //光路中不修改优化参数的都填充*
				xml+="<SNo>"+'*'+"</SNo>\n";
					for(int PCount=1;PCount<=7;PCount++){
					
					xml+="<P0"+PCount+">"+'*'+"</P0"+PCount+">\n";
	            }
			}
				xml+="</A"+ACount+">\n";
		}
		
		xml+="</SetDefaultTestSegment>\n";
		xml+="</SegmentCode>";
      return xml;
  
}
   /**-----------------------设置周期测试时间表信息----------------------*/
   public static String setCycParaCode(Map<String, String> indexPara,String[][] cycPara ){
	    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		xml+="<SegmentCode>\n";
		xml+="<SetCycleTestSegment>\n";
		xml+="<CMDcode>120</CMDcode>\n";
		xml+="<R>*</R>\n";
		xml+="<CM>"+indexPara.get("CM")+"</CM>\n";
		xml+="<CLP>"+indexPara.get("CLP")+"</CLP>\n";
		xml+="<SN>"+indexPara.get("SN")+"</SN>\n";
		/*分别给每个Ai的P01-P07写入值，待完善*/

		for(int ACount=1;ACount<=Integer.parseInt(indexPara.get("SN"));ACount++){
			xml+="<B"+ACount+">\n"; 
		if(ACount<=cycPara.length){ //需要修改周期测试时间参数的Ai输出参数，否者输出*
			xml+="<SNo>"+cycPara[ACount-1][0]+"</SNo>\n";
			xml+="<T1>"+cycPara[ACount-1][1]+"</T1>\n";//起始时间
			xml+="<T2>"+cycPara[ACount-1][2]+"</T2>\n";//时间间隔
			xml+="<IP01>"+cycPara[ACount-1][3]+"</IP01>\n";//回传地址
			for(int PCount=2;PCount<=6;PCount++){  //给每个Ai添加P01-P07
				
				xml+="<IP0"+PCount+">"+cycPara[ACount-1][4]+"</IP0"+PCount+">\n";
            }
		}
		else{  //光路中不设置参数的都填充*
			
			    xml+="<SNo>"+'*'+"</SNo>\n";
			    xml+="<T1>"+'*'+"</T1>\n";//起始时间
				xml+="<T2>"+'*'+"</T2>\n";//时间间隔
				for(int PCount=1;PCount<=6;PCount++){
				
				xml+="<IP0"+PCount+">"+'*'+"</IP0"+PCount+">\n";
            }
		}
			xml+="</B"+ACount+">\n";
	}
	
	xml+="</SetCycleTestSegment>\n";
	return xml+="</SegmentCode>";
   }
   /**---------------------------取消周期测试--------------------------*/
   public static String cancelCycTestCode(Map<String, String> indexPara,String[] cancelIndex ){
	    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		xml+="<SegmentCode>\n";
		xml+="<CancelCycleTest>\n";
		xml+="<CMDcode>220</CMDcode>\n";
		xml+="<R>*</R>\n";
		xml+="<CM>"+indexPara.get("CM")+"</CM>\n";
		xml+="<CLP>"+indexPara.get("CLP")+"</CLP>\n";
		xml+="<SN>"+indexPara.get("SN")+"</SN>\n";
		for(int canCount=1;canCount<=Integer.parseInt(String.valueOf(indexPara.get("SN")));canCount++){
			if(canCount<=cancelIndex.length){
				xml+="<E"+canCount+">"+cancelIndex[canCount-1]+"</E"+canCount+">\n";
			}else{
				xml+="<E"+canCount+">"+'*'+"</E"+canCount+">\n";
			}
		}
		
		xml+="</CancelCycleTest>\n";
		return xml+="</SegmentCode>";
   }
   /**------------障碍告警测试-----------
    * 说明：入口参数为Map<String, Object> alarmPara，包含了指令所需的全部数据
    * 分别为  CM(String)  rtuId
    *  CLP(String)    局站Id
    *  AN(String)    告警组数目
    *  groupList(List<Object> ) 全部告警组的所有参数信息
    *  groupList(i) Map<String, Object> 为第i组的参数信息，包括以下内容：
    *     ANo(String) 告警组序号
    *     ASN(String) 光端口数（为下发中含有的总数）
    *     threshodsList(List<Map<String,Object>>) 一个告警组中所有光路的门限和测试参数信息
    *     threshodsList(i) 该告警组下第i条光路的门限和测试参数信息，包括以下内容
    *          ASNo(String) 光端口序号
    *          AT01-AT06(String) 六种门限
    *          TYPE(String) 光纤类型（0：备纤；1：在线纤）
    *          IP01-IP06(String) 回传IP
    *          T3(String) 成功发送告警后的二次发送间隔
    *          T4(String) 告警发送失败后的重发间隔时间
    *          PS(String) 使用优化测试参数还是当前下发参数的标志位(0:使用优化测试参数；1：使用本次下发的参数)
    *          P21-P27(String) 测试条件参数
    * -----*/
  @SuppressWarnings("unchecked")
public static String setAlarmTestCode(Map<String, Object> alarmPara ){
	    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		xml+="<SegmentCode>\n";
		xml+="<SetAlarmTestSegment>\n";
		xml+="<CMDcode>130</CMDcode>\n";
		xml+="<R>*</R>\n";
		xml+="<CM>"+alarmPara.get("CM")+"</CM>\n";
		xml+="<CLP>"+alarmPara.get("CLP")+"</CLP>\n";
		xml+="<AN>"+alarmPara.get("AN")+"</AN>\n";//告警组数目
		List<Object> groupList=(List<Object>) alarmPara.get("groupList");//所有告警组的参数
		for(int groupCount=1;groupCount<=groupList.size();groupCount++){
			Map<String,Object> groupMap=(Map<String, Object>) groupList.get(groupCount-1);//一个告警组的参数
			xml+="<C"+groupCount+">\n";
			xml+="<ANo>"+groupMap.get("ANo")+"</ANo>\n";
			xml+="<ASN>"+groupMap.get("ASN")+"</ASN>\n";
			List<Map<String,Object>>threshodsList=(List<Map<String, Object>>) groupMap.get("threshodsList");//获取一个告警组下所有光路的门限
			for(int routeCount=1;routeCount<=threshodsList.size();routeCount++){
					Map<String, Object>threshodsMap=threshodsList.get(routeCount-1);//获取一条光路的门限
					xml+="<D"+routeCount+">\n";
					  xml+="<ASNo>"+threshodsMap.get("ASNo")+"</ASNo>\n";//光开关端口序号
					  for(int ATI=1;ATI<=6;ATI++){//门限
						  String AT0i="AT0"+ATI;
						  xml+="<AT0"+ATI+">"+threshodsMap.get(AT0i)+"</AT0"+ATI+">\n";
					  }
					  xml+="<TYPE>"+threshodsMap.get("TYPE")+"</TYPE>\n";//光开关端口序号
					  for(int IPI=1;IPI<=6;IPI++){//回传IP
						  String IP0i="IP0"+IPI;
						  xml+="<IP0"+IPI+">"+threshodsMap.get(IP0i)+"</IP0"+IPI+">\n";
					  }
					xml+="<T3>"+threshodsMap.get("T3")+"</T3>\n";  //成功告警的二次告警间隔时间
					xml+="<T4>"+threshodsMap.get("T4")+"</T4>\n"; //告警发送失败的重发告警间隔时间
					xml+="<PS>"+threshodsMap.get("PS")+"</PS>\n";  //0 使用优化测试参数，1使用当前下发参数
					for(int PCount=1;PCount<=7;PCount++){//新的测试参数
						String P2i="P2"+PCount;
						String P2iV=(String) threshodsMap.get(P2i);
						if(PCount==1){
							P2iV+="000";
						}
						xml+="<P2"+PCount+">"+P2iV+"</P2"+PCount+">\n";
					}
				 xml+="</D"+routeCount+">\n";
			}
			
		  xml+="</C"+groupCount+">\n";
		}
		
		xml+="</SetAlarmTestSegment>\n";
		return xml+="</SegmentCode>";
   }
  

  /**------------设置光保护组--
   * 说明：入口参数groupPara(Map<String, Object>)光保护组信息，包含以下内容：
   *      CM(String)：rtuId
   *      CLP:局站ID
   *      PN:光保护组数目
   *      groupList(List<Map<String,Object>>) 所有的保护组配对信息
   *      groupMap(Map<String,Object>) 一个保护组的配对信息,包含以下内容：
   *        ConnectPos String 主从模块连接关系，主-从0:A-A B-B  1:A-C B-D
   *        ModNo:从模块在RTU的模块序号
   *        IP：从模块所在RTU的IP
   *        Recv Map<String,Object>  下行配对组的端口和光开关信息 
   *        Send Map<String,Object>  上行配对组的光开关信息
   *        Recv和Send均包括以下item：
   *           PNo：配对组序号
   *           SNoA：光路A的端口号(在线)
   *           SNoB：光路B的端口号 (备纤) 
   *           SwitchPos:光开关状态16 光开关1-3联通 ；96 光开关1-2联通 
   * --------------*/
@SuppressWarnings("unchecked")
public static String setProtectGroup(Map<String, Object> groupPara ){
	    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		xml+="<SegmentCode>\n";
		xml+="<SetProtectGroup>\n";  
		xml+="<CMDcode>170</CMDcode>\n";
		xml+="<R>*</R>\n";
		xml+="<CM>"+groupPara.get("CM")+"</CM>\n";  
		xml+="<CLP>"+groupPara.get("CLP")+"</CLP>\n";
	    xml+="<PN>"+groupPara.get("PN")+"</PN>\n";
	    List<Object> groupList=(List<Object>) groupPara.get("groupList");//所有的保护组配对信息
	    for(int groupCount=1;groupCount<=groupList.size();groupCount++){
			Map<String,Object> groupMap=(Map<String, Object>) groupList.get(groupCount-1);//一个保护组的配对信息
			xml+="<P"+groupCount+">\n";
			xml+="<IP>"+groupMap.get("IP")+"</IP>\n";
			xml+="<ConnectPos>"+groupMap.get("ConnectPos")+"</ConnectPos>\n";
			xml+="<ModNo>"+groupMap.get("ModNo")+"</ModNo>\n";
			Map<String,Object> recv=(Map<String, Object>)groupMap.get("Recv");
			Map<String,Object> send=(Map<String, Object>)groupMap.get("Send");
			xml+="<Recv>\n";
			xml+="<PNo>"+recv.get("PNo")+"</PNo>\n";
			xml+="<SNoA>"+recv.get("SNoA")+"</SNoA>\n";
			xml+="<SNoB>"+recv.get("SNoB")+"</SNoB>\n";
			xml+="<SwitchPos>"+recv.get("SwitchPos")+"</SwitchPos>\n";
			xml+="</Recv>\n";
			xml+="<Send>\n";
			xml+="<PNo>"+send.get("PNo")+"</PNo>\n";
			xml+="<SNoA>"+send.get("SNoA")+"</SNoA>\n";
			xml+="<SNoB>"+send.get("SNoB")+"</SNoB>\n";
			xml+="<SwitchPos>"+send.get("SwitchPos")+"</SwitchPos>\n";
			xml+="</Send>\n";
			xml+="</P"+groupCount+">\n";
	    }
	    xml+="</SetProtectGroup>\n";
	    return xml+="</SegmentCode>\n";
 }
 
/**---------------取消光保护组--------------
 * 说明：入口参数：Map<String, Object> groupPara
 *               包括：CM
 *                    CLP
 *                    QNs：为List[],每个元素为要取消的保护组号
 * -----*/
@SuppressWarnings("rawtypes")
public static String cancelProtectGroup(Map<String, Object> groupPara ){
    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	xml+="<SegmentCode>\n";
	xml+="<CancelProtectGroup>\n";  
	xml+="<CMDcode>250</CMDcode>\n";
	xml+="<R>*</R>\n";
	xml+="<CM>"+groupPara.get("CM")+"</CM>\n";  
	xml+="<CLP>"+groupPara.get("CLP")+"</CLP>\n";
    List QNs=(List)groupPara.get("QNs");
	xml+="<PN>"+QNs.size()+"</PN>\n";
	for(int SNoIndex=0;SNoIndex<QNs.size();SNoIndex++){
		xml+="<Q"+(SNoIndex+1)+">"+QNs.get(SNoIndex)+"</Q"+(SNoIndex+1)+">\n";
	}
    xml+="</CancelProtectGroup>\n";
    return xml+="</SegmentCode>\n";
}


  
  /**------------障碍告警测试故障告警方式--
   * 说明：入口参数alarmPara(Map<String, Object>)配置rtu告警方式的参数，包含以下内容：
   *      CM(String)：rtuId
   *      CLP:局站ID
   *      AI：告警方式码(1,2,3,4,5)
   *          1：本地可闻可见
   *          2：远端可闻可见
   *          3：本地及远端可闻可见
   *          4：本地可见
   *          5：远端可见
   *          
   * --------------*/
public static String setAlarmAlertWay(Map<String, Object> alarmPara ){
	    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		xml+="<SegmentCode>\n";
		xml+="<SetAlarmSegment>\n";
		xml+="<CMDcode>140</CMDcode>\n";
		xml+="<R>*</R>\n";
		xml+="<CM>"+alarmPara.get("CM")+"</CM>\n";
		xml+="<CLP>"+alarmPara.get("CLP")+"</CLP>\n";
	    xml+="<AI>"+alarmPara.get("AI")+"</AI>\n";
	    xml+="</SetAlarmSegment>\n";
	    return xml+="</SegmentCode>\n";
 }
/**------------请求光保护切换--------
 * 说明：入口参数groupPara(Map<String, Object>)光保护组信息，包含以下内容：
 *      CM(String)：rtuId
 *      CLP:局站ID
 *      PN:光保护组数目
 *      groupList(List<Map<String,Object>>) 所有的保护组配对信息
 *      groupMap(Map<String,Object>) 一个保护组的配对信息,包含以下内容：
 *        PNo：配对组序号
 *        SNoA：光路A的端口号
 *        SNoB：光路B的端口号  
 * --------------*/
@SuppressWarnings("unchecked")
public static String requestRouteProtect(Map<String, Object> groupPara ){
	    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		xml+="<SegmentCode>\n";
		xml+="<RequestCableProtect>\n";  
		xml+="<CMDcode>370</CMDcode>\n";
		xml+="<R>*</R>\n";
		xml+="<CM>"+groupPara.get("CM")+"</CM>\n";  
		xml+="<CLP>"+groupPara.get("CLP")+"</CLP>\n";
	    xml+="<PN>"+groupPara.get("PN")+"</PN>\n";
	    List<Object> groupList=(List<Object>) groupPara.get("groupList");//所有的保护组配对信息
	    for(int groupCount=1;groupCount<=groupList.size();groupCount++){
			Map<String,Object> groupMap=(Map<String, Object>) groupList.get(groupCount-1);//一个保护组的配对信息
			xml+="<G"+groupCount+">\n";
			xml+="<PNo>"+groupMap.get("PNo")+"</PNo>\n";
			xml+="<SNoA>"+groupMap.get("SNoA")+"</SNoA>\n";
			xml+="<SNoB>"+groupMap.get("SNoB")+"</SNoB>\n";
			xml+="<SwitchPos>"+groupMap.get("SwitchPos")+"</SwitchPos>\n";
			xml+="</G"+groupCount+">\n";
	    }
	    xml+="</RequestCableProtect>\n";
	    return xml+="</SegmentCode>\n";
}
  
/**--------------获取光功率数据-
 * 
参数：groupPara (Map<String, Object> ):包含以下内容
     CM:rtuId
     CLP:stationId
     SNo:光端口序号
 * -------------*/
public static String getOpticalValue(Map<String, Object> groupPara ){
	    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		xml+="<SegmentCode>\n";
		xml+="<RequestOpticalPower>\n";  
		xml+="<CMDcode>360</CMDcode>\n";
		xml+="<R>*</R>\n";
		xml+="<CM>"+groupPara.get("CM")+"</CM>\n";  
		xml+="<CLP>"+groupPara.get("CLP")+"</CLP>\n";
	    xml+="<SNo>"+groupPara.get("SNo")+"</SNo>\n";
	    xml+="</RequestOpticalPower>\n";
	    return xml+="</SegmentCode>\n";
}

/**--------------取消告警提示-
 * 取消rtu的告警提示，取消之后将不会以任何新式报警
参数：groupPara (Map<String, Object> ):包含以下内容
     CM:rtuId
     CLP:stationId
* -------------*/
public static String cancelAlarmAlert(Map<String, Object> groupPara ){
	    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		xml+="<SegmentCode>\n";
		xml+="<CancelAlarm>\n";  
		xml+="<CMDcode>240</CMDcode>\n";
		xml+="<R>*</R>\n";
		xml+="<CM>"+groupPara.get("CM")+"</CM>\n";  
		xml+="<CLP>"+groupPara.get("CLP")+"</CLP>\n";
	    xml+="</CancelAlarm>\n";
	    return xml+="</SegmentCode>\n";
}

/**--------------取消障碍告警-
 * 取消rtu的告警提示，取消之后将不会以任何新式报警
参数：groupPara (Map<String, Object> ):包含以下内容
     CM:rtuId
     CLP:stationId
* -------------*/
public static String cancelObstacle(Map<String, Object> groupPara ){
	    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		xml+="<SegmentCode>\n";
		xml+="<CancelAlarmTest >\n";  
		xml+="<CMDcode>230</CMDcode>\n";
		xml+="<R>*</R>\n";
		xml+="<CM>"+groupPara.get("CM")+"</CM>\n";  
		xml+="<CLP>"+groupPara.get("CLP")+"</CLP>\n";
		String[] SNos=(String[])groupPara.get("SNos");
		xml+="<SN>"+SNos.length+"</SN>\n";
		for(int SNoIndex=0;SNoIndex<SNos.length;SNoIndex++){
			xml+="<F"+(SNoIndex+1)+">"+SNos[SNoIndex]+"</F"+(SNoIndex+1)+">\n";
		}
	    xml+="</CancelAlarmTest>\n";
	    return xml+="</SegmentCode>\n";
}
/**
 * 设置RTU模式
 * @param  para  Map<String, Object> 包含以下字段
 *        CM：RTU标识
 *        CLP：局站标识
 *        modules：List<Map<String,String>> 各模块信息
 *        每个module为一个 Map<String,String>，包含以下字段
 *          KNo：子模块物理编号(1-8)
 *          Type:子单元模块类型（ 1： 在纤， 2： 备纤， 3： 保护-主， 4：在线OPM，5：保护-从 ）
 * **/
@SuppressWarnings("unchecked")
public static String setRtuMode(Map<String, Object> para){
	    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		xml+="<SegmentCode>\n";
		xml+="<SetRTUMode>\n";  
		xml+="<CMDcode>180</CMDcode>\n";
		xml+="<R>*</R>\n";
		xml+="<CM>"+para.get("CM")+"</CM>\n";  
		xml+="<CLP>"+para.get("CLP")+"</CLP>\n";
		List<Map<String,String>> modules=(List<Map<String, String>>) para.get("modules");
		xml+="<MN>"+modules.size()+"</MN>\n";
		for(int count=0;count<modules.size();count++){
			xml+="<K"+(count+1)+">\n";
			 Map<String,String> module=(Map<String, String>) modules.get(count);
			 xml+="<KNo>"+module.get("KNo")+"</KNo>\n";
			 xml+="<Type>"+module.get("Type")+"</Type>\n";
			xml+="</K"+(count+1)+">\n";
		}
		xml+="</SetRTUMode>\n";
		return xml+="</SegmentCode>\n";
}

/**
 * 清除RTU模式，相当于使RTU离线或删除RTU
 * @param  para  Map<String, Object> 包含以下字段
 *        CM：RTU标识
 *        CLP：局站标识
 *       
 * **/
public static String cancelRtuMode(Map<String, Object> para){
	    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		xml+="<SegmentCode>\n";
		xml+="<CancelRTUMode>\n";  
		xml+="<CMDcode>260</CMDcode>\n";
		xml+="<R>*</R>\n";
		xml+="<CM>"+para.get("CM")+"</CM>\n";  
		xml+="<CLP>"+para.get("CLP")+"</CLP>\n";
		xml+="</CancelRTUMode>\n";
		return xml+="</SegmentCode>\n";
}
/**
 * RTU
 * 定时同步
 * @param para包括以下参数
 *    CM
 *    CLP
 *    T8其中T8为当前时间，时间戳
 * **/
public static String adjustRtuTime(Map<String, Object> para){
    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	xml+="<SegmentCode>\n";
	xml+="<SetReferenceTime>\n";  
	xml+="<CMDcode>150</CMDcode>\n";
	xml+="<R>*</R>\n";
	xml+="<CM>"+para.get("CM")+"</CM>\n";  
	xml+="<CLP>"+para.get("CLP")+"</CLP>\n";
	xml+="<T8>"+para.get("T8")+"</T8>\n";
	xml+="</SetReferenceTime>\n";
	return xml+="</SegmentCode>\n";
}
/**
 * 获取RTU当前时间
 * 
 * @param para包括以下参数
 *    CM
 *    CLP
 *    
 * **/
public static String getRtuTime(Map<String, Object> para){
    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	xml+="<SegmentCode>\n";
	xml+="<RequestReferenceTime>\n";  
	xml+="<CMDcode>320</CMDcode>\n";
	xml+="<R>*</R>\n";
	xml+="<CM>"+para.get("CM")+"</CM>\n";  
	xml+="<CLP>"+para.get("CLP")+"</CLP>\n";
	xml+="<T8>"+"*</T8>\n";
	xml+="</RequestReferenceTime>\n";
	return xml+="</SegmentCode>\n";
}
/**
 * 设置RTU网络参数
 * @param para包括以下字段
 *     CM
 *     CLP
 *     IP：RTU IP地址
 *     subnetMask：当前网络的子网掩码
 *     Gateway:网关地址
 * **/
public static String setRtuNetwork(Map<String, Object> para){
    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	xml+="<SegmentCode>\n";
	xml+="<SetNetwork>\n";  
	xml+="<CMDcode>160</CMDcode>\n";
	xml+="<R>*</R>\n";
	xml+="<CM>"+para.get("CM")+"</CM>\n";  
	xml+="<CLP>"+para.get("CLP")+"</CLP>\n";
	xml+="<IP>"+para.get("IP")+"</IP>\n";
	xml+="<Netmask>"+para.get("subnetMask")+"</Netmask>\n";
	xml+="<Gateway>"+para.get("Gateway")+"</Gateway>\n";
	xml+="</SetNetwork>\n";
	return xml+="</SegmentCode>\n";
}

/**
 * 设置RTU端口占用,当光路生成时下发
 * @param  para Map<String, Object>包括以下参数
 *         CLP
 *         CM
 *         ports List<Integer> 为占用的端口 
 *         types List<String> 光路状态  0：备纤  1：在线 
 * **/
@SuppressWarnings("unchecked")
public static String setPortOccupy(Map<String, Object> para){
    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	xml+="<SegmentCode>\n";
	xml+="<SetRTUPort>\n";  
	xml+="<CMDcode>190</CMDcode>\n";
	xml+="<R>*</R>\n";
	xml+="<CM>"+para.get("CM")+"</CM>\n";  
	xml+="<CLP>"+para.get("CLP")+"</CLP>\n";
	List<Integer> ports=(List<Integer>)para.get("ports");
	List<String> types=(List<String>)para.get("types");
	xml+="<PN>"+ports.size()+"</PN>\n";
	for(int index=0;index<ports.size();index++){
		xml+="<P"+(index+1)+">\n";
	        xml+="<SNo>"+ports.get(index)+"</SNo>\n";
	        xml+="<Type>"+types.get(index)+"</Type>\n";
		xml+="</P"+(index+1)+">\n";
		
	}
	
	xml+="</SetRTUPort>\n";
	return xml+="</SegmentCode>\n";
}
/**
 * 解除RTU端口占用,当取消光路时下发
 * @param  para Map<String, Object>包括以下参数
 *         CLP
 *         CM
 *         ports List<Integer> 为占用的端口 
 * **/
@SuppressWarnings("unchecked")
public static String cancelPortOccupy(Map<String, Object> para){
    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	xml+="<SegmentCode>\n";
	xml+="<CancelRTUPort>\n";  
	xml+="<CMDcode>270</CMDcode>\n";
	xml+="<R>*</R>\n";
	xml+="<CM>"+para.get("CM")+"</CM>\n";  
	xml+="<CLP>"+para.get("CLP")+"</CLP>\n";
	List<Integer> ports=(List<Integer>)para.get("ports");
	xml+="<PN>"+ports.size()+"</PN>\n";
	for(int index=0;index<ports.size();index++){
		xml+="<P"+(index+1)+">\n";
		 xml+="<SNo>"+ports.get(index)+"</SNo>\n";
        xml+="</P"+(index+1)+">\n";
	}
	
	xml+="</CancelRTUPort>\n";
	return xml+="</SegmentCode>\n";
}

/**
 * 获取RTU网络参数**/
public static String getRtuNetwork(Map<String, Object> para){
    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	xml+="<SegmentCode>\n";
	xml+="<RequestNetwork>\n";  
	xml+="<CMDcode>330</CMDcode>\n";
	xml+="<R>*</R>\n";
	xml+="<CM>"+para.get("CM")+"</CM>\n";  
	xml+="<CLP>"+para.get("CLP")+"</CLP>\n";
	xml+="<IP>"+"*</IP>\n";
	xml+="<Netmask>"+"*</Netmask>\n";
	xml+="<Gateway>"+"*</Gateway>\n";
	xml+="</RequestNetwork>\n";
	return xml+="</SegmentCode>\n";
}
/**
 * 重启RTU
 * 说明：入口参数Map<String, Object> para
 * 包含 CM CLP Time**/
public static String restartRtu(Map<String, Object> para){
    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	xml+="<SegmentCode>\n";
	xml+="<RequestRebootRtu>\n";  
	xml+="<CMDcode>380</CMDcode>\n";
	xml+="<R>*</R>\n";
	xml+="<CM>"+para.get("CM")+"</CM>\n";  
	xml+="<CLP>"+para.get("CLP")+"</CLP>\n";
	xml+="<Time>"+para.get("Time")+"</Time>\n";
	xml+="</RequestRebootRtu>\n";
	return xml+="</SegmentCode>\n";
}
   /**----------------------回复码-----------------------------*/
   public static String responseCode(String StatusCode){
	    String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		xml+="<ConfirmMessage>\n";
		xml+="<StatusCode>"+StatusCode+"</StatusCode>\n";
		return xml+="</ConfirmMessage>\n";
   }
   
}
