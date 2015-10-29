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
import echo.sp.app.dao.UserAblityDAO;
import echo.sp.app.dao.UserDAO;
import echo.sp.app.service.UserAblityService;

/**   
 * 用户技能服务
 * @author Ethan   
 * @date 2015年10月24日 
 */
@Service
public class UserAblityServiceImpl implements UserAblityService {

	private static final Logger logger = LoggerFactory.getLogger(UserAblityServiceImpl.class);
	
	@Autowired
	private UserAblityDAO userAblityDAO;
	
	@Autowired
	private UserDAO userDAO;

	@Override
	public int addAblity(Map parmMap) {
		int returnInt = 0;
    	try {
    		userAblityDAO.addAblity(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			logger.error("UserAblityServiceImpl---addAblity---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int updateAblity(Map parmMap) {
		int returnInt = 0;
    	try {
    		userAblityDAO.updateAblity(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			logger.error("UserAblityServiceImpl---updateAblity---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int deleteAblity(Map parmMap) {
		int returnInt = 0;
    	try {
    		userAblityDAO.deleteAblity(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			logger.error("UserAblityServiceImpl---deleteAblity---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;	
	}

	@Override
	public Map searchAblityById(Map parmMap) {
		return userAblityDAO.searchAblityById(parmMap);
	}

	@Override
	public Map getCommentById(Map parmMap) {
		return userAblityDAO.getCommentById(parmMap);
	}

	@Override
	public int addComment(Map parmMap) {
		int returnInt = 0;
    	try {
    		userAblityDAO.delComment(parmMap);
    		userAblityDAO.addComment(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			logger.error("UserAblityServiceImpl---addComment---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int delComment(Map parmMap) {
		int returnInt = 0;
    	try {
    		userAblityDAO.delComment(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			logger.error("UserAblityServiceImpl---delComment---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	/**
	 * 增加技能购买订单
	 * 减少用户金额
	 * 增加系统账户金额
	 */
	@Override
	public int addBuyAbility(Map parmMap) {
		int returnInt = 0;
    	try {
    		String dateTime = DateUtils.getDateTime();
    		parmMap.put("PAY_TIME", dateTime);
    		parmMap.put("STATUS", "10");
    		parmMap.put("PAY_TYPE", "50");
    		if (userAblityDAO.addBuyAbility(parmMap) > 0) {
    			BigDecimal payment = new BigDecimal(parmMap.get("TOTAL_PAY").toString());
				// 用户金额消费明细表参数集
				Map tranMap = new HashMap();
				tranMap.put("USER_ID", parmMap.get("USER_ID"));
				tranMap.put("TIME1", dateTime);
				tranMap.put("DATE1", DateUtils.getToday());
				tranMap.put("MONEY_NUM", payment);
				tranMap.put("MN_TYPE", "30");
				tranMap.put("TASK_ID", "");
				tranMap.put("ORDER_ID", "");
				tranMap.put("ABLI_ORDER_ID", parmMap.get("ABLI_ORDER_ID").toString());
				tranMap.put("PRE_PAID_ID", "");
				tranMap.put("STATUS", "20");// 减
				// 产生用户金额消费记录
				userDAO.insertUserMoneyRecord(tranMap);
				// 修改用户金额-减
				BigDecimal total_money_Big = new BigDecimal(parmMap.get("TOTAL_MONEY").toString());
				tranMap.put("TOTAL_MONEY", total_money_Big.subtract(payment));
				userDAO.updateUserMoney(tranMap);
				// 将付款暂存到系统账户-增
				tranMap.put("USER_ID", Prop.getString("system.systemAdminId"));// 系统账号
				tranMap.put("STATUS", "10");// 增
				userDAO.insertUserMoneyRecord(tranMap);
				total_money_Big =  new BigDecimal(userDAO.getUserExpandInfo(tranMap).get("TOTAL_MONEY").toString());
				tranMap.put("TOTAL_MONEY", total_money_Big.add(payment));
				userDAO.updateUserMoney(tranMap);
				returnInt = 1;
    		}
		} catch (Exception e) {
			logger.error("UserOrderServiceImpl---addMallOrder---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public Map getAbliOrderById(Map parmMap) {
		Map resMap = new HashMap();
		resMap.put("ABLI_ORDER", userAblityDAO.getAbliOrderById(parmMap));
		resMap.put("ABLI_INFO", userAblityDAO.searchAblityById(parmMap));
		return resMap;
	}
	
}
