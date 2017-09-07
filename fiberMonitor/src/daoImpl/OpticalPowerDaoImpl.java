package daoImpl;

import java.util.List;

import dao.OpticalPowerDao;
import domain.Optical_powers;

public class OpticalPowerDaoImpl extends BaseDaoImpl<Optical_powers> implements OpticalPowerDao {

	//通过routeId查找
	public List<Optical_powers> findOpticalPowersByRouteId(Long id) {
		String sql = " select optical_powers from Optical_powers as optical_powers where optical_powers.route_id = ?0";
		return findMulti(sql,id);
	}
      
}
