package echo.sp.app.dao;

import java.util.Map;

/**   
 * 用户商品DAO
 * @author Ethan   
 * @date 2015年8月29日 
 */
public interface UserItemDAO {
	// 收藏店铺
	public int addMerColl(Map parmMap);
	// 收藏商品
	public int addItemColl(Map parmMap);
	// 删除收藏店铺
	public int deleteMerColl(Map parmMap);
	// 删除收藏商品
	public int deleteItemColl(Map parmMap);
}
