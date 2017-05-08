package cn.jianke.sample.httprequest.okhttp.websocket;

import okhttp3.WebSocket;
import okio.ByteString;

/**
 * @interfaceName:
 * @interfaceDescription:
 * @author: leibing
 * @createTime: 2017/5/8
 */
public interface IJkWsManager {

    /**
     * 获取websocket实例
     * @param
     * @return
     */
    WebSocket getWebSocket();

    /**
     * 开始连接
     * @param
     * @return
     */
    void startConnect();

    /**
     * 停止连接
     * @param
     * @return
     */
    void stopConnect();

    /**
     * 是否websocket连接上
     * @param
     * @return
     */
    boolean isWsConnected();

    /**
     * 获取当前状态
     * @param
     * @return
     */
    int getCurrentStatus();

    /**
     * 发送消息
     * @param msg 消息字符串
     * @return
     */
    boolean sendMessage(String msg);

    /**
     * 发送消息
     * @param byteString 消息ByteString数据
     * @return
     */
    boolean sendMessage(ByteString byteString);
}
