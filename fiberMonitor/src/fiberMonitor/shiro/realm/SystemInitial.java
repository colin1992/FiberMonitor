package fiberMonitor.shiro.realm;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import fiberMonitor.bean.AESHelper;

public class SystemInitial extends HttpServlet{
    /** 
	 */
	private static final long serialVersionUID = 1L;
	 public void init(ServletConfig config) {  
	        try {  
	            super.init();  
	        } catch (ServletException e) {  
	            e.printStackTrace();  
	        } 
	        String path= this.getClass().getResource("/properties/jdbc.properties").getPath();
	        System.out.println("=================文件路径："+path.substring(1));
	        boolean status= AESHelper.ConfigurationEncryption(path.substring(0));
	        String info=status?"加密数据库配置文件完成":"加密配置文件失败";
	        System.out.println("================="+info);
	 }
}
