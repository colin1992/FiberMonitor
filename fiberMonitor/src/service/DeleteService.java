package service;

import java.io.Serializable;


public interface DeleteService 
{
//删除区域(Areas)
	void deleteArea(Serializable id);
	
//删除局站(Stations)
	void deleteStation(Serializable id);

//删除地标(Landmarks)
	void deleteLandmark(Serializable id);
	
//删除机柜(Cabinets)
	void deleteCabinet(Serializable id);
	
//删除机架(Rakcs)
	
//删除配线架(Distributing_frames)
	void deleteDistributingFrame(Serializable id);
	
//删除配线架端口(Frame_ports)
	
//删除RTU(Rtus)
	void deleteRtu(Serializable id);	
	
//删除RTU端口(Rtu_ports)
	void deleteRtuPort(Serializable id);
	
//删除光缆(Optival_cables)
	void deleteOpticalCable(Serializable id);
	
//删除纤芯(Fiber_cores)
	
	
//删除配线架跳纤(Jumper_frames)
	void deleteJumperFrame(Serializable id);
	
//删除光路跳纤(Jumper_routes)
	void deleteJumperRoute(Serializable id);
//删除备纤光源跳纤	
	public void deleteJumperBackup(Serializable id);	
//删除光路(Routes)
	boolean deleteRoute(Serializable id);
	
//删除优化参数(Optimize_parameters)
	void deleteOptimizeParameter(Serializable id);
	
//删除周期测试参数(Period_parameters)
	void deletePeriodParameter(Serializable id);
	
//删除回传曲线(Curves)
	void deleteCurve(Serializable id);
	
//删除回传事件(Event_curves)
	
//删除回传测试参数(Parameter_curves)
	
//删除告警组
	void deletePriority(Serializable id);
//删除配对组
	void deleteProtectGroup(Serializable id);
//删除角色
	public void deleteRole(Serializable id);
//删除权限
	public void deletePermission(Serializable id);
//删除用户
	public void deleteUser(Serializable id);
//删除门限
	public void deleteThreshold(Serializable id);	
//删除值班表
	public void deleteDutySchedule(Serializable id);
//删除值班人员
	public void deleteDutyOperator(Serializable id);
//删除验证码记录
	public void deleteVerifyCode(Serializable id);
//删除日志
	public void deleteLog(Serializable id);
	//删除告警
	public boolean deleteAlarm(Serializable id);
/**删除告警经验**/	
	public void deleteAlarmBank(Serializable id);
//删除序列号
    public void deleteSNCode(Long id);
}
