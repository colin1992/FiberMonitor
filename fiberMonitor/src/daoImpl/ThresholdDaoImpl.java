package daoImpl;

import java.io.Serializable;
import dao.ThresholdDao;
import domain.Threshold;

public class ThresholdDaoImpl extends BaseDaoImpl<Threshold> implements  ThresholdDao{
	//通过光路Id获取门限	
	public Threshold findByRouteId(Serializable id)
	{
		String sql = " select threshold from Threshold as threshold where threshold.route.id = ?0";
		return findOne(sql,id);
	}
}
