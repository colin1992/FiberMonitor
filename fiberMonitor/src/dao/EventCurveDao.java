package dao;

import java.io.Serializable;
import java.util.List;

import domain.Event_curves;

public interface EventCurveDao extends BaseDao<Event_curves>
{
//根据曲线Id查询该曲线的事件
	List<Event_curves> findByCurveId(Serializable id);
}
