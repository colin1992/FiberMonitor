

/*配线架*/


package domain;

import java.util.List;

import javax.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="DISTRIBUTING_FRAMES")
public class Distributing_frames
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true)
	private Long id;		//配线架标识
	
	@Column(name="NAME",nullable=false)
	private String frame_name;		//配线架名	
	
	@Column(name="PORT_NUMBER",columnDefinition="Integer default 0")
	private int port_number;		//配线架端口数
	
	@Column(name="DESCRIPTION")
	private String description;		//描述
	
	@Column(name="CREATE_DATE",updatable=false)
	private String create_date;			//创建日期
	
	@Column(name="ALTER_DATE")			
	private String alter_date;			//修改日期
	
	@Column(name="CREATE_USER",updatable=false)
	private String create_user;		//创建用户
	
	@Column(name="ALTER_USER")
	private String alter_user;		//修改用户
	
//foreign key
	//Frame_ports		配线架端口
	@OneToMany(targetEntity=Frame_ports.class,mappedBy="frame")
	@Cascade({CascadeType.DELETE})
	private List<Frame_ports> ports;
	public List<Frame_ports> getPots()
	{
		return this.ports;
	}
	
	//Stations			配线架所属局站
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
	
	//Racks    配线架所在的机架
	@OneToOne(targetEntity=Racks.class)
	@JoinColumn(name="RACK_ID",referencedColumnName="ID",nullable=true)
	private Racks rack;
	public void setRack(Racks rack)
	{
		this.rack = rack;
	}
	public Racks getRack()
	{
		return this.rack;
	}
	
//Distributing_frames()
	public Distributing_frames(){}
	
//frame_id
	public Long getId()
	{
		return this.id;
	}
	
//frame_name
	public void setFrame_name(String name)
	{
		this.frame_name = name;
	}
	public String getFrame_name()
	{
		return this.frame_name;
	}
	
//port_number
	public void setPort_number(int number)
	{
		this.port_number = number;
	}
	public int getPort_number()
	{
		return this.port_number;
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
@Override
	public String toString() {
		return "{\"id\":" + "\"30_"+id +"\""+ ", \"pid\":" + "\"2_"+station.getId()+"\""
				+ ", \"name\":" +"\""+ frame_name +"\""+ ", \"open\":"
				+ "\"false\"" +",\"hrefAddress\":"+"\""+"javascript:add_ajax('"+id+"','"+station.getId()+"')"+"\""+"}"; 
	}	
}
