package cn.cndoppler.p2p.util;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.view.View;

import cn.cndoppler.p2p.common.MyApplication;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public class UIUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px( float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp( float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static Context getContext(){
        return MyApplication.context;
    }

    public static Handler getHandler(){
        return MyApplication.handler;
    }

    //返回指定colorId对应的颜色值
    public static int getColor(int colorId){
        return getContext().getResources().getColor(colorId);
    }

    //加载指定viewId的视图对象，并返回
    public static View getView(int viewId){
        View view = View.inflate(getContext(), viewId, null);
        return view;
    }

    public static String[] getStringArr(int strArrId){
        String[] stringArray = getContext().getResources().getStringArray(strArrId);
        return stringArray;
    }

    //保证runnable中的操作在主线程中执行
    public static void runOnUiThread(Runnable runnable){
        if (isInMainThread()){
            runnable.run();
        }else{
            UIUtils.getHandler().post(runnable);
        }
    }
    //是否在主线程
    private static boolean isInMainThread() {
        int myId = android.os.Process.myPid();
        return myId == MyApplication.mainThreadId;
    }
}
