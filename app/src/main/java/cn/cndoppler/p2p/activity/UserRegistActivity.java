package cn.cndoppler.p2p.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.cndoppler.p2p.R;
import cn.cndoppler.p2p.common.ActivityManager;
import cn.cndoppler.p2p.common.AppNetConfig;
import cn.cndoppler.p2p.common.BaseActivity;
import cn.cndoppler.p2p.ui.CommonTitleView;
import cn.cndoppler.p2p.util.MD5Utils;
import cn.cndoppler.p2p.util.ToastUtils;
import cn.cndoppler.p2p.util.UIUtils;

public class UserRegistActivity extends BaseActivity {

    @BindView(R.id.titleView)
    CommonTitleView titleView;
    @BindView(R.id.et_register_number)
    EditText etRegisterNumber;
    @BindView(R.id.et_register_name)
    EditText etRegisterName;
    @BindView(R.id.et_register_pwd)
    EditText etRegisterPwd;
    @BindView(R.id.et_register_pwdagain)
    EditText etRegisterPwdagain;
    @BindView(R.id.btn_register)
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_regist);
        ButterKnife.bind(this);
        initData();
        setListener();
    }

    private void setListener() {
        titleView.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().removeCurrent();
            }
        });
    }

    private void initData() {
        //设置“注册”button的点击事件
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.获取用户注册的信息
                String name = etRegisterName.getText().toString().trim();
                String number = etRegisterNumber.getText().toString().trim();
                String pwd = etRegisterPwd.getText().toString().trim();
                String pwdAgain = etRegisterPwdagain.getText().toString().trim();


                //2.所填写的信息不能为空
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(number) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdAgain)){
                    ToastUtils.showToastShort(UserRegistActivity.this,"填写的信息不能为空");
                }else if(!pwd.equals(pwdAgain)){//2.两次密码必须一致
                    ToastUtils.showToastShort(UserRegistActivity.this,"两次填写的密码不一致");
                    etRegisterPwd.setText("");
                    etRegisterPwdagain.setText("");

                }else{
                    //3.联网发送用户注册信息
                    String url = AppNetConfig.USERREGISTER;
                    RequestParams params = new RequestParams();
                    try {
                        name = new String(name.getBytes(),"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    params.put("name",name);
                    params.put("password", MD5Utils.MD5(pwd));
                    params.put("phone",number);
                    client.post(url,params,new AsyncHttpResponseHandler(){
                        @Override
                        public void onSuccess(String content) {
                            JSONObject jsonObject = JSON.parseObject(content);
                            boolean isExist = jsonObject.getBoolean("isExist");
                            if(isExist){//已经注册过
                                ToastUtils.showToastShort(UserRegistActivity.this,"此用户已注册");
                            }else{
                                ToastUtils.showToastShort(UserRegistActivity.this,"注册成功");
                            }

                        }

                        @Override
                        public void onFailure(Throwable error, String content) {
                            ToastUtils.showToastShort(UserRegistActivity.this,"联网请求失败");
                        }
                    });
                }

            }
        });
    }
}
