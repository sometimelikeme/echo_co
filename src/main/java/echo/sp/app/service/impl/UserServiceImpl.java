package echo.sp.app.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import echo.sp.app.dao.UserDAO;
import echo.sp.app.model.User;
import echo.sp.app.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
    private UserDAO userDAO;
     
    @Override
    public int insertUser(User user) {
    	logger.debug("ggg---insertUser---begin-ttt");
    	user.setId(4);
    	userDAO.insertUser1(user);
    	logger.debug("ggg---insertUser---prase1");
    	User user1 = new User();
    	user1.setNickname("jingwen");
    	user1.setState(3);
    	user1.setId(5);
    	userDAO.insertUser1(user1);
    	/*try {
    		userDAO.insertUser1(user1);
		} catch (Exception e) {
			logger.debug("ggg---insertUser---prase2",e);
		}*/
    	logger.debug("ggg---insertUser---prase2");
        return 1;
    }
    
    @Override
    public List getData(Map<String, Object> parmMap){
        return userDAO.getData(parmMap);
    }
}
