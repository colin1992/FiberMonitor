package fiberMonitor.shiro.realm;
import java.io.Serializable;
import java.util.Collection;  
  
import org.apache.commons.lang.StringUtils;  
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.Session;  
import org.apache.shiro.session.mgt.eis.SessionDAO;  
import org.apache.shiro.subject.PrincipalCollection;  
import org.apache.shiro.subject.Subject;  
import org.apache.shiro.subject.support.DefaultSubjectContext;  
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;
import fiberMonitor.bean.NumConv;  

public class ShiroSecurityHelper {  
    private final static Logger log = LoggerFactory.getLogger(ShiroSecurityHelper.class); 
    
    private static SessionDAO sessionDAO;  
    private static AbstractCacheManager cacheManager;;  
    public static void  setSessionDAO(SessionDAO sessionDAO){
    	ShiroSecurityHelper.sessionDAO=sessionDAO;
    }
    public static void  setCacheManager(AbstractCacheManager cacheManager){
    	ShiroSecurityHelper.cacheManager=cacheManager;
    }
    
    /** 
     * 获得当前用户名 
     *  
     * @return 
     */  
    public static String getCurrentUsername() {  
        Subject subject = getSubject();  
        PrincipalCollection collection = subject.getPrincipals();  
        if (null != collection && !collection.isEmpty()) {  
            return (String) collection.iterator().next();  
        }  
        return null;  
    }  
  /** 
     *  
     * @return 
     */  
    public static Session getSession() {  
        return SecurityUtils.getSubject().getSession();  
    }  
  
    /** 
     *  
     * @return 
     */  
    public static String getSessionId() {  
        Session session = getSession();  
        if (null == session) {  
            return null;  
        }  
        return getSession().getId().toString();  
    }  
      
    /** 
     * @param username 
     * @return 
     */  
    public static Session getSessionByUsername(String username){  
    	
        Collection<Session> sessions = sessionDAO.getActiveSessions(); 
        System.out.println("seesion:"+sessions);
        for(Session session : sessions){  
        	System.out.println("this session:"+session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY));
            if(null !=session && username.equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))){  
                return session;  
            }  
        }  
        return null;  
    }  
      
    /**踢除用户 
     * @param username 
     */  
    public static void kickOutUser(String username){  
        Session session = getSessionByUsername(username);  
        if(null != session && !StringUtils.equals(String.valueOf(session.getId()), ShiroSecurityHelper.getSessionId())){  
            clearAuthenticationInfo(session.getId());  
            log.info("############## success kick out user 【{}】 ------ {} #################", username,NumConv.currentTime());  
        }  
    }  
  
      
    /**清除session(认证信息) 
     * @param JSESSIONID 
     */  
    public static void clearAuthenticationInfo(Serializable JSESSIONID){  
        if(log.isDebugEnabled()){  
            log.debug("clear the session " + JSESSIONID);  
        }  
        Cache<Object, Object> cache = cacheManager.getCache("shiro-activeSessionCache");  
        cache.remove(JSESSIONID);  
    }  
  
    /** 
     * 判断当前用户是否已通过认证 
     * @return 
     */  
    public static boolean hasAuthenticated() {  
        return getSubject().isAuthenticated();  
    }  
  
    private static Subject getSubject() {  
        return SecurityUtils.getSubject();  
    }  
  
  
}  