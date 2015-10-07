package echo.sp.app.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import echo.sp.app.command.utils.DateUtils;
import echo.sp.app.command.utils.Prop;
import echo.sp.app.dao.UserDAO;
import echo.sp.app.dao.UserTaskDAO;
import echo.sp.app.service.UserTaskService;

/**  
 * 用户任务
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Service
public class UserTaskServiceImpl implements UserTaskService{
	
	private static final Logger logger = LoggerFactory.getLogger(UserTaskServiceImpl.class);
	
	@Autowired
    private UserTaskDAO userTaskDAO;
	
	@Autowired
	private UserDAO userDAO;

	@Override
	public int addTask(Map parmMap) {
		int returnInt = 0;
    	try {
    		// 发布任务
    		userTaskDAO.addTask(parmMap);
    		// 用户金额消费明细表参数集
			Map tranMap = new HashMap();
			tranMap.put("USER_ID", parmMap.get("USER_ID"));
			tranMap.put("TIME1", parmMap.get("TASK_CREATE_TIME"));
			tranMap.put("DATE1", DateUtils.getToday());
			tranMap.put("MONEY_NUM", parmMap.get("TASK_TOTAL_PAID"));
			tranMap.put("TASK_ID", parmMap.get("TASK_ID"));
			tranMap.put("ORDER_ID", "");
			tranMap.put("ABLI_ORDER_ID", "");
			tranMap.put("PRE_PAID_ID", "");
			tranMap.put("STATUS", "20");// 减
			tranMap.put("MN_TYPE", "10");// 任务
			userDAO.insertUserMoneyRecord(parmMap);
			// 修改用户金额-减
			BigDecimal payment = new BigDecimal(parmMap.get("TASK_TOTAL_PAID").toString());
			BigDecimal total_money_Big = new BigDecimal(parmMap.get("TOTAL_MONEY").toString());
			parmMap.put("TOTAL_MONEY", total_money_Big.subtract(payment));
			userDAO.updateUserMoney(parmMap);
			// 系统账户金额消费明细表参数集
			parmMap.put("USER_ID", Prop.getString("system.systemAdminId"));// 系统账号
			parmMap.put("STATUS", "10");// 增
			userDAO.insertUserMoneyRecord(parmMap);
			// 将付款暂存到系统账户-增
			total_money_Big =  new BigDecimal(userDAO.getUserExpandInfo(parmMap).get("TOTAL_MONEY").toString());
			parmMap.put("TOTAL_MONEY", total_money_Big.add(payment));
			userDAO.updateUserMoney(parmMap);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int updateTask(Map parmMap) {
		return userTaskDAO.updateTask(parmMap);
	}

	@Override
	public Map getTaskInfoByTaskId(Map parmMap) {
		Map resMap = userTaskDAO.getTaskHeadByTaskId(parmMap);
		resMap.put("TASK_LINE", userTaskDAO.getTaskLineByTaskId(parmMap));
		return resMap;
	}

	@Override
	public int deleteTask(Map parmMap) {
		return userTaskDAO.deleteTask(parmMap);
	}

	@Override
	public int updateTaskForCancel(Map parmMap) {
		return userTaskDAO.updateTaskForCancel(parmMap);
	}
}
