package echo.sp.app.controller.ability;

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
import echo.sp.app.command.utils.IdGen;
import echo.sp.app.command.utils.UserAgentUtils;
import echo.sp.app.service.PubToolService;
import echo.sp.app.service.UserAblityService;

/** 
 * 用户技能  
 * @author Ethan   
 * @date 2015年10月24日 
 */
@Controller
public class UserAblityController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(UserAblityController.class);
	
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
				// ABLI_ID
				String abli_id = IdGen.uuid();
				paramMap.put("ABLI_ID", abli_id);
				// Check The Task 
				Map parmMap = new HashMap();
				parmMap.put("CANT_CODE", (String) session.getAttribute("CANT_CODE"));
				parmMap.put("PARM_NAME", "ABLI_TASK_CHECK");
				paramMap.put("ABLI_STATUS", "1".equals(PubTool.getOrgParm(parmMap, pubToolService)) ? "10" : "20");
				// Publish Status
				String dateTime = DateUtils.getDateTime();
				paramMap.put("ABLI_CREATE_TIME", dateTime);
				paramMap.put("ABLI_LAST_UPDATE", dateTime);
				paramMap.put("ABLI_BUY_COUNT", "0");
				userAblityService.addAblity(paramMap);
				// Get Ability Info
				parmMap = new HashMap();
				parmMap.put("ABLI_ID", abli_id);
				parmMap = userAblityService.searchAblityById(parmMap);
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, parmMap, null);
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
				paramMap.put("ABLI_LAST_UPDATE", DateUtils.getDateTime());
				userAblityService.updateAblity(paramMap);
				paramMap = userAblityService.searchAblityById(paramMap);
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, paramMap, null);
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
				paramMap.put("ABLI_LAST_UPDATE", DateUtils.getDateTime());
				userAblityService.deleteAblity(paramMap);
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, null, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserAblityController---deleteAbli---interface error: ", e);
		}
	}
	
	
	/**
	 * 查询技能列表
	 * 1.查询用户发布的技能列表 -OPTIMAL                       
	 * 2.查询所有的技能列表 -DEFAULT
	 * 3.根据距离查询 LONGITUDE LATITUDE -REQUIRED
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("abli/searchAblityList")
	public void searchAblityList(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserAblityController---searchAblityList---begin");
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				// DIST.desc,ABLI_BUY_COUNT.desc,ABLI_COMMMENT.desc,ABLI_POINT.desc,ABLI_LAST_UPDATE.desc
				String sortString = paramMap.get("sort").toString();
				int pageInt = Integer.parseInt(paramMap.get("page").toString());// PAGE NUMBER
				int pageSizeInt = Integer.parseInt(paramMap.get("pageSize").toString());// MAX ROWS RETURN
				
				PageBounds pageBounds = new PageBounds(pageInt, pageSizeInt , Order.formString(sortString));
				
				List resList = PubTool.getResultList("UserAblityDAO.searchAblityList", paramMap, pageBounds, sqlSessionFactory);
				
				Map resMap = new HashMap();
				int totalCount = 0;
				if (PubTool.isListHasData(resList)) {
		    		totalCount = ((PageList) resList).getPaginator().getTotalCount();
		    		PubTool.reCalculateDistance(resList, paramMap, "ABLI_LONGITUDE", "ABLI_LATITUDE");
				}
				resMap.put("totalCount", totalCount);
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, resMap, resList);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserAblityController---searchAblityList---interface error: ", e);
		}
	}
	
	
	/**
	 * 查询单技能
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("abli/searchAblityById")
	public void searchTaskById(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserAblityController---searchAblityById---begin");
		}
		
		try {
			super.getParm(req, response);
			
			if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				Map paramMap = data.getDataset();
				
				Object page = paramMap.get("page"),// PAGE NUMBER
					   pageSize  = paramMap.get("pageSize"),// MAX ROWS RETURN
					   sort = paramMap.get("sort");// SORT INFO
				
				String sortString = sort == null ? "COMMENT_TIME.desc" : sort.toString();// 评论排序
				int pageInt = Integer.parseInt(page.toString());
				int pageSizeInt = Integer.parseInt(pageSize.toString());
				
				Map resMap = new HashMap();
				List resList = null;
				
				// 分页查询，仅仅在第一页中加入技能数据，满足的记录数totalCount， 以及第一页的技能留言
				if (pageInt == 1) {
					resMap = userAblityService.searchAblityById(paramMap);
				}
				
				PageBounds pageBounds = new PageBounds(pageInt, pageSizeInt , Order.formString(sortString));
				
				resList = PubTool.getResultList("UserAblityDAO.getAbliComments", paramMap, pageBounds, sqlSessionFactory);
				
				// 仅在第一页加入评论
				int totalCount = 0;
				if (PubTool.isListHasData(resList) && pageInt == 1) {
					totalCount = ((PageList) resList).getPaginator().getTotalCount();
				}
				resMap.put("totalCount", totalCount);
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, resMap, resList);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserAblityController---searchAblityById---interface error: ", e);
		}
	}
}
