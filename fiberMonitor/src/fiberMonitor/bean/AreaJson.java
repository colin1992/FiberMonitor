package fiberMonitor.bean;

public class AreaJson {
	private long area_id;		//区域标识
	
	private String area_name;		//区域名
	
	private String description;		//描述
	
	private String create_date;			//创建日期
		
	private String alter_date;			//修改日期

	public long getArea_id(){
		return area_id;
	}
	
	public String getArea_name(){
		return area_name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getCreate_date(){
		return create_date;
	}
	
	public String getAlter_date(){
		return alter_date;
	}
	/********************************************/
	public void setArea_id(long area_id){
		this.area_id=area_id;
	}
	
	public void setArea_name(String area_name){
		this.area_name=area_name;
	}
	
	public void setDescription(String description){
		this.description=description;
	}
	
	public void setCreate_date(String create_date){
		this.create_date=create_date;
	}

	public void setAlter_date(String alter_date){
		this.alter_date=alter_date;
	}
}
