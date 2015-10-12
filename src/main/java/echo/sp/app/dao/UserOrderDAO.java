package echo.sp.app.dao;

import java.util.List;
import java.util.Map;

/** 
 * 用户订单  
 * @author Ethan   
 * @date 2015年8月29日 
 */
public interface UserOrderDAO {
	
	// Make Order
	public int addOrderHead(Map parmMap);
	// Make Order
	public int addOrderLine(List list);
	// Update Inventory and Sold in T_ITEMS
	public int modifyItemQty(List list);    
	
	// Get Order Head Information
	public Map getOrderHead(Map parmMap);
	
	// Cancel Order   
	public int updateOrderForCancel(Map parmMap);
	
	// Generate Pay Log
	// Update Pay Action on Order
	public int insertToWxLog(Map parmMap);
	public int insertToAliLog(Map parmMap);
	public int insertToUnLog(Map parmMap);
	public int insertToPayLog(Map parmMap);
	// 付款处理
	public int updateOrderPay(Map parmMap);
	// 扣款处理
	public int updateOrderPayBack(Map parmMap);
	
	// 根据PRE_PAID_ID获取用户充值和提现记录
	public Map getPrePayInfoById(Map parmMap);
	// 将支付记录保存到用户预支付金额记录表
	public int insertPrePayInfo(Map parmMap);
	
	// Make Order
	public int addMallOrderHead(Map parmMap);
}
