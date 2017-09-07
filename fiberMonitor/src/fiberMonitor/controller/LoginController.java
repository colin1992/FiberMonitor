package fiberMonitor.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fiberMonitor.bean.EncryptUtils;
import net.sf.json.JSONArray;



@Controller
public class LoginController {
	@RequestMapping(value = "dologin")  
    public  void login(HttpServletRequest request,HttpServletResponse response) throws IOException  { 
    	String msg = "";  
        String userName = request.getParameter("userName"); 
        String password=request.getParameter("password");
		try {
			password = EncryptUtils.aesDecrypt(password);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);  
        token.setRememberMe(true);
        boolean status=false;
		Map<String,Object> responseData=new LinkedHashMap<String, Object>();
        Subject subject = SecurityUtils.getSubject();  
        try {  
            subject.login(token);  
            if (subject.isAuthenticated()) {  
            	status=true; 
            } 
        } catch (IncorrectCredentialsException e) {  
            msg = "账号或密码错误。";  
        } catch (ExcessiveAttemptsException e) {  
            msg = "由于登录失败次数过多，当前账号被锁定十分钟，请稍后重试。";   
        } catch (LockedAccountException e) {  
            msg = "帐号已被锁定。";   
        } catch (DisabledAccountException e) {  
            msg = "当前帐号已被禁用，请联系管理员激活账号。";   
        } catch (ExpiredCredentialsException e) {  
            msg = "当前帐号已过期。";   
        } catch (UnknownAccountException e) {  
            msg = "用户名不存在。";   
        } catch (UnauthorizedException e) {  
            msg = "您没有得到相应的授权！";  
        } 
        /**回传数据**/
        responseData.put("status", status);
        responseData.put("msg", msg);
		JSONArray responseJson=JSONArray.fromObject(responseData);//生成json格式
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter out=response.getWriter();
		out.println(responseJson);
		out.flush();
		out.close(); 
    }      
}