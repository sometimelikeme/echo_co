package echo.sp.app.service;

import java.util.List;
import java.util.Map;

/**   
 * 商铺订单
 * @author Ethan   
 * @date 2015年8月29日 
 */
public interface MerOrderService {
	// 消费订单
	public int updateOrderComsume(Map parmMap);
	// 结束订单
	public int updateOrderClose(Map parmMap);
	
	// 根据用户ID来获取对某一个订单中某一个商品的评论
	public Map getSingleCommentByUserId(Map parmMap);
	// 增加商品评论
	public int addComment(Map parmMap);
	// 删除商品评论
	public int deleteComment(Map parmMap);
	
	// 删除订单
	public int updateOrderForDelete(Map parmMap);
	
	// 获取已消费未关闭的订单列表
	public List getConsumeOrders(Map parmMap);
	
	// 根据订单ID获取订单商品详情
	public List getOrderDetailByOrderId(Map parmMap);
}
