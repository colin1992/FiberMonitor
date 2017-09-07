package dao;

import java.util.List;

import domain.Log;

public interface LogDao extends BaseDao<Log>{
	//查找全部日志
     List<Log> findLogs(String logType);
    //分页查找日志
     List<Log>findLogByPage(String logType,int page,int perCount);
}
