package echo.sp.app.command.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * 
 * @author ThinkGem
 * @version 2014-4-15
 */
public class DateUtils extends DateFormatUtils {

	private static String[] parsePatterns = { "yyyy-MM-dd",
			"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd",
			"yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd",
			"yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM" };
	
	public static void main(String args[]) {
//		System.out.print("ddddddddd: " + getNextDay(getToday(),3));
//		System.out.println(getCurMonth());
//		System.out.println(getNextMonth(getCurMonth(),1));
//		System.out.println(getWeek(getToday()));
//		System.out.println(getCurrentTimeMillis());
//		Date before = parseStringDate("2015-09-25 6:46:09");
//		Date after = parseStringDate("2015-09-27 18:46:09");
//		System.out.println(before);
//		System.out.println(after);
//		System.out.println(getDistanceOfTwoDate(before, after));
	}
	
	/**
	 * 获取当前系统日期 返回 8位 like 20050101
	 * @return
	 */
	public static String getToday() {
		String dateStr = getDate();
		return dateStr.substring(0, 4) + dateStr.substring(5, 7) + dateStr.substring(8, 10);
	}
	
	/**
	 * 获取输入日期的下一天 返回 8位 like 20050101
	 * @param today
	 * @return
	 */
	public static String getNextDay(String day) {
		return getNextDay(day, 1);
	}
	
	/**
	 * 获取输入日期的下 n天 返回 8位 like 20050101
	 * @param day
	 * @param n
	 * @return
	 */
	public static String getNextDay(String day, int n) {
		if (day == null || "".equals(day) || day.length() != 8) {
			throw new RuntimeException("由于缺少必要的参数，系统无法进行制定的日期换算.");
		}
		try {
			String sYear = day.substring(0, 4);
			int year = Integer.parseInt(sYear);
			String sMonth = day.substring(4, 6);
			int month = Integer.parseInt(sMonth);
			String sDay = day.substring(6, 8);
			int dday = Integer.parseInt(sDay);
			Calendar cal = Calendar.getInstance();
			cal.set(year, month - 1, dday);
			cal.add(Calendar.DATE, n);
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			return df.format(cal.getTime());
		} catch (Exception e) {
			throw new RuntimeException("进行日期运算时输入得参数不符合系统规格." + e);

		}
	}
	
	/**
	 * 获取输入日期的前天 返回 8位 like 20050101
	 * @param day
	 * @param n
	 * @return
	 */
	public static String getPreviousDay(String day) {
		return getNextDay(day, -1);
	}
	
	/**
	 * 获取输入日期的前 n 天 返回 8位 like 20050101
	 * @param day
	 * @param n
	 * @return
	 */
	public static String getPreviousDay(String day, int n) {
		return getNextDay(day, -n);
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 获取当前系统月份 返回 6位 like 201501
	 */
	public static String getCurMonth() {
		String dateStr = getDate("yyyy-MM");
		return dateStr.substring(0, 4) + dateStr.substring(5, 7);
	}
	
	/**
	 * 获取输入 月份的下 n 月份 返回 6位 like 200501
	 * @param month like 200404
	 * @param n
	 * @return
	 */
	public static String getNextMonth(String month, int n) {
		if (month == null || "".equals(month) || month.length() != 6) {
			throw new RuntimeException("由于缺少必要的参数，系统无法进行制定的月份换算.");
		}
		try {
			String sYear = month.substring(0, 4);
			int year = Integer.parseInt(sYear);
			String sMonth = month.substring(4, 6);
			int mon = Integer.parseInt(sMonth);
			Calendar cal = Calendar.getInstance();
			cal.set(year, mon - 1, 1);
			cal.add(Calendar.MARCH, n);
			SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
			return df.format(cal.getTime());
		} catch (Exception e) {
			throw new RuntimeException("进行月份运算时输入得参数不符合系统规格." + e);
		}
	}
	
	/**
	 * 获取输入 月份的下1月份 返回 6位 like 200501
	 * @param month like 200404
	 * @param n
	 * @return
	 */
	public static String getNextMonth(String month) {
		return getNextMonth(month, 1);
	}
	
	/**
	 * 获取输入 月份的前 n 月份 返回 6位 like 200501
	 * @param month
	 * @param n
	 * @return
	 */
	public static String getPreviousMonth(String month, int n) {
		return getNextMonth(month, -n);
	}
	
	/**
	 * 获取输入 月份的前 1 月份 返回 6位 like 200501
	 * @param month
	 * @param n
	 * @return
	 */
	public static String getPreviousMonth(String month) {
		return getNextMonth(month, -1);
	}
	
	/**
	 * 获取输入 日期的前n年同期，如去年同期，如：2009-->2008，200904-->200804,20090410-->20080410
	 * @param month like 201304
	 * @param n
	 * @return
	 */
	public static String getPreSamePeriod(String date, int n) {
		String datenow = "";
		if (date.length() == 6 || date.length() == 8) {
			String dateyear = date.substring(0, 4);
			int year = Integer.parseInt(dateyear);
			int yeartemp = year - n;
			datenow = String.valueOf(yeartemp) + date.substring(4);
		} else if (date.length() == 4) {
			int year = Integer.parseInt(date);
			int yeartemp = year - n;
			datenow = String.valueOf(yeartemp);
		} else {
			throw new RuntimeException("由于缺少必要的参数，系统无法进行制定的换算.");
		}
		return datenow;
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	
	/**
	 * 将String的yyyy-MM-dd HH:mm:ss日期转化为日期
	 * @param s
	 * @return
	 */
	public static Date parseStringDate(String s) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = sdf.parse(s);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return date;
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取当前系统时间 返回 12:12:12
	 * @return
	 */
	public static long getCurrentTimeMillis() {
		return System.currentTimeMillis();
	}
	
	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 返回星期 0 星期天 6 星期陆
	 * @param date 20040101
	 * @return
	 */
	public static int getWeek(String date) {
		if (date == null || date.length() != 8) {
			throw new RuntimeException("由于缺少必要的参数，系统无法进行制定的月份换算.");
		}
		String sYear = date.substring(0, 4);
		int year = Integer.parseInt(sYear);
		String sMonth = date.substring(4, 6);
		int mon = Integer.parseInt(sMonth);
		String sDay = date.substring(6, 8);
		int dday = Integer.parseInt(sDay);
		Calendar cal = Calendar.getInstance();
		cal.set(year, mon - 1, dday);
		int weekday = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return weekday;
	}
	
	/**
	 * 获取过去的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取过去的小时
	 * 
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 60 * 1000);
	}

	/**
	 * 获取过去的分钟
	 * 
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 1000);
	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String formatDateTime(long timeMillis) {
		long day = timeMillis / (24 * 60 * 60 * 1000);
		long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
		long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60
				* 1000 - min * 60 * 1000 - s * 1000);
		return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "."
				+ sss;
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
}
