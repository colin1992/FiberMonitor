package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name="JUMPER_BACKUPS")
/**备纤光源跳纤    连接配线架和备纤光源RTU的跳纤**/
public class Jumper_backups {


	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true)
	private Long id;			//跳纤标识
	
	@Column(name="NAME")
	private String name;		//跳纤名
	
	@Column(name="LENGTH")
	private String length;					//跳纤长度
	
	@Column(name="DESCRIPTION")
	private String description;				//描述
	
	@Column(name="RTU_NAME")		//Rtu名字
	private String rtu_name;
	
	@Column(name="RTU_ID",columnDefinition="long default null")
	private Long rtu_id;			//RTU-ID
	
	@Column(name="IN_PORT_ORDER",columnDefinition="long default null")
	private int in_port_order;			//RTU-IN端口序号
	
	@Column(name="FRAME_NAME")		//配线架名字
	private String frame_name;
	
	@Column(name="FRAME_ID",columnDefinition="long default null")
	private Long frame_id;			//配线架ID
	
	@Column(name="FRAME_PORT_ORDER",columnDefinition="long default null")
	private int frame_port_order;			//配线架端口序号
	
	@Column(name="ROUTE_ID",columnDefinition="long default null")
	private Long route_id;					//挂载的光路id(如果没有则为空)
	
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
	
//jumper_fiber_id
	public Long getId()
	{
		return this.id;
	}
	
//jumper_route_name
	public void setJumperName(String name)
	{
		this.name = name;
	}
	public String getJumperName()
	{
		return this.name;
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
	

//in_port_order
	public void setIn_port_order(int portOrder)
	{
		this.in_port_order = portOrder;
	}
	public int getIn_port_order()
	{
		return this.in_port_order;
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
