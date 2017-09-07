package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name ="ROLE")
public class Role {
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true)
	private Long id;
	@Column(name="NAME",length=50)
	private String name;
	@Column(name="ADD_USER")
	private String add_user;
	@Column(name="ADD_TIME")
	private String add_time;
	@Column(name="ALTER_USER")
	private String alter_user;
	@Column(name="ALTER_TIME")
	private String alter_time;
	@Column(name="DESCRIPTION",length=50)
	private String description;
	
	//User		
	@OneToMany(targetEntity=User.class,cascade=CascadeType.ALL,mappedBy="role")
	private List<User> users = new ArrayList<User>();
	//cascade={CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE}, 
	@ManyToMany(fetch = FetchType.EAGER )
	private List<Permissions> pmss;
	//id
	public Long getId() {
		return id;
	}
	//name
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	//描述
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	//用户
	public List<User> getUsers() {
		return users;
	}
	
	//权限
	public List<Permissions> getPmss() {
		return pmss;
	}
	public void setPmss(List<Permissions> pmss) {
		this.pmss = pmss;
	}
	//add_time
	public String getAddTime() {
		return this.add_time;
	}
	public void setAddTime(String add_time) {
		this.add_time = add_time;
	}
	//add_user
	public String getAddUser() {
		return this.add_user;
	}
	public void setAddUser(String add_user) {
		this.add_user = add_user;
	}
	//alter_time
	public String getAlterTime() {
		return this.alter_time;
	}
	public void setAlterTime(String alter_time) {
		this.alter_time = alter_time;
	}
	//alter_user
	public String getAlterUser() {
		return this.alter_user;
	}
	public void setAlterUser(String alter_user) {
		this.alter_user = alter_user;
	}
}
