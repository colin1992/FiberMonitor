package fiberMonitor.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class  Demo{
	public void Print(){
		//System.out.println("Hello world");
	}
}
/**
 * TCP service  用于测试
 * **/
public class TCPService {
    /**
     * @param args
     */
	 Demo demo=new Demo();
    public static final int SERVER_PORT=8088;
    /****/
    public  class InnerStataiClass{
		public   void Print(){
			demo.Print();
		}
	}
    InnerStataiClass InnerStataiClass=new InnerStataiClass();
    public static void main(String[] args) throws IOException {
            //System.out.println("==============TCP Server===========");
            @SuppressWarnings("resource")
			ServerSocket vntServer= new ServerSocket(SERVER_PORT);
            //System.out.println("监听端口："+vntServer.getLocalPort()+"...");
            while(true){
                Socket vntClient=vntServer.accept();
                //System.out.println("检测到客户端接入 ，IP地址为： "+vntClient.getInetAddress());
               // //System.out.println("端口为："+vntClient.getPort());
                new GetGpsThreadFun(vntClient).start();
       }
    	
    }
}

class GetGpsThreadFun extends Thread{
    private Socket vntThreadClient;
    public GetGpsThreadFun(Socket vntThreadSocket){
        vntThreadClient=vntThreadSocket;
    }
    public void run(){
  	    try{
             byte[]recBbuf=new byte[1024];//128个字节的接收数据缓冲区
		      int hasRead=0;
			  ByteArrayOutputStream outStream = new ByteArrayOutputStream();//利用输出流来承接数据  
			  InputStream recv=vntThreadClient.getInputStream();//接收数据
			  hasRead=recv.read(recBbuf);
		      outStream.write(recBbuf,0,hasRead);
		      byte[] rec=outStream.toByteArray();
		      NumConv.byteArrayToHex(rec);//将接受数据转为字节数返回
		      //System.out.println("Received: " + hexString);
              OutputStream send= vntThreadClient.getOutputStream();
              
             switch (rec[4]){//判断指令
               case 0x01:
            	       byte[] resp={(byte)0xe7,(byte)0xe7,0x06,(byte)0xff,0x01,0x01,0x02,0x03,(byte) 0xda};
		               //System.out.println("回复："+NumConv.byteArrayToHex(resp));
		               send.write(resp);
                break;
               case 0x02: 
	            	   byte[] res={(byte)0xe7,(byte)0xe7,0x06,(byte)0xff,0x02,0x01,0x02,0x03,(byte) 0xdb};
		               //System.out.println("回复："+NumConv.byteArrayToHex(res));
		               send.write(res);
            	   break;
               case 0x03: 
            	   byte[] red={(byte)0xe7,(byte)0xe7,0x05,(byte)0xff,0x03,0x01,0x02,(byte) 0xdb};
            	   byte su=0;
            	   for(int i=0;i<red.length-1;i++){
            		   su+=red[i];
            	   }
            	   red[red.length-1]=su;
	               //System.out.println("回复："+NumConv.byteArrayToHex(red));
	               send.write(red);
        	   break;	   
               case 0x20:
	            	   byte[] re={(byte)0xe7,(byte)0xe7,0x0b,(byte)0xff,0x20,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,(byte) 0x1c};
	            	   byte sum=0;
	            	   for(int i=0;i<re.length-1;i++){
	            		   sum+=re[i];
	            	   }
	            	   re[re.length-1]=sum;
		               //System.out.println("回复："+NumConv.byteArrayToHex(re));
		               send.write(re);
               case (byte)0x42:
            	   byte[] resps={(byte)0xe7,(byte)0xe7,0x03,(byte)0xff,0x42,0x12};
            	   send.write(resps);
            	   break;
               case (byte) 0xe4://切换光开关
            	   byte[] resprs={(byte)0xe7,(byte)0xe7,0x03,(byte)0xff,(byte) 0xe4,(byte) 0xb4};
                   send.write(resprs);
            	   break;
               case(byte)0xe3:
            	   byte[] ip={(byte)0xe7,(byte)0xe7,0x03,(byte)0xff,(byte)0xe3,(byte) 0xb3}; 
                   send.write(ip);
            	   break;
            	   default:
            		   byte[] defaul={(byte)0xe7,(byte)0xe7,0x03,(byte)0xff,(byte)0xff,0x12};
            		   byte deSum=0;
            		   for(int i=0;i<defaul.length-1;i++){
            			   deSum+=defaul[i];
	            	   }
            		   defaul[defaul.length-1]=deSum;
                	   send.write(defaul);
            		   break;
             }
           vntThreadClient.close();
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
       }
    }
}
