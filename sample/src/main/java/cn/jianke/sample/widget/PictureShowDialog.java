package cn.jianke.sample.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import cn.jianke.sample.R;
import cn.jianke.sample.module.ImageLoader;

/**
 * @className: PictureShowDialog
 * @classDescription: 图片查看器
 * @author: leibing
 * @createTime: 2016/08/24
 */
public class PictureShowDialog extends Dialog {
    // 图片查看对话框
    private ZoomImageView picLookZiv;

    /**
     * 构造函数
     * @author leibing
     * @createTime 2016/08/24
     * @lastModify 2016/08/24
     * @param context 上下文
     * @param picUrl 图片url
     * @return
     */
    public PictureShowDialog(Context context, String picUrl) {
        super(context, android.R.style.Theme);
        // 设置窗体无标题样式d
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设定布局
        setContentView(R.layout.dialog_picture_show);
        // FindView
        picLookZiv = (ZoomImageView) findViewById(R.id.ziv_pic_look);
        picLookZiv.setOnSingleTapListener(new ZoomImageView.OnSingleTouchListener() {
            @Override
            public void onSingleTouch() {
                dismiss();
            }
        });
        // load img
        ImageLoader.getInstance().load(context, picLookZiv, picUrl);
    }
}
