package echo.sp.app.dao;

import java.util.Map;

/**   
 * 商铺订单
 * @author Ethan   
 * @date 2015年8月29日 
 */
public interface MerOrderDAO {
	// 消费订单
	public int updateOrderComsume(Map parmMap);
	// 结束订单
	public int updateOrderClose(Map parmMap);
}
