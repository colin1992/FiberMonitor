package fiberMonitor.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpException;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fiberMonitor.bean.XmlDom;
import service.AddService;
import service.AlterService;
import service.FindService;
import fiberMonitor.bean.NumConv;
import fiberMonitor.bean.XmlCodeCreater;


@Controller
public class CenterServerController {
	@Resource(name="findService")
	private FindService findService;
    @Resource(name="addService") 
	private AddService addService;
    @Resource(name="alterService") 
   	private AlterService alterService;
	/**说明：该方法采用引入参数request与Request的而不是直接用@Resource Request与Response的方式
	    * 主要是为了防止Circular view path错误
	    * 如果用后者，需要采用MVC模式，必须返回一个视图*/
	@RequestMapping(value="TomCat")
	public void RtuVisit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, DocumentException, HttpException{
		//System.out.println("visit type:");
		/**请求类型为Get*/
		if( request.getParameterNames().hasMoreElements()){ 
			//System.out.println("GET");
			handleGetRequest( request,response);
		}
		/**请求中含有文件，为多媒体请求*/
		else if( ServletFileUpload.isMultipartContent(request)){
			//System.out.println("Multi");
			handlemMultiRequest(request,response);
		}
            
	   /**POST请求*/
	  else {
		  //System.out.println("POST");
		  handlePostRequest(request,response);
       }
	
	}
    /**处理POST请求*/
	public  void handlePostRequest(HttpServletRequest request,HttpServletResponse response) throws IOException, DocumentException, HttpException{
		String fileName=NumConv.createVerifyCode(20);
		String requestFlie="newRequest"+fileName+".xml";
		String testDecode="testDecode"+fileName+".xml";
		FileWriter requestData=new FileWriter(requestFlie);
	    BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));  
		String line ="";  
		//System.out.print("请求参数为：");
		while((line = br.readLine())!=null){  
			  //System.out.println(line);
			  requestData.write(line);
			        
		}
		requestData.close();//存储完成，关闭文件
		String StatusCode="0";//将回复码设置为0
       /**向客户端发送反馈信息*/
		response.setContentType("text/xml;charset=utf-8");
		String responseXML=XmlCodeCreater.responseCode(StatusCode);//生成回复内容
		PrintWriter out=response.getWriter();
		out.println(responseXML);
		out.flush();
		out.close();
         /**解析推送的数据文件*/
		/**-----------传入对数据库的操作-----------*/
		XmlDom XmlDom=new XmlDom();
		XmlDom.AnalyseRespondse(requestFlie,testDecode);//解析收到的xml文件,分析存储数据文件
	}
	/**请求中含有文件的情况*/
	@SuppressWarnings("unchecked")
	public  void handlemMultiRequest(HttpServletRequest request,HttpServletResponse response) throws IOException, DocumentException, HttpException{
		DiskFileItemFactory factory = new DiskFileItemFactory();//磁盘对象
		ServletFileUpload upload = new ServletFileUpload(factory);//声明解析request的对象
		String fileName=NumConv.createVerifyCode(20);
		String requestFlie="newRequest"+fileName+".xml";
		String testDecode="testDecode"+fileName+".xml";
		upload.setHeaderEncoding("UTF-8"); //处理文件名中文
		String StatusCode="1";
		try {
			  List<FileItem> list = upload.parseRequest(request);// 解析
			  for (FileItem fileContent : list) {
				   if (fileContent.isFormField()) {
						String ds = fileContent.getString("UTF-8");//处理中文
						System.err.println("包含的内容为:" + ds);
						StatusCode="0";
					}else {
			               //直接使用commons.io.FileUtils存储文件内容
					        FileUtils.copyInputStreamToFile( 
							fileContent.getInputStream(),
							new File(requestFlie));
					        StatusCode="0";
					        //System.out.print("content:"+fileContent.getString("UTF-8"));
	                  }
             }
		} catch (Exception err) {
		    System.err.println("错误为:" );
		    err.printStackTrace();
		    StatusCode="1";
	   }
	   /**向客户端发送反馈信息*/
		response.setContentType("text/xml;charset=utf-8");
		String responseXML=XmlCodeCreater.responseCode(StatusCode);//生成回复内容
		PrintWriter out=response.getWriter();
		out.println(responseXML);
		out.flush();
		out.close();
		/**-----------传入对数据库的操作-----------*/
		XmlDom XmlDom=new XmlDom();
		XmlDom.AnalyseRespondse(requestFlie,testDecode);//解析收到的xml文件,分析存储数据文件
   }
		/**处理Get请求*/
	public void handleGetRequest(HttpServletRequest request,HttpServletResponse response) throws IOException{
		//Enumeration<?> queryString=request.getParameterNames();  
		//System.out.print("请求参数为：");
		/*while( queryString.hasMoreElements()){  
			String paraName= queryString.nextElement().toString();  
			//System.out.println(paraName+": "+request.getParameter(paraName));  
	   }*/
		/**向客户端发送反馈信息*/
		response.setContentType("text/xml;charset=utf-8");
		String StatusCode="0";
		String responseXML=XmlCodeCreater.responseCode(StatusCode);//生成回复内容
		PrintWriter out=response.getWriter();
		out.println(responseXML);
		out.flush();
		out.close();
	}
}
