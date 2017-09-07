

/**优先级类型**/


package domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="PRIORITIES")
public class Priorities
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true)
	private Long id;	//优先级类型标识
	
	@Column(name="NAME",nullable=false)
	private String name;		//优先级类型名
	
	@Column(name="DESCRIPTION")
	private String description;		//描述
	
//foreign key
	@Column(name="RTU_ID",nullable=false,updatable=false)
	private Long rtu_id;	//指定该光路所属rtu id
//Priorities()
	public Priorities(){}
	
//id
	public Long getId()
	{
		return this.id;
	}
	
//name
	public void setName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return this.name;
	}
//rtu_id
	public void setRtuId(Long rtu_id){
		this.rtu_id=rtu_id;
	}
	public Long gerRtuId(){
		return this.rtu_id;
	}
//description
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getDescription()
	{
		return this.description;
	}
}
