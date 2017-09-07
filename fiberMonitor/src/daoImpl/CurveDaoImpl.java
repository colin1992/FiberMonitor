package daoImpl;

import java.io.Serializable;
import java.util.List;
import dao.CurveDao;
import domain.Curves;

public class CurveDaoImpl extends BaseDaoImpl<Curves> implements CurveDao{
//根据Rtu-Id找到其对应的所有回传曲线
	public List<Curves> findAllByRtuId(Serializable id){
		String sql = " select curves from Curves as curves where curves.route.rtu_id = ?0";
		return findMulti(sql,id);
	}
	
/**根据光路id分页获取曲线
 * @param id 光路Id
 * @param page 当前页码
 * @param perCount 每页显示的条目数量
 */
	public  List<Curves> findPaginationByRouteId(Serializable id,int page,int perCount){
		String sql = " select curves from Curves as curves where curves.route.id ="+id+" order by id desc";
		return findPagination(sql,page,perCount);
	}
	//分页查找周期测试获得的曲线
	public List<Curves> findPaginationPeriodByRouteId(Serializable id,int page,int perCount){
		String sql = " select curves from Curves as curves where curves.route.id ="+id+" and curves.curve_type='周期测试' order by id desc";
		return findPagination(sql,page,perCount);
	}
	public List<Curves> findAllByRouteId(Serializable id){
		String sql = " select curves from Curves as curves where curves.route.id = ?0 order by id desc";
		return findMulti(sql,id);
	}
//根据光路id获取参考曲线
	public Curves findReferenceByRouteId(Serializable id){
		String sql = "select curves from Curves as curves where curves.route.id = ?0 and curves.referring=true";
		List<Curves> curves=findMulti(sql, id);
		if((curves!=null)&&(curves.size()>0)){
			return curves.get(0);
		}
		else{
			return null;
		}
		 
	}
	public List<Curves> findCurveByTypeAndRouteId(Serializable id,String curveType){
		String sql = "select curves from Curves as curves where curves.route.id = ?0 and curves.curve_type= ?1 order by id desc";
		return findMulti(sql,id,curveType);
	}
}
