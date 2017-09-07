

/**光路拓扑图：站点**/


package domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="TOPOLOGIC_POINTS")
public class Topologic_points
{
	@Id
	@GenericGenerator(name="key_increment",strategy="increment")
	@GeneratedValue(generator="key_increment")
	@Column(name="ID",nullable=false,unique=true,updatable=false)
	private Long id;		//光路拓扑图-站点标识
	
	@Column(name="TOPOLOGIC_ORDER")
	private int topologic_order;		//元素在该拓扑图中序号
	
	@Column(name="TOPOLOGIC_TYPE")
	private String topologic_type;		//元素类型:RTU、配线架
	
	@Column(name="TOPOLOGIC_ID")
	private Long topologic_id;			//元素ID
	
//foreign key
	//Routes		//该光路拓扑图元素对应的光路
	@ManyToOne(targetEntity=Routes.class)
	@JoinColumn(name="ROUTE_ID",referencedColumnName="ID")
	private Routes route;
	public void setRoute(Routes route)
	{
		this.route = route;
	}
	public Routes getRoute()
	{
		return this.route;
	}
	
//id
	public Long getId()
	{
		return this.id;
	}
	
//topologic_order
	public void setTopologic_order(int order)
	{
		this.topologic_order = order;
	}
	public int getTopologic_order()
	{
		return this.topologic_order;
	}
	
//topologic_type
	public void setTopologic_type(String type)
	{
		this.topologic_type = type;
	}
	public String getTopologic_type()
	{
		return this.topologic_type;
	}
	
//topologic_id
	public void setTopologic_id(Long topologic_id)
	{
		this.topologic_id = topologic_id;
	}
	public Long getTopologic_id()
	{
		return this.topologic_id;
	}
}
