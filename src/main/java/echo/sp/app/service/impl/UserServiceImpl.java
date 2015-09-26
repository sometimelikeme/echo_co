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
    	try {
    		if (userDAO.addRegistAlg(parmMap) > 0) {
    			parmMap.put("TOTAL_POINT", "0");
    			parmMap.put("TOTAL_MONEY", "0");
    			if (userDAO.addUserExpand(parmMap) > 0) {
    				returnInt = 1;
    			};
    		}
		} catch (Exception e) {
			//  默认spring事务只在发生未被捕获的 runtimeexcetpion时才回滚
			throw new RuntimeException();
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
		int returnInt = 0;
    	try {
    		returnInt = userDAO.updatePwd(parmMap);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
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
    	try {
    		if (userDAO.insertToMerchant(parmMap) > 0) {
    			parmMap.put("TOTAL_POINT", "5");// 总和评分初始化为5
    			if (userDAO.insertToMerchantExpand(parmMap) > 0) {
    				returnInt = 1;
    			};
    		}
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}
	
	// FULFILL MERCHANT INFORMATION
	@Override
	public int updateToMerchant(Map parmMap) {
		int returnInt = 0;
    	try {
    		returnInt = userDAO.updateToMerchant(parmMap);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}
	
	// 登陆后获取用户基本信息
	@Override
	public Map getUserInfo(Map parmMap) {
		return userDAO.getUserInfo(parmMap);
	}

	// 完善用户基本信息
	@Override
	public int updateUserInfo(Map parmMap) {
		int returnInt = 0;
    	try {
    		returnInt = userDAO.updateUserInfo(parmMap);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}
	
	// 完善用户身份证信息
	@Override
	public int updateUserIC(Map parmMap) {
		int returnInt = 0;
    	try {
    		returnInt = userDAO.updateUserIC(parmMap);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}

	// FULFILL USER BANK ACCOUNT
	@Override
	public int updateUserAcc(Map parmMap) {
		int returnInt = 0;
    	try {
    		returnInt = userDAO.updateUserAcc(parmMap);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}

}
