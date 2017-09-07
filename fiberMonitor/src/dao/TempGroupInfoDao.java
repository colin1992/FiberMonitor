package dao;

import java.io.Serializable;
import java.util.List;
import domain.TempGroupInfo;

public interface TempGroupInfoDao  extends BaseDao<TempGroupInfo>{
	//通过rtuId获取当前rtu下暂存的告警组
	 List<TempGroupInfo> findAllByRtuId(Serializable id);
	//通过rtuId和alarmGroupId获取当前rtu下暂存的告警组信息
	 TempGroupInfo findByRtuIdAndalarmGroupId(Serializable id, Long alarmGroupId);
}
