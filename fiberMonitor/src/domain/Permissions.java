package domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="PERMISSION")
public class Permissions  {
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true)
    private Long id;
	@Column(name="NAME", length=50)
	private String name;
	@Column(name="DESCRIPTION",length=100)
	private String description;
	@Column(name="PERMISSION",length=50,unique=true)//权限必须唯一
	private String permission;
	
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PARENT_ID")
	private Permissions parent;
	
	@OneToMany(mappedBy = "parent",fetch=FetchType.LAZY,cascade={CascadeType.ALL})
	private List<Permissions> children;
	//cascade = CascadeType.ALL, 
	@ManyToMany(fetch = FetchType.EAGER, mappedBy="pmss")
	private List<Role> roles;
	public Permissions (){
		
	}
    //ID
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
    
    public Permissions getParent() {
		return parent;
	}

	public void setParent(Permissions parent) {
		this.parent = parent;
	}

	public List<Permissions> getChildren() {
		return children;
	}

	public void setChildren(List<Permissions> children) {
		this.children = children;
	}

	public List <Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
}
