package dao;

import java.util.List;

import domain.Optical_powers;

public interface OpticalPowerDao  extends BaseDao<Optical_powers>{
	//通过routeId查找光功率数据
	public List<Optical_powers> findOpticalPowersByRouteId(Long id);
}
