package fiberMonitor.bean;

public class distributeFrameJson {
    private long frame_id;		//配线架标识
	
	private String frame_name;		//配线架名	
	
	private int port_number;		//配线架端口数
	
	private String description;		//描述
	
	private long stationId;        //局站标识
	
	private long cabinetId;        //机柜标识
	
	private int  rackOrder;         //机架序号
	
	private String create_time;     //创建时间
	
	private String alter_time;      //修改时间

	public void setFrame_id(long frame_id){
		this.frame_id=frame_id;
	}
	public long getFrame_id()
	{
		return frame_id;
	}
	
//frame_name
	public void setFrame_name(String name)
	{
		this.frame_name = name;
	}
	public String getFrame_name()
	{
		return frame_name;
	}
	
//port_number
	public void setPort_number(int number)
	{
		this.port_number = number;
	}
	public int getPort_number()
	{
		return port_number;
	}

//description
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public String getDescription()
	{
		return description;
	}
//局站id	
	public void setStationId(long stationId){
		this.stationId=stationId;
	}
	
	public long getStationId(){
		return stationId;
	}
	
//机柜ID
	public void setCabinetId(long cabinatId){
		this.cabinetId=cabinatId;
	}
	
	public long getCabinetId(){
		return cabinetId;
	}
	
//机架序号
	public void setRackOrder(int rackOrder){
		this.rackOrder=rackOrder;
	}
	
	public int getRackOrder(){
		return rackOrder;
	}
	
//创建时间
	public void setCreate_time(String create_time){
		this.create_time=create_time;
	}
	
	public String getCreate_time(){
		return create_time;
	}
	
//修改时间
	public void setAlter_time(String alter_time){
		this.alter_time=alter_time;
	}
	
	public String getAlter_time(){
		return alter_time;
	}
}
