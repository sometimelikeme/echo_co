package echo.sp.app.controller.comment;

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
import echo.sp.app.service.MerOrderService;

/**  
 * 商品评论 
 * @author Ethan   
 * @date 2015年9月26日 
 */
@Controller
public class ItemCommentCtrl extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(ItemCommentCtrl.class);
	
	@Autowired
	private MerOrderService merOrderService;
	
	/**
	 * 根据用户ID来获取对某一个订单中某一个商品的评论
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("comment/getSingleCommentByUserId")
	public void getSingleCommentByUserId(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("ItemCommentCtrl---getSingleCommentByUserId---dataParm: " + dataParm);
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
				paramMap = merOrderService.getSingleCommentByUserId(paramMap);
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, paramMap, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("ItemCommentCtrl---getSingleCommentByUserId---interface error: ", e);
		}
	}
	
	/**
	 * 增加商品评论
	 * 若商品评论已经存在，则先删后插
	 * 客户端需返回所有参数
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("comment/addUpdateComment")
	public void addUpdateComment(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("ItemCommentCtrl---addUpdateComment---dataParm: " + dataParm);
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
				merOrderService.addComment(paramMap);
				paramMap = merOrderService.getSingleCommentByUserId(paramMap);
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, paramMap, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("ItemCommentCtrl---addUpdateComment---interface error: ", e);
		}
	}
	
	/**
	 * 删除商品评论
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("comment/deleteComment")
	public void deleteComment(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("ItemCommentCtrl---deleteComment---dataParm: " + dataParm);
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   ut = (String) paramMap.get("ut"), 
				   s_user_id = (String) session.getAttribute("user_id");
			
			if (user_id == null || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!"10".equals(ut)) {
				super.writeJson(response, "9998", "无效客户端", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				merOrderService.deleteComment(paramMap);
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, null, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("ItemCommentCtrl---deleteComment---interface error: ", e);
		}
	}
}
