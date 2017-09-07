
/*日志*/

package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "LOG")
public class Log {
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true,updatable=false)
	private Long id;		//日志标识
	
	@Column(name="RESOURCETYPE",columnDefinition="")
	private String resourceType;   //资源类别
	
	@Column(name="RESOURCNAME",columnDefinition="")
	private String resourceName;   //资源名称
	
	@Column(name="OPERATEDETAIL",columnDefinition="")
	private String operateDetail;   //操作详情
	
	@Column(name="USER",columnDefinition="")
	private String user;   //操作用户
	
	@Column(name="DATE",columnDefinition="")
	private String date;   //操作时间
	
	//ID
	public Long getId()
	{
		return this.id;
	}
	//ResourceType
	public void setResourceType(String resourceType)
	{
		this.resourceType = resourceType;
	}
	public String getResourceType()
	{
		return resourceType;
	}
	//resourceName
	public void setResourceName(String resourceName)
	{
		this.resourceName = resourceName;
	}
	public String getResourceName()
	{
		return resourceName;
	}
	//operateDetail
	public void setOperateDetail(String operateDetail)
	{
		this.operateDetail = operateDetail;
	}
	public String getOperateDetail()
	{
		return this.operateDetail;
	}
	//user
	public void setUser(String user)
	{
		this.user = user ;
	}
	public String getUser()
	{
		return this.user;
	}
	//date
	public void setDate(String date)
	{
		this.date = date;
	}
	public String getDate()
	{
		return this.date;
	}
	
}
