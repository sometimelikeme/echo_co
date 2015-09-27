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
	public int updateUserTotalPoint(Map parmMap);
	
	// 根据用户ID来获取对某一个订单中某一个商品的评论
	public Map getSingleCommentByUserId(Map parmMap);
	// 删除商品评论
	public int deleteComment(Map parmMap);
	// 增加商品评论
	public int addComment(Map parmMap);
	// 修改订单状态为已评论状态
	public int updateOrderComment(Map parmMap);
	
	// 删除订单
	public int updateOrderForDelete(Map parmMap);
}
