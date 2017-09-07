

/*RTU*/


package domain;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="RTUS")
public class Rtus 
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true)
	private Long id;		//RTU标识
	
	@Column(name="NAME",nullable=false)
	private String rtu_name;		//RTU名
	
	@Column(name="RTU_KEY",nullable=false)
	private String rtu_key;				//rtu激活码
	
	@Column(name="RTU_URL",nullable=false)
	private String rtu_url;				//rtu地址url
	
	@Column(name="INSTALL_INFO")
	private String install_info;			//rtu的安装信息
	
	@Column(name="TYPE")
	private String type;			//普通RTU 或者 切换RTU
	
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
	
	@Column(name="ALARM_WAYID")
	private Long alarm_wayId;		//告警方式ID
	
	@Column(name="DIVID_NUM",columnDefinition="int default 0")
	private int divid_num;		//分光路数，切换RTU和备纤光源RTU使用  激光器的分光路数 1-2  1-4   1-8 1-32 1-64
	
	@Column(name="PORT_NUM",columnDefinition="int default 0")
	private int port_num;		//记录 备纤光源RTU的光端口数
	
	@Column(name="PVAL")
	private String pVal;		//记录 备纤光源RTU的发射端光功率值
//foreign key
	//RTU下的端口
	@OneToMany(targetEntity=Rtu_ports.class,mappedBy="rtu")
	@Cascade({CascadeType.ALL})
	private List<Rtu_ports> rtu_ports;
	public List<Rtu_ports> getRtu_ports()
	{
		return this.rtu_ports;
	}	
	
	//Stations			RTU所属局站
	@ManyToOne(targetEntity=Stations.class)
	@JoinColumn(name="STATION_ID",referencedColumnName="ID",nullable=false)
	private Stations station;					
	public void setStation(Stations station)
	{
		this.station = station;
	}
	public Stations getStation()
	{
		return this.station;
	}
	
	//Racks    RTU所在的机架
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
	
//Rtus()
	public Rtus(){}
	
//rtu_id
	public Long getId()
	{
		return this.id;
	}
	
//rtu_name
	public void setRtu_name(String name)
	{
		this.rtu_name = name;
	}
	public String getRtu_name()
	{
		return this.rtu_name;
	}	
	
//key
	public void setRtu_key(String rtu_key)
	{
		this.rtu_key = rtu_key;
	}
	public String getRtu_key()
	{
		return this.rtu_key;
	}	
	
//url
	public void setRtu_url(String url)
	{
		this.rtu_url = url;
	}
	public String getRtu_url()
	{
		return this.rtu_url;
	}		

//install_info
	public void setInstallInfo(String install_info)
	{
		this.install_info = install_info;
	}
	public String getInstallInfo()
	{
		return this.install_info;
	}

//type
	public void setType(String type)
	{
		this.type = type;
	}
	public String getType()
	{
		return this.type;
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
//alarm_wayId
	public void setAlarm_wayId(Long alarm_wayId){
		this.alarm_wayId=alarm_wayId;
	}
	public Long getAlarm_wayId(){
		return this.alarm_wayId;
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
//divid_num
	public void setDividNum(int dividNum)
	{
		this.divid_num = dividNum;
	}
	public int getDividNum()
	{
		return this.divid_num;
	}
//	port_num
	public void setPortNum(int port_num)
	{
		this.port_num = port_num;
	}
	public int getPortNum()
	{
		return this.port_num;
	}
//	pVal
	public void setPVal(String pVal)
	{
		this.pVal = pVal;
	}
	public String getPVal()
	{
		return this.pVal;
	}	
	@Override
	public String toString() {
		return "{\"id\":" + "\"3_"+id +"\""+ ", \"pid\":" + "\"2_"+station.getId()+"\""
				+ ", \"name\":" +"\""+ rtu_name +"\""+ ", \"open\":"
				+ "\"false\"" +",\"hrefAddress\":"+"\""+"javascript:add_ajax('"+id+"','"+station.getId()+"')"+"\""+"}"; 
	}	
}
