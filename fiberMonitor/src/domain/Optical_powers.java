package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/*--------光路的光功率数据---------*/
@Entity
@Table(name="OPTICAL_POWERS")
public class Optical_powers {
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true)
	private Long id;			//序号
	
	@Column(name="TEST_TIME")
	private String test_time;				//测试时间
	
	@Column(name="POWER_VALUE")
	private String power_value;				//光功率值
	
	@Column(name="ROUTE_ID")
	private Long route_id;				//所属曲线ID
	
	//id
	public Long getId(){
		return this.id;
	}
	
	//test_time
	public void setTestTime(String test_time){
		this.test_time=test_time;
	}
	public String getTestTime(){
		return this.test_time;
	}
	
	//curve_id
	public void setRouteId(Long routeId){
		this.route_id=routeId;
	}
	public Long getRouteId(){
		return this.route_id;
	}
	
	//power_value
	public void setPowerValue(String power_value){
		this.power_value=power_value;
	}
	public String getPowerValue(){
		return this.power_value;
	}
	  
}
