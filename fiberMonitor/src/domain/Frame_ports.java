

/**配线架端口**/


package domain;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="FRAME_PORTS")
public class Frame_ports 
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID")
	private Long id;				//端口标识
	
	@Column(name="PORT_ORDER")
	private int port_order;			//端口序号
	
	@Column(name="STATUS",columnDefinition="boolean default false")	//端口状态，是否被占用,默认未被占用
	private boolean status;
	
	@Column(name="CONNECTION_TYPE")
	private String connection_type;		//该端口连接的物体类型:光路跳纤\配线架跳纤
	
	@Column(name="CONNECTION_ID")
	private Long connection_id;			//连接物的标识
	
	@Column(name="HAS_FIBER",columnDefinition="boolean default false")
	private boolean has_fiber;			//是否连接光纤
	
	
	/*--------------------new add----------*/
	@Column(name="PORT_NAME")
	private String name;			//端口名
	
	@Column(name="DESCRIPTION")
	private String description;		//
	
	@Column(name="ALTER_TIME")
	private String alter_time;		//alter_time
	
	@Column(name="ALTER_USER")
	private String alter_user;		//alter_time
	
	/*---------------------------------------*/
	
//foreign key
	
	//Distributing_frames         //获取该端口对应的配线架
	@ManyToOne(targetEntity=Distributing_frames.class)
	@JoinColumn(name="FRAME_ID",referencedColumnName="ID",nullable=false)
	private Distributing_frames frame;
	public void setFrame(Distributing_frames frame)
	{
		this.frame = frame;
	}
	public Distributing_frames getFrame()
	{
		return this.frame;
	}
	
//Frame_ports()
	public Frame_ports(){}
	
//port_id
	public Long getId()
	{
		return this.id;
	}	
	
//port_order
	public void setPort_order(int port_order)
	{
		this.port_order = port_order;
	}
	public int getPort_order()
	{
		return this.port_order;
	}
	
//status
	public void setStatus(boolean status)
	{
		this.status = status;
	}
	public boolean getStatus()
	{
		return this.status;
	}
	
//connection_type
	public void setConnection_type(String connection_type)
	{
		this.connection_type = connection_type;
	}
	public String getConnection_type()
	{
		return this.connection_type;
	}
	
//connection_id
	public void setConnection_id(Long connection_id)
	{
		this.connection_id = connection_id;
	}
	public Long getConnection_id()
	{
		return this.connection_id;
	}
	
//has_fiber
	public void setHas_fiber(boolean has_fiber)
	{
		this.has_fiber = has_fiber;
	}
	public boolean getHas_fiber()
	{
		return this.has_fiber;
	}
	
	/*----------------new add-----------------*/
//port_name
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
//description
	public String getDescription()
	{
		return this.description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	
//alter_time	
	public String getAlterTime()
	{
		return this.alter_time;
	}
	public void setAlterTime(String alter_time)
	{
		this.alter_time = alter_time;
	}
//alter_user
	public String getAlterUser()
	{
		return this.alter_user;
	}
	public void setAlterUser(String alter_user)
	{
		this.alter_user = alter_user;
	}		
	
	/*----------------------------------------*/
}
