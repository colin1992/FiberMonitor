

/*局站*/


package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="STATIONS")
public class Stations 
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true,updatable=false)
	private Long id;			//局站标识
	
	@Column(name="NAME",nullable=false)
	private String station_name;		//局站名
	
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
	
	@Column(name="LONGITUDE")	
	private String longitude; 	//经度
	
	@Column(name="LATITUDE")	
	private String latitude; 	//纬度
	
	
	
//foreign key
	
	//Areas			为该局站配置区域属性
	@ManyToOne(targetEntity=Areas.class)
	@JoinColumn(name="AREA_ID",referencedColumnName="ID",nullable=true,updatable=false)
	private Areas area;					//关联表：区域
	public void setArea(Areas area)
	{
		this.area = area;
	}
	public Areas getArea()		//查询该局站所属的区域
	{
		return this.area;
	}

	//Distributing_frames 获取该局站下的配线架
	@OneToMany(targetEntity=Distributing_frames.class,mappedBy="station")
	@Cascade({CascadeType.DELETE})
	private List<Distributing_frames> distributing_frames = new ArrayList<Distributing_frames>();
	public List<Distributing_frames> getDistributing_frames()
	{
		return this.distributing_frames;
	}

	//Rtus
	@OneToMany(targetEntity=Rtus.class,mappedBy="station")
	@Cascade({CascadeType.DELETE})
	private List<Rtus> rtus = new ArrayList<Rtus>();				//关联表：RTU
	public List<Rtus> getRtus()
	{
		return this.rtus;
	}
	
	//Cabinet  该局站下的所有机柜
	@OneToMany(targetEntity=Cabinets.class,mappedBy="station")
	@Cascade({CascadeType.DELETE})
	private List<Cabinets> cabinets;
	public List<Cabinets> getCabinets()
	{
		return this.cabinets;
	}

	
//Stations()
	public Stations(){}
	
//station_id
	public Long getId()
	{
		return this.id;
	}
//station_name
	public void setStation_name(String name)
	{
		this.station_name = name;
	}
	public String getStation_name()
	{
		return this.station_name;
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
	
//longitude
	public void setLongitude(String longitude)
	{
		this.longitude = longitude;
	}
	public String getLongitude()
	{
		return this.longitude;
	}
	
//latitude
	public void setLatitude(String latitude)
	{
		this.latitude = latitude;
	}
	public String getLatitude()
	{
		return this.latitude;
	}
@Override
	public String toString() {
		   return "{\"id\":" + "\"2_"+id +"\""+ ", \"pid\":" + "\"1_"+area.getId().toString()+"\""
					+ ", \"name\":" +"\""+ station_name +"\""+ ", \"open\":"
					+ "\"false\"" +",\"hrefAddress\":"+"\""+"\""+"}" ;
		}

}
