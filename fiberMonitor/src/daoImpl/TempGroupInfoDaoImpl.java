package daoImpl;

import java.io.Serializable;
import java.util.List;

import dao.TempGroupInfoDao;
import domain.TempGroupInfo;

public class TempGroupInfoDaoImpl  extends BaseDaoImpl<TempGroupInfo> implements TempGroupInfoDao{
	//根据Rtu-Id找到该rtu下暂存的全部分组记录
		public List<TempGroupInfo> findAllByRtuId(Serializable id)
		{
			String sql = " select tempgroupinfo from TempGroupInfo as tempgroupinfo where tempgroupinfo.rtu_id = ?0";
			return findMulti(sql,id);
		}
	//通过rtuId和alarmGroupId获取当前rtu下暂存的告警组信息
		public TempGroupInfo findByRtuIdAndalarmGroupId(Serializable id,Long alarmId){
			String sql = " select tempgroupinfo from TempGroupInfo as tempgroupinfo where tempgroupinfo.rtu_id = ?0 and tempgroupinfo.alarmGroup_id = ?1";
			return findOne(sql,id,alarmId);
		}	
}
