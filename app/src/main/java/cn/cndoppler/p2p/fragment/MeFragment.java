package cn.cndoppler.p2p.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.loopj.android.http.RequestParams;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cndoppler.p2p.R;
import cn.cndoppler.p2p.activity.ChongZhiActivity;
import cn.cndoppler.p2p.activity.LoginActivity;
import cn.cndoppler.p2p.activity.UserInfoActivity;
import cn.cndoppler.p2p.bean.User;
import cn.cndoppler.p2p.common.BaseActivity;
import cn.cndoppler.p2p.common.BaseFragment;
import cn.cndoppler.p2p.ui.CommonTitleView;
import cn.cndoppler.p2p.util.BitmapUtils;
import cn.cndoppler.p2p.util.UIUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends BaseFragment {


    @BindView(R.id.iv_me_icon)
    ImageView ivMeIcon;
    @BindView(R.id.rl_me_icon)
    RelativeLayout rlMeIcon;
    @BindView(R.id.tv_me_name)
    TextView tvMeName;
    @BindView(R.id.rl_me)
    RelativeLayout rlMe;
    @BindView(R.id.recharge)
    ImageView recharge;
    @BindView(R.id.withdraw)
    ImageView withdraw;
    @BindView(R.id.ll_touzi)
    TextView llTouzi;
    @BindView(R.id.ll_touzi_zhiguan)
    TextView llTouziZhiguan;
    @BindView(R.id.ll_zichan)
    TextView llZichan;
    @BindView(R.id.titleView)
    CommonTitleView titleView;

    public MeFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        //读取本地保存的图片
        readImage();
    }

    private boolean readImage() {
        File fileDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            fileDir = getActivity().getExternalFilesDir("");
        }else{
            //手机内部存储
            //路径：data/data/包名/files
            fileDir = getActivity().getFilesDir();
        }
        File file = new File(fileDir,"icon.png");
        if (file.exists()){
            //存储--->内存
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            ivMeIcon.setImageBitmap(bitmap);
            return true;
        }
        return false;
    }

    @Override
    protected void initData(String content) {
        //判断用户是否已经登录
        isLogin();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListener();
    }

    private void setListener() {
        titleView.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity)getActivity()).openActivity(UserInfoActivity.class);
            }
        });
    }

    private void isLogin() {
        //查看本地是否有用户的登录信息
        SharedPreferences sp = this.getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String name = sp.getString("name", "");
        if (TextUtils.isEmpty(name)) {
            //本地没有保存过用户信息，给出提示：登录
            doLogin();
        } else {
            //已经登录过，则直接加载用户的信息并显示
            doUser();
        }
    }

    //加载用户信息并显示
    private void doUser() {
        //1.读取本地保存的用户信息
        User user = ((BaseActivity) this.getActivity()).readUser();
        //2.获取对象信息，并设置给相应的视图显示。
        tvMeName.setText(user.getName());
        //判断本地是否已经保存头像的图片，如果有，则不再执行联网操作
        boolean isExist = readImage();
        if(isExist){
            return;
        }
        Glide.with(this.getActivity()).load(user.getImageurl()).asBitmap().transform(new MyTransformation(getActivity())).into(ivMeIcon);
    }

    //给出提示：登录
    private void doLogin() {
        new AlertDialog.Builder(this.getActivity())
                .setTitle("提示")
                .setMessage("您还没有登录哦！么么~")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                            UIUtils.toast("进入登录页面",false);
                        ((BaseActivity) (MeFragment.this.getActivity())).openActivity(LoginActivity.class);
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_me;
    }

    class MyTransformation extends BitmapTransformation {


        public MyTransformation(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            //压缩处理
            Bitmap bitmap = BitmapUtils.zoom(toTransform, UIUtils.dp2px(100), UIUtils.dp2px(100));
            //圆形处理
            bitmap = BitmapUtils.circleBitmap(bitmap);
            //回收bitmap资源
            toTransform.recycle();
            return bitmap;
        }

        @Override
        public String getId() {
            return "";//需要保证返回值不能为null。否则报错
        }
    }

    //设置“充值”操作
    @OnClick(R.id.recharge)
    public void reCharge(View view){
        ((BaseActivity)this.getActivity()).openActivity(ChongZhiActivity.class);
    }

    //设置“提现”操作
    @OnClick(R.id.withdraw)
    public void withdraw(View view){

    }
}
