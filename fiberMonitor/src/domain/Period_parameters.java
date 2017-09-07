

/**周期测试参数**/


package domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="PERIOD_PARAMETERS")
public class Period_parameters 
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true)
	private Long id;		//周期测试参数
	
	@Column(name="START_TIME")
	private String start_time;		//开始时间
	
	@Column(name="INTERVAL_TIME")
	private String interval_time;	//时间间隔
	
	@Column(name="RETURN_IP_1")
	private String return_ip_1;	//回传IP1
	
	@Column(name="RETURN_IP_2")
	private String return_ip_2; //回传IP2
	
//foreign key
	//Routes				
	@OneToOne(targetEntity=Routes.class)
	@JoinColumn(name="ROUTE_ID",referencedColumnName="ID",nullable=false,updatable=false)
	private Routes route;
	public void setRoute(Routes route)				//为某光路添加周期测试参数
	{
		this.route = route;
	}
	public Routes getRoute()						//获取该测试参数对应的光路
	{
		return this.route;
	}
	
//Period_parameters
	public Period_parameters(){}
	
//id
	public Long getId()
	{
		return this.id;
	}
	
//start_time
	public void setStart_time(String start_time)
	{
		this.start_time = start_time;
	}
	public String getStart_time()
	{
		return this.start_time;
	}
	
//interval_time
	public void setInterval_time(String interval_time)
	{
		this.interval_time = interval_time;
	}
	public String getInterval_time()
	{
		return this.interval_time;
	}
	
//return_ip_1
	public void setReturn_ip_1(String return_ip_1)
	{
		this.return_ip_1 = return_ip_1;
	}
	public String getReturn_ip_1()
	{
		return this.return_ip_1;
	}
	
//return_ip_2
	public void setReturn_ip_2(String return_ip_2)
	{
		this.return_ip_2 = return_ip_2;
	}
	public String getReturn_ip_2()
	{
		return this.return_ip_2;
	}
}
