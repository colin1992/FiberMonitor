

/*光路*/


package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="ROUTES")
public class Routes 
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true,updatable=false)
	private Long id;			//光路标识
	
	@Column(name="NAME",nullable=true)
	private String route_name;		//光路名
	
	@Column(name="LENGTH")
	private String length;			//光路长度
	
	@Column(name="IS_BROKEN",nullable=false,columnDefinition="boolean default false")
	private boolean is_broken;			//光路状态，是否损坏
	
	@Column(name="IS_PERIOD",columnDefinition="boolean default false")
	private boolean is_period;	//是否有周期测试
	
	@Column(name="IS_ONLINE",columnDefinition="boolean default false")
	private boolean is_online;		//是否是在线纤\备纤,false代表备纤
	
	@Column(name="DESCRIPTION")
	private String description;		//描述
	
	@Column(name="CROSS_NUMBER")
	private int cross_number;		//跨段次数：添加光路跳纤时，指定为0
	
	@Column(name="RTU_ID",nullable=false,updatable=false)
	private Long rtu_id;	//指定该光路所属rtu
	
	@Column(name="RTU_Model_ORDER",nullable=false,updatable=false)
	private int rtu_model_order;	//指定rtu下光端口所在的model的序号
	
	@Column(name="RTU_PORT_ORDER",nullable=false,updatable=false)
	private int rtu_port_order;	//指定rtu下光端口序号
	
	@Column(name="FRAME_A_ID",nullable=false)
	private Long frame_a_id;		//指定该光路a端所连配线架id
	
	@Column(name="FRAME_A_ORDER",nullable=false)
	private int frame_a_order;		//指定该光路a端所连配线架端口序号
	
	@Column(name="FRAME_Z_ID",nullable=false)
	private Long frame_z_id;		//指定该光路末端所连配线架id
	
	@Column(name="FRAME_Z_ORDER",nullable=false)
	private int frame_z_order;		//指定该光路末端所连配线架端口序号
	
	@Column(name="CREATE_DATE",updatable=false)
	private String create_date;			//创建日期
	
	@Column(name="ALTER_DATE")			
	private String alter_date;			//修改日期
	
	@Column(name="CREATE_USER",updatable=false)
	private String create_user;		//创建用户
	
	@Column(name="ALTER_USER")
	private String alter_user;		//修改用户
	
	@Column(name="STATION_A_ID")
	private Long station_a_id;		//a端局站的id

	@Column(name="STATION_A_NAME")
	private String station_a_name;		//a端局站的名称

	@Column(name="STATION_Z_ID")
	private Long station_z_id;		//z端局站的id

	@Column(name="STATION_Z_NAME")
	private String station_z_name;		//z端局站的名称
	
	@Column(name="PREPARATORY_ID")
	private Long preparatoty_id;		//该光路对应的预备光路(逻辑字段，非物理字段)
	
	@Column(name="RTU_NAME")
	private String rtu_name;			//rtu name
    
	@Column(name="IS_OBSTACLE",columnDefinition="boolean default false")
	private boolean is_obstacle;			//障碍告警测试
	
	@Column(name="PRIOTITY_ID",nullable=true)
	private Long priotity_id;			//告警组ID
	
	@Column(name="PRIOTITY_NAME")
	private String priotity_name;			//告警组name
	
	@Column(name="IS_PROTECT",columnDefinition="boolean default false")
	private boolean is_protect;			//是否有配对
	
	@Column(name="IS_UPLINK",columnDefinition="boolean default false")
	private boolean is_uplink;			//是否为上行链路，用于区分光路为下行还是上行链路 
	
	@Column(name="SWITCH_JUMPER_ID",nullable=true)
	private Long switch_jumper_id;			//切换跳纤ID
	
	@Column(name="SWITCH_RTU_ID",nullable=true)
	private Long switch_rtu_id;			  //切换rtu_id
	
	@Column(name="SWITCH_RTU_ORDER",nullable=true)
	private Integer switch_rtu_order;		//指定该光路所连切换rtu端口序号
	
	@Column(name="BACKUP_RTU_ID",nullable=true)
	private Long backup_rtu_id;			  //备纤光源rtu_id
	
	@Column(name="BACKUP_JUMPER_ID",nullable=true)
	private Long backup_jumper_id;			  //备纤光源跳纤id
	
	@Column(name="BACKUP_RTU_ORDER",nullable=true)
	private Integer backup_rtu_order;		//备纤光源rtu端口序号
	
