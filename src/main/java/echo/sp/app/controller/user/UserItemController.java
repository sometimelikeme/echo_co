package echo.sp.app.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import echo.sp.app.command.core.CoreController;
import echo.sp.app.service.UserItemService;

/**   
 * 用户商品
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Controller
public class UserItemController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(UserItemController.class);
	
	@Autowired
	private UserItemService userItemService;
	
	
}