package echo.sp.app.controller.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import echo.sp.app.command.core.CoreController;
import echo.sp.app.command.model.Code;
import echo.sp.app.command.utils.UserAgentUtils;
import echo.sp.app.command.utils.ValidUtils;

/**   
 * @author Ethan   
 * @date 2015年8月28日 
 */
public class UpdateMerchant extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(UpdateMerchant.class);
	
	/**
	 * 用户申请为商铺
	 * @param response
	 * @param tel
	 */
	@RequestMapping("user/toMerchant")
	public void toMerchant(HttpServletRequest req,
			HttpServletResponse response, @RequestParam String u_id,
			@RequestParam String ut) {
		if (logger.isDebugEnabled()) {
			logger.debug("UpdateMerchant---toMerchant---begin: " + u_id);
		}
		if (!"10".equals(ut)) {
			super.writeJson(response, Code.FAIL, "无效客户端", null, null);
		} else if ("10".equals(ut) && !UserAgentUtils.isMobileOrTablet(req)) {
			super.writeJson(response, "9998", "无效设备", null, null);
		} else if ("".equals(u_id) || u_id.length() == 0) {
			super.writeJson(response, "9997", "无效账号", null, null);
		} else {
			
		}
	}
	
}
