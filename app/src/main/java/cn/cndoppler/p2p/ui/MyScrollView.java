package cn.cndoppler.p2p.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import cn.cndoppler.p2p.util.UIUtils;

/**
 * Created by Administrator on 2017/11/2 0002.
 */

public class MyScrollView extends ScrollView {

    private View childView;
    private boolean isFinishAnimation = true;//是否动画结束;

    public MyScrollView(Context context) {
        this(context,null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount()>0){
            childView = getChildAt(0);
        }
    }

    int lastY,downX, downY;;//上一次y轴方向操作的坐标位置
    private Rect normal = new Rect();//用于记录临界状态的左、上、右、下

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY = lastY = (int) ev.getY();
                downX =  (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int absX = (int) Math.abs(ev.getX() - downX);
                int absY = (int) Math.abs(ev.getY() - downY);
                if (absY>absX && absY> UIUtils.dp2px(10)){
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (childView == null || !isFinishAnimation){
            return super.onTouchEvent(ev);
        }
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) (ev.getY()-lastY);
                if (isNeedMove()){
                    if (normal.isEmpty()){
                        //记录了childView的临界状态的左、上、右、下
                        normal.set(childView.getLeft(),childView.getTop(),childView.getRight(),childView.getBottom());
                    }
                    //重新布局
                    childView.layout(childView.getLeft(),childView.getTop()+dy/2,childView.getRight(),childView.getBottom()+dy/2);
                }
                lastY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()){
                    //使用平移动画
                    int translateY = childView.getBottom() - normal.bottom;
                    TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -translateY);
                    translateAnimation.setDuration(200);
//                translateAnimation.setFillAfter(true);//停留在最终位置上

                    translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            isFinishAnimation = false;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isFinishAnimation = true;
                            childView.clearAnimation();//清除动画
                            //重新布局
                            childView.layout(normal.left, normal.top, normal.right, normal.bottom);
                            //清除normal的数据
                            normal.setEmpty();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    //启动动画
                    childView.startAnimation(translateAnimation);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public boolean isNeedMove(){
        int childHeight = childView.getMeasuredHeight();//得到子控件的高度
        int scrollviewHeight = getMeasuredHeight();//得到控件的高度
        int dy = childHeight-scrollviewHeight;
        int scrollY = this.getScrollY();//获取用户在y轴方向上的偏移量 （上 + 下 -）
        if (scrollY<=0 || scrollY>=dy){
            return true;//按照我们自定义的MyScrollView的方式处理
        }
        //其他处在临界范围内的，返回false。即表示，仍按照ScrollView的方式处理
        return false;
    }

    //判断是否需要执行平移动画
    private boolean isNeedAnimation() {
        return !normal.isEmpty();

    }
}
