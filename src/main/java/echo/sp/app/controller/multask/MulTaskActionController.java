package echo.sp.app.controller.multask;

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
import echo.sp.app.service.UserMulTaskActionService;
import echo.sp.app.service.UserService;

/**  
 * 多人任务
 * @author Ethan   
 * @date 2015年10月4日 
 */
@Controller
public class MulTaskActionController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(MulTaskActionController.class);
	
	@Autowired
	private UserMulTaskActionService userMulTaskActionService;
	
	@Autowired
	private UserService userService;
	
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
	@RequestMapping("multask/addTask")
	public void addTask(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("MulTaskActionController---addTask---begin");
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
				userMulTaskActionService.addTask(paramMap);
				// Get Task Info
				parmMap = new HashMap();
				parmMap.put("TASK_ID", task_id);
				parmMap = userMulTaskActionService.getTaskInfoByTaskId(parmMap);
				parmMap.remove("TASK_LINE");
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, parmMap, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("MulTaskActionController---addTask---interface error: ", e);
		}
	}
	
	
	/**
	 * 修改任务
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("multask/updateTask")
	public void updateTask(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("MulTaskActionController---updateTask---begin");
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
				parmMap = userMulTaskActionService.getTaskInfoByTaskId(parmMap);
				if (!"10".equals(parmMap.get("TASK_BID_STATUS").toString())) {
					super.writeJson(response, "9996", "任务执行中，不能修改！", null, null);
					return;
				}
				// 修改任务信息
				userMulTaskActionService.updateTask(paramMap);
				// Get Task Info
				parmMap = userMulTaskActionService.getTaskInfoByTaskId(parmMap);
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
			logger.error("MulTaskActionController---updateTask---interface error: ", e);
		}
	}
	
	
	/**
	 * 删除任务
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("multask/deleteTask")
	public void deleteTask(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("MulTaskActionController---deleteTask---begin");
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
				parmMap = userMulTaskActionService.getTaskInfoByTaskId(parmMap);
				String task_statu = parmMap.get("TASK_BID_STATUS").toString();
				if (!"30".equals(task_statu)) {
					super.writeJson(response, "9996", "任务进行中，不能删除！", null, null);
					return;
				}
				// 2.修改任务状态为删除状态
				paramMap.put("TASK_DEL_TIME", DateUtils.getDateTime());
				userMulTaskActionService.deleteTask(paramMap);
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, null, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("MulTaskActionController---deleteTask---interface error: ", e);
		}
	}
	
	/**
	 * 查询单个任务
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("multask/searchTaskById")
	public void searchTaskById(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("MulTaskActionController---searchTaskById---begin");
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
					resMap = userMulTaskActionService.getTaskInfoByTaskId(paramMap);
					Object obj = resMap.get("TASK_LINE");
					if (obj != null) {
						lineList = (List)obj;
					}
					resMap.remove("TASK_LINE");
				}
				
				PageBounds pageBounds = new PageBounds(pageInt, pageSizeInt , Order.formString(sortString));
				
				resList = PubTool.getResultList("UserTaskDAO.getTaskMessages", paramMap, pageBounds, sqlSessionFactory);
				
				// 仅在第一页加入留言量
				if (PubTool.isListHasData(resList) && pageInt == 1) {
					PageList pageList = (PageList) resList;
					resMap.put("totalCount", ((PageList) resList).getPaginator().getTotalCount());
				}
				
				resMap.put("MSG_LIST", resList);
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, resMap, lineList);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("MulTaskActionController---searchTaskById---interface error: ", e);
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
	@RequestMapping("multask/searchTaskList")
	public void searchTaskList(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("MulTaskActionController---searchTaskList---begin");
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
				List resList = PubTool.getResultList("UserMulTaskActionDAO.searchTaskList", paramMap, pageBounds, sqlSessionFactory);
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
			logger.error("MulTaskActionController---searchTaskList---interface error: ", e);
		}
	}
	
	
	/**
	 * 查询用户竞标的任务列表
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("multask/searchBidTaskByUserId")
	public void searchBidTaskByUserId(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("MulTaskActionController---searchBidTaskByUserId---begin");
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
				String sortString = "DIST.asc,TASK_BID_TIME.desc,TASK_CREATE_TIME.asc";
				int pageInt = Integer.parseInt(paramMap.get("page").toString());// PAGE NUMBER
				int pageSizeInt = Integer.parseInt(paramMap.get("pageSize").toString());// MAX ROWS RETURN
				PageBounds pageBounds = new PageBounds(pageInt, pageSizeInt , Order.formString(sortString));
				List resList = PubTool.getResultList("UserMulTaskActionDAO.searchBidTaskByUserId", paramMap, pageBounds, sqlSessionFactory);
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
			logger.error("MulTaskActionController---searchBidTaskByUserId---interface error: ", e);
		}
	}
	
	
	/**
	 * 竞标任务：竞标人发起
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("multask/bideTask")
	public void bideTask(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("MulTaskActionController---bideTask---begin");
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
				// 1.查询T_TASKS.TASK_BID_STATUS, 若不是10或30状态,提示不能竞标
				Map parmMap = new HashMap();
				parmMap.put("TASK_ID", paramMap.get("TASK_ID"));
				parmMap = userMulTaskActionService.getTaskInfoByTaskId(parmMap);
				String task_statu = parmMap.get("TASK_BID_STATUS").toString();
				if (!"10".equals(task_statu) && !"20".equals(task_statu)) {
					super.writeJson(response, "9996", "任务已经结束！", null, null);
					return;
				}
				// 2.插入T_MUL_TASKS_LINE竞标人信息;修改T_TASKS.TASK_BID_STATUS = '20', TASK_BID_NUM数量自增1
				paramMap.put("PATI_TIME", DateUtils.getDateTime());
				userMulTaskActionService.addTaskBider(paramMap);
				// 3.返回任务信息
				parmMap = userMulTaskActionService.getTaskInfoByTaskId(parmMap);
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
			logger.error("MulTaskActionController---bideTask---interface error: ", e);
		}
	}
	
	
	/**
	 * 完成任务: 竞标人发起
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("multask/doneTask")
	public void doneTask(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("MulTaskActionController---doneTask---begin");
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
				parmMap = userMulTaskActionService.getTaskInfoByTaskId(parmMap);
				String task_statu = parmMap.get("TASK_BID_STATUS").toString();
				if (!"20".equals(task_statu)) {
					super.writeJson(response, "9996", "任务状态错误，无法完成！", null, null);
					return;
				}
				parmMap = new HashMap();
				parmMap.put("TASK_ID", paramMap.get("TASK_ID"));
				parmMap.put("USER_ID", user_id);
				parmMap = userMulTaskActionService.getTaskInfoByTaskUserId(parmMap);
				if (parmMap == null || (parmMap != null && !"10".equals(parmMap.get("STATUS").toString()))) {
					super.writeJson(response, "9995", "任务状态错误，无法完成！", null, null);
					return;
				}
				// 2.修改任务状态为回退状态
				paramMap.put("DONE_TIME", DateUtils.getDateTime());
				userMulTaskActionService.updateTaskDone(paramMap);
				// 3.返回任务信息
				parmMap = userMulTaskActionService.getTaskInfoByTaskId(parmMap);
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
			logger.error("MulTaskActionController---doneTask---interface error: ", e);
		}
	}
	
	
	/**
	 * 未完成任务：发布者
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("multask/undoneTask")
	public void undoneTask(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("MulTaskActionController---undoneTask---begin");
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
				parmMap.put("USER_ID", user_id);
				parmMap = userMulTaskActionService.getTaskInfoByTaskUserId(parmMap);
				if (parmMap == null || (parmMap != null && !"20".equals(parmMap.get("STATUS").toString()))) {
					super.writeJson(response, "9996", "任务状态错误，无法操作！", null, null);
					return;
				}
				// 2.修改任务状态为任务发布者确认未状态
				paramMap.put("UNDONE_TIME", DateUtils.getDateTime());
				userMulTaskActionService.updateTaskUnFinish(paramMap);
				// 3.返回任务信息
				parmMap = userMulTaskActionService.getTaskInfoByTaskId(parmMap);
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
			logger.error("MulTaskActionController---undoneTask---interface error: ", e);
		}
	}
}
