package echo.sp.app.command.model;

import java.util.HashMap;
import java.util.Map;

/**   
 * STORE SECRET KEY AND NORMAL KEY
 * @author Ethan   
 * @date 2015��8��23�� 
 */
public class SecCode {
	
	public static String no_co_org = "ABCDEFG";// ORGINAL NORMAL CODE
	
	private static Map<String, String> secMap = new HashMap<String, String>();
	
	// д�뻺��
	public static void setKey(String key, String val) { 
		secMap.put(key, val);
	}
	
	// д�뻺��
	public static String getKey(String key) {
		return secMap.get(key);
	}
}
