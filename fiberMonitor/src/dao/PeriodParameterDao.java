package dao;

import java.io.Serializable;

import domain.Period_parameters;

public interface PeriodParameterDao extends BaseDao<Period_parameters>
{
//根据光路Id查询周期测试参数
	Period_parameters findByRouteId(Serializable id);
}
