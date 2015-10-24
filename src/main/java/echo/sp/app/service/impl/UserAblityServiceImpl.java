package echo.sp.app.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import echo.sp.app.dao.UserAblityDAO;
import echo.sp.app.service.UserAblityService;

/**   
 * 用户技能服务
 * @author Ethan   
 * @date 2015年10月24日 
 */
@Service
public class UserAblityServiceImpl implements UserAblityService {

	private static final Logger logger = LoggerFactory.getLogger(UserAblityServiceImpl.class);
	
	@Autowired
	private UserAblityDAO userAblityDAO;

	@Override
	public int addAblity(Map parmMap) {
		int returnInt = 0;
    	try {
    		userAblityDAO.addAblity(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			logger.error("UserAblityServiceImpl---addAblity---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int updateAblity(Map parmMap) {
		int returnInt = 0;
    	try {
    		userAblityDAO.updateAblity(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			logger.error("UserAblityServiceImpl---updateAblity---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int deleteAblity(Map parmMap) {
		int returnInt = 0;
    	try {
    		userAblityDAO.deleteAblity(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			logger.error("UserAblityServiceImpl---deleteAblity---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;	}
	
}
