package cn.cndoppler.p2p.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cndoppler.p2p.R;
import cn.cndoppler.p2p.common.ActivityManager;
import cn.cndoppler.p2p.common.BaseActivity;
import cn.cndoppler.p2p.ui.CommonTitleView;
import cn.cndoppler.p2p.util.ToastUtils;
import cn.cndoppler.p2p.util.UIUtils;

public class TiXianActivity extends BaseActivity {

    @BindView(R.id.titleView)
    CommonTitleView titleView;
    @BindView(R.id.account_zhifubao)
    TextView accountZhifubao;
    @BindView(R.id.select_bank)
    RelativeLayout selectBank;
    @BindView(R.id.chongzhi_text)
    TextView chongzhiText;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.et_input_money)
    EditText etInputMoney;
    @BindView(R.id.chongzhi_text2)
    TextView chongzhiText2;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.btn_tixian)
    Button btnTixian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ti_xian);
        ButterKnife.bind(this);
        initData();
        setListener();
    }

    private void initData() {
        //设置当前的体现的button是不可操作的
        btnTixian.setClickable(false);
        etInputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String money = etInputMoney.getText().toString().trim();
                if(TextUtils.isEmpty(money)){
                    //设置button不可操作的
                    btnTixian.setClickable(false);
                    //修改背景颜色
                    btnTixian.setBackgroundResource(R.drawable.btn_02);
                }else{
                    //设置button可操作的
                    btnTixian.setClickable(true);
                    //修改背景颜色
                    btnTixian.setBackgroundResource(R.drawable.btn_01);
                }
            }
        });
    }

    private void setListener() {
        titleView.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().removeCurrent();
            }
        });
    }

    @OnClick(R.id.btn_tixian)
    public void tiXian(View view){
        //将要提现的数据数额发送给后台，由后台连接第三方支付平台，完成金额的提现操作。（略）
        //提示用户信息：
        ToastUtils.showToastShort(this,"您的提现申请已被成功受理。审核通过后，24小时内，你的钱自然会到账");

        UIUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityManager.getInstance().removeCurrent();
            }
        },2000);
    }


}
