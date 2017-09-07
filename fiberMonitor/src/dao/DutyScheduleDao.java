package dao;

import java.io.Serializable;
import java.util.List;

import domain.Duty_schedule;

public interface DutyScheduleDao extends BaseDao<Duty_schedule>
{
//根据用户ID查询值班表
	List<Duty_schedule> findAllByDutyOperatorId(Serializable id);
//根据日期查询值班表
	Duty_schedule findByDate(int date);
}
