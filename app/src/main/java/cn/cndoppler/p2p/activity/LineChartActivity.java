package cn.cndoppler.p2p.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.cndoppler.p2p.R;
import cn.cndoppler.p2p.common.ActivityManager;
import cn.cndoppler.p2p.common.BaseActivity;
import cn.cndoppler.p2p.ui.CommonTitleView;

public class LineChartActivity extends BaseActivity {

    @BindView(R.id.titleView)
    CommonTitleView titleView;
    @BindView(R.id.line_chart)
    LineChart lineChart;
    private Typeface mTf;//声明字体库
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        ButterKnife.bind(this);
        setListener();
        initData();
    }

    private void initData() {
        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        //设置当前的折线图的描述
        lineChart.setDescription("林丹出轨事件的关注度");
        //是否绘制网格背景
        lineChart.setDrawGridBackground(true);

        //获取当前的x轴对象
        XAxis xAxis = lineChart.getXAxis();
        //设置x轴的显示位置
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        //设置x轴的字体
        xAxis.setTypeface(mTf);
        //是否绘制x轴的网格线
        xAxis.setDrawGridLines(false);
        //是否绘制x轴的轴线
        xAxis.setDrawAxisLine(true);

        //获取左边的y轴对象
        YAxis leftAxis = lineChart.getAxisLeft();
        //设置左边y轴的字体
        leftAxis.setTypeface(mTf);
        //参数1：左边y轴提供的区间的个数。 参数2：是否均匀分布这几个区间。 false：均匀。 true：不均匀
        leftAxis.setLabelCount(5, false);

        //获取右边的y轴对象
        YAxis rightAxis = lineChart.getAxisRight();
        //将右边的y轴设置为不显式的
        rightAxis.setEnabled(false);

        // 提供折线数据。（通常情况下，折线的数据来自于服务器的数据）
        LineData lineData = generateDataLine();
        lineChart.setData(lineData);

        // 设置x轴方向的动画。执行时间是750.
        // 不需要再执行：invalidate();
        lineChart.animateX(750);
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private LineData generateDataLine() {
        //折线1：
        ArrayList<Entry> e1 = new ArrayList<Entry>();
        //提供折线中点的数据
        for (int i = 0; i < 12; i++) {
            e1.add(new Entry((int) (Math.random() * 65) + 40, i));
        }

        LineDataSet d1 = new LineDataSet(e1, "New DataSet 1");
        //设置折线的宽度
        d1.setLineWidth(4.5f);
        //设置小圆圈的尺寸
        d1.setCircleSize(4.5f);
        //设置高亮的颜色
        d1.setHighLightColor(Color.rgb(244, 0, 0));
        //是否显示小圆圈对应的数值
        d1.setDrawValues(true);

        //折线2：
//        ArrayList<Entry> e2 = new ArrayList<Entry>();
//
//        for (int i = 0; i < 12; i++) {
//            e2.add(new Entry(e1.get(i).getVal() - 30, i));
//        }
//
//        LineDataSet d2 = new LineDataSet(e2, "New DataSet " + cnt + ", (2)");
//        d2.setLineWidth(2.5f);
//        d2.setCircleSize(4.5f);
//        d2.setHighLightColor(Color.rgb(244, 117, 117));
//        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
//        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
//        d2.setDrawValues(false);

        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);

        LineData cd = new LineData(getMonths(), sets);
        return cd;
    }

    private ArrayList<String> getMonths() {

        ArrayList<String> m = new ArrayList<String>();
        m.add("Jan");
        m.add("Feb");
        m.add("Mar");
        m.add("Apr");
        m.add("May");
        m.add("Jun");
        m.add("Jul");
        m.add("Aug");
        m.add("Sep");
        m.add("Okt");
        m.add("Nov");
        m.add("Dec");

        return m;
    }

    private void setListener() {
        titleView.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().removeCurrent();
            }
        });
    }
}
