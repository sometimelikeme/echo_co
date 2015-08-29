package echo.sp.app.service.impl;

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
	
}
