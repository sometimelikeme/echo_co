package echo.sp.app.service.impl;

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

}
