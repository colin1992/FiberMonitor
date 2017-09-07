package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/*告警方式*/
@Entity
@Table(name="Alarm_way")
public class Alarm_ways 
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true,updatable=false)
	private Long id;		//标识
	
	@Column(name="NAME",nullable=false,columnDefinition="")
	private String alarm_name;		//名
	
	@Column(name="DESCRIPTION",columnDefinition="")
	private String description;		//描述
	
//Alarm_way()
	public Alarm_ways(){}
	
//areaId
	public Long getId()
	{
		return this.id;
	}
//areaName
	public void setAlarm_name(String name)
	{
		this.alarm_name = name;
	}
	public String getAlarm_name()
	{
		return this.alarm_name;
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
