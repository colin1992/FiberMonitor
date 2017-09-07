package fiberMonitor.paraReturn;

import java.util.LinkedHashMap;
import java.util.Map;

/*数据分析中的参数获取返回类
 * 包含Map testPara 
 * 和 int endPos*/
public class TestParaReturn {
	private  Map<String, Object> testPara=new LinkedHashMap<String, Object>();
	private  int endPos=0;
	  /*设置testPara*/
	public  void setTestPara(Map<String, Object> testPara){
		this.testPara=testPara;
		
	}
	/*设置endPos*/
	public  void setEndPos(int pos){
		this.endPos=pos;
		
	}
	/*获取testPara*/
	public  Map<String, Object> getTestPara(){
		
		return this.testPara;
		
	}
	/*获取endPos*/
	public  int getEndPos(){
		return this.endPos;
		
	}

}
