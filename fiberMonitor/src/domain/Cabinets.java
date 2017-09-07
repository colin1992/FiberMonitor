

/**机柜**/


package domain;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="CABINETS")
public class Cabinets
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true,updatable=false)
	private Long id;		//机柜标识
	
	@Column(name="NAME",nullable=false)
	private String cabinet_name;	//机柜名称
	
	@Column(name="RACK_NUMBER")
	private int rack_number;	//机架数量
	
	@Column(name="DESCRIPTION")
	private String description;	//描述
	
	@Column(name="CREATE_DATE",updatable=false)
	private String create_date;	//创建时间
	
	@Column(name="ALTER_DATE")
	private String alter_date;	//修改时间
	
	@Column(name="CREATE_USER",updatable=false)
	private String create_user;		//创建用户
	
	@Column(name="ALTER_USER")
	private String alter_user;		//修改用户
	
//foreign key
	//Stations   该机柜所属的局站
	@ManyToOne(targetEntity=Stations.class)
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
	
	//Racks   该机柜下的所有机架
	@OneToMany(targetEntity=Racks.class,mappedBy="cabinet")
	@Cascade({CascadeType.DELETE})
	private List<Racks> racks;
	public List<Racks> getRacks()
	{
		return this.racks;
	}
	
//Cabinet()
	public Cabinets(){}
	
//id
	public Long getId()
	{
		return this.id;
	}
	
//cabinet_name
	public void setCabinet_name(String name)
	{
		this.cabinet_name = name;
	}
	public String getCabinet_name()
	{
		return this.cabinet_name;
	}
	
//rack_number
	public void setRack_number(int number)
	{
		this.rack_number = number;
	}
	public int getRack_number()
	{
		return this.rack_number;
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
	
}
