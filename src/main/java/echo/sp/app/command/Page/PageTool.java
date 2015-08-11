package echo.sp.app.command.Page;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**   
 * @author Ethan   
 * @date 2015Äê8ÔÂ11ÈÕ 
 */
public class PageTool {

	@Autowired
	private static SqlSessionFactory sqlSessionFactory;
	
	/**
	 * Get List result
	 * @param mapper
	 * @param map
	 * @param pageBounds
	 * @return
	 */
	public static List getResultList(String mapper, Map map, PageBounds pageBounds) {
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
