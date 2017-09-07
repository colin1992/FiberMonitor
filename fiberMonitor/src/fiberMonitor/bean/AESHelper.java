package fiberMonitor.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;  
import java.io.OutputStream;  
import java.security.MessageDigest;  
import java.security.spec.AlgorithmParameterSpec;
import java.util.Enumeration;
import java.util.Properties;

import javax.crypto.Cipher;  
import javax.crypto.SecretKey;  
import javax.crypto.spec.IvParameterSpec;  
import javax.crypto.spec.SecretKeySpec;  
  
/** 
 * AES工具类，用于数据库配置文件的加密和解密
 */  
public class AESHelper {  
    Cipher ecipher;  
    Cipher dcipher;   
    /** 
     * Input a string that will be md5 hashed to create the key. 
     *  
     * @return void, cipher initialized 
     */  
    public AESHelper() {  
        SecretKeySpec skey = new SecretKeySpec(getMD5("fiberMonitor"), "AES");
        this.setupCrypto(skey);  
    }  
    public AESHelper(String key) {  
        SecretKeySpec skey = new SecretKeySpec(getMD5(key), "AES");  
        this.setupCrypto(skey);  
    }   
    private void setupCrypto(SecretKey key) {  
        // Create an 8-byte initialization vector  
        byte[] iv = new byte[] {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};  
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);  
        try {  
            ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");  
            dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");   
            // CBC requires an initialization vector  
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);  
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);  
        }  
        catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    /** 
     * AES加密
     *@param String 要解密的密文
     *@return String 解密后的明文
     */   
    public String encrypt(String plaintext) {  
        try {  
            byte[] ciphertext = ecipher.doFinal(plaintext.getBytes("UTF-8"));  
            return byteToHex(ciphertext);  
        }  
        catch (Exception e) {  
            e.printStackTrace();  
            return new String();  
        }         
    }  
    /** 
     * AES解密
     *@param String 要解密的密文 
     *@return String 解密后的明文
     */  
    public String decrypt(String hexCipherText) {  
        try {  
            String plaintext = new String(dcipher.doFinal(hexToByte(hexCipherText)), "UTF-8");  
            return plaintext;  
        }  
        catch (Exception e) {  
            e.printStackTrace();  
            return new String();  
        }  
    } 
    
    public static void main(String[]ar){
    	AESHelper aes=new AESHelper();
    	String key=aes.encrypt("123456");
    	System.out.println(key);
    	System.out.println(aes.decrypt(key));
    }  
       
    //获取MD5  
    private static byte[] getMD5(String input) {  
        try {  
            byte[] bytesOfMessage = input.getBytes("UTF-8");  
            MessageDigest md = MessageDigest.getInstance("MD5"); 
            return md.digest(bytesOfMessage);  
        }  
        catch (Exception e) {  
            return null;  
        }  
    }  
    /** 
     * 数据库配置文件加密 
     * @param filePath 文件路径，即文件所在包的路径，例如：java/util/config.properties 
     * @return 
     */  
    public static boolean ConfigurationEncryption(String filePath) {   
        AESHelper aesHelper = new AESHelper();//AES加密工具类  
        Properties prop = new Properties();  
        try {  
            File file = new File(filePath);  
            if (file.exists()){
            	InputStream fis = new FileInputStream(file);  
                prop.load(fis); //以key-value的方式载入配置文件
                //一定要在修改值之前关闭fis  
                fis.close(); 
              
                Enumeration<?> keys = prop.propertyNames();  
                while (keys.hasMoreElements()) {  
                    String key = (String)keys.nextElement();  
                    String value = prop.getProperty(key);  
                    prop.remove(key);
                    if (!key.endsWith(".encryption")&&null!=value) {  
                    	value = aesHelper.encrypt(value.trim()); //将明文进行AES加密 
                        prop.setProperty(key+".encryption", value);//存放数据库配置信息  
                    }  
                }
                if(!prop.isEmpty()){
                	  OutputStream fos = new FileOutputStream(filePath);  
                	 //保存，并加入注释  
                    prop.store(fos, "JDBC Encrypted Configuration ");  
                    fos.close(); 
                }   
                return true;
            }else{//文件不存在
            	System.out.println("can't find file property in "+filePath);
            	return false;
           }
        } catch (Exception e) {  
            System.err.println("exception occured when try to  encrypting JDBC Configuration");
            return false;
        }  
    }       
    /***字节转为hex形式**/  
    private  String byteToHex(byte[] raw) {  
         String HEXES = "0123456789ABCDEF";  
        if (raw == null) {  
            return null;  
        }  
        final StringBuilder hex = new StringBuilder(2 * raw.length);  
        for (final byte b : raw) {  
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));  
        }  
        return hex.toString();  
    }  
    /***字节转为hex形式转为字节**/   
    private  byte[] hexToByte(String hexString) {  
        int len = hexString.length();  
        byte[] ba = new byte[len / 2];  
        for (int i = 0; i < len; i += 2) {  
            ba[i / 2] =  
                (byte)((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));  
        }  
        return ba;  
    }       
}  
