package fiberMonitor.bean;
import java.util.Enumeration;  
import java.util.Properties;  
  
import org.springframework.beans.BeansException;  
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;  
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;  
  /***
   * 读取数据库配置文件时进行AES解密
   * ***/
public class GvtvPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {  
    @Override  
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {  
          
        AESHelper aesHelper = new AESHelper();  
        Enumeration<?> keys = props.propertyNames();  
        while (keys.hasMoreElements()) {  
            String key = (String)keys.nextElement();  
            String value = props.getProperty(key);  
            if (key.endsWith(".encryption") && null != value) {  
                props.remove(key);  
                key = key.substring(0, key.length()-11); 
                value = aesHelper.decrypt(value.trim());  
                props.setProperty(key, value);  
            }  
            System.setProperty(key, value);  
        }  
          
        super.processProperties(beanFactoryToProcess, props);  
    }  
}  