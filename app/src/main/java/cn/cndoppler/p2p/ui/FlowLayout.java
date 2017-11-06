package cn.cndoppler.p2p.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.cndoppler.p2p.util.LogUtils;

/**
 * Created by Administrator on 2017/11/6 0006.
 */

public class FlowLayout extends ViewGroup{

    private List<LayoutLocation> layoutLocation = new ArrayList<>();
    public FlowLayout(Context context) {
        this(context,null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //能够设置当前布局的宽度和高度
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode =  MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //如果用户使用的至多模式，那么使用如下两个变量计算真实的宽高值。
        int width = 0;
        int height = 0;
        //每一行的宽度
        int lineWidth = 0;
        int lineHeight = 0;
        //获取子视图
        int childCound = getChildCount();
        for (int i = 0;i<childCound;i++){
            View childView = getChildAt(i);
            //只有调用了如下的方法，方可计算子视图的测量的宽高
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
            //获取子视图的宽高
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            //要想保证可以获取子视图的边距参数对象，必须重写generateLayoutParams().
            MarginLayoutParams mp = (MarginLayoutParams) childView.getLayoutParams();
            if (lineWidth+childWidth+mp.leftMargin+mp.rightMargin<=widthSize){
                //不换行
                //子控件的位置
                LayoutLocation ll = new LayoutLocation(lineWidth+mp.leftMargin,height+mp.topMargin,
                        lineWidth+childWidth+mp.rightMargin+mp.leftMargin,
                        height+childHeight+mp.topMargin+mp.bottomMargin);
                layoutLocation.add(ll);
                lineWidth += childWidth+mp.rightMargin+mp.leftMargin;
                lineHeight = Math.max(lineHeight,childHeight+mp.topMargin+mp.bottomMargin);
            }else{
                //换行
                width = Math.max(width,lineWidth);
                height += lineHeight;
                //重置
                lineWidth = childWidth + mp.leftMargin + mp.rightMargin;
                lineHeight = childHeight + mp.topMargin + mp.bottomMargin;
                LayoutLocation ll = new LayoutLocation(mp.leftMargin,height+mp.topMargin,
                        childWidth+mp.rightMargin+mp.leftMargin,
                        height+childHeight+mp.topMargin+mp.bottomMargin);
                layoutLocation.add(ll);
            }

            LogUtils.e("widthSize = " + widthSize + ",heightSize = " + heightSize);
            LogUtils.e("width = " + width + ",height = " + height);
            //设置当前流式布局的宽高
            setMeasuredDimension(widthMode == MeasureSpec.EXACTLY?widthSize:width,
                    heightMode == MeasureSpec.EXACTLY?heightSize:height);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        MarginLayoutParams mp = new MarginLayoutParams(getContext(),attrs);
        return mp;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i=0;i<getChildCount()-1;i++){
            View childView = getChildAt(i);
            LayoutLocation ll = layoutLocation.get(i);
            childView.layout(ll.left,ll.top,ll.right,ll.bottom);
        }
    }

    class LayoutLocation{
        private int left;
        private int right;
        private int top;
        private int bottom;

        public LayoutLocation(int left, int top, int right, int bottom) {
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }
    }
}
