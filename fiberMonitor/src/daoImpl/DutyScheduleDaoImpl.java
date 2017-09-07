package daoImpl;

import java.io.Serializable;
import java.util.List;

import dao.DutyScheduleDao;
import domain.Duty_schedule;

public class DutyScheduleDaoImpl extends BaseDaoImpl<Duty_schedule> implements DutyScheduleDao
{
	public List<Duty_schedule> findAllByDutyOperatorId(Serializable id)
	{
		String sql = " select duty_schedule from Duty_schedule as duty_schedule where duty_schedule.duty_operator.id = ?0 ";
		return findMulti(sql,id);
	}
//根据日期查询值班表
	public Duty_schedule findByDate(int date){
		String sql = " select schedule from Duty_schedule as schedule where schedule.duty_week = ?0 ";
		return findOne(sql,date);
	}
}
