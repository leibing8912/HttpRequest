package cn.jianke.httprequest.httprequest.okhttp.websocket;

import okhttp3.Response;
import okio.ByteString;

/**
 * @className: JkWsStatusListener
 * @classDescription: 用于监听ws连接状态以及进一步扩展
 * @author: leibing
 * @createTime: 2017/5/8
 */
public abstract class JkWsStatusListener {

    /**
     * 打开
     * @param response 数据响应
     * @return
     */
    public void onOpen(Response response) {
    }

    /**
     * 接收数据
     * @param  text 接收字符串数据
     * @return
     */
    public void onMessage(String text) {
    }

    /**
     * 接收数据
     * @param  bytes 接收ByteString数据
     * @return
     */
    public void onMessage(ByteString bytes) {
    }

    /**
     * 重新连接
     * @param
     * @return
     */
    public void onReconnect() {
    }

    /**
     * 正在关闭中
     * @param code 关闭码
     * @param reason 关闭原因
     * @return
     */
    public void onClosing(int code, String reason) {
    }

    /**
     * 关闭
     * @param code 关闭码
     * @param reason 关闭原因
     * @return
     */
    public void onClosed(int code, String reason) {
    }

    /**
     * 失败
     * @param t 异常
     * @param response 数据
     * @return
     */
    public void onFailure(Throwable t, Response response) {
    }

    /**
     * 无网
     * @param
     * @return
     */
    public void onNoNetWork(){
    }
}
