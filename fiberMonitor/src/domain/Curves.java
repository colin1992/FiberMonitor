

/*曲线回传信息3：测试数据*/


package domain;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="CURVES")
public class Curves
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true)
	private Long id;			//曲线标识
	
	@Column(name="CURVE_TYPE")
	private String curve_type;				//曲线来源：点名测试、周期测试、...
	
	@Column(name="REFERRING")
	private boolean referring;				//是否参考
	
	@Column(name="TEST_DATE",updatable=false)
	private String test_date;			//测定时间
	
	@Lob  
	@Column(name="CURVE_DATA",columnDefinition="MEDIUMBLOB",updatable=false)
	private byte[] curve_data;		//测试数据
	
	@Column(name="DESCRIPTION")
	private String description;			//描述
	
	@Column(name="ALERT_LEVEL")
	private String alert_level;	//告警级别
	
//foreign key
	//Routes		该曲线对应的光路			
	@ManyToOne(targetEntity=Routes.class)
	@JoinColumn(name="ROUTE_ID",referencedColumnName="ID",nullable=false,updatable=false)
	private Routes route;
	public void setRoute(Routes route)				//设置该曲线对应的某条光路
	{
		this.route = route;
	}
	public Routes getRoute()						//获取该曲线对应的光路
	{
		return this.route;
	}
	
	//Parameter_curves			//该曲线的测试参数
	@OneToOne(targetEntity=Parameter_curves.class,mappedBy="curve")
	@Cascade({CascadeType.DELETE})
	private Parameter_curves test_parameter;
	public Parameter_curves getTest_parameter()						//获取该曲线的测量参数
	{
		return this.test_parameter;
	}
	
	//Events					//该曲线下的多个事件
	@OneToMany(targetEntity=Event_curves.class,mappedBy="curve")
	@Cascade({CascadeType.DELETE})
	private List<Event_curves> events = new ArrayList<Event_curves>();
	public List<Event_curves> getEvents()						//获取该曲线下的所有事件
	{
		return this.events;
	}
	
//	//Curve_analysis_parameters      该曲线的分析参数
//	@OneToOne(targetEntity=Curve_analysis_parameters.class,mappedBy="curve")
//	private Curve_analysis_parameters curve_parameter;
//	public void setCurve_parameter(Curve_analysis_parameters parameter)	//设置该曲线的分析参数
//	{
//		this.curve_parameter = parameter;
//	}
//	public Curve_analysis_parameters getCurve_parameter()		//获取该曲线的分析参数
//	{
//		return this.curve_parameter;
//	}

	
//	//Results_test      该曲线的分析结果
//	@OneToOne(targetEntity=Result_tests.class,mappedBy="curve")
//	private Result_tests result_test;
//	public void setResults_test(Result_tests result_test)		//设置该曲线的测试结果
//	{
//		this.result_test = result_test;
//	}
//	public Result_tests getResult_test()		//获取该曲线的分析参数
//	{
//		return this.result_test;
//	}

//Curve_managers
	public Curves(){}
	
//curve_manger_id
	public Long getId()
	{
		return this.id;
	}
	
//route_id

//curve_type
	public void setCurve_type(String type)
	{
		this.curve_type = type;
	}
	public String getCurve_type()
	{
		return this.curve_type;
	}

//referring
	public void setReferring(boolean referring)
	{
		this.referring = referring;
	}
	public boolean getReferring()
	{
		return this.referring;
	}
	
//call_date
	public void setTest_date(String date)
	{
		this.test_date = date;
	}
	public String getTest_date()
	{
		return this.test_date;
	}
	
	
//curve_data
	public void setCurve_data(byte[] data)
	{
		this.curve_data = data;
	}
	public byte[] getCurve_data()
	{
		return this.curve_data;
	}
	
//description
	public void setDescripton(String description)
	{
		this.description = description;
	}
	public String getDescription()
	{
		return this.description;
	}
	
//alert_level
	public void setAlert_level(String alert_level)
	{
		this.alert_level = alert_level;
	}
	public String getAlert_level()
	{
		return this.alert_level;
	}
	
}
