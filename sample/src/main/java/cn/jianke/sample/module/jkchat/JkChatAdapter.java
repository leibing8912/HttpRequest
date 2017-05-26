package cn.jianke.sample.module.jkchat;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.text.ParseException;
import java.util.ArrayList;
import cn.jianke.httprequest.utils.StringUtil;
import cn.jianke.sample.R;
import cn.jianke.sample.cache.ListCache;
import cn.jianke.sample.cache.SpLocalCache;
import cn.jianke.sample.module.ImageLoader;
import cn.jianke.sample.module.ShareApplication;
import cn.jianke.sample.module.jkchat.bean.JkChatBean;
import cn.jianke.sample.module.jkchat.util.DateUtils;
import cn.jianke.sample.module.jkchat.util.JkChatUtils;
import static cn.jianke.sample.module.jkchat.JkChatConstant.MSG_STATUS_SENDING;
import static cn.jianke.sample.module.jkchat.JkChatConstant.MSG_STATUS_SEND_FAIL;
import static cn.jianke.sample.module.jkchat.JkChatConstant.MSG_STATUS_SEND_SUCCESS;
import static cn.jianke.sample.module.jkchat.JkChatConstant.MSG_TYPE_TXT;

/**
 * @className: JkChatAdapter
 * @classDescription: 健客聊天适配器
 * @author: leibing
 * @createTime: 2017/5/11
 */
public class JkChatAdapter extends BaseAdapter {
    // 消息显示间隔
    public final static long MSG_DIV_SHOW_TIME = 90*1000;
    // 布局
    private LayoutInflater mLayoutInflater;
    // 数据源
    private ArrayList<JkChatBean> mData;
    // 屏幕宽度
    private int screenWidth = 0;
    // 适配器监听
    private AdapterCallBackListener mAdapterCallBackListener;
    // 聊天数据缓存
    private SpLocalCache<ListCache> mSpLocalCache;
    private ListCache<JkChatBean> mListCache;

    /**
     * Constructor
     * @author leibing
     * @createTime 2017/5/11
     * @lastModify 2017/5/11
     * @param context 上下文
     * @param mData 数据源
     * @return
     */
    public JkChatAdapter(Context context, ArrayList<JkChatBean> mData){
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mData = mData;
        mSpLocalCache = new SpLocalCache<>(ListCache.class, JkChatBean.class);
        mListCache = new ListCache<>();
    }

    /**
     * 局部刷新数据
     * @author leibing
     * @createTime 2017/5/17
     * @lastModify 2017/5/17
     * @param view
     * @param mJkChatBean
     * @return
     */
    public void updateView(View view, JkChatBean mJkChatBean){
        if(view == null){
            return;
        }
        // 从view中取得holder
        ViewHolder holder = (ViewHolder) view.getTag();
        // 局部更新ui
        holder.updateUi(mJkChatBean);
        // 滑动到底部
        if (mAdapterCallBackListener != null)
            mAdapterCallBackListener.msgSlideBottom();
    }

    /**
      * 设置屏幕宽度
      * @author leibing
      * @createTime 2017/5/16
      * @lastModify 2017/5/16
      * @param screenWidth 屏幕宽度
      * @return
      */
    public void setScreenWidth(int screenWidth){
        this.screenWidth = screenWidth;
    }

