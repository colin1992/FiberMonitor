package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/*暂存光路分组信息*/
    @Entity
	@Table(name="TEMPGROUPINFO")
	public class TempGroupInfo
	{
		@Id
		@GenericGenerator(name="key_increment",strategy="increment")
		@GeneratedValue(generator="key_increment")
		@Column(name="ID",nullable=false,unique=true)
		private Long id;		//暂存分组的id
		
		
		@Column(name="RTU_ID")
		private Long rtu_id;	//指定该分组所属rtu id
		
		//alarmGroupId
		@Column(name="ALARMGROUP_ID")
		private Long alarmGroup_id;	//指定该分组记录所属alarmGroupId

		//routeId			//该分组包含的routeId
		@Column(name="ROUTE_IDS",nullable=false)
		private String route_ids;
		
	   //TempGroupInfo
		public TempGroupInfo(){}
		
	   //id
		public Long getId()
		{
			return this.id;
		}
	  //alarmGroupId
		public void setalarmGroupId(Long alarmGroupId)
		{
			this.alarmGroup_id = alarmGroupId;
		}
		public Long getalarmGroupId()
		{
			return this.alarmGroup_id;
		}
	   //rtu_Id
		public void setRtuId(Long rtu_Id)
		{
			this.rtu_id = rtu_Id;
		}
		public Long getRtuId()
		{
			return this.rtu_id;
		}
		
	//route_ids
		public void setRouteIds(String route_ids)
		{
			this.route_ids = route_ids;
		}
		public String getRouteIds()
		{
			return this.route_ids;
		}
		
	

}