package echo.sp.app.controller.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import echo.sp.app.command.core.CoreController;
import echo.sp.app.command.model.Code;
import echo.sp.app.command.utils.Encodes;
import echo.sp.app.command.utils.IdcardUtils;
import echo.sp.app.command.utils.MD5Util;
import echo.sp.app.command.utils.UserAgentUtils;
import echo.sp.app.command.utils.ValidUtils;
import echo.sp.app.service.UserService;

/** 
 * Fulfill User Information
 * @author Ethan   
 * @date 2015年9月1日 
 */
@Controller
public class UserInfoController extends CoreController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);
	
	@Autowired
	private UserService userService;
	
	/**
	 * Fulfill User Base Information
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("user/updateUserInfo")
	public void updateUserInfo(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserInfoController---updateUserInfo---begin");
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   ut = (String) paramMap.get("ut"), 
				   s_user_id = (String) session.getAttribute("user_id"),
				   email = (String)paramMap.get("EMAIL");
			
			// Get and compare with user id in session
			if (user_id == null || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!"10".equals(ut)) {// Only user has access
				super.writeJson(response, "9998", "无效客户端", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else if (!"".equals(email) && !ValidUtils.isEmail(email)) {
				super.writeJson(response, "9996", "无效邮箱", null, null);	
			} else {
				
				int res = userService.updateUserInfo(paramMap);
				
				paramMap = new HashMap();
				paramMap.put("USER_ID", user_id);
				
				paramMap = userService.getUserInfo(paramMap);

				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, paramMap, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserInfoController---updateUserInfo---interface error: ", e);
		}
	}
	
	
	/**
	 * Fulfill User ID CARD Information
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("user/updateUserIC")
	public void updateUserIC(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserInfoController---updateUserIC---begin");
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   ut = (String) paramMap.get("ut"), 
				   iden_card = (String) paramMap.get("IDEN_CARD"),
				   s_user_id = (String) session.getAttribute("user_id");
			
			// Get and compare with user id in session
			if (user_id == null || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!"10".equals(ut)) {// Only user has access
				super.writeJson(response, "9998", "无效客户端", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else if (!IdcardUtils.validateCard(iden_card)) {
				super.writeJson(response, "9996", "身份证不合法", null, null);
			} else {
				
				int res = userService.updateUserIC(paramMap);
				
				paramMap = new HashMap();
				paramMap.put("USER_ID", user_id);
				
				paramMap = userService.getUserInfo(paramMap);

				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, paramMap, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserInfoController---updateUserIC---interface error: ", e);
		}
	}
	
	
	/**
	 * FULFILL USER BANK ACCOUNT
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("user/updateUserAcc")
	public void updateUserAcc(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserInfoController---updateUserAcc---begin");
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   ut = (String) paramMap.get("ut"), 
				   s_user_id = (String) session.getAttribute("user_id");
			
			// Get and compare with user id in session
			if (user_id == null || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!"10".equals(ut)) {// Only user has access
				super.writeJson(response, "9998", "无效客户端", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				
				paramMap.put("PAY_PWD", MD5Util.getMD5String(Encodes.decodeBase64String(paramMap.get("PAY_PWD").toString())));
				
				int res = userService.updateUserAcc(paramMap);
				
				paramMap = new HashMap();
				paramMap.put("USER_ID", user_id);
				
				paramMap = userService.getUserInfo(paramMap);

				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, paramMap, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserInfoController---updateUserAcc---interface error: ", e);
		}
	}
	
	
	/**
	 * 获取用户扩展信息
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("user/getUserExpandInfo")
	public void getUserExpandInfo(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserInfoController---getUserExpandInfo---begin");
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   ut = (String) paramMap.get("ut"), 
				   s_user_id = (String) session.getAttribute("user_id");
			
			if (user_id == null || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!"10".equals(ut)) {// Only user has access
				super.writeJson(response, "9998", "无效客户端", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, userService.getUserExpandInfo(paramMap), null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserInfoController---getUserExpandInfo---interface error: ", e);
		}
	}
}
