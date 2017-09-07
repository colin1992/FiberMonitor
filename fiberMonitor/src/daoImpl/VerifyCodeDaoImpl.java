package daoImpl;

import dao.VerifyCodeDao;
import domain.Verify_codes;

public class VerifyCodeDaoImpl extends BaseDaoImpl<Verify_codes> implements VerifyCodeDao{
	 //通过账户查找
		public Verify_codes findByAccount(String account){
			String sql="select verify_codes from Verify_codes as verify_codes where verify_codes.account=?0";
			return findOne(sql,account);
		}
}
