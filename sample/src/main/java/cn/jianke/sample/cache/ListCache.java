package cn.jianke.sample.cache;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @className: ListCache
 * @classDescription: 列表缓存(用于缓存列表数据,减弱对sqlite的依赖)
 * @author: leibing
 * @createTime: 2016/08/26
 */
public class ListCache<T> implements Serializable {
    // 序列化UID 当需要反序列化的时候,此UID必须要.
    private static final long serialVersionUID = -3276096981990292013L;
    // 对象列表(用于存储需要缓存下来的列表)
    private ArrayList<T> objList;

    public ArrayList<T> getObjList() {
        return objList;
    }

    public void setObjList(ArrayList<T> objList) {
        this.objList = objList;
    }
}
