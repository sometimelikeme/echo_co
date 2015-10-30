package echo.sp.app.controller.ability;

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
import echo.sp.app.command.utils.DateUtils;
import echo.sp.app.command.utils.UserAgentUtils;
import echo.sp.app.service.UserAblityService;

/**  
 * 技能评论 
 * @author Ethan   
 * @date 2015年10月28日 
 */
@Controller
public class UserAbliCommentCtrl extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(UserAbliCommentCtrl.class);
	
	@Autowired
	private UserAblityService userAblityService;
	
	/**
	 * 根据用户ID来获取对某一个订单中某一个技能的评论
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("abli/getCommentById")
	public void getCommentById(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserAbliCommentCtrl---getCommentById---begin");
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
				paramMap = userAblityService.getCommentById(paramMap);
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, paramMap, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserAbliCommentCtrl---getCommentById---interface error: ", e);
		}
	}
	
	/**
	 * 增加技能评论
	 * 若评论已经存在，则先删后插
	 * 客户端需返回所有参数
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("abli/addComment")
	public void addComment(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserAbliCommentCtrl---addComment---begin");
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
				paramMap.put("COMMENT_TIME", DateUtils.getDateTime());
				userAblityService.addComment(paramMap);
				paramMap = userAblityService.getCommentById(paramMap);
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, paramMap, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserAbliCommentCtrl---addComment---interface error: ", e);
		}
	}
	
	/**
	 * 删除评论
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("abli/delComment")
	public void delComment(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserAbliCommentCtrl---delComment---begin");
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   ut = (String) paramMap.get("ut"), 
				   s_user_id = (String) session.getAttribute("user_id");
			
			if (user_id == null || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				userAblityService.delComment(paramMap);
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, null, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserAbliCommentCtrl---delComment---interface error: ", e);
		}
	}
}
