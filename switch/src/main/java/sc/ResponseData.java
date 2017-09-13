/**
 * 
 */
package sc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ResponseData implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 成功 */
	public static final int STATUS_SUCCESS = 1;
	/** 失败 */
	public static final int STATUS_ERROR = 0;

	/** 返回值的状态,可以为success或者error */
	private Integer status;

	/** 成功或者错误的信息 */
	private String message;

	private Map<String, Object> data = new HashMap<String, Object>();

	public ResponseData() {
		super();
	}

	public ResponseData(Integer status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public ResponseData(Integer status, String message, Map<String, Object> data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public void put(String key, Object value) {
		this.data.put(key, value);
	}

	public Object get(String key) {
		if (this.data == null) {
			return null;
		}
		return this.data.get(key);
	}

	public boolean isSuccess() {
		if (status != null && status == STATUS_SUCCESS) {
			return true;
		}
		return false;
	}

}
