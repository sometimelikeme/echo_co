package echo.sp.app.controller.task;

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
import echo.sp.app.service.UserService;
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
	
	@Autowired
	private UserService userService;
	
	/**
	 * 增加任务
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("task/addTask")
	public void addTask(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserTaskController---addTask---begin");
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
				// 判断用户余额-仅在金额任务中
				if ("10".equals(paramMap.get("TASK_TYPE").toString())) {
					BigDecimal payment = new BigDecimal(paramMap.get("TASK_TOTAL_PAID").toString());
					BigDecimal total_money_Big = new BigDecimal(userService.getUserExpandInfo(paramMap).get("TOTAL_MONEY").toString());
					if (total_money_Big.compareTo(payment) < 0) {
						super.writeJson(response, "9996", "余额不足，请充值！", null, null);
						return;
					}
					paramMap.put("TOTAL_MONEY", total_money_Big);
				}
				// TASK ID
				String task_id = IdGen.uuid();
				paramMap.put("TASK_ID", task_id);
				// Check The Task 
				Map parmMap = new HashMap();
				parmMap.put("CANT_CODE", (String) session.getAttribute("CANT_CODE"));
				parmMap.put("PARM_NAME", "USER_UP_TASK_CHECK");
				paramMap.put("TASK_AUDIT_STATUS", "1".equals(PubTool.getOrgParm(parmMap, pubToolService)) ? "10" : "20");
				// Publish Status
				paramMap.put("TASK_BID_STATUS", "10");
				paramMap.put("TASK_CREATE_TIME", DateUtils.getDateTime());
				userTaskService.addTask(paramMap);
				// Get Task Info
				parmMap = new HashMap();
				parmMap.put("TASK_ID", task_id);
				parmMap.put("USER_ID", user_id);
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
			logger.debug("UserTaskController---updateTask---begin");
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
				// 获取当前任务状态,非10状态则不允许修改
				Map parmMap = new HashMap();
				parmMap.put("TASK_ID", paramMap.get("TASK_ID"));
				parmMap = userTaskService.getTaskInfoByTaskId(parmMap);
				if (!"10".equals(parmMap.get("TASK_BID_STATUS").toString())) {
					super.writeJson(response, "9996", "任务执行中，不能修改！", null, null);
					return;
				}
				// 修改任务信息
				userTaskService.updateTask(paramMap);
				// Get Task Info
				parmMap.put("USER_ID", user_id);
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
	 * 删除任务
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("task/deleteTask")
	public void deleteTask(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserTaskController---deleteTask---begin");
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
				// 1.查询T_TASKS.TASK_BID_STATUS
				Map parmMap = new HashMap();
				parmMap.put("TASK_ID", paramMap.get("TASK_ID"));
				parmMap = userTaskService.getTaskInfoByTaskId(parmMap);
				String task_statu = parmMap.get("TASK_BID_STATUS").toString();
				if (!("20".equals(task_statu) || "70".equals(task_statu))) {
					super.writeJson(response, "9996", "任务进行中，不能删除！", null, null);
					return;
				}
				// 2.修改任务状态为删除状态
				paramMap.put("TASK_DEL_TIME", DateUtils.getDateTime());
				userTaskService.deleteTask(paramMap);
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, null, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserTaskController---deleteTask---interface error: ", e);
		}
	}
	
	/**
	 * 取消任务
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("task/cancelTask")
	public void cancelTask(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserTaskController---cancelTask---begin");
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
				// 1.查询T_TASKS.TASK_BID_STATUS
				Map parmMap = new HashMap();
				parmMap.put("TASK_ID", paramMap.get("TASK_ID"));
				parmMap = userTaskService.getTaskInfoByTaskId(parmMap);
				if (!"10".equals(parmMap.get("TASK_BID_STATUS").toString())) {
					super.writeJson(response, "9996", "任务进行中，不能取消！", null, null);
					return;
				}
				// 2.修改任务状态为取消状态
				paramMap.put("TASK_CANCEL_TIME", DateUtils.getDateTime());
				userTaskService.updateTaskForCancel(paramMap);
				// 3.Get Task Info
				parmMap.put("USER_ID", user_id);
				parmMap = userTaskService.getTaskInfoByTaskId(paramMap);
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
			logger.error("UserTaskController---cancelTask---interface error: ", e);
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
			logger.debug("UserTaskController---searchTaskById---begin");
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
				
				String sortString = sort == null ? "TIME1.desc" : sort.toString();// 评论排序
				int pageInt = Integer.parseInt(page.toString());
				int pageSizeInt = Integer.parseInt(pageSize.toString());
				
				Map resMap = new HashMap();
				List lineList = null;
				List resList = null;
				
				// 仅仅在第一页中加入任务数据 以及第一页的留言
				if (pageInt == 1) {
					resMap = userTaskService.getTaskInfoByTaskId(paramMap);
					Object obj = resMap.get("TASK_LINE");
					if (obj != null) {
						lineList = (List)obj;
					}
					resMap.remove("TASK_LINE");
				}
				
				PageBounds pageBounds = new PageBounds(pageInt, pageSizeInt , Order.formString(sortString));
				
				resList = PubTool.getResultList("UserTaskDAO.getTaskMessages", paramMap, pageBounds, sqlSessionFactory);
				
				// 仅在第一页加入留言量
				int totalCount = 0;
				if (PubTool.isListHasData(resList) && pageInt == 1) {
					totalCount = ((PageList) resList).getPaginator().getTotalCount();
				}
				resMap.put("totalCount", totalCount);
				
				resMap.put("MSG_LIST", resList);
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, resMap, lineList);
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
			logger.debug("UserTaskController---searchTaskList---begin");
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				paramMap.put("CURR_DATE", DateUtils.getDateTime());
				String sortString = paramMap.get("sort").toString();
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
			logger.debug("UserTaskController---searchBidTaskByUserId---begin");
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
				String sortString = "DIST.asc,BID_TIME.desc,TASK_CREATE_TIME.asc";
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
	
	/**
	 * 任务留言
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("task/leaveMsg")
	public void leaveMsg(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserTaskController---leaveMsg---begin");
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
				paramMap.put("MSG_ID", IdGen.uuid());
				paramMap.put("TIME1", DateUtils.getDateTime());
				userTaskService.addMsg(paramMap);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserTaskController---leaveMsg---interface error: ", e);
		}
	}
	
	/**
	 * 删除任务留言
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("task/delMsg")
	public void delMsg(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserTaskController---delMsg---begin:");
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
				userTaskService.deleteMsg(paramMap);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserTaskController---leaveMsg---interface error: ", e);
		}
	}
}
