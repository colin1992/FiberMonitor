package daoImpl;

import java.util.List;

import dao.AlarmDao;
import domain.Alarm;

public class AlarmDaoImpl extends BaseDaoImpl<Alarm> implements AlarmDao {
	    //通过RTU id
		public  List<Alarm> findByRtuId(Long id){
			String sql = " select alarm from Alarm as alarm where alarm.rtu_id = ?0";
			return findMulti(sql,id);
		}
		
		//通过光路id
		public  List<Alarm> findByRouteId(Long id){
			String sql = " select alarm from Alarm as alarm where route.id = ?0 order by id desc";
			return findMulti(sql,id);
		}
		//通过告警级别
		public  List<Alarm> findByLevel(String level){
			String sql = " select alarm from Alarm as alarm where alarm.alarm_level = ?0";
			return findMulti(sql,level);
		}
		//分页获取告警
		public List<Alarm> findPaginationByRouteId(Long routeId, int page, int perCount){
			String sql = " select alarm from Alarm as alarm where alarm.route.id = "+routeId+"order by id desc";
			return findPagination(sql,page,perCount);
		}
}
