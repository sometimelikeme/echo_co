package echo.sp.app.command.core;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import echo.sp.app.command.model.JsonBean;
import echo.sp.app.command.page.PubTool;

/**
 * 所有Controller的基类
 * @author Ethan
 * @date 2015年8月10日
 */ 
public abstract class CoreController {
	protected JsonBean data;
	protected HttpSession session;
	
	/**
	 * @description 将传入参数转化为JSONDataBean对象,后台解析
	 * @version 1.0
	 */
	protected void getParm(HttpServletRequest req) {
		session = req.getSession();
		String jsonData = PubTool.processParm(req.getParameter("data"));
		if (!"".equals(jsonData)) {
			Gson gson = new Gson();
			Type type = new TypeToken<JsonBean>(){}.getType();
			data = gson.fromJson(jsonData, type);
		}
	}

	/**
	 * @description 将参数封装成JSON,后台封装;输出jsonParm
	 * @version 1.0
	 */
	protected void writeJson(HttpServletResponse response, String status,
			String msg, Map dataset,
			List dataset_line) {

		data = new JsonBean();
		data.setMsg(msg);
		data.setStatus(status);
		data.setDataset(dataset);
		data.setDataset_line(dataset_line);

		Gson gson = new Gson();
		String jsonParm = gson.toJson(data);  
		
		response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        
		PrintWriter writer = null;
		
		try {
			writer = response.getWriter();
			writer.write(jsonParm);
			response.getWriter().flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (null != writer) {
				writer.close();
			}
		}
	}
}
