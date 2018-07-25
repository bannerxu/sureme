package top.xuguoliang.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultBean {
	private int status;
	private String message;
	
	private Object Data;
	public Object getData() {
		return Data;
	}
	public void setData(Object data) {
		Data = data;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
