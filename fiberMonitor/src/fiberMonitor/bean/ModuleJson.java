package fiberMonitor.bean;


public class ModuleJson {
    private long id;			//模块类型标识

	private String module_type_name;		//模块类型名：otdr,wdm...

	private int port_number;		//端口数量

	private String module_name;				//自动生成模块名：模块类型+端口数量	

	private String create_date;			//创建日期
			
	private String alter_date;			//修改日期
   
	//id
	public void setId(long id){
		this.id = id;
	}
	public long getId(){
		return id;
	}
	//模块类别
	public void setModule_type_name(String module_type_name){
		this.module_type_name = module_type_name;
	}
	public String getModule_type_name(){
		return module_type_name;
	}
	//端口数量
	public void setPort_number(int port_number){
		this.port_number = port_number;
	}
	public int getPort_number(){
		return port_number;
	}
	//模块名
	public void setModule_name(String module_name){
		this.module_name = module_name;
	}
	public String getModule_name(){
		return module_name;
	}
	//创建日期
	public void setCreate_date(String create_date){
		this.create_date = create_date;
	}
	public String getCreate_date(){
		return create_date;
	}
	//修改日期
	public void setAlter_date(String alter_date){
		this.alter_date = alter_date;
	}
	public String getAlter_date(){
		return alter_date;
	}
	
}
