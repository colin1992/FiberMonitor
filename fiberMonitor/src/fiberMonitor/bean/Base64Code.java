package fiberMonitor.bean;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class Base64Code {
	/**base64 Encode 
	 * 入口参数：String FileName:欲编码源文件位置
	 *          String encodeFileName 编码后的存储文件位置
	 * 返回参数： boolean:编码存储成功为true或者为false         
	*/

	public static boolean Base64Encode(String sourceFileName,String encodeFileName){
    	String encodeResult=new String();
		try {
			 InputStream dataIn =new FileInputStream(sourceFileName);
			 byte[] bytes=new byte[dataIn.available()];
				dataIn.read(bytes);
				encodeResult=new BASE64Encoder().encode(bytes); //采用Base64编码
				dataIn.close();//关闭文件
		        StringReader sr=new StringReader(encodeResult);
		        BufferedReader  br =new  BufferedReader(sr);
		        FileWriter fs=new FileWriter( encodeFileName);//存储编码后的结果
				String line=null;   
		        while((line = br.readLine())!=null){ 
		            fs.write(line);
		        }
		        fs.flush();
		        fs.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
						e.printStackTrace();
			return false;
			
		}
		
       return true;
 }
	  public static boolean Base64Decode(String sourceFileName,String encodeFileName) {
	   try {
			 FileInputStream dataIn=new FileInputStream(sourceFileName);
			 byte[] bytes = new BASE64Decoder().decodeBuffer(dataIn);  
		        ByteArrayInputStream inStream=new java.io.ByteArrayInputStream(bytes);
		        byte[]  buffer =new  byte[1024];
		        /*存储*/
		        FileOutputStream fs=new FileOutputStream(encodeFileName);
		        int byteread=0;    
		        while ((byteread=inStream.read(buffer))!=-1)  {    
		           fs.write(buffer,0,byteread);
		        }
		        fs.flush();
		        fs.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;  
		}
	   return true;
	}
	  
	  
	  /**Base64解码 
	   * 入口参数：
	   *    String decodeData:编码后的数据，字符串的方式
	   *    String encodeFileName：解码后存放数据的文件
	   * */
	public static byte[] Base64DecodeString(String decodeData,String encodeFileName){
		    byte[] bytes = null;
			try {
				bytes = new BASE64Decoder().decodeBuffer(decodeData);
				ByteArrayInputStream inStream=new java.io.ByteArrayInputStream(bytes);
				byte[]  buffer =new  byte[1024];
			    FileOutputStream fs=new FileOutputStream(encodeFileName);
			    int byteread=0;    
			    while ((byteread=inStream.read(buffer))!=-1)  {    
			        fs.write(buffer,0,byteread);
			   }
			    fs.flush();
			    fs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		return bytes;  
}	     
}
