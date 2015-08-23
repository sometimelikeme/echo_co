package echo.sp.app.command.model;

import java.util.HashMap;
import java.util.Map;

/**   
 * STORE SECRET KEY AND NORMAL KEY
 * @author Ethan   
 * @date 2015年8月23日 
 */
public class SecCode {
	
	private static Map<String, String> secMap = new HashMap<String, String>();
	
	// 写入缓存
	public static void setKey(String key, String val) { 
		secMap.put(key, val);
	}
	
	// 写入缓存
	public static String getKey(String key) {
		return secMap.get(key);
	}
}
