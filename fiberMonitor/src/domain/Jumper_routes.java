

/*光路跳纤*/


package domain;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="JUMPER_ROUTES")
public class Jumper_routes 
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true)
	private Long id;			//跳纤标识
	
	@Column(name="NAME")
	private String jumper_route_name;		//跳纤名
	
	@Column(name="LENGTH")
	private String length;					//跳纤长度
	
	@Column(name="DESCRIPTION")
	private String description;				//描述
	
	@Column(name="RTU_NAME")		//Rtu名字
	private String rtu_name;
	
	@Column(name="RTU_ID",columnDefinition="long default null")
	private Long rtu_id;			//RTU-ID
	
	@Column(name="MODEL_ORDER",columnDefinition="long default null")
	private int model_order;			//RTU-OTDR端口所在模块序号
	
	@Column(name="OTDR_PORT_ORDER",columnDefinition="long default null")
	private int otdr_port_order;			//RTU-OTDR端口序号
	
	@Column(name="FRAME_NAME")		//配线架名字
	private String frame_name;
	
	@Column(name="FRAME_ID",columnDefinition="long default null")
	private Long frame_id;			//配线架ID
	
	@Column(name="FRAME_PORT_ORDER",columnDefinition="long default null")
	private int frame_port_order;			//配线架端口序号
	
	@Column(name="ROUTE_ID",columnDefinition="long default null")
	private Long route_id;					//挂载的光路id(如果没有则为空)
	
	@Column(name="IS_SWITCH",columnDefinition="boolean default false")		//是否为切换跳纤
	private boolean is_switch;
/*-----------newly add-------------------*/
	
	@Column(name="CREATE_TIME")		//create_time
	private String create_time;
	
	@Column(name="ALTER_TIME")		//alter_time
	private String alter_time;
	
	/*------------------------------------------*/
	
//foreign key
	
	//Stations			指定跳纤所在的局站 
	@ManyToOne(targetEntity=Stations.class)
	@Cascade({CascadeType.REFRESH})
	@JoinColumn(name="STATION_ID",referencedColumnName="ID")
	private Stations station;
	public void setStation(Stations station)
	{
		this.station = station;
	}
	public Stations getStation()
	{
		return this.station;
	}
	
	//Preparatory_routes   //该光路跳纤对应的预备光路
	@OneToOne(targetEntity=Preparatory_routes.class,mappedBy="jumper_route")
	@Cascade({CascadeType.DELETE})
	private Preparatory_routes preparatory_route;
	public Preparatory_routes getPreparatory_route()
	{
		return this.preparatory_route;
	}
		
//Jumper_fibers()
	public Jumper_routes(){}
	
//jumper_fiber_id
	public Long getId()
	{
		return this.id;
	}
	
//jumper_route_name
	public void setJumper_route_name(String name)
	{
		this.jumper_route_name = name;
	}
	public String getJumper_route_name()
	{
		return this.jumper_route_name;
	}	
//length
	public void setLength(String len)
	{
		this.length = len;
	}
	public String getLength()
	{
		return this.length;
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
	
//rtu_name
	public void setRtu_name(String rtu_name)
	{
		this.rtu_name = rtu_name;
	}
	public String getRtu_name()
	{
		return this.rtu_name;
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
	
//frame_name
	public void setFrame_name(String frame_name)
	{
		this.frame_name = frame_name;
	}
	public String getFrame_name()
	{
		return this.frame_name;
	}
	
//frame_id
	public void setFrame_id(Long frame_id)
	{
		this.frame_id = frame_id;
	}
	public Long getFrame_id()
	{
		return this.frame_id;
	}
	
//model_order
	public void setModelOrder(int modelOrder)
	{
		this.model_order = modelOrder;
	}
	public int getModelOrder()
	{
		return this.model_order;
	}
//otdr_port_order
	public void setOtdr_port_order(int otdr_port_order)
	{
		this.otdr_port_order = otdr_port_order;
	}
	public int getOtdr_port_order()
	{
		return this.otdr_port_order;
	}
	
//frame_port_order
	public void setFrame_port_order(int frame_port_order)
	{
		this.frame_port_order = frame_port_order;
	}
	public int getFrame_port_order()
	{
		return this.frame_port_order;
	}
	
//route_id
	public void setRoute_id(Long route_id)
	{
		this.route_id = route_id;
	}
	public Long getRoute_id()
	{
		return this.route_id;
	}
	
	/*-------------new add-------------------*/
//create_time
	public void setCreateTime(String create_time)
	{
		this.create_time = create_time;
	}
	public String getCreateTime()
	{
		return this.create_time;
	}
//alter_time
	public void setAlterTime(String alter_time)
	{
		this.create_time = alter_time;
	}
	public String getAlterTime()
	{
		return this.alter_time;
	}
//is_switch
	public boolean getIsSwitch()
	{
		return this.is_switch;
	}
	public void setIsSwitch(boolean is_switch)
	{
		 this.is_switch=is_switch;
	}
}
