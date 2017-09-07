package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="SYSTEMINFO")
/**系统参数**/
public class SystemInfo {
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true,updatable=false)
	private Long id;			//标识
	@Column(name="IP")
	private String ip;			//服务器IP地址
	
	@Column(name="EMAIL_ADDR")
	private String email_addr;			//邮件服务器地址
	
	@Column(name="EMAIL_PORT")
	private String email_port;			//邮件服务器端口
	
	@Column(name="EMAIL_ACCOUNT")
	private String email_account;			//邮件账户
	
	@Column(name="EMAIL_PWORD")
	private String email_pword;			//邮件密码
	
	@Column(name="SMS_PORT")
	private String sms_port;			//短信模块端口
	
	//id
	public Long getId(){
		return this.id;
	}
	//ip
	public void setIp(String ip){
		this.ip=ip;
	}
	public String getIp(){
		return this.ip;
	}
	//email_addr
	public void setEmailAddr(String addr){
		this.email_addr=addr;
	}
	public String getEmailAddr(){
		return this.email_addr;
	}
	//email_port
	public void setEmailPort(String port){
		this.email_port=port;
	}
	public String getEmailPort(){
		return this.email_port;
	}
	//email_account
	public void setEmailAccount(String account){
		this.email_account=account;
	}
	public String getEmailAccount(){
		return this.email_account;
	}
	//email_pword
	public void setEmailPword(String pword){
		this.email_pword=pword;
	}
	public String getEmailPword(){
		return this.email_pword;
	}
	//sms_port
	public void setSmsPort(String sms_port){
		this.sms_port=sms_port;
	}
	public String getSmsPort(){
		return this.sms_port;
	}
}
