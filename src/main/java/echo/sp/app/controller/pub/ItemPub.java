package echo.sp.app.controller.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import echo.sp.app.command.page.PubTool;
import echo.sp.app.controller.mer.MerItemController;

/**   
 * ITEM PUB SERVICE
 * @author Ethan   
 * @date 2015年9月7日 
 */
public class ItemPub {
	
	private static final Logger logger = LoggerFactory.getLogger(MerItemController.class);
	
	/**
	 * 查询商品接口
	 * 1.根据ITEM_ID,ITEM_NAME,ITEM_SHORT_NAME,MERCHANT_ID,CATEGORY_ID,IS_PREF,IS_SKILL,IS_POINT
	 *   条件来查询商品
	 *   根据ITEM_ID来查询商品的,接口返回商品的评论信息
	 * 2.提供分页查询功能
	 * 	  通过附加参数page,pageSize来执行分页查询,
	 *   sort参数来执行排序
	 *   同时返回查询的总记录数
	 */
	public static Map searchMerItem(Map paramMap, SqlSessionFactory sqlSessionFactory) {
		// PAGE PARAMETERS
		Object page = paramMap.get("page"),// PAGE NUMBER
			   pageSize  = paramMap.get("pageSize"),// MAX ROWS RETURN
			   sort = paramMap.get("sort"),// SORT INFO
			   item_id = paramMap.get("ITEM_ID");
		
		String sortString = sort == null ? "" : sort.toString();
		
		// PageList类是继承于ArrayList的，这样Dao中就不用为了专门分页再多写一个方法
		// 使用PageBounds这个对象来控制结果的输出，常用的使用方式一般都可以通过构造函数来配置。
		// new PageBounds();//默认构造函数不提供分页，返回ArrayList
		// new PageBounds(int limit);//取TOPN操作，返回ArrayList
		// new PageBounds(Order... order);//只排序不分页，返回ArrayList
		// new PageBounds(int page, int limit);//默认分页，返回PageList
		// new PageBounds(int page, int limit, Order... order);//分页加排序，返回PageList
		// new PageBounds(int page, int limit, List<Order> orders, boolean containsTotalCount);
		// 使用containsTotalCount来决定查不查询totalCount，即返回ArrayList还是PageList
		PageBounds pageBounds;
					
		List resList = null;
		Boolean isPage = false;
		
		if (page != null && pageSize != null) {// PAGE CONTROL
			isPage = true;
			int pageInt = Integer.parseInt(page.toString()),
				pageSizeInt = Integer.parseInt(pageSize.toString());
			pageBounds = new PageBounds(pageInt, pageSizeInt , Order.formString(sortString));
		} else if (!"".equals(sort)) {// SORT WIHIOUT PAGE CONTROL
			pageBounds = new PageBounds(Order.formString(sortString));
		} else {// SEARCH ALL
			pageBounds = new PageBounds();
		}
		
		resList = PubTool.getResultList("MerItemDAO.searchMerItem", paramMap, pageBounds, sqlSessionFactory);
		
		Map resMap = new HashMap();
		
		Boolean hasData = false;

		if (PubTool.isListHasData(resList)) {
			hasData = true;
			// 处理策略一：如执行分页查询,则将总页数参数放入MAP对象中
	        // 处理策略二: 若传入的是ITEM_ID切当前有数据集，则将当前商品放入MAP中,评论放入LIST
			// 当非上述两种情况，直接将查询出来的商品列表放入dataset_line中，dataset对象为空，数据格式请参照分页的结果
	        if (isPage) {
	        	// Get totalCount
	    		PageList pageList = (PageList) resList;
	    		int totalCount = pageList.getPaginator().getTotalCount();
	        	paramMap = new HashMap();
	        	paramMap.put("totalCount", totalCount);
			} else if (item_id != null && !"".equals(item_id.toString())
					&& PubTool.isListHasData(resList) && resList.size() == 1) {// 标志当前执行查询的为具体商品
				paramMap = (Map) resList.get(0);
				sortString = "COMMENT_TIME.desc";// 默认按照时间倒序排序
				pageBounds = new PageBounds(Order.formString(sortString));
				Map parmMap = new HashMap();
				parmMap.put("ITEM_ID", item_id);
				resList = PubTool.getResultList("MerItemDAO.searchItemComments", parmMap, pageBounds, sqlSessionFactory);
			} else {
				paramMap = null;// 清空
			}
	        resMap.put("resList", resList);
	        resMap.put("paramMap", paramMap);
		}
		resMap.put("hasData", hasData);
		
		return resMap;
	}
	
}