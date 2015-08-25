package echo.sp.app.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import echo.sp.app.command.core.CoreController;
import echo.sp.app.command.model.Code;
import echo.sp.app.command.model.SecCode;
import echo.sp.app.command.utils.Encodes;

/**   
 * GET THE NORMAL CODE FOR CLIENT
 * @author Ethan   
 * @date 2015年8月23日 
 */
@Controller
public class NoCoController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(NoCoController.class);
	
	@RequestMapping("login/getCode")
	public void getCode(HttpServletResponse response, @RequestParam String no_co) {
		if (logger.isDebugEnabled()) {
			logger.debug("NoCoController---getCode---no_co: " + no_co);
		}
		if (SecCode.getKey("NO_CO_ORI") == null) {
			SecCode.setKey("NO_CO_ORI", SecCode.no_co_org);
		}
		if (SecCode.getKey("NO_CO_ORI").equals(no_co)) {
			String NO_CO = Encodes.encodeBase64(no_co);
			SecCode.setKey("NO_CO", NO_CO);
			Map resMap = new HashMap(); 
			resMap.put("no_co", NO_CO);
			super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, resMap, null);
		} else {
			logger.error("NoCoController---getCode---bad no_co: " + no_co);
			response.setStatus(404);
		}
	}
}
