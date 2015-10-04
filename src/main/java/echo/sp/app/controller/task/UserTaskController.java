package echo.sp.app.controller.task;

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
import echo.sp.app.service.UserTaskService;

/**  
 * 用户任务 
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Controller
public class UserTaskController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(UserTaskController.class);
	
	@Autowired
	private UserTaskService userTaskService;
	
	@Autowired
	private PubToolService pubToolService;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	/**
	 * 增加任务
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("task/addTask")
	public void addTask(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserTaskController---addTask---dataParm: " + dataParm);
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
				// TASK ID
				String task_id = IdGen.uuid();
				paramMap.put("TASK_ID", task_id);
				// Check The Task 
				Map parmMap = new HashMap();
				parmMap.put("CANT_CODE", (String) session.getAttribute("CANT_CODE"));
				parmMap.put("PARM_NAME", "USER_UP_TASK_CHECK");
				paramMap.put("TASK_AUDIT_STATUS", "1".equals(PubTool.getOrgParm(parmMap, pubToolService)) ? "10" : "20");
				// Publish Status
				paramMap.put("TASK_STATUS", "10");
				paramMap.put("TASK_CREATE_TIME", DateUtils.getDateTime());
				userTaskService.addTask(paramMap);
				// Get Task Info
				parmMap = new HashMap();
				parmMap.put("TASK_ID", task_id);
				parmMap = userTaskService.getTaskInfoByTaskId(parmMap);
				parmMap.remove("TASK_LINE");
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, parmMap, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserTaskController---addTask---interface error: ", e);
		}
	}
	
	/**
	 * 修改任务
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("task/updateTask")
	public void updateTask(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserTaskController---updateTask---dataParm: " + dataParm);
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
				userTaskService.updateTask(paramMap);
				// Get Task Info
				Map parmMap = new HashMap();
				parmMap.put("TASK_ID", paramMap.get("TASK_ID"));
				parmMap = userTaskService.getTaskInfoByTaskId(parmMap);
				List lineList = null;
				Object obj = parmMap.get("TASK_LINE");
				if (obj != null) {
					lineList = (List)obj;
				}
				parmMap.remove("TASK_LINE");
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, parmMap, lineList);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserTaskController---updateTask---interface error: ", e);
		}
	}
	
	/**
	 * 查询单个任务
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("task/searchTaskById")
	public void searchTaskById(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserTaskController---searchTaskById---dataParm: " + dataParm);
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
				paramMap = userTaskService.getTaskInfoByTaskId(paramMap);
				List lineList = null;
				Object obj = paramMap.get("TASK_LINE");
				if (obj != null) {
					lineList = (List)obj;
				}
				paramMap.remove("TASK_LINE");
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, paramMap, lineList);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserTaskController---searchTaskById---interface error: ", e);
		}
	}
	
	/**
	 * 查询任务列表
	 * 1.查询用户发布的任务列表 -OPTIMAL                       
	 * 2.查询所有的任务列表 -DEFAULT
	 * 3.查询一级、二级分类下的任务列表 -OPTIMAL
	 * 4.根据任务标题模糊查询 -OPTIMAL
	 * 5.根据距离查询 LONGITUDE LATITUDE -REQUIRED
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("task/searchTaskList")
	public void searchTaskList(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserTaskController---searchTaskList---dataParm: " + dataParm);
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				String sortString = "DIST.asc,TASK_CREATE_TIME.desc";
				int pageInt = Integer.parseInt(paramMap.get("page").toString());// PAGE NUMBER
				int pageSizeInt = Integer.parseInt(paramMap.get("pageSize").toString());// MAX ROWS RETURN
				PageBounds pageBounds = new PageBounds(pageInt, pageSizeInt , Order.formString(sortString));
				List resList = PubTool.getResultList("UserTaskDAO.searchTaskList", paramMap, pageBounds, sqlSessionFactory);
				Map resMap = new HashMap();
				int totalCount = 0;
				if (PubTool.isListHasData(resList)) {
		    		totalCount = ((PageList) resList).getPaginator().getTotalCount();
		    		PubTool.reCalculateDistance(resList, paramMap, "TASK_LONGITUDE", "TASK_LATITUDE");
				}
				resMap.put("totalCount", totalCount);
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, resMap, resList);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserTaskController---searchTaskList---interface error: ", e);
		}
	}
	
	/**
	 * 查询用户竞标的任务列表
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("task/searchBidTaskByUserId")
	public void searchBidTaskByUserId(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserTaskController---searchBidTaskByUserId---dataParm: " + dataParm);
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
				String sortString = "TASK_BID_TIME.desc,DIST.asc,TASK_CREATE_TIME.asc";
				int pageInt = Integer.parseInt(paramMap.get("page").toString());// PAGE NUMBER
				int pageSizeInt = Integer.parseInt(paramMap.get("pageSize").toString());// MAX ROWS RETURN
				PageBounds pageBounds = new PageBounds(pageInt, pageSizeInt , Order.formString(sortString));
				List resList = PubTool.getResultList("UserTaskDAO.searchBidTaskByUserId", paramMap, pageBounds, sqlSessionFactory);
				Map resMap = new HashMap();
				int totalCount = 0;
				if (PubTool.isListHasData(resList)) {
		    		totalCount = ((PageList) resList).getPaginator().getTotalCount();
		    		PubTool.reCalculateDistance(resList, paramMap, "TASK_LONGITUDE", "TASK_LATITUDE");
				}
				resMap.put("totalCount", totalCount);
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, resMap, resList);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserTaskController---searchBidTaskByUserId---interface error: ", e);
		}
	}
	
}
