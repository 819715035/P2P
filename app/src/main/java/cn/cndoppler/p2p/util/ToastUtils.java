package cn.cndoppler.p2p.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/8/25 0025.
 */

public class ToastUtils {
    private static Toast mToast = null;
    public static void showToastShort(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
    public static void showToastLong(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

}
