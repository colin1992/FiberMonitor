package dao;

import java.io.Serializable;
import java.util.List;

import domain.Jumper_backups;

public interface JumperBackupDao extends BaseDao<Jumper_backups>{
	//根据局站id获取当前局站的所有备纤光源跳纤
		List<Jumper_backups> findAllByStationId(Serializable id);
	//光路ID
		Jumper_backups findAllByRouteId(Serializable id);
//名称
		public List<Jumper_backups>findAllByName(String name);		
}
