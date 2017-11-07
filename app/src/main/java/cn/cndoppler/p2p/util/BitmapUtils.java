package cn.cndoppler.p2p.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * Created by Administrator on 2017/11/7 0007.
 */

public class BitmapUtils {
    /**
     * 代码：功能性代码；非功能性代码。
     *
     * @param source
     * @return
     */
    public static Bitmap circleBitmap(Bitmap source) {
        int width = source.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(width,width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawCircle(width/2,width/2,width/2,paint);
        //设置图片相交情况下的处理方式
        //setXfermode：设置当绘制的图像出现相交情况时候的处理方式的,它包含的常用模式有：
        //PorterDuff.Mode.SRC_IN 取两层图像交集部分,只显示上层图像
        //PorterDuff.Mode.DST_IN 取两层图像交集部分,只显示下层图像
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //在画布上绘制bitmap
        canvas.drawBitmap(source, 0, 0, paint);
        return bitmap;
    }

    //实现图片的压缩处理
    //设置宽高必须使用浮点型，否则导致压缩的比例：0
    public static Bitmap zoom(Bitmap source,float width ,float height){
        Matrix matrix = new Matrix();
        matrix.postScale(width/source.getWidth(),height/source.getHeight());
        Bitmap bitmap = Bitmap.createBitmap(source,0,0,source.getWidth(),source.getHeight(),matrix, false);
        return bitmap;
    }
}
