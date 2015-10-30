package echo.sp.app.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import echo.sp.app.command.page.PubTool;
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
    		// 汇总评论量和综合评分
    		Map resMap = userAblityDAO.getAbliCommentInfo(parmMap);
    		BigDecimal commmentCountBig = new BigDecimal(resMap.get("ABLI_COMMMENT").toString());
    		BigDecimal newCountBig = commmentCountBig.add(new BigDecimal("1"));
    		BigDecimal thisPoint = new BigDecimal(parmMap.get("TOTAL_POINT").toString());
    		BigDecimal abliPoint = new BigDecimal(resMap.get("ABLI_POINT").toString());
			abliPoint = (abliPoint.multiply(commmentCountBig).add(thisPoint))
					.divide(newCountBig, 2, BigDecimal.ROUND_HALF_UP);
			resMap.put("ABLI_COMMMENT", newCountBig);
			resMap.put("ABLI_POINT", abliPoint);
			resMap.put("ABLI_ID", parmMap.get("ABLI_ID"));
			userAblityDAO.updateAbliCommentInfo(resMap);
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
    			userAblityDAO.updateAbliBuyCount(parmMap);
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
			logger.error("UserOrderServiceImpl---addBuyAbility---interface error: ",e);
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

	@Override
	public int updateDeclineContract(Map parmMap) {
		int returnInt = 0;
    	try {
    		userAblityDAO.updateDeclineContract(parmMap);
    		// 返还金额到用户账户
    		Map resMap = userAblityDAO.getAbliOrderById(parmMap);
    		returnMoney(resMap);
    		returnInt = 1;
		} catch (Exception e) {
			logger.error("UserAblityServiceImpl---updateDeclineContract---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int updateConfirmContract(Map parmMap) {
		int returnInt = 0;
    	try {
    		userAblityDAO.updateConfirmContract(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			logger.error("UserAblityServiceImpl---updateConfirmContract---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int updateDoneAbility(Map parmMap) {
		int returnInt = 0;
    	try {
    		userAblityDAO.updateDoneAbility(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			logger.error("UserAblityServiceImpl---updateDoneAbility---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int updateConfirmDone(Map parmMap) {
		int returnInt = 0;
    	try {
    		String dateTime = DateUtils.getDateTime();
    		parmMap.put("CONFIRM_TIME", dateTime);
    		if (userAblityDAO.updateConfirmDone(parmMap) > 0) {
    			// 获取本次订单金额
    			Map orderMap = userAblityDAO.getAbliOrderById(parmMap);
    			BigDecimal payment = new BigDecimal(orderMap.get("TOTAL_PAY").toString());
    			String perOfSystemPaid = Prop.getString("system.perOfSystemPaidAbli");// 平台抽取比
    			payment = payment.subtract(payment.multiply(
    					new BigDecimal(perOfSystemPaid)).divide(
    					new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
				// 用户金额明细表参数集
				Map tranMap = new HashMap();
				tranMap.put("USER_ID", parmMap.get("ABLI_USER_ID"));
				tranMap.put("TIME1", dateTime);
				tranMap.put("DATE1", DateUtils.getToday());
				tranMap.put("MONEY_NUM", payment);
				tranMap.put("MN_TYPE", "30");
				tranMap.put("TASK_ID", "");
				tranMap.put("ORDER_ID", "");
				tranMap.put("ABLI_ORDER_ID", parmMap.get("ABLI_ORDER_ID").toString());
				tranMap.put("PRE_PAID_ID", "");
				tranMap.put("STATUS", "10");// 增
				// 产生用户金额消费记录
				userDAO.insertUserMoneyRecord(tranMap);
				// 修改用户金额-增
				BigDecimal total_money_Big = new BigDecimal(userDAO.getUserExpandInfo(tranMap).get("TOTAL_MONEY").toString());
				tranMap.put("TOTAL_MONEY", total_money_Big.add(payment));
				userDAO.updateUserMoney(tranMap);
				// 从系统账户减除
				tranMap.put("USER_ID", Prop.getString("system.systemAdminId"));// 系统账号
				tranMap.put("STATUS", "20");// 减
				userDAO.insertUserMoneyRecord(tranMap);
				total_money_Big =  new BigDecimal(userDAO.getUserExpandInfo(tranMap).get("TOTAL_MONEY").toString());
				tranMap.put("TOTAL_MONEY", total_money_Big.subtract(payment));
				userDAO.updateUserMoney(tranMap);
				returnInt = 1;
    		}
		} catch (Exception e) {
			logger.error("UserOrderServiceImpl---updateConfirmDone---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int updateConfirmUnDone(Map parmMap) {
		int returnInt = 0;
    	try {
    		userAblityDAO.updateConfirmUnDone(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			logger.error("UserAblityServiceImpl---updateConfirmUnDone---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int deleteOrder(Map parmMap) {
		int returnInt = 0;
    	try {
    		userAblityDAO.deleteOrder(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			logger.error("UserAblityServiceImpl---deleteOrder---interface error: ",e);
			throw new RuntimeException(); 
		}
		return returnInt;
	}

	@Override
	public int updateProcessDeadOrders(Map parmMap) {
		int returnInt = 0;
    	try {
    		// 获取订单列表
    		parmMap.put("CURR_DATE", DateUtils.getDateTime());
    		List resList = userAblityDAO.getUnProcessOrders(parmMap);
    		if (PubTool.isListHasData(resList)) {
				String status;
				int resListLength = resList.size();
				Map orderMap;
				for (int i = 0; i < resListLength; i++) {
					orderMap = (Map)resList.get(i);
					status = orderMap.get("STATUS").toString();
					if ("10".equals(status) || "60".equals(status)) {
						returnMoney(orderMap);
					} else if ("40".equals(status)) {
						orderMap.put("USER_ID", userAblityDAO.getAbilityUserId(orderMap));
						giveMoney(parmMap);
					}
				}
			}
    		returnInt = 1;
		} catch (Exception e) {
			logger.error("UserAblityServiceImpl---updateProcessDeadOrders---interface error: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}
	
	/**
	 * 返还金额到用户
	 * @param parmMap
	 */
	private void returnMoney(Map parmMap){
		BigDecimal payment = new BigDecimal(parmMap.get("TOTAL_PAY").toString());
		String dateTime = DateUtils.getDateTime();
		// 用户金额消费明细表参数集-增
		Map tranMap = new HashMap();
		tranMap.put("USER_ID", parmMap.get("USER_ID"));
		tranMap.put("TIME1", dateTime);
		tranMap.put("DATE1", DateUtils.getToday());
		tranMap.put("MONEY_NUM", payment);
		tranMap.put("TASK_ID", "");
		tranMap.put("ORDER_ID", "");
		tranMap.put("ABLI_ORDER_ID", parmMap.get("ABLI_ORDER_ID"));
		tranMap.put("PRE_PAID_ID", "");
		tranMap.put("STATUS", "10");// 增
		tranMap.put("MN_TYPE", "30");// 技能
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
	 * 奖励金额
	 * @param parmMap
	 */
	private void giveMoney(Map parmMap){
		BigDecimal payment = new BigDecimal(parmMap.get("TOTAL_PAY").toString());
		String perOfSystemPaid = Prop.getString("system.perOfSystemPaidAbli");// 平台抽取比
		payment = payment.subtract(payment.multiply(
				new BigDecimal(perOfSystemPaid)).divide(
				new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
		parmMap.put("TOTAL_PAY", payment);
		returnMoney(parmMap);
	}
}
