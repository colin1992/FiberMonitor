package daoImpl;

import java.io.Serializable;
import java.util.List;

import dao.OpticalCableDao;
import domain.Optical_cables;

public class OpticalCableDaoImpl extends BaseDaoImpl<Optical_cables> implements OpticalCableDao
{
//根据局站id获取当前局站的所有光缆
	public List<Optical_cables> findAllByStationId(Serializable id)
	{
		String sql = " select opticalCables "
				+ " from Optical_cables as opticalCables "
				+ " where opticalCables.station_a_id = ?0 "
				+ " or opticalCables.station_z_id = ?0 ";
		return findMulti(sql,id);
	}
//根据AZ局站名称查找光缆	
	public	List<Optical_cables> findByAZStationId(Long idA,Long idZ){
		String sql = " select opticalCables "
				+ " from Optical_cables as opticalCables "
				+ " where opticalCables.station_a_id = ?0 "
				+ " and opticalCables.station_z_id = ?1 ";
		return findMulti(sql,idA,idZ);
	}
}
