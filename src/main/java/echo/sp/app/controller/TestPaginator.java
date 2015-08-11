package echo.sp.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import org.apache.ibatis.session.SqlSessionFactory;

/**   
 * @author Ethan   
 * @date 2015Äê8ÔÂ11ÈÕ 
 */
@Controller
public class TestPaginator{
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	private static final Logger logger = LoggerFactory.getLogger(TestPaginator.class);
	
	@RequestMapping("paginator")
    public List paginator(HttpServletRequest req,HttpServletResponse response){
		int page = 1;
        int pageSize = 20;
        String sortString = "id.desc";
        PageBounds pageBounds = new PageBounds(page, pageSize , Order.formString(sortString));
        
        logger.debug("paginator -- " + pageBounds.toString());
        
        List list = findByCity("370100", pageBounds);
        
        logger.debug("result: " + list);
        
        //Get totalCount
        PageList pageList = (PageList)list;
        System.out.println("totalCount: " + pageList.getPaginator().getTotalCount());

        return null;
    }
	
	public List findByCity(String code, PageBounds pageBounds){
        SqlSession session = null;
        try{
            session = SqlSessionUtils.getSqlSession(sqlSessionFactory);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("code","");
            return session.selectList("echo.sp.app.dao.UserDAO.showUser", params, pageBounds);
        }finally {
            session.close();
        }
    }
}
