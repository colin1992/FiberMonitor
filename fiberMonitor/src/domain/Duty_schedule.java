

/**值班表*/

package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="DUTY_SCHEDULE")
public class Duty_schedule {
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true,updatable=false)
	private Long id;    //值班标识
	
	@Column(name="DUTY_WEEK" , nullable=false)
	private int duty_week;
	
//foreign key
	//员工
	@ManyToMany(fetch=FetchType.EAGER,mappedBy="schedule")
	private List<Duty_operator> operators = new ArrayList<Duty_operator>();
	
	public List<Duty_operator> getOperators()
	{
		return this.operators;
	}
//ID
	public Long getId(){
		return this.id;
	}
//dutyWeek
	public void setDutyWeek(int dutyWeek){
		this.duty_week = dutyWeek;
	}
	public int getDutyWeek(){
		return this.duty_week;
	}
}
