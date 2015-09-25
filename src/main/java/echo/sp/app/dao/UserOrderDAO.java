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
	// Get Order Line Information
	public List getOrderLine(Map parmMap);
	
	// Cancel Order   
	public int updateOrderForCancel(Map parmMap);
	
	// Generate Pay Log
	// Update Pay Action on Order
	public int insertToWxLog(Map parmMap);
	public int insertToAliLog(Map parmMap);
	public int insertToUnLog(Map parmMap);
	public int insertToPayLog(Map parmMap);
	public int updateOrderPay(Map parmMap);
}
