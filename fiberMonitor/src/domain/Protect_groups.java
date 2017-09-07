/**光路切换表**/
package domain;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="PROTECT_GROUPS")
public class Protect_groups 
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true)
	private Long id;	//光路切换表
	
	@Column(name="DOWNLINK_ON_ID",nullable=false)
	private Long downlink_on_id;	//下行链路在线光路Id
	
	@Column(name="DOWNLINK_ON_NAME",nullable=true)
	private String downlink_on_name;	//下行链路在线光路名称
	
	@Column(name="DOWNLINK_OFF_ID",nullable=false)
	private Long downlink_off_id;	//下行链路备纤光路Id
	
	@Column(name="DOWNLINK_OFF_NAME",nullable=true)
	private String downlink_off_name;	//下行链路备纤光路名称
	
	@Column(name="UPLINK_ON_ID",nullable=false)
	private Long uplink_on_id;	//上行链路在线光路Id
	
	@Column(name="UPLINK_ON_NAME",nullable=true)
	private String uplink_on_name;	//上行链路在线光路名称
	
	@Column(name="UPLINK_OFF_ID",nullable=false)
	private Long uplink_off_id;	//上行链路备纤光路Id
	
	@Column(name="UPLINK_OFF_NAME",nullable=true)
	private String uplink_off_name;	//上行链路备纤光路名称
	
	@Column(name="RTU_MASTER_ID",nullable=false)
	private Long rtu_master_id;	//保护-主模块所在RTU ID
	
	@Column(name="RTU_MASTER_NAME",nullable=true)
	private String rtu_master_name;	//保护-主模块所在RTU name
	
	@Column(name="RTU_SLAVE_ID",nullable=false)
	private Long rtu_slave_id;	//保护-从模块所在RTU ID
	
	@Column(name="RTU_SLAVE_NAME",nullable=true)
	private String rtu_slave_name;	//保护-从模块所在RTU name
	
	@Column(name="UP_SWITCH_POS")
	private String up_switch_pos;	//上行链路光开关状态
	
	@Column(name="DOWN_SWITCH_POS")
	private String down_switch_pos;	//下行链路光开关状态
	
	@Column(name="CREATE_DATE")
	private String create_date;			//创建日期
	
	@Column(name="CREATE_USER",updatable=false)
	private String create_user;		//创建用户
		
//Protect_groups()
	public Protect_groups(){}
	
//id
	public Long getId()
	{
		return this.id;
	}
	
//downlink_on_id
	public void setRouteDownOnId(Long downOnId)
	{
		this.downlink_on_id = downOnId;
	}
	public Long getRouteDownOnId()
	{
		return this.downlink_on_id;
	}
//downlink_on_name
	public void setRouteDownOnName(String downOnName)
	{
		this.downlink_on_name = downOnName;
	}
	public String getRouteDownOnName()
	{
		return this.downlink_on_name;
	}	
//downlink_off_id
	public void setRouteDownOffId(Long downOffId)
	{
		this.downlink_off_id = downOffId;
	}
	public Long getRouteDownOffId()
	{
		return this.downlink_off_id;
	}
//	downlink_off_name
	public void setRouteDownOffName(String name)
	{
		this.downlink_off_name = name;
	}
	public String getRouteDownOffName()
	{
		return this.downlink_off_name;
	}	
//uplink_on_id
	public void setRouteUpOnId(Long upOnId)
	{
		this.uplink_on_id = upOnId;
	}
	public Long getRouteUpOnId()
	{
		return this.uplink_on_id;
	}
//uplink_on_name
	public void setRouteUpOnName(String name)
	{
		this.uplink_on_name = name;
	}
	public String getRouteUpOnName()
	{
		return this.uplink_on_name;
	}			
//uplink_off_id
	public void setRouteUpOffId(Long upOffId)
	{
		this.uplink_off_id = upOffId;
	}
	public Long getRouteUpOffId()
	{
		return this.uplink_off_id;
	}
//	uplink_off_name
	public void setRouteUpOffName(String name)
	{
		this.uplink_off_name = name;
	}
	public String getRouteUpOffName()
	{
		return this.uplink_off_name;
	}	
//rtu_master_id
	public void setRtuMasterId(Long id)
	{
		this.rtu_master_id = id;
	}
	public Long getRtuMasterId()
	{
		return this.rtu_master_id;
	}
//rtu_master_name
	public void setRtuMasterName(String name)
	{
		this.rtu_master_name = name;
	}
	public String getRtuMasterName()
	{
		return this.rtu_master_name;
	}	
//	rtu_slave_id
	public void setRtuSlaveId(Long id)
	{
		this.rtu_slave_id = id;
	}
	public Long getRtuSlaveId()
	{
		return this.rtu_slave_id;
	}
//rtu_slave_name
	public void setRtuSlaveName(String name)
	{
		this.rtu_slave_name = name;
	}
	public String getRtuSlaveName()
	{
		return this.rtu_slave_name;
	}	
//up_switch_pos
	public void setUpSwitchPos(String pos)
	{
		this.up_switch_pos = pos;
	}
	public String getUpSwitchPos()
	{
		return this.up_switch_pos;
	}
//down_switch_pos
	public void setDownSwitchPos(String pos)
	{
		this.down_switch_pos = pos;
	}
	public String getDownSwitchPos()
	{
		return this.down_switch_pos;
	}
//create_date
	public void setCreateDate(String date)
	{
		this.create_date = date;
	}
	public String getCreateDate()
	{
		return this.create_date;
	}	
//create_user
	public void setCreateUser(String create_user)
	{
		this.create_user = create_user;
	}
	public String getCreateUser()
	{
		return this.create_user;
	}

}
