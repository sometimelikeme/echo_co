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
    		// 积分任务由平台发布
    		// 无需产生积分消费记录
    		if ("20".equals(parmMap.get("TASK_TYPE").toString())) {
				return 1;
			}
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
			userDAO.insertUserMoneyRecord(tranMap); 
			// 修改用户金额-减
			BigDecimal payment = new BigDecimal(parmMap.get("TASK_TOTAL_PAID").toString());
			BigDecimal total_money_Big = new BigDecimal(parmMap.get("TOTAL_MONEY").toString());
			tranMap.put("TOTAL_MONEY", total_money_Big.subtract(payment));
			userDAO.updateUserMoney(tranMap);
			// 系统账户金额消费明细表参数集
			tranMap.put("USER_ID", Prop.getString("system.systemAdminId"));// 系统账号
			tranMap.put("STATUS", "10");// 增
			userDAO.insertUserMoneyRecord(tranMap);
			// 将付款暂存到系统账户-增
			total_money_Big =  new BigDecimal(userDAO.getUserExpandInfo(tranMap).get("TOTAL_MONEY").toString());
			tranMap.put("TOTAL_MONEY", total_money_Big.add(payment));
			userDAO.updateUserMoney(tranMap);
		} catch (Exception e) {
			logger.error("UserTaskServiceImpl---addTask---interface error: ",e);
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
		Map sMap = userTaskDAO.getBidInfo(parmMap);
		String is_bid = "0";
		String is_bid_status = "0";
		if (sMap != null) {
			is_bid = "10".equals(sMap.get("IS_BIDE").toString()) ? "1" : "0";
			is_bid_status = "1";
		}
		resMap.put("is_bid", is_bid);
		resMap.put("is_bid_status", is_bid_status);
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

	@Override
	public int addMsg(Map parmMap) {
		int returnInt = 0;
    	try {
    		returnInt = userTaskDAO.addMsg(parmMap);
		} catch (Exception e) {
			logger.error("UserTaskServiceImpl---addMsg---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int deleteMsg(Map parmMap) {
		int returnInt = 0;
    	try {
    		returnInt = userTaskDAO.deleteMsg(parmMap);
		} catch (Exception e) {
			logger.error("UserTaskServiceImpl---deleteMsg---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}
}
