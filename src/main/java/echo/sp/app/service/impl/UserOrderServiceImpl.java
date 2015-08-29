package echo.sp.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import echo.sp.app.dao.UserOrderDAO;
import echo.sp.app.service.UserOrderService;

/**   
 * 用户订单
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Service
public class UserOrderServiceImpl implements UserOrderService{
	
	private static final Logger logger = LoggerFactory.getLogger(UserOrderServiceImpl.class);
	
	@Autowired
    private UserOrderDAO userOrderDAO;
}
