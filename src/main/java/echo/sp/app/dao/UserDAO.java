package echo.sp.app.dao;

import java.util.List;
import java.util.Map;

import echo.sp.app.model.User;

public interface UserDAO {
	/**
     * 添加新用户
     * @param user
     * @return
     */
    public int insertUser(User user);
    public int insertUser1(User user);
    public List getData(Map<String, Object> parmMap);
}