//foreign key
	
	//Optimize_parameters        获取该光路的优化测试参数
	@OneToOne(targetEntity=Optimize_parameters.class,mappedBy="route")
	@Cascade({CascadeType.DELETE})
	private Optimize_parameters optimize_parameter;
	public Optimize_parameters getOptimize_parameter()		//获取该光路的优化测试参数
	{
		return this.optimize_parameter;
	}
	
	//Period_parameters        获取该光路的周期测试参数
	@OneToOne(targetEntity=Period_parameters.class,mappedBy="route")
	@Cascade({CascadeType.DELETE})
	private Period_parameters period_parameters;
	public Period_parameters getPeriod_parameters()		//获取周期测试光路的周期测试参数
	{
		return this.period_parameters;
	}
	
	//Curves					该光路下的许多测试曲线
	@OneToMany(targetEntity=Curves.class,mappedBy="route")
	@Cascade({CascadeType.DELETE})
	private List<Curves> curves = new ArrayList<Curves>();
	public List<Curves> getCurves()
	{
		return this.curves;
	}
	
	//Topologic_routes
	@OneToMany(targetEntity=Topologic_routes.class,mappedBy="route")
	@Cascade({CascadeType.DELETE})
	private List<Topologic_routes> topologic_routes = new ArrayList<Topologic_routes>();
	public List<Topologic_routes> getTopologic_routes()
	{
		return this.topologic_routes;
	}
	
	//Topologic_points
	@OneToMany(targetEntity=Topologic_points.class,mappedBy="route")
	@Cascade({CascadeType.DELETE})
	private List<Topologic_points> topologic_points = new ArrayList<Topologic_points>();
	public List<Topologic_points> getTopologic_points()
	{
		return this.topologic_points;
	}
	
//Routes()
	public Routes(){}
	
//route_id
	public Long getId()
	{
		return this.id;
	}
	
//route_name
	public void setRoute_name(String name)
	{
		this.route_name = name;
	}
	public String getRoute_name()
	{
		return this.route_name;
	}
	
//is_broken
	public void setIsBroken(boolean is_broken)
	{
		this.is_broken = is_broken;
	}
	public boolean getIsBroken()
	{
		return this.is_broken;
	}
	
//length
	public void setLength(String len)
	{
		this.length = len;
	}
	public String getLength()
	{
		return length;
	}

	
//is_period
	public void setIs_period(boolean is_period)
	{
		this.is_period = is_period;
	}
	public boolean getIs_period()
	{
		return is_period;
	}
	
//description
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getDescription()
	{
		return this.description;
	}
	
//rtu_id
	public void setRtu_id(Long rtu_id)
	{
		this.rtu_id = rtu_id;
	}
	public Long getRtu_id()
	{
		return this.rtu_id;
	}
	
//cross_number
	public void setCross_number(int cross_number)
	{
		this.cross_number = cross_number;
	}
	public int getCross_number()
	{
		return this.cross_number;
	}
	
//model_order
	public void setRtu_model_order(int rtu_model_order)
	{
		this.rtu_model_order = rtu_model_order;
	}
	public int getRtu_model_order()
	{
		return this.rtu_model_order;
	}
//port_order
	public void setRtu_port_order(int rtu_port_order)
	{
		this.rtu_port_order = rtu_port_order;
	}
	public int getRtu_port_order()
	{
		return this.rtu_port_order;
	}
	
	
//is_online
	public void setIs_online(boolean is_online)
	{
		this.is_online = is_online;
	}
	public boolean getIs_online()
	{
		return is_online;
	}
	
//frame_a_id
	public void setFrame_a_id(Long frame_a_id)
	{
		this.frame_a_id = frame_a_id;
	}
	public Long getFrame_a_id()
	{
		return this.frame_a_id;
	}
	
//frame_a_order
	public void setFrame_a_order(int frame_a_order)
	{
		this.frame_a_order = frame_a_order;
	}
	public int getFrame_a_order()
	{
		return this.frame_a_order;
	}
	
//	frame_z_id
	public void setFrame_z_id(Long frame_z_id)
	{
		this.frame_z_id = frame_z_id;
	}
	public Long getFrame_z_id()
	{
		return this.frame_z_id;
	}
	
//frame_z_order
	public void setFrame_z_order(int frame_z_order)
	{
		this.frame_z_order = frame_z_order;
	}
	public int getFrame_z_order()
	{
		return this.frame_z_order;
	}
//create_date
	public void setCreate_date(String date)
	{
		this.create_date = date;
	}
	public String getCreate_date()
	{
		return this.create_date;
	}
	
//alter_date
	public void setAlter_date(String date)
	{
		this.alter_date = date;
	}
	public String getAlter_date()
	{
		return this.alter_date;
	}
	
