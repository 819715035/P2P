package cn.cndoppler.p2p.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.cndoppler.p2p.R;
import cn.cndoppler.p2p.util.UiUtil;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public class CommonTitleView extends LinearLayout {

    private TextView titleTv;
    private View backIv;
    private boolean showLeftView,showRightView;
    private View rightIv;
    private int titleSize;
    private int titleColor;
    private String titleContent;
    private View view;
    private OnClickListener leftListener,rightListener;

    public CommonTitleView(Context context) {
        this(context,null);
    }

    public CommonTitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CommonTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        view = View.inflate(context, R.layout.common_title,this);
        initView();
        getAttrs(context,attrs);
        setView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        titleTv= (TextView) view.findViewById(R.id.title_tv);
        backIv=  view.findViewById(R.id.back_iv);
        rightIv= view.findViewById(R.id.right_iv);
    }

    /**
     * 获取属性
     */
    public void getAttrs(Context context,AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.commonTitleView);
        //是否显示左边，默认不显示
        showLeftView = typedArray.getBoolean(R.styleable.commonTitleView_show_left,false);
        //是否显示右边，默认不显示
        showRightView = typedArray.getBoolean(R.styleable.commonTitleView_show_right,false);
        //标题内容
        titleContent = typedArray.getString(R.styleable.commonTitleView_title_context);
        //标题颜色,默认为#26B8EE颜色
        titleColor = typedArray.getColor(R.styleable.commonTitleView_title_color,Color.parseColor("#26B8EE"));
        //标题文字大小,默认20sp
        int  textSize = typedArray.getDimensionPixelSize(R.styleable.commonTitleView_title_size, UiUtil.dip2px(context,20));
        //转换成dp
        titleSize = UiUtil.px2dip(context,textSize);
    }


    /**
     * 设置控件
     */
    private void setView() {
        titleTv.setTextColor(titleColor);
        titleTv.setTextSize(titleSize);
        titleTv.setText(titleContent);
        if (showLeftView) {
            backIv.setVisibility(VISIBLE);
            backIv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (leftListener!=null){
                        leftListener.onClick(v);
                    }
                }
            });
        }else{
            backIv.setVisibility(INVISIBLE);
        }

        if (showRightView){
            rightIv.setVisibility(VISIBLE);
            rightIv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rightListener!=null){
                        rightListener.onClick(v);
                    }
                }
            });
        }else{
            rightIv.setVisibility(INVISIBLE);
        }
    }

    public void setLeftListener(OnClickListener leftListener){
        this.leftListener = leftListener;
    }

    public void setRightListener(OnClickListener rightListener){
        this.rightListener = rightListener;
    }
}
