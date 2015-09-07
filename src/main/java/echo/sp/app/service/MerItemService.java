package echo.sp.app.service;

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
	
}