//create_user
	public void setCreate_user(String create_user)
	{
		this.create_user = create_user;
	}
	public String getCreate_user()
	{
		return this.create_user;
	}
	
//alter_user
	public void setAlter_user(String alter_user)
	{
		this.alter_user = alter_user;
	}
	public String getAlter_user()
	{
		return this.alter_user;
	}
	
//preparatoty_id
	public void setPreparatoty_id(Long preparatoty_id)
	{
		this.preparatoty_id = preparatoty_id;
	}
	public Long getPreparatoty_id()
	{
		return this.preparatoty_id;
	}
//station_a_id
	public void setStation_a_id(Long station_a_id)
	{
		this.station_a_id = station_a_id;
	}
	public Long getStation_a_id()
	{
		return this.station_a_id;
	}

//station_z_id
	public void setStation_z_id(Long station_z_id)
	{
		this.station_z_id = station_z_id;
	}
	public Long getStation_z_id()
	{
		return this.station_z_id;
	}

//station_a_name
	public void setStation_a_name(String station_a_name)
	{
		this.station_a_name = station_a_name;
	}
	public String getStation_a_name()
	{
		return this.station_a_name;
	}

//station_z_name
	public void setStation_z_name(String station_z_name)
	{
		this.station_z_name = station_z_name;
	}
	public String getStation_z_name()
	{
		return this.station_z_name;
	}

//rtu_name
	public void setRtu_name(String rtu_name)
	{
		this.rtu_name = rtu_name;
	}
	public String getRtu_name()
	{
		return this.rtu_name;
	}
//is_obstacle
	public void setIsObstalce (boolean is_obstacle)
	{
		this.is_obstacle = is_obstacle;
	}
	public boolean getIsObstalce()
	{
		return this.is_obstacle;
	}
//priotity_id	
	public void setPriotityId (Long priotity_id)
	{
		this.priotity_id = priotity_id;
	}
	public Long getPriotityId()
	{
		return this.priotity_id;
	}
//	priotity_name
	public void setPriotityName(String priotity_name)
	{
		this.priotity_name = priotity_name;
	}
	public String getPriotityName()
	{
		return this.priotity_name;
	}
//is_protect
	public void setIsProtect (boolean is_protect)
	{
		this.is_protect = is_protect;
	}
	public boolean getIsProtect()
	{
		return this.is_protect;
	}
//is_uplink
	public void setIsUplink (boolean is_uplink)
	{
		this.is_uplink = is_uplink;
	}
	public boolean getIsUplink()
	{
		return this.is_uplink;
	}	
//switch_jumper_id
	public void setSwitchJumperId (Long switch_jumper_id)
	{
		this.switch_jumper_id = switch_jumper_id;
	}
	public Long getSwitchJumperId()
	{
		return this.switch_jumper_id;
	}
//	switch_rtu_id
	public void setSwitchRtuId (Long switch_rtu_id)
	{
		this.switch_rtu_id = switch_rtu_id;
	}
	public Long getSwitchRtuId()
	{
		return this.switch_rtu_id;
	}
//switch_rtu_order
	public void setSwitchRtuOrder(Integer switch_rtu_order)
	{
		this.switch_rtu_order = switch_rtu_order;
	}
	public Integer getSwitchRtuOrder()
	{
		return this.switch_rtu_order;
	}
//	backup_rtu_id
	public void setBackupRtuId (Long backup_rtu_id)
	{
		this.backup_rtu_id = backup_rtu_id;
	}
	public Long getBackupRtuId()
	{
		return this.backup_rtu_id;
	}
//backup_rtu_order
	public void setBackupRtuOrder(Integer backup_rtu_order)
	{
		this.backup_rtu_order = backup_rtu_order;
	}
	public Integer getBackupRtuOrder()
	{
		return this.backup_rtu_order;
	}
//backup_jumper_id	
	public void setBackupJumperId(Long backup_jumper_id)
	{
		this.backup_jumper_id = backup_jumper_id;
	}
	public Long getBackupJumperId()
	{
		return this.backup_jumper_id;
	}
	@Override
	public String toString() {
		return "{\"id\":" + "\"4_"+id +"\""+ ", \"pid\":" + "\"3_"+rtu_id+"\""
				+ ", \"name\":" +"\""+ route_name +"\""+ ", \"open\":"
				+ "\"false\"" +",\"hrefAddress\":"+"\""+"javascript:add_ajax('"+id+"','"+rtu_id+"')"+"\""+"}"; 
	}
}
