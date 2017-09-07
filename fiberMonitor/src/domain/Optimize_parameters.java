

/*优化参数*/


package domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="OPTIMIZE_PARAMETERS")
public class Optimize_parameters 
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true)
	private Long id;		//优化参数标识
	
	@Column(name="MAX_RANGE",nullable=false)
	private int max_range;			//量程
	
	@Column(name="PULSE_WIDTH")
	private int pulse_width;		//脉冲宽度
	
	@Column(name="WAVE_LENGTH")
	private int wave_length;				//波长
	
	@Column(name="AVERAGE_TIMES")
	private int average_times;			//平均次数
	
	@Column(name="REFRACTIVE_INDEX")
	private String refractive_index;		//折射率
	
	@Column(name="END_THRESHOLD")		//结束门限
	private String end_threshold;     
	
	@Column(name="NON_REFRACTIVE_THRESHOLD")	//非反射门限
	private String non_refractive_threshold;
	
//foreign key
	//Routes				
	@OneToOne(targetEntity=Routes.class)
	@JoinColumn(name="ROUTE_ID",referencedColumnName="ID",nullable=false,updatable=false)
	private Routes route;
	public void setRoute(Routes route)				//为某光路添加优化测试参数
	{
		this.route = route;
	}
	public Routes getRoute()						//获取该测试参数对应的光路
	{
		return this.route;
	}
	
	
//Alert_test_parameters
	public Optimize_parameters(){}
	
//alert_parameters_id
	public Long getId()
	{
		return this.id;
	}
	
//range
	public void setMax_range(int range)
	{
		this.max_range = range;
	}
	public int getMax_range()
	{
		return this.max_range;
	}
	
//pulse_width
	public void setPulse_width(int pulse_width)
	{
		this.pulse_width = pulse_width;
	}
	public int getPulse_width()
	{
		return this.pulse_width;
	}	
	
//wave_length
	public void setWave_length(int wave_length)
	{
		this.wave_length = wave_length;
	}
	public int getWave_length()
	{
		return this.wave_length;
	}	
	
//average_times
	public void setAverage_times(int average_times)
	{
		this.average_times = average_times;
	}
	public int getAverage_times()
	{
		return this.average_times;
	}
	
//refractive_index
	public void setRefractive_index(String refractive_index)
	{
		this.refractive_index = refractive_index;
	}
	public String getRefractive_index()
	{
		return this.refractive_index;
	}	
	
//end_threshold
	public void setEnd_threshold(String end_threshold)
	{
		this.end_threshold = end_threshold;
	}
	public String getEnd_threshold()
	{
		return this.end_threshold;
	}	
	
//non_refractive_threshold
	public void setNon_refractive_threshold(String non_refractive_threshold)
	{
		this.non_refractive_threshold = non_refractive_threshold;
	}
	public String getNon_refractive_threshold()
	{
		return this.non_refractive_threshold;
	}	
}
