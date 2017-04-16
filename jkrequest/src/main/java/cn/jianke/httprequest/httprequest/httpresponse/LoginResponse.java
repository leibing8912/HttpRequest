package cn.jianke.httprequest.httprequest.httpresponse;

import java.io.Serializable;

/**
 * @className: LoginResponse
 * @classDescription: 获取登录返回的信息
 * @author: leibing
 * @createTime: 2016/08/30
 */
public class LoginResponse extends BaseResponse implements Serializable{
    // 序列化UID 用于反序列化
    private static final long serialVersionUID = 4863726647304575308L;
    // token
    public String accesstoken;
}
