package dao;

import java.util.List;

import domain.Duty_operator;

public interface DutyOperatorDao extends BaseDao<Duty_operator>{

	List<Duty_operator> findByName(String name);

}
