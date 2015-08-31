package echo.sp.app.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import echo.sp.app.dao.PubToolDAO;
import echo.sp.app.service.PubToolService;

/**   
 * 公用服务 
 * @author Ethan   
 * @date 2015年8月31日 
 */
@Service
public class PubToolServiceImpl implements PubToolService {
	
	private static final Logger logger = LoggerFactory.getLogger(PubToolServiceImpl.class);
	
	@Autowired
	private PubToolDAO pubToolDAO;
	
	@Override
	public String getOrgParm(Map parmMap) {
		return pubToolDAO.getOrgParm(parmMap);
	}

}
