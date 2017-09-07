package fiberMonitor.shiro.realm;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import domain.SerialCode;
import fiberMonitor.bean.NumConv;
import service.FindService;
/**自定义shiro拦截器
 * 判断系统是否已经过鉴权
 * **/
public  class VlidateTokenAuthentication extends AuthorizationFilter  {
    //认证的页面
	public static final String valitationUrl = "/valid.html";
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object object) throws Exception {
		 System.out.println(((HttpServletRequest) request).getRequestURI()+"进行鉴权验证，为认证页面：" + pathsMatch(valitationUrl, request));
		 boolean status=false;
		 WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();    
	     FindService findServiceImpl=(FindService)wac.getBean("findService");
	     SerialCode serialCode=findServiceImpl.findSNCodeById((long)1);
	     //如果已经是认证页面，不再重定向
	     if(pathsMatch(valitationUrl, request)){
	    	 status=true;
	     }
	     /**不是认证页面，判断是否已经完成认证
	      * 判断是否完成认证的方式：读取用户输入的认证码(Validation字段，未经过MD5加密)，将其进行MD5加密后与密文进行对比
	      * **/
	     else if(serialCode!=null){
	    	 if(serialCode.getValidation()!=null&&!serialCode.getValidation().isEmpty()){
	    		 if(serialCode.getSerialCode().equals(NumConv.passwordMD5(serialCode.getValidation()))){
		    		 status=true;
		    	 }
	    	 }
	    	
	     }
	     //不是认证页面且未完成认证，重定向到认证页面
	     if(!status){
	    	 try {
	 			WebUtils.issueRedirect(request, response, valitationUrl);
	 		} catch (IOException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	     }
		// TODO Auto-generated method stub
		return true;
	}
	
	
}
