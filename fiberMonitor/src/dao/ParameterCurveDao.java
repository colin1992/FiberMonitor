package dao;

import java.io.Serializable;

import domain.Parameter_curves;

public interface ParameterCurveDao extends BaseDao<Parameter_curves>
{
    public Parameter_curves findByCurveId(Serializable id);
}
