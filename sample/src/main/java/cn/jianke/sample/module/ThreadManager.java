package cn.jianke.sample.module;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @className:ThreadManager
 * @classDescription:线程管理
 * @author: leibing
 * @createTime: 2016/8/15
 */
public class ThreadManager {
    /**
     * 单例
     */
    private static ThreadManager instance;

    /**
     * （创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收）
     */
    private ExecutorService cachedThreadPool;

    /**
     * 构造函数
     * @author leibing
     * @createTime 2016/8/15
     * @lastModify 2016/8/15
     * @param
     * @return
     */
    private ThreadManager(){
        cachedThreadPool = Executors.newCachedThreadPool();
    }

    /**
     * 单例
     * @author leibing
     * @createTime 2016/8/15
     * @lastModify 2016/8/15
     * @param
     * @return
     */
    public static ThreadManager getInstance(){
        if (instance == null)
            instance = new ThreadManager();

        return instance;
    }

    /**
     * 获取线程池（创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收）
     * @author leibing
     * @createTime 2016/8/15
     * @lastModify 2016/8/15
     * @param
     * @return cachedThreadPool
     */
    public ExecutorService getNewCachedThreadPool(){
        if (cachedThreadPool == null)
            cachedThreadPool = Executors.newCachedThreadPool();
        return cachedThreadPool;
    }
}