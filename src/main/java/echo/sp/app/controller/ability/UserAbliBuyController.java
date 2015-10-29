package echo.sp.app.controller.ability;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import echo.sp.app.command.core.CoreController;
import echo.sp.app.command.model.Code;
import echo.sp.app.command.utils.IdGen;
import echo.sp.app.command.utils.UserAgentUtils;
import echo.sp.app.service.PubToolService;
import echo.sp.app.service.UserAblityService;
import echo.sp.app.service.UserService;

/**   
 * @author Ethan   
 * @date 2015年10月29日 
 */
@Controller
public class UserAbliBuyController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(UserAbliBuyController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PubToolService pubToolService;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	private UserAblityService userAblityService;
	
	
	/**
	 * 购买技能
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("abli/buyAbility")
	public void buyAbility(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserAbliBuyController---buyAbility---begin");
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   s_user_id = (String) session.getAttribute("user_id");
			
			if (user_id == null || "".equals(user_id) || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				// 判断用户余额
				BigDecimal payment = new BigDecimal(paramMap.get("TOTAL_PAY").toString());
				BigDecimal total_money_Big = new BigDecimal(userService.getUserExpandInfo(paramMap).get("TOTAL_MONEY").toString());
				if (total_money_Big.compareTo(payment) < 0) {
					super.writeJson(response, "9996", "余额不足，请充值！", null, null);
					return;
				}
				
				String order_id = IdGen.uuid();
				paramMap.put("ABLI_ORDER_ID", order_id);
				paramMap.put("TOTAL_MONEY", total_money_Big);
				
				userAblityService.addBuyAbility(paramMap);
				
				// ABLI_ORDER 技能订单详情
				// ABLI_INFO 技能详情
				paramMap = userAblityService.getAbliOrderById(paramMap);
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, paramMap, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserAbliBuyController---buyAbility---interface error: ", e);
		}
	}
}
