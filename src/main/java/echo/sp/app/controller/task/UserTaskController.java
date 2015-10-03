package echo.sp.app.controller.task;

import java.util.HashMap;
import java.util.List;
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
				paramMap.put("TASK_AUDIT_STATUS", "1".equals(PubTool.getOrgParm(parmMap, pubToolService)) ? "20" : "10");
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
}
