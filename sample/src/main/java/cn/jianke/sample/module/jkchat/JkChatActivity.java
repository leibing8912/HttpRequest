package cn.jianke.sample.module.jkchat;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jianke.httprequest.module.AppManager;
import cn.jianke.httprequest.utils.StringUtil;
import cn.jianke.sample.R;
import cn.jianke.sample.module.BaseActivity;
import cn.jianke.sample.module.ImageLoader;
import cn.jianke.sample.module.ShareApplication;
import cn.jianke.sample.module.jkchat.bean.JkChatBean;
import cn.jianke.sample.module.jkchat.util.FileUtils;
import cn.jianke.sample.module.jkchat.util.ToastUtils;
import cn.jianke.sample.widget.PictureShowDialog;
import static cn.jianke.sample.module.jkchat.JkChatConstant.BOTTOM_VIEW_SHOW_CONTINUE_CONSULTING;
import static cn.jianke.sample.module.jkchat.JkChatConstant.BOTTOM_VIEW_SHOW_INPUT;
import static cn.jianke.sample.module.jkchat.JkChatConstant.BOTTOM_VIEW_SHOW_PRAISE;
import static cn.jianke.sample.module.jkchat.JkChatConstant.CONTINUE_ASK_NO_NETWORK_TIP;
import static cn.jianke.sample.module.jkchat.JkChatConstant.GOTO_GOODS_DETAILS_PAGE;
import static cn.jianke.sample.module.jkchat.JkChatConstant.GOTO_GOODS_DETAILS_REQUEST_CODE;
import static cn.jianke.sample.module.jkchat.JkChatConstant.INTENT_DRUG_INFO;
import static cn.jianke.sample.module.jkchat.JkChatConstant.JK_CHAT_DISCONNECT_TIP;
import static cn.jianke.sample.module.jkchat.JkChatConstant.JK_CHAT_TITLE;
import static cn.jianke.sample.module.jkchat.JkChatConstant.MSG_TYPE_IMG;
import static cn.jianke.sample.module.jkchat.JkChatConstant.MSG_TYPE_LINK;
import static cn.jianke.sample.module.jkchat.JkChatConstant.MSG_TYPE_SEND_DRUG;
import static cn.jianke.sample.module.jkchat.JkChatConstant.MSG_TYPE_TXT;
import static cn.jianke.sample.module.jkchat.JkChatConstant.PIC_SELECT_FAIL;
import static cn.jianke.sample.module.jkchat.JkChatConstant.PLEASE_INPUT_TXT_TIP;
import static cn.jianke.sample.module.jkchat.JkChatConstant.PRAISE_FAIL;
import static cn.jianke.sample.module.jkchat.JkChatConstant.PRAISE_LEVEL_NO_PRAISE;
import static cn.jianke.sample.module.jkchat.JkChatConstant.PRAISE_NO_SELECT_TIP;
import static cn.jianke.sample.module.jkchat.JkChatConstant.PRAISE_SUCCESS;
import static cn.jianke.sample.module.jkchat.JkChatConstant.PROMPT_LINE_TIP_FOUR_STATUS;
import static cn.jianke.sample.module.jkchat.JkChatConstant.PROMPT_LINE_TIP_ONE_STATUS;
import static cn.jianke.sample.module.jkchat.JkChatConstant.PROMPT_LINE_TIP_SIX_STATUS;
import static cn.jianke.sample.module.jkchat.JkChatConstant.PROMPT_LINE_TIP_THREE_STATUS;
import static cn.jianke.sample.module.jkchat.JkChatConstant.SDCARD_NO_EXIST;
import static cn.jianke.sample.module.jkchat.JkChatConstant.SELECT_PIC_CODE;
import static cn.jianke.sample.module.jkchat.JkChatConstant.TAKE_PHOTO_CODE;

/**
 * @className: JkChatActivity
 * @classDescription: 健客商务通（UI层）
 * @author: leibing
 * @createTime: 2017/5/4
 */
