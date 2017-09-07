

/*区域*/


package domain;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="AREAS")
public class Areas 
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true,updatable=false)
	private Long id;		//区域标识
	
	@Column(name="NAME",nullable=false,columnDefinition="")
	private String area_name;		//区域名
	
	@Column(name="DESCRIPTION",columnDefinition="")
	private String description;		//描述
	
	@Column(name="CREATE_DATE",updatable=false,columnDefinition="")
	private String create_date;			//创建日期
	
	@Column(name="ALTER_DATE",columnDefinition="")			
	private String alter_date;			//修改日期
	
	@Column(name="CREATE_USER",updatable=false,columnDefinition="")
	private String create_user;		//创建用户
	
	@Column(name="ALTER_USER",columnDefinition="")
	private String alter_user;		//修改用户
	
//foreign key
	//Stations			获取该区域下的所有局站
	@OneToMany(targetEntity=Stations.class,mappedBy="area")
	@Cascade({CascadeType.DELETE})
	private List<Stations> stations = new ArrayList<>();
	public List<Stations> getStations()
	{
		return this.stations; 
	}

//Areas()
	public Areas(){}
	
//areaId
	public Long getId()
	{
		return this.id;
	}
//areaName
	public void setArea_name(String name)
	{
		this.area_name = name;
	}
	public String getArea_name()
	{
		return this.area_name;
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
	
//create_date
	public void setCreate_date(String date)
	{
		this.create_date = date;
	}
	public String getCreate_date()
	{
		return this.create_date;
	}
	
//alter_date
	public void setAlter_date(String date)
	{
		this.alter_date = date;
	}
	public String getAlter_date()
	{
		return this.alter_date;
	}
	
//create_user
	public void setCreate_user(String create_user)
	{
		this.create_user = create_user;
	}
	public String getCreate_user()
	{
		return this.create_user;
	}
	
//alter_user
	public void setAlter_user(String alter_user)
	{
		this.alter_user = alter_user;
	}
	public String getAlter_user()
	{
		return this.alter_user;
	}
	@Override
	public String toString() {
		return "{\"id\":" + "\"1_"+id +"\""+ ", \"pid\":" + "\"0\""
				+ ", \"name\":" +"\""+ area_name +"\""+ ", \"open\":"
				+ "\"false\"" +",\"hrefAddress\":"+"\""+"\""+"}" ;
	}
}
