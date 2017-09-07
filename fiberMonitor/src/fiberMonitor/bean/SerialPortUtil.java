package fiberMonitor.bean;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
/**
 * 功能说明：用于检测获取当前GSM模块连接串口的工具类
 * 检测方式：通过遍历发送AT测试指令，分析回复信息进行确认
 * @author 心之&所系
 * **/
public class SerialPortUtil implements SerialPortEventListener {
    private CommPortIdentifier portId;
	private InputStream inputStream;
	private OutputStream outputStream;
    private  SerialPort serialPort;
	private  String portName="";
	private int count=0;

    /**构造函数一,初始化时获取GSM端口，并设定波特率
     * @param buadRate 波特率
     **/
    public SerialPortUtil(int buadRate){//构造函数
    	getGSMMoulde(buadRate);//获取GSM模块的端口
    	
    }
    /**
     * 构造函数二 根据指定的端口和波特率连接模块
     * @param portName 端口名称
     * @param buadRate 波特率
     * **/
    public SerialPortUtil(String portName,int buadRate) {//构造函数
    	openPort(portName,buadRate);
	
    }
    public SerialPortUtil() {//构造函数
    	
	
    }
    /**
     * 获取GSM所在串口名称
     * @return  String portName  
	 * 如果不存在，则返回null
     * **/
    public String GetGsmPort(){
    	if(!portName.equals(""))
    	return this.portName;
    	else{
    		return null;
    	}
	}
    /**
     * 指定串口名和波特率打开串口
     * @param serialName 串口名，COMXX
     * @param buadRate 波特率
     * @return boolean 打开状态
     **/
    public boolean openPort(String serialName,int buadRate){
    	@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier>portList = CommPortIdentifier.getPortIdentifiers();
    	// 用循环结构找出串口
    	while (portList.hasMoreElements()) {
    		//  强制转换为通讯端口类型
    		portId = (CommPortIdentifier) portList.nextElement();
    		// 判断是否为串口
    		if ((portId.getPortType() == CommPortIdentifier.PORT_SERIAL)&&(portId.getName().equals(serialName))) {
    			//System.out.println("未占用："+portId.getName());
		    try {
					// open:（应用程序名【随意命名】，阻塞时等待的毫秒数）
					serialPort = (SerialPort) portId.open("serial",500);
					/* 设置串口通讯参数 */
					// 波特率，数据位，停止位和校验方式
					// 波特率9600,偶校验
					serialPort.setSerialPortParams(buadRate,
							SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
							SerialPort.PARITY_EVEN);
					outputStream = serialPort.getOutputStream();
		            // 设置串口监听
					serialPort.addEventListener(this);  
			        serialPort.notifyOnDataAvailable(true);  
			       // this.sendMsg("AT\r");//发送AT指令测试模块是否能接收AT指令
			        inputStream=serialPort.getInputStream();
			}
			catch ( TooManyListenersException e )  
			 {  
				return false;   //"端口"+serialParams.get( PARAMS_PORT ).toString()+"监听者过多";  
			 }  
			 catch (UnsupportedCommOperationException e )  
			  {  
			     return false;  //"端口操作命令不支持";  
			  }  
			 catch ( IOException e )  
			  {  
			     return false;  //"打开端口"+serialParams.get( PARAMS_PORT ).toString()+"失败";  
			  } 
			 catch (PortInUseException e)
			  {
				return false; 
			  }
		   }
      }
    	return true;
	}
   
