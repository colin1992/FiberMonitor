package dao;

import java.io.Serializable;
import java.util.List;
import domain.Curves;

public interface CurveDao extends BaseDao<Curves>
{
//根据Rtu-Id找到其对应的所有回传曲线
	abstract List<Curves> findAllByRtuId(Serializable id);
	
/**根据光路id分页获取曲线
 * @param id 光路Id
 * @param page 当前页码
 * @param perCount 每页显示的条目数量
 * @param id
 * @param page
 * @param perCount
 * @return
 */
	 List<Curves> findPaginationByRouteId(Serializable id,int page,int perCount);
//分页查找周期测试获得的曲线
	 List<Curves> findPaginationPeriodByRouteId(Serializable id,int page,int perCount);
//根据光路ID获取所有的曲线	
	 List<Curves> findAllByRouteId(Serializable id);
//根据光路id获取参考曲线
	 Curves findReferenceByRouteId(Serializable id);
	 List<Curves> findCurveByTypeAndRouteId(Serializable routeId,String curveType);
	
}
