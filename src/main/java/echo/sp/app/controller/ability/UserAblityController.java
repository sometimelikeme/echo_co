package echo.sp.app.controller.ability;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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
import echo.sp.app.command.page.PubTool;
import echo.sp.app.command.utils.DateUtils;
import echo.sp.app.command.utils.IdGen;
import echo.sp.app.command.utils.UserAgentUtils;
import echo.sp.app.service.PubToolService;
import echo.sp.app.service.UserAblityService;
import echo.sp.app.service.UserService;

/** 
 * 用户技能  
 * @author Ethan   
 * @date 2015年10月24日 
 */
@Controller
public class UserAblityController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(UserAblityController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PubToolService pubToolService;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	private UserAblityService userAblityService;
	
	/**
	 * 增加技能
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("abli/addAbli")
	public void addAbli(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserAblityController---addAbli---begin");
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   s_user_id = (String) session.getAttribute("user_id");
			
			if (user_id == null || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserAblityController---addAbli---interface error: ", e);
		}
	}
	
	
	/**
	 * 修改技能
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("abli/updateAbli")
	public void updateAbli(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserAblityController---updateAbli---begin");
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   s_user_id = (String) session.getAttribute("user_id");
			
			if (user_id == null || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserAblityController---updateAbli---interface error: ", e);
		}
	}
	
	
	/**
	 * 删除技能
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("abli/deleteAbli")
	public void deleteAbli(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserAblityController---deleteAbli---begin");
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   s_user_id = (String) session.getAttribute("user_id");
				
			if (user_id == null || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserAblityController---deleteAbli---interface error: ", e);
		}
	}
}
