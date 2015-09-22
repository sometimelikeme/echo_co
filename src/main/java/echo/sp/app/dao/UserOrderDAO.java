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
}