    /**
     * 设置数据源
     * @author leibing
     * @createTime 2017/5/11
     * @lastModify 2017/5/11
     * @param mData 数据源
     * @return
     */
    public void setData(ArrayList<JkChatBean> mData){
        if (mData == null)
            return;
        try {
            this.mData = timeDealForData(mData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JkChatAdapter.this.notifyDataSetChanged();
        // 滑动到底部
        if (mAdapterCallBackListener != null)
            mAdapterCallBackListener.msgSlideBottom();
    }

    /**
      * 对数据源做时间处理
      * @author leibing
      * @createTime 2017/5/18
      * @lastModify 2017/5/18
      * @param mData 数据源
      * @return
      */
    public ArrayList<JkChatBean> timeDealForData(ArrayList<JkChatBean> mData) throws ParseException {
        long lastTime = 0;
       for (int i=0; i< mData.size();i++){
            JkChatBean mJkChatBean = mData.get(i);
           if (mJkChatBean != null){
               switch (mJkChatBean.msgDirection){
                   case JkChatConstant.MSG_DIRECTION_SEND:
                       // 发送消息
                       // 根据消息类型显示
                       switch (mJkChatBean.msgType){
                           case MSG_TYPE_TXT:
                           case JkChatConstant.MSG_TYPE_LINK:
                               // 文本消息
                               // 链接
                               if (mJkChatBean.mSendTxtMsg != null
                                       && StringUtil.isNotEmpty(mJkChatBean.mSendTxtMsg.timeStr)){
                                    long msgTime = DateUtils.stringToLong(
                                            mJkChatBean.mSendTxtMsg.timeStr, DateUtils.TIME_FORMAT);
                                   long divTime = msgTime - lastTime;
                                   if (divTime > MSG_DIV_SHOW_TIME){
                                       // 大于MSG_DIV_SHOW_TIME
                                       mData.get(i).mSendTxtMsg.isNeedShowTime = true;
                                   }else {
                                       mData.get(i).mSendTxtMsg.isNeedShowTime = false;
                                   }
                                   lastTime = msgTime;
                               }
                               break;
                           case JkChatConstant.MSG_TYPE_IMG:
                               // 图片消息
                               if (mJkChatBean.mSendImgMsg != null
                                       && StringUtil.isNotEmpty(mJkChatBean.mSendImgMsg.timeStr)){
                                   long msgTime = DateUtils.stringToLong(
                                           mJkChatBean.mSendImgMsg.timeStr, DateUtils.TIME_FORMAT);
                                   long divTime = msgTime - lastTime;
                                   if (divTime > MSG_DIV_SHOW_TIME){
                                       // 大于MSG_DIV_SHOW_TIME
                                       mData.get(i).mSendImgMsg.isNeedShowTime = true;
                                   }else {
                                       mData.get(i).mSendImgMsg.isNeedShowTime = false;
                                   }
                                   lastTime = msgTime;
                               }
                               break;
                           case JkChatConstant.MSG_TYPE_SEND_DRUG:
                               // 发送药品信息
                               if (mJkChatBean.mSendDrugMsg != null
                                       && StringUtil.isNotEmpty(mJkChatBean.mSendDrugMsg.timeStr)){
                                   long msgTime = DateUtils.stringToLong(
                                           mJkChatBean.mSendDrugMsg.timeStr, DateUtils.TIME_FORMAT);
                                   long divTime = msgTime - lastTime;
                                   if (divTime > MSG_DIV_SHOW_TIME){
                                       // 大于MSG_DIV_SHOW_TIME
                                       mData.get(i).mSendDrugMsg.isNeedShowTime = true;
                                   }else {
                                       mData.get(i).mSendDrugMsg.isNeedShowTime = false;
                                   }
                                   lastTime = msgTime;
                               }
                               break;
                       }
                       break;
                   case JkChatConstant.MSG_DIRECTION_RECEIVE:
                       // 接收消息
                       // 根据消息类型显示
                       switch (mJkChatBean.msgType){
                           case MSG_TYPE_TXT:
                           case JkChatConstant.MSG_TYPE_LINK:
                               // 文本消息
                               // 链接
                               if (mJkChatBean.mReceiveTxtMsg != null
                                       && StringUtil.isNotEmpty(mJkChatBean.mReceiveTxtMsg.timeStr)){
                                   long msgTime = DateUtils.stringToLong(
                                           mJkChatBean.mReceiveTxtMsg.timeStr, DateUtils.TIME_FORMAT);
                                   long divTime = msgTime - lastTime;
                                   if (divTime > MSG_DIV_SHOW_TIME){
                                       // 大于MSG_DIV_SHOW_TIME
                                       mData.get(i).mReceiveTxtMsg.isNeedShowTime = true;
                                   }else {
                                       mData.get(i).mReceiveTxtMsg.isNeedShowTime = false;
                                   }
                                   lastTime = msgTime;
                               }
                               break;
                           case JkChatConstant.MSG_TYPE_IMG:
                               // 图片消息
                               if (mJkChatBean.mReceiveImgMsg != null
                                       && StringUtil.isNotEmpty(mJkChatBean.mReceiveImgMsg.timeStr)){
                                   long msgTime = DateUtils.stringToLong(
                                           mJkChatBean.mReceiveImgMsg.timeStr, DateUtils.TIME_FORMAT);
                                   long divTime = msgTime - lastTime;
                                   if (divTime > MSG_DIV_SHOW_TIME){
                                       // 大于MSG_DIV_SHOW_TIME
                                       mJkChatBean.mReceiveImgMsg.isNeedShowTime = true;
                                   }else {
                                       mJkChatBean.mReceiveImgMsg.isNeedShowTime = false;
                                   }
                                   lastTime = msgTime;
                               }
                               break;
                       }
                       break;
                   default:
                       break;
               }
               }
           }
        return mData;
    }

    /**
     * 保存缓存数据
     * @author leibing
     * @createTime 2017/5/25
     * @lastModify 2017/5/25
     * @param
     * @return
     */
    public void saveChatCache(){
        saveChatCache(mData);
    }

    /**
     * 保存缓存数据
     * @author leibing
     * @createTime 2017/5/18
     * @lastModify 2017/5/18
     * @param mData 数据源
     * @return
     */
    public void saveChatCache(ArrayList<JkChatBean> mData){
        // 缓存聊天数据
        if (mListCache != null && mSpLocalCache != null) {
            ArrayList needSavaList;
            // 当缓存大于50条数据时只缓存最后50条数据
            if (mData != null && mData.size() !=0) {
                int size = mData.size();
                if (size > 50){
                    needSavaList = new ArrayList(mData.subList(size-50, size));
                }else {
                    needSavaList = mData;
                }
                mListCache.setObjList(needSavaList);
                mSpLocalCache.save(ShareApplication.getInstance(), mListCache);
            }
        }
    }

    /**
      * 设置适配器监听
      * @author leibing
      * @createTime 2017/5/17
      * @lastModify 2017/5/17
      * @param mAdapterCallBackListener 适配器监听
      * @return
      */
    public void setAdapterCallBackListener(AdapterCallBackListener mAdapterCallBackListener){
        this.mAdapterCallBackListener = mAdapterCallBackListener;
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return mData != null ? mData.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null){
            view = mLayoutInflater.inflate(R.layout.item_jk_chat, null);
            viewHolder = new ViewHolder(view, screenWidth);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (mData != null
                && mData.size() != 0
                && i< mData.size()){
            // 更新ui
            viewHolder.updateUi(mData.get(i));
            // 设置onClick监听
            viewHolder.setOnClick(new AdapterCallBackListener() {
                @Override
                public void retryMsg(int msgIndex, int msgType) {
                    if (mAdapterCallBackListener != null){
                        if (msgType == MSG_TYPE_TXT){
                            msgType = mData.get(i).msgType;
                        }
                        mAdapterCallBackListener.retryMsg(i, msgType);
                    }
                }

                @Override
                public void sendDrugMsg(JkChatBean.Drug mDrug) {
                    if (mAdapterCallBackListener != null){
                        mAdapterCallBackListener.sendDrugMsg(mData.get(i).mDrug);
                    }
                }

                @Override
                public void msgSlideBottom() {
                }

                @Override
                public void openLargerPic(String url) {
                    if (mAdapterCallBackListener != null){
                        if (mData.get(i).mReceiveImgMsg != null){
                            mAdapterCallBackListener.
                                    openLargerPic(mData.get(i).mReceiveImgMsg.msgImgUrl);
                        }else if (mData.get(i).mSendImgMsg != null){
                            mAdapterCallBackListener.
                                    openLargerPic(mData.get(i).mSendImgMsg.msgImgUrl);
                        }
                    }
                }
            });
        }

        return view;
    }

    /**
     * @className: ViewHolder
     * @classDescription: 聊天item视图容器
     * @author: leibing
     * @createTime: 2017/5/11
     */
    static class ViewHolder{
        // 屏幕宽度
        public int screenWidth;
        // 药品信息消息
        public LinearLayout drugInformationLy;
        public ImageView drugPicIv;
        public TextView drugNameTv;
        public TextView drugEffectTv;
        public TextView drugPriceTv;
        public Button sendDrugLinkBtn;
        // 接收图片消息
        public LinearLayout receiveImgMsgLy;
        public TextView receiveImgMsgTimeTv;
        public ImageView receiveImgMsgHeadIv;
        public ImageView receiveImgMsgContentIv;
        // 接收文本消息
        public LinearLayout receiveTxtMsgLy;
        public TextView receiveTxtMsgTimeTv;
        public ImageView receiveTxtMsgHeadIv;
        public TextView receiveTxtMsgContentTv;
        // 发送药品消息
        public LinearLayout sendDrugMsgsLy;
        public TextView sendDrugMsgTimeTv;
        public ImageView sendDrugMsgHeadIv;
        public ImageView sendDrugMsgDrugHeadIv;
        public TextView sendDrugMsgNameTv;
        public TextView sendDrugMsgEffectTv;
        public ImageView sendDrugMsgFailIv;
        // 发送图片消息
        public LinearLayout sendImgMsgLy;
        public TextView sendImgMsgTimeTv;
        public ImageView sendImgMsgHeadIv;
        public ImageView sendImgMsgContentIv;
        public ImageView sendImgMsgFailIv;
        public ProgressBar sendImgMsgPb;
        // 发送文本消息
        public LinearLayout sendTxtMsgLy;
        public TextView sendTxtMsgTimeTv;
        public ImageView sendTxtMsgHeadIv;
        public TextView sendTxtMsgContentTv;
        public ImageView sendTxtMsgFailIv;

        /**
         * Construction
         * @author leibing
         * @createTime 2017/5/11
         * @lastModify 2017/5/11
         * @param view
         * @return
         */
        public ViewHolder(View view, int screenWidth){
            // 屏幕宽度
            this.screenWidth = screenWidth;
            // 药品信息消息
            drugInformationLy = (LinearLayout) view.findViewById(R.id.ly_drug_information);
            drugPicIv = (ImageView) view.findViewById(R.id.iv_drug_pic);
            drugNameTv = (TextView) view.findViewById(R.id.tv_drug_name);
            drugEffectTv = (TextView) view.findViewById(R.id.tv_drug_effect);
            drugPriceTv = (TextView) view.findViewById(R.id.tv_drug_price);
            sendDrugLinkBtn = (Button) view.findViewById(R.id.btn_send_drug_link);
            // 接收图片消息
            receiveImgMsgLy = (LinearLayout) view.findViewById(R.id.ly_receive_img_msg);
            receiveImgMsgTimeTv = (TextView) view.findViewById(R.id.tv_receive_img_msg_time);
            receiveImgMsgHeadIv = (ImageView) view.findViewById(R.id.iv_receive_img_msg_head);
            receiveImgMsgContentIv = (ImageView) view.findViewById(R.id.iv_receive_img_msg_content);
            // 接收文本消息
            receiveTxtMsgLy = (LinearLayout) view.findViewById(R.id.ly_receive_txt_msg);
            receiveTxtMsgTimeTv = (TextView) view.findViewById(R.id.tv_receive_txt_msg_time);
            receiveTxtMsgHeadIv = (ImageView) view.findViewById(R.id.iv_receive_txt_msg_head);
            receiveTxtMsgContentTv = (TextView) view.findViewById(R.id.tv_receive_txt_msg_content);
            // 发送药品消息
            sendDrugMsgsLy = (LinearLayout) view.findViewById(R.id.ly_send_drug_msgs);
            sendDrugMsgTimeTv = (TextView) view.findViewById(R.id.tv_send_drug_msg_time);
            sendDrugMsgHeadIv = (ImageView) view.findViewById(R.id.iv_send_drug_msg_head);
            sendDrugMsgDrugHeadIv = (ImageView) view.findViewById(R.id.iv_send_drug_msg_drug_head);
            sendDrugMsgNameTv = (TextView) view.findViewById(R.id.tv_send_drug_msg_name);
            sendDrugMsgEffectTv = (TextView) view.findViewById(R.id.tv_send_drug_msg_effect);
            sendDrugMsgFailIv = (ImageView) view.findViewById(R.id.iv_send_drug_msg_fail);
            // 发送图片消息
            sendImgMsgLy = (LinearLayout) view.findViewById(R.id.ly_send_img_msg);
            sendImgMsgTimeTv = (TextView) view.findViewById(R.id.tv_send_img_msg_time);
            sendImgMsgHeadIv = (ImageView) view.findViewById(R.id.iv_send_img_msg_head);
            sendImgMsgContentIv = (ImageView) view.findViewById(R.id.iv_send_img_msg_content);
            sendImgMsgFailIv = (ImageView) view.findViewById(R.id.iv_send_img_msg_fail);
            sendImgMsgPb = (ProgressBar) view.findViewById(R.id.pb_send_img_msg);
            // 发送文本消息
            sendTxtMsgLy = (LinearLayout) view.findViewById(R.id.ly_send_txt_msg);
            sendTxtMsgTimeTv = (TextView) view.findViewById(R.id.tv_send_txt_msg_time);
            sendTxtMsgHeadIv = (ImageView) view.findViewById(R.id.iv_send_txt_msg_head);
            sendTxtMsgContentTv = (TextView) view.findViewById(R.id.tv_send_txt_msg_content);
            sendTxtMsgFailIv = (ImageView) view.findViewById(R.id.iv_send_txt_msg_fail);
        }

        /**
          * 设置点击事件监听
          * @author leibing
          * @createTime 2017/5/17
          * @lastModify 2017/5/17
          * @param
          * @return
          */
        public void setOnClick(final AdapterCallBackListener mListener){
            if (mListener == null)
                return;
            // 发送药品消息
            sendDrugMsgFailIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.retryMsg(0, JkChatConstant.MSG_TYPE_SEND_DRUG);
                }
            });
            // 发送图片消息
            sendImgMsgFailIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.retryMsg(0, JkChatConstant.MSG_TYPE_IMG);
                }
            });
            sendImgMsgContentIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.openLargerPic(null);
                }
            });
            // 发送文本消息
            sendTxtMsgFailIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.retryMsg(0, MSG_TYPE_TXT);
                }
            });

            // 药品信息消息
            sendDrugLinkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.sendDrugMsg(null);
                }
            });
            // 接收图片消息
            receiveImgMsgContentIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.openLargerPic(null);
                }
            });
        }

        /**
         * 更新ui
         * @author leibing
         * @createTime 2017/5/11
         * @lastModify 2017/5/11
         * @param mJkChatBean 数据源
         * @return
         */
        public void updateUi(JkChatBean mJkChatBean){
            if (mJkChatBean == null)
                return;
            // 根据消息方向显示Ui
            switch (mJkChatBean.msgDirection){
                case JkChatConstant.MSG_DIRECTION_SEND:
                    // 发送消息
                    // 根据消息类型显示
                    switch (mJkChatBean.msgType){
                        case MSG_TYPE_TXT:
                            // 文本消息
                            sendTxtMsgUiShow(mJkChatBean, false);
                            break;
                        case JkChatConstant.MSG_TYPE_IMG:
                            // 图片消息
                            sendImgMsgUiShow(mJkChatBean);
                            break;
                        case JkChatConstant.MSG_TYPE_DRUG:
                            // 药品信息
                            drugInformationUiShow(mJkChatBean);
                            break;
                        case JkChatConstant.MSG_TYPE_SEND_DRUG:
                            // 发送药品消息
                            sendDrugMsgUiShow(mJkChatBean);
                            break;
                        case JkChatConstant.MSG_TYPE_LINK:
                            // 链接
                            // 文本消息
                            sendTxtMsgUiShow(mJkChatBean, true);
                            break;
                    }
                    break;
                case JkChatConstant.MSG_DIRECTION_RECEIVE:
                    // 接收消息
                    // 根据消息类型显示
                    switch (mJkChatBean.msgType){
                        case MSG_TYPE_TXT:
                            // 文本消息
                            receiveTxtMsgUiShow(mJkChatBean, false);
                            break;
                        case JkChatConstant.MSG_TYPE_IMG:
                            // 图片消息
                            receiveImgMsgUiShow(mJkChatBean);
                            break;
                        case JkChatConstant.MSG_TYPE_LINK:
                            // 链接
                            // 文本消息
                            receiveTxtMsgUiShow(mJkChatBean, true);
                            break;
                    }
                    break;
                default:
                    break;
            }
        }

        /**
         * 药品信息ui显示
         * @author leibing
         * @createTime 2017/5/11
         * @lastModify 2017/5/11
         * @param mJkChatBean 数据源
         * @return
         */
        private void drugInformationUiShow(JkChatBean mJkChatBean){
            drugInformationLy.setVisibility(View.VISIBLE);
            receiveImgMsgLy.setVisibility(View.GONE);
            receiveTxtMsgLy.setVisibility(View.GONE);
            sendDrugMsgsLy.setVisibility(View.GONE);
            sendImgMsgLy.setVisibility(View.GONE);
            sendTxtMsgLy.setVisibility(View.GONE);
            if (mJkChatBean.mDrug != null){
                // 药品图片
                if (StringUtil.isNotEmpty(mJkChatBean.mDrug.drugImgUrl))
                    ImageLoader.getInstance().load(
                        ShareApplication.getInstance().getApplicationContext(),
                        drugPicIv,
                        mJkChatBean.mDrug.drugImgUrl,
                            ShareApplication.getInstance()
                                    .getResources().getDrawable(R.drawable.icon_product_defoult));
                // 药品名称
                if (StringUtil.isNotEmpty(mJkChatBean.mDrug.drugName))
                    drugNameTv.setText(mJkChatBean.mDrug.drugName);
                // 药品描述
                if (StringUtil.isNotEmpty(mJkChatBean.mDrug.drugDescription))
                    drugEffectTv.setText(mJkChatBean.mDrug.drugDescription);
                // 药品价格
                if (StringUtil.strIsNum(mJkChatBean.mDrug.drugPrice)){
                    drugPriceTv.setText("￥" + JkChatUtils.doubleTwoDecimal(
                            Double.parseDouble(mJkChatBean.mDrug.drugPrice)/100));
                }
            }
        }
        
        /**
         * 接收图片消息ui显示
         * @author leibing
         * @createTime 2017/5/11
         * @lastModify 2017/5/11
         * @param mJkChatBean 数据源
         * @return
         */
        private void receiveImgMsgUiShow(JkChatBean mJkChatBean){
            drugInformationLy.setVisibility(View.GONE);
            receiveImgMsgLy.setVisibility(View.VISIBLE);
            receiveTxtMsgLy.setVisibility(View.GONE);
            sendDrugMsgsLy.setVisibility(View.GONE);
            sendImgMsgLy.setVisibility(View.GONE);
            sendTxtMsgLy.setVisibility(View.GONE);
            if (mJkChatBean.mReceiveImgMsg != null){
                // 时间
                if (mJkChatBean.mReceiveImgMsg.isNeedShowTime){
                    receiveImgMsgTimeTv.setVisibility(View.VISIBLE);
                    receiveImgMsgTimeTv.setText(mJkChatBean.mReceiveImgMsg.timeStr);
                }else {
                    receiveImgMsgTimeTv.setVisibility(View.GONE);
                }
                // 头像
                // 保证医生头像一致 add by leibing 2017/5/24
                mJkChatBean.mReceiveImgMsg.headPortraitUrl
                        = ImageLoader.getDrawableSource(ShareApplication.getInstance(),
                        R.drawable.doctor_head_portrait);
                if (StringUtil.isNotEmpty(mJkChatBean.mReceiveImgMsg.headPortraitUrl))
                    ImageLoader.getInstance().load(
                            ShareApplication.getInstance().getApplicationContext(),
                            receiveImgMsgHeadIv,
                            mJkChatBean.mReceiveImgMsg.headPortraitUrl,
                            ShareApplication.getInstance()
                                    .getResources().getDrawable(R.drawable.doctor_head_portrait),
                            ShareApplication.getInstance()
                                    .getResources().getDrawable(R.drawable.doctor_head_portrait),
                            true);
                // 消息图片
                if (StringUtil.isNotEmpty(mJkChatBean.mReceiveImgMsg.msgImgUrl)) {
                    if (mJkChatBean.mReceiveImgMsg.msgImgUrl.contains(".gif")){
                        ImageView newIv = ImageLoader.getInstance().resetImgWidthHeight(receiveImgMsgContentIv,
                                100,
                                100,
                                screenWidth / 8);
                        ImageLoader.getInstance().loadAsGif(
                                ShareApplication.getInstance().getApplicationContext(),
                                newIv,
                                mJkChatBean.mReceiveImgMsg.msgImgUrl);
                    }else {
                        ImageView newIv = ImageLoader.getInstance().resetImgWidthHeight(receiveImgMsgContentIv,
                                mJkChatBean.mReceiveImgMsg.width,
                                mJkChatBean.mReceiveImgMsg.height,
                                screenWidth / 4);
                        ImageLoader.getInstance().load(
                                ShareApplication.getInstance().getApplicationContext(),
                                newIv,
                                mJkChatBean.mReceiveImgMsg.msgImgUrl);
                    }
                }
            }
        }

        /**
         * 接收文本消息ui显示
         * @author leibing
         * @createTime 2017/5/11
         * @lastModify 2017/5/11
         * @param mJkChatBean 数据源
         * @param isLink 是否超链接
         * @return
         */
        private void receiveTxtMsgUiShow(JkChatBean mJkChatBean, boolean isLink){
            drugInformationLy.setVisibility(View.GONE);
            receiveImgMsgLy.setVisibility(View.GONE);
            receiveTxtMsgLy.setVisibility(View.VISIBLE);
            sendDrugMsgsLy.setVisibility(View.GONE);
            sendImgMsgLy.setVisibility(View.GONE);
            sendTxtMsgLy.setVisibility(View.GONE);
            if (mJkChatBean.mReceiveTxtMsg != null){
                // 时间
                if (mJkChatBean.mReceiveTxtMsg.isNeedShowTime){
                    receiveTxtMsgTimeTv.setVisibility(View.VISIBLE);
                    receiveTxtMsgTimeTv.setText(mJkChatBean.mReceiveTxtMsg.timeStr);
                }else {
                    receiveTxtMsgTimeTv.setVisibility(View.GONE);
                }
                // 头像
                // 保证医生头像一致 add by leibing 2017/5/24
                mJkChatBean.mReceiveTxtMsg.headPortraitUrl
                        = ImageLoader.getDrawableSource(ShareApplication.getInstance(),
                        R.drawable.doctor_head_portrait);
                if (StringUtil.isNotEmpty(mJkChatBean.mReceiveTxtMsg.headPortraitUrl))
                    ImageLoader.getInstance().load(
                            ShareApplication.getInstance().getApplicationContext(),
                            receiveTxtMsgHeadIv,
                            mJkChatBean.mReceiveTxtMsg.headPortraitUrl,
                            ShareApplication.getInstance()
                                    .getResources().getDrawable(R.drawable.doctor_head_portrait),
                            ShareApplication.getInstance()
                                    .getResources().getDrawable(R.drawable.doctor_head_portrait),
                            true);
                // 消息文本
                if (StringUtil.isNotEmpty(mJkChatBean.mReceiveTxtMsg.msgTxt)){
                    if (isLink){
                        String webLinkText = "<a href='"+ mJkChatBean.mReceiveTxtMsg.msgTxt +"'> "
                                + mJkChatBean.mReceiveTxtMsg.msgTxt +"</a>";
                        receiveTxtMsgContentTv.setText(Html.fromHtml(webLinkText));
                    }else {
                        receiveTxtMsgContentTv.setText(mJkChatBean.mReceiveTxtMsg.msgTxt);
                    }
                }
            }
        }

        /**
         * 发送药品消息ui显示
         * @author leibing
         * @createTime 2017/5/11
         * @lastModify 2017/5/11
         * @param mJkChatBean 数据源
         * @return
         */
        private void sendDrugMsgUiShow(JkChatBean mJkChatBean){
            drugInformationLy.setVisibility(View.GONE);
            receiveImgMsgLy.setVisibility(View.GONE);
            receiveTxtMsgLy.setVisibility(View.GONE);
            sendDrugMsgsLy.setVisibility(View.VISIBLE);
            sendImgMsgLy.setVisibility(View.GONE);
            sendTxtMsgLy.setVisibility(View.GONE);
            if (mJkChatBean.mSendDrugMsg != null){
                // 时间
                if (mJkChatBean.mSendDrugMsg.isNeedShowTime){
                    sendDrugMsgTimeTv.setVisibility(View.VISIBLE);
                    sendDrugMsgTimeTv.setText(mJkChatBean.mSendDrugMsg.timeStr);
                }else {
                    sendDrugMsgTimeTv.setVisibility(View.GONE);
                }
                // 头像
                // 为保证用户头像一致，直接取用户当前头像地址 add by leibing 2017/5/23
                mJkChatBean.mSendDrugMsg.headPortraitUrl
                        = ImageLoader.getDrawableSource(ShareApplication.getInstance(),
                        R.drawable.default_headportrait);
                if (StringUtil.isNotEmpty(mJkChatBean.mSendDrugMsg.headPortraitUrl))
                    ImageLoader.getInstance().load(
                            ShareApplication.getInstance().getApplicationContext(),
                            sendDrugMsgHeadIv,
                            mJkChatBean.mSendDrugMsg.headPortraitUrl,
                            ShareApplication.getInstance()
                                    .getResources().getDrawable(R.drawable.default_headportrait),
                            ShareApplication.getInstance()
                                    .getResources().getDrawable(R.drawable.default_headportrait),
                            true);
                // 药品图片
                if (StringUtil.isNotEmpty(mJkChatBean.mSendDrugMsg.drugImgUrl))
                    ImageLoader.getInstance().load(
                            ShareApplication.getInstance().getApplicationContext(),
                            sendDrugMsgDrugHeadIv,
                            mJkChatBean.mSendDrugMsg.drugImgUrl,
                            ShareApplication.getInstance()
                                    .getResources().getDrawable(R.drawable.icon_product_defoult));
                //  药品名称
                if (StringUtil.isNotEmpty(mJkChatBean.mSendDrugMsg.drugName))
                    sendDrugMsgNameTv.setText(mJkChatBean.mSendDrugMsg.drugName);
                // 药品描述
                if (StringUtil.isNotEmpty(mJkChatBean.mSendDrugMsg.drugDescription))
                    sendDrugMsgEffectTv.setText(mJkChatBean.mSendDrugMsg.drugDescription);
            }
            // 根据图片发送状态显示
            switch (mJkChatBean.msgStatus){
                case MSG_STATUS_SEND_FAIL:
                    // 发送失败
                    sendDrugMsgFailIv.setVisibility(View.VISIBLE);
                    break;
                case MSG_STATUS_SEND_SUCCESS:
                    // 发送成功
                    sendDrugMsgFailIv.setVisibility(View.GONE);
                    break;
            }
        }

        /**
         * 发送图片消息ui显示
         * @author leibing
         * @createTime 2017/5/11
         * @lastModify 2017/5/11
         * @param mJkChatBean 数据源
         * @return
         */
        private void sendImgMsgUiShow(JkChatBean mJkChatBean){
            drugInformationLy.setVisibility(View.GONE);
            receiveImgMsgLy.setVisibility(View.GONE);
            receiveTxtMsgLy.setVisibility(View.GONE);
            sendDrugMsgsLy.setVisibility(View.GONE);
            sendImgMsgLy.setVisibility(View.VISIBLE);
            sendTxtMsgLy.setVisibility(View.GONE);
            if (mJkChatBean.mSendImgMsg != null){
                // 时间
                if (mJkChatBean.mSendImgMsg.isNeedShowTime){
                    sendImgMsgTimeTv.setVisibility(View.VISIBLE);
                    sendImgMsgTimeTv.setText(mJkChatBean.mSendImgMsg.timeStr);
                }else {
                    sendImgMsgTimeTv.setVisibility(View.GONE);
                }
                // 头像
                // 为保证用户头像一致，直接取用户当前头像地址 add by leibing 2017/5/23
                mJkChatBean.mSendImgMsg.headPortraitUrl
                        = ImageLoader.getDrawableSource(ShareApplication.getInstance(),
                        R.drawable.default_headportrait);
                if (StringUtil.isNotEmpty(mJkChatBean.mSendImgMsg.headPortraitUrl))
                    ImageLoader.getInstance().load(
                            ShareApplication.getInstance().getApplicationContext(),
                            sendImgMsgHeadIv,
                            mJkChatBean.mSendImgMsg.headPortraitUrl,
                            ShareApplication.getInstance()
                                    .getResources().getDrawable(R.drawable.default_headportrait),
                            ShareApplication.getInstance()
                                    .getResources().getDrawable(R.drawable.default_headportrait),
                            true);
                // 消息图片
                if (StringUtil.isNotEmpty(mJkChatBean.mSendImgMsg.msgImgUrl)) {
                    ImageView newIv = ImageLoader.getInstance().resetImgWidthHeight(sendImgMsgContentIv,
                            mJkChatBean.mSendImgMsg.width,
                            mJkChatBean.mSendImgMsg.height,
                            screenWidth/4);
                    ImageLoader.getInstance().load(
                            ShareApplication.getInstance().getApplicationContext(),
                            newIv,
                            mJkChatBean.mSendImgMsg.msgImgUrl);
                }
            }
            // 根据图片发送状态显示
            switch (mJkChatBean.msgStatus){
                case MSG_STATUS_SEND_FAIL:
                    // 发送失败
                    sendImgMsgPb.setVisibility(View.GONE);
                    sendImgMsgFailIv.setVisibility(View.VISIBLE);
                    break;
                case MSG_STATUS_SEND_SUCCESS:
                    // 发送成功
                    sendImgMsgPb.setVisibility(View.GONE);
                    sendImgMsgFailIv.setVisibility(View.GONE);
                    break;
                case MSG_STATUS_SENDING:
                    // 发送中
                    sendImgMsgPb.setVisibility(View.VISIBLE);
                    sendImgMsgFailIv.setVisibility(View.GONE);
                    break;
            }
        }

        /**
         * 发送文本消息
         * @author leibing
         * @createTime 2017/5/11
         * @lastModify 2017/5/11
         * @param mJkChatBean 数据源
         * @param isLink 是否超链接
         * @return
         */
        private void sendTxtMsgUiShow(JkChatBean mJkChatBean, boolean isLink){
            drugInformationLy.setVisibility(View.GONE);
            receiveImgMsgLy.setVisibility(View.GONE);
            receiveTxtMsgLy.setVisibility(View.GONE);
            sendDrugMsgsLy.setVisibility(View.GONE);
            sendImgMsgLy.setVisibility(View.GONE);
            sendTxtMsgLy.setVisibility(View.VISIBLE);
            if (mJkChatBean.mSendTxtMsg != null){
                // 时间
                if (mJkChatBean.mSendTxtMsg.isNeedShowTime){
                    sendTxtMsgTimeTv.setVisibility(View.VISIBLE);
                    sendTxtMsgTimeTv.setText(mJkChatBean.mSendTxtMsg.timeStr);
                }else {
                    sendTxtMsgTimeTv.setVisibility(View.GONE);
                }
                // 头像
                // 为保证用户头像一致，直接取用户当前头像地址 add by leibing 2017/5/23
                mJkChatBean.mSendTxtMsg.headPortraitUrl
                        = ImageLoader.getDrawableSource(ShareApplication.getInstance(),
                        R.drawable.default_headportrait);
                if (StringUtil.isNotEmpty(mJkChatBean.mSendTxtMsg.headPortraitUrl))
                    ImageLoader.getInstance().load(
                            ShareApplication.getInstance().getApplicationContext(),
                            sendTxtMsgHeadIv,
                            mJkChatBean.mSendTxtMsg.headPortraitUrl,
                            ShareApplication.getInstance()
                                    .getResources().getDrawable(R.drawable.default_headportrait),
                            ShareApplication.getInstance()
                                    .getResources().getDrawable(R.drawable.default_headportrait),
                            true);
                // 消息文本
                if (StringUtil.isNotEmpty(mJkChatBean.mSendTxtMsg.msgTxt)) {
                    if (isLink){
                        String webLinkText = "<a href='"+ mJkChatBean.mSendTxtMsg.msgTxt +"'> "
                                + mJkChatBean.mSendTxtMsg.msgTxt +"</a>";
                        sendTxtMsgContentTv.setText(Html.fromHtml(webLinkText));
                    }else {
                        sendTxtMsgContentTv.setText(mJkChatBean.mSendTxtMsg.msgTxt);
                    }
                }
                // 根据文本发送状态显示
                switch (mJkChatBean.msgStatus){
                    case MSG_STATUS_SEND_FAIL:
                        // 发送失败
                        sendTxtMsgFailIv.setVisibility(View.VISIBLE);
                        break;
                    case MSG_STATUS_SEND_SUCCESS:
                        // 发送成功
                        sendTxtMsgFailIv.setVisibility(View.GONE);
                        break;
                }
            }
        }
    }

    /**
     * @interfaceName: AdapterCallBackListener
     * @interfaceDescription: 适配器事件监听
     * @author: leibing
     * @createTime: 2017/5/17
     */
    public interface AdapterCallBackListener{
        // 重发消息
        void retryMsg(int msgIndex, int msgType);
        // 发送药品信息
        void sendDrugMsg(JkChatBean.Drug mDrug);
        // 消息滑动到底部
        void msgSlideBottom();
        // 打开大图
        void openLargerPic(String url);
    }
}
