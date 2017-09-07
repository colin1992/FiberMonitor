package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**告警经验库**/
@Entity
@Table(name="ALARM_BANK")
public class AlarmBank {
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true,updatable=false)
	private Long id;			//告警标识
	@Column(name="ALARM_TYPE")
	private String alarm_type;			//告警类别，光功率告警或OTDR
	
	@Column(name="ALARM_SOURCE")
	private String alarm_source;			//告警来源，周期测试、点名测试、障碍告警测试、光功率告警等
	
	@Column(name="ALARM_REASON")
	private String alarm_reason;			//告警原因
	
	@Column(name="HANDLE_EXPERIENCE")
	private String handle_experience;			//处理经验
	
	@Column(name="CREATE_TIME")
	private String create_time;			//创建时间
	
	@Column(name="CREATE_USER")
	private String create_user;			//创建用户
	
	@Column(name="ALTER_TIME")
	private String alter_time;			//修改时间
	
	@Column(name="ALTER_USER")
	private String alter_user;			//修改用户
	
//id
	public Long getId()
	{
		return this.id;
	}
//alert_type
	public void setAlarm_type(String alert_type)
	{
		this.alarm_type = alert_type;
	}
	public String getAlarm_type()
    {
		return this.alarm_type;
	}
		
//alert_source
	public void setAlarm_source(String alarm_source)
	{
		this.alarm_source = alarm_source;
	}
	public String getAlarm_source()
	{
		return this.alarm_source;
	}
//alert_type
	public void setAlarmReason(String alarm_reason)
	{
		this.alarm_reason = alarm_reason;
	}
	public String getAlarmReason()
    {
		return this.alarm_reason;
	}	
//handle_experience
	public void setHandleExper(String handle_experience)
	{
		this.handle_experience = handle_experience;
	}
	public String getHandleExper()
	{
		return this.handle_experience;
	}	
	//create_time
	public void setCreateTime(String createTime)
	{
		this.create_time = createTime;
	}
	public String getCreateTime()
	{
		return this.create_time;
	}	
//create_user
	public void setCreateUser(String createUser)
	{
		this.create_user = createUser;
	}
	public String getCreateUser()
	{
		return this.create_user;
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
		
}
