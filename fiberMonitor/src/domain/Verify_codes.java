package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 找回密码的验证码
 * **/
@Entity
@Table(name="VERIFY_CODE")
public class Verify_codes {
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true,updatable=false)
	private Long id;			//标识
	@Column(name="CODE")
	private String code;			//验证码
	
	@Column(name="ACCOUNT")
	private String account;			//验证码
	
	//id
	public Long getId(){
		return this.id;
	}
	//code
	public void setVerifyCode(String code){
		this.code=code;
	}
	public String getVerifyCode(){
		return this.code;
	}
	//account
	public void setAccount(String account){
		this.account=account;
	}
	public String getAccount(){
		return this.account;
	}
}
