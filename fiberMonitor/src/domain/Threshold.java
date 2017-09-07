package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="THRESHOLD")
/**
 * 光功率门限表
 * 暂时设为四级门限
 * **/
public class Threshold {
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true,updatable=false)
	private Long id;			//门限标识
	
	@Column(name="THRE1")
	private String thre1;			//门限1
	
	@Column(name="THRE2")
	private String thre2;			//门限2
	
	@Column(name="THRE3")
	private String thre3;			//门限3
	
	@Column(name="THRE4")
	private String thre4;			//门限4
	
	@Column(name="SWITCH_THRE")
	private String switch_thre;	   //切换门限
	
	//foreign key 光路和门限一一对应，一条光路只能有一组门限			
	@OneToOne(targetEntity=Routes.class)
	@JoinColumn(name="ROUTE_ID",referencedColumnName="ID",nullable=false,updatable=false)
	private Routes route;
	
	//Route
	public void setRoute(Routes route)				
	{
		this.route = route;
	}
	public Routes getRoute()						
	{
		return this.route;
	}
//id
	public Long getId()
	{
		return this.id;
	}
//thre1
	public void setThre1(String thre)
	{
		 this.thre1=thre;
	}
	public String getThre1()
	{
		return this.thre1;
	}
//thre2
	public void setThre2(String thre)
	{
		 this.thre2=thre;
	}
	public String getThre2()
	{
		return this.thre2;
	}
//	thre3
	public void setThre3(String thre)
	{
		 this.thre3=thre;
	}
	public String getThre3()
	{
		return this.thre3;
	}
//thre4
	public void setThre4(String thre)
	{
		 this.thre4=thre;
	}
	public String getThre4()
	{
		return this.thre4;
	}
//switch_thre
	public void setSwitchThre(String switch_thre)
	{
			this.switch_thre=switch_thre;
	}
	public String getSwitchThre()
	{
		return this.switch_thre;
	}	
}
