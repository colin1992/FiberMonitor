package daoImpl;

import java.io.Serializable;

import dao.ParameterCurveDao;
import domain.Parameter_curves;

public class ParameterCurveDaoImpl extends BaseDaoImpl<Parameter_curves> implements ParameterCurveDao
{
	 public Parameter_curves findByCurveId(Serializable id){
		 String sql = " select parameter_curves from Parameter_curves as parameter_curves where parameter_curves.curve.id = ?0";
			return findOne(sql,id);
	 }
}
