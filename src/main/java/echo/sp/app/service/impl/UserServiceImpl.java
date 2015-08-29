package echo.sp.app.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import echo.sp.app.dao.UserDAO;
import echo.sp.app.service.UserService;

/**
 * 用户登录
 * @author Ethan
 * @date 2015年8月29日 
 */
@Service
public class UserServiceImpl implements UserService{
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
    private UserDAO userDAO;
    
	// CHECK IF THE USER EXISTS 
    @Override
	public int getCheckReg(Map parmMap) {
		return userDAO.getCheckReg(parmMap);
	}
    
    // REGIST USER AND LOGIN IN 
    @Override
	public int addRegistAlg(Map parmMap) {
		return userDAO.addRegistAlg(parmMap);
	}
    
    // LOGIN
    @Override
	public String login(Map parmMap) {
		return userDAO.login(parmMap);
	}
    
    // GET MERCHANTS INFO
	@Override
	public Map getMerchantInfo(Map parmMap) {
		return userDAO.getMerchantInfo(parmMap);
	}

	// CHANGE PASSWORD
	@Override
	public int updatePwd(Map parmMap) {
		return userDAO.updatePwd(parmMap);
	}
	
	// CHECK IF MERCHANT
	@Override
	public int checkMerchant(Map parmMap) {
		return userDAO.checkMerchant(parmMap);
	}

	// UPGRADE TO MERCHANT
	@Override
	public int insertToMerchant(Map parmMap) {
		return userDAO.insertToMerchant(parmMap);
	}

	@Override
	public int updateToMerchant(Map parmMap) {
		return userDAO.updateToMerchant(parmMap);
	}

	
}
