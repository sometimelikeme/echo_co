package echo.sp.app.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import echo.sp.app.service.UserItemService;

/**   
 * 用户商品控制器
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Controller
public class UserItem {
	
	@Autowired
	private UserItemService userItemService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserItem.class);
	
	
}
