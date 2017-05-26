package cn.jianke.sample.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import java.io.File;
import java.util.List;
import cn.jianke.sample.R;
import cn.jianke.sample.module.ImageLoader;

/**
 * @className: PictureShowDialog
 * @classDescription: 图片轮播器对话框
 * @author: leibing
 * @createTime: 2016/08/24
 */
public class PictureShowDialog extends Dialog implements GestureDetector.OnGestureListener {
    // 图片轮播器
    private ViewFlipper mDialogPictureVf;
    // 手势检测
    private GestureDetector detector;
    // 进出动画
    private Animation leftInAnimation;
    private Animation leftOutAnimation;
    private Animation rightInAnimation;
    private Animation rightOutAnimation;

    /**
     * 构造函数
     * @author leibing
     * @createTime 2016/08/24
     * @lastModify 2016/08/24
     * @param context 上下文
     * @param imageUrlList 图片url列表
     * @return
     */
    public PictureShowDialog(Context context, List imageUrlList) {
        super(context, android.R.style.Theme);
        // 设置窗体无标题样式d
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设定布局
        setContentView(R.layout.dialog_picture_show);
        // FindView
        mDialogPictureVf = (ViewFlipper) findViewById(R.id.vf_dialog_picture);
        // 动态导入的方式为ViewFlipper加入子View
        for (int i=0;i<imageUrlList.size();i++){
            ImageView imageView = new ImageView(context);
            // 加载图片
            if (imageUrlList.get(i).toString().contains("http")
                    || imageUrlList.get(i).toString().contains("https")){
                ImageLoader.getInstance().load(context, imageView, imageUrlList.get(i).toString());
            }else {
                ImageLoader.getInstance().load(context, imageView, new File(imageUrlList.get(i).toString()));
            }
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mDialogPictureVf.addView(imageView);
        }
        // 初始化手势
        detector = new GestureDetector(this);
        // 动画效果
        leftInAnimation = AnimationUtils.loadAnimation(context, R.anim.left_in);
        leftOutAnimation = AnimationUtils.loadAnimation(context, R.anim.left_out);
        rightInAnimation = AnimationUtils.loadAnimation(context, R.anim.right_in);
        rightOutAnimation = AnimationUtils.loadAnimation(context, R.anim.right_out);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // touch事件交给手势处理
        return this.detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        dismiss();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
        if(e1.getX()-e2.getX()>120){
            mDialogPictureVf.setInAnimation(leftInAnimation);
            mDialogPictureVf.setOutAnimation(leftOutAnimation);
            // 向右滑动
            mDialogPictureVf.showNext();
            return true;
        }else if(e1.getX()-e2.getY()<-120){
            mDialogPictureVf.setInAnimation(rightInAnimation);
            mDialogPictureVf.setOutAnimation(rightOutAnimation);
            // 向左滑动
            mDialogPictureVf.showPrevious();
            return true;
        }
        return false;
    }
}
