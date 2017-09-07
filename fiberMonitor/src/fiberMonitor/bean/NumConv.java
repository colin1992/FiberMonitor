package fiberMonitor.bean;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * 数值转换工具类*/
public class NumConv {
    /**4字节组转换为32位的无符号整数
     * 高字节位在后*/
		public static int bytesToUint32(byte[] bytes) {  
		   int number = bytes[0] & 0xFF;  
		   // "|="按位或赋值。  
		   number |= ((bytes[1] << 8) & 0xFF00);  
		   number |= ((bytes[2] << 16) & 0xFF0000);  
		   number |= ((bytes[3] << 24) & 0xFF000000);  
		   return number;  
		} 
		/**2字节组转换为16位的无符号整数
		* 高字节位在后*/
		public static int bytesToUint16(byte[] bytes) {  
		   int number = bytes[0] & 0xFF;  
		   // "|="按位或赋值。  
		   number |= ((bytes[1] << 8) & 0xFF00);  
		   return number;  
		} 
		/**2字节组转换为16位的有符号整数
		* 高字节位在前*/
       public static short bytesToShort(byte[] bytes) {
    	   byte high =bytes[0];
    	   byte low = bytes[1];
    	   // 复原
    	   short z = (short)(((high & 0x000000FF) << 8) | (0x000000FF & low));
    	   return z;
       }
       /**
        * 四字节数转浮点数
        * **/
		public static float bytesToFloat(byte[] bytes) { 
		    int num; 
		    num = bytes[ 0]; 
		    num &= 0xff; 
		    num |= ((long) bytes[1] << 8); 
		    num &= 0xffff; 
		    num |= ((long) bytes[2] << 16); 
		    num &= 0xffffff; 
		    num |= ((long)bytes[3] << 24); 
		    return Float.intBitsToFloat(num); 
		}	
	/**时间格式化***/	
	public static String timeFormat(String testTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String formatTime = sdf.format(new Date(Long.parseLong(testTime)*1000));
        return formatTime.replace("-0", "-");//将形如2016-08-01 转换为2016-8-1
	}
/**16位整数 转为双字节
 * 低字节为高八位 高字节为低八位
 * **/	
	public static byte[] intToBytes(int data){
		byte[] bytes=new byte[2];
		bytes[1]=(byte)(data&0x00000000ff);
		bytes[0]=(byte)((data>>8)&0x000000ff);
		return bytes;
	}
/**
 * 获取当前系统时间
 * @return 字符串形式的当前时间 格式为yyyy-MM-dd HH:mm:ss
 * **/
  public static String currentTime() {
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	return dateFormat.format(new Date());
	}
 /***
  * 获取当前星期
  * @return int 当天礼拜的顺序 星期一 1 星期天7
  * ***/ 
  public static int todayWeek(){
	  Calendar cal = Calendar.getInstance();
	  return cal.get(Calendar.DAY_OF_WEEK)-1;
  }
  /**
   * 经纬度转换，小数形式与经纬度形式的相互转换
   * @param lngAndLat String 字符串形式的经纬度
   * @return String 转换后的经纬度
   * **/
  public static String lngAndLatConv(String lngAndLat) {
		String format="";
		if(lngAndLat.contains("°")){//度数形式转换为小数
			String degree=lngAndLat.split("°")[0];
			String minute=(lngAndLat.split("°")[1]).split("′")[0];
			String second=lngAndLat.split("°")[1].split("′")[1].split("″")[0];
			format=String.valueOf(Float.parseFloat(degree)+Float.parseFloat(minute)/60+Float.parseFloat(second)/3600);
		}else{//小数转为度分秒
		   //小数点“.”代表匹配的是任意字符，而不是小数点了  必须用\\.的方式匹配小数点
			String degree=lngAndLat.split("\\.")[0];
			double degFloat=Double.parseDouble("0."+lngAndLat.split("\\.")[1]);
			/**分是小数部分乘以60取整**/
			String minute=String.valueOf(degFloat*60).split("\\.")[0];
			double minFloat=Double.parseDouble("0."+String.valueOf(degFloat*60).split("\\.")[1]);
			String second=String.valueOf(minFloat*60);
			format=degree+"°"+minute+"′"+second+"″";
		}
		return format;
	}
  /**
   * 获取加密后的字符串
   * 主要用于用户密码在存进数据库前进行加密，避免明文存放
   * MD5算法是不可逆的
   * @param input
   * @return String 通过MD5加密后的数据 
   */
  public static String passwordMD5(String pw) {
   try { 
     
         // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”） 
         MessageDigest messageDigest =MessageDigest.getInstance("MD5"); 
         // 输入的字符串转换成字节数组 
         byte[] inputByteArray = pw.getBytes(); 
         // inputByteArray是输入字符串转换得到的字节数组 
         messageDigest.update(inputByteArray); 
         // 转换并返回结果，也是字节数组，包含16个元素 
         byte[] resultByteArray = messageDigest.digest(); 
         // 字符数组转换成字符串返回 
         return byteArrayToHex(resultByteArray); 
      } catch (NoSuchAlgorithmException e) { 
         return null; 
      } 
  }
  
