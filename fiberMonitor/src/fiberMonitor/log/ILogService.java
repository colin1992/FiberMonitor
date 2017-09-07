package fiberMonitor.log;
import org.aspectj.lang.JoinPoint;
public interface  ILogService {
	//给切换光路添加日志的方法
    public void switchLog(JoinPoint point);
    //给删除资源添加日志的方法
    public void deleteResLog(JoinPoint point);
    //给增加资源添加日志的方法
    public void addResLog(JoinPoint point,Object returnObj);
}
