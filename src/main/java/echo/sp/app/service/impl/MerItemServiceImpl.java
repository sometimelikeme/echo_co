package echo.sp.app.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import echo.sp.app.dao.MerItemDAO;
import echo.sp.app.model.Item;
import echo.sp.app.service.MerItemService;

/**   
 * 商铺商品
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Service
public class MerItemServiceImpl implements MerItemService {
	
	private static final Logger logger = LoggerFactory.getLogger(MerItemServiceImpl.class);
	
	@Autowired
	private MerItemDAO merItemDAO;
	
	// 增加商品
	@Override
	public int addMerItem(Item item) {
		int returnInt = 0;
    	try {
    		returnInt = merItemDAO.addMerItem(item);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}

	// 修改商品
	@Override 
	public int updateMerItem(Map parmMap) {
		int returnInt = 0;
    	try {
    		returnInt = merItemDAO.updateMerItem(parmMap);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}

	// 检查商品是否为本店商品
	@Override
	public int checkMerItem(Map parmMap) {
		return merItemDAO.checkMerItem(parmMap);
	}

	// 升级商品为团购商品
	@Override
	public int updateToPreItem(Map parmMap) {
		int returnInt = 0;
    	try {
    		returnInt = merItemDAO.updateToPreItem(parmMap);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}
	
	// 获取商品信息
	@Override
	public Map getItemInfo(Map parmMap) {
		return merItemDAO.getItemInfo(parmMap);
	}
	
	// 获取某店铺下的商品总量
	@Override
	public int getMerItemQty(Map parmMap) {
		return merItemDAO.getMerItemQty(parmMap);
	}

	// 获取某商品手否被当前用户收藏
	@Override
	public int getIsItemColl(Map parmMap) {
		return merItemDAO.getIsItemColl(parmMap);
	}

}
