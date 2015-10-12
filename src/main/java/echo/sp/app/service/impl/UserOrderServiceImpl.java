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
import echo.sp.app.command.utils.RandomUtil;
import echo.sp.app.dao.MerItemDAO;
import echo.sp.app.dao.MerOrderDAO;
import echo.sp.app.dao.UserDAO;
import echo.sp.app.dao.UserOrderDAO;
import echo.sp.app.service.UserOrderService;

/**   
 * 用户订单
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Service
public class UserOrderServiceImpl implements UserOrderService{
	
	private static final Logger logger = LoggerFactory.getLogger(UserOrderServiceImpl.class);
	
	@Autowired
    private UserOrderDAO userOrderDAO;
	
	@Autowired
	private MerItemDAO merItemDAO;
	
	@Autowired
	private MerOrderDAO merOrderDAO;
	
	@Autowired
	private UserDAO userDAO;

	/**
	 * 执行订单头表和行表
	 */
	@Override
	public int addOrder(Map parmMap) {
		int returnInt = 0;
    	try {
    		if (userOrderDAO.addOrderHead((Map)parmMap.get("head")) > 0) {
    			List lineList = (List)parmMap.get("line");
    			if (userOrderDAO.addOrderLine(lineList) > 0) {
    				// if (userOrderDAO.modifyItemQty(lineList) > 0) {
    					returnInt = 1;
					// }
    			};
    		}
		} catch (Exception e) {
			//  默认spring事务只在发生未被捕获的 runtimeexcetpion时才回滚
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public Map getOrderDetail(Map parmMap) {
		
		Map resMap = new HashMap();
		
		resMap.put("HEAD", userOrderDAO.getOrderHead(parmMap));
		resMap.put("LINE", merOrderDAO.getOrderDetailByOrderId(parmMap));
		
		return resMap;
		
	}

	@Override
	public int updateOrderForCancel(Map parmMap) {
		return userOrderDAO.updateOrderForCancel(parmMap);
	}

	/**
	 * 调用第三方支付
	 */
	@Override
	public int updateOrderPay(Map parmMap) {
		int returnInt = 0;
    	try {
    		Map payLogMap = (Map)parmMap.get("payLogMap");// 支付信息日志主表参数
    		Map payMap = (Map)parmMap.get("payMap");// 第三方支付返回的支付信息
    		Map upMap = (Map)parmMap.get("upMap");// 更新订单信息
    		
    		// Step1: 保存第三方支付返回的支付信息的数据库（包含支付和退款信息）
    		String pay_type = payLogMap.get("PAY_TYPE").toString();
    		if ("10".equals(pay_type)) {
				userOrderDAO.insertToAliLog(payMap);
			} else if ("20".equals(pay_type)) {
				userOrderDAO.insertToWxLog(payMap);
			} else if ("30".equals(pay_type)) {
				userOrderDAO.insertToUnLog(payMap);  
			}
    		
    		// Step2: 支付信息日志主表参数
    		userOrderDAO.insertToPayLog(payLogMap);
    		
    		// Step3: 更新订单信息，修改支付和退款状态
    		String statusPay = upMap.get("STATUS").toString();
    		if ("30".equals(statusPay)) {
    			userOrderDAO.updateOrderPay(upMap);
			} else {
				userOrderDAO.updateOrderPayBack(upMap);
			}
    		
    		// Step4: 处理库存
    		processItemInventory(upMap, statusPay);
    		
			returnInt = 1; 
			
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public Map getPrePayInfoById(Map parmMap) {
		return userOrderDAO.getPrePayInfoById(parmMap);
	}

	/**
	 * 平台充值
	 */
	@Override
	public int insertPrePayInfo(Map parmMap) {
		int returnInt = 0;
    	try {
    		Map prePayMap = (Map)parmMap.get("prePayMap");// 用户预支付金额记录表
    		Map payMap = (Map)parmMap.get("payMap");// 第三方支付返回的支付信息
    		
    		// Step1: 保存第三方支付返回的支付信息的数据库（包含支付和退款信息）
    		String pay_type = prePayMap.get("PAY_TYPE").toString();
    		if ("10".equals(pay_type)) {
				userOrderDAO.insertToAliLog(payMap);
			} else if ("20".equals(pay_type)) {
				userOrderDAO.insertToWxLog(payMap);
			} else if ("30".equals(pay_type)) {
				userOrderDAO.insertToUnLog(payMap);  
			}
    		
    		// Step2: 将支付记录保存到用户预支付金额记录表
    		userOrderDAO.insertPrePayInfo(prePayMap);
    		
    		// Step3: 将支付记录信息保存到用户金钱交易日志表
    		// 用户金额消费明细表参数集
			Map tranMap = new HashMap();
			tranMap.put("USER_ID", prePayMap.get("USER_ID"));
			tranMap.put("TIME1", DateUtils.getDateTime());
			tranMap.put("DATE1", DateUtils.getToday());
			tranMap.put("MONEY_NUM", prePayMap.get("PAYMENT"));
			tranMap.put("TASK_ID", "");
			tranMap.put("ORDER_ID", "");
			tranMap.put("MN_TYPE", "40");
			tranMap.put("ABLI_ORDER_ID", "");
			tranMap.put("PRE_PAID_ID", prePayMap.get("PRE_PAID_ID"));
			tranMap.put("STATUS", "10");
			userDAO.insertUserMoneyRecord(tranMap);
    		
    		// Step4: 更新用户可消费金额T_USERS_EXPAND
			BigDecimal payment = new BigDecimal(prePayMap.get("PAYMENT").toString());
			BigDecimal total_money_Big = new BigDecimal(userDAO.getUserExpandInfo(prePayMap).get("TOTAL_MONEY").toString());
			prePayMap.put("TOTAL_MONEY", total_money_Big.add(payment));
			userDAO.updateUserMoney(prePayMap);
    		
    		returnInt = 1;
    	} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int updatePayAction(Map parmMap) {
		int returnInt = 0;
		try {
			// 产生用户金额消费记录
			userDAO.insertUserMoneyRecord(parmMap);
			
			// 修改用户金额-减
			BigDecimal payment = new BigDecimal(parmMap.get("MONEY_NUM").toString());
			BigDecimal total_money_Big = new BigDecimal(parmMap.get("TOTAL_MONEY").toString());
			parmMap.put("TOTAL_MONEY", total_money_Big.subtract(payment));
			userDAO.updateUserMoney(parmMap);
			
			// 将付款暂存到系统账户-增
			parmMap.put("USER_ID", Prop.getString("system.systemAdminId"));// 系统账号
			parmMap.put("STATUS", "10");// 增
			userDAO.insertUserMoneyRecord(parmMap);
			total_money_Big =  new BigDecimal(userDAO.getUserExpandInfo(parmMap).get("TOTAL_MONEY").toString());
			parmMap.put("TOTAL_MONEY", total_money_Big.add(payment));
			userDAO.updateUserMoney(parmMap);
			
			// 修改订单状态-付款
			Map upMap = new HashMap();
			upMap.put("ORDER_ID", parmMap.get("ORDER_ID"));
			upMap.put("STATUS", "30");
			upMap.put("PAY_TYPE", "50");
			upMap.put("CAPTCHA", RandomUtil.generateString(6));
			upMap.put("PAY_TIME", DateUtils.getDateTime());
			userOrderDAO.updateOrderPay(upMap);
			
			// 处理库存
			processItemInventory(upMap, "30");
			
			returnInt = 1;
		} catch (Exception e) {
			logger.error("UserOrderServiceImpl---updatePayAction: ",e);
			throw new RuntimeException();
		}
		return returnInt;
	}
	
	
	@Override
	public int updateBackPayAction(Map parmMap) {
		int returnInt = 0;
		try {
			// 产生用户金额消费记录
			userDAO.insertUserMoneyRecord(parmMap);
			
			// 修改用户金额-增
			BigDecimal payment = new BigDecimal(parmMap.get("MONEY_NUM").toString());
			BigDecimal total_money_Big = new BigDecimal(parmMap.get("TOTAL_MONEY").toString());
			parmMap.put("TOTAL_MONEY", total_money_Big.add(payment));
			userDAO.updateUserMoney(parmMap);
			
			// 将付款暂存到系统账户-减
			parmMap.put("USER_ID", Prop.getString("system.systemAdminId"));// 系统账号
			parmMap.put("STATUS", "20");// 减
			userDAO.insertUserMoneyRecord(parmMap);
			total_money_Big =  new BigDecimal(userDAO.getUserExpandInfo(parmMap).get("TOTAL_MONEY").toString());
			parmMap.put("TOTAL_MONEY", total_money_Big.subtract(payment));
			userDAO.updateUserMoney(parmMap);
			
			// 修改订单状态-退款
			Map upMap = new HashMap();
			upMap.put("ORDER_ID", parmMap.get("ORDER_ID"));
			upMap.put("STATUS", "40");
			upMap.put("BACK_TIME", DateUtils.getDateTime());
			userOrderDAO.updateOrderPayBack(upMap);
			
			// 处理库存
			processItemInventory(upMap, "40");
			
			returnInt = 1;
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}
	
	
	/**
	 * 处理库存 
	 * 其中包含库存的扣减和增加
	 * 获取订单行表商品信息
	 * 汇总库存和销量
	 */
	private void processItemInventory(Map upMap, String statusPay){
		List itemList = merOrderDAO.getOrderDetailByOrderId(upMap);
		Map temMap;
		Map resMap;
		BigDecimal inventory_de;// 库存量
		BigDecimal qty_sold_de;// 总销量
		BigDecimal qty_sold;// 本次销量
		for (int i = 0; i < itemList.size(); i++) {
			temMap = (Map) itemList.get(i);
			// 获取每个商品的当前库存和销量
			resMap = merItemDAO.getItemInvtentory(temMap);
			inventory_de = new BigDecimal(resMap.get("INVENTORY").toString());// 当前库存
			qty_sold_de = new BigDecimal(resMap.get("QTY_SOLD").toString());// 总销量
			qty_sold = new BigDecimal(temMap.get("QTY_SOLD").toString());// 本次销量
			if ("30".equals(statusPay)) {// 付款
				inventory_de = inventory_de.subtract(qty_sold);
				qty_sold_de = qty_sold_de.add(qty_sold);
			} else {// 退款
				inventory_de = inventory_de.add(qty_sold);
				qty_sold_de = qty_sold_de.subtract(qty_sold);
			}
			temMap.put("INVENTORY", inventory_de);
			temMap.put("QTY_SOLD_REAL", qty_sold_de);
		}
		userOrderDAO.modifyItemQty(itemList);
	}

	@Override
	public int addMallOrder(Map parmMap) {
		int returnInt = 0;
    	try {
    		Map headMap = (Map)parmMap.get("head");
    		String dateTime = DateUtils.getDateTime();
    		headMap.put("ORDER_ALIAS_ID", RandomUtil.generateNumString(12));
    		headMap.put("CAPTCHA", RandomUtil.generateString(6));
    		headMap.put("ORDER_TIME", dateTime);
    		if (userOrderDAO.addMallOrderHead(headMap) > 0) {
    			List lineList = (List)parmMap.get("line");
    			if (userOrderDAO.addOrderLine(lineList) > 0) {
    				// 用户积分消费明细表参数集
    				Map tranMap = new HashMap();
    				tranMap.put("USER_ID", headMap.get("USER_ID"));
    				tranMap.put("TIME1", dateTime);
    				tranMap.put("DATE1", DateUtils.getToday());
    				tranMap.put("POINT_NUM", headMap.get("TOTAL_PAY").toString());
    				tranMap.put("MN_TYPE", "20");
    				tranMap.put("TASK_ID", "");
    				tranMap.put("ORDER_ID", headMap.get("ORDER_ID"));
    				tranMap.put("ABLI_ORDER_ID", "");
    				tranMap.put("AWARD_ID", "");
    				tranMap.put("STATUS", "20");// 减
    				// 产生用户积分消费记录
    				userDAO.insertUserPointRecord(tranMap);
    				// 修改用户积分-减
    				BigDecimal payment = new BigDecimal(parmMap.get("TOTAL_PAY").toString());
    				BigDecimal total_money_Big = new BigDecimal(parmMap.get("TOTAL_POINT").toString());
    				tranMap.put("TOTAL_POINT", total_money_Big.subtract(payment));
    				userDAO.updateUserPoint(tranMap);
    				// 这里不再处理系统积分，因为系统积分购买的商品，不存在退的情况！！！
    				// 处理库存
    				processItemInventory(headMap, "30");
					returnInt = 1;
    			};
    		}
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}
}
