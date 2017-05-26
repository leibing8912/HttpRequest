package cn.jianke.sample.module.jkchat.util;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @className: FileUtils
 * @classDescription: 文件工具类
 * @author: leibing
 * @createTime: 2017/5/25
 */
public class FileUtils {

    /**
      * 把Uri转换为文件路径
      * @author leibing
      * @createTime 2017/5/16
      * @lastModify 2017/5/16
      * @param uri
      * @param activity
      * @return
      */
    public static String uriToFilePath(Uri uri, Activity activity) {
        // 获取图片数据
        String[] proj = {MediaStore.Images.Media.DATA};
        // 查询
        Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
        // 获得用户选择的图片的索引值
        int image_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        // 返回图片路径
        return cursor.getString(image_index);
    }

    /**
      * 压缩图片（质量压缩）
      * @author leibing
      * @createTime 2017/5/16
      * @lastModify 2017/5/16
      * @param
      * @return
      */
    public static File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于500kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 500) {
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            // 这里压缩options%，把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            long length = baos.toByteArray().length;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date);
        File file = new File(Environment.getExternalStorageDirectory(),filename+".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        recycleBitmap(bitmap);
        return file;
    }

    /**
      * 释放bitmap资源
      * @author leibing
      * @createTime 2017/5/16
      * @lastModify 2017/5/16
      * @param bitmaps
      * @return
      */
    public static void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps==null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }

    /**
      * 根据路径获得图片并压缩，返回bitmap用于显示
      * @author leibing
      * @createTime 2017/5/16
      * @lastModify 2017/5/16
      * @param filePath
      * @return
      */
    public static Bitmap getSmallBitmap(String filePath, int sampleSize) {
        // 图片所在SD卡的路径
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
      * 计算图片的缩放值
      * @author leibing
      * @createTime 2017/5/16
      * @lastModify 2017/5/16
      * @param options
      * @param reqWidth
      * @param reqHeight
      * @return
      */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 获取图片的高
        final int height = options.outHeight;
        // 获取图片的框
        final int width = options.outWidth;
        int inSampleSize = 4;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        // 求出缩放值
        return inSampleSize;
    }

    /**
      *  获取文件大小
      * @author leibing
      * @createTime 2017/5/16
      * @lastModify 2017/5/16
      * @param file
      * @return
      */
    public static String getFileSize(File file){
        String size = "";
        if(file.exists() && file.isFile()){
            long fileS = file.length();
            DecimalFormat df = new DecimalFormat("#.00");
            if (fileS < 1024) {
                size = df.format((double) fileS) + "BT";
            } else if (fileS < 1048576) {
                size = df.format((double) fileS / 1024) + "KB";
            } else if (fileS < 1073741824) {
                size = df.format((double) fileS / 1048576) + "MB";
            } else {
                size = df.format((double) fileS / 1073741824) +"GB";
            }
        }else if(file.exists() && file.isDirectory()){
            size = "";
        }else{
            size = "0BT";
        }
        return size;
    }
}
