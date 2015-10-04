package echo.sp.app.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import echo.sp.app.dao.UserTaskActionDAO;
import echo.sp.app.service.UserTaskActionService;

/** 
 * 用户任务流程  
 * @author Ethan   
 * @date 2015年10月4日 
 */
@Service
public class UserTaskActionServiceImpl implements UserTaskActionService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserTaskActionServiceImpl.class);
	
	@Autowired
	private UserTaskActionDAO userTaskActionDAO;

	@Override
	public int addTaskBider(Map parmMap) {
		int returnInt = 0;
    	try {
    		userTaskActionDAO.addTaskBider(parmMap);
    		userTaskActionDAO.updateTaskBider(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int updateTaskBider(Map parmMap) {
		int returnInt = 0;
    	try {
    		userTaskActionDAO.updateCancelTaskBider(parmMap);
    		int bid_num = Integer.parseInt(parmMap.get("BID_NUM").toString());
    		if (Integer.parseInt(parmMap.get("BID_NUM").toString()) <= 1) {
    			bid_num = 0;
    			parmMap.put("TASK_STATUS", "10");
			} else {
				bid_num -= 1;
			}
    		parmMap.put("BID_NUM", bid_num);
    		userTaskActionDAO.updateTaskStatus(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}
	
}
