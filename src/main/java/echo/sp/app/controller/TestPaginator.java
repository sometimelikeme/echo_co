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
 * @date 2015��8��11�� 
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
		
//		PageList���Ǽ̳���ArrayList�ģ�����Dao�оͲ���Ϊ��ר�ŷ�ҳ�ٶ�дһ������
//		ʹ��PageBounds������������ƽ������������õ�ʹ�÷�ʽһ�㶼����ͨ�����캯�������á�
//		new PageBounds();//Ĭ�Ϲ��캯�����ṩ��ҳ������ArrayList
//		new PageBounds(int limit);//ȡTOPN����������ArrayList
//		new PageBounds(Order... order);//ֻ���򲻷�ҳ������ArrayList
//		 
//		new PageBounds(int page, int limit);//Ĭ�Ϸ�ҳ������PageList
//		new PageBounds(int page, int limit, Order... order);//��ҳ�����򣬷���PageList
//		new PageBounds(int page, int limit, List<Order> orders, boolean containsTotalCount);//ʹ��containsTotalCount�������鲻��ѯtotalCount��������ArrayList����PageList

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
