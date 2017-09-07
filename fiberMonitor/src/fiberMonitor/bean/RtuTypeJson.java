package fiberMonitor.bean;

public class RtuTypeJson {
	private long rtu_type_id;		//RTU类型标识
	
	private String rtu_type_name;		//RTU类型名
	
	private String description;		//描述
	
	private String module1Name;  //模块1的描述
	
	private String module2Name;  //模块2的描述
	
	private String create_date;         //创建时间
	
	private String alter_date;          //修改时间
////////////////////rtu类型标识////////////////////	
	public void setRtu_type_id(long rtu_type_id){
		this.rtu_type_id=rtu_type_id;
	}
	
	public long getRtu_type_id(){
		return rtu_type_id;
	}
///////////////////rtu类型名字//////////////////	
	public void setRtu_type_name(String rtu_type_name){
		this.rtu_type_name=rtu_type_name;
	}
	public String getRtu_type_name(){
		return rtu_type_name;
	}
////////////////rtu类型描述//////////////////////
	public void setDescription(String description){
		this.description=description;
	}
	public String getDescription(){
		return description;
	}
/////////////////模块1的描述///////////////////
	public void setModule1Name(String module1Name){
		this.module1Name=module1Name;
	}
	public String getModule1Name(){
		return module1Name;
	}
/////////////////模块2的描述///////////////////
	public void setModule2Name(String module2Name){
		this.module2Name=module2Name;
	}
	public String getModule2Name(){
		return module2Name;
	}
///////////////创建时间/////////////////////
	public void setCreate_date(String create_date){
		this.create_date=create_date;
	}
	public String getCreate_date(){
		return create_date;
	}
//////////////////修改时间////////////////////
	public void setAlter_date(String alter_date){
		this.alter_date=alter_date;
	}
	public String getAlter_date(){
		return alter_date;
	}
}
