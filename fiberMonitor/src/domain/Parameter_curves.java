

/*曲线回传信息1：测试条件信息*/


package domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="PARAMETER_CURVES")
public class Parameter_curves 
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true)
	private Long id;			//回传测试条件信息标识
	
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
	
	@Column(name="OPTIMIZE_PARA")		//是否启用了优化参数
	private boolean optimize_para;
	
	@Column(name="TIME_LENGTH")		//测试时长
	private String time_length;
	
	@Column(name="PATH_LENGTH")		//链路长度
	private String path_length;
	
	@Column(name="PATH_LOSS")		//链路损耗
	private String path_loss;
	
	@Column(name="PATH_ATTENUATION")	//链路衰减
	private String path_attenuation;
	
	/*-----------------new add-------------------*/
	@Column(name="SAMPLE_FRE",nullable=false)
	private int sample_fre;			//采样频率
	
	@Column(name="TEST_MODE")
	private String test_mode;			//测试模式
	//另外：将测试时长 time_length改为了String
	
	@Column(name="TEST_WAY")
	private String test_way;			//测试方式

	
	
//foreign key
	//Curves
	@OneToOne(targetEntity=Curves.class)
	@JoinColumn(name="CURVE_ID",referencedColumnName="ID")
	private Curves curve;
	public void setCurve(Curves curve)		//设置该测试参数对应的测试曲线
	{
		this.curve = curve;
	}
	public Curves getCurve()				//获取该测试参数对应的测试曲线
	{
		return this.curve;
	}

//Parameter_curves
	public Parameter_curves(){}
	
//parameter_curve_id
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
	
//optimize
	public void setOptimize(boolean optimize_para)
	{
		this.optimize_para = optimize_para;
	}
	public boolean getOptimize()
	{
		return this.optimize_para;
	}	
	
//time_length
	public void setTime_length(String string)
	{
		this.time_length = string;
	}
	public String getTime_length()
	{
		return this.time_length;
	}
	
//path_length
	public void setPath_length(String path_length)
	{
		this.path_length = path_length;
	}
	public String getPath_length()
	{
		return this.path_length;
	}
	
//path_loss
	public void setPath_loss(String path_loss)
	{
		this.path_loss = path_loss;
	}
	public String getPath_loss()
	{
		return this.path_loss;
	}
	
//path_attenuation
	public void setPath_attenuation(String path_attenuation)
	{
		this.path_attenuation = path_attenuation;
	}
	public String getPath_attenuation()
	{
		return this.path_attenuation;
	}	
//sample_fre
	public void setSample_fre(int sample_fre)
	{
		this.sample_fre = sample_fre;
	}
	public int getSample_fre()
	{
		return this.sample_fre;
	}	
//test_mode
	public void setTestMode(String test_mode)
	{
		this.test_mode = test_mode;
	}
	public String getTestMode()
	{
		return this.test_mode;
	}	
//test_way
	public void setTestWay(String test_way)
	{
		this.test_way=test_way;
	}
	public String getTestWay()
	{
		return this.test_way;
	}	
}
