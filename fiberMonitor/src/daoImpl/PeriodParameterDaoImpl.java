package daoImpl;

import java.io.Serializable;

import dao.PeriodParameterDao;
import domain.Period_parameters;


public class PeriodParameterDaoImpl extends BaseDaoImpl<Period_parameters> implements PeriodParameterDao
{
//根据光路Id查询周期测试参数
	public Period_parameters findByRouteId(Serializable id)
	{
		String sql = " select periodParameter "
				+ " from Period_parameters as periodParameter "
				+ " where periodParameter.route.id = ?0 ";
		return findOne(sql,id);
	}
}

