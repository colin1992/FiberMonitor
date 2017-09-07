package dao;

import java.io.Serializable;

import domain.Optimize_parameters;

public interface OptimizeParameterDao extends BaseDao<Optimize_parameters>
{
//根据光路id获取优化测试参数
	Optimize_parameters findByRouteId(Serializable id);
}
