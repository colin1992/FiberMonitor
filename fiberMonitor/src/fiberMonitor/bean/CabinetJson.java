package fiberMonitor.bean;

public class CabinetJson {
	
    private long id;		//机柜标识
	
	private String cabinet_name;	//机柜名称
	
	private int rack_number;	//机架数量
	
	private String description;	//描述
	
	private long stationId;     //局站标识
	
	private String create_date;	//创建时间
	
	private String alter_date;	//修改时间
	
	//机柜标识
	public void setId(long id){
		this.id = id;
	}
	public long getId(){
		return id;
	}
	//机柜名称
	public void setCabinet_name(String cabinete_name){
		this.cabinet_name = cabinete_name;
	}
	public String getCabinete_name(){
		return cabinet_name;
	}
	//机架数量
	public void setRack_number(int rack_number){
		this.rack_number=rack_number;
	}
	public int getRack_number(){
		return rack_number;
	}
	//描述
	public void setDescription(String description){
		this.description = description;
	}
	public String getDescription(){
		return description;
	}
	//局站标识
	public void setStationId(long stationId){
		this.stationId = stationId;
	}
	public long  getStationId(){
		return stationId;
	}
	//创建时间
	public void setCreate_date(String create_date){
		this.create_date = create_date;
	}
	public String getCreate_date(){
		return create_date;
	}
	//修改时间
	public void setAlter_date(String alter_date){
		this.alter_date = alter_date;
	}
	public String getAlter_date(){
		return alter_date;
	}
}
