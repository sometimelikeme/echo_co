package echo.sp.app.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import echo.sp.app.dao.UserDAO;
import echo.sp.app.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
    private UserDAO userDAO;
    
	// CHECK IF THE USER EXISTS 
    @Override
	public int getCheckReg(Map parmMap) {
		return userDAO.getCheckReg(parmMap);
	}
    
    // REGIST USER AND LOGIN IN 
	public int registAlg(Map parmMap) {
		return userDAO.registAlg(parmMap);
	}
    
    // LOGIN
    @Override
	public int login(Map parmMap) {
		return userDAO.login(parmMap);
	}
}
