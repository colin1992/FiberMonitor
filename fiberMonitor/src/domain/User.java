package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="USER")
public class User{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true,updatable=false)
	private Long id;
	@Column(name="ACCOUNT",length=50, unique=true)
	private String account;
	@Column(name="PHONE",length=20)
	private String phone;
	@Column(name="EMAIL",length=50)
	private String email;
	@Column(name="DESCRIPTION")
	private String description;
	@Column(name="PASSWORD",length = 100)
    private String password;
	
	//foreign key  Role		
	@ManyToOne(targetEntity=Role.class)
    @JoinColumn(name="ROLE_ID",referencedColumnName="ID",nullable=false)
	private Role role;
	//id
	public Long getId() {
		return id;
	}
    public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
    //password
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
 
    //role
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
   //phone
	public void setPhone(String phone){
		this.phone=phone;
	}
	public String getPhone(){
		return this.phone;
	}
   //email
	public void setEmail(String email){
		this.email=email;
	}
	public String getEmail(){
		return this.email;
	}
  //description
	public void setDescription(String description){
		this.description=description;
	}
	public String getDescription(){
		return this.description;
	}
}
