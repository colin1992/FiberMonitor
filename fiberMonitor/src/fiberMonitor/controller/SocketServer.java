package fiberMonitor.controller;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fiberMonitor.bean.SessionUtils;
/**
 * 功能说明：用于实现webSocket
 * @author 心之&所系
 */
@ServerEndpoint("/webSocket")
	public class SocketServer {
	 private static Log log = LogFactory.getLog(SocketServer.class);
	 /**
	  * 打开连接时触发
	  * @param session
	  */
	  @OnOpen
	  public void onOpen(Session session) {
		SessionUtils.put(session);//记录当前连接的session
	    log.info("new webSocket");
	   
	  }
	  
	 /**
	  * 收到客户端消息时触发
	  * @param message
	  * @return
	 * @throws IOException 
	    */
	  @OnMessage
	  public void onMessage(String message, Session session) throws IOException
	   {
	     //System.out.println("接收到来自用户"+session.getId()+"的消息:"+message);
		 String sentMessage="用户"+session.getId()+"说:"+message;
		 SessionUtils.broadcast(sentMessage);
	   }
	  
	  /**
	     * 异常时触发
	     * @param relationId
	     * @param userCode
	     * @param session
	     */
	    @OnError
	    public void onError(Throwable throwable,Session session) {
	        log.info("Websocket Connection Exception: ");
	        log.info(throwable.getMessage(), throwable);
	        SessionUtils.remove(session.getId());
	    }
	    /**
	     * 关闭时触发，移除session中的记录
	     * @param session
	     */
	  @OnClose
	  public void onClose(Session session) {
		  log.info("Websocket Connection close: ");
	      log.info("user"+session.getId());
	      SessionUtils.remove(session.getId());
	   }
	}
	
	
