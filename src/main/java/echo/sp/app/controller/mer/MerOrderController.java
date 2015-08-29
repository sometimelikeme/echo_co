package echo.sp.app.controller.mer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import echo.sp.app.command.core.CoreController;
import echo.sp.app.service.MerOrderService;

/**  
 * 商铺订单 
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Controller
public class MerOrderController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(MerOrderController.class);
	
	@Autowired
	private MerOrderService merOrderService;
}
