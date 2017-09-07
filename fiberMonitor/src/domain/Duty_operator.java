
/*
 * 值班人员
 * */

package domain;

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
@Table(name = "DUTY_OPERATOR")
public class Duty_operator {
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true,updatable=false)
	private Long id;
	
	@Column(name="ACCOUNT",length = 50, unique = true)
	private String account;
	
	@Column(name="PHONE_NUMBER",length = 50)
	private String phone_number;
	
	@Column(name="EMAIL",length = 50)
	private String email;
	
	//schedule   该值班人员下面的所有的值班信息
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Duty_schedule> schedule;
	//schedule
	public void setScedule(List<Duty_schedule> schedule){
		this.schedule=schedule;
	}
	public List<Duty_schedule> getScedule(){
		return this.schedule;
	}
	//id
	public Long getId(){
		return id;
	}
	//account
	public String getAccount(){
		return account;
	}
	public void setAccount(String account){
		this.account = account;
	}
	//phoneNumber
	public String getPhoneNumber(){
		return phone_number;
	}
	public void setPhoneNumber(String phone_number){
		this.phone_number = phone_number;
	}
	//email
	public String getEmail(){
		return email;
	}
	public void setEmail(String email){
		this.email = email;
	}
}
