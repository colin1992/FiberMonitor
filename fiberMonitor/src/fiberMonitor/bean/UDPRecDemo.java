package fiberMonitor.bean;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPRecDemo {
			final int INPORT = 1712;
		    byte[] buf = new byte[512];
		    DatagramPacket dp = new DatagramPacket(buf, buf.length);
		    DatagramSocket socket;

		    public UDPRecDemo() {
		        try {
		            socket = new DatagramSocket(INPORT);// 创建一接收消息的对象，而不是每次接收消息都创建一个
		             long count=0;
		            System.out.println("Server started");
		            while (true) {
		                socket.receive(dp);
		                //接收到客户端的消息
		                String rcvd = Dgram.toString(dp) + ",from address:"
		                        + dp.getAddress() + ",port:" + dp.getPort();
		                System.out.println("From Client:"+rcvd);
		                count++;
		                System.out.println("index:"+count+"==rec:"+NumConv.byteArrayToHex( Dgram.toString(dp).getBytes())+"==current time:"+System.currentTimeMillis());
		                byte[] resprs={0x01,0x02,(byte)0x7e};
		                DatagramPacket echo = new DatagramPacket(resprs,resprs.length,
		                        dp.getAddress(), dp.getPort());
		                //将数据包发送给客户端
		                socket.send(echo);
		                System.out.println("==current time:"+System.currentTimeMillis());
		            }
		        } catch (SocketException e) {
		            System.err.println("Can't open socket");
		            System.exit(1);
		        } catch (IOException e) {
		            System.err.println("Communication error");
		            e.printStackTrace();
		        }
		    }

		    public static void main(String[] args) {
		    	System.out.println("接收端启动…………"); 
		        new UDPRecDemo();
		    }
		 
}
