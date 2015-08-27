package echo.sp.app.command.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**   
 * @author Ethan   
 * @date 2015年8月28日 
 */
public class ValidUtils {
	
	/**
	 * Valid Phone Number
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}  
	
	/**
	 * Valid Email
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}
	
//	public static void main(String args[]) {
//		System.out.println("----------- " + isMobileNO("151641193128"));
//		System.out.println("----------- " + isEmail("programccc@1163.com"));
//	}
}
