package fiberMonitor.bean;

import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 心之&所系
 * create date:2016/11/03
 * 说明：与切换RTU之间进行通信的类，用于生成下发指令和解析回复
 *   
 * **/
public class TCPComndUtil {
	  //帧头7e7e
     final static byte[] head={0x7e,0x7e};
     //通用地址 0xff
     final static byte addr=(byte)0xff;
     /**
      * 生成请求命令帧
      * @param order 命令字节
      * data为空
      * @return byte[]
      * **/
     private static byte[] requestComndCode(byte order){
    	 byte len=3;/**长度，从adr到sum的字节数，包括这两个字节
    	  			 对于命令帧，data为空，len为4*/
    	 byte adr=addr;//地址
    	 byte comnd=order;//命令字
    	 byte sum=(byte)(head[0]+head[1]+comnd+len+adr);//单字节校验和，head+adr+len+comnd+data
    	 byte[]code={head[0],head[1],len,adr,comnd,sum};
    	 return code;
     }
     /**
      * 生成设置命令帧
      * @param order 命令字节
      * 此时data不为空
      * @return byte[]
      * **/
     private static byte[] setComndCode(byte order,byte[]data){
    	 byte len=(byte)(data.length+3);/**长度，从adr到sum的字节数，包括这两个字节
    	  			 对于命令帧，data为空，len为4*/
    	 byte adr=addr;//地址
    	 byte comnd=order;//命令字
    	 byte sum=(byte)(head[0]+head[1]+comnd+len+adr);//单字节校验和，head+adr+len+comnd+data
    	 for(int i=0;i<data.length;i++){
    		 sum+=data[i];
    	 }
    	 byte[]code=new byte[len+3];
    	 byte []tem={head[0],head[1],len,adr,comnd};
    	 System.arraycopy(tem, 0, code, 0, tem.length);
    	 System.arraycopy(data, 0, code, tem.length, data.length);
    	 code[len+2]=sum;
    	 return code;
     }
     /**
      * 读取全部参数
      * 命令字为0x00
      * @return byte[]
      * **/
     public static byte[] comndGetAllPara(){
    	 byte cmd=0x00;
    	 return requestComndCode(cmd);
     }
     /**
      * 读取序列号
      * 命令字为0x01
      * @return byte[]
      * **/
     public static byte[] comndGetSN(){
    	 byte cmd=0x01;
    	 return requestComndCode(cmd);
     }
     /**
      * 读取告警
      * 命令字0x02
      * @return byte[]
      * **/
     public static byte[]comndGetAlarm(){
    	 byte cmd=0x02;
    	 return requestComndCode(cmd);
     }
     /**
      * 读取模块温度
      * 命令字0x03
      * @return byte[]
      * **/
     public static byte[]comndGetTemp(){
    	 byte cmd=0x03;
    	 return requestComndCode(cmd);
     }
   
