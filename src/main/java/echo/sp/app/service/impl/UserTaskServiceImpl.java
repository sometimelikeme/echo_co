package echo.sp.app.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import echo.sp.app.dao.UserTaskDAO;
import echo.sp.app.service.UserTaskService;

/**  
 * 用户任务
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Service
public class UserTaskServiceImpl implements UserTaskService{
	
	private static final Logger logger = LoggerFactory.getLogger(UserTaskServiceImpl.class);
	
	@Autowired
    private UserTaskDAO userTaskDAO;

	@Override
	public int addTask(Map parmMap) {
		return userTaskDAO.addTask(parmMap);
	}
}
