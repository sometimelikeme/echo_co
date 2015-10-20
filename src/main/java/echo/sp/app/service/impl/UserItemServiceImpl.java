package echo.sp.app.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import echo.sp.app.dao.UserItemDAO;
import echo.sp.app.service.UserItemService;

/**   
 * 用户商品
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Service
public class UserItemServiceImpl implements UserItemService{
	
	private static final Logger logger = LoggerFactory.getLogger(UserItemServiceImpl.class);
	
	@Autowired
    private UserItemDAO userItemDAO;

	@Override
	public int addMerColl(Map parmMap) {
		int returnInt = 0;
    	try {
    		returnInt = userItemDAO.addMerColl(parmMap);
		} catch (Exception e) {
			logger.error("UserItemServiceImpl---addMerColl---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int addItemColl(Map parmMap) {
		int returnInt = 0;
    	try {
    		returnInt = userItemDAO.addItemColl(parmMap);
		} catch (Exception e) {
			logger.error("UserItemServiceImpl---addItemColl---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int deleteMerColl(Map parmMap) {
		int returnInt = 0;
    	try {
    		returnInt = userItemDAO.deleteMerColl(parmMap);
		} catch (Exception e) {
			logger.error("UserItemServiceImpl---deleteMerColl---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int deleteItemColl(Map parmMap) {
		int returnInt = 0;
    	try {
    		returnInt = userItemDAO.deleteItemColl(parmMap);
		} catch (Exception e) {
			logger.error("UserItemServiceImpl---deleteItemColl---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}
	
}
