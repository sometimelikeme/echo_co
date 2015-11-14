package echo.sp.app.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import echo.sp.app.command.utils.DateUtils;
import echo.sp.app.command.utils.Prop;
import echo.sp.app.dao.UserDAO;
import echo.sp.app.dao.UserMulTaskActionDAO;
import echo.sp.app.service.UserMulTaskActionService;

/**   
 * @author Ethan   
 * @date 2015年10月22日 
 */
@Service
public class UserMulTaskActionServiceImpl implements UserMulTaskActionService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserMulTaskActionServiceImpl.class);
	
	@Autowired
	private UserMulTaskActionDAO userMulTaskActionDAO;
	
	@Autowired
	private UserDAO userDAO;

	@Override
	public int addTask(Map parmMap) {
		int returnInt = 0;
    	try {
    		// 发布任务
    		userMulTaskActionDAO.addTask(parmMap);
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
			logger.error("UserMulTaskActionServiceImpl---addTask---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public Map getTaskInfoByTaskId(Map parmMap) {
		Map resMap = userMulTaskActionDAO.getTaskHeadByTaskId(parmMap);
		resMap.put("is_bid_status", userMulTaskActionDAO.getBidInfo(parmMap));
		resMap.put("TASK_LINE", userMulTaskActionDAO.getTaskLineByTaskId(parmMap));
		return resMap;
	}

	@Override
	public int updateTask(Map parmMap) {
		return userMulTaskActionDAO.updateTask(parmMap);
	}

	@Override
	public int deleteTask(Map parmMap) {
		return userMulTaskActionDAO.deleteTask(parmMap);
	}

	@Override
	public int addTaskBider(Map parmMap) {
		int returnInt = 0;
    	try {
    		userMulTaskActionDAO.addTaskBider(parmMap);
    		userMulTaskActionDAO.updateTaskBider(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			logger.error("UserMulTaskActionServiceImpl---addTaskBider---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int updateTaskDone(Map parmMap) {
		int returnInt = 0;
    	try {
    		userMulTaskActionDAO.updateTaskDone(parmMap);
    		userMulTaskActionDAO.updateTaskLineDone(parmMap);
    		returnInt = 1;
    	} catch (Exception e) {
    		logger.error("UserMulTaskActionServiceImpl---updateTaskDone---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int updateTaskUnFinish(Map parmMap) {
		return userMulTaskActionDAO.updateTaskUnFinish(parmMap);
	}

	@Override
	public Map getTaskInfoByTaskUserId(Map parmMap) {
		return userMulTaskActionDAO.getTaskInfoByTaskUserId(parmMap);
	}

	@Override
	public List getUnProcessTasks(Map parmMap) {
		return userMulTaskActionDAO.getUnProcessTasks(parmMap);
	}

	@Override
	public int updateTaskClose(Map parmMap) {
		return userMulTaskActionDAO.updateTaskClose(parmMap);
	}

	@Override
	public List getDoneTaskers(Map parmMap) {
		return userMulTaskActionDAO.getDoneTaskers(parmMap);
	}

	@Override
	public int updateTaskProcess(List taskList) {
		int returnInt = 0;
    	try {
    		Iterator iterList = taskList.iterator();
    		Map paramMap;
    		String task_type;// 任务类型
    		int done_num = 0;// 完成人数
    		int need_num = 0;// 需求人数
    		List bideList;// 获取完成任务用户列表
    		int bideListLength;
    		String perOfSystemPaid = Prop.getString("system.perOfSystemPaid");// 平台抽取比
    		BigDecimal payment;// 奖励金额
    		BigDecimal backment;// 返还金额
    		String datetime = DateUtils.getDateTime();
    		
    		while (iterList.hasNext()) {
    			paramMap = (Map)iterList.next();
    			paramMap.put("TASK_FINISH_TIME", datetime);
    			// 关闭任务
    			userMulTaskActionDAO.updateTaskClose(paramMap);
    			task_type = paramMap.get("TASK_TYPE").toString();
    			done_num = Integer.parseInt(paramMap.get("TASK_DONE_NUM").toString());
    			need_num = Integer.parseInt(paramMap.get("TASK_NEED_NUM").toString());
    			if (done_num > 0) {
    				// 奖励任务
    				// 计算单份任务奖励
    				// 计算用户返还金额
    	    		payment = new BigDecimal(paramMap.get("TASK_SING_PAID").toString());
    	    		backment = new BigDecimal(paramMap.get("TASK_TOTAL_PAID").toString());
    	    		backment = backment.subtract(payment.multiply(new BigDecimal(done_num)));
    	    		if ("10".equals(task_type)) {// 金额任务收取手续费
            			payment = payment.subtract(payment.multiply(
            					new BigDecimal(perOfSystemPaid)).divide(
            					new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
					}
        			paramMap.put("DONE_PAID", payment);// 应该奖励给完成任务者的金额
        			paramMap.put("BACK_PAID", backment);// 应该返还为发布任务者的金额
    				// 获取完成任务用户列表
    				bideList = userMulTaskActionDAO.getDoneTaskers(paramMap);
    				if (bideList != null) {
    					bideListLength = bideList.size();
        				for (int i = 0; i < bideListLength; i++) {
        					// 修改T_MUL_TASKS_LINE
        					paramMap.put("BIDE_USER_ID", ((Map)bideList.get(i)).get("BIDE_USER_ID"));
        					userMulTaskActionDAO.updateTaskLineClose(paramMap);
        					// 产生交易记录
        					if ("10".equals(task_type)) {// 金额任务
        						takserMoneyRecord(paramMap);
    						} else if ("20".equals(task_type)) {// 积分任务
    							takserPointRecord(paramMap);
    						}
        				}
        				// 将未完成的任务金额返还发布任务者
        				if (done_num < need_num && "10".equals(task_type)) {// 只回退金额任务
        					paramMap.put("IS_BACK", 1);
        					// 将返回的金额补充到发布任务者
        					paramMap.put("DONE_PAID", backment);
        					// 产生交易记录
        					takserMoneyRecord(paramMap);
        				}
					}
    			} else {
    				if ("10".equals(task_type)) {
    					// 回退奖励
        				paramMap.put("IS_BACK", 1);
    					// 将返回的金额补充到发布任务者
    					paramMap.put("DONE_PAID", paramMap.get("TASK_TOTAL_PAID"));
    					// 产生交易记录
    					takserMoneyRecord(paramMap);
					}
    			}
    			
    		}
    		returnInt = 1;
    	} catch (Exception e) {
    		logger.error("UserMulTaskActionServiceImpl---updateTaskDone---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}
	
	/**
	 * 任务完成后奖励金额
	 * @param parmMap
	 */
	private void takserMoneyRecord(Map parmMap){
		BigDecimal payment = new BigDecimal(parmMap.get("DONE_PAID").toString());
		// 用户金额消费明细表参数集-增
		Map tranMap = new HashMap();
		tranMap.put("USER_ID", "1".equals((parmMap.get("IS_BACK").toString())) ? parmMap.get("USER_ID") : parmMap.get("BIDE_USER_ID"));
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
	}
	
	/**
	 * 任务完成后奖励金币
	 * @param parmMap
	 */
	private void takserPointRecord(Map parmMap){
		BigDecimal payment = new BigDecimal(parmMap.get("DONE_PAID").toString());
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
		tranMap.put("TOTAL_POINT", total_money_Big.add(payment));
		userDAO.updateUserPoint(tranMap);
	}

	@Override
	public int judgeMulTaskDead(Map parmMap) {
		return userMulTaskActionDAO.judgeMulTaskDead(parmMap);
	}
}
