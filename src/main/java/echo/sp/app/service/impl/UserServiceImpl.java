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
    // INIT USER EXPAND INFORMATION
    @Override
	public int addRegistAlg(Map parmMap) {
    	int returnInt = 0;
		if (userDAO.addRegistAlg(parmMap) > 0) {
			parmMap.put("TOTAL_POINT", "0");
			parmMap.put("TOTAL_MONEY", "0");
			if (userDAO.addUserExpand(parmMap) > 0) {
				returnInt = 1;
			};
		}
		return returnInt;
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
	// INIT MERCHANT EXPAND INFORMATION
	@Override
	public int insertToMerchant(Map parmMap) {
		int returnInt = 0;
		if (userDAO.insertToMerchant(parmMap) > 0) {
			parmMap.put("TOTAL_POINT", "0");// 总和评分初始化为0
			if (userDAO.insertToMerchantExpand(parmMap) > 0) {
				returnInt = 1;
			};
		}
		return returnInt;
	}
	
	// FULFILL MERCHANT INFORMATION
	@Override
	public int updateToMerchant(Map parmMap) {
		return userDAO.updateToMerchant(parmMap);
	}
	
	// 登陆后获取用户基本信息
	@Override
	public Map getUserInfo(Map parmMap) {
		return userDAO.getUserInfo(parmMap);
	}

	// 完善用户基本信息
	@Override
	public int updateUserInfo(Map parmMap) {
		return userDAO.updateUserInfo(parmMap);
	}
	
	// 完善用户身份证信息
	@Override
	public int updateUserIC(Map parmMap) {
		return userDAO.updateUserIC(parmMap);
	}

	// FULFILL USER BANK ACCOUNT
	@Override
	public int updateUserAcc(Map parmMap) {
		return userDAO.updateUserAcc(parmMap);
	}

}
