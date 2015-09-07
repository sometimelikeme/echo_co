package echo.sp.app.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import echo.sp.app.dao.MerStoreDAO;
import echo.sp.app.service.MerStoreService;

/**   
 * 商铺
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Service
public class MerStoreServiceImpl implements MerStoreService {
	
	private static final Logger logger = LoggerFactory.getLogger(MerStoreServiceImpl.class);
	
	@Autowired
	private MerStoreDAO merStoreDAO;
	
	// GET MERCHANTS DETAIL INFORMATION
	@Override
	public Map getMerDetail(Map parmMap) {
		return merStoreDAO.getMerDetail(parmMap);
	}
}
