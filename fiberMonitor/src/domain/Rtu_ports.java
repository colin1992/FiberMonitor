

/*RTU端口*/


package domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="RTU_PORTS")
public class Rtu_ports 
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID")
	private Long id;				//端口标识
	
	@Column(name="PORT_TYPE")	//端口类型:OTDR\...
	private String port_type;
	
	@Column(name="PORT_ORDER")
	private int port_order;			//端口序号
	
	@Column(name="STATUS",columnDefinition="boolean default false")	//端口状态，是否被占用,默认未被占用
	private boolean status;
	
	@Column(name="JUMPER_ROUTE_ID")
	private Long jumper_route_id;			//连接光路跳纤的标识
	
	@Column(name="MODULE_ORDER")
	private int module_order;			//该端口所属于的模块的序号
	
	
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
	//该rtu端口对应的rtu
	@ManyToOne(targetEntity=Rtus.class)
	@JoinColumn(name="RTU_ID",referencedColumnName="ID",nullable=false)
	private Rtus rtu;
	public void setRtu(Rtus rtu)//设置该端口的rtu的id
	{
		this.rtu = rtu;
	}
	public Rtus getRtu()//获取该端口的rtu的id
	{
		return this.rtu;
	}
	
	//Jumper_routes   获取该端口下的光路跳纤
//	@OneToOne(targetEntity=Jumper_routes.class,mappedBy="rtu_port")
//	@Cascade({CascadeType.DELETE})
//	private Jumper_routes jumper_route;
//	public Jumper_routes getJumper_route()
//	{
//		return this.jumper_route;
//	}
	
//	//Routes     获取该端口下的光路
//	@OneToOne(targetEntity=Routes.class,mappedBy="rtu_port")
//	@Cascade({CascadeType.DELETE})
//	private Routes route;
//	public Routes getRoute()
//	{
//		return this.route;
//	}
	
//Ports()
	public Rtu_ports(){}
	
//port_id
	public Long getId()
	{
		return this.id;
	}	
	
//port_type
	public void setPort_type(String type)
	{
		this.port_type = type;
	}
	public String getPort_type()
	{
		return this.port_type;
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
	
//moldule_order
	public void setModuleOrder(int module_order)
	{
		this.module_order = module_order;
	}
	public int getModuleOrder()
	{
		return this.module_order;
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
//jumper_route_id
	public void setJumper_route_id(Long jumper_route_id)
	{
		this.jumper_route_id = jumper_route_id;
	}
	public Long getJumper_route_id()
	{
		return this.jumper_route_id;
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
