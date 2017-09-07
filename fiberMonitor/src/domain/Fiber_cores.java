/*纤芯*/
package domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="FIBER_CORES")
public class Fiber_cores 
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true)
	private Long id;		//纤芯标识
	
	@Column(name="CORE_ORDER")			//线芯序号
	private int core_order;
	
	@Column(name="STATUS",columnDefinition="boolean default false")
	private boolean status;				// 线芯状态

	@Column(name="STATION_A_ID")
	private Long station_a_id;			//a端局站ID
	
	@Column(name="FRAME_A_ID",columnDefinition="long default null")
	private Long frame_a_id;			//a端配线架ID
	
	@Column(name="PORT_ORDER_A",columnDefinition="long default null")
	private int port_order_a;			//a端配线架端口序号
	
	@Column(name="STATION_Z_ID")
	private Long station_z_id;			//z端局站ID
	
	@Column(name="FRAME_Z_ID",columnDefinition="long default null")
	private Long frame_z_id;			//z端配线架ID
	
	@Column(name="PORT_ORDER_Z",columnDefinition="long default null")
	private int port_order_z;			//z端配线架端口序号
	
	@Column(name="ROUTE_ID",columnDefinition="long default null")
	private Long route_id;					//挂载的光路id(如果没有则为空)
	
	/*-------------new add-----------------*/
	@Column(name="NAME",nullable=true)
	private String name;		//纤芯名称
	
	@Column(name="REFRACTIVE_INDEX")
	private String refractive_index;		//纤芯折射率
	
	@Column(name="LENGTH")
	private String length;		//纤芯长度
	
    @Column(name="FRAME_A_NAME")
	private String frame_a_name;		//a端配线架名称
	
	@Column(name="FRAME_Z_NAME")
	private String frame_z_name;		//z端配线架名称
	
	@Column(name="DESCRIPTION")
	private String description;		//
	
	/*----------------------------------------*/
//foreign key
	
	//Optical_cables 线芯所属光缆
	@ManyToOne(targetEntity=Optical_cables.class)
	@JoinColumn(name="OPTICAL_CABLE_ID",referencedColumnName="ID",nullable=false)
	private Optical_cables optical_cable;
	public void setOptival_cable(Optical_cables optical_cable)
	{
		this.optical_cable = optical_cable;
	}
	public Optical_cables getOptical_cable()
	{
		return this.optical_cable;
	}
	
//Fiber_cores()
	public Fiber_cores(){}
	
//fiber_core_id
	public Long getId()
	{
		return this.id;
	}
	
//core_order
	public void setCore_order(int order)
	{
		this.core_order = order;
	}
	public int getCore_order()
	{
		return this.core_order;
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
	/*-----------new add----------------*/
//name
	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return this.name;
	}
//refractive_index
	public void setRefractive_index(String Refractive_index){
		this.refractive_index=Refractive_index;
	}
	public String getRefractive_index(){
		return this.refractive_index;
	}
//length
	public void setLength(String length){
		this.length=length;
	}
	public String getLength(){
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
//frame_z_name
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
	/*--------------------------------------*/
}
