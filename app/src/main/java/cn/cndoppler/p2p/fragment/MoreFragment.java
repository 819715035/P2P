package cn.cndoppler.p2p.fragment;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cndoppler.p2p.R;
import cn.cndoppler.p2p.activity.GestureEditActivity;
import cn.cndoppler.p2p.activity.GuiGuInvestActivity;
import cn.cndoppler.p2p.activity.UserRegistActivity;
import cn.cndoppler.p2p.common.AppNetConfig;
import cn.cndoppler.p2p.common.BaseActivity;
import cn.cndoppler.p2p.common.BaseFragment;
import cn.cndoppler.p2p.util.ToastUtils;
import cn.cndoppler.p2p.util.UIUtils;
import cn.sharesdk.onekeyshare.OnekeyShare;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks{


    @BindView(R.id.tv_more_regist)
    TextView tvMoreRegist;
    @BindView(R.id.toggle_more)
    ToggleButton toggleMore;
    @BindView(R.id.tv_more_reset)
    TextView tvMoreReset;
    @BindView(R.id.tv_more_phone)
    TextView tvMorePhone;
    @BindView(R.id.rl_more_contact)
    RelativeLayout rlMoreContact;
    @BindView(R.id.tv_more_fankui)
    TextView tvMoreFankui;
    @BindView(R.id.tv_more_share)
    TextView tvMoreShare;
    @BindView(R.id.tv_more_about)
    TextView tvMoreAbout;
    private SharedPreferences sp;

    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    protected void initData(String content) {
        //初始化SharedPreferences
        sp = getActivity().getSharedPreferences("secret_protect", Context.MODE_PRIVATE);
        //用户注册
        userResgist();

        //获取当前设置手势密码的ToggleButton的状态
        getGestureStatus();

        //设置手势密码
        setGesturePassword();

        //重置手势密码
        resetGesture();

        //联系客服
        contactService();

        //提交反馈意见
        commitFanKui();

        //分享
        share();

        //关于硅谷理财
        aboutInvest();
    }

    private void aboutInvest() {
        tvMoreAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) MoreFragment.this.getActivity()).openActivity(GuiGuInvestActivity.class);
            }
        });
    }

    private void userResgist() {
        tvMoreRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) MoreFragment.this.getActivity()).openActivity(UserRegistActivity.class);
            }
        });
    }

    private void getGestureStatus() {
        boolean isOpen = sp.getBoolean("isOpen", false);
        toggleMore.setChecked(isOpen);
    }

    private void setGesturePassword() {
        toggleMore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    UIUtils.toast("开启了手势密码", false);
//                    sp.edit().putBoolean("isOpen", true).commit();
                    String inputCode = sp.getString("inputCode", "");
                    if (TextUtils.isEmpty(inputCode)) {//之前没有设置过
                        new AlertDialog.Builder(MoreFragment.this.getActivity())
                                .setTitle("设置手势密码")
                                .setMessage("是否现在设置手势密码")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ToastUtils.showToastShort(getActivity(),"现在设置手势密码");
                                        sp.edit().putBoolean("isOpen", true).commit();
//                                            toggleMore.setChecked(true);
                                        //开启新的activity:
                                        ((BaseActivity) MoreFragment.this.getActivity()).openActivity(GestureEditActivity.class);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ToastUtils.showToastShort(getActivity(),"取消了现在设置手势密码");
                                        sp.edit().putBoolean("isOpen", false).commit();
                                        toggleMore.setChecked(false);

                                    }
                                })
                                .show();
                    } else {
                        ToastUtils.showToastShort(getActivity(),"开启手势密码");
                        sp.edit().putBoolean("isOpen", true).commit();
//                        toggleMore.setChecked(true);
                    }
                } else {
                    ToastUtils.showToastShort(getActivity(),"关闭了手势密码");
                    sp.edit().putBoolean("isOpen", false).commit();
//                    toggleMore.setChecked(false);

                }
            }
        });
    }

    private void resetGesture() {
        tvMoreReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = toggleMore.isChecked();
                if (checked) {
                    ((BaseActivity) MoreFragment.this.getActivity()).openActivity(GestureEditActivity.class);
                } else {
                    ToastUtils.showToastShort(getActivity(),"手势密码操作已关闭，请开启后再设置");
                }
            }
        });
    }

    private void contactService() {
        rlMoreContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] perms = {Manifest.permission.CALL_PHONE};
                if (EasyPermissions.hasPermissions(getActivity(), perms)) {
                    showCallPhoneDialog();
                } else {
                    EasyPermissions.requestPermissions(getActivity(), "拨打电话需要授权", 0, perms);
                }

            }
        });
    }

    private void showCallPhoneDialog() {
        new AlertDialog.Builder(MoreFragment.this.getActivity())
                .setTitle("联系客服")
                .setMessage("是否现在联系客服：010-56253825")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            //获取手机号码
                            String phone = tvMorePhone.getText().toString().trim();
                            //使用隐式意图，启动系统拨号应用界面
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + phone));
                            getActivity().startActivity(intent);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private String department = "不明确";
    private void commitFanKui() {
        tvMoreFankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提供一个View
                View view = View.inflate(MoreFragment.this.getActivity(), R.layout.view_fankui, null);
                final RadioGroup rg = (RadioGroup) view.findViewById(R.id.rg_fankui);
                final EditText et_fankui_content = (EditText) view.findViewById(R.id.et_fankui_content);

                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton rb = (RadioButton) rg.findViewById(checkedId);
                        department = rb.getText().toString();
                    }
                });

                new AlertDialog.Builder(MoreFragment.this.getActivity())
                        .setView(view)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //获取反馈的信息
                                String content = et_fankui_content.getText().toString();
                                //联网发送反馈信息
                                AsyncHttpClient client = new AsyncHttpClient();
                                String url = AppNetConfig.FEEDBACK;
                                RequestParams params = new RequestParams();
                                params.put("department",department);
                                params.put("content",content);
                                client.post(url,params,new AsyncHttpResponseHandler(){
                                    @Override
                                    public void onSuccess(String content) {
                                        ToastUtils.showToastShort(getActivity(),"发送反馈信息成功");

                                    }

                                    @Override
                                    public void onFailure(Throwable error, String content) {
                                        ToastUtils.showToastShort(getActivity(),"发送反馈信息失败");

                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

            }
        });
    }

    private void share() {
        tvMoreShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
    }

    public void showShare(){
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://www.baidu.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("世界上最遥远的距离，是我在if里你在else里，似乎一直相伴又永远分离；\\n\" +\n" +
                "        \"     世界上最痴心的等待，是我当case你是switch，或许永远都选不上自己；\\n\" +\n" +
                "        \"     世界上最真情的相依，是你在try我在catch。无论你发神马脾气，我都默默承受，静静处理。到那时，再来期待我们的finally。");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.baidu.com");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("word天哪，说的太精辟了！");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this.getActivity());
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
        return R.layout.fragment_more;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        showCallPhoneDialog();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        ToastUtils.showToastShort(getActivity(),"请授权才能拨打电话");
    }
}
