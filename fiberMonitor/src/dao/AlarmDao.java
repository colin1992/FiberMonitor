package dao;

import java.util.List;

import domain.Alarm;

public interface AlarmDao extends BaseDao<Alarm>  {
	    //通过RTU id
		public  List<Alarm> findByRtuId(Long id);
		//通过光路id
		public  List<Alarm> findByRouteId(Long id);
		//通过告警级别
		public  List<Alarm> findByLevel(String level);
		//分页获取告警
		public List<Alarm> findPaginationByRouteId(Long routeId, int page, int perCount);
}
