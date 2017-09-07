

/*配线架跳纤*/


package domain;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="JUMPER_FRAMES")
public class Jumper_frames 
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true)
	private Long id;			//跳纤标识
	
	@Column(name="NAME")
	private String jumper_frame_name;		//跳纤名
	
	@Column(name="LENGTH")
	private String length;					//跳纤长度
	
	@Column(name="DESCRIPTION")
	private String description;				//描述
	
	@Column(name="FRAME_A_NAME")
	private String frame_a_name;			//a端配线架名称
	
	@Column(name="FRAME_A_ID",columnDefinition="long default null")
	private Long frame_a_id;			//a端配线架ID
	
	@Column(name="PORT_ORDER_A",columnDefinition="long default null")
	private int port_order_a;			//a端配线架端口序号
	
	@Column(name="FRAME_Z_NAME")
	private String frame_z_name;			//z端配线架名称

	@Column(name="FRAME_Z_ID",columnDefinition="long default null")
	private Long frame_z_id;			//z端配线架ID
	
	@Column(name="PORT_ORDER_Z",columnDefinition="long default null")
	private int port_order_z;			//z端配线架端口序号
	
	@Column(name="ROUTE_ID",columnDefinition="long default null")
	private Long route_id;					//挂载的光路id(如果没有则为空)
	
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
	
//Jumper_fibers()
	public Jumper_frames(){}
	
//jumper_fiber_id
	public Long getId()
	{
		return this.id;
	}
	
//jumper_route_name
	public void setJumper_frame_name(String name)
	{
		this.jumper_frame_name = name;
	}
	public String getJumper_frame_name()
	{
		return this.jumper_frame_name;
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
	
//frame_a_name
	public void setFrame_a_name(String frame_a_name)
	{
		this.frame_a_name = frame_a_name;
	}
	public String getFrame_a_name()
	{
		return this.frame_a_name;
	}
	
//frame_z_name
	public void setFrame_z_name(String frame_z_name)
	{
		this.frame_z_name = frame_z_name;
	}
	public String getFrame_z_name()
	{
		return this.frame_z_name;
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
	
//frame_z_id
	public void setFrame_z_id(Long frame_z_id)
	{
		this.frame_z_id = frame_z_id;
	}
	public Long getFrame_z_id()
	{
		return this.frame_z_id;
	}
	
//port_order_a
	public void setPort_order_a(int port_order_a)
	{
		this.port_order_a = port_order_a;
	}
	public int getPort_order_a()
	{
		return this.port_order_a;
	}
	
//port_order_z
	public void setPort_order_z(int port_order_z)
	{
		this.port_order_z = port_order_z;
	}
	public int getPort_order_z()
	{
		return this.port_order_z;
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
}
