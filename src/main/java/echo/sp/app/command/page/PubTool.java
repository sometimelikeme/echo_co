package echo.sp.app.command.page;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**   
 * @author Ethan   
 * @date 2015Äê8ÔÂ11ÈÕ 
 */
public class PubTool {

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

}
