package cn.jianke.sample.httprequest.okhttp;

/**
 * @interfaceName: UpLoadFileCallBack
 * @interfaceDescription: 上传下载回调
 * @author: leibing
 * @createTime: 2017/5/6
 */
public interface UpLoadFileCallBack {

    /**
     *  成功回调
     * @param data 数据
     */
    void onSuccess(String data);

    /**
     *  失败回调
     * @param
     */
    void onFail();

    /**
     *  异常回调
     * @param e 异常信息
     */
    void onException(Exception e);

    /**
     * 进度回调
     * @param total 文件总大小
     * @param current 当前文件大小
     */
    void onProgress(long total, long current);
}
