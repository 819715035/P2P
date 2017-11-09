package cn.cndoppler.p2p.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.content.FileProvider;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.cndoppler.p2p.R;
import cn.cndoppler.p2p.bean.UpdateInfo;
import cn.cndoppler.p2p.common.ActivityManager;
import cn.cndoppler.p2p.common.AppNetConfig;
import cn.cndoppler.p2p.common.BaseActivity;
import cn.cndoppler.p2p.util.LogUtils;
import cn.cndoppler.p2p.util.ToastUtils;
import cn.cndoppler.p2p.util.UIUtils;

import static cn.cndoppler.p2p.common.MyApplication.handler;

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.iv_welcome_icon)
    ImageView ivWelcomeIcon;
    @BindView(R.id.rl_welcome)
    RelativeLayout rlWelcome;
    @BindView(R.id.version_tv)
    TextView versionTv;
    private AlphaAnimation alphaAnimation;
    private long startTime;
    private UpdateInfo updateInfo;
    private static final int TO_MAIN = 1;
    private static final int DOWNLOAD_VERSION_SUCCESS = 2;
    private static final int DOWNLOAD_APK_FAIL = 3;
    private static final int DOWNLOAD_APK_SUCCESS = 4;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TO_MAIN:
                    finish();
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    break;
                case DOWNLOAD_VERSION_SUCCESS:
                    //获取当前应用的版本信息
                    String version = getVersion();
                    //更新页面显示的版本信息
                    versionTv.setText(version);
                    //比较服务器获取的最新的版本跟本应用的版本是否一致
                    if(version.equals(updateInfo.version)){
                        toMain();
                    }else{
                        new AlertDialog.Builder(WelcomeActivity.this)
                                .setTitle("下载最新版本")
                                .setMessage(updateInfo.desc)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //下载服务器保存的应用数据
                                        downloadApk();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        toMain();
                                    }
                                })
                                .show();
                    }

                    break;
                case DOWNLOAD_APK_FAIL:
                    ToastUtils.showToastShort(WelcomeActivity.this,"联网下载数据失败");
                    toMain();
                    break;
                case DOWNLOAD_APK_SUCCESS:
                    ToastUtils.showToastShort(WelcomeActivity.this,"下载应用数据成功");
                    dialog.dismiss();
                    installApk();//安装下载好的应用
                    finish();//结束当前的welcomeActivity的显示
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏顶部的状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        //联网更新应用
        updateApkFile();
    }

    private void updateApkFile() {
        //获取系统当前时间
        startTime = System.currentTimeMillis();

        //1.判断手机是否可以联网
        boolean connect = isConnect();
        if (!connect) {//没有移动网络
            ToastUtils.showToastShort(this,"当前没有移动数据网络");
            toMain();
        } else {//有移动网络
            //联网获取服务器的最新版本数据
            AsyncHttpClient client = new AsyncHttpClient();
            String url = AppNetConfig.UPDATE;
            client.post(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String content) {
                    //解析json数据
                    updateInfo = JSON.parseObject(content, UpdateInfo.class);
                    handler.sendEmptyMessage(DOWNLOAD_VERSION_SUCCESS);
                }

                @Override
                public void onFailure(Throwable error, String content) {
                    ToastUtils.showToastShort(WelcomeActivity.this,"联网请求数据失败");
                    toMain();
                }
            });

        }
    }

    private void toMain() {
        long currentTime = System.currentTimeMillis();
        long delayTime = 3000 - (currentTime - startTime);
        if (delayTime < 0) {
            delayTime = 0;
        }


        handler.sendEmptyMessageDelayed(TO_MAIN, delayTime);
    }

    public boolean isConnect() {
        boolean connected = false;

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            connected = networkInfo.isConnected();
        }
        return connected;
    }


    @Override
    protected void onResume() {
        super.onResume();
        //开启动画
        startAnimation();
    }

    private void startAnimation() {
        //透明
        alphaAnimation = new AlphaAnimation(0.2f,1);
        alphaAnimation.setDuration(3000);
        alphaAnimation.setFillAfter(true);
        //匀速透明
        alphaAnimation.setInterpolator(new LinearInterpolator());
       /* alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                openActivity(MainActivity.class);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });*/
        rlWelcome.startAnimation(alphaAnimation);
    }


    /**
     * 当前版本号
     *
     * @return
     */
    private String getVersion() {
        String version = "未知版本";
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace(); //如果找不到对应的应用包信息, 就返回"未知版本"
        }
        return version;
    }

    private ProgressDialog dialog;
    private File apkFile;
    private void downloadApk() {
        //初始化水平进度条的dialog
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.show();
        //初始化数据要保持的位置
        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            filesDir = this.getExternalFilesDir("");
        }else{
            filesDir = this.getFilesDir();
        }
        apkFile = new File(filesDir,"update.apk");

        //启动一个分线程联网下载数据：
        new Thread(){
            public void run(){
                //String path = updateInfo.apkUrl;
                String path = "http://47.93.118.241:8081/P2PInvest/app_new.apk";
                InputStream is = null;
                FileOutputStream fos = null;
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(path);
                    conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);

                    conn.connect();

                    if(conn.getResponseCode() == 200){
                        dialog.setMax(conn.getContentLength());//设置dialog的最大值
                        is = conn.getInputStream();
                        fos = new FileOutputStream(apkFile);

                        byte[] buffer = new byte[1024];
                        int len;
                        while((len = is.read(buffer)) != -1){
                            //更新dialog的进度
                            dialog.incrementProgressBy(len);
                            fos.write(buffer,0,len);

                            SystemClock.sleep(1);
                        }

                        handler.sendEmptyMessage(DOWNLOAD_APK_SUCCESS);

                    }else{
                        handler.sendEmptyMessage(DOWNLOAD_APK_FAIL);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }finally{
                    if(conn != null){
                        conn.disconnect();
                    }
                    if(is != null){
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(fos != null){
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


            }
        }.start();
    }

    private void installApk() {
        Uri contentUri;
        //判断是否是AndroidN以及更高的版本
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N) {
            File file = new File(apkFile.getAbsolutePath());
            contentUri = FileProvider.getUriForFile(this,"cn.cndoppler.p2p.fileProvider",file);

        }else{
            contentUri = Uri.parse("file:" + apkFile.getAbsolutePath());
        }
        Intent intent = new Intent("android.intent.action.INSTALL_PACKAGE");
        intent.setData(contentUri);
        startActivity(intent);
    }
}
