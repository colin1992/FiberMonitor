package dao;

import java.io.Serializable;
import java.util.List;

import domain.Optical_cables;

public interface OpticalCableDao extends BaseDao<Optical_cables>
{
//根据局站id获取当前局站的所有光缆
	List<Optical_cables> findAllByStationId(Serializable id);
//根据AZ局站名称查找光缆	
	List<Optical_cables> findByAZStationId(Long idA,Long idZ);
}
