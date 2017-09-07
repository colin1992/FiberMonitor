package fiberMonitor.controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/AIOServer")
public class AIOServer {
	public static void main(String[] args){
		//System.out.println(new InetSocketAddress(PORT));
	} 
    static final int PORT=8080;
    static List <AsynchronousSocketChannel> channelList=new ArrayList<>();
    public void startListen() throws IOException{
    	//创建一个线程池
    	ExecutorService executor=Executors.newFixedThreadPool(20);
        //以指定线程池来建立一个异步信道组
    	AsynchronousChannelGroup channelGroup=AsynchronousChannelGroup.withThreadPool(executor);
    	//以指定线程池来建立一个AsynchronousSocketChannel
    	AsynchronousServerSocketChannel serverChannel=AsynchronousServerSocketChannel.open(channelGroup).bind(new InetSocketAddress("192.168.0.108",PORT));//监听本地的端口
    	//System.out.println(new InetSocketAddress(PORT));
    	//使用CompeletionHandler接收来自客户端的请求
    	serverChannel.accept(null,new AcceptHandler(serverChannel));
    }
}
class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel,Object>{
    private AsynchronousServerSocketChannel serverChannel;
    public AcceptHandler(AsynchronousServerSocketChannel sc){
    	this.serverChannel=sc;
    }
    //定义buffer准备读取数据
    ByteBuffer buff=ByteBuffer.allocate(1024);
    //当实际IO操作完成时触发该方法
	@Override
	public void completed(final AsynchronousSocketChannel sc, Object attachment) {
		// 记录新连接进来的channel
		AIOServer.channelList.add(sc);
		//准备接收下一次的连接
		serverChannel.accept(null,this);
		sc.read(buff,null,new CompletionHandler<Integer,Object>() {
			public void completed(Integer result,Object attachment){
				buff.flip();
				//将buff中的内容转成字符串
				String content=StandardCharsets.UTF_8.decode(buff).toString();
				//遍历每个Channel，将接受到的信息存入个Channel中
				for(AsynchronousSocketChannel c:AIOServer.channelList){
					try
					{
						c.write(ByteBuffer.wrap(content.getBytes(("utf-8")))).get();
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
				buff.clear();
				//读取下一次数据
				sc.read(buff, null, this);;
			}
			@Override
			public void failed(Throwable exc, Object attachment) {
				// TODO Auto-generated method stub
				//System.out.println("读取失败："+exc);
				//将获取失败的信道从列表中删除
				AIOServer.channelList.remove(sc);
			}
		});
	}

	@Override
	public void failed(Throwable exc, Object attachment) {
		// TODO Auto-generated method stub
		
	}
	
}
