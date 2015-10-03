package echo.sp.app.controller.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import echo.sp.app.command.core.CoreController;
import echo.sp.app.service.UserTaskService;

/**  
 * 用户任务 
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Controller
public class UserTaskController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(UserTaskController.class);
	
	@Autowired
	private UserTaskService userTaskService;
}
