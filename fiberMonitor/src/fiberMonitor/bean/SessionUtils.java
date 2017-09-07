package fiberMonitor.bean;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

/**
 * 功能说明：用来存储业务定义的sessionId和连接的对应关系
 *          利用业务逻辑中组装的sessionId获取有效连接后进行后续操作
 * @author 心之&所系
 */
public class SessionUtils {

    private static  Map<String, Session> clients=new ConcurrentHashMap<>();
    /**
     * 获取当前连接的全部session
     * @return Map<String, Session>
     * **/
    public static Map<String, Session> getClients(){
    	return SessionUtils.clients;
    }
    /**
     * 记录存储连接session
     * @param session 连接的session
     * **/
    public static void put(Session session){
        clients.put(session.getId(), session);
    }
    /**
     * 通过用户Id获取session
     * @param index 连接编号
     * **/
    public static Session get(String index){
        return clients.get(index);
    }
    /**
     * 通过编号移除session
     * @param index 连接编号
     * **/
    public static void remove(String index){
        clients.remove(index);
    }
    /**
     * 获取当前连接数
     * @return Integer
     * **/
    public static  int getSize(){
       return clients.size();
    }
    /**
     * 判断是否有指定连接
     * @param index
     * @return boolean
     */
    public static boolean hasConnection(String index) {
        return clients.containsKey(index);
    }
    /**
     * 广播数据到客户端
     * 异步的方式
      * @param message 广播的内容
     */
    public static void broadcast(String message) {
    	Set<String> get = (Set<String>) clients.keySet(); 
        for (String key:get) {
        	clients.get(key).getAsyncRemote().sendText(message);
        }
    }
    /**
     * 发送数据到指定客户端
     * 异步的方式
     * @param index 用户的编号
     * @param message 发送的内容
     */
    public static void sendMessage(String index,String message) {
        if (SessionUtils.hasConnection(index)) {
        	SessionUtils.get(index).getAsyncRemote().sendText(message);
        } else {
            throw new NullPointerException(SessionUtils.get(index) + " Connection does not exist");
        }
    }
}