  /** 
   * 将时间戳转换为时间
   * @param  stamp String时间戳 
   * @return String 时间
   */
  public static String stampToDate(String stamp){
      String res;
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      long lt = new Long(stamp);
      Date date = new Date(lt);
      res = simpleDateFormat.format(date);
      return res;
  }
  
  /**
   * 将时间转换为Unix时间戳和java时间戳相差1000倍
   * @param date String 时间
   * @return String 时间戳
   */    
  public static String dateToUnixStamp(String date) throws ParseException{
      String res;
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date dat=simpleDateFormat.parse(date);
      long ts=dat.getTime()/1000;
      res = String.valueOf(ts);
      return res;
  }
  /**
   * 将时间转换为Java时间戳和java时间戳相差1000倍
   * @param date String 时间
   * @return String 时间戳
   */    
  public static String dateToJavaStamp(String date) throws ParseException{
      String res;
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date dat=simpleDateFormat.parse(date);
      long ts=dat.getTime();
      res = String.valueOf(ts);
      return res;
  }
  /**
   * 字节组转为hex形式显示
   * @param byteArray byte[] 待转换的字节组
   * @return String Hex形式的字符串
   * **/
  public static String byteArrayToHex(byte[] byteArray) { 
        
         // 首先初始化一个字符数组，用来存放每个16进制字符 
         char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' }; 
         // new一个字符数组，这个就是用来组成结果字符串 
         char[] resultCharArray =new char[byteArray.length * 2]; 
         // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去 
         int index = 0;
         for (byte b : byteArray) { 
            resultCharArray[index++]=hexDigits[b>>> 4 & 0xf]; 
            resultCharArray[index++]=hexDigits[b& 0xf]; 
         }
         // 字符数组组合成字符串返回 
         return new String(resultCharArray); 
     }
  /** 
   * 
   * 将byte转换为一个长度为8的byte数组，数组每个字节表示一个bit
   * @param b byte 待转换的字节
   * @return byte[] 
   */  
  public static byte[] byteToBitArray(byte b) {  
      byte[] array = new byte[8];  
      for (int i = 7; i >= 0; i--) {  
          array[i] = (byte)(b & 1);  
          b = (byte) (b >> 1);  
      }  
      return array;  
  }  
  /** 字节转换为位
   * 字符串形式 
   * @param b byte 带转换的字节
   * @return String 
   */  
  public static String byteToBitString(byte b) {  
      return ""  
              + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)  
              + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)  
              + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)  
              + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);  
  }
  /**
   * 数值字符串数组转字节组
   * @param byteStr String[] 待转换的字符串数组
   * @return byte[] 转换完成的字节组
   * **/
  public static byte[] stringArryTobyteArry(String[] byteStr) {  
     byte[] arry=new byte[byteStr.length];
     for(int i=0;i<byteStr.length;i++){
    	 arry[i]=(byte)Integer.parseInt((byteStr[i]));
     }
     return arry;
  }
  /**
   * 数值字符串转字节组
   * @param byteStr String 待转换的字符串
   * @return byte[] 转换完成的字节组
   * **/
  public static byte[] stringTobyteArry(String byteStr) {  
     byte[] arry=new byte[byteStr.length()];
     for(int i=0;i<byteStr.length();i++){
    	 arry[i]=(byte)byteStr.charAt(i);
     }
     return arry;
  }
  /** 
   * 4位或8位二进制字符串转byte 
   * @param biteStr String 待转换的位字符串
   * @return byte 转换后的单字节
   */  
  public static byte binaryStringToByte(String biteStr) {  
      int re, len;  
      if (null == biteStr) {  
          return 0;  
      }  
      len = biteStr.length();  
      if (len != 4 && len != 8) {  
          return 0;  
      }  
      if (len == 8) {// 8 bit处理  
          if (biteStr.charAt(0) == '0') {// 正数  
              re = Integer.parseInt(biteStr, 2);  
          } else {// 负数  
              re = Integer.parseInt(biteStr, 2) - 256;  
          }  
      } else {// 4 bit处理  
          re = Integer.parseInt(biteStr, 2);  
      }  
      return (byte) re;  
  } 
