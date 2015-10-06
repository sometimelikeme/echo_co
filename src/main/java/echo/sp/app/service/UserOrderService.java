package echo.sp.app.service;

import java.util.Map;

/**   
 * 用户订单 
 * @author Ethan   
 * @date 2015年8月29日 
 */
public interface UserOrderService {
	// Make Order
	public int addOrder(Map parmMap);
	// Get Order Detail
	public Map getOrderDetail(Map parmMap);
	// Cancel Order
	public int updateOrderForCancel(Map parmMap);
	// Generate Pay Log
	// Update Pay Action on Order
	public int updateOrderPay(Map parmMap);   
	
	// 根据PRE_PAID_ID获取用户充值和提现记录
	public Map getPrePayInfoById(Map parmMap); 
	// 将通过第三方支付（AWU）的信息保存到对应的日志表中
	// 生成充值记录
	// 更新用户可消费金额T_USERS_EXPAND
	public int insertPrePayInfo(Map parmMap);  
	
	// 产生用户金额消费记录
	// 修改用户金额
	// 修改订单状态
	public int updatePayAction(Map parmMap); 
}
