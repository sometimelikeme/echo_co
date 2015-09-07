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
		return merItemDAO.addMerItem(item);
	}

	// 修改商品
	@Override
	public int updateMerItem(Map parmMap) {
		return merItemDAO.updateMerItem(parmMap);
	}

	// 检查商品是否为本店商品
	@Override
	public int checkMerItem(Map parmMap) {
		return merItemDAO.checkMerItem(parmMap);
	}

}
