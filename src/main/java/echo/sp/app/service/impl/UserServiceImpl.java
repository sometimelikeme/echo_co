package echo.sp.app.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import echo.sp.app.dao.UserDAO;
import echo.sp.app.model.User;
import echo.sp.app.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
    private UserDAO userDAO;
     
    @Override
    public int insertUser(User user) {
        return userDAO.insertUser(user);
    }
    
    @Override
    public List getData(Map<String, Object> parmMap){
        return userDAO.getData(parmMap);
    }
}
