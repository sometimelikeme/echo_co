package echo.sp.app.service;

import java.util.List;
import java.util.Map;

import echo.sp.app.model.User;

public interface UserService {
	public int insertUser(User user);
	public List getData(Map<String, Object> parmMap);
}
