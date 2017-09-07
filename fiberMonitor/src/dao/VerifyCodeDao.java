package dao;

import domain.Verify_codes;

public interface VerifyCodeDao extends BaseDao<Verify_codes> {
    //通过账户查找
	public Verify_codes findByAccount(String account);
}
