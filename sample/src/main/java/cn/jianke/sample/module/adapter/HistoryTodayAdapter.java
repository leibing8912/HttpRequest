package cn.jianke.sample.module.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import cn.jianke.httprequest.utils.StringUtil;
import cn.jianke.sample.R;
import cn.jianke.sample.httprequest.httpresponse.HistoryTodayResponse;

/**
 * @className: HistoryTodayAdapter
 * @classDescription: 历史上的今天适配器
 * @author: leibing
 * @createTime: 2016/11/10
 */
public class HistoryTodayAdapter extends BaseAdapter{
    // 布局
    private LayoutInflater mLayoutInflater;
    // 数据源
    private ArrayList<HistoryTodayResponse.ResultList> mData;

    /**
     *
     * @author leibing
     * @createTime 2016/11/10
     * @lastModify 2016/11/10
     * @param context 上下文
     * @param mData 数据源
     * @return
     */
    public HistoryTodayAdapter(Context context, ArrayList<HistoryTodayResponse.ResultList> mData){
        if (context != null)
            mLayoutInflater = LayoutInflater.from(context);
        this.mData = mData;
    }

    /**
     * 更新数据源
     * @author leibing
     * @createTime 2016/11/10
     * @lastModify 2016/11/10
     * @param mData 数据源
     * @return
     */
    public void updateData(ArrayList<HistoryTodayResponse.ResultList> mData){
        this.mData = mData;
        HistoryTodayAdapter.this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size():0;
    }

    @Override
    public Object getItem(int i) {
        return mData != null?mData.get(i):null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            view = mLayoutInflater.inflate(R.layout.layout_history_today_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // 更新数据
        if (mData != null && mData.size() != 0 && i < mData.size()){
            viewHolder.updateUI(mData.get(i));
        }

        return view;
    }

    /**
     * @className: ViewHolder
     * @classDescription: 布局容器
     * @author: leibing
     * @createTime: 2016/11/10
     */
    public static class ViewHolder{
        // 文案控件
        private TextView historyIdTv, historyDayTv, historyDesTv,
                historyLunarTv, historyMonthTv, historyPicTv,
                historyTitleTv, historyYearTv;

        /**
         * 构造函数
         * @author leibing
         * @createTime 2016/11/10
         * @lastModify 2016/11/10
         * @param view 布局
         * @return
         */
        public ViewHolder(View view){
            // 初始化文案控件
            historyIdTv = (TextView) view.findViewById(R.id.tv_history_id);
            historyDayTv = (TextView) view.findViewById(R.id.tv_history_day);
            historyDesTv = (TextView) view.findViewById(R.id.tv_history_des);
            historyLunarTv = (TextView) view.findViewById(R.id.tv_history_lunar);
            historyMonthTv = (TextView) view.findViewById(R.id.tv_history_month);
            historyPicTv = (TextView) view.findViewById(R.id.tv_history_pic);
            historyTitleTv = (TextView) view.findViewById(R.id.tv_history_title);
            historyYearTv = (TextView) view.findViewById(R.id.tv_history_year);
        }

        /**
         * 更新数据
         * @author leibing
         * @createTime 2016/11/10
         * @lastModify 2016/11/10
         * @param result
         * @return
         */
        public void updateUI(HistoryTodayResponse.ResultList result){
            if (StringUtil.isNotEmpty(result.id) && historyIdTv != null){
                historyIdTv.setText("id :" + result.id);
            }
            if (StringUtil.isNotEmpty(result.day) && historyDayTv != null){
                historyDayTv.setText("day:" + result.day);
            }
            if (StringUtil.isNotEmpty(result.des) && historyDesTv != null){
                historyDesTv.setText("des:" + result.des);
            }
            if (StringUtil.isNotEmpty(result.lunar) && historyLunarTv != null){
                historyLunarTv.setText("lunar:" + result.lunar);
            }
            if (StringUtil.isNotEmpty(result.month) && historyMonthTv != null){
                historyMonthTv.setText("month:" + result.month);
            }
            if (StringUtil.isNotEmpty(result.pic) && historyPicTv != null){
                historyPicTv.setText("pic:" + result.pic);
            }
            if (StringUtil.isNotEmpty(result.title) && historyTitleTv != null){
                historyTitleTv.setText("title:" + result.title);
            }
            if (StringUtil.isNotEmpty(result.year) && historyYearTv != null){
                historyYearTv.setText("year:" + result.year);
            }
        }
    }
}
