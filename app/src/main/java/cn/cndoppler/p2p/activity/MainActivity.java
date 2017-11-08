package cn.cndoppler.p2p.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cndoppler.p2p.R;
import cn.cndoppler.p2p.common.ActivityManager;
import cn.cndoppler.p2p.common.BaseActivity;
import cn.cndoppler.p2p.fragment.HomeFragment;
import cn.cndoppler.p2p.fragment.InverstFragment;
import cn.cndoppler.p2p.fragment.MeFragment;
import cn.cndoppler.p2p.fragment.MoreFragment;
import cn.cndoppler.p2p.util.LogUtils;
import cn.cndoppler.p2p.util.ToastUtils;

public class MainActivity extends BaseActivity {


    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.iv_main_home)
    ImageView ivMainHome;
    @BindView(R.id.tv_main_home)
    TextView tvMainHome;
    @BindView(R.id.ll_main_home)
    LinearLayout llMainHome;
    @BindView(R.id.iv_main_invest)
    ImageView ivMainInvest;
    @BindView(R.id.tv_main_invest)
    TextView tvMainInvest;
    @BindView(R.id.ll_main_invest)
    LinearLayout llMainInvest;
    @BindView(R.id.iv_main_me)
    ImageView ivMainMe;
    @BindView(R.id.tv_main_me)
    TextView tvMainMe;
    @BindView(R.id.ll_main_me)
    LinearLayout llMainMe;
    @BindView(R.id.iv_main_more)
    ImageView ivMainMore;
    @BindView(R.id.tv_main_more)
    TextView tvMainMore;
    @BindView(R.id.ll_main_more)
    LinearLayout llMainMore;
    private Fragment homeFragment;
    private Fragment inverstFragment;
    private Fragment meFragment;
    private Fragment moreFragment;
    private int selectPosition;
    private FragmentTransaction transaction;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        selectFragment(selectPosition);
    }

    /**
     * 选中哪个fragment
     * @param selectPosition
     */
    private void selectFragment(int selectPosition) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        //隐藏fragment
        hiddleFragment();
        //重置tag
        resetTag();
        switch (selectPosition){
            case 0:
                //选中首页
                if (homeFragment==null){
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fl_main,homeFragment);
                }
                transaction.show(homeFragment);
                //改变选中项的图片和文本颜色的变化
                ivMainHome.setImageResource(R.drawable.bottom02);
                tvMainHome.setTextColor(getResources().getColor(R.color.home_back_selected));
                break;
            case 1:
                //选中投资
                //选中首页
                if (inverstFragment==null){
                    inverstFragment = new InverstFragment();
                    transaction.add(R.id.fl_main,inverstFragment);
                }
                transaction.show(inverstFragment);
                //改变选中项的图片和文本颜色的变化
                ivMainInvest.setImageResource(R.drawable.bottom04);
                tvMainInvest.setTextColor(getResources().getColor(R.color.home_back_selected));
                break;
            case 2:
                //选中我的资产
                //选中首页
                if (meFragment==null){
                    meFragment = new MeFragment();
                    transaction.add(R.id.fl_main,meFragment);
                }
                transaction.show(meFragment);
                //改变选中项的图片和文本颜色的变化
                ivMainMe.setImageResource(R.drawable.bottom06);
                tvMainMe.setTextColor(getResources().getColor(R.color.home_back_selected01));
                break;
            case 3:
                //选中更多
                //选中首页
                if (moreFragment==null){
                    moreFragment = new MoreFragment();
                    transaction.add(R.id.fl_main,moreFragment);
                }
                transaction.show(moreFragment);
                //改变选中项的图片和文本颜色的变化
                ivMainMore.setImageResource(R.drawable.bottom08);
                tvMainMore.setTextColor(getResources().getColor(R.color.home_back_selected));
                break;
        }
        transaction.commit();
    }

    /**
     *
     */
    private void resetTag() {
        ivMainHome.setImageResource(R.drawable.bottom01);
        ivMainInvest.setImageResource(R.drawable.bottom03);
        ivMainMe.setImageResource(R.drawable.bottom05);
        ivMainMore.setImageResource(R.drawable.bottom07);

        tvMainHome.setTextColor(getResources().getColor(R.color.home_back_unselected));
        tvMainInvest.setTextColor(getResources().getColor(R.color.home_back_unselected));
        tvMainMe.setTextColor(getResources().getColor(R.color.home_back_unselected));
        tvMainMore.setTextColor(getResources().getColor(R.color.home_back_unselected));
        //这种方式也可以
        tvMainMore.setTextColor(ContextCompat.getColor(this, R.color.home_back_unselected));
    }

    /**
     * 隐藏所有的fragment
     */
    private void hiddleFragment() {
        if(homeFragment!=null){
            transaction.hide(homeFragment);
        }
        if(inverstFragment!=null){
            transaction.hide(inverstFragment);
        }
        if(meFragment!=null){
            transaction.hide(meFragment);
        }
        if(moreFragment!=null){
            transaction.hide(moreFragment);
        }
    }

    @OnClick({R.id.ll_main_home,R.id.ll_main_invest,R.id.ll_main_me,R.id.ll_main_more})
    public void clickButton(View view){
        switch (view.getId()){
            case R.id.ll_main_home:
                selectPosition = 0;
                break;
            case R.id.ll_main_invest:
                selectPosition = 1;
                break;
            case R.id.ll_main_me:
                selectPosition = 2;
                break;
            case R.id.ll_main_more:
                selectPosition = 3;
                break;
        }
        selectFragment(selectPosition);
    }

    private boolean flag = true;
    private final int EXIT_PROGRAM = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case EXIT_PROGRAM:
                    flag = true;
                    break;
            }
        }
    };
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && flag){
            ToastUtils.showToastShort(this,"再点击一次退出");
            flag = false;
            handler.sendEmptyMessageDelayed(EXIT_PROGRAM,2*1000);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
