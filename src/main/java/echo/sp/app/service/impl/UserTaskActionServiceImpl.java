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
import echo.sp.app.dao.UserTaskActionDAO;
import echo.sp.app.service.UserTaskActionService;

/** 
 * 用户任务流程  
 * @author Ethan   
 * @date 2015年10月4日 
 */
@Service
public class UserTaskActionServiceImpl implements UserTaskActionService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserTaskActionServiceImpl.class);
	
	@Autowired
	private UserTaskActionDAO userTaskActionDAO;
	
	@Autowired
	private UserDAO userDAO;

	@Override
	public int addTaskBider(Map parmMap) {
		int returnInt = 0;
    	try {
    		userTaskActionDAO.addTaskBider(parmMap);
    		userTaskActionDAO.updateTaskBider(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int updateTaskBider(Map parmMap) {
		int returnInt = 0;
    	try {
    		userTaskActionDAO.updateCancelTaskBider(parmMap);
    		int bid_num = Integer.parseInt(parmMap.get("BID_NUM").toString());
    		if (Integer.parseInt(parmMap.get("BID_NUM").toString()) <= 1) {
    			bid_num = 0;
    			parmMap.put("TASK_STATUS", "10");
			} else {
				bid_num -= 1;
			}
    		parmMap.put("BID_NUM", bid_num);
    		userTaskActionDAO.updateTaskStatus(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int updateChooseTasker(Map parmMap) {
		int returnInt = 0;
    	try {
    		// 将用户账户余额减去此次任务费用, 生成用户账户交易记录
    		userDAO.updateUserMoney(parmMap);
    		String dateTime = DateUtils.getDateTime();
    		String date = DateUtils.getToday();
    		BigDecimal task_paid_Big = new BigDecimal(parmMap.get("TASK_PAID").toString());
    		parmMap.put("TIME1", dateTime);
    		parmMap.put("DATE1", date);
    		parmMap.put("MONEY_NUM", task_paid_Big);
    		parmMap.put("STATUS", "0");
    		parmMap.put("ORDER_ID", "");
    		parmMap.put("ABLI_ORDER_ID", "");
    		parmMap.put("PRE_PAID_ID", "");
    		userDAO.insertUserMoneyRecord(parmMap);
			// 将此次费用增加到系统账户, 生成系统账户交易记录
    		Map tMap = new HashMap();
    		tMap.put("USER_ID", Prop.getString("system.systemAccountId"));
    		BigDecimal total_money_Big = new BigDecimal(userDAO.getUserExpandInfo(tMap).get("TOTAL_MONEY").toString());
			tMap.put("TOTAL_MONEY", total_money_Big.add(task_paid_Big));
			userDAO.updateUserMoney(tMap);
			tMap.put("TIME1", dateTime);
			tMap.put("DATE1", date);
			tMap.put("MONEY_NUM", task_paid_Big);
			tMap.put("TASK_ID", parmMap.get("TASK_ID"));
			tMap.put("ORDER_ID", "");
			tMap.put("ABLI_ORDER_ID", "");
			tMap.put("PRE_PAID_ID", "");
			tMap.put("STATUS", "1");
			// 修改任务状态选定他人中标任务，加入中标时间戳
			parmMap.put("TASK_GET_TIME", dateTime);
			userTaskActionDAO.updateTaskStatusForSec(parmMap);
			// 修改投标人TASK_IS_BIDE为1
    		userTaskActionDAO.updateBiderStatus(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int updateBiderBackTask(Map parmMap) {
		return userTaskActionDAO.updateBiderBackTask(parmMap);
	}

	@Override
	public int updatePuberBackTask(Map parmMap) {
		return userTaskActionDAO.updatePuberBackTask(parmMap);
	}
	
}
