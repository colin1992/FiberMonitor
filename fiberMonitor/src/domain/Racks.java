

/**机架**/


package domain;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="RACKS")
public class Racks 
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true,updatable=false)
	private Long id;		//机架标识
	
	@Column(name="RACK_ORDER",nullable=false,updatable=false)
	private int rack_order;	//机架序号
	
	@Column(name="THING_TYPE",nullable=true,columnDefinition="VARCHAR(255) default null")
	private String thing_type;	//机架放置物类型：RTU、配线架、光端机
	
	@Column(name="THING_NAME",nullable=true,columnDefinition="VARCHAR(255) default null")
	private String thing_name;	//机架放置物名称
	
	@Column(name="THING_ID",nullable=true,columnDefinition="BIGINT(20) default null")
	private Long thing_id;		//机架放置物ID
	
//foreign key
	//Cabinets				 机架所属机柜
	@ManyToOne(targetEntity=Cabinets.class)
	@JoinColumn(name="CABINET_ID",referencedColumnName="ID",nullable=false,updatable=false)
	private Cabinets cabinet;
	public void setCabinet(Cabinets cabinet)
	{
		this.cabinet = cabinet;
	}
	public Cabinets getCabinets()
	{
		return this.cabinet;
	}
	
	//RTUs		
	@OneToOne(targetEntity=Rtus.class,mappedBy="rack")
	@Cascade({CascadeType.DELETE})
	private Rtus rtus;
	
	//Distributing_frames
	@OneToOne(targetEntity=Distributing_frames.class,mappedBy="rack")
	@Cascade({CascadeType.DELETE})
	private Distributing_frames frame;
	
//Racks
	public Racks(){}
	
//id
	public Long getId()
	{
		return this.id;
	}
	
//rack_order
	public void setRack_order(int rack_order)
	{
		this.rack_order = rack_order;
	}
	public int getRack_order()
	{
		return this.rack_order;
	}
	
//thing_type
	public void setThing_type(String thing_type)
	{
		this.thing_type = thing_type;
	}
	public String getThing_type()
	{
		return this.thing_type;
	}
//thing_name
	public void setThingName(String thingName)
	{
		this.thing_name = thingName;
	}
	public String getThingName()
	{
		return this.thing_name;
	}	
//thing_id
	public void setThing_id(Long thing_id)
	{
		this.thing_id = thing_id;
	}
	public Long getThing_id()
	{
		return this.thing_id;
	}
}