/**byte 转为无符号数
 * **/
  public static int byteToUnint(byte b){
	  return (int)(0x00000000ff&b);
  }
/**
 * 生成四位的随机验证码**/	
  public static String createVerifyCode(int codeLen){
	  String source = "0123456789";
	  int souLen = source.length();
      Random rand = new Random(System.currentTimeMillis());
      StringBuilder verifyCode = new StringBuilder(codeLen);
      for(int i = 0; i <codeLen; i++){
          verifyCode.append(source.charAt(rand.nextInt(souLen-1)));
      }
	  return verifyCode.toString();//生成随机字串 
		        
  } 
/**获取MAC地址
 * 返回以字符连接的MAC地址
 * 如果获取失败，返回默认的MAC "3C-46-D8-CC-80-2B"
 */
  public static String getMACAddress(){  
 	 String MAC="";
 	 try {
			 //获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。 
			InetAddress ia = InetAddress.getLocalHost();
			 byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();  
	         //下面代码是把mac地址拼装成String  
	         StringBuffer macSb = new StringBuffer();  
	         for(int i=0;i<mac.length;i++){  
	             if(i!=0){  
	            	 macSb.append("-");  
	             }  
	             //mac[i] & 0xFF 是为了把byte转化为正整数  
	             String s = Integer.toHexString(mac[i] & 0xFF);  
	             macSb.append(s.length()==1?0+s:s); 
	             //把字符串所有小写字母改为大写成为正规的MAC地址
	             MAC=macSb.toString().toUpperCase();
	         }  
		} catch (UnknownHostException | SocketException e) {
			//出错之后采用默认的MAC
			MAC="3C-46-D8-CC-80-2B";
			e.printStackTrace();
		}
     return MAC;  
  }  
  /**
   * 基于MAC地址生成20位数的序列号用于部署时的产品验证，
   * 初始化时必须输入正确的验证码才能使用系统
   * @param boolean 是否需要格式化  
   * 		true 生成形如76BA8-E83A7-F2C16-3BA0F的序列号
   *        false 生成形如76BA8E83A7F2C163BA0F的序列号
   *             
   * @return String 基于MAC的20位序列号
   * **/
  public static String createSNCode(boolean isFormat){
	  String SN=passwordMD5(getMACAddress()).substring(0,20);
	  String SNCode="";
	  if(isFormat){//需要格式化
		  for(int i=1;i<=SN.length()/5;i++){
			  SNCode+=SN.substring(5*(i-1), 5*i);
			  if(i!=4){
				  SNCode+="-"; 
			  }
		  }
	  }
	  else{
		  SNCode=SN;
	  }
	  return SNCode;
  }
  public static void main(String[] args){
	  //System.out.println("verifyCode:"+createSNCode(true)+"\nresult:"+"7B2700AA3DEA8437B7A1".equals(createSNCode(false)));
  }
}
