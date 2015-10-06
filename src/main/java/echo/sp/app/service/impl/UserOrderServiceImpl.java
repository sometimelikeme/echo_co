package echo.sp.app.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import echo.sp.app.dao.MerItemDAO;
import echo.sp.app.dao.MerOrderDAO;
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
    		// 其中包含库存的扣减和增加
    		// 获取订单行表商品信息
    		// 汇总库存和销量
    		List itemList = merOrderDAO.getOrderDetailByOrderId(upMap);
			Map temMap;
			Map resMap;
			BigDecimal inventory_de;// 库存量
			BigDecimal qty_sold_de;// 销量
			BigDecimal qty_sold;// 需求量
			for (int i = 0; i < itemList.size(); i++) {
				
				temMap = (Map) itemList.get(i);
				
				// 获取每个商品的当前库存和销量
				resMap = merItemDAO.getItemInvtentory(temMap);
				
				inventory_de = new BigDecimal(resMap.get("INVENTORY").toString());// 当前库存
				
				qty_sold_de = new BigDecimal(resMap.get("QTY_SOLD").toString());// 当前销量
				
				qty_sold = new BigDecimal(temMap.get("QTY_SOLD").toString());// 本次订单销量
				
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
    		
			returnInt = 1; 
			
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}
}
