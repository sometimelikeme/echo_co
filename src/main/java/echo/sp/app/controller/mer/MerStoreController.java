package echo.sp.app.controller.mer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import echo.sp.app.command.core.CoreController;
import echo.sp.app.service.MerStoreService;

/**   
 * 商铺
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Controller
public class MerStoreController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(MerStoreController.class);
	
	@Autowired
	private MerStoreService merStoreService;
}
