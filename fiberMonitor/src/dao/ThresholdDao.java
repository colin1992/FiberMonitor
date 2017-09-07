package dao;

import java.io.Serializable;

import domain.Threshold;

public interface ThresholdDao extends BaseDao<Threshold>{
	//通过光路Id获取门限
	public Threshold findByRouteId(Serializable id);
}
