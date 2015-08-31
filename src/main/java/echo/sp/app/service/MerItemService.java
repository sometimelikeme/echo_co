package echo.sp.app.service;

import java.util.List;
import java.util.Map;

/**   
 * 商铺商品
 * @author Ethan   
 * @date 2015年8月29日 
 */
public interface MerItemService {
	
	// 增加商品
	public int addMerItem(Map parmMap);
	
	// 修改商品
	public int updateMerItem(Map parmMap);
	
	// 查询商品
	public List searchMerItem(Map parmMap);
}
