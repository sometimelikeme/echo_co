package echo.sp.app.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import echo.sp.app.dao.MerItemDAO;
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
	public int addMerItem(Map parmMap) {
		return merItemDAO.addMerItem(parmMap);
	}

	// 修改商品
	@Override
	public int updateMerItem(Map parmMap) {
		return merItemDAO.updateMerItem(parmMap);
	}

	// 查询商品
	@Override
	public List searchMerItem(Map parmMap) {
		return merItemDAO.searchMerItem(parmMap);
	}

}
