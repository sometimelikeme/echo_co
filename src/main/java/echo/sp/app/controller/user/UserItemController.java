package echo.sp.app.controller.user;

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

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import echo.sp.app.command.core.CoreController;
import echo.sp.app.command.model.Code;
import echo.sp.app.command.page.PubTool;
import echo.sp.app.command.utils.DateUtils;
import echo.sp.app.command.utils.UserAgentUtils;
import echo.sp.app.service.UserItemService;

/**   
 * 用户商品
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Controller
public class UserItemController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(UserItemController.class);
	
	@Autowired
	private UserItemService userItemService;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	/**
	 * 收藏店铺
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("user/addMerColl")
	public void addMerColl(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserItemController---addMerColl---begin");
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
				paramMap.put("TIME1", DateUtils.getDateTime());
				paramMap.put("NOTE", "");
				userItemService.addMerColl(paramMap);
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, null, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserItemController---addMerColl---interface error: ", e);
		}
	}
	
	/**
	 * 收藏商品
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("user/addItemColl")
	public void addItemColl(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserItemController---addItemColl---begin");
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
				paramMap.put("TIME1", DateUtils.getDateTime());
				paramMap.put("NOTE", "");
				userItemService.addItemColl(paramMap);
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, null, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserItemController---addItemColl---interface error: ", e);
		}
	}
	
	/**
	 * 删除收藏店铺
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("user/deleteMerColl")
	public void deleteMerColl(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserItemController---deleteMerColl---begin");
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
				userItemService.deleteMerColl(paramMap);
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, null, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserItemController---deleteMerColl---interface error: ", e);
		}
	}
	
	/**
	 * 删除收藏商品
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("user/deleteItemColl")
	public void deleteItemColl(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserItemController---deleteItemColl---begin");
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
				userItemService.deleteItemColl(paramMap);
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, null, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserItemController---deleteItemColl---interface error: ", e);
		}
	}
	
	/**
	 * 获取收藏店铺列表
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("user/getMerColl")
	public void getMerColl(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserItemController---getMerColl---begin");
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
				int pageInt = Integer.parseInt(paramMap.get("page").toString());
				int pageSizeInt = Integer.parseInt(paramMap.get("pageSize").toString());
				String sortString = "TIME1.desc";
				
				PageBounds pageBounds = new PageBounds(pageInt, pageSizeInt , Order.formString(sortString));
				List resList = PubTool.getResultList("UserItemDAO.getMerColl", paramMap, pageBounds, sqlSessionFactory);
				
				Map daMap = null;
				if (PubTool.isListHasData(resList)) {
					daMap = new HashMap();
		    		daMap.put("totalCount", ((PageList) resList).getPaginator().getTotalCount());
				}
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, daMap, resList);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserItemController---getMerColl---interface error: ", e);
		}
	}
	
	/**
	 * 获取收藏商品列表
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("user/getItemColl")
	public void getItemColl(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserItemController---getItemColl---begin");
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
				int pageInt = Integer.parseInt(paramMap.get("page").toString());
				int pageSizeInt = Integer.parseInt(paramMap.get("pageSize").toString());
				String sortString = "TIME1.desc";
				
				PageBounds pageBounds = new PageBounds(pageInt, pageSizeInt , Order.formString(sortString));
				List resList = PubTool.getResultList("UserItemDAO.getItemColl", paramMap, pageBounds, sqlSessionFactory);
				
				Map daMap = null;
				if (PubTool.isListHasData(resList)) {
					daMap = new HashMap();
		    		daMap.put("totalCount", ((PageList) resList).getPaginator().getTotalCount());
				}
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, daMap, resList);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserItemController---getItemColl---interface error: ", e);
		}
	}
}
