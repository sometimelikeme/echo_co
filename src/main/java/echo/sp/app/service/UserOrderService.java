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
}
