package fiberMonitor.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import domain.Alarm;
import domain.AlarmBank;
import domain.Alarm_ways;
import domain.Areas;
import domain.Curves;
import domain.Event_curves;
import domain.Optimize_parameters;
import domain.Preparatory_routes;
import domain.Priorities;
import domain.Routes;
import domain.Rtus;
import domain.Stations;
import domain.Threshold;
import net.sf.json.JSONArray;
import service.AddService;
import service.AlterService;
import service.DeleteService;
import service.FindService;
import fiberMonitor.bean.AnalysisData;
import fiberMonitor.bean.CurvesJson;
import fiberMonitor.bean.MessageUtil;
import fiberMonitor.bean.NumConv;
import fiberMonitor.bean.TCPComndUtil;
import fiberMonitor.bean.TcpClient;
import fiberMonitor.bean.root;

@Controller
public class RouteController {
	@Resource(name="findService")
	private FindService findService;
    @Resource(name="addService") 
	private AddService addService;
    @Resource(name="alterService") 
   	private AlterService alterService;
    @Resource(name="deleteService") 
   	private DeleteService delService;
   	/**生成前端树的json数据**/
	@RequestMapping(value="create_tree")
	public void  createTree (HttpServletRequest request,HttpServletResponse response) throws IOException{
		/******获取区域，局站，rtu表中的所有的信息*******/
		List<Areas> areas = findService.findAllAreas();
		List<Stations> stations=findService.findAllStations();		
		List<Rtus> rtus=findService.findAllRtus();
		for(int i = 0;i<rtus.size();i++){
			if(!rtus.get(i).getType().equals("普通rtu"))
				rtus.remove(i--);
		}
		List<root> roots=new ArrayList<root >(1);
		root root = new root();
		roots.add(root);
		/************新建一个list，将三个对象的list进行拼接，并生成.jon文件，方便之后生成左侧的树形图***********/
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
	@RequestMapping(value="curveTree")
	public void  curveTree (HttpServletRequest request,HttpServletResponse response) throws IOException{
		/******获取区域，局站，rtu表中的所有的信息*******/
		List<Areas> areas = findService.findAllAreas();
		List<Stations> stations=findService.findAllStations();		
		List<Rtus> rtus=findService.findAllRtus();
		List<Routes> routes=findService.findAllRoutes();
		for(int i = 0;i<rtus.size();i++){
			if(!rtus.get(i).getType().equals("普通rtu"))
				rtus.remove(i--);
		}
		List<root> roots=new ArrayList<root >(1);
		root root = new root();
		roots.add(root);
		/************新建一个list，将三个对象的list进行拼接，并生成.jon文件，方便之后生成左侧的树形图***********/
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
		//System.out.println(list);
		out.flush();
		out.close();
 }
   /**
    * 点击光路获取当前光路的测试曲线
    * 返回的数据包含总的记录条数和第一页的数据
    * **/
	@RequestMapping(value="listCurvesByRouteId")	
	public void listCurvesByRouteId(HttpServletRequest request,HttpServletResponse response) throws IOException{   
		long routeId =Long.parseLong(request.getParameter("routeId"));
		int perCount =Integer.parseInt(request.getParameter("perCount"));//每页的条目数
		List<Curves> curves =findService.findCurvesByRouteId(routeId);
		List<CurvesJson> curveJson=new ArrayList<CurvesJson>();
		int pageCount=(int) Math.ceil(curves.size()*1.0/(perCount*1.0));//页码数
		for(int i=0;i<curves.size();i++){
			if(curveJson.size()>=perCount)//只传送首页的数据
				break;
			CurvesJson cJson = new CurvesJson(); 
			cJson.setCurve_id(curves.get(i).getId());
			cJson.setCurve_type(curves.get(i).getCurve_type());
			cJson.setDescription(curves.get(i).getDescription());
			cJson.setReferring(curves.get(i).getReferring());
			cJson.setTest_date(curves.get(i).getTest_date());
			cJson.setRoutename(curves.get(i).getRoute().getRoute_name());
			curveJson.add(cJson);
		}
		Map<String,Object>responseData=new LinkedHashMap<String,Object>();
		pageCount=pageCount==0?1:pageCount;
		responseData.put("curves", curveJson);
		responseData.put("pageCount", pageCount);
		JSONArray curvesJson=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(curvesJson.toString());
		out.flush();
		out.close();	
        
	}
	/**分页获取曲线数据**/
	@RequestMapping(value="getCurvesByPage")	
	public void getCurvesByPage(HttpServletRequest request,HttpServletResponse response) throws IOException{   
		long routeId =Long.parseLong(request.getParameter("routeId"));
		int perCount =Integer.parseInt(request.getParameter("perCount"));//每页的条目数
		int page=Integer.parseInt(request.getParameter("page"));//当前页码
		List<Curves> curves =findService.findPaginationCurvesByRouteId(routeId, page, perCount);
		List<CurvesJson> curveJson=new ArrayList<CurvesJson>();
		for(int i=0;i<curves.size();i++){
			CurvesJson cJson = new CurvesJson(); 
			cJson.setCurve_id(curves.get(i).getId());
			cJson.setCurve_type(curves.get(i).getCurve_type());
			cJson.setDescription(curves.get(i).getDescription());
			cJson.setReferring(curves.get(i).getReferring());
			cJson.setTest_date(curves.get(i).getTest_date());
			cJson.setRoutename(curves.get(i).getRoute().getRoute_name());
			curveJson.add(cJson);
		}
		Map<String,Object>responseData=new LinkedHashMap<String,Object>();
		responseData.put("curves", curveJson);
		JSONArray curvesJson=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(curvesJson.toString());
		out.flush();
		out.close();	
        
	}
	/***周期测试曲线获取
	 * 返回的数据包含总的记录条数和第一页的数据
	* */
		@RequestMapping(value="periodListCurves")	
		public  void periodlistCurves (HttpServletRequest request,HttpServletResponse response) throws IOException{
			long routeId =Long.parseLong(request.getParameter("routeId"));
			List<Curves> curves=findService.findCurveByTypeAndRouteId(routeId,"周期测试");
			int perCount =Integer.parseInt(request.getParameter("perCount"));//每页的条目数
			int pageCount=(int) Math.ceil(curves.size()*1.0/(perCount*1.0));//页码数
			//System.out.println("size:"+curves.size()+"\npage:"+pageCount);
			List<CurvesJson> curveJson=new ArrayList<CurvesJson>();
			for(int i=0;i<curves.size();i++){
				if(curveJson.size()>=perCount)//只传送首页的数据
					break;
				CurvesJson rJson = new CurvesJson();  
				rJson.setCurve_id(curves.get(i).getId());
				rJson.setCurve_type(curves.get(i).getCurve_type());
				rJson.setDescription(curves.get(i).getDescription());
				rJson.setReferring(curves.get(i).getReferring());
				rJson.setTest_date(curves.get(i).getTest_date());
				rJson.setRoutename(curves.get(i).getRoute().getRoute_name());
				curveJson.add(rJson); 
			}
			Map<String,Object>responseData=new LinkedHashMap<String,Object>();
			pageCount=pageCount==0?1:pageCount;
			responseData.put("curves", curveJson);
			responseData.put("pageCount", pageCount);
			JSONArray responseD=JSONArray.fromObject(responseData);
			response.setContentType("text/xml");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(responseD);			
			out.flush();
			out.close();	
	        
		}	
	
	/**分页获取周期测试曲线数据**/
	@RequestMapping(value="getPeriodCurvesByPage")	
	public void getPeriodCurvesByPage(HttpServletRequest request,HttpServletResponse response) throws IOException{   
		long routeId =Long.parseLong(request.getParameter("routeId"));
		int perCount =Integer.parseInt(request.getParameter("perCount"));//每页的条目数
		int page=Integer.parseInt(request.getParameter("page"));//当前页码
		List<Curves> curves =findService.findPaginationPeriodCurvesByRouteId(routeId, page, perCount);
		List<CurvesJson> curveJson=new ArrayList<CurvesJson>();
		//System.out.println(curves.size());
		for(int i=0;i<curves.size();i++){
			CurvesJson cJson = new CurvesJson(); 
			cJson.setCurve_id(curves.get(i).getId());
			cJson.setCurve_type(curves.get(i).getCurve_type());
			cJson.setDescription(curves.get(i).getDescription());
			cJson.setReferring(curves.get(i).getReferring());
			cJson.setTest_date(curves.get(i).getTest_date());
			cJson.setRoutename(curves.get(i).getRoute().getRoute_name());
			curveJson.add(cJson);
		}
		Map<String,Object>responseData=new LinkedHashMap<String,Object>();
		responseData.put("curves", curveJson);
		JSONArray curvesJson=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(curvesJson.toString());
		out.flush();
		out.close();	
        
	}
	/**查找曲线**/
	@RequestMapping(value="searchCurve")	
	public void searchCurve(HttpServletRequest request,HttpServletResponse response) throws IOException{   
		long routeId =Long.parseLong(request.getParameter("routeId"));
		int perCount =Integer.parseInt(request.getParameter("perCount"));//每页的条目数
		String curveType =request.getParameter("curveType");
		List<Curves> curves =findService.findCurveByTypeAndRouteId(routeId,curveType);
		int pageCount=(int) Math.ceil(curves.size()*1.0/(perCount*1.0));//页码数
		List<CurvesJson> curveJson=new ArrayList<CurvesJson>();
		for(int i=curves.size()-1;i>=0;i--){
			if(curveJson.size()>=perCount)//只传送首页的数据
				break;
			CurvesJson cJson = new CurvesJson(); 
			cJson.setCurve_id(curves.get(i).getId());
			cJson.setCurve_type(curves.get(i).getCurve_type());
			cJson.setDescription(curves.get(i).getDescription());
			cJson.setReferring(curves.get(i).getReferring());
			cJson.setTest_date(curves.get(i).getTest_date());
			cJson.setRoutename(curves.get(i).getRoute().getRoute_name());
			curveJson.add(cJson);
		}
		boolean status=curveJson.size()>0?true:false;
		Map<String,Object>responseData=new LinkedHashMap<String,Object>();
		pageCount=pageCount==0?1:pageCount;
		responseData.put("status", status);
		responseData.put("curves", curveJson);
		responseData.put("pageCount", pageCount);
		JSONArray curvesJson=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(curvesJson.toString());
		//System.out.println("response:"+curvesJson);
		out.flush();
		out.close();	
        
	}
	/***
	 * 根据曲线Id获取曲线数据和事件点、测试条件等信息
	 * */
	  @SuppressWarnings("unchecked")
	@RequestMapping("getCurveAndParas")
	public void getCurveById(HttpServletRequest request,HttpServletResponse response) throws IOException{
		long curveId=Long.parseLong(request.getParameter("curveId"));
		Curves curve= findService.findCurve(curveId);
		byte[] curveData= curve.getCurve_data();//获得曲线数据
		List<Event_curves> events=curve.getEvents();
	    int endSite=0;//事件点的结束位置
	    Long startSiteId=null;
	    float endValue=0;//结束事件处的值
	    int maxRange=curve.getTest_parameter().getMax_range();//量程
	    float reIndex=Float.parseFloat(curve.getTest_parameter().getRefractive_index());//折射率
	    int sample=curve.getTest_parameter().getSample_fre();//采样率
	    List<Object> eventList=new ArrayList<>();
	    List<Integer> eventSites=new ArrayList<>();//存放各个事件点位置
	    DecimalFormat format2=new DecimalFormat("#0.00"); 
		DecimalFormat format3=new DecimalFormat("#0.000"); 
	    double pointsPerM=(2*reIndex*sample/(2.9979*Math.pow(10, 8)));//每m的采样个数
	    if(pointsPerM==0){
	    	pointsPerM=1;
	    }
	    for(int index=0;index<events.size();index++){
	    	if(events.get(index).getEvent_type().equals("起始事件")){
	    		startSiteId=events.get(index).getId();
			}
	    }
		for(int index=0;index<events.size();index++){
			if(events.get(index).getEvent_type().equals("结束事件")){
				endSite=Integer.parseInt(events.get(index).getPotition());
				endValue=(float)(endSite*2.997*Math.pow(10,8)/(2*reIndex*sample));
			}
			Long thisSiteId=events.get(index).getId();
			int thisSite=Integer.parseInt(events.get(index).getPotition());
			if(thisSiteId-startSiteId<1+events.size()){/**排除用户添加的事件点的影响
			    如果当前事件点的Id减去开始事件点的Id大于所有事件点的个数，则该事件点为后来添加的*/
				//System.out.println("this event site:"+thisSite);
				eventSites.add(thisSite);
			}
			Map<String,Object> eventPara=new LinkedHashMap<String,Object>();//一个事件点
			eventPara.put("type", events.get(index).getEvent_type());//事件类型
			eventPara.put("site", thisSite);//位置
			eventPara.put("insertLoss", events.get(index).getInsertion_loss());//插入损耗
			eventPara.put("returnLoss", events.get(index).getReturn_loss());//回传损耗
			eventPara.put("totalLoss", events.get(index).getTotal_loss());//总损耗
			eventPara.put("attenIndex", events.get(index).getAttenuation_coeff());//衰减系数
			eventList.add(eventPara);
		}
		//System.out.println("结束事件位置："+endSite);
		int dataPointSize=AnalysisData.getIntData(curveData,0,4);  //读取数据点个数
		//System.out.println("数据点个数："+dataPointSize+"\n事件点个数："+eventSites.size());
		List<String[]> dataPoint=new ArrayList<String[]>();//新建float数组，用于存放每个数据点的数据
		int eventIndex=0;
		for(int size=0;size<dataPointSize;size++){
			if(size<=endSite+(int)Math.floor(80*pointsPerM)){//结束事件之前
				String[] oneSite={
									format2.format((size)*2.9979*Math.pow(10,8)/(2*reIndex*sample)),
									format3.format(AnalysisData.getIntData(curveData,4+size*2,2)/1000.0-5.0)//还原数据,保留三位小数
					      		};
				/**距离换算 x=m*c/(2*n*F) *m为数组序列号  c为光速3*10^8m/s
				*n为折射率    F为采样率 */ 
				if(size==0){
					oneSite[0]="0";
				}
				dataPoint.add(oneSite);
				boolean isNearByEvent=false;
				for(int index=eventIndex;index<eventSites.size();index++){
					if((eventSites.get(index)-(int)Math.floor((40*pointsPerM))<=size)&&(size<=eventSites.get(index)+(int)Math.floor(80*pointsPerM))){
						isNearByEvent=true;//该点靠近事件点
						    if(size==eventSites.get(index)){
								eventIndex++;
								Map<String,Object> eventPara=(Map<String, Object>) eventList.get(index);
								eventPara.replace("site", dataPoint.size()-1);
								eventList.set(index, eventPara);//将事件点的位置改为转换后的值
							}
							break;//只要有符合的范围就跳出循环，减少循环次数
						}
					}
				  if(!isNearByEvent){//非事件点附近的点，进行隔10点采样
					size+=6;
				  }
			}
			else{   //结束事件20m以后，隔15点采样
				String[] oneSite={
									format2.format(endValue+(maxRange-endValue)/(dataPointSize-endSite-1)*(size-endSite)),
									format3.format(AnalysisData.getIntData(curveData,4+(size)*2,2)/1000.0-5.0)//还原数据,保留三位小数
									
								};
				dataPoint.add(oneSite);//还原数据,保留三位小数
				size+=15;
			}
        }
		String[] oneSite={
							format2.format(endValue+(maxRange-endValue)/(dataPointSize-endSite-1)*(dataPointSize-1-endSite)),
							format3.format(AnalysisData.getIntData(curveData,4+(dataPointSize-1)*2,2)/1000.0-5.0)//还原数据,保留三位小数
						};
		dataPoint.add(oneSite);//还原数据,保留三位小数
		Map<String, Object> testPara=new LinkedHashMap<String, Object>();//测试参数
		testPara.put("P11", maxRange);//量程，m
		testPara.put("P12", curve.getTest_parameter().getPulse_width());//脉宽
		testPara.put("P13", curve.getTest_parameter().getWave_length());//波长
		testPara.put("P14", curve.getTest_parameter().getAverage_times());//平均次数
		testPara.put("P15",format2.format(reIndex));//折射率
		testPara.put("P16", curve.getTest_parameter().getNon_refractive_threshold());//非反射门限
		testPara.put("P17", curve.getTest_parameter().getEnd_threshold());//结束门限
		testPara.put("testMode", curve.getTest_parameter().getTestMode());//测试模式
		testPara.put("testWay", curve.getTest_parameter().getTestWay());//测试方法
		testPara.put("sample", sample);//采样频率
		testPara.put("costTime", curve.getTest_parameter().getTime_length());//耗时
		Map<String, Object> testResult=new LinkedHashMap<String, Object>();//测试结果
		testResult.put("chainLength", curve.getTest_parameter().getPath_length());//链长
		testResult.put("chainLoss", curve.getTest_parameter().getPath_loss());//链损耗
		testResult.put("chainAtten", curve.getTest_parameter().getPath_attenuation());//链衰减
		testResult.put("testTime", curve.getTest_date());//测试时间
		/**-----------回传数据---------*/
		Map<String, Object> CurveData=new LinkedHashMap<String, Object>();
		CurveData.put("curve",dataPoint);//曲线数据
		CurveData.put("events",eventList);//事件点
		CurveData.put("testPara",testPara);//测试条件
		CurveData.put("testResult",testResult);//测试结果
		JSONArray responseData=JSONArray.fromObject(CurveData);//将反馈曲线数据生成JSON文件
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.println(responseData); 
	    out.flush();
	    out.close();
	  }
	  
/***-获取光路
 * 用于障碍告警优先级分组设置
 * 只显示可以障碍告警的光路*/
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping("getRoutes")
  public void getRoutes(HttpServletRequest request,HttpServletResponse response) throws IOException{
	Long rtuId=Long.parseLong(request.getParameter("rtuId"));
	List<Routes> routes=findService.findRoutesByRtuId(rtuId);
	LinkedHashMap[] responseData=new LinkedHashMap[2];
	responseData[0]=new LinkedHashMap<Object, Object>();
	responseData[1]=new LinkedHashMap<Object, Object>();
	boolean status=false;
    if(routes.size()>0){
		status=true;
		for(int count=0;count<routes.size();count++){
			/***剔除不可进行障碍告警测试的光路**/
			Long CM=routes.get(count).getRtu_id();
			Rtus rtu=findService.findRtu(CM);
			int order=routes.get(count).getRtu_model_order();
			String moudleType=rtu.getInstallInfo().substring(order-1,order);  
			/**光路所在的模块类型：1为在线 2为备纤 3为保护-主  4为在线OPM
            	备纤均可以测光功率，在线纤只有3和4可以  */
			if(routes.get(count).getIs_online()&&(!(moudleType.equals("4")||moudleType.equals("3")))){
			  continue;
			}
			Map<String,Object> routePara=new LinkedHashMap<String, Object>();
			routePara.put("id", routes.get(count).getId());
			String routeName=routes.get(count).getRoute_name();
			if(routeName.equals("")||routeName==null){
				routeName="光路"+routes.get(count).getId();
			}
			routePara.put("text",routeName);
			responseData[1].put(String.valueOf(count), routePara);
		}
	}
	responseData[0].put("status", status);
	JSONArray responseD=JSONArray.fromObject(responseData);
	response.setContentType("text/xml");
	response.setCharacterEncoding("utf-8");
	//System.out.println("回传数据为："+responseD);
	PrintWriter out = response.getWriter();
	out.print(responseD);			
	out.flush();
	out.close();	
	
}

/***
 * 获取在进行障碍告警测试的光路
 * */
@RequestMapping("getOnObstacleRoutes")
  public void getOnObstacleRoutes(HttpServletRequest request,HttpServletResponse response) throws IOException{
	Long rtuId=Long.parseLong(request.getParameter("rtuId"));
	List<Routes> routes=findService.findRoutesByRtuId(rtuId);
	for(int index=0;index<routes.size();index++){
		if(!(routes.get(index).getIsObstalce())){//未进行障碍告警测试,remove
			routes.remove(index--);
		}
	}
	@SuppressWarnings("unchecked")
	LinkedHashMap<Object, Object>[] responseData=new LinkedHashMap[2];
	responseData[0]=new LinkedHashMap<Object, Object>();
	responseData[1]=new LinkedHashMap<Object, Object>();
	boolean status=false;
    if(routes.size()>0){
		status=true;
		for(int count=0;count<routes.size();count++){
			Map<String,Object> routePara=new LinkedHashMap<String, Object>();
			String SNoAndId=routes.get(count).getRtu_port_order()+","+routes.get(count).getId();//形式为SNo,Id的组合 如SNO=4，Id=2 结果为4,2
			routePara.put("id", SNoAndId);
			String routeName=routes.get(count).getRoute_name();
			if(routeName.equals("")||routeName==null){
				routeName="光路"+routes.get(count).getId();
			}
			routePara.put("text",routeName);
			responseData[1].put(String.valueOf(count), routePara);
		}
	}
	responseData[0].put("status", status);
	JSONArray curvesJson=JSONArray.fromObject(responseData);
	response.setContentType("text/xml");
	response.setCharacterEncoding("utf-8");
	PrintWriter out = response.getWriter();
	out.print(curvesJson.toString());			
	out.flush();
	out.close();	
	
}
/***
 * 通过rtuId获取当前rtu下的告警组
 * */
@RequestMapping("getPriorityLevel")
  public void getPriorityLevel(HttpServletRequest request,HttpServletResponse response) throws IOException{
	Long rtuId=Long.parseLong(request.getParameter("rtuId"));
	List<Priorities> prioritiesDefault=findService.findAllPrioritiesByRtuId((long)0);
	List<Priorities> priorities=findService.findAllPrioritiesByRtuId(rtuId);
	for(int index=0;index<priorities.size();index++){//默认+新增
		prioritiesDefault.add(priorities.get(index));
	}
	Map <String,Object>responseData=new LinkedHashMap<String,Object>();
	boolean status=false;
	Long []id=new Long[prioritiesDefault.size()];
	String []name=new String[prioritiesDefault.size()];
	String []description=new String[prioritiesDefault.size()];
	if(prioritiesDefault.size()>0){
		status=true;
		for(int count=0;count<prioritiesDefault.size();count++){
			id[count]=prioritiesDefault.get(count).getId();
			name[count]=prioritiesDefault.get(count).getName();
			description[count]=prioritiesDefault.get(count).getDescription();
		}
	}
    responseData.put("status", status);
    responseData.put("id", id);
    responseData.put("name", name);
    responseData.put("description", description); 
	JSONArray responseJson=JSONArray.fromObject(responseData);
	response.setContentType("text/xml");
	response.setCharacterEncoding("utf-8");
	PrintWriter out=response.getWriter();
	out.print(responseJson.toString());			
	out.flush();
	out.close();		
}
/***
 * 通过Id获取告警组
 * */
@RequestMapping("getAlarmGroupById")
public void getAlarmGroupById(HttpServletRequest request,HttpServletResponse response) throws IOException{
	Long id=Long.parseLong(request.getParameter("id"));
	Map <String,Object>responseData=new LinkedHashMap<String,Object>();
	Priorities prioritiy=findService.findPriorityById(id);
	boolean status=false;
	if(prioritiy!=null){
		  status=true;
		  responseData.put("status", status);
		  responseData.put("id", id);
		  responseData.put("name", prioritiy.getName());
		  responseData.put("description", prioritiy.getDescription()); 
	}
    JSONArray responseJson=JSONArray.fromObject(responseData);
	response.setContentType("text/xml");
	response.setCharacterEncoding("utf-8");
	PrintWriter out = response.getWriter();
	out.print(responseJson.toString());			
	out.flush();
	out.close();	
}
/**给光路设置分组信息
 * 前端下发的参数包括
 *  rtuId(Long)
 *  groupId(Long)
 *  routeIds(int[]) 
 **/
	@RequestMapping("addGroupInfo")
	public void addTempGroupInfo(HttpServletRequest request,HttpServletResponse response) throws IOException{
		ResourceController.printAllRequestPara(request);
		JSONArray groupArray=JSONArray.fromObject(request.getParameter("groupArray"));
		boolean status=true;
		for(int index=0;index<groupArray.size();index++){
			JSONArray groupPara=groupArray.getJSONArray(index);//一个暂存组的信息
			Long groupId=Long.parseLong(groupPara.getString(0));
			String[]routeIds=groupPara.getString(1).split(",");
			if(routeIds.length>0){
				for(int routeIndex=0;routeIndex<routeIds.length;routeIndex++){
					Long routeId=Long.parseLong(routeIds[routeIndex]);
					Map<String, Object> tempInfo=new LinkedHashMap<String, Object>();
					tempInfo.put("priotity_id", groupId);
					tempInfo.put("priotity_name", findService.findPriorityById(groupId).getName());
					status=alterService.updateRoute(routeId, tempInfo);
				}
			}
		}
		Map <String,Object>responseData=new LinkedHashMap<String,Object>();
		responseData.put("status",status);
		JSONArray responseD=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();	
	}
    /**
	 * 核对是否设置了优化测试参数
	 * */
	@RequestMapping("checkOpticalPara")
	public void getGroupRoutesByGroupId(HttpServletRequest request,HttpServletResponse response) throws IOException{
		ResourceController.printAllRequestPara(request);
		JSONArray routeIdArray=JSONArray.fromObject(request.getParameter("routeIdS"));
		List<Long>unSetRouteIds=new ArrayList<Long>();
		for(int count=0;count<routeIdArray.size();count++){
			Long routeId=Long.parseLong(routeIdArray.getString(count));
			if(findService.findRoute(routeId).getOptimize_parameter()==null){
				unSetRouteIds.add(routeId);
			}
		}
		Map <String,Object>responseData=new LinkedHashMap<String,Object>();
		boolean status=false;
		if(unSetRouteIds.size()>0){
			status=true;
		}
		responseData.put("status", status);
		responseData.put("unSetRoutes",unSetRouteIds);
		JSONArray responseD=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();	
	}
	/**获取优化测试参数
	 * */
	@RequestMapping("getOpticalPara")
	public void getOpticalPara(HttpServletRequest request,HttpServletResponse response) throws IOException{
		ResourceController.printAllRequestPara(request);
		Long routeId=Long.parseLong(request.getParameter("routeId"));
		List<Object> opticalPara=new ArrayList<Object>();
		Map <String,Object>responseData=new LinkedHashMap<String,Object>();
		boolean status=false;
		Optimize_parameters Optical=findService.findRoute(routeId).getOptimize_parameter();
			if(Optical!=null){
				status=true;
				opticalPara.add(Optical.getMax_range());
				opticalPara.add(Optical.getPulse_width());
				opticalPara.add(Optical.getWave_length());
				opticalPara.add(Optical.getAverage_times());
				opticalPara.add(Optical.getRefractive_index());
				opticalPara.add(Optical.getNon_refractive_threshold());
				opticalPara.add(Optical.getEnd_threshold());
			}
		responseData.put("status", status);
		responseData.put("optiPara",opticalPara);
		JSONArray responseD=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();	
	}
	/**
	 * 获取所有的告警方式
	 * */
	@SuppressWarnings("unchecked")
	@RequestMapping("getAllAlarmWays")
	public void getAllAlarmWays(HttpServletRequest request,HttpServletResponse response) throws IOException{
		List<Alarm_ways> alarmWays=findService.findAllAlarmWay();
		LinkedHashMap<Object, Object>[] responseData=new LinkedHashMap[2];
		responseData[0]=new LinkedHashMap<Object, Object>();
		responseData[1]=new LinkedHashMap<Object, Object>();
		boolean status=false;
	    if(alarmWays.size()>0){
			status=true;
			for(int count=0;count<alarmWays.size();count++){
				Map<String,Object> routePara=new LinkedHashMap<String, Object>();
				routePara.put("id", alarmWays.get(count).getId());
				String routeName=alarmWays.get(count).getAlarm_name();
				if(routeName.equals("")||routeName==null){
					routeName="光路"+alarmWays.get(count).getId();
				}
				routePara.put("text",routeName);
				responseData[1].put(String.valueOf(count), routePara);
			}
		}
		responseData[0].put("status", status);
		JSONArray responseD=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();	
	}
	/**
     * 修改告警组
     * */
	@RequestMapping("alterAlarmGroup")
	public void alterAlarmGroup(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Long id=Long.parseLong(request.getParameter("id"));
		String name=request.getParameter("name");
		String description=request.getParameter("description");
		Map<String,Object> priorityPara=new LinkedHashMap<String, Object>();
		Map<String,Object> responseData=new LinkedHashMap<String, Object>();
		priorityPara.put("name", name);
		priorityPara.put("description", description);
		boolean status=alterService.updatePriorities(id, priorityPara);
		if(status){//如果成功，修改数据库中光路priotity_name字段
			List<Routes> routes=findService.findRoutesByPriotityId(id);
			Map<String,Object>routePara=new LinkedHashMap<String,Object>();
			routePara.put("priotity_name",name);
 		    for(int SNoIndex=0;SNoIndex<routes.size();SNoIndex++){//修改数据库的字段
 		    	status=alterService.updateRoute(routes.get(SNoIndex).getId(), routePara);
 		    }
		}
		responseData.put("status", status);
		JSONArray responseD=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();	
	}
	/**
	 * 增加告警组
	 * */
	@RequestMapping("addAlarmGroup")
	public void addAlarmGroup(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Long CM=Long.parseLong(request.getParameter("CM"));
		String name=request.getParameter("name");
		String description=request.getParameter("description");
		Priorities priority=new Priorities();
		priority.setName(name);
		priority.setDescription(description);
		Map<String,Object> responseData=new LinkedHashMap<String, Object>();
		boolean status=false;
		priority.setRtuId(CM);
		Serializable groupId=addService.addPriorities(priority);
		if(groupId!=null){
			status=true;
		}
		responseData.put("status", status);
		JSONArray responseD=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();		
 }
	/**
	 * 获取发射机端的光功率值
	 * **/
	@RequestMapping("route/getStartPval")
	public void getStartPval(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Long routeId=Long.parseLong(request.getParameter("routeId"));
		Routes route=findService.findRoute(routeId);
		boolean status=false;
		Map<String,Object> responseData=new LinkedHashMap<String, Object>();
		float startPVal=0;//发射机的光功率值
		  if(route.getSwitchRtuId()!=null){
			  responseData.put("routeName", route.getRoute_name());
			  Rtus pwRtu=findService.findRtu(route.getSwitchRtuId());
			  String pv=pwRtu.getPVal();
			  /**如果数据库中存有值，则获取数据库中的发射端光功率值**/
			  if(pv!=null){
				  if(!pv.equals("0.00")){
					  status=true;
					  startPVal=Float.parseFloat(pv);
				  }
			  }
			  else{//数据库中的值无效，读取光功率值
				  String ip=pwRtu.getRtu_url();//服务器IP
			      int port=8088;//服务器端口
			      TcpClient tcpConn =new TcpClient(ip,port,2000);
			      byte[] code=TCPComndUtil.comndGetPowerVal();//获取发射机的光功率值
			   	  status=tcpConn.sendData(code);
			      while(!tcpConn.getIsReady());//等待完成
			      if(status){
			    	byte []rec=tcpConn.recData();//接收数据
			    	tcpConn.closeConn();//发送后释放连接
			    	Map<String,Object>resData=TCPComndUtil.anlyzeRecData(rec);
			    	status=(boolean)resData.get("status");
			    	if(status){
			    		float pw=(float)resData.get("pwVal");//读取到的发射机光功率值
			    		startPVal=pw/pwRtu.getDividNum();//进行分光
			    	}
			    }
			  }
		 }
	    if(status){
	    	 DecimalFormat format2=new DecimalFormat("#0.00"); 
	    	 responseData.put("pVal", format2.format(startPVal));
	    }
		responseData.put("status", status);
		JSONArray responseD=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		//System.out.println("回传数据为："+responseD);
		PrintWriter out = response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();		
  }

	/***
	 * 设置光功率门限**/
	@RequestMapping("route/setThreshold")
	public void setThreshold(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Long routeId=Long.parseLong(request.getParameter("routeId"));
		Threshold threshold=findService.findThresholdByRouteId(routeId);
		boolean status=false;
		if(threshold!=null){//存在门限，进行修改
			threshold.setThre1(request.getParameter("thre1"));
			threshold.setThre2(request.getParameter("thre2"));
			threshold.setThre3(request.getParameter("thre3"));
			threshold.setThre4(request.getParameter("thre4"));
			threshold.setSwitchThre(request.getParameter("threSwitch"));
			threshold.setRoute(findService.findRoute(routeId));
			status=alterService.alterThreshold(threshold);
		}
		else{
			threshold=new Threshold();
			threshold.setThre1(request.getParameter("thre1"));
			threshold.setThre2(request.getParameter("thre2"));
			threshold.setThre3(request.getParameter("thre3"));
			threshold.setThre4(request.getParameter("thre4"));
			threshold.setSwitchThre(request.getParameter("threSwitch"));
			threshold.setRoute(findService.findRoute(routeId));
			Serializable threId=addService.addThreshold(threshold);
			if(threId!=null){
				status=true;
			}
		}
	    Map<String,Object> responseData=new LinkedHashMap<String, Object>();
		responseData.put("status", status);
		JSONArray responseD=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();	
  }
  /***
   * 增加事件
   * */
  @RequestMapping("curve/checkCurve/addEvent")
  public void addEventPoint(HttpServletRequest request,HttpServletResponse response) throws IOException{
	ResourceController.printAllRequestPara(request);
	Event_curves eventPoint=new Event_curves();
	Long curveId=Long.parseLong(request.getParameter("curveId"));
	eventPoint.setCurves(findService.findCurve(curveId));
	eventPoint.setPotition(request.getParameter("startSite"));
	eventPoint.setAttenuation_coeff(request.getParameter("lossIndex"));
	eventPoint.setEvent_type(request.getParameter("type"));
	Serializable eventId= addService.addEventCurve(eventPoint);
	boolean status=(eventId!=null);
	Map<String,Object> responseData=new LinkedHashMap<String, Object>();
	responseData.put("status", status);
	JSONArray responseD=JSONArray.fromObject(responseData);
	response.setContentType("text/xml");
	response.setCharacterEncoding("utf-8");
	PrintWriter out = response.getWriter();
	out.print(responseD);			
	out.flush();
	out.close();	
	
}
  /***
   * 删除曲线
   * */
  @RequestMapping("curve/delCurve")
  public void delCurve(HttpServletRequest request,HttpServletResponse response) throws IOException{
	Long curveId=Long.parseLong(request.getParameter("curveId"));
	Long routeId=findService.findCurve(curveId).getRoute().getId();
	boolean status=true;
	delService.deleteCurve(curveId);
	if(findService.findCurve(curveId)!=null){
		status=false;
	}
	Map<String,Object> responseData=new LinkedHashMap<String, Object>();
	responseData.put("status", status);
	responseData.put("routeId", routeId);
	JSONArray responseD=JSONArray.fromObject(responseData);
	response.setContentType("text/xml");
	response.setCharacterEncoding("utf-8");
	PrintWriter out = response.getWriter();
	out.print(responseD);			
	out.flush();
	out.close();	
  }
  /**
   * *设为参考曲线
   * */
  @RequestMapping("curve/checkCurve/setRefrence")
  public void saveAsReference(HttpServletRequest request,HttpServletResponse response) throws IOException{
	Long curveId=Long.parseLong(request.getParameter("id"));
	Curves curve=findService.findCurve(curveId);
	Long routeId=curve.getRoute().getId();//光路
	boolean status=true;
	Curves refrenceCurve=findService.findReferenceCurveByRouteId(routeId);//原来的参考曲线
	if(refrenceCurve!=null){//取消原来参考曲线的擦可靠状态
		refrenceCurve.setReferring(false);
		status=alterService.alterCurve(refrenceCurve);
    } 
	if(status){//设置当前曲线的参考状态
		curve.setReferring(true);
		status=alterService.alterCurve(curve);
	}
	Map<String,Object> responseData=new LinkedHashMap<String, Object>();
	responseData.put("status", status);
	JSONArray responseD=JSONArray.fromObject(responseData);
	response.setContentType("text/xml");
	response.setCharacterEncoding("utf-8");
	PrintWriter out = response.getWriter();
	out.print(responseD);			
	out.flush();
	out.close();	
  } 
  /**
   * *查看参考曲线
   * */  
    @SuppressWarnings("unchecked")
	@RequestMapping("getReferenceCurve")
	public void getReferenceCurve(HttpServletRequest request,HttpServletResponse response) throws IOException{
		long curveId=Long.parseLong(request.getParameter("id"));
		Curves curve=findService.findCurve(curveId);
		Curves referenceCurve= findService.findReferenceCurveByRouteId(curve.getRoute().getId());
		Map<String, Object> CurveData=new LinkedHashMap<String, Object>();
		int status=0;
		if(referenceCurve!=null){
			if(referenceCurve.getId()==curveId){//参考曲线为当前查看的曲线
				status=1;	
			}
			else{
				status=2;
				byte[] curveData= referenceCurve.getCurve_data();//获得曲线数据
				List<Event_curves> events=referenceCurve.getEvents();
			    int endSite=0;//事件点的结束位置
			    float endValue=0;//结束事件处的值
			    int maxRange=referenceCurve.getTest_parameter().getMax_range();//量程
			    float reIndex=Float.parseFloat(referenceCurve.getTest_parameter().getRefractive_index());//折射率
			    int sample=referenceCurve.getTest_parameter().getSample_fre();//采样率
			    List<Object> eventList=new ArrayList<>();
			    List<Integer> eventSites=new ArrayList<>();//存放各个事件点位置
			    DecimalFormat format2=new DecimalFormat("#0.00"); 
				DecimalFormat format3=new DecimalFormat("#0.000"); 
				Long startSiteId=null;
			    double pointsPerM=(2*reIndex*sample/(2.9979*Math.pow(10, 8)));//每m的采样个数
			    if(pointsPerM==0){
			    	pointsPerM=1;
			    }
			    for(int index=0;index<events.size();index++){
			    	if(events.get(index).getEvent_type().equals("起始事件")){
			    		startSiteId=events.get(index).getId();
					}
			    }
				for(int index=0;index<events.size();index++){
					if(events.get(index).getEvent_type().equals("结束事件")){
						endSite=Integer.parseInt(events.get(index).getPotition());
						endValue=(float)(endSite*2.997*Math.pow(10,8)/(2*reIndex*sample));
					}
					Long thisSiteId=events.get(index).getId();
					int thisSite=Integer.parseInt(events.get(index).getPotition());
					if(thisSiteId-startSiteId<events.size()+1){//排除用户添加的事件点的影响
						eventSites.add(thisSite);
					}
					Map<String,Object> eventPara=new LinkedHashMap<String,Object>();//一个事件点
					eventPara.put("type", events.get(index).getEvent_type());//事件类型
					eventPara.put("site", thisSite);//位置
					eventPara.put("insertLoss", events.get(index).getInsertion_loss());//插入损耗
					eventPara.put("returnLoss", events.get(index).getReturn_loss());//回传损耗
					eventPara.put("totalLoss", events.get(index).getTotal_loss());//总损耗
					eventPara.put("attenIndex", events.get(index).getAttenuation_coeff());//衰减系数
					eventList.add(eventPara);
				}
				int dataPointSize=AnalysisData.getIntData(curveData,0,4);  //读取数据点个数
				List<String[]> dataPoint=new ArrayList<String[]>();//新建float数组，用于存放每个数据点的数据
			    int eventIndex=0;
				for(int size=0;size<dataPointSize;size++){
					if(size<=endSite+(int)Math.floor(80*pointsPerM)){//结束事件之前
						String[] oneSite={  
											format2.format((size)*2.9979*Math.pow(10,8)/(2*reIndex*sample)),
											format3.format(AnalysisData.getIntData(curveData,4+size*2,2)/1000.0-5.0)//还原数据,保留三位小数
										 };
						/**距离换算 x=m*c/(2*n*F) *m为数组序列号  c为光速3*10^8m/s
						*n为折射率    F为采样率 */ 
						if(size==0){
							oneSite[0]="0";
						}
						dataPoint.add(oneSite);
						boolean isNearByEvent=false;
						for(int index=eventIndex;index<eventSites.size();index++){
							if((eventSites.get(index)-(int)Math.floor((40*pointsPerM))<=size)&&(size<=eventSites.get(index)+(int)Math.floor(80*pointsPerM))){
								    isNearByEvent=true;//该点靠近事件点
								    if(size==eventSites.get(index)){
										eventIndex++;
										Map<String,Object> eventPara=(Map<String, Object>) eventList.get(index);
										eventPara.replace("site", dataPoint.size()-1);
										eventList.set(index, eventPara);//将事件点的位置改为转换后的值
									}
									break;//只要有符合的范围就跳出循环，减少循环次数
								}
							}
						  if(!isNearByEvent)//非事件点附近的点，进行隔6点采样
							{
								size+=6;
							}
					}
					else{   //结束事件20m以后，隔15点采样
						String[] oneSite={  
											format2.format(endValue+(maxRange-endValue)/(dataPointSize-endSite-1)*(size-endSite)),
											format3.format(AnalysisData.getIntData(curveData,4+(size)*2,2)/1000.0-5.0)//还原数据,保留三位小数
										 };
						dataPoint.add(oneSite);//还原数据,保留三位小数
						size+=15;
					}
		        }
				String[] oneSite={
									format2.format(endValue+(maxRange-endValue)/(dataPointSize-endSite-1)*(dataPointSize-1-endSite)),
									format3.format(AnalysisData.getIntData(curveData,4+(dataPointSize-1)*2,2)/1000.0-5.0)//还原数据,保留三位小数
								};
				dataPoint.add(oneSite);//还原数据,保留三位小数
				Map<String, Object> testPara=new LinkedHashMap<String, Object>();//测试参数
				testPara.put("P11", maxRange);//量程，m
				testPara.put("P12", curve.getTest_parameter().getPulse_width());//脉宽
				testPara.put("P13", curve.getTest_parameter().getWave_length());//波长
				testPara.put("P14", curve.getTest_parameter().getAverage_times());//平均次数
				testPara.put("P15",format2.format(reIndex));//折射率
				testPara.put("P16", curve.getTest_parameter().getNon_refractive_threshold());//非反射门限
				testPara.put("P17", curve.getTest_parameter().getEnd_threshold());//结束门限
				testPara.put("testMode", curve.getTest_parameter().getTestMode());//测试模式
				testPara.put("testWay", curve.getTest_parameter().getTestWay());//测试方法
				testPara.put("sample", sample);//采样频率
				testPara.put("costTime", curve.getTest_parameter().getTime_length());//耗时
				Map<String, Object> testResult=new LinkedHashMap<String, Object>();//测试结果
				testResult.put("chainLength", curve.getTest_parameter().getPath_length());//链长
				testResult.put("chainLoss", curve.getTest_parameter().getPath_loss());//链损耗
				testResult.put("chainAtten", curve.getTest_parameter().getPath_attenuation());//链衰减
				testResult.put("testTime", curve.getTest_date());//测试时间
				/**-----------回传数据---------*/
				CurveData.put("curve",dataPoint);//曲线数据
				CurveData.put("events",eventList);//事件点
				CurveData.put("testPara",testPara);//测试条件
				CurveData.put("testResult",testResult);//测试结果
			}
		}
		CurveData.put("status", status);
		JSONArray responseData=JSONArray.fromObject(CurveData);//将反馈曲线数据生成JSON文件
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.println(responseData); 
	    out.flush();
	    out.close();
	  }
    /**
     * *获取告警
     * **/  
    @RequestMapping("alarm/getWarn")
	public void getWarn(HttpServletRequest request,HttpServletResponse response) throws IOException{
		long routeId =Long.parseLong(request.getParameter("routeId"));
		int perCount =Integer.parseInt(request.getParameter("perCount"));//每页的条目数
		List<Alarm> alarm=findService.findAlarmByRouteId(routeId);
		int pageCount=(int) Math.ceil(alarm.size()*1.0/(perCount*1.0));//页码数
		boolean status=false;
		Map<String,Object> responseData=new LinkedHashMap<String, Object>();
		if(alarm!=null){
			List<Map<String,Object>> alarms=new ArrayList<Map<String, Object>>();
			status=true;
			for(int index=0;index<alarm.size();index++){
				if(alarms.size()>=perCount)
					break;
				Map<String,Object> alar=new LinkedHashMap<String, Object>();
				alar.put("source", alarm.get(index).getAlarm_source());
				alar.put("level", alarm.get(index).getAlarm_level());
				alar.put("type", alarm.get(index).getAlarm_type());
				alar.put("alarmTime", alarm.get(index).getAlarm_time());
				if("OTDR告警".equals(alarm.get(index).getAlarm_type())){
					   alar.put("distance","距RTU"+alarm.get(index).getDistance()+"m");
					}
					else{
						alar.put("distance","----");
					}
				alar.put("handleTime", alarm.get(index).getHandle_time());
				String isHandle="未处理";
				if(alarm.get(index).getIs_handle()){
					isHandle="已处理";
				}
				alar.put("isHandle",isHandle);
				alar.put("handleUser", alarm.get(index).getHandle_user());
				alar.put("routeName", alarm.get(index).getRoute().getRoute_name());
				alar.put("id", alarm.get(index).getId());
				alarms.add(alar);
			}
			responseData.put("alarms", alarms);
		}
		pageCount=pageCount>0?pageCount:1;
		responseData.put("pageCount", pageCount);
		responseData.put("status", status);
		JSONArray responseD=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();
    }
    /**分页读取告警**/
	@RequestMapping(value="getWarnsByPage")	
	public void getWarnsByPage(HttpServletRequest request,HttpServletResponse response) throws IOException{   
		long routeId =Long.parseLong(request.getParameter("routeId"));
		int perCount =Integer.parseInt(request.getParameter("perCount"));//每页的条目数
		int page=Integer.parseInt(request.getParameter("page"));//当前页码
		List<Alarm> alarm =findService.findPaginationAlarmsByRouteId(routeId, page, perCount);
		boolean status=false;
		Map<String,Object> responseData=new LinkedHashMap<String, Object>();
		if(alarm!=null){
			List<Map<String,Object>> alarms=new ArrayList<Map<String, Object>>();
			status=true;
			for(int index=0;index<alarm.size();index++){
				if(alarms.size()>=perCount)
					break;
				Map<String,Object> alar=new LinkedHashMap<String, Object>();
				alar.put("source", alarm.get(index).getAlarm_source());
				alar.put("level", alarm.get(index).getAlarm_level());
				alar.put("type", alarm.get(index).getAlarm_type());
				alar.put("alarmTime", alarm.get(index).getAlarm_time());
				if("OTDR告警".equals(alarm.get(index).getAlarm_type())){
					 alar.put("distance","距RTU"+alarm.get(index).getDistance()+"m");
				}
				else{
					alar.put("distance","----");
				}
				alar.put("handleTime", alarm.get(index).getHandle_time());
				String isHandle="未处理";
				if(alarm.get(index).getIs_handle()){
					isHandle="已处理";
				}
				alar.put("isHandle",isHandle);
				alar.put("handleUser", alarm.get(index).getHandle_user());
				alar.put("routeName", alarm.get(index).getRoute().getRoute_name());
				alar.put("id", alarm.get(index).getId());
				alarms.add(alar);
			}
			responseData.put("alarms", alarms);
		}
		responseData.put("status", status);
		JSONArray curvesJson=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(curvesJson.toString());
		out.flush();
		out.close();	
        
	}
	 /***
	  * 查询告警
	 * @throws ParseException
	 *  **/  
	@RequestMapping("alarm/checkAlarm")
    public void checkAlarm(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
		Long routeId=Long.parseLong(request.getParameter("routeId"));
		int perCount =Integer.parseInt(request.getParameter("perCount"));//每页的条目数
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String start=request.getParameter("startTime");
		if(start.equals("")){
			start="2000-10-10 00:00:00";
		}
		Date startTime=format.parse(start);
		String end=request.getParameter("endTime");
		if(end.equals("")){
			end=NumConv.currentTime();
		}
		Date endTime=format.parse(end);
		Boolean handle=request.getParameter("status").equals("")?null:Boolean.parseBoolean(request.getParameter("status"));
		if(!request.getParameter("routeId").equals("")){
			routeId=Long.parseLong(request.getParameter("routeId"));
		}
		List<Alarm> alarm=findService.findAlarmByRouteId(routeId);
		boolean status=false;
		Map<String,Object> responseData=new LinkedHashMap<String, Object>();
		if(alarm!=null){
			List<Map<String,Object>> alarms=new ArrayList<Map<String, Object>>();
			for(int i=0;i<alarm.size();i++){
				Date alarmTime=format.parse(alarm.get(i).getAlarm_time());
				if(handle!=null&&alarm.get(i).getIs_handle()!=handle){//进行光路选择时将不符合条件的剔除
					alarm.remove(i--);
				}
				else if((alarmTime.getTime()<startTime.getTime()||alarmTime.getTime()>endTime.getTime())){
					alarm.remove(i--);
				}
			}
			int pageCount=(int) Math.ceil(alarm.size()*1.0/(perCount*1.0));//页码数
			for(int index=0;index<alarm.size();index++){
				Date alarmTime=format.parse(alarm.get(index).getAlarm_time());
				if((alarmTime.getTime()>=startTime.getTime()&&alarmTime.getTime()<=endTime.getTime())){
					Map<String,Object> alar=new LinkedHashMap<String, Object>();
					alar.put("source",alarm.get(index).getAlarm_source());
					alar.put("level",alarm.get(index).getAlarm_level());
					alar.put("type",alarm.get(index).getAlarm_type());
					alar.put("alarmTime",alarm.get(index).getAlarm_time());
					if("OTDR告警".equals(alarm.get(index).getAlarm_type())){
						  alar.put("distance","距RTU"+alarm.get(index).getDistance()+"m");
					}
					else{
						alar.put("distance","----");
					}
					alar.put("handleTime",alarm.get(index).getHandle_time());
					String isHandle="未处理";
					if(alarm.get(index).getIs_handle()){
						isHandle="已处理";
					}
					alar.put("isHandle",isHandle);
					alar.put("handleUser", alarm.get(index).getHandle_user());
					alar.put("routeName", alarm.get(index).getRoute().getRoute_name());
					alar.put("id", alarm.get(index).getId());
					alarms.add(alar);
				 }
				 if(alarms.size()>0){
					   status=true;
				   }
			}
			responseData.put("alarms", alarms);
			responseData.put("pageCount", pageCount);
		}
		responseData.put("status", status);
		JSONArray responseD=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();
	 }
	  /**------------------忽略告警信息---------------------*/
		@RequestMapping("alarm/handleAlarm/ignore")
	    public void warnIgnore(HttpServletRequest request,HttpServletResponse response) throws IOException{
			long warnId= Long.parseLong(request.getParameter("warnId"));
			//更改数据库中的更改告警状态、处理用户和处理时间
			Alarm alertInfo = findService.findAlarmById(warnId);
	        alertInfo.setIs_handle(true);
	        Subject currentUser = SecurityUtils.getSubject();//获取当前用户
	  	    String Account=currentUser.getPrincipal().toString();//当前用户的账号
	        alertInfo.setHandle_user(Account);
	        alertInfo.setHandle_time(NumConv.currentTime());
	        boolean status=alterService.alterAlarm(alertInfo);
	        Map<String,Object> responseData=new LinkedHashMap<String,Object>();
	        responseData.put("status", status);
	        responseData.put("routeId", alertInfo.getRoute().getId());
	        JSONArray responseJson=JSONArray.fromObject(responseData); 
	    	//System.out.println(responseJson);
			PrintWriter out=response.getWriter();
			out.println(responseJson);
			out.flush(); 
			out.close(); 
		}
		 /**删除告警信息*/
		@RequestMapping("alarm/delAlarm")
	    public void delAlarm(HttpServletRequest request,HttpServletResponse response) throws IOException{
			long warnId= Long.parseLong(request.getParameter("warnId"));
			long routeId= findService.findAlarmById(warnId).getRoute().getId();
	        boolean status=delService.deleteAlarm(warnId);
	        Map<String,Object> responseData=new LinkedHashMap<String,Object>();
	        responseData.put("status", status);
	        responseData.put("routeId", routeId);
	        JSONArray responseJson=JSONArray.fromObject(responseData); 
	    	//System.out.println(responseJson);
			PrintWriter out=response.getWriter();
			out.println(responseJson);
			out.flush(); 
			out.close(); 
		}
	/**
	 * *处理告警(邮件)**/
	@RequestMapping("alarm/handleAlarm/email")
	public void handleAlarmByEmail(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
		long warnId=Long.parseLong(request.getParameter("alarmId"));
		//更改数据库中的更改告警状态、处理用户和处理时间
		Alarm alertInfo = findService.findAlarmById(warnId);
	    alertInfo.setIs_handle(true);
	    Subject currentUser = SecurityUtils.getSubject();//获取当前用户
	  	String Account=currentUser.getPrincipal().toString();//当前用户的账号
	    alertInfo.setHandle_user(Account);
	    alertInfo.setHandle_time(NumConv.currentTime());
	    String info=request.getParameter("info");
	    String email=request.getParameter("email");
	    String head=request.getParameter("head");
	    List<String> emails=new ArrayList<String>();
		emails.add(email);
	    boolean status=MessageUtil.sendEmail(emails, head, info);
	    String err="";
	    if(status){
	       status=alterService.alterAlarm(alertInfo);
	    }
	    else{
	       err="邮件发送失败";
	    }
	    Map<String,Object> responseData=new LinkedHashMap<String, Object>();
	    responseData.put("status", status);
	    responseData.put("err",err);
	    responseData.put("routeId", alertInfo.getRoute().getId());
		JSONArray responseD=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();
	}	  
	/**
	 * *处理告警(短信)
	 * **/
	@RequestMapping("alarm/handleAlarm/textMessage")
	public void handleAlarmBySMS(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
		long warnId= Long.parseLong(request.getParameter("alarmId"));
		//更改数据库中的更改告警状态、处理用户和处理时间
		Alarm alertInfo = findService.findAlarmById(warnId);
	    alertInfo.setIs_handle(true);
	    Subject currentUser = SecurityUtils.getSubject();//获取当前用户
	  	String Account=currentUser.getPrincipal().toString();//当前用户的账号
	    alertInfo.setHandle_user(Account);
	    alertInfo.setHandle_time(NumConv.currentTime());
	    String info=request.getParameter("info");
	    String phone=request.getParameter("phone");
	    boolean status=false;
	    String err="";
	    List<String> phones=new ArrayList<String>();
	    phones.add(phone);
	    status= MessageUtil.sendMessage(phones, info);
	    if(status){
	       status=alterService.alterAlarm(alertInfo);
	    }else{
	       err="短息发送失败";
	    }
	    Map<String,Object> responseData=new LinkedHashMap<String, Object>();
	    responseData.put("status", status);
	    responseData.put("err",err);
	    responseData.put("routeId", alertInfo.getRoute().getId());
		JSONArray responseD=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();
	}
	/**
	 * **设置光路时获取预备光路所在的RTU模块类型
	 * **/
	@RequestMapping("getPreparatoryRouteMoudle")
	public void getPreparatoryRouteMoudle(HttpServletRequest request,HttpServletResponse response) throws IOException{
		long ID=Long.parseLong(request.getParameter("preId"));
		//更改数据库中的更改告警状态、处理用户和处理时间
		Preparatory_routes preparatoryRoute = findService.findPreparatoryRoute(ID);
		int moduleOrder=preparatoryRoute.getJumper_route().getModelOrder();//模块序号
		String installInfo=findService.findRtu(preparatoryRoute.getRtu_id()).getInstallInfo();
		String moudleType=installInfo.substring(moduleOrder-1, moduleOrder);
		boolean isUpLink=false;
		switch(moudleType){
			case"1"://在线
			case"4"://在线光功率
				moudleType="1";
				break;
			case"2"://备纤
				moudleType="2";
				break;
			case"3"://保护-主
				moudleType="3";
				/**在保护模块上，端口A、C(端口序号1,3,5,7)为下行链路，B、D(端口序号为2,4,6,8)为上行链路 **/
				int portOrder=preparatoryRoute.getRtu_port_order();
				if((portOrder)%8%2==0){
					isUpLink=true;
				}
				break;
			default:
				moudleType="3";
				break;
		}
	    boolean status=true;
	    Map<String,Object> responseData=new LinkedHashMap<String, Object>();
	    responseData.put("status", status);
	    responseData.put("isUpLink", isUpLink);
	    responseData.put("moudleType",moudleType);
	    JSONArray responseD=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();
	}
	/**
	 * *修改光路时获取光路所在模块信息，限制光路的状态
	 * 只有在光路处于保护-主模块且未设置保护组时允许修改光路状态
	 * **/
	@RequestMapping("getRouteMoudleType")
	public void getRouteMoudleType(HttpServletRequest request,HttpServletResponse response) throws IOException{
		long ID=Long.parseLong(request.getParameter("routeId"));
		//更改数据库中的更改告警状态、处理用户和处理时间
		Routes route = findService.findRoute(ID);
		int moduleOrder=route.getRtu_model_order();//模块序号
		String installInfo=findService.findRtu(route.getRtu_id()).getInstallInfo();
		String moudleType=installInfo.substring(moduleOrder-1, moduleOrder);
		Map<String,Object> responseData=new LinkedHashMap<String, Object>();
		boolean isProtect=false;
		switch(moudleType){
			case"1"://在线
			case"4"://在线光功率
				moudleType="1";
				break;
			case"2"://备纤
				moudleType="2";
				break;
			case"3"://保护-主
				if(route.getIsProtect()){//判断是否有保护组
					isProtect=true;//有保护组的时候不能修改状态
				}
				break;
			default:
				moudleType=route.getIs_online()?"1":"2";//其他情形时由光路状态决定
				break;
		}
	    boolean status=true;
	    responseData.put("isOnline", route.getIs_online());
	    responseData.put("isUplink", route.getIsUplink());
	    responseData.put("status", status);
	    responseData.put("isProtect", isProtect);
	    responseData.put("moudleType",moudleType);
	    JSONArray responseD=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();
	}
	/**
	 * 障碍告警下列出光路，只列可以测试光功率的光路
	 * 包括备纤(3模块除外)  在线纤OPM
	 * **/
	@SuppressWarnings("unchecked")
	@RequestMapping("getObstacleRouteByRtuId")
	public void getObstacleRouteByRtuId(HttpServletRequest request,HttpServletResponse response) throws IOException{
		  Long id = Long.parseLong(request.getParameter("id"));
		  List<Routes> routes=findService.findRoutesByRtuId(id);
		  for(int index=0;index<routes.size();index++){
				Long CM=routes.get(index).getRtu_id();
				Rtus rtu=findService.findRtu(CM);
				int order=routes.get(index).getRtu_model_order();
				String moudleType=rtu.getInstallInfo().substring(order-1,order);
				    /**光路所在的模块类型
		 			 1为在线 2为备纤 3为保护-主  4为在线OPM
		 			  备纤均可测试光功率，
					  在线只有模块3和模块4的可以测光功率
					  除了模块3上的光路外，能进行光功率测试的光路均可以进行障碍告警
					 **/
			    if(routes.get(index).getIs_online()&&(!moudleType.equals("4"))||moudleType.equals("3")){
		    		routes.remove(index--);
		    	}
		  } 	
		  LinkedHashMap<String,Object>[] responseData=new LinkedHashMap[routes.size()+1];
		  responseData[0]=new LinkedHashMap<String,Object>();
		  boolean status=false;
		 
		  if(responseData.length>1){
			  status=true;
			  for(int count=0;count<routes.size();count++){
				  String routeStatus="备纤";
				  String isObstacle="否";
				  if(routes.get(count).getIs_online()){
					  routeStatus="在线纤";
				  }
				  if(routes.get(count).getIsObstalce()){
					  isObstacle="是";
				  }
				  responseData[count+1]=new LinkedHashMap<String, Object>();
				  responseData[count+1].put("id",routes.get(count).getId());
				  responseData[count+1].put("name",routes.get(count).getRoute_name());
				  responseData[count+1].put("stationAId",routes.get(count).getStation_a_id());
				  responseData[count+1].put("stationAName",routes.get(count).getStation_a_name());
				  responseData[count+1].put("rtuId",routes.get(count).getRtu_id());
				  responseData[count+1].put("rtuName",routes.get(count).getRtu_name());
				  int portOrder=routes.get(count).getRtu_port_order();
				  int moudleOrder=routes.get(count).getRtu_model_order();
				  responseData[count+1].put("rtuOrder",getPortOrder(portOrder,moudleOrder));
				  responseData[count+1].put("stationZId",routes.get(count).getStation_z_id());
				  responseData[count+1].put("stationZName",routes.get(count).getStation_z_name());
				  responseData[count+1].put("frameId",routes.get(count).getFrame_z_id());
				  responseData[count+1].put("frameOrder",routes.get(count).getFrame_z_order());
				  responseData[count+1].put("length",routes.get(count).getLength());
				  responseData[count+1].put("status",routeStatus);
				  String isBroken=routes.get(count).getIsBroken()?"损坏":"正常";
				  responseData[count+1].put("isBroken",isBroken);
				  responseData[count+1].put("description",routes.get(count).getDescription());
				  responseData[count+1].put("createTime",routes.get(count).getCreate_date());
				  responseData[count+1].put("createUser",routes.get(count).getCreate_user());
				  responseData[count+1].put("alterTime",routes.get(count).getAlter_date());
				  responseData[count+1].put("alterUser",routes.get(count).getAlter_user());
				  responseData[count+1].put("isObstacle",isObstacle);
				  responseData[count+1].put("priorityId",routes.get(count).getPriotityId());
				  responseData[count+1].put("priorityName",routes.get(count).getPriotityName());
				  /**=========Z端配线架=======*/
				  responseData[count+1].put("frameName",findService.findDistributingFrame(routes.get(count).getFrame_z_id()).getFrame_name());//
			  }
		  }
		    responseData[0].put("status", status);
			JSONArray responseJson=JSONArray.fromObject(responseData);
			PrintWriter out=response.getWriter();
			out.println(responseJson);
			out.flush();
			out.close();  
	}
	/**
	   * 端口号的格式为 Mi-Xj
	   *其中Mi表示为第几个模块 Xj表示为该模块的编号
	   *@param portOrder 端口序号
	   *@param moudleOrder 模块序号
	   */
	public static String getPortOrder(int portOrder,int moudleOrder){
		  String[] portRows={"A","B","C","D"};
		  String[] portIndexs={"1","2","3","4"};
		  int ind=(portOrder-1)%8;//得出在模块上的序号  1-8
		  String order="M"+moudleOrder+"-"+portRows[ind%4]+portIndexs[ind/4];
		  return order;
	}
	/**---------------------通过RTU id获取光路-------------------------------*/
	  @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("getRouteByRtuId")
	   public void getRouteByRtuId(HttpServletRequest request,HttpServletResponse response) throws IOException{
		  Long id = Long.parseLong(request.getParameter("id"));
		  List<Routes> routes=findService.findRoutesByRtuId(id);
		  LinkedHashMap[] responseData=new LinkedHashMap[routes.size()+1];
		  responseData[0]=new LinkedHashMap();
		  boolean status=false;
		  if(responseData.length>1){
			  status=true;
			  for(int count=0;count<routes.size();count++){
				  String routeStatus=routes.get(count).getIs_online()?"在线纤":"备纤";
				  String isObstacle=routes.get(count).getIsObstalce()?"是":"否";
				  String isUplink=routes.get(count).getIsUplink()?"上行":"下行";
				  String isPeriod=routes.get(count).getIs_period()?"是":"否";
				  responseData[count+1]=new LinkedHashMap();
				  responseData[count+1].put("id",routes.get(count).getId());
				  responseData[count+1].put("name",routes.get(count).getRoute_name());
				  responseData[count+1].put("stationAId",routes.get(count).getStation_a_id());
				  responseData[count+1].put("stationAName",routes.get(count).getStation_a_name());
				  responseData[count+1].put("rtuId",routes.get(count).getRtu_id());
				  responseData[count+1].put("rtuName",routes.get(count).getRtu_name());
				  int portOrder=routes.get(count).getRtu_port_order();
				  int moudleOrder=routes.get(count).getRtu_model_order();
				  responseData[count+1].put("rtuOrder",getPortOrder(portOrder,moudleOrder));
				  responseData[count+1].put("stationZId",routes.get(count).getStation_z_id());
				  responseData[count+1].put("stationZName",routes.get(count).getStation_z_name());
				  responseData[count+1].put("frameId",routes.get(count).getFrame_z_id());
				  responseData[count+1].put("frameName",findService.findDistributingFrame(routes.get(count).getFrame_a_id()).getFrame_name());
				  responseData[count+1].put("frameOrder",routes.get(count).getFrame_z_order());
				  responseData[count+1].put("length",routes.get(count).getLength());
				  responseData[count+1].put("status",routeStatus);
				  String isBroken=routes.get(count).getIsBroken()?"损坏":"正常";
				  responseData[count+1].put("isBroken",isBroken);
				  responseData[count+1].put("isUplink",isUplink);
				  responseData[count+1].put("isPeriod",isPeriod);
				  responseData[count+1].put("description",routes.get(count).getDescription());
				  responseData[count+1].put("createTime",routes.get(count).getCreate_date());
				  responseData[count+1].put("createUser",routes.get(count).getCreate_user());
				  responseData[count+1].put("alterTime",routes.get(count).getAlter_date());
				  responseData[count+1].put("alterUser",routes.get(count).getAlter_user());
				  responseData[count+1].put("isObstacle",isObstacle);
				  responseData[count+1].put("priorityId",routes.get(count).getPriotityId());
				  responseData[count+1].put("priorityName",routes.get(count).getPriotityName());
			  }
		  }
		    responseData[0].put("status", status);
			JSONArray responseJson=JSONArray.fromObject(responseData);
			PrintWriter out=response.getWriter();
			out.println(responseJson);
			out.flush();
			out.close();  
	  }
	  /**----------------------通过RTU id获取光路键值对-------------------------------*/
	@RequestMapping("getRouteKey")
	   public void getRoute(HttpServletRequest request,HttpServletResponse response) throws IOException{
		  Long id = Long.parseLong(request.getParameter("rtuId"));
		  List<Routes> routes=findService.findRoutesByRtuId(id);
		  Map<String,Object> responseData=new LinkedHashMap<String,Object>();
		  boolean status=false;
		  if(routes!=null){
			  status=true;
			  List< Map<String,Object>> routeKeys=new ArrayList< Map<String,Object>>();
			  for(int count=0;count<routes.size();count++){
				  Map<String,Object> routeKey=new LinkedHashMap<String,Object>();
				  routeKey.put("id",routes.get(count).getId());
				  routeKey.put("name",routes.get(count).getRoute_name());
				  routeKeys.add(routeKey);
			 }
			responseData.put("routeKeys", routeKeys);
		   }
		    responseData.put("status", status);
			JSONArray responseJson=JSONArray.fromObject(responseData);
			PrintWriter out=response.getWriter();
			out.println(responseJson);
			out.flush();
			out.close();  
	  }
	/**----------------------下发局站id，生成可选光路-------------------------------*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("setRoute")
	  public void setRoute(HttpServletRequest request,HttpServletResponse response) throws IOException{
			  JSONArray jsonArray = JSONArray.fromObject(request.getParameter("id"));//前端发送的为数组,将其装转换为JSONArray
			  List<Serializable>IDs=new ArrayList<Serializable>();
			  for(int mainCount=0;mainCount<jsonArray.size();mainCount++){  //遍历获取每个一维数组
					IDs.add(Long.parseLong(jsonArray.getString(mainCount)));//遍历获取每个一维数组的元素
			  }
			  List<Map<String,Object>> prepareRoutes=addService.addTopology(IDs);
			  int size=0;
			  if(prepareRoutes!=null){
				  size=prepareRoutes.size(); 
			  }
			  LinkedHashMap[] responseData=new LinkedHashMap[size+1];
			  responseData[0]=new LinkedHashMap();
			  boolean status=false;
			 if(responseData.length>1){//有可用光路
				  status=true;
				  for(int count=0;count<prepareRoutes.size();count++){
					 responseData[count+1]=new LinkedHashMap();
					  Set<String> keys = (Set<String>) prepareRoutes.get(count).keySet(); 
					  for (String key:keys) {
						  if(key.equals("rtuPortOrder")){
							  int portOrder=(int) prepareRoutes.get(count).get(key);
							  int moduleOrder=portOrder/8+1;//计算模块序号，每个模块8个端口
							  responseData[count+1].put(key,getPortOrder(portOrder,moduleOrder));//遍历获取各条预备光路信息 
						  }
						  else{
							  responseData[count+1].put(key, prepareRoutes.get(count).get(key));//遍历获取各条预备光路信息 
						  }
				         
				      }
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
	/**----------------------多条件查找光路-----------------------*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("routeSearch")
	  public void routeSearch(HttpServletRequest request,HttpServletResponse response ) throws IOException{
		 Map<String,Object> routePara=new LinkedHashMap<String,Object>();
		 String name=request.getParameter("name");
		 if(name!=null&&(!name.equals(""))){
			 routePara.put("route_name", name);
		 }
		 List<Routes> routes=findService.findRouteByMulti(routePara);
		 LinkedHashMap[] responseData=new LinkedHashMap[routes.size()+1];
		  responseData[0]=new LinkedHashMap();
		  boolean status=false;
		  if(responseData.length>1){
			  status=true;
			  for(int count=0;count<routes.size();count++){
				  String routeStatus=routes.get(count).getIs_online()? routeStatus="在线纤":"备纤";
				  String isObstacle=routes.get(count).getIsObstalce()?"是":"否";
				  String isUplink=routes.get(count).getIsUplink()?"上行":"下行";
				  responseData[count+1]=new LinkedHashMap();
				  responseData[count+1].put("id",routes.get(count).getId());
				  responseData[count+1].put("name",routes.get(count).getRoute_name());
				  responseData[count+1].put("stationAId",routes.get(count).getStation_a_id());
				  responseData[count+1].put("stationAName",routes.get(count).getStation_a_name());
				  responseData[count+1].put("rtuId",routes.get(count).getRtu_id());
				  responseData[count+1].put("rtuName",routes.get(count).getRtu_name());
				  int portOrder=routes.get(count).getRtu_port_order();
				  int moudleOrder=routes.get(count).getRtu_model_order();
				  responseData[count+1].put("rtuOrder",getPortOrder(portOrder,moudleOrder));
				  responseData[count+1].put("stationZId",routes.get(count).getStation_z_id());
				  responseData[count+1].put("stationZName",routes.get(count).getStation_z_name());
				  responseData[count+1].put("frameId",routes.get(count).getFrame_z_id());
				  responseData[count+1].put("frameOrder",routes.get(count).getFrame_z_order());
				  responseData[count+1].put("length",routes.get(count).getLength());
				  responseData[count+1].put("frameName",findService.findDistributingFrame(routes.get(count).getFrame_z_id()).getFrame_name());
				  responseData[count+1].put("status",routeStatus);
				  responseData[count+1].put("isUplink",isUplink);
				  responseData[count+1].put("isObstacle",isObstacle);
				  responseData[count+1].put("priorityId",routes.get(count).getPriotityId());
				  responseData[count+1].put("priorityName",routes.get(count).getPriotityName());
				  responseData[count+1].put("description",routes.get(count).getDescription());
				  responseData[count+1].put("createTime",routes.get(count).getCreate_date());
				  responseData[count+1].put("createUser",routes.get(count).getCreate_user());
				  responseData[count+1].put("alterTime",routes.get(count).getAlter_date());
				  responseData[count+1].put("alterUser",routes.get(count).getAlter_user());
				  
			  }
		  }
		  responseData[0].put("status", status);
		  JSONArray responseJson=JSONArray.fromObject(responseData);
		  PrintWriter out=response.getWriter();
		  out.println(responseJson);
		  out.flush();
		  out.close();  
	 } 
	/**
	 * *获取光路的光功率门限
	 * **/
	@RequestMapping("degradation/getPth")
	public void getPth(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
		long routeId=Long.parseLong(request.getParameter("routeId"));
		boolean status=false;
		Map<String,Object> responseData=new LinkedHashMap<String, Object>();
		Threshold threshold=findService.findThresholdByRouteId(routeId);
	    if(threshold!=null){
	        status=true;
	        String[]pTh=new String[4];
	        pTh[0]=threshold.getThre1();
	        pTh[1]=threshold.getThre2();
	        pTh[2]=threshold.getThre3();
	        pTh[3]=threshold.getThre4();
	        responseData.put("pTh", pTh);
	    }
	    responseData.put("status", status);
	    JSONArray responseD=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();
	}
	/**
	 * *多条件查找告警经验
	 * **/	
	@RequestMapping("searchAlarmBank")
	public void searchAlarmBank(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
		String type=request.getParameter("type");
		Map<String,Object> para=new LinkedHashMap<String,Object>();
		List<AlarmBank> alarmBank=null;
		String source=request.getParameter("source");
		if(!type.equals("all")){
			para.put("alarm_type", type);
		}
		if(!source.equals("all")){
			para.put("alarm_source", source);
		}
		if(para.isEmpty()){
			alarmBank=findService.findAllAlarmExperience();
		}
		else{
			alarmBank=findService.findAlarmByMuyi(para); 
		}
		boolean status=false;
		Map<String,Object> responseData=new LinkedHashMap<String, Object>();
		if(alarmBank!=null){
			if(alarmBank.size()>0){
				status=true;
				List<Map<String,Object>> banks=new ArrayList<Map<String,Object>>();
				for(AlarmBank aBank:alarmBank){
				      Map<String,Object>bank=new LinkedHashMap<String,Object>(); 
				      bank.put("id", aBank.getId());
				      bank.put("source", aBank.getAlarm_source());
				      bank.put("type", aBank.getAlarm_type());
				      bank.put("level", aBank.getAlarmReason());
				      bank.put("experience", aBank.getHandleExper());
				      bank.put("createTime", aBank.getCreateTime());
				      bank.put("createUser", aBank.getCreateUser());
				      bank.put("alterTime", aBank.getAlterTime());
				      bank.put("alterUser", aBank.getAlterUser());
				      banks.add(bank);
				 }
				 responseData.put("banks", banks);
			}
		}
		responseData.put("status", status);
		JSONArray responseD=JSONArray.fromObject(responseData);
		//System.out.println("responseData:"+responseD);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();
	}
    /**获取所有的告警经验**/	
	@RequestMapping("getAllWarnExperience")
	public void getAllWarnExperience(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
		List<AlarmBank> alarmBank=findService.findAllAlarmExperience();
		boolean status=false;
		Map<String,Object> responseData=new LinkedHashMap<String, Object>();
		if(alarmBank!=null){
			if(alarmBank.size()>0){
				status=true;
				List<Map<String,Object>> banks=new ArrayList<Map<String,Object>>();
		        for(AlarmBank aBank:alarmBank){
		        	Map<String,Object>bank=new LinkedHashMap<String,Object>(); 
		        	bank.put("id", aBank.getId());
		        	bank.put("source", aBank.getAlarm_source());
		        	bank.put("type", aBank.getAlarm_type());
		        	bank.put("level", aBank.getAlarmReason());
		        	bank.put("experience", aBank.getHandleExper());
		        	bank.put("createTime", aBank.getCreateTime());
		        	bank.put("createUser", aBank.getCreateUser());
		        	bank.put("alterTime", aBank.getAlterTime());
		        	bank.put("alterUser", aBank.getAlterUser());
		        	banks.add(bank);
		       }
		       responseData.put("banks", banks);
			}
	    }
	    responseData.put("status", status);
	    JSONArray responseD=JSONArray.fromObject(responseData);
	    //System.out.println("responseData:"+responseD);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();
   }
	/**增加告警经验**/
	@RequestMapping("alarm/addAlarmBank")
	public void addAlarmBank(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
		boolean status=false;
		Map<String,Object> responseData=new LinkedHashMap<String, Object>();
		AlarmBank alarmBank=new AlarmBank();
		alarmBank.setAlarm_source(request.getParameter("source"));
		alarmBank.setAlarmReason(request.getParameter("reason"));
		alarmBank.setAlarm_type(request.getParameter("type"));
		alarmBank.setHandleExper(request.getParameter("experience"));
		alarmBank.setCreateTime(NumConv.currentTime());
		Subject currentUser = SecurityUtils.getSubject();//获取当前用户
		String Account=currentUser.getPrincipal().toString();//当前用户的账号
		alarmBank.setCreateUser(Account);
		Serializable id=addService.addAlarmBank(alarmBank);
		if(id!=null){
			status=true;
		}
	    responseData.put("status", status);
	    JSONArray responseD=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();
   }
	/**删除告警经验**/
	@RequestMapping("alarm/delAlarmBank")
	public void delAlarmBank(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
		boolean status=true;
		Map<String,Object> responseData=new LinkedHashMap<String, Object>();
		long id=Long.parseLong(request.getParameter("warnBankId"));
		delService.deleteAlarmBank(id);
		if(findService.findAlarm(id)!=null){
			status=false;
		} 
	    responseData.put("status", status);
	    JSONArray responseD=JSONArray.fromObject(responseData);
	    response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();
   }
	/**修改告警经验**/
	@RequestMapping("alarm/modifyAlarmBank")
	public void modifyAlarmBank(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
		boolean status=false;
		Map<String,Object> responseData=new LinkedHashMap<String, Object>();
		AlarmBank alarmBank =findService.findAlarm(Long.parseLong(request.getParameter("id")));
		alarmBank.setAlarm_source(request.getParameter("source"));
		alarmBank.setAlarmReason(request.getParameter("reason"));
		alarmBank.setAlarm_type(request.getParameter("type"));
		alarmBank.setHandleExper(request.getParameter("experience"));
		alarmBank.setAlterTime(NumConv.currentTime());
		Subject currentUser = SecurityUtils.getSubject();//获取当前用户
	  	String Account=currentUser.getPrincipal().toString();//当前用户的账号
		alarmBank.setAlterUser(Account);
		status=alterService.alterAlarmBank(alarmBank);
		responseData.put("status", status);
        JSONArray responseD=JSONArray.fromObject(responseData);
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(responseD);			
		out.flush();
		out.close();
   }
}

