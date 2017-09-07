package fiberMonitor.bean;


public class RoutesJson 
{
	private long route_id;			//光路标识
	
	private String route_name;		//光路名
	
	private boolean status;			//光路状态
	
	private String length;			//光路长度
	
	private boolean retest;			//二次测试
	
	private boolean is_alert;		//是否有告警测试参数
	
	private String description;		//描述
	
	private String port_name;       //端口名称
	
	private int port_order;			//端口序号	
	
	private String station_name_b;  //终端局站名称
	
	private int framePort_order;         //frame port
	
	private int rtuPort_order;         //rtu的otdr端口
	
	private long rtu_id;            //所属RTU的ID
	
	private long station_id;		//所属局站的ID
	
	private boolean is_period;      //是否正在周期测试
	
	private String create_time;     //创建时间
	
	private String alter_time;		//修改时间
	
	private int port_umber;		//端口数
	
public long getroute_id()
{
	return route_id;
	}

public String getroute_name()
{
	return route_name;
}

public boolean getstatus()
{
	return status;
}

public String getlength()
{
	return length;
}

public boolean getretest()
{
	return retest;
}

public boolean getis_alert()
{
	return is_alert;
}

public String getdescription()
{
	return description;
}

public String getPort_name(){
	
	return port_name;
}

public int getPort_order(){
	
	return port_order;
}

public String getStation_name_b(){
	
	return station_name_b;
}

public long getRTU_id(){
	
	return rtu_id;
}

public long getstation_id(){
	
	return station_id;
}

public int getframePortOrder(){
	
	return framePort_order;
}
public int getRtuPortOrder(){
	
	return rtuPort_order;
}
public boolean getIs_period(){
	
	return is_period;
}

public String getCreate_time(){
	
	return create_time;
}

public String getAlter_time(){
	
	return alter_time;
}
/////////////////////////////////////////
public void setroute_id(long route_id)
{
	this.route_id=route_id;
}

public void setroute_name(String route_name)
{
	this.route_name=route_name;
}

public void setstatus(boolean status)
{
	this.status=status;
}

public void setlength(String length)
{
	this.length=length;
}

public void setretest(boolean retest)
{
	this.retest=retest;
}

public void setis_alert(boolean is_alert)
{
	this.is_alert=is_alert;
}

public void setdescription(String description)
{
	this.description=description;
}

public void setRtu_port_order(int rtuPortOrder){
	
	this.rtuPort_order=rtuPortOrder;
}
public void setFrame_port_order(int framePortOrder){
	
	this.framePort_order=framePortOrder;
}
public void setPort_order(int port_order){
	
	this.port_order=port_order;
}

public void setStation_name_b(String station_name_b){
	
	this.station_name_b=station_name_b;
}

public void setRtu_id(long rtu_id){
	
	this.rtu_id=rtu_id;
}

public void setStation_id(long station_id){
	
	this.station_id=station_id;
}

public void setIs_period(boolean is_period){
	
	this.is_period=is_period;
}

public void setCreate_time(String create_time){
	
	this.create_time=create_time;
}

public void setAlter_time(String alter_time){
	
	this.alter_time=alter_time;
}

public void setPortNumber(int port_count){
	
	this.port_umber=port_count;
}
public int getPortnumber(){
	
	return this.port_umber;
}
}
