package echo.sp.app.dao;

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
}
