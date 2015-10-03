package echo.sp.app.command.page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;

import java.lang.Object;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

import echo.sp.app.service.PubToolService;

/**   
 * @author Ethan   
 * @date 2015年8月11日 
 */
public class PubTool {
	
	private static final Logger logger = LoggerFactory.getLogger(PubTool.class);
	
	private  static double EARTH_RADIUS = 6378137; //WGS84  6378137.0 米
	
	/**
	 * 计算经纬度之间的距离
	 * 1.java计算的较为精确, SQL计算能很好利用数据库的分页，但不够精确
	 * 2.然而, SQL和java计算的出来按照距离排序，由近到远是一致的
	 * 3.由此可以先用SQL分页查询出距离，这个距离为不精确距离，只是用来排序；取出数据集之后，再用java来计算精确距离
	 * @param resList 处理LIST
	 * @param paramMap 参数
	 * @param longtitudeName 获取参数经度键
	 * @param lantitudeName 获取参数纬度键
	 */
	public static void reCalculateDistance(List resList, Map paramMap, String longtitudeName, String lantitudeName) {
		double d1 = Double.parseDouble(paramMap.get("LONGITUDE").toString());// 参数经度
		double d2 = Double.parseDouble(paramMap.get("LATITUDE").toString());// 参数维度
		double d3;// 比较经度
		double d4;// 比较维度
		double dist;// 距离/米
		Map temMap;
		for (int i = 0; i < resList.size(); i++) {
			temMap = (Map) resList.get(i);
			d3 = Double.parseDouble(temMap.get(longtitudeName).toString());
			d4 = Double.parseDouble(temMap.get(lantitudeName).toString());
			dist = PubTool.GetDistance(d1, d2, d3, d4);// recalculate
			temMap.put("DIST", dist);
		}
	}
	
    private static double rad(double d){
        return d * Math.PI / 180.0;
    }
    
    /*   计算两点之间的距离     */
	public static int GetDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
		int returnDistance = 0;
		try {
			double radLat1 = rad(latitude1);
			double radLat2 = rad(latitude2);
			double a = radLat1 - radLat2;
			double b = rad(longitude1) - rad(longitude2);
			double doubleDistance = 2 * Math.asin(Math.sqrt(Math.pow(
					Math.sin(a / 2), 2)
					+ Math.cos(radLat1)
					* Math.cos(radLat2)
					* Math.pow(Math.sin(b / 2), 2)));
			doubleDistance = doubleDistance * EARTH_RADIUS;
			doubleDistance = Math.round(doubleDistance * 10000) / (double) 10000;
			returnDistance = (int) doubleDistance;
		} catch (Exception e) {
			logger.error("  GetDistance error ...... longitude1=" + longitude1
					+ " latitude1==" + latitude1 + "longitude2=" + longitude2
					+ " latitude2==" + latitude2);
		}

		return returnDistance;
	}
	
	// 转化为数字
	public static String parseNumber(Object o) {
		String str = o.toString();
		return "".equals(o.toString()) ? "0" : str;
	}
	
	/**
	 * 转化支付货币分为元单位,空返回0
	 * @param o
	 * @return
	 */
	public static String parseCentToYu(Object o) {
		String str = o.toString();
		if ("".equals(str)) {
			return "0";
		}
		BigDecimal bi = new BigDecimal(str);
		return bi.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP).toString();
	}
	
	/**
	 * 返回变量是否为NULL或者空
	 * @param o
	 * @return
	 */
	public static boolean processNullAndEmpty(String s) { 
		return s == null || (s != null && "".equals(s));
	}
	
	/**
	 * PROCESS THE PARM. 
	 * IF NULL, RETURN EMPTY
	 * @param s
	 * @return
	 */
	public static String processParm(String s) { 
		return s == null ? "" : s;
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String processNull(Object s) { 
		return s == null ? "" : s.toString();
	}
	
	/**
	 * 
	 * DESC：判断List不为null切含有数据
	 * By Ethan
	 * 2015年8月1日
	 */
	public static boolean isListHasData(List l) {
		return l != null && l.size() > 0;
	} 

	/**
	 * Get List result
	 * @param mapper
	 * @param map
	 * @param pageBounds
	 * @return
	 */
	public static List getResultList(String mapper, Map map, PageBounds pageBounds,
			SqlSessionFactory sqlSessionFactory) {
		String prefix = "echo.sp.app.dao.";  
		SqlSession sqlSession = null;
		List list = null;
		try {
			sqlSession = SqlSessionUtils.getSqlSession(sqlSessionFactory);
			list = sqlSession.selectList(prefix + mapper, map, pageBounds);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return list;
	}
	
	
	/**
	 * 获取机构参数
	 * @param parmMap
	 * @param pubToolService
	 * @return
	 */
	public static String getOrgParm(Map parmMap, PubToolService pubToolService){
		
		String resStr = "";
		
		try {
			parmMap.put("IN_USE", "1");
			resStr = pubToolService.getOrgParm(parmMap);
			resStr = resStr == null ? "" : resStr;
		} catch (Exception e) {
			logger.error("PubTool--getOrgParm: ",e);
		}
		
		return resStr;
	}

}
