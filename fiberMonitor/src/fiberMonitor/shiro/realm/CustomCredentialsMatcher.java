package fiberMonitor.shiro.realm;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
 
 /**
  * @author 
  */
 public class CustomCredentialsMatcher extends HashedCredentialsMatcher {
     
     private Cache<String,AtomicInteger> passwordRetryCache;
 
     public CustomCredentialsMatcher(CacheManager cacheManager) {
         passwordRetryCache = cacheManager.getCache("passwordRetryCache");  
    }
     
     @Override
     public boolean doCredentialsMatch(AuthenticationToken token,
             AuthenticationInfo info) {
         String loginName = (String) token.getPrincipal();
         AtomicInteger retryCount = passwordRetryCache.get(loginName);
         if(retryCount==null){
             retryCount = new AtomicInteger();
             passwordRetryCache.put(loginName, retryCount);
         }
         //密码输入次数超过3次时锁定十分钟
         if(retryCount.incrementAndGet()>3){
             throw new ExcessiveAttemptsException();
         }
         boolean matchs = super.doCredentialsMatch(token, info);
         if(matchs){
             passwordRetryCache.remove(loginName);
         }
         return matchs;
     }
 
     public Cache<String, AtomicInteger> getPasswordRetryCache() {
         return passwordRetryCache;
     }
 
     public void setPasswordRetryCache(Cache<String, AtomicInteger> passwordRetryCache) {
         this.passwordRetryCache = passwordRetryCache;
     }
     
 }