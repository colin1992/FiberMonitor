package fiberMonitor.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.http.HttpException;

/**类名：HttpClientUtil
 * 创建时间：2016/6/27
 * 说明：该类作为HttpClient用于向RTU下发指令和发送请求
 * 发送请求的内容为XML字符串，需要requestCodeCreater 的支持，利用它生成对应指令相关的XML字符串
 * 方法：
 *      Get(String url,String sendFile,String responseFile) 
 *      利用HTTPClient实现get请求，发送文件
 *      
 *      Post(String url,String xml,String responseFile)
 *      利用HTTPClient实现Post携带字符串的请求方式（该项目中主要采用该方法）
 *      
 *      queryPost(String urlStr,String xml,String responseFile)
 *      用URLconnection的方式发送字符串
 *      
 *      sendFile(String url,String sendFile,String responseFile)
 *      读取本地文件的内容，利用POST方法发送
 *      
 *      upLoad(String url,String sendFile,String responseFile)
 *      直接发送本地文件，包含文件头等相关信息，考虑用作后期的远程升级RTU固件，待完善
 * */

public class HttpClientUtil {

	/**Get方法：利用HTTPClient实现get请求，发送文件
	 * 
	 * 入口参数：String url：目标地址
	 *          String sendFile：欲发送的文件
	 *          String responseFile：返回内容的存放文件
	 * 
	 * */
	public static void Get(String url,String sendFile,String responseFile) throws HttpException, IOException{
		     HttpClient httpClient = new HttpClient(); //构造HttpClient的实例
			 GetMethod getMethod = new GetMethod(url);
	         int statusCode = httpClient.executeMethod(getMethod);  //执行getMethod
			 ////System.out.println("状态："+getMethod.getStatusLine());
			 if (statusCode != HttpStatus.SC_OK) {
		          System.err.println("Method failed: "+ getMethod.getStatusLine());
		      }
		     byte[] responseBody = getMethod.getResponseBody();//读取内容
			 FileOutputStream file=new FileOutputStream(responseFile);//使用IO流将内容保存到文件中
			 file.write(responseBody);
			 file.flush();
			 file.close();
		     System.out.println("回复数据："+new String(responseBody));//处理内容
			 getMethod.releaseConnection();
		}
	/**Post方法：利用HTTPClient实现Post携带字符串的请求方式
	 * 采用字符串entity
	 * @param url String 目标地址
	 * @param xml String 欲发送的xml字符串内容
	 * @param responseFile String返回内容的存放文件
	 * @param ConnTimeOut int 连接超时时间
	 * @param WaitTimeOut int 读取超时时间 
	 * 
	 * */
	public static void Post(String url,String xml,String responseFile,int ConnTimeOut, int WaitTimeOut) throws HttpException,SocketTimeoutException,ConnectTimeoutException,IOException{
	      PostMethod post = new PostMethod(url);
          RequestEntity entity = new StringRequestEntity(xml, "text/xml", "utf-8");  
	      post.setRequestEntity(entity);
 	        // Get HTTP client
 	      HttpClient HttpClient = new HttpClient();
 	                /**设置连接超时参数 5S
 	                 * 连接超时指通信建立连接的时间超过了设定的时间*/
 	      HttpClient.getHttpConnectionManager().getParams().setConnectionTimeout(ConnTimeOut);
 	             /**设置等待超时参数
 	              * 建立连接后等待回传的时间超过了设定的时间*/
 	      HttpClient.getHttpConnectionManager().getParams().setSoTimeout(WaitTimeOut);
 	      HttpClient.executeMethod(post);
 	      byte[] responseBody = post.getResponseBody();//读取内容
		  FileOutputStream file=new FileOutputStream(responseFile);//使用IO流将内容保存到文件中
		  file.write(responseBody);
		  file.flush();
		  file.close();
	      post.releaseConnection();
		  System.out.println("========文件存储完毕========文件名为："+responseFile);//处理内容
	 }
	/**用URLconnection的方式发送
     * 入口参数：String url 目标地址
     *          String sendFile 欲发送的文件 
     * */
	 public static void queryPost(String urlStr,String xml,String responseFile) {  
	        try {  
	            URL url = new URL(urlStr);  
	            URLConnection con = url.openConnection();  
	            con.setDoOutput(true);  
	            con.setRequestProperty("Pragma", "no-cache");  
	            con.setRequestProperty("Cache-Control", "no-cache");  
	            con.setRequestProperty("Content-Type", "text/xml");  
                OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());      
	            out.write(new String(xml.getBytes("UTF-8")));  
	            out.flush();  
	            out.close();  
	            try(InputStream responseBody = con 
	                    .getInputStream())//读取内容
			      {
					  FileOutputStream respon=new FileOutputStream(responseFile);//使用IO流将内容保存到文件中
				      byte [] bbuf=new byte[1024];
				      int hasRead=0;
				      while((hasRead=responseBody.read(bbuf))>0){
				    	  ////System.out.println(hasRead);  
				    	  respon.write(bbuf,0,hasRead);
				      }
				      respon.flush();
				      respon.close();
			      }
	            
	           } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }  
	  
	   
	    /**读取文件内容，以文件实体的方式请求
	     * 入口参数：String url 目标地址
	     *          String sendFile 欲发送的文件 
	     *          String responseFile 存储接收的内容
	     * */
	    public static void sendFile(String url,String sendFile,String responseFile) throws IOException {  
	    	   String strURL = url;
	 	        // Get file to be posted
	 	        String strXMLFilename = sendFile;
	  	        File input = new File(strXMLFilename);
		        // Prepare HTTP post
	 	        PostMethod post = new PostMethod(strURL);
		        // Request content will be retrieved directly
	 	        // from the input stream
                FileRequestEntity  entity = new FileRequestEntity(input, "text/xml; charset=utf-8");
                post.setRequestEntity(entity);
	  	        // Get HTTP client
	  	        HttpClient HttpClient = new HttpClient();
	  	        HttpClient.executeMethod(post);
	  	        /*打印返回内容和响应状态*/
	             //System.out.println("Response status code: " + result);
	               ////System.out.print("Response body: ");
	               ////System.out.println(post.getResponseBodyAsString());
	             
	             /*存储接收到的文件*/
		      try(InputStream responseBody = post.getResponseBodyAsStream())//读取内容
		      {
				  FileOutputStream file=new FileOutputStream(responseFile);//使用IO流将内容保存到文件中
			      byte [] bbuf=new byte[1024];
			      int hasRead=0;
			      while((hasRead=responseBody.read(bbuf))>0){
			    	  file.write(bbuf,0,hasRead);
			      }
			      file.flush();
			      file.close();
		      }
		     
		      /*释放连接*/
	  	       post.releaseConnection();
              
        }  
	    
	    /*以文件方式传送*/
	    public static void upload(String URL_STR,String sendFile,String responseFile){
	             File file = new File(sendFile);
	    	     PostMethod filePost = new PostMethod(URL_STR);
	    	     HttpClient client = new HttpClient();
	    	     try {
	              // 通过以下方法可以模拟页面参数提交
	             Part[] parts = { new FilePart(file.getName(), file) };
	    	     filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
	             client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
	             int status = client.executeMethod(filePost);
	    	          if (status == HttpStatus.SC_OK) {
	    	            	  /*打印返回内容和响应状态*/
	    	   	            try(InputStream responseBody = filePost.getResponseBodyAsStream())//读取内容
	    			         {
	    					  FileOutputStream respon=new FileOutputStream(responseFile);//使用IO流将内容保存到文件中
	    				      byte [] bbuf=new byte[1024];
	    				      int hasRead=0;
	    				      while((hasRead=responseBody.read(bbuf))>0){
	    				    	  respon.write(bbuf,0,hasRead);
	    				       }
	    				      respon.flush();
	    				      respon.close();
	    			      }
	    	           } else {
	    	                ////System.out.println("上传失败");
	    	            }
	    	        } catch (Exception ex) {
	    	          ex.printStackTrace();
	    	       } finally {
	    	           filePost.releaseConnection();
	    	        }
	    	     }
}
