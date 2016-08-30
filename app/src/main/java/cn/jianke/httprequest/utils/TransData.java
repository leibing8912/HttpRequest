package cn.jianke.httprequest.utils;

/**
 * @className : TransData
 * @classDescription : Http请求返回信息类
 * @author : leibing
 * @createTime : 2016/08/30
 */
public class TransData {
	// 错误码  errorcode.equals("0") 成功 or 失败
	public String errorcode;
	// 数据
	public String info;
	// 错误消息
	public String errormsg;

	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
}
