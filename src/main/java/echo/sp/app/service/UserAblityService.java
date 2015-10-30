package echo.sp.app.service;

import java.util.Map;

/**   
 * 用户技能服务
 * @author Ethan   
 * @date 2015年10月24日 
 */
public interface UserAblityService {
	// 增加技能
	public int addAblity(Map parmMap);
	// 修改技能
	public int updateAblity(Map parmMap);
	// 删除技能
	public int deleteAblity(Map parmMap);
	// 查询技能
	public Map searchAblityById(Map parmMap);
	// 查询评论
	public Map getCommentById(Map parmMap);
	// 增加评论
	public int addComment(Map parmMap);
	// 删除评论
	public int delComment(Map parmMap);
	// 购买技能
	public int addBuyAbility(Map parmMap);
	// 查询订单
	public Map getAbliOrderById(Map parmMap);
	// 拒绝购买
	public int updateDeclineContract(Map parmMap);
	// 确认购买
	public int updateConfirmContract(Map parmMap);
	// 交付订单
	public int updateDoneAbility(Map parmMap);
	// 确认交付
	public int updateConfirmDone(Map parmMap);
	// 确认未交付
	public int updateConfirmUnDone(Map parmMap);
	// 删除订单
	public int deleteOrder(Map parmMap);
	// 处理过期的订单
	public int updateProcessDeadOrders(Map parmMap);
}
