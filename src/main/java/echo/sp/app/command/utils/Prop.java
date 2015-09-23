package echo.sp.app.command.utils;

import java.util.MissingResourceException;

import java.util.ResourceBundle;

/**
 * 读取配置文件properties
 */
public class Prop {

	private static final String BUNDLE_NAME = "app-props";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private Prop() {

	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return "?" + key;
		}
	}
	
//	public static void main(String args[]){
//		System.out.println(Prop.getString("XSM_ST_URL"));
//	}

}