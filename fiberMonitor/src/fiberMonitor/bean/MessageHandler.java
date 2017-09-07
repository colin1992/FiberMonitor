package fiberMonitor.bean;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.LinkedList;
import java.util.List;
import javax.mail.MessagingException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.smslib.AGateway;
import org.smslib.AGateway.Protocols;
import org.smslib.GatewayException;
import org.smslib.InboundMessage;
import org.smslib.Message.MessageEncodings;
import org.smslib.OutboundMessage;
import org.smslib.SMSLibException;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;
/**
 * 短信处理类，用于发送短信
 * @author 心之&所系
 * **/
public class MessageHandler {  
    private static final Logger logger = Logger.getLogger(MessageHandler.class);  
  
    private Service smsService;  
    private SerialModemGateway gateway;
    /**
     * @param comPort 模块所连接的端口名称
     * @param boudRate 波特率 
     */  
    public MessageHandler(String comPort,int boudRate) {  
        smsService = Service.getInstance();  
        gateway= new SerialModemGateway(  
                "DTU-900A." + comPort, comPort, 9600, "SIMCOM", "DTU-900A");//建立模块网关连接  
        gateway.setInbound(true); //接收短信使能 
        gateway.setOutbound(true); //发送短信使能 
        gateway.setProtocol(Protocols.PDU);  
        gateway.setSimPin("0000");  
        // gateway.set
        //System.out.println("初始化成功，准备开启服务");  
        try {
			smsService.addGateway(gateway);
		} catch (GatewayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 logger.info("try to start service"); 
		 //System.out.println("开启服务......."); 
		// smsService.set
		try {
			smsService.startService();
		} catch (SMSLibException | IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		       
        } 
    /** 
     * 断开连接 
     */  
    public void destroy() {  
        try {  
        	Service.getInstance().stopService();//关闭服务
        	Service.getInstance().removeGateway(gateway);//关闭网关 
        } catch (Exception ex) {  
            logger.error("SMS service stop error:", ex);  
        }  
          logger.info("SMS service stop");  
    }  
  
    /** 
     * send SMS 
     * @param phoneNumber 11位的手机号 
     * @param context 短信内容
     * @return Boolean 
     */  
    public Boolean sendSMS(String phoneNumber,String context) {  
    	OutboundMessage outMsg = new OutboundMessage(phoneNumber, context);  
        try {  
        	 outMsg.setEncoding(MessageEncodings.ENCUCS2);//采用中文方式  
             boolean status= smsService.sendMessage(outMsg);  
             return status;
        } catch (Exception e) {  
            logger.error("send error:", e);  
        }  
        return false;  
    }  
    /** 
     * open service 
     * @return Boolean 
     */ 
    private boolean isStarted() {  
        if (smsService.getServiceStatus() == Service.ServiceStatus.STARTED) {  
            for (AGateway gateway : smsService.getGateways()) {  
                if (gateway.getStatus() == AGateway.GatewayStatuses.STARTED) {  
                    return true;  
                }  
            }  
        }  
        return false;  
    }  
    /** 
     * read SMS 
     * @return List 
     */  
    public List<InboundMessage> readSMS() {  
        List<InboundMessage> msgList = new LinkedList<InboundMessage>();  
        if (!isStarted()) {  
            return msgList;  
        }  
        try {  
            this.smsService.readMessages(msgList,  
                    InboundMessage.MessageClasses.ALL);  
            logger.info("read SMS size: " + msgList.size());  
        } catch (Exception e) {  
            logger.error("read error:", e);  
        }  
        return msgList;  
    }  
    
 /** 
     * @param args 
 * @throws InterruptedException 
 * @throws GeneralSecurityException 
 * @throws MessagingException 
 * @throws IOException 
 * @throws SMSLibException 
 * @throws TimeoutException 
     */  
    public static void main(String[] args) throws InterruptedException {  
        Logger.getRootLogger().setLevel(Level.INFO);  
        SerialPortUtil serial=new SerialPortUtil(9600); 
        String GSMPort=serial.GetGsmPort();
        if(GSMPort!=null){
        	MessageHandler smsHandler = new MessageHandler(GSMPort,9600);  
            boolean status=smsHandler.sendSMS("18483617018", "信息测试");
            smsHandler.destroy();
            System.out.println(status);
        }
  }
    

}  