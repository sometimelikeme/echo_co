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
import org.springframework.web.bind.annotation.RequestParam;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import echo.sp.app.command.Page.PageParm;
import echo.sp.app.command.core.CoreController;

import org.apache.ibatis.session.SqlSessionFactory;

/**   
 * @author Ethan   
 * @date 2015年8月11日 
 */
@Controller
public class TestPaginator extends CoreController implements PageParm{
	
//	@Autowired
//	private SqlSessionFactory sqlSessionFactory;
	
	private static final Logger logger = LoggerFactory.getLogger(TestPaginator.class);
	
	@RequestMapping("paginatorParm")
    public List paginatorParm(@RequestParam String state,
            @RequestParam(required = false,defaultValue = page) int page,
            @RequestParam(required = false,defaultValue = limit) int limit,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String dir){
		
//		PageList类是继承于ArrayList的，这样Dao中就不用为了专门分页再多写一个方法
//		使用PageBounds这个对象来控制结果的输出，常用的使用方式一般都可以通过构造函数来配置。
//		new PageBounds();//默认构造函数不提供分页，返回ArrayList
//		new PageBounds(int limit);//取TOPN操作，返回ArrayList
//		new PageBounds(Order... order);//只排序不分页，返回ArrayList
//		 
//		new PageBounds(int page, int limit);//默认分页，返回PageList
//		new PageBounds(int page, int limit, Order... order);//分页加排序，返回PageList
//		new PageBounds(int page, int limit, List<Order> orders, boolean containsTotalCount);//使用containsTotalCount来决定查不查询totalCount，即返回ArrayList还是PageList

        PageBounds pageBounds = new PageBounds(page, limit , Order.formString(sort));
        
        logger.debug("paginatorParm -- " + pageBounds.toString());
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("state",state);
        
        List list = new PubTool().getResultList("UserDAO.byState", params, pageBounds);
        
        // List list = findByState(state, pageBounds);
        
        logger.debug("paginatorParm result: " + list);
        
        // Get totalCount
        PageList pageList = (PageList)list;
        System.out.println("paginatorParm totalCount: " + pageList.getPaginator().getTotalCount());

        return null;
    }
	
//	@RequestMapping("paginator")
//    public List paginator(HttpServletRequest req,HttpServletResponse response){
//		int page = 1;
//        int pageSize = 20;
//        String sortString = "id.desc";
//        PageBounds pageBounds = new PageBounds(page, pageSize , Order.formString(sortString));
//        
//        logger.debug("paginator -- " + pageBounds.toString());
//        
//        List list = findByCity("370100", pageBounds);
//        
//        logger.debug("result: " + list);
//        
//        //Get totalCount
//        PageList pageList = (PageList)list;
//        System.out.println("totalCount: " + pageList.getPaginator().getTotalCount());
//
//        return null;
//    }
	
//	public List findByCity(String code, PageBounds pageBounds){
//        SqlSession session = null;
//        try{
//            session = SqlSessionUtils.getSqlSession(sqlSessionFactory);
//            Map<String, Object> params = new HashMap<String, Object>();
//            params.put("id","1");
//            return session.selectList("echo.sp.app.dao.UserDAO.showUser", params, pageBounds);
//        }finally {
//            session.close();
//        }
//    }
	
//	public List findByState(String state, PageBounds pageBounds){
//        SqlSession session = null;
//        try{
//            session = SqlSessionUtils.getSqlSession(sqlSessionFactory);
//            Map<String, Object> params = new HashMap<String, Object>();
//            params.put("state",state);
//            return session.selectList("echo.sp.app.dao.UserDAO.byState", params, pageBounds);
//        }finally {
//            session.close();
//        }
//    }
}