     /**
      * 读取光功率值
      * 命令字0x20
      * @return byte[]
      * **/
     public static byte[]comndGetPowerVal(){
    	 byte cmd=0x20;
    	 return requestComndCode(cmd);
     }
     /**
      * 设置光功率输出门限
      * 命令字0x42
      * @param pth 光功率值 为两个字节
      * @return byte[]
      * **/
     public static byte[]comndSetPowerTh(float pth){
    	 byte cmd=0x42;
    	 return setComndCode(cmd, formatPTh(pth));
     }
     /**
      *设置输出光功率值
      *命令字0x18
      *@param 光功率值
     ****/
     public static byte[]comndSetPower(float pth){
    	 byte cmd=0x18;
    	 byte pVal[]=formatPTh(pth);
    	 byte []data={(byte) 0x80,pVal[0],pVal[1]};
    	 return setComndCode(cmd,data);
     }
    /**系统复位
      * 命令字：0xc0
      * @return byte[]
      * **/
     public static byte[] comndReset(){
    	 byte cmd=(byte)0xc0;
    	 return requestComndCode(cmd);
     }
     /**
      * 网络心跳检测回复(服务器检测客户端)
      * 命令字：0xe1
      * @return byte[]
      * **/
     public static byte[]resHeartbeat(){
    	 byte cmd=(byte) 0xe1;
    	 return requestComndCode(cmd);
     }
     /**
      * 服务器掉线
      * 命令字：0xe2
      * @return byte[]
      * **/
     public static byte[]comdSetServiceOff(){
    	 byte cmd=(byte) 0xe2;
    	 return requestComndCode(cmd);
     }
     /**
      * 设置新的服务器地址==========该条命令存疑
      * 命令字：0xe3
      * @param ip String 新的服务器IP地址
      * @param submask String 新的服务器子网掩码
      * @return byte[]
      * @throws IOException 
      * **/
     public static byte[]comdSetNewIp(String ip,String submask) throws IOException{
    	 byte cmd=(byte) 0xe3;
    	 byte []newIp=NumConv.stringArryTobyteArry(ip.split("\\."));//以小数点分隔
    	 byte []subMask=NumConv.stringArryTobyteArry(submask.split("\\."));
    	 byte[] localIp=InetAddress.getLocalHost().getAddress();//获得本机IP 
    	 byte[] data=new byte[newIp.length+subMask.length+localIp.length];
    	 System.arraycopy(newIp, 0, data, 0, newIp.length);
    	 System.arraycopy(localIp, 0, data, newIp.length, localIp.length);
    	 System.arraycopy(subMask, 0, data, newIp.length+localIp.length, subMask.length);
    	 //System.out.println("data:"+NumConv.byteArrayToHex(data));
    	 return setComndCode(cmd,data);
     }
     /**
      * 设置光开关工作模式
      * 切换光功率
      * 命令字：0xe4
      * 0x00-模式1（port1-3 port 2-4）平行方式
      * 0x01-模式2（port1-4 port 2-3）交叉方式
      * @param boolean  true:平行模式  
      * 				false:交叉模式
      * @param byte group 组号
      * @return byte[]
      * **/
     public static byte[]comdSetMode(byte group,boolean isParallel){
    	 byte cmd=(byte) 0xe4;
    	 byte[] mode={group,(isParallel)?(byte)0x00:0x01};
    	 return setComndCode(cmd,mode);
     }
     /**
      * 修改IP
      * @param ip String IP地址
      * @param newPort int 新的端口
      * @param subnet String 新的 子网掩码
      * **/
     public static byte[]comdSetIP(String ip,int newPort,String subnet){
    	 byte cmd=(byte) 0xe5;
    	 byte []newIp=NumConv.stringArryTobyteArry(ip.split("\\."));//以小数点分隔
    	 byte []subMask=NumConv.stringArryTobyteArry(subnet.split("\\."));
    	 byte []data=new byte[newIp.length+3+subMask.length];
    	 System.arraycopy(newIp, 0, data, 0, newIp.length);
    	 byte []port=NumConv.intToBytes(newPort);
    	 System.arraycopy(port, 0, data, newIp.length, port.length);
    	 data[newIp.length+port.length]=(byte)0x0a;
    	 System.arraycopy(subMask, 0, data, newIp.length+port.length+1, subMask.length);
    	 return setComndCode(cmd,data);
     }
     /**校验接收数据的正确性
      * 判断所有字节之和是否等于SUM
      * @param recData 接收的回传数据，为字节数组
      * @return boolean
      * **/
     public static boolean checkSum(byte[]recData){
    	 byte sum=0;
    	 int len=recData.length;
		 for(int i=0;i<len-1;i++){//进行校验和判定
			  sum+=recData[i];
		 }
		 return sum==recData[len-1];
     }
    /**
     * 将下发的光功率门限值转换成激光器接收的格式
     * Pin_th=(pTH+70)*10
     * @param pth float 输入的光功率门限
     * @return byte[] 转换后的字节数组，为两个字节
     * **/ 
     public static byte[] formatPTh(float pth){
    	short pTh=(short)((pth+70)*10);
    	byte[] format={(byte)((pTh&0xff00)>>8),(byte)(pTh&0x0000ff)};
		return format;
    	 
     }
     /**
      * 解析回复数据
      * @param recData byte[]接收到的数据，为字节组
      * @return Map<String,Object> 根据不同的数据有不同的返回结果
      *      操作                 		key             				  含义
      *   读取序列号                    SN          				 String类型的序列号
      *   读取光功率值                pwVal,pwTh   				  光功率值和门限值
      *   读取告警字节                powerIn，powerOut,temp       输入光功率，输出光功率，温度  均为Boolean，true正常 false告警
      *   设置命令                        status        				 boolean型 true为成功，false为失败
      * **/
     public  static Map<String,Object> anlyzeRecData(byte[]recData){
    	  Map<String,Object> decodeData=new LinkedHashMap<String,Object>();//解析完成的结果
    	  if(checkSum(recData)){//先校验数据是否正确
    		 //System.out.println("=======checked=======");
    		 byte len=recData[2];//数据长度
    		 byte cmnd=recData[4];//回传的命令字
    		 byte data[]=new byte[len-3];//数据
    		 System.arraycopy(recData, 5, data, 0, len-3);//读取回传数据
    		 //System.out.println("data part:"+NumConv.byteArrayToHex(data));
    		 switch (cmnd){
    		  case 0x00://读取全部参数
    			   
    			   break;
    		  case 0x01://读取序列号     每个字节都是无符号数
    			   long SN=(NumConv.byteToUnint(data[0])*65536+NumConv.byteToUnint(data[1])*256+NumConv.byteToUnint(data[2]));
    			   decodeData.put("SN",SN);//存储序列号
    			   decodeData.put("status",true);//
    			   break;
    		  case 0x02://读取告警字节
    			        /**返回的各告警位为Boolean型，true为正常  false为告警**/
    			   byte alarmBits[]=NumConv.byteToBitArray(data[0]);//读取告警位，只关心第一个字节
    			   decodeData.put("powerIn",alarmBits[0]==0);//输入光功率值告警位
    			   decodeData.put("powerOut",alarmBits[1]==0);//输出光功率告警位
    			   decodeData.put("temp",alarmBits[2]==0);//模块温度告警位
    			   decodeData.put("status",true);//
    			   break;
    		  case 0x03://读取模块温度
    			   float temp=(float) (NumConv.bytesToShort(data)/10.0);
    			   decodeData.put("temp",temp);//存储温度
    			   decodeData.put("status",true);//
    			   break;
    		  case 0x18://设置光功率输出值
    			   if(data[0]==(byte)0x80){//模式为设置定值
    				   decodeData.put("status",true);
    			   }
    			   else{
    				   decodeData.put("status",false);
    			   }
    			   break;
    		  case 0x20://读取光功率数据
    			   byte[] pw={data[2],data[3]};//光功率值
    			   byte[] pwT={data[6],data[7]};//光功率门限
    			   float pwVal=(float)(NumConv.bytesToShort(pw)/10.0-70);//转换值
    			   float pwTh=(float)(NumConv.bytesToShort(pwT)/10.0-70);
    			   decodeData.put("pwVal",pwVal);
    			   decodeData.put("pwTh",pwTh);
    			   decodeData.put("status",true);//
    			   //System.out.println("pVal:"+decodeData.get("pwVal"));
    			   break;
    		  case 0x42: //设置输出光功率告警门限
    			   decodeData.put("status",true);
    			   break;
    		  case (byte) 0xe1://服务器心跳检测
    			   break;
    		  case(byte)0xe3://更新IP
    			  decodeData.put("status",true);
    		  case (byte) 0xe4://设置光开关工作模式
    			  decodeData.put("status",true);
   			   break;
    		  case (byte)0xe5://修改IP
    			  decodeData.put("status",true);
    		      break;
    		  case (byte)0xff://发生故障时的回复码
    		   default:	  
    			  decodeData.put("status",false);
    			  break;
    		 }
    		
    	 }
    	 return decodeData;
     }
     /**
      * just for test
     * @throws InterruptedException 
      * **/
     public static void main(String args[]){} 
}
