package org.nbone.web.support;

import org.nbone.mx.datacontrols.support.ResultWrapper;


public class WebResultWrapper extends ResultWrapper {
	
	
	public static final String TYPE_ERROR = "ERROR";
	public static final String TYPE_INFO = "INFO";
	public static final String TYPE_WARNING = "WARNING";
	
	public static final String DEFAULT_TYPE_ERROR = TYPE_ERROR;
	/**
	 * web error page 
	 */
	private String errorPage;
	private String type;

	public WebResultWrapper() {
		type = DEFAULT_TYPE_ERROR;
	}
	

	public WebResultWrapper(boolean isSuccess, Object data, String tip,String errorPage, String type) {
		super(isSuccess, data, tip);
		this.type = TYPE_ERROR;
		this.errorPage = errorPage;
		this.type = type;
	}
	/**
	 * 用作成功结果集返回
	 * @param data
	 * @return
	 */
	public static WebResultWrapper successResultWraped(Object data) {
		return new WebResultWrapper(true, data, "", "", "");
	}
    /**
     * 用作失败返回消息提示
     * @param exMessage 提示消息
     * @return
     * @see #
     */
	public static WebResultWrapper failedResultWraped(String message) {
		return failedResultWraped(message,null);
	}
    /**
     * 用作失败返回消息提示
     * @param exMessage  提示消息
     * @param errorPage  错误页面
     * @return
     * @see #
     */
	public static WebResultWrapper failedResultWraped(String message,String errorPage) {
		return failedResultWraped(message, errorPage, TYPE_ERROR);
	}
    /**
     * 用作失败返回消息提示
     * @param exMessage 提示消息
     * @param errorPage 错误页面
     * @param type      类型
     * @return
     */
	public static WebResultWrapper failedResultWraped(String message,String errorPage, String type) {
		return new WebResultWrapper(false,null, message, errorPage, type);
	}
	
	
    //seter/geter
	public String getErrorPage() {
		return errorPage;
	}
	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	

}
