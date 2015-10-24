package echo.sp.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	private UserAblityService userAblityService;
	
	
}
