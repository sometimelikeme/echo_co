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
	// 产生用户积分
	public int insertUserPoint(Map parmMap);
	// 获取当前总积分
	public String getTotalPoint(Map parmMap);
	// 汇总用户积分
	public int UpdateUserTotalPoint(Map parmMap);
}