    /**
     * 利用AT指令遍历测试所有端口
     * 获取连接GSM模块的端口
     * @param buadRate  波特率
     **/
    private void getGSMMoulde(int buadRate){
    	@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier>portList = CommPortIdentifier.getPortIdentifiers();
    	// 用循环结构找出串口
    	while (portList.hasMoreElements()) {
    		//  强制转换为通讯端口类型
    		portId = (CommPortIdentifier) portList.nextElement();
    		// 判断是否为串口
    		if ((portId.getPortType() == CommPortIdentifier.PORT_SERIAL)&&(!portId.isCurrentlyOwned())) {
    		try {
					// open:（应用程序名【随意命名】，阻塞时等待的毫秒数）
					serialPort = (SerialPort) portId.open("serial", 500);
					/* 设置串口通讯参数 */
					// 波特率，数据位，停止位和校验方式
					// 波特率9600,偶校验
					serialPort.setSerialPortParams(buadRate,
							SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
							SerialPort.PARITY_NONE);
					outputStream = serialPort.getOutputStream();
		            // 设置串口监听
					serialPort.addEventListener(this);  
			        serialPort.notifyOnDataAvailable(true);  
			        this.sendMsg("AT\r");//发送AT指令测试模块是否能接收AT指令
			        inputStream=serialPort.getInputStream();
			        Thread.sleep(500);//等待读取回传数据
			        serialPort.notifyOnDataAvailable(false);
		        	serialPort.removeEventListener();
		        	serialPort.close();
			        if(!portName.equals("")){//找到端口后就直接跳出循环，不再继续循环测试下一个端口
			        	break;
			        }
			 }
			catch ( TooManyListenersException e )  
			 {  
				continue;   //"端口"+serialParams.get( PARAMS_PORT ).toString()+"监听者过多";  
			 }  
			 catch (UnsupportedCommOperationException e )  
			  {  
				
				 continue;  //"端口操作命令不支持";  
			  }  
			 catch ( IOException e )  
			  {  
				 //System.out.println("端口"+serialPort.getName()+"打开失败");
				 continue;  //"打开端口"+serialParams.get( PARAMS_PORT ).toString()+"失败";  
			  } 
			 catch (PortInUseException e)
			  {
				 //System.out.println("端口"+serialPort.getName()+"已被占用"); 
				 continue; 
			  } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   }
      }
		
    }
	/**
	 * 实现接口SerialPortEventListener中的方法 读取从串口中接收的数据
	 */
	public void serialEvent(SerialPortEvent event) {
	  switch (event.getEventType()) {
		case SerialPortEvent.DATA_AVAILABLE://获取到串口返回信息
			  String name=serialPort.getName();
			  String eventSource=name.substring(name.indexOf("COM"),name.length());
			  if(portName.equals("")||portName.equals(eventSource))
			  readComm(eventSource);                 //在获取到端口以后，如果不为当前端口，屏蔽       
			  break;
		default:
			break;
		}
	}

	/**读取串口返回信息
     **/
	public void readComm(String eventSource) {
		 byte[]readBuffer=new byte[512];
		 int numBytes=0;
		 try {
			 Thread.sleep(100);
			numBytes = inputStream.read(readBuffer);
		  } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		 } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 byte[] temp = new byte[numBytes];  
         System.arraycopy(readBuffer, 0, temp, 0, numBytes);  
         String reMsg=new String(temp);
		 if(reMsg.contains("OK")||reMsg.contains("ok"))//看回复是否包含OK
			{  
			    count++;
			    if(count==1){
			    	 portName=eventSource;
			    	 this.sendMsg("AT+IPR=9600\r");//找到模块后将波特率设置为9600
			    }
			    if(count==2){
			    	this.sendMsg("AT&W\r");//找到模块后将波特率设置为9600
			    }
			    else{
			    	count=0;
			    }
				
			}
		 else if(reMsg.contains("ERROR")||reMsg.contains("error"))	 
    	   {
			 portName=eventSource;
		   }
		
	}  

	/**
	 * 关闭串口
	 */
	public  void closeSerialPort() {
		serialPort.notifyOnDataAvailable(false);
		serialPort.removeEventListener();
		serialPort.close();
	}

	/**向串口发送信息
	 *
	 * @param msg  欲发送的内容
	 * @return boolean 发送成功为true，否则为false
	 */
	public boolean sendMsg(String msg) {
		try {
			outputStream.write(msg.getBytes());
			inputStream = serialPort.getInputStream();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
public static void main(String args[]) throws InterruptedException{
	SerialPortUtil serial=new SerialPortUtil(9600);
	
	//System.out.println("start time:"+System.currentTimeMillis());
	String portName=serial.GetGsmPort();
	//System.out.println("end time:"+System.currentTimeMillis());
	//System.out.println("try to open:"+System.currentTimeMillis());
	MessageHandler smsHandler = new MessageHandler(portName,9600);
	//System.out.println("open time:"+System.currentTimeMillis());
	smsHandler.sendSMS("18428067311", "短信发送测试");  
	//System.out.println("send finished time:"+System.currentTimeMillis());
}	
}

