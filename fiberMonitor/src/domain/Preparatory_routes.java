

/**预备光路**/


package domain;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="PREPARATORY_ROUTES")
public class Preparatory_routes 
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true,updatable=false)
	private Long id;				//预备光路标识
	
	@Column(name="STATION_A_ID")
	private Long station_a_id;		//起点局站标识(A)
	
	@Column(name="RTU_ID")
	private Long rtu_id;			//RTU标识
	
	@Column(name="RTU_PORT_ORDER")
	private int rtu_port_order;		//RTU端口序号
	
	@Column(name="FRAME_A_ID")
	private Long frame_a_id;			//配线架标识(A)
	
	@Column(name="FRAME_A_PORT_ORDER")
	private int frame_a_port_order;	//配线架端口序号
	
	@Column(name="FIBER_CORE_ID")
	private Long fiber_core_id;		//光纤标识
	
	@Column(name="STATION_Z_ID")
	private Long station_z_id;		//终点局站标识(Z)
	
	@Column(name="FRAME_Z_ID")
	private Long frame_z_id;			//配线架标识(Z)
	
	@Column(name="FRAME_Z_PORT_ORDER")
	private int frame_z_port_order;	//配线架端口序号
	
	@Column(name="STATUS",columnDefinition="boolean default false")
	private boolean status;		//预备光路状态：备选状态\激活状态，默认为false（备选状态）
	
//foreign key
	//Jumper_routes
	@OneToOne(targetEntity=Jumper_routes.class)
	@Cascade({CascadeType.DELETE})
	@JoinColumn(name="JUMPER_ROUTE_ID",referencedColumnName="ID")
	private Jumper_routes jumper_route;
	public void setJumper_route(Jumper_routes jumper_route)
	{
		this.jumper_route = jumper_route;
	}
	public Jumper_routes getJumper_route()
	{
		return this.jumper_route;
	}

//Preparatory_routes()
	public Preparatory_routes(){}
	
//id
	public Long getId()
	{
		return this.id;
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
	
//rtu_id
	public void setRtu_id(Long rtu_id)
	{
		this.rtu_id = rtu_id;
	}
	public Long getRtu_id()
	{
		return this.rtu_id;
	}
	
//rtu_port_order
	public void setRtu_port_order(int rtu_port_order)
	{
		this.rtu_port_order = rtu_port_order;
	}
	public int getRtu_port_order()
	{
		return this.rtu_port_order;
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

//frame_a_port_order
	public void setFrame_a_port_order(int frame_a_port_order)
	{
		this.frame_a_port_order = frame_a_port_order;
	}
	public int getFrame_a_port_order()
	{
		return this.frame_a_port_order;
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

//frame_z_port_order
	public void setFrame_z_port_order(int frame_z_port_order)
	{
		this.frame_z_port_order = frame_z_port_order;
	}
	public int getFrame_z_port_order()
	{
		return this.frame_z_port_order;
	}
	
//fiber_core_id
	public void setFiber_core_id(Long fiber_core_id)
	{
		this.fiber_core_id = fiber_core_id;
	}
	public Long getFiber_core_id()
	{
		return this.fiber_core_id;
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

}
