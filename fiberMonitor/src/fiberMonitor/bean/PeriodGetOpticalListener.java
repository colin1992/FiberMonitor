package fiberMonitor.bean;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.http.HttpException;
import org.dom4j.DocumentException;

import domain.Routes;
import domain.Rtus;
import service.AddService;
import service.AlterService;
import service.DeleteService;
import service.FindService;
/**功能说明：用于周期获取光功率值
 * 其时间间隔和启动时间延时相关信息在spring-mvc.xml文件中配置
 **/
public class PeriodGetOpticalListener extends TimerTask{
	@Resource(name="findService")
	private FindService findService;
    @Resource(name="addService") 
	private AddService addService;
    @Resource(name="alterService") 
   	private AlterService alterService;
    @Resource(name="deleteService") 
   	private DeleteService delService;
	public PeriodGetOpticalListener() {
		
	}
	/**---------执行方法--------*/
    public void run() {
		getAllRouteOptical();
	}
	/**采集光功率的线程**/
	class PeriodGetOp extends Thread{
		private String code;
		private String url;
		public  PeriodGetOp(String code,String url){
			this.code=code;
			this.url=url;
		}
		public void run(){
			String responseFile=NumConv.createVerifyCode(20)+this.getName()+"periodGetOp.xml";
		    String testDecode=NumConv.createVerifyCode(20)+this.getName()+"periodGetOpDecode.txt";
			try {
				HttpClientUtil.Post(url, code, responseFile,2000,20000);
				/*接收数据解析返回的需要回传的参数*/
				XmlDom XmlDom=new XmlDom();
			    XmlDom.AnalyseRespondse(responseFile,testDecode);
			} catch (SocketTimeoutException|ConnectTimeoutException |HttpException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException|DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void getAllRouteOptical(){
		//获取所有光路
		List<Routes> routes= findService.findAllRoutes();
		for(int index=0;index<routes.size();index++){
			Long CM=routes.get(index).getRtu_id();
			Rtus rtu=findService.findRtu(CM);
			int order=routes.get(index).getRtu_model_order();
			String moudleType=rtu.getInstallInfo().substring(order-1,order);
			  /**光路所在的模块类型：1为在线 2为备纤 3为保护-主  4为在线OPM
	                        备纤均可以测光功率，在线纤只有3和4可以  */
			if(routes.get(index).getIs_online()&&(!(moudleType.equals("4")||moudleType.equals("3")))){
			      continue;
		    }
			int SNo=routes.get(index).getRtu_port_order();
			Long CLP=findService.findRtu(CM).getStation().getId();
			Map<String,Object> paraMap=new LinkedHashMap<String,Object>();
			paraMap.put("CLP",CLP);
			paraMap.put("CM",CM);
			paraMap.put("SNo",SNo);
			String xmlCode=XmlCodeCreater.getOpticalValue(paraMap);
			String rtuUrl="http://"+rtu.getRtu_url()+":5000/cgi-bin/BoaCom.cgi";
			PeriodGetOp thread=new PeriodGetOp(xmlCode,rtuUrl);
			thread.start();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
    }
}


 


