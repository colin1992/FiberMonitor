

/**告警**/


package domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="ALARM")
public class Alarm
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true,updatable=false)
	private Long id;			//告警标识
	@Column(name="ALARM_TYPE")
	private String alarm_type;			//告警类别，光功率告警或OTDR
	
	@Column(name="ALARM_SOURCE")
	private String alarm_source;			//告警来源，周期测试、点名测试、障碍告警测试、光功率告警等
	
	@Column(name="ALARM_TIME")
	private String alarm_time;		//告警时间
	
	@Column(name="DISTANCE")
	private String distance;		//告警位置 与RTU距离
	
	@Column(name="ALARM_LEVEL")
	private String alarm_level;			//告警级别(针对光功率告警而言) 事件类型（针对OTDR而言）
	
	@Column(name="IS_HANDLE")
	private Boolean is_handle;			//是否处理
	
	@Column(name="HANDLE_TIME")
	private String handle_time;		//处理时间
	
	@Column(name="HANDLE_USER")
	private String handle_user;		//处理用户
	
	@Column(name="RTU_ID")
	private Long rtu_id;			//所属RTU
	//foreign key		
	@ManyToOne(targetEntity=Routes.class)
	@JoinColumn(name="ROUTE_ID",referencedColumnName="ID",nullable=false,updatable=false)
	private Routes route;
		public void setRoute(Routes route)				//光路
		{
			this.route = route;
		}
		public Routes getRoute()						
		{
			return this.route;
		}
//Alarm()
	public Alarm(){}
	
//id
	public Long getId()
	{
		return this.id;
	}
	
//alert_time
	public void setAlarm_time(String alert_time)
	{
		this.alarm_time = alert_time;
	}
	public String getAlarm_time()
	{
		return this.alarm_time;
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
	
//distance
	public void setDistance(String distance)
	{
		this.distance =distance;
	}
	public String getDistance()
	{
		return this.distance;
	}
	
//alert_level
	public void setAlarm_level(String alert_level)
	{
		this.alarm_level = alert_level;
	}
	public String getAlarm_level()
	{
		return this.alarm_level;
	}
//is_handle
	public void setIs_handle(boolean is_handle)
	{
		this.is_handle = is_handle;
	}
	public boolean getIs_handle()
	{
		return this.is_handle;
	}
//handle_time
	public void setHandle_time(String handle_time)
	{
		this.handle_time = handle_time;
	}
	public String getHandle_time()
	{
		return this.handle_time;
	}	
//handle_time
	public void setHandle_user(String handle_user)
	{
		this.handle_user = handle_user;
	}
	public String getHandle_user()
	{
		return this.handle_user;
	}	
//rtu_id
	public void setRtu_id(Long rtu_id)
	{
		this.rtu_id = rtu_id;
	}
	public Long getRtu_id()
	{
		return this.rtu_id;
	}		
}
