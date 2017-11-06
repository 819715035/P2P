package cn.cndoppler.p2p.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import cn.cndoppler.p2p.R;
import cn.cndoppler.p2p.util.UIUtils;

/**
 * Created by Administrator on 2017/11/2 0002.
 */

public class RoundProgress extends View {

    private int width;
    private int height;
    private int radio;
    private Paint paint;
    private int centerX;
    private int centerY;
    private float lineHeight;
    private float progress;
    private float maxProgress;
    private int roundColor;
    private int arcColor;
    private int textColor;
    private float textSize;
    private float tempProgress;

    public RoundProgress(Context context) {
        this(context,null);
    }

    public RoundProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttrs(context,attrs);
        initView();
    }


    //设置控件属性
    private void setAttrs(Context context,AttributeSet attrs) {
        TypedArray styleable = context.obtainStyledAttributes(attrs,R.styleable.RoundProgress);
        lineHeight = styleable.getDimension(R.styleable.RoundProgress_lineHeight,UIUtils.dp2px(10));
        progress = styleable.getFloat(R.styleable.RoundProgress_progress,0);
        maxProgress = styleable.getFloat(R.styleable.RoundProgress_maxProgress,100);
        roundColor = styleable.getColor(R.styleable.RoundProgress_roundColor,getResources().getColor(R.color.product_detail_common));
        arcColor = styleable.getColor(R.styleable.RoundProgress_arcColor,getResources().getColor(R.color.round_red_common));
        textColor = styleable.getColor(R.styleable.RoundProgress_textColor,getResources().getColor(R.color.text_progress));
        textSize = styleable.getDimension(R.styleable.RoundProgress_textSize,UIUtils.dp2px(12));
        styleable.recycle();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //线的厚度
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        radio = (int) (Math.min(width,height)/2-lineHeight/2);
        centerX = width/2;
        centerY = height/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(centerX,centerY);
        paint.setColor(roundColor);
        paint.setStrokeWidth(lineHeight);
        RectF rectF = new RectF(-radio,-radio,radio,radio);
        //绘制圆环
        canvas.drawOval(rectF,paint);
        //绘制圆弧
        paint.setColor(arcColor);
        canvas.drawArc(rectF,0,progress/maxProgress*360,false,paint);
        //绘制文本
        String progressText = ((int)(progress*10000/maxProgress))/100+"%";
        paint.setStrokeWidth(0);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        Rect bounds = new Rect();
        paint.getTextBounds(progressText,0,progressText.length(),bounds);
        canvas.drawText(progressText,-bounds.width()/2,bounds.height()/2,paint);
    }

    public void  setMaxProgress(float maxProgress){
        this.maxProgress = maxProgress;
    }
    public void  setProgress(float progress){
        this.progress = progress;
    }
}
