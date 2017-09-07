

/*地标*/


package domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="LANDMARKS")
public class Landmarks 
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true)
	private Long id;		//地标标识
	
	@Column(name="NAME",nullable=false)
	private String landmark_name;		//地标名
	
	@Column(name="LONGITUDE")	
	private String longitude; 	//经度
	
	@Column(name="LATITUDE")	
	private String latitude; 	//纬度

	@Column(name="TYPE")	
	private String type; 	//地标类型  变电站，电力杆
	
	@Column(name="DESCRIPTION")
	private String description;			//描述
	
/*----------new add------------*/	
	@Column(name="CREATE_TIME")
	private String create_time;			//create_time
	
	@Column(name="CREATE_USER")
	private String create_user;			//create_user
	
	@Column(name="ALTER_TIME")
	private String alter_time;			//alter_time
	
	@Column(name="ALTER_USER")
	private String alter_user;			//alter_user
/*--------------------------*/
	
//foreign key
	//Optical_cables		所属光缆
	@ManyToOne(targetEntity=Optical_cables.class)
	@JoinColumn(name="CABLE_ID",referencedColumnName="ID")
	private Optical_cables optical_cable;
	public void setOptical_cable(Optical_cables optical_cable)
	{
		this.optical_cable = optical_cable;
	}
	public Optical_cables getOptical_cable()
	{
		return this.optical_cable;
	}
	
//Landmarks()
	public Landmarks(){
	}
	
//landmark_id
	public Long getId()
	{
		return this.id;
	}
//landmark_name
	public void setName(String name)
	{
		this.landmark_name = name;
	}
	public String getName()
	{
		return this.landmark_name;
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
//description
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getDescription()
	{
		return this.description;
	}
/*--------------------new add------------------*/	
	//create_user
		public void setCreateUser(String create_user)
		{
			this.create_user = create_user;
		}
		public String getCreateUser()
		{
			return this.create_user;
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
			this.alter_time = alter_time;
		}
		public String getAlterTime()
		{
			return this.alter_time;
		}	
	//alter_user
		public void setAlterUser(String alter_user)
		{
			this.alter_user = alter_user;
		}
		public String getAlterUser()
		{
			return this.alter_user;
		}
	//type
		public void setType(String type){
			this.type=type;
		}
		public String getType(){
			return this.type;
		}
}
