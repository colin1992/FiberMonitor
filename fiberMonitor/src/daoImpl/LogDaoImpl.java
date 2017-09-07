package daoImpl;

import java.util.List;

import dao.LogDao;
import domain.Log;

public class LogDaoImpl  extends BaseDaoImpl<Log> implements LogDao{
	//查找RTU全部日志
    public List<Log> findLogs(String logType){
    	String sql="select Log from Log as Log where Log.resourceType= '"+logType+"' order by id desc";
    	return find(sql);
    }
    //分页查找日志
    public List<Log>findLogByPage(String logType,int page,int perCount){
    	String sql="select Log from Log as Log where Log.resourceType= '"+logType+ "' order by id desc";
    	return findPagination(sql,page,perCount);
    }
}
