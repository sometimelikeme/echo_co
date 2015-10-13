package echo.sp.app.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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
    			parmMap.put("TASK_BID_STATUS", "10");
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
			// 修改任务状态选定他人中标任务，加入中标时间戳
			parmMap.put("TASK_GET_TIME", DateUtils.getDateTime());
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

	@Override
	public int updateTaskDone(Map parmMap) {
		int returnInt = 0;
    	try {
    		userTaskActionDAO.updateTaskDone(parmMap);
    		userTaskActionDAO.updateTaskLineDone(parmMap);
    		returnInt = 1;
    	} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int updateTaskFinish(Map parmMap) {
		int returnInt = 0;
    	try {
    		// 结束任务
    		userTaskActionDAO.updateTaskFinish(parmMap);
    		// 奖励将本次任务金额
    		String perOfSystemPaid = Prop.getString("system.perOfSystemPaid");
    		BigDecimal payment = new BigDecimal(parmMap.get("TASK_TOTAL_PAID").toString());
    		if (parmMap.get("RETURN_MONEY") == null) {// 退款到任务发布者不收取费用
    			payment = payment.subtract(payment.multiply(
    					new BigDecimal(perOfSystemPaid)).divide(
    					new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
			}
			parmMap.put("GET_PAID", payment);
			userTaskActionDAO.updateTaskLineFinish(parmMap);
			// 用户金额消费明细表参数集-增
			Map tranMap = new HashMap();
			tranMap.put("USER_ID", parmMap.get("BIDE_USER_ID"));
			tranMap.put("TIME1", parmMap.get("TASK_FINISH_TIME"));
			tranMap.put("DATE1", DateUtils.getToday());
			tranMap.put("MONEY_NUM", payment);
			tranMap.put("TASK_ID", parmMap.get("TASK_ID"));
			tranMap.put("ORDER_ID", "");
			tranMap.put("ABLI_ORDER_ID", "");
			tranMap.put("PRE_PAID_ID", "");
			tranMap.put("STATUS", "10");// 增
			tranMap.put("MN_TYPE", "10");// 任务
			userDAO.insertUserMoneyRecord(tranMap);
			// 修改用户金额-增
			BigDecimal total_money_Big = new BigDecimal(userDAO.getUserExpandInfo(tranMap).get("TOTAL_MONEY").toString());
			tranMap.put("TOTAL_MONEY", total_money_Big.add(payment));
			userDAO.updateUserMoney(tranMap);
			// 系统账户金额消费明细表参数集
			tranMap.put("USER_ID", Prop.getString("system.systemAdminId"));// 系统账号
			tranMap.put("STATUS", "20");// 减
			userDAO.insertUserMoneyRecord(tranMap);
			// 将付款暂存到系统账户-减
			total_money_Big =  new BigDecimal(userDAO.getUserExpandInfo(tranMap).get("TOTAL_MONEY").toString());
			tranMap.put("TOTAL_MONEY", total_money_Big.subtract(payment));
			userDAO.updateUserMoney(tranMap);
		} catch (Exception e) {
			logger.error("UserTaskActionServiceImpl---updateTaskFinish--error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}
	
	@Override
	public int updateTaskFinishPoint(Map parmMap) {
		int returnInt = 0;
    	try {
    		// 结束任务
    		userTaskActionDAO.updateTaskFinish(parmMap);
    		// 奖励将本次任务积分
    		String payment = parmMap.get("TASK_TOTAL_PAID").toString();
			parmMap.put("GET_PAID", payment);
			userTaskActionDAO.updateTaskLineFinish(parmMap);
			// 用户积分消费明细表参数集-增
			Map tranMap = new HashMap();
			tranMap.put("USER_ID", parmMap.get("BIDE_USER_ID"));
			tranMap.put("TIME1", parmMap.get("TASK_FINISH_TIME"));
			tranMap.put("DATE1", DateUtils.getToday());
			tranMap.put("POINT_NUM", payment);
			tranMap.put("TASK_ID", parmMap.get("TASK_ID"));
			tranMap.put("ORDER_ID", "");
			tranMap.put("ABLI_ORDER_ID", "");
			tranMap.put("AWARD_ID", "");
			tranMap.put("STATUS", "10");// 增
			tranMap.put("MN_TYPE", "10");// 任务
			userDAO.insertUserPointRecord(tranMap);
			// 修改用户总积分-增
			BigDecimal total_money_Big = new BigDecimal(userDAO.getUserExpandInfo(tranMap).get("TOTAL_POINT").toString());
			tranMap.put("TOTAL_POINT", total_money_Big.add(new BigDecimal(payment)));
			userDAO.updateUserPoint(tranMap);
		} catch (Exception e) {
			logger.error("UserTaskActionServiceImpl---updateTaskFinishPoint--error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int updateTaskUnFinish(Map parmMap) {
		return userTaskActionDAO.updateTaskUnFinish(parmMap);
	}

	@Override
	public List getUnProcessTasks(Map parmMap) {
		return userTaskActionDAO.getUnProcessTasks(parmMap);
	}
	
}
