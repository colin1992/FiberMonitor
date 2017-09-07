package fiberMonitor.bean;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSendDemo { 
	public static void main(String[] args) throws IOException{ 
		         System.out.println("=========测试方式：经过路由========"); 
				 System.out.println("client start…………"); 
				 //使用DatagramPacket将数据封装到该对象中 
				 byte[] buf=TCPComndUtil.comdSetMode((byte) 0x03,true); 
				 byte[] rebuf = new byte[128];
				 DatagramPacket reDp = new DatagramPacket(rebuf, rebuf.length);
				 long startTime=System.currentTimeMillis();
				 System.out.println("start time:"+startTime);
				 int count=5000;
				 int reCount=0;
				  for(int i=0;i<count;i++){
					 // System.out.println("time 1:"+System.currentTimeMillis());
					  DatagramSocket ds=new DatagramSocket(1714);//监听端口 
					 // System.out.println("time 2:"+System.currentTimeMillis());
					DatagramPacket dp= 
							  new DatagramPacket(buf, buf.length,InetAddress.getByName("192.168.0.119"),1711); 
					// System.out.println("time 3:"+System.currentTimeMillis());
							 //通过udp的socket服务将数据包发送出去，通过send方法 
							  ds.send(dp);
							  //System.out.println("time 4:"+System.currentTimeMillis());
							  while(true){
								  ds.receive(reDp);
								  //System.out.println("time 5:"+System.currentTimeMillis());
								  byte[] rev=Dgram.toString(reDp).getBytes();
									 // System.out.println(NumConv.byteArrayToHex(rev));
									  if(rev[0]==0x01){
										  reCount++; 
										  //关闭资源 
										  ds.close(); 
										  break;
									}
							}
							//  System.out.println("time 6:"+System.currentTimeMillis());
				  }
				  System.out.println("end time:"+System.currentTimeMillis()+"\nrepeat times："+count+"\nloss packets："+(count-reCount)+"\npacket loss rate:"+(1-reCount*1.0/count));
				  System.out.println("cost time:"+(System.currentTimeMillis()-startTime)+"ms\naverage time:"+(System.currentTimeMillis()-startTime)/(count*1.0)+"ms");
		} 
	}
