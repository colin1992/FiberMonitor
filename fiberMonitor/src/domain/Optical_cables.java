/*光缆*/


package domain;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="OPTICAL_CABLES")
public class Optical_cables 
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true)
	private Long id;		//光缆标识
	
	@Column(name="NAME",nullable=false)
	private String name;		//光缆名
	
	@Column(name="LENGTH")
	private String length;			//光缆长度
	
	@Column(name="CORENUMBER")
	private int coreNumber;		//纤芯数量
	
	@Column(name="REFRACTIVE_INDEX")
	private String refractive_index;			//反射系数
	
	@Column(name="PORT_A_REMAIN",columnDefinition="VARCHAR(255) default 0.0")
	private String port_a_remain;	//A端盘留	
	
	@Column(name="PORT_Z_REMAIN",columnDefinition="VARCHAR(255) default 0.0")
	private String port_z_remain;	//Z端盘留
	
	@Column(name="CABLECORESTRUCTURE")
	private String cableCoreStructure;	//纤芯结构
	
	@Column(name="DESCRIPTION")
	private String description;		//描述
	
	@Column(name="STATION_A_Id")
	private Long station_a_id;			//a端局站id

	@Column(name="STATION_Z_Id")
	private Long station_z_id;			//z端局站id
	
	@Column(name="STATION_A_NAME")
	private String station_a_Name;			//a端局站name

	@Column(name="STATION_Z_NAME")
	private String station_z_Name;			//z端局站name
	
	@Column(name="CABLE_LEVEL")
	private String cable_level;		//网络层次
	
	@Column(name="LAID_WAY")
	private String laid_way;		//铺设方式
	    
    @Column(name="CREATE_USER")
   	private String create_user;		//创建用户
    
    @Column(name="ALTER_USER")
   	private String alter_user;		//修改用户
    
    @Column(name="ALTER_TIME")
   	private String alter_time;		//修改时间
    
    @Column(name="CREATE_TIME")
   	private String create_time;		//创建时间
	
    
    
    /*------------new add--------------*/
    @Column(name="CORE_STRCT")
	private String core_strct  ;		//纤芯结构
    
 
    /*---------------------------------*/
//foreign key
	//Landmarks   光缆地标
	@OneToMany(targetEntity=Landmarks.class,mappedBy="optical_cable")
	@Cascade({CascadeType.DELETE})
	private List<Landmarks> landmarks = new ArrayList<Landmarks>();			//关联表：地标
	public List<Landmarks> getLandmarks()
	{
		return this.landmarks;
	}
	
	//Fiber_cores 该光缆下的所有纤芯
	@OneToMany(targetEntity=Fiber_cores.class,mappedBy="optical_cable")
	@Cascade({CascadeType.DELETE})
	private List<Fiber_cores> fiber_cores = new ArrayList<Fiber_cores>();
	public List<Fiber_cores> getFiber_cores()
	{
		return this.fiber_cores;
	}
	
//Optical_cables()
	public Optical_cables(){}
	
//optical_cable_name
	public Long getId()
	{
		return this.id;
	}
//optical_cable_name
	public void setOptical_cable_name(String name)
	{
		this.name = name;
	}
	public String getOptical_cable_name()
	{
		return this.name;
	}
	
//length
	public void setLength(String length)
	{
		this.length = length;
	}
	public String getLength()
	{
		return this.length;
	}
	
//coreNumber
	public void setCoreNumber(int coreNumber)
	{
		this.coreNumber = coreNumber;
	}
	public int getCoreNumber()
	{
		return this.coreNumber;
	}
	
//laid_way
	public void setLaidWay(String LaidWay){
		this.laid_way=LaidWay;
	}
	
	
	public String getLaidWay(){
		 return this.laid_way;
	}
	
//cable_level
	public void setCableLevel(String CableLevel){
		this.cable_level=CableLevel;
	}
	
	 public String geCableLevel(){
		 return this.cable_level;
	}
	 
//create_user	 
	 public void setCreateUser(String cteate_user){
		 this.create_user=cteate_user;
	 }
	 public String getCreateUser(){
		 return this.create_user;
	 }
//alter_user
	 public void setAlterUser(String alter_user){
		 this.create_user=alter_user;
	 }
	 public String getAlterUser(){
		 return this.alter_user;
	 }	

//refractive_index
	public void setRefractive_index(String length)
	{
		this.refractive_index = length;
	}
	public String getRefractive_index()
	{
		return this.refractive_index;
	}	
	
//port_a_remain
	public void setPort_a_remain(String a_remain)
	{
		this.port_a_remain = a_remain;
	}
	public String getPort_a_remain()
	{
		return this.port_a_remain;
	}
	
//port_z_remain
	public void setPort_z_remain(String z_remain)
	{
		this.port_z_remain = z_remain;
	}
	public String getPort_z_remain()
	{
		return this.port_z_remain;
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
//station_a_name	
	public void setStation_a_name(String a)
	{
		this.station_a_Name = a;
	}
	public String getStation_a_name()
	{
		return this.station_a_Name;
	}
//station_z_name	
	public void setStation_z_name(String z)
	{
		this.station_z_Name= z;
	}
	public String getStation_z_name()
	{
		return this.station_z_Name;
	}
//station_a_id	
	public void setStation_a_id(Long a)
	{
		this.station_a_id = a;
	}
	public Long getStation_a_id()
	{
		return this.station_a_id;
	}
//station_z_id	
	public void setStation_z_id(Long z)
	{
		this.station_z_id = z;
	}
	public Long getStation_z_id()
	{
		return this.station_z_id;
	}

	
	/*----------------------new add--------------------*/
//Core_Strcu	
	public void setCore_strct(String core_strct)
		{
			this.core_strct = core_strct;
		}
	public String getCore_strct()
		{
			return this.core_strct;
		}
//alter_time
	 public void setAlterTime(String alter_time){
		 this.alter_time=alter_time;
		}
	 public String getAlterTime(){
		 return this.alter_time;
	 } 	
//create_time
	public void setCreateTime(String create_time){
		this.create_time=create_time;
	}
	 public String getCreateTime(){
		 return this.create_time;
   } 
	public String toString() {
		return "{\"id\":" + "\"3_"+id +"\""+ ", \"pid\":" + "\"2_"+station_a_id+"\""
				+ ", \"name\":" +"\""+ name +"\""+ ", \"open\":"
				+ "\"false\"" +"}"; 
	}
	/*------------------------------------------*/
}