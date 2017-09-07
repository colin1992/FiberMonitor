package fiberMonitor.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;

public class ThreadTest extends Thread{
	private int i;
	public  ThreadTest(int i){
		this.i=i;
	}
   public void run(){
	      try {
				HttpClientUtil.Post("http://"+"192.168.0.140:5000/cgi-bin/BoaCom.cgi", "sdsd", "text.xml", 1000, 1500);
			} catch (HttpException | IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			System.out.println("=====this is thread==="+i+"\ncurrent time is"+System.currentTimeMillis());
		
   }
   public static void  main(String[] args){
	   List<ThreadTest> threads=new ArrayList<ThreadTest>();
	   for(int i=0;i<10;i++){
		   ThreadTest thres=  new ThreadTest(i);
		   threads.add(thres);
		   thres.start();
	   }
	   for(ThreadTest thres:threads){
		   try {
			thres.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
	   System.out.println("=====all is well==="+"\ncurrent time is"+System.currentTimeMillis());
   }
}