public class JkChatActivity extends BaseActivity implements View.OnLayoutChangeListener{
    // activity最外层的Layout视图
    @BindView(R.id.fly_jk_chat_root_view)
    FrameLayout activityRootView;
    // actionbar右文案
    @BindView(R.id.tvMenu)
    TextView menuTv;
    // actionbar右按钮
    @BindView(R.id.btnMenu)
    ImageView menuBtn;
    // actionbar标题
    @BindView(R.id.tvTitle)
    TextView titleTv;
    // 提示栏容器
    @BindView(R.id.ly_jk_chat_tip)
    LinearLayout jkChatTipLy;
    // 对话状态提示
    @BindView(R.id.ly_jk_chat_tip_one)
    LinearLayout jkChatTipOneLy;
    // 对话状态提示--文案
    @BindView(R.id.tv_jk_chat_tip_one)
    TextView jkChatTipOneTv;
    // 网络状态差提示
    @BindView(R.id.ly_jk_chat_tip_two)
    LinearLayout jkChatTipTwoLy;
    // 发送消息容器
    @BindView(R.id.rly_send_msg)
    RelativeLayout sendMsgRly;
    // 其他功能容器
    @BindView(R.id.ly_other_function)
    LinearLayout otherFunctionLy;
    // 再次咨询容器
    @BindView(R.id.ly_consulting_again)
    LinearLayout consultingAgainLy;
    // 评价布局容器
    @BindView(R.id.rly_jk_chat_praise)
    RelativeLayout jkChatPraiseLy;
    // 服务评价星级1
    @BindView(R.id.iv_service_evaluation_star_one)
    ImageView serviceStarOneIv;
    // 服务评价星级2
    @BindView(R.id.iv_service_evaluation_star_two)
    ImageView serviceStarTwoIv;
    // 服务评价星级3
    @BindView(R.id.iv_service_evaluation_star_three)
    ImageView serviceStarThreeIv;
    // 服务评价星级4
    @BindView(R.id.iv_service_evaluation_star_four)
    ImageView serviceStarFourIv;
    // 服务评价星级5
    @BindView(R.id.iv_service_evaluation_star_five)
    ImageView serviceStarFiveIv;
    // 回复速度星级1
    @BindView(R.id.iv_reply_speed_star_one)
    ImageView replySpeedStarOneIv;
    // 回复速度星级2
    @BindView(R.id.iv_reply_speed_star_two)
    ImageView replySpeedStarTwoIv;
    // 回复速度星级3
    @BindView(R.id.iv_reply_speed_star_three)
    ImageView replySpeedStarThreeIv;
    // 回复速度星级4
    @BindView(R.id.iv_reply_speed_star_four)
    ImageView replySpeedStarFourIv;
    // 回复速度星级5
    @BindView(R.id.iv_reply_speed_star_five)
    ImageView replySpeedStarFiveIv;
    // 评价输入框
    @BindView(R.id.edt_praise_input)
    EditText praiseInputEdt;
    // 消息输入框
    @BindView(R.id.edt_send_msg)
    EditText sendMsgEdt;
    // 消息发送按钮
    @BindView(R.id.btn_send_msg)
    Button sendMsgBtn;
    // 其他功能
    @BindView(R.id.iv_open_other_function)
    ImageView otherFunctionIv;
    // 聊天列表
    @BindView(R.id.lv_jk_chat)
    ListView jkChatLv;
    // 删除、重发、复制操作窗体
    private PopupWindow popupWindow;
    // 适配器
    private JkChatAdapter mAdapter;
    // 数据源
    private ArrayList<JkChatBean> mData;
    // 定义图片的Uri
    private Uri photoUri;
    // 图片文件路径
    private String picPath;
    // ui thread handler
    private Handler uiHandler = new Handler(Looper.getMainLooper());
    // jk chat logical processing instance
    private JkChatViewModel mJkChatViewModel;
    // 屏幕宽度
    private int screenWidth = 0;
    //屏幕高度
    private int screenHeight = 0;
    // 软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    // 是否打开其他功能
    private boolean isOpenOtherFunction = false;
    // 服务等级
    private int serviceLevel = PRAISE_LEVEL_NO_PRAISE;
    // 回复速度等级
    private int replyLevel = PRAISE_LEVEL_NO_PRAISE;
    // 是否结束对话(用于点击结束对话之后逻辑处理)
    private boolean isCloseChat = false;
    // 连接状态
    private int connectStatus =  PROMPT_LINE_TIP_ONE_STATUS;
    // 是否item长按
    private boolean isItemLongOnClick = false;
    // 药品信息
    private JkChatBean.Drug mDrug;
    // 是否无医生在线
    private boolean isNoDoctorOnline = false;
    // jk chat logical processing listener
    private JkChatViewModel.ViewModelListener mViewModelListener
            = new JkChatViewModel.ViewModelListener() {
        @Override
        public void connect() {
            // 当对话断开时的布局显示(此时还未连接上医生，虽然连接上websocket,依然显示为断开时布局)
            disConnectedView();
            // 状态提示栏为正在为您转接......
            promptLineShow(PROMPT_LINE_TIP_ONE_STATUS, "");
            connectStatus = PROMPT_LINE_TIP_ONE_STATUS;
        }

        @Override
        public void connected(String doctorName) {
            // 当对话连接上时的布局显示
            connectedView();
            // 连接上并获取到医生名称
            promptLineShow(JkChatConstant.PROMPT_LINE_TIP_TWO_STATUS, doctorName);
            connectStatus = JkChatConstant.PROMPT_LINE_TIP_TWO_STATUS;
        }

        @Override
        public void closed() {
            if (isCloseChat) {
                isCloseChat = false;
                // 当对话断开时的布局显示
                disConnectedView();
                // 关闭对话
                promptLineShow(PROMPT_LINE_TIP_FOUR_STATUS, "");
                connectStatus = PROMPT_LINE_TIP_FOUR_STATUS;
                // 底部布局显示（评价页面）
                whatBottomViewShow(JkChatConstant.BOTTOM_VIEW_SHOW_PRAISE);
            }
        }

        @Override
        public void disConnect() {
            if (isNoDoctorOnline){
                isNoDoctorOnline = false;
                return;
            }
            // 当对话断开时的布局显示
            disConnectedView();
            // 断开连接时提示框显示
            promptLineShow(PROMPT_LINE_TIP_THREE_STATUS, "");
            connectStatus = PROMPT_LINE_TIP_THREE_STATUS;
            // 底部布局显示（继续咨询）
            whatBottomViewShow(JkChatConstant.BOTTOM_VIEW_SHOW_CONTINUE_CONSULTING);
        }

        @Override
        public void noDoctorOnline() {
            isNoDoctorOnline = true;
            // 当对话断开时的布局显示
            disConnectedView();
            // 断开连接时提示框显示
            promptLineShow(PROMPT_LINE_TIP_SIX_STATUS, "");
            connectStatus = PROMPT_LINE_TIP_SIX_STATUS;
            // 底部布局显示（继续咨询）
            whatBottomViewShow(JkChatConstant.BOTTOM_VIEW_SHOW_CONTINUE_CONSULTING);
        }

        @Override
        public void noNetWork() {
            if (isNoDoctorOnline){
                isNoDoctorOnline = false;
                return;
            }
            // 断开连接
            disConnect();
            // 无网提示
            ToastUtils.show(JkChatActivity.this, CONTINUE_ASK_NO_NETWORK_TIP);
        }

        @Override
        public void updatePanelView(final int count,
                                    final JkChatBean mJkChatBean, final int msgType) {
            // 更新局部item view
            if (uiHandler != null){
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mJkChatBean.msgStatus = msgType;
                        updateItem(count, mJkChatBean);
                    }
                });
            }
        }

        @Override
        public void removeDrugMsgItem() {
            // 删除药品信息item
            removeDrugMsgItems();
        }

        @Override
        public void updateUI(final Object object) {
            // 普通更新item ui
            if (object != null
                    && uiHandler != null){
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateListUi((JkChatBean) object);
                    }
                });
            }
        }

        @Override
        public void jkChatPraise(boolean isSuccess) {
            if (isSuccess){
                // 评价成功
                ToastUtils.show(JkChatActivity.this, PRAISE_SUCCESS);
                // 重置评价页面数据
                praisePageReset();
                // 底部布局显示（继续咨询）
                whatBottomViewShow(JkChatConstant.BOTTOM_VIEW_SHOW_CONTINUE_CONSULTING);
            }else {
                // 评价失败
                ToastUtils.show(JkChatActivity.this, PRAISE_FAIL);
            }
        }

        @Override
        public void setData(ArrayList data) {
            // 读取缓存或拿取服务器数据批量更新item ui
            if (mAdapter != null
                    && data != null){
                mData = data;
                mAdapter.setData(mData);
            }
            // 删除药品信息item
            removeDrugMsgItems();
            // send drug msg
            sendDrugMsg();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jk_chat);
        // bind buffer knife
        ButterKnife.bind(this);
        // init view
        initView();
        // set adapter
        setAdapter();
        // get intent data update ui
        getIntentDataUpdateUi();
        // init jk chat logical processing
        initJkChatViewModel();
    }

    /**
     * get intent data update ui
     * @author leibing
     * @createTime 2017/5/17
     * @lastModify 2017/5/17
     * @param
     * @return
     */
    private void getIntentDataUpdateUi() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            mDrug = (JkChatBean.Drug) bundle.getSerializable(INTENT_DRUG_INFO);
            String title = bundle.getString(JK_CHAT_TITLE);
            if (StringUtil.isNotEmpty(title)
                    && titleTv != null){
                // 设置默认标题栏
                titleTv.setText(title);
            }
        }
    }

    /**
     * 发送药品信息
     * @author leibing
     * @createTime 2017/5/25
     * @lastModify 2017/5/25
     * @param
     * @return
     */
    private void sendDrugMsg(){
        if (mDrug != null
                && mJkChatViewModel != null){
            mJkChatViewModel.showDrugMsg(mDrug);
        }
    }

    /**
     * 删除药品信息item
     * @author leibing
     * @createTime 2017/5/18
     * @lastModify 2017/5/18
     * @param
     * @return
     */
    private void removeDrugMsgItems(){
        if (mData != null
                && mData.size() != 0){
            for (int i=0;i<mData.size();i++){
                JkChatBean mJkChatBean = mData.get(i);
                if (mJkChatBean != null
                        && JkChatConstant.MSG_TYPE_DRUG == mJkChatBean.msgType){
                    mData.remove(mJkChatBean);
                }
            }
            // 更新数据源
            if (mAdapter != null){
                mAdapter.setData(mData);
            }
        }
    }

    /**
     * set adapter
     * @author leibing
     * @createTime 2017/5/15
     * @lastModify 2017/5/15
     * @param
     * @return
     */
    private void setAdapter() {
        // init data
        mData = new ArrayList<>();
        // init adapter
        mAdapter = new JkChatAdapter(this, mData);
        mAdapter.setScreenWidth(screenWidth);
        mAdapter.setAdapterCallBackListener(new JkChatAdapter.AdapterCallBackListener() {
            @Override
            public void retryMsg(int msgIndex, int msgType) {
                // 重发消息
                switch (msgType){
                    case MSG_TYPE_IMG:
                        // 图片
                        if (mJkChatViewModel != null) {
                            mJkChatViewModel.retrySendImgMsg(msgIndex, mData.get(msgIndex));
                        }
                        break;
                    case MSG_TYPE_TXT:
                        // 文本
                        if (mJkChatViewModel != null) {
                            mJkChatViewModel.retrySendTxtMsg(msgIndex, mData.get(msgIndex), false, false);
                        }
                        break;
                    case MSG_TYPE_LINK:
                        // 链接
                        if (mJkChatViewModel != null) {
                            mJkChatViewModel.retrySendTxtMsg(msgIndex, mData.get(msgIndex), true, false);
                        }
                        break;
                    case MSG_TYPE_SEND_DRUG:
                        // 发送药品信息
                        if (mJkChatViewModel != null){
                            mJkChatViewModel.retrySendDrugMsg(msgIndex, mData.get(msgIndex));
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void sendDrugMsg(JkChatBean.Drug mDrug) {
                // （点击发送链接）发送药品消息
                if (connectStatus == PROMPT_LINE_TIP_ONE_STATUS
                        || connectStatus == PROMPT_LINE_TIP_THREE_STATUS
                        || connectStatus == PROMPT_LINE_TIP_FOUR_STATUS){
                    ToastUtils.show(JkChatActivity.this, JK_CHAT_DISCONNECT_TIP);
                    return;
                }
                if (mDrug != null){
                    JkChatBean.SendDrugMsg mSendDrugMsg = new JkChatBean.SendDrugMsg();
                    mSendDrugMsg.drugName = mDrug.drugName;
                    mSendDrugMsg.drugDescription = mDrug.drugDescription;
                    mSendDrugMsg.drugImgUrl = mDrug.drugImgUrl;
                    mSendDrugMsg.drugId = mDrug.drugId;
                    mSendDrugMsg.headPortraitUrl = "";
                    mSendDrugMsg.drugLink = mDrug.drugLink;
                    if (mJkChatViewModel != null){
                        // 更新发送药品消息状态为发送中
                        JkChatBean mJkChatBean
                                =  mJkChatViewModel.updatesendDrugMsgUi(mSendDrugMsg,
                                JkChatConstant.MSG_STATUS_SENDING);
                        // 发送药品信息
                        mJkChatViewModel.sendDrugMsg(mDrug.drugLink,
                                mAdapter.getCount(), mJkChatBean);
                    }
                }
            }

            @Override
            public void msgSlideBottom() {
                // 滑动到底部
                if (jkChatLv != null)
                    jkChatLv.setSelection(jkChatLv.getBottom());
            }

            @Override
            public void openLargerPic(String url) {
                // 聊天列表中图片大图预览
                ArrayList urlsList = new ArrayList();
                urlsList.add(url);
                PictureShowDialog dialog = new PictureShowDialog(JkChatActivity.this,
                        urlsList);
                dialog.show();
            }
        });
        // 设置点击item进入药品详情
        jkChatLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isItemLongOnClick){
                    isItemLongOnClick = false;
                    return;
                }
                if (mData == null || mData.size() == 0 || mData.get(i) == null)
                    return;
                if (mData.get(i).msgType == JkChatConstant.MSG_TYPE_SEND_DRUG){
                    // 跳转商品详情页
                    turnToGoodsDetailsPage(mData.get(i).mSendDrugMsg.drugId);
                }else if (mData.get(i).msgType == JkChatConstant.MSG_TYPE_LINK){
                    if (mData.get(i).mSendTxtMsg != null){
                        if (StringUtil.isNotEmpty(mData.get(i).mSendTxtMsg.msgTxt)
                                && mData.get(i).mSendTxtMsg.msgTxt.contains("product")){
                            String pId = mData.get(i).mSendTxtMsg.msgTxt.
                                    substring(mData.get(i).mSendTxtMsg.msgTxt.lastIndexOf("/") + 1,
                                            mData.get(i).mSendTxtMsg.msgTxt.lastIndexOf("."));
                            // 跳转商品详情页
                            turnToGoodsDetailsPage(pId);
                        }else {
                            // 跳转到网页
                            if (mJkChatViewModel != null)
                                mJkChatViewModel.turnToHtmlPage(mData.get(i).mSendTxtMsg.msgTxt);
                        }
                    }else if (mData.get(i).mReceiveTxtMsg != null){
                        if (StringUtil.isNotEmpty(mData.get(i).mReceiveTxtMsg.msgTxt)
                                && mData.get(i).mReceiveTxtMsg.msgTxt.contains("product")){
                            String pId = mData.get(i).mReceiveTxtMsg.msgTxt.
                                    substring(mData.get(i).mReceiveTxtMsg.msgTxt.lastIndexOf("/") + 1,
                                            mData.get(i).mReceiveTxtMsg.msgTxt.lastIndexOf("."));
                            // 跳转商品详情页
                            turnToGoodsDetailsPage(pId);
                        }else {
                            // 跳转到网页
                            if (mJkChatViewModel != null)
                                mJkChatViewModel.turnToHtmlPage(mData.get(i).mReceiveTxtMsg.msgTxt);
                        }
                    }
                }
            }
        });
        // 设置长按item删除监听
        jkChatLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView,
                                           View view, final int i, long l) {
                isItemLongOnClick = true;
                if (mData == null || mData.size() == 0 || mData.get(i) == null)
                    return false;
                switch (mData.get(i).msgDirection){
                    case JkChatConstant.MSG_DIRECTION_SEND:
                        // 发送消息
                        // 根据消息类型显示
                        switch (mData.get(i).msgType){
                            case JkChatConstant.MSG_TYPE_TXT:
                            case JkChatConstant.MSG_TYPE_LINK:
                                // 文本消息
                                // 链接
                                TextView popTxtTv
                                        = (TextView) view.findViewById(R.id.tv_send_txt_msg_content);
                                showPopUps(popTxtTv, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        switch (view.getId()){
                                            case R.id.tv_pop_jk_copy:
                                                // 复制
                                                ClipboardManager cm = (ClipboardManager)
                                                        getSystemService(Context.CLIPBOARD_SERVICE);
                                                cm.setText(mData.get(i).mSendTxtMsg.msgTxt);
                                                break;
                                            case R.id.tv_pop_jk_del:
                                                // 删除
                                                delListDataByItem(i);
                                                break;
                                        }
                                        disMissPop();
                                    }
                                });
                                break;
                            case JkChatConstant.MSG_TYPE_IMG:
                                // 图片消息
                                ImageView popImgIv
                                        = (ImageView) view.findViewById(R.id.iv_send_img_msg_content);
                                switch (mData.get(i).msgStatus){
                                    case JkChatConstant.MSG_STATUS_SEND_FAIL:
                                        // 状态为失败则重发
                                        resendMsgPop(popImgIv, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                // 重发图片消息
                                                if (mJkChatViewModel != null) {
                                                    mJkChatViewModel.retrySendImgMsg(i,
                                                            mData.get(i));
                                                }
                                                disMissPop();
                                            }
                                        });
                                        break;
                                    case JkChatConstant.MSG_STATUS_SEND_SUCCESS:
                                        // 状态为成功则删除
                                        delMsgPop(popImgIv, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                // 删除
                                                delListDataByItem(i);
                                                disMissPop();
                                            }
                                        });
                                        break;
                                }
                                break;
                            case JkChatConstant.MSG_TYPE_SEND_DRUG:
                                // 发送药品消息
                                LinearLayout popDrugLy
                                        = (LinearLayout) view.findViewById(R.id.ly_send_drug_msg);
                                switch (mData.get(i).msgStatus){
                                    case JkChatConstant.MSG_STATUS_SEND_FAIL:
                                        // 状态为失败则重发
                                        resendMsgPop(popDrugLy, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                // 重发药品消息
                                                if (mJkChatViewModel != null){
                                                    mJkChatViewModel.retrySendDrugMsg(i,
                                                            mData.get(i));
                                                }
                                                disMissPop();
                                            }
                                        });
                                        break;
                                    case JkChatConstant.MSG_STATUS_SEND_SUCCESS:
                                        // 状态为成功则删除
                                        delMsgPop(popDrugLy, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                // 删除
                                                delListDataByItem(i);
                                                disMissPop();
                                            }
                                        });
                                        break;
                                }
                                break;
                        }
                        break;
                    case JkChatConstant.MSG_DIRECTION_RECEIVE:
                        // 接收消息
                        // 根据消息类型显示
                        switch (mData.get(i).msgType){
                            case JkChatConstant.MSG_TYPE_TXT:
                            case JkChatConstant.MSG_TYPE_LINK:
                                // 文本消息
                                TextView popTxtTv
                                        = (TextView) view.findViewById(R.id.tv_receive_txt_msg_content);
                                showPopUps(popTxtTv, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        switch (view.getId()){
                                            case R.id.tv_pop_jk_copy:
                                                // 复制
                                                ClipboardManager cm = (ClipboardManager)
                                                        getSystemService(Context.CLIPBOARD_SERVICE);
                                                cm.setText(mData.get(i).mReceiveTxtMsg.msgTxt);
                                                break;
                                            case R.id.tv_pop_jk_del:
                                                // 删除
                                                delListDataByItem(i);
                                                break;
                                        }
                                        disMissPop();
                                    }
                                });
                                break;
                            case JkChatConstant.MSG_TYPE_IMG:
                                // 图片消息
                                ImageView popMsgTv
                                        = (ImageView) view.findViewById(R.id.iv_receive_img_msg_content);
                                delMsgPop(popMsgTv, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        delListDataByItem(i);
                                        disMissPop();
                                    }
                                });
                                break;
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        // bind adapter
        jkChatLv.setAdapter(mAdapter);
    }

    /**
     * 跳转商品详情页
     * @author leibing
     * @createTime 2017/5/23
     * @lastModify 2017/5/23
     * @param productId 商品id
     * @return
     */
    private void turnToGoodsDetailsPage(String productId){
        if (StringUtil.isEmpty(productId))
            return;
        ToastUtils.show(JkChatActivity.this, GOTO_GOODS_DETAILS_PAGE);
    }

    /**
     * 删除列表数据通过item
     * @author leibing
     * @createTime 2017/5/19
     * @lastModify 2017/5/19
     * @param item
     * @return
     */
    private void delListDataByItem(int item){
        if (mData != null && mData.size() != 0){
            mData.remove(item);
            if (mAdapter != null)
                mAdapter.setData(mData);
        }
    }

    /**
     * 更新列表ui
     * @author leibing
     * @createTime 2017/5/15
     * @lastModify 2017/5/15
     * @param mJkChatBean
     * @return
     */
    private void updateListUi(JkChatBean mJkChatBean){
        if (mData != null) {
            mData.add(mJkChatBean);
            if (mAdapter != null) {
                mAdapter.setData(mData);
            }
        }
    }

    /**
     * 局部刷新数据
     * @author leibing
     * @createTime 2017/5/17
     * @lastModify 2017/5/17
     * @param itemIndex
     * @param mJkChatBean
     * @return
     */
    public void updateItem(int itemIndex, JkChatBean mJkChatBean) {
        if (jkChatLv == null || mAdapter == null)
            return;
        if (mData != null
                && mData.size() != 0
                && itemIndex < mData.size()){
            if (mData.get(itemIndex) != null){
                mData.remove(itemIndex);
                mData.add(itemIndex, mJkChatBean);
                if (mAdapter != null)
                    mAdapter.saveChatCache(mData);
            }
        }
        // 得到第一个可显示控件的位置，
        int visiblePosition = jkChatLv.getFirstVisiblePosition();
        // 只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
        if (itemIndex - visiblePosition >= 0) {
            // 得到要更新的item的view
            View view = jkChatLv.getChildAt(itemIndex - visiblePosition);
            // 调用adapter更新界面
            mAdapter.updateView(view, mJkChatBean);
        }
    }

    /**
     * init view
     * @author leibing
     * @createTime 2017/5/4
     * @lastModify 2017/5/4
     * @param
     * @return
     */
    private void initView() {
        menuBtn.setVisibility(View.GONE);
        menuTv.setVisibility(View.VISIBLE);
        menuTv.setText("结束对话");
        // 设置默认标题栏
        if (titleTv != null)
            titleTv.setText("在线咨询");
        // 获取屏幕宽度
        WindowManager wm = this.getWindowManager();
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
        // 阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;
        // 当对话断开时的布局显示
        disConnectedView();
        // 提示栏状态--"正在为您转接......"
        promptLineShow(PROMPT_LINE_TIP_ONE_STATUS, "");
        //添加layout大小发生改变监听器
        activityRootView.addOnLayoutChangeListener(this);
        // 发送消息容器触摸事件
        sendMsgRly.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (connectStatus == PROMPT_LINE_TIP_ONE_STATUS
                        || connectStatus == PROMPT_LINE_TIP_THREE_STATUS
                        || connectStatus == PROMPT_LINE_TIP_FOUR_STATUS){
                    ToastUtils.show(JkChatActivity.this, JK_CHAT_DISCONNECT_TIP);
                }
                return false;
            }
        });
    }

    /**
     * 当对话连接上时的布局显示
     * @author leibing
     * @createTime 2017/5/4
     * @lastModify 2017/5/4
     * @param
     * @return
     */
    private void connectedView(){
        if (uiHandler != null){
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    menuTv.setTextColor(getResources().getColor(R.color.close_chat_txt_color_red));
                    // 当对话连接上才允许结束对话
                    menuTv.setClickable(true);
                }
            });
        }
    }

    /**
     * 当对话断开时的布局显示
     * @author leibing
     * @createTime 2017/5/16
     * @lastModify 2017/5/16
     * @param
     * @return
     */
    private void disConnectedView(){
        if (uiHandler != null){
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    menuTv.setTextColor(getResources().getColor(R.color.gray));
                    menuTv.setClickable(false);
                }
            });
        }
    }

    /**
     * 根据状态显示提示栏
     * @author leibing
     * @createTime 2017/5/4
     * @lastModify 2017/5/4
     * @param status 状态
     * @param doctorName 药师名称
     * @return
     */
    private void promptLineShow(final int status, final String doctorName){
        if (uiHandler != null){
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    jkChatTipLy.setVisibility(View.VISIBLE);
                    switch (status){
                        case PROMPT_LINE_TIP_ONE_STATUS:
                            // 提示栏状态--"正在为您转接......"
                            whatTipViewShow(JkChatConstant.PROMPT_LINE_SELECT_DIALOG_PROMPT);
                            jkChatTipOneTv.setText(JkChatConstant.PROMPT_LINE_TIP_ONE);
                            sendMsgBtn.setClickable(false);
                            sendMsgBtn.setBackgroundColor(getResources().getColor(R.color.gray));
                            sendMsgEdt.setEnabled(false);
                            otherFunctionIv.setClickable(false);
                            break;
                        case JkChatConstant.PROMPT_LINE_TIP_TWO_STATUS:
                            // 提示栏状态--"对话已建立，您正在与xx药师对话"
                            whatTipViewShow(JkChatConstant.PROMPT_LINE_SELECT_DIALOG_PROMPT);
                            if (StringUtil.isNotEmpty(doctorName)) {
                                jkChatTipOneTv.setText(JkChatConstant.PROMPT_LINE_TIP_THREE_UPPER_HALF
                                        + doctorName + JkChatConstant.PROMPT_LINE_TIP_THREE_LOWER_HALF);
                            }
                            sendMsgBtn.setClickable(true);
                            sendMsgBtn.setBackgroundColor(getResources().getColor(R.color.text_color_blue));
                            sendMsgEdt.setEnabled(true);
                            otherFunctionIv.setClickable(true);
                            break;
                        case PROMPT_LINE_TIP_THREE_STATUS:
                            // 提示栏状态--"网络状况不好，请重新尝试！"
                            whatTipViewShow(JkChatConstant.PROMPT_LINE_SELECT_POOR_NETWORK);
                            sendMsgBtn.setClickable(false);
                            sendMsgBtn.setBackgroundColor(getResources().getColor(R.color.gray));
                            sendMsgEdt.setEnabled(false);
                            otherFunctionIv.setClickable(false);
                            break;
                        case PROMPT_LINE_TIP_FOUR_STATUS:
                            // 提示栏状态--"对话已结束，感谢您的咨询！"
                            whatTipViewShow(JkChatConstant.PROMPT_LINE_SELECT_DIALOG_PROMPT);
                            jkChatTipOneTv.setText(JkChatConstant.PROMPT_LINE_TIP_TWO);
                            break;
                        case JkChatConstant.PROMPT_LINE_TIP_FIVE_STATUS:
                            // 提示栏状态--不需要显示状态栏
                            jkChatTipLy.setVisibility(View.GONE);
                            break;
                        case PROMPT_LINE_TIP_SIX_STATUS:
                            // 提示栏状态--"客服正忙，如有需要请拨打4006989999"
                            jkChatTipOneTv.setText(JkChatConstant.PROMPT_CUSTOMER_SERVICE_BUSY);
                            whatTipViewShow(JkChatConstant.PROMPT_LINE_SELECT_DIALOG_PROMPT);
                            sendMsgBtn.setClickable(false);
                            sendMsgBtn.setBackgroundColor(getResources().getColor(R.color.gray));
                            sendMsgEdt.setEnabled(false);
                            otherFunctionIv.setClickable(false);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    /**
     * 决定显示那种提示栏布局
     * @author leibing
     * @createTime 2017/5/4
     * @lastModify 2017/5/4
     * @param viewIndex 布局标识
     * @return
     */
    private void  whatTipViewShow(int viewIndex){
        switch (viewIndex){
            case JkChatConstant.PROMPT_LINE_SELECT_DIALOG_PROMPT:
                // 提示栏布局选择--对话状态提示
                if (jkChatTipOneLy != null)
                    jkChatTipOneLy.setVisibility(View.VISIBLE);
                if (jkChatTipTwoLy != null)
                    jkChatTipTwoLy.setVisibility(View.GONE);
                break;
            case JkChatConstant.PROMPT_LINE_SELECT_POOR_NETWORK:
                // 提示栏布局选择--网络状态差提示
                if (jkChatTipOneLy != null)
                    jkChatTipOneLy.setVisibility(View.GONE);
                if (jkChatTipTwoLy != null)
                    jkChatTipTwoLy.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 其他功能布局显示
     * @author leibing
     * @createTime 2017/5/16
     * @lastModify 2017/5/16
     * @param
     * @return
     */
    private void otherFunctionShow(){
        // 隐藏软键盘
        hideSoftInput();
        if (!isOpenOtherFunction){
            isOpenOtherFunction = true;
            otherFunctionLy.setVisibility(View.VISIBLE);
        }else {
            isOpenOtherFunction = false;
            otherFunctionLy.setVisibility(View.GONE);
        }
        otherFunctionLy.post(new Runnable() {
            @Override
            public void run() {
                // 滑动到底部
                if (jkChatLv != null)
                    jkChatLv.setSelection(jkChatLv.getBottom());
            }
        });
    }

    /**
     * 底部布局显示
     * @author leibing
     * @createTime 2017/5/16
     * @lastModify 2017/5/16
     * @param viewIndex
     * @return
     */
    private void whatBottomViewShow(final int viewIndex){
        if (uiHandler != null) {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    otherFunctionLy.setVisibility(View.GONE);
                    switch (viewIndex) {
                        case BOTTOM_VIEW_SHOW_INPUT:
                            // 显示聊天输入框
                            sendMsgRly.setVisibility(View.VISIBLE);
                            consultingAgainLy.setVisibility(View.GONE);
                            jkChatPraiseLy.setVisibility(View.GONE);
                            break;
                        case BOTTOM_VIEW_SHOW_PRAISE:
                            // 显示评价页面
                            sendMsgRly.setVisibility(View.GONE);
                            consultingAgainLy.setVisibility(View.GONE);
                            jkChatPraiseLy.setVisibility(View.VISIBLE);
                            break;
                        case BOTTOM_VIEW_SHOW_CONTINUE_CONSULTING:
                            // 显示继续咨询
                            sendMsgRly.setVisibility(View.GONE);
                            consultingAgainLy.setVisibility(View.VISIBLE);
                            jkChatPraiseLy.setVisibility(View.GONE);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    /**
     * 重发窗体显示
     * @author leibing
     * @createTime 2017/5/5
     * @lastModify 2017/5/5
     * @param v 位置参照物
     * @param resendOnClick 点击事件
     * @return
     */
    private void resendMsgPop(View v, View.OnClickListener resendOnClick){
        showPopUp(v, JkChatConstant.MSG_ACTION_RESEND, resendOnClick);
    }

    /**
     * 删除窗体显示
     * @author leibing
     * @createTime 2017/5/5
     * @lastModify 2017/5/5
     * @param v 位置参照物
     * @param delOnClick 点击事件
     * @return
     */
    private void delMsgPop(View v, View.OnClickListener delOnClick) {
        showPopUp(v, JkChatConstant.MSG_ACTION_DEL, delOnClick);
    }

    /**
     * 删除或重发窗体显示
     * @author leibing
     * @createTime 2017/5/5
     * @lastModify 2017/5/5
     * @param v 位置参照物
     * @param action 操作名称
     * @param onClick 点击事件
     * @return
     */
    private void showPopUp(View v,String action, View.OnClickListener onClick){
        LinearLayout layout = new LinearLayout(this);
        layout.setBackground(getResources().getDrawable(R.drawable.jk_chat_long_press_the_trigger));
        layout.setGravity(Gravity.CENTER);
        TextView tv = new TextView(this);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setText(action);
        tv.setPadding(0, 0, 0, 16);
        tv.setTextColor(Color.WHITE);
        tv.setOnClickListener(onClick);
        layout.addView(tv);
        popupWindow = new PopupWindow(layout, 120, 100);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] + v.getMeasuredWidth() / 3,
                location[1]-popupWindow.getHeight());
    }

    /**
     * 复制、删除窗体显示
     * @author leibing
     * @createTime 2017/5/5
     * @lastModify 2017/5/5
     * @param v 位置参照物
     * @param onClick 点击事件
     * @return
     */
    private void showPopUps(View v, View.OnClickListener onClick){
        View popLayout = LayoutInflater.from(this).inflate(R.layout.pop_jk_chat, null);
        TextView copyTv = (TextView) popLayout.findViewById(R.id.tv_pop_jk_copy);
        TextView delTv = (TextView) popLayout.findViewById(R.id.tv_pop_jk_del);
        copyTv.setOnClickListener(onClick);
        delTv.setOnClickListener(onClick);
        popupWindow = new PopupWindow(popLayout, 240, 100);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] + v.getMeasuredWidth() / 3,
                location[1]-popupWindow.getHeight());
    }

    /**
     * 关闭窗体
     * @author leibing
     * @createTime 2017/5/5
     * @lastModify 2017/5/5
     * @param
     * @return
     */
    private void disMissPop(){
        if (popupWindow != null
                && popupWindow.isShowing())
            popupWindow.dismiss();
    }

    /**
     * init jk chat logical processing
     * @author leibing
     * @createTime 2017/5/4
     * @lastModify 2017/5/4
     * @param
     * @return
     */
    private void initJkChatViewModel() {
        // init jk chat logical processing instance
        mJkChatViewModel = new JkChatViewModel(this, mViewModelListener);
    }

    @OnClick({R.id.btnBack, R.id.btn_send_msg,
            R.id.tvMenu, R.id.iv_open_other_function,
            R.id.ly_select_photo, R.id.ly_take_photos,
            R.id.iv_jk_chat_close,R.id.iv_service_evaluation_star_one,
            R.id.iv_service_evaluation_star_two, R.id.iv_service_evaluation_star_three,
            R.id.iv_service_evaluation_star_four, R.id.iv_service_evaluation_star_five,
            R.id.iv_reply_speed_star_one, R.id.iv_reply_speed_star_two,
            R.id.iv_reply_speed_star_three, R.id.iv_reply_speed_star_four,
            R.id.iv_reply_speed_star_five, R.id.btn_praise_commit,
            R.id.btn_consulting_again})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBack:
                // 删除药品信息item
                removeDrugMsgItems();
                // 结束对话
                if (mJkChatViewModel != null)
                    mJkChatViewModel.stopConnect();
                // 关闭当前页面
                AppManager.getInstance().finishActivity();
                break;
            case R.id.btn_send_msg:
                // 消息发送（文本or链接）
                sendTxtMsg();
                break;
            case R.id.tvMenu:
                // 隐藏软键盘
                hideSoftInput();
                // 结束对话
                isCloseChat = true;
                if (mJkChatViewModel != null)
                    mJkChatViewModel.stopConnect();
                break;
            case R.id.iv_open_other_function:
                // 更多功能展开
                otherFunctionShow();
                break;
            case R.id.ly_select_photo:
                // 选择照片
                selectPhoto();
                break;
            case R.id.ly_take_photos:
                // 拍照
                takePhotos();
                break;
            case R.id.iv_jk_chat_close:
                // 关闭评价
                // 重置评价页面数据
                praisePageReset();
                // 底部布局显示（继续咨询）
                whatBottomViewShow(JkChatConstant.BOTTOM_VIEW_SHOW_CONTINUE_CONSULTING);
                break;
            case R.id.iv_service_evaluation_star_one:
                // 服务评价星级1
                serviceLevel = JkChatConstant.PRAISE_LEVEL_ONE_STAR;
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarOneIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarTwoIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarThreeIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarFourIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarFiveIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                break;
            case R.id.iv_service_evaluation_star_two:
                // 服务评价星级2
                serviceLevel = JkChatConstant.PRAISE_LEVEL_TWO_STAR;
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarOneIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarTwoIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarThreeIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarFourIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarFiveIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                break;
            case R.id.iv_service_evaluation_star_three:
                // 服务评价星级3
                serviceLevel = JkChatConstant.PRAISE_LEVEL_THREE_STAR;
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarOneIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarTwoIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarThreeIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarFourIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarFiveIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                break;
            case R.id.iv_service_evaluation_star_four:
                // 服务评价星级4
                serviceLevel = JkChatConstant.PRAISE_LEVEL_FOUR_STAR;
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarOneIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarTwoIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarThreeIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarFourIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarFiveIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                break;
            case R.id.iv_service_evaluation_star_five:
                // 服务评价星级5
                serviceLevel = JkChatConstant.PRAISE_LEVEL_FIVE_STAR;
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarOneIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarTwoIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarThreeIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarFourIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        serviceStarFiveIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                break;
            case R.id.iv_reply_speed_star_one:
                // 回复速度星级1
                replyLevel = JkChatConstant.PRAISE_LEVEL_ONE_STAR;
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarOneIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarTwoIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarThreeIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarFourIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarFiveIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                break;
            case R.id.iv_reply_speed_star_two:
                // 回复速度星级2
                replyLevel = JkChatConstant.PRAISE_LEVEL_TWO_STAR;
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarOneIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarTwoIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarThreeIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarFourIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarFiveIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                break;
            case R.id.iv_reply_speed_star_three:
                // 回复速度星级3
                replyLevel = JkChatConstant.PRAISE_LEVEL_THREE_STAR;
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarOneIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarTwoIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarThreeIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarFourIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarFiveIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                break;
            case R.id.iv_reply_speed_star_four:
                // 回复速度星级4
                replyLevel = JkChatConstant.PRAISE_LEVEL_FOUR_STAR;
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarOneIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarTwoIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarThreeIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarFourIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarFiveIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
                break;
            case R.id.iv_reply_speed_star_five:
                // 回复速度星级5
                replyLevel = JkChatConstant.PRAISE_LEVEL_FIVE_STAR;
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarOneIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarTwoIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarThreeIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarFourIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                ImageLoader.getInstance().load(ShareApplication.getInstance(),
                        replySpeedStarFiveIv,ImageLoader.getDrawableSource(
                                ShareApplication.getInstance(), R.drawable.jk_chat_star));
                break;
            case R.id.btn_praise_commit:
                // 提交评价
                if (serviceLevel == JkChatConstant.PRAISE_LEVEL_NO_PRAISE
                        || replyLevel == JkChatConstant.PRAISE_LEVEL_NO_PRAISE){
                    ToastUtils.show(JkChatActivity.this, PRAISE_NO_SELECT_TIP);
                    return;
                }
                if (mJkChatViewModel != null)
                    mJkChatViewModel.commitPraise(serviceLevel, replyLevel,
                            praiseInputEdt.getText().toString());
                break;
            case R.id.btn_consulting_again:
                // 再次咨询
                // 底部布局显示（显示聊天输入框）
                whatBottomViewShow(JkChatConstant.BOTTOM_VIEW_SHOW_INPUT);
                // start websocket connect
                if (mJkChatViewModel != null)
                    mJkChatViewModel.startConnect();
                break;
            default:
                break;
        }
    }

    /**
     * 评价页面重置
     * @author leibing
     * @createTime 2017/5/16
     * @lastModify 2017/5/16
     * @param
     * @return
     */
    private void praisePageReset(){
        replyLevel = JkChatConstant.PRAISE_LEVEL_NO_PRAISE;
        serviceLevel = JkChatConstant.PRAISE_LEVEL_NO_PRAISE;
        praiseInputEdt.setText("");
        ImageLoader.getInstance().load(ShareApplication.getInstance(),
                replySpeedStarOneIv,ImageLoader.getDrawableSource(
                        ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
        ImageLoader.getInstance().load(ShareApplication.getInstance(),
                replySpeedStarTwoIv,ImageLoader.getDrawableSource(
                        ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
        ImageLoader.getInstance().load(ShareApplication.getInstance(),
                replySpeedStarThreeIv,ImageLoader.getDrawableSource(
                        ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
        ImageLoader.getInstance().load(ShareApplication.getInstance(),
                replySpeedStarFourIv,ImageLoader.getDrawableSource(
                        ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
        ImageLoader.getInstance().load(ShareApplication.getInstance(),
                replySpeedStarFiveIv,ImageLoader.getDrawableSource(
                        ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
        ImageLoader.getInstance().load(ShareApplication.getInstance(),
                serviceStarOneIv,ImageLoader.getDrawableSource(
                        ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
        ImageLoader.getInstance().load(ShareApplication.getInstance(),
                serviceStarTwoIv,ImageLoader.getDrawableSource(
                        ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
        ImageLoader.getInstance().load(ShareApplication.getInstance(),
                serviceStarThreeIv,ImageLoader.getDrawableSource(
                        ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
        ImageLoader.getInstance().load(ShareApplication.getInstance(),
                serviceStarFourIv,ImageLoader.getDrawableSource(
                        ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
        ImageLoader.getInstance().load(ShareApplication.getInstance(),
                serviceStarFiveIv,ImageLoader.getDrawableSource(
                        ShareApplication.getInstance(), R.drawable.jk_chat_star_gray));
    }

    /**
     * 拍照
     * @author leibing
     * @createTime 2017/5/16
     * @lastModify 2017/5/16
     * @param
     * @return
     */
    private void takePhotos() {
        if (connectStatus == PROMPT_LINE_TIP_ONE_STATUS
                || connectStatus == PROMPT_LINE_TIP_THREE_STATUS
                || connectStatus == PROMPT_LINE_TIP_FOUR_STATUS){
            ToastUtils.show(JkChatActivity.this, JK_CHAT_DISCONNECT_TIP);
            return;
        }
        // 判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ContentValues values = new ContentValues();
            // 使用照相机拍照，拍照后的图片会存放在相册中。使用这种方式好处就是：获取的图片是拍照后的原图，
            // 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图有可能不清晰
            photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, TAKE_PHOTO_CODE);
        } else {
            Toast.makeText(this, SDCARD_NO_EXIST, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 选择照片
     * @author leibing
     * @createTime 2017/5/16
     * @lastModify 2017/5/16
     * @param
     * @return
     */
    private void selectPhoto() {
        if (connectStatus == PROMPT_LINE_TIP_ONE_STATUS
                || connectStatus == PROMPT_LINE_TIP_THREE_STATUS
                || connectStatus == PROMPT_LINE_TIP_FOUR_STATUS){
            ToastUtils.show(JkChatActivity.this, JK_CHAT_DISCONNECT_TIP);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, SELECT_PIC_CODE);
    }

    /**
     * 发送文本or链接消息
     * @author leibing
     * @createTime 2017/5/15
     * @lastModify 2017/5/15
     * @param
     * @return
     */
    private void sendTxtMsg() {
        if (sendMsgEdt == null)
            return;
        String msg = sendMsgEdt.getText().toString();
        if (StringUtil.isEmpty(msg)){
            ToastUtils.show(this, PLEASE_INPUT_TXT_TIP);
            return;
        }
        if (msg.contains("http://")
                || msg.contains("https://")){
            // 更新链接消息状态为发送中
            JkChatBean mJkChatBean
                    =  mJkChatViewModel.updateSendTxtMsgUi(msg,
                    JkChatConstant.MSG_STATUS_SENDING, true);
            // 发送链接消息
            mJkChatViewModel.sendTxtMsg(msg, mAdapter.getCount(), mJkChatBean, true, false);
        }else {
            // 普通文本
            if (mJkChatViewModel != null) {
                // 更新文本消息状态为发送中
                JkChatBean mJkChatBean
                        =  mJkChatViewModel.updateSendTxtMsgUi(msg,
                        JkChatConstant.MSG_STATUS_SENDING, false);
                // 发送普通文本消息
                mJkChatViewModel.sendTxtMsg(msg, mAdapter.getCount(), mJkChatBean, false, false);
            }
        }
        sendMsgEdt.setText("");
    }

    /**
     * 上传图片
     * @author leibing
     * @createTime 2017/5/16
     * @lastModify 2017/5/16
     * @param filePath 文件路径
     * @return
     */
    private void upLoadPic(final String filePath){
        if (mJkChatViewModel != null && StringUtil.isNotEmpty(filePath)) {
            mJkChatViewModel.preSendImgMsg(filePath, new JkChatViewModel.CallBackListener() {
                @Override
                public void dataCallBack(Object object) {
                    JkChatBean mJkChatBean = (JkChatBean) object;
                    mJkChatViewModel.upLoadPic(mAdapter.getCount(), mJkChatBean);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            // 从相册取图片，有些手机有异常情况，请注意
            if (requestCode == SELECT_PIC_CODE) {
                if (null != data && null != data.getData()) {
                    photoUri = data.getData();
                    picPath = FileUtils.uriToFilePath(photoUri, JkChatActivity.this);
                    // 上传图片
                    upLoadPic(picPath);
                } else {
                    Toast.makeText(this, PIC_SELECT_FAIL, Toast.LENGTH_LONG).show();
                }
            }else if (requestCode == TAKE_PHOTO_CODE) {
                // 拍照
                String[] pojo = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                    cursor.moveToFirst();
                    picPath = cursor.getString(columnIndex);
                    if (Build.VERSION.SDK_INT < 14) {
                        cursor.close();
                    }
                }
                if (StringUtil.isNotEmpty(picPath)) {
                    // 上传图片
                    upLoadPic(picPath);
                } else {
                    Toast.makeText(this, PIC_SELECT_FAIL, Toast.LENGTH_LONG).show();
                }
            }else if (requestCode == GOTO_GOODS_DETAILS_REQUEST_CODE){
                if (mJkChatViewModel != null) {
                    // read cache data
                    mJkChatViewModel.readCacheData();
                    // start websocket connect
                    mJkChatViewModel.startConnect();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 缓存数据
        if (mAdapter != null)
            mAdapter.saveChatCache();
        // 隐藏软键盘
        hideSoftInput();
    }

    /**
     * 掩藏软键盘
     * @author leibing
     * @createTime 2017/5/17
     * @lastModify 2017/5/17
     * @param
     * @return
     */
    private void hideSoftInput(){
        // 隐藏软键盘
        View mv = this.getWindow().peekDecorView();
        if (mv != null){
            InputMethodManager inputmanger
                    = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(mv.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        // 删除药品信息item
        removeDrugMsgItems();
        // 结束对话
        if (mJkChatViewModel != null)
            mJkChatViewModel.stopConnect();
        super.onBackPressed();
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        // 现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){
            // 软键盘弹起
            // 滑动到底部
            if (jkChatLv != null)
                jkChatLv.setSelection(jkChatLv.getBottom());
        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){
            // 软键盘收起
        }
    }
}
