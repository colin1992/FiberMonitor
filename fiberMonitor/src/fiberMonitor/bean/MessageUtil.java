package fiberMonitor.bean;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.sun.mail.util.MailSSLSocketFactory;

import domain.Log;
import domain.SystemInfo;
import service.AddService;
import service.FindService;

/**
 * 发送消息工具类接口
 * 提供发送短信和邮件的方法
 * **/

public class MessageUtil{
	private static AddService addServiceImpl;
	private static FindService findServiceImpl;
	public void setAddService(AddService addServiceImpl)
	{
		MessageUtil.addServiceImpl  = addServiceImpl;
	}
	
	public void setFindService(FindService findServiceImpl)
	{
		MessageUtil.findServiceImpl  = findServiceImpl;
	}
	/**向前端推送告警信息
	 * @param  title (String) 消息标题
	 * @param  context (String)  消息内容
	 * @return boolean 短信发送状态 
	 * **/
	public static void broadcastMessage(String title,String context){
		String message=title+"context:"+context;
		SessionUtils.broadcast(message);
	}
	/**
	 * 发送短信
	 * @param  phone (String) 收件人手机号
	 * @param  context (String)  短信内容
	 * @return boolean 短信发送状态 
	 * **/
	public static boolean sendMessage(List<String> phone,String context){
		    WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();    
	        String GSMPort=null;
	        SerialPortUtil serial=new SerialPortUtil(9600);
	 	    GSMPort=serial.GetGsmPort();
	 	    if(GSMPort!=null){   
		    	addServiceImpl=(AddService)wac.getBean("addService");
	   	      	MessageHandler smsHandler = new MessageHandler(GSMPort,9600);
	        	
			    boolean status=false;
			    for(String ph:phone){
			    	status=smsHandler.sendSMS(ph, context);
			    	if(!status){
			    		break;
			    	}
			    	Log log = new Log();
				    log.setDate(NumConv.currentTime());
				    String Account="系统发送";
				    log.setUser(Account);
				    log.setResourceName("短信");
				    log.setResourceType("消息");
				    log.setOperateDetail("发送给"+ph);
				    addServiceImpl.addLog(log);
			    }
			    //关闭服务器
			    smsHandler.destroy();
	        	return status;
	        }
	        else{
	        	return false;
	        }
	 }
	/**验证服务器有效性**/
	 public static boolean checkServer(Map<String,Object> emailPara){
		  String address=(String) emailPara.get("address");
		  int port =(int)emailPara.get("port");
		  String account=(String) emailPara.get("account");
		  String pword=(String) emailPara.get("pword");
		  // 配置发送邮件的环境属性
	      final Properties props = new Properties();
	      // 表示SMTP发送邮件，需要进行身份验证
	      props.put("mail.smtp.auth", "true");  //SMTP服务器是否需要用户认证，默认为false
	      props.put("mail.smtp.host",address); //SMTP服务器地址
	      props.put("mail.smtp.port",port); //SMTP服务器端口
	        // 发件人的账号(一定要开通pop3/smtp协议)
	      props.put("mail.smtp.user", account);
	        // 访问SMTP服务时需要提供的密码(必须为授权码,qq授权码可以有多个，这里任意用一个就可以,新浪邮箱在这里可以直接输入密码就行，无需授权码)
	      props.put("mail.smtp.password",pword);// pword
         //开启ssl加密(qq邮箱需要ssl加密)
	      MailSSLSocketFactory sf;
	      try {
				sf = new MailSSLSocketFactory();
				sf.setTrustAllHosts(true); 
//				props.put("mail.smtp.ssl.enable","true"); //是否开启ssl验证
				props.put("mail.smtp.ssl.socketFactory",sf); 
			} catch (GeneralSecurityException e1) {
			// TODO Auto-generated catch block
			  e1.printStackTrace();
			  return false;
			} 
	     // 构建授权信息，用于进行SMTP进行身份验证
	      Authenticator authenticator = new Authenticator() {
	          @Override
	          protected PasswordAuthentication getPasswordAuthentication() {
	              // 用户名、密码
	              String userName = props.getProperty("mail.smtp.user");
	              String password = props.getProperty("mail.smtp.password");
	              return new PasswordAuthentication(userName, password);
	          }
	      };
	      // 使用环境属性和授权信息，创建邮件会话
	      Session mailSession = Session.getInstance(props, authenticator);
	      // 创建邮件消息
	      MimeMessage message = new MimeMessage(mailSession);
	      // 设置发件人
	      InternetAddress form;
		try {
			form = new InternetAddress(props.getProperty("mail.smtp.user"));
			message.setFrom(form);
			 // 设置收件人
		     InternetAddress[] sendTo = new InternetAddress[1];
		     sendTo[0] = new InternetAddress(account);//给自己发送邮件测试
		     message.setRecipients(javax.mail.internet.MimeMessage.RecipientType.TO,  sendTo);
		   // 设置邮件标题
		     message.setSubject("服务器测试");
		  // 设置邮件的内容体       
		     message.setContent("Hello word","text/html;charset=UTF-8");
			  // 发送邮件
		     Transport.send(message);
		     // 设置抄送(抄送只能指定一个人)
		    return true;
		  } catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		 }
		
	  }  
	/**
	 * 邮件发送
	 * @param recAddress 收件人地址
	 * @param mailHead 邮件主题
	 * @param context 邮件内容
	 * @return boolean 发送状态
	 * **/    
	  public static boolean sendEmail(List<String> recAddress,String mailHead,String context){
		  WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();    
	      addServiceImpl=(AddService)wac.getBean("addService");
	      findServiceImpl=(FindService)wac.getBean("findService");
		  SystemInfo systemInfo=findServiceImpl.findSystemInfoById((long)1);
		  if(systemInfo.getEmailPort()!=null){
			  // 配置发送邮件的环境属性
		      final Properties props = new Properties();
		      // 表示SMTP发送邮件，需要进行身份验证
		      props.put("mail.smtp.auth", "true");  //SMTP服务器是否需要用户认证，默认为false
		      props.put("mail.smtp.host", systemInfo.getEmailAddr()); //SMTP服务器地址
		      props.put("mail.smtp.port",systemInfo.getEmailPort()); //SMTP服务器端口
		        // 发件人的账号(一定要开通pop3/smtp协议)
		     // //System.out.println("======port====="+props.getProperty(key));
		      props.put("mail.smtp.user", systemInfo.getEmailAccount());
		        // 访问SMTP服务时需要提供的密码(必须为授权码,qq授权码可以有多个，这里任意用一个就可以,新浪邮箱在这里可以直接输入密码就行，无需授权码)
		      props.put("mail.smtp.password",systemInfo.getEmailPword());// 
	          //开启ssl加密(qq邮箱需要ssl加密)
		      MailSSLSocketFactory sf;
		      try {
					sf = new MailSSLSocketFactory();
					sf.setTrustAllHosts(true); 
//					props.put("mail.smtp.ssl.enable","true"); 
					props.put("mail.smtp.ssl.socketFactory",sf); 
				} catch (GeneralSecurityException e1) {
				// TODO Auto-generated catch block
				  e1.printStackTrace();
				  return false;
				} 
		     // 构建授权信息，用于进行SMTP进行身份验证
		      Authenticator authenticator = new Authenticator() {
		          @Override
		          protected PasswordAuthentication getPasswordAuthentication() {
		              // 用户名、密码
		              String userName = props.getProperty("mail.smtp.user");
		              String password = props.getProperty("mail.smtp.password");
		              return new PasswordAuthentication(userName, password);
		          }
		      };
		      // 使用环境属性和授权信息，创建邮件会话
		      Session mailSession = Session.getInstance(props, authenticator);
		      // 创建邮件消息
		      MimeMessage message = new MimeMessage(mailSession);
		      // 设置发件人
		      InternetAddress form;
			try {
				form = new InternetAddress(props.getProperty("mail.smtp.user"));
				message.setFrom(form);
				 // 设置收件人
			     InternetAddress[] sendTo = new InternetAddress[recAddress.size()];
			     for(int i=0;i<recAddress.size();i++){
			    	 sendTo[i] = new InternetAddress(recAddress.get(i));
			     }
			     message.setRecipients(javax.mail.internet.MimeMessage.RecipientType.TO,  sendTo);
			   // 设置邮件标题
			     message.setSubject(mailHead);
			  // 设置邮件的内容体       
			     message.setContent(context,"text/html;charset=UTF-8");
				  // 发送邮件
			     Transport.send(message);
			     // 设置抄送(抄送只能指定一个人)
			   //  new InternetAddress("931385512@qq.com");
			     Log log = new Log();
			     log.setDate(NumConv.currentTime());
			     String Account="系统发送";
			     log.setUser(Account);
			     log.setResourceName("邮件");
			     log.setResourceType("消息");
			     log.setOperateDetail("发送给"+recAddress);
			     addServiceImpl.addLog(log);
			     return true;
			  } catch (MessagingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return false;
			 }
		  }else{
			  return false;
		  }
		 
		
	  }  
	  public static void main(String args[]){
		  //sendEmail("sdustguanjie@163.com","测试","Hello world");
		  
	  }
}
