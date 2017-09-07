package daoImpl;

import java.io.Serializable;

import dao.OptimizeParameterDao;
import domain.Optimize_parameters;

public class OptimizeParameterDaoImpl extends BaseDaoImpl<Optimize_parameters> implements OptimizeParameterDao
{
//根据光路id获取优化测试参数
	public Optimize_parameters findByRouteId(Serializable id)
	{
		String sql = " select optimizeParameter "
				+ " from Optimize_parameters as optimizeParameter "
				+ " where optimizeParameter.route.id = ?0";
		return findOne(sql,id);
	}
}
