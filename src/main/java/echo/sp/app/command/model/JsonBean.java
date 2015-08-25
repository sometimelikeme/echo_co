package echo.sp.app.command.model;

import java.util.Map;
import java.util.List;

/**
 * 
 * 本类为AJAX请求后台解析JSON Bean
 * @author Ethan   
 * @version 1.0
 */
public class JsonBean {

	String status;// 状态码
	String msg;// 状态说明

	List<Map<String, Object>> dataset_line;

	Map<String, Object> dataset;

	public List<Map<String, Object>> getDataset_line() {
		return dataset_line;
	}

	public void setDataset_line(List<Map<String, Object>> dataset_line) {
		this.dataset_line = dataset_line;
	}

	public Map<String, Object> getDataset() {
		return dataset;
	}

	public void setDataset(Map<String, Object> dataset) {
		this.dataset = dataset;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
