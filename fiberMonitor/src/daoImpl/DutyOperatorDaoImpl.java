package daoImpl;

import java.util.List;

import dao.DutyOperatorDao;
import domain.Duty_operator;

public class DutyOperatorDaoImpl extends BaseDaoImpl<Duty_operator> implements DutyOperatorDao{

	@Override
	public List<Duty_operator> findByName(String name) {
		String sql = " select duty_operator from Duty_operator as duty_operator "
				+ " where duty_operator.account= ?0 ";
		return findMulti(sql,name);
	}
  
}
