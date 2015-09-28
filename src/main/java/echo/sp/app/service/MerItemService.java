package echo.sp.app.service;

import java.util.List;
import java.util.Map;

import echo.sp.app.model.Item;

/**   
 * 商铺商品
 * @author Ethan   
 * @date 2015年8月29日 
 */
public interface MerItemService {
	
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
	
	// 获取某商品手否被当前用户收藏
	public int getIsItemColl(Map parmMap);
	
	// 查询某商品的库存量和销量
	public Map getItemInvtentory(Map parmMap);
	
	// 根据商品ID获取店铺信息
	public Map getMerInfoByItemId(Map parmMap);
	
	// 根据商品ID未完成的订单列表
	public List getUnFinishedOrders(Map parmMap);
	
	// 修改商品为删除状态
	public int updateDeleteItem(Map parmMap);
}
