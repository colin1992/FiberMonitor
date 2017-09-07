package daoImpl;

import java.io.Serializable;
import java.util.List;

import dao.JumperBackupDao;
import domain.Jumper_backups;

public class JumperBackupDaoImpl  extends BaseDaoImpl<Jumper_backups> implements JumperBackupDao{

	//局站ID
	public List<Jumper_backups> findAllByStationId(Serializable id) {
		String sql = " select jumperBackup"
				+ " from Jumper_backups as jumperBackup "
				+ " where jumperBackup.station.id = ?0 ";
		return findMulti(sql,id);
	}

	//光路ID
	public Jumper_backups findAllByRouteId(Serializable id) {
		String sql = "select jumperBackup from Jumper_backups as jumperBackup "
				+ " where jumperBackup.route_id = ?0";
		return findOne(sql,id);
	}
//名称
	public List<Jumper_backups> findAllByName(String name) {
		String sql = "select jumperBackup from Jumper_backups as jumperBackup "
				+ " where jumperBackup.name = ?0";
		return findMulti(sql,name);
	}
}
