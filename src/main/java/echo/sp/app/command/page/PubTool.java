package echo.sp.app.command.page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
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
