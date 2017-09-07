

/*曲线回传信息2：事件信息*/


package domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="EVENT_CURVES")
public class Event_curves 
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true)
	private Long id;		//事件标识
	
	@Column(name="EVENT_TYPE")
	private String event_type;	//事件类型
	
	@Column(name="POTITION")
	private String potition;		//事件位置
	
	@Column(name="INSERTION_LOSS")
	private String insertion_loss;	//插入损耗
	
	@Column(name="RETURN_LOSS")
	private String return_loss;		//回波损耗
	
	@Column(name="ATTENUATION_COEFF")
	private String attenuation_coeff;	//衰减系数
	
	@Column(name="TOTAL_LOSS")			//累计损耗
	private String total_loss;
		
//foreign key
	//Curves     //获取该事件对应的曲线
	@ManyToOne(targetEntity=Curves.class)
	@JoinColumn(name="CURVE_ID",referencedColumnName="ID",nullable=false)
	private Curves curve;
	public void setCurves(Curves curve)				//设置该测试曲线的各个事件点
	{
		this.curve = curve;
	}
	public Curves getCurve()						//获取该事件点所对应的测试曲线
	{
		return this.curve;
	}
	
//Events
	public Event_curves(){}
	
//event_id
	public Long getId()
	{
		return this.id;
	}
	
//event_type
	public void setEvent_type(String type)
	{
		this.event_type = type;
	}
	public String getEvent_type()
	{
		return this.event_type;
	}
	
//postition
	public void setPotition(String potition)
	{
		this.potition = potition;
	}
	public String getPotition()
	{
		return this.potition;
	}
	
//insertion_loss
	public void setInsertion_loss(String insertion_loss)
	{
		this.insertion_loss = insertion_loss;
	}
	public String getInsertion_loss()
	{
		return this.insertion_loss;
	}
	
//return_loss
	public void setReturn_loss(String return_loss)
	{
		this.return_loss = return_loss;
	}
	public String getReturn_loss()
	{
		return this.return_loss;
	}
	
//attenuation_coeff
	public void setAttenuation_coeff(String attenuation_coeff)
	{
		this.attenuation_coeff = attenuation_coeff;
	}
	public String getAttenuation_coeff()
	{
		return this.attenuation_coeff;
	}
	
//total_loss
	public void setTotal_loss(String total_loss)
	{
		this.total_loss = total_loss;
	}
	public String getTotal_loss()
	{
		return this.total_loss;
	}

}
