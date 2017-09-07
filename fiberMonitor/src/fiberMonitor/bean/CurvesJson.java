package fiberMonitor.bean;

public class CurvesJson {

	private long curve_id;			//曲线标识
	
	private String curve_type;				//曲线来源：点名测试、周期测试、...
	
	private Boolean referring;				//是否参考
	
	private String test_date;			//测定时间：年月日
	
	private String description;			//描述
	
	private String route_name;			//光路名称
   /************************定义get方法***********************/
	public long getCurve_id(){
		return curve_id;
	}
	
	public String getCurve_type(){
		return curve_type;
	}
	
	public Boolean getReferring(){
		return referring;
	}
	
	public String getTest_date(){
		return test_date;
	}
	
	
	public String getDescription(){
		
		return description;
	}
	
	public String getRoute_name(){
		
		return route_name;
	}
	
	/*******************定义set方法******************/
	
	public void setCurve_id(long curve_id){
		this.curve_id=curve_id;
	}
	
	public void setCurve_type(String curve_type){
		
		this.curve_type=curve_type;
	}
	
	public void setReferring(boolean referring){
		
		this.referring=referring;
	}
	
	public void setTest_date(String test_date){
		this.test_date=test_date;
	}
	
	
	public void setDescription(String description){
		
		this.description=description;
	}
	
	public void setRoutename(String route_name){
		
		this.route_name=route_name;
	}
	
}
