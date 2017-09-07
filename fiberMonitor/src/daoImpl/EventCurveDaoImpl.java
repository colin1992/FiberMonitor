package daoImpl;

import java.io.Serializable;
import java.util.List;

import dao.EventCurveDao;
import domain.Event_curves;

public class EventCurveDaoImpl extends BaseDaoImpl<Event_curves> implements EventCurveDao
{
//根据曲线Id查询该曲线的事件
	public List<Event_curves> findByCurveId(Serializable id)
	{
		String sql = " select events from Event_curves as events "
				+ " where events.curve.id = ?0";
		return findMulti(sql,id);
	}
}
