package echo.sp.app.dao;

import java.util.List;
import java.util.Map;

/**   
 * 用户技能服务
 * @author Ethan   
 * @date 2015年10月24日 
 */
public interface UserAblityDAO {
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
	// 查询订单信息
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
	// 增加技能购买量
	public int updateAbliBuyCount(Map parmMap);
	// 获取技能评论量和综合评分
	public Map getAbliCommentInfo(Map parmMap);
	// 更新技能评论量和综合评分
	public int updateAbliCommentInfo(Map parmMap);
	// 获取待处理订单列表
	public List getUnProcessOrders(Map parmMap);
	// 获取任务对应的用户ID
	public String getAbilityUserId(Map parmMap);
}
