package echo.sp.app.dao;

import java.util.Map;

import echo.sp.app.model.Item;

/**   
 * 商铺商品
 * @author Ethan   
 * @date 2015年8月29日 
 */
public interface MerItemDAO {
	
	// 增加商品
	public int addMerItem(Item item);
	
	// 修改商品
	public int updateMerItem(Map parmMap);
	
	// 检查商品是否为本店商品
	public int checkMerItem(Map parmMap); 
	
	// 升级商品为团购商品
	public int updateToPreItem(Map parmMap);
	
	// 获取商品信息
	public Map getItemInfo(Map parmMap);
	
	// 获取某店铺下的商品总量
	public int getMerItemQty(Map parmMap);
}
