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
import echo.sp.app.command.utils.DateUtils;
import echo.sp.app.command.utils.UserAgentUtils;
import echo.sp.app.service.UserTaskActionService;
import echo.sp.app.service.UserTaskService;

/**  
 * 用户做任务流程 
 * @author Ethan   
 * @date 2015年10月4日 
 */
@Controller
public class UserTaskActionController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(UserTaskActionController.class);
	
	@Autowired
	private UserTaskService userTaskService;
	
	@Autowired
	private UserTaskActionService userTaskActionService;
	
	/**
	 * 竞标任务：竞标人发起
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("task/bideTask")
	public void bideTask(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserTaskActionController---bideTask---dataParm: " + dataParm);
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
				// 1.查询T_TASKS.TASK_STATUS, 若不是10或30状态,提示不能竞标
				Map parmMap = new HashMap();
				parmMap.put("TASK_ID", paramMap.get("TASK_ID"));
				parmMap = userTaskService.getTaskInfoByTaskId(parmMap);
				String task_statu = parmMap.get("TASK_STATUS").toString();
				if (!("10".equals(task_statu) || "30".equals(task_statu))) {
					super.writeJson(response, "9996", "任务执行中，不能竞标！", null, null);
					return;
				}
				// 2.插入T_TASKS_LINE竞标人信息;修改T_TASKS.TASK_STATUS = '30', BID_NUM数量自增1
				paramMap.put("TASK_BID_TIME", DateUtils.getDateTime());
				userTaskActionService.addTaskBider(paramMap);
				// 3.返回任务信息
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
			logger.error("UserTaskActionController---bideTask---interface error: ", e);
		}
	}
	
	/**
	 * 取消竞标：竞标人发起
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("task/cancelBideTask")
	public void cancelBideTask(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserTaskActionController---cancelBideTask---dataParm: " + dataParm);
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
				// 1.查询T_TASKS.TASK_STATUS, 若是40状态,提示不能取消竞标
				Map parmMap = new HashMap();
				parmMap.put("TASK_ID", paramMap.get("TASK_ID"));
				parmMap = userTaskService.getTaskInfoByTaskId(parmMap);
				String task_statu = parmMap.get("TASK_STATUS").toString();
				if ("40".equals(task_statu)) {
					super.writeJson(response, "9996", "您已获得任务，请联系任务发布者取消！", null, null);
					return;
				}
				// 2.修改T_TASKS_LINE竞标人信息为取消竞标状态；BID_NUM数量自减少1，若发现此时BID_NUM为0，则修改TASK_STATUS为10状态
				paramMap.put("BID_NUM", parmMap.get("BID_NUM"));
				paramMap.put("CANCEL_TIME", DateUtils.getDateTime());
				userTaskActionService.updateTaskBider(paramMap);
				// 3.返回任务信息
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
			logger.error("UserTaskActionController---cancelBideTask---interface error: ", e);
		}
	}
	
	/**
	 * 选择竞标人：发布者
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("task/chooseTasker")
	public void chooseTasker(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserTaskActionController---chooseTasker---dataParm: " + dataParm);
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
				// 1.查询T_TASKS.TASK_STATUS, 若非30状态，不能选定竞标人
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, null, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserTaskActionController---chooseTasker---interface error: ", e);
		}
	}
	
	/**
	 * 回退任务: 选定中标人之后，与中标人协商解除任务 - 发布者
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("task/backTasker")
	public void backTasker(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserTaskActionController---backTasker---dataParm: " + dataParm);
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
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, null, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserTaskActionController---backTasker---interface error: ", e);
		}
	}
	
	/**
	 * 完成任务: 竞标人发起
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("task/doneTask")
	public void doneTask(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserTaskActionController---doneTask---dataParm: " + dataParm);
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
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, null, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserTaskActionController---doneTask---interface error: ", e);
		}
	}
	
	/**
	 * 结束任务: 发布者
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("task/closeTask")
	public void closeTask(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserTaskActionController---closeTask---dataParm: " + dataParm);
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
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, null, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserTaskActionController---closeTask---interface error: ", e);
		}
	}
}
