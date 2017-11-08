package cn.cndoppler.p2p.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.cndoppler.p2p.R;
import cn.cndoppler.p2p.common.ActivityManager;
import cn.cndoppler.p2p.common.BaseActivity;
import cn.cndoppler.p2p.ui.CommonTitleView;

public class PieChartActivity extends BaseActivity {

    @BindView(R.id.titleView)
    CommonTitleView titleView;
    @BindView(R.id.pie_chart)
    PieChart pieChart;
    private Typeface mTf;//声明字体库
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        ButterKnife.bind(this);
        setListener();
        initData();
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
        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        // apply styling
        pieChart.setDescription("android厂商2016年手机占有率");
        pieChart.setHoleRadius(52f);//最内层的圆的半径
        pieChart.setTransparentCircleRadius(60f);//包裹内层圆的半径
        pieChart.setCenterText("Android\n厂商占比");
        pieChart.setCenterTextTypeface(mTf);
        pieChart.setCenterTextSize(18f);
        //是否使用百分比。true:各部分的百分比的和是100%。
        pieChart.setUsePercentValues(true);

        PieData pieData = generateDataPie();
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTypeface(mTf);
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.RED);
        // set data
        pieChart.setData(pieData);

        //获取右上角的描述结构的对象
        Legend l = pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setYEntrySpace(10f);//相邻的entry在y轴上的间距
        l.setYOffset(30f);//第一个entry距离最顶端的间距

        // do not forget to refresh the chart
        // pieChart.invalidate();
        pieChart.animateXY(900, 900);
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private PieData generateDataPie() {

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int i = 0; i < 4; i++) {
            entries.add(new Entry((int) (Math.random() * 70) + 30, i));
        }

        PieDataSet d = new PieDataSet(entries, "");

        // 相邻模块的间距
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(getQuarters(), d);
        return cd;
    }

    private ArrayList<String> getQuarters() {

        ArrayList<String> q = new ArrayList<String>();
        q.add("三星");
        q.add("华为");
        q.add("oppo");
        q.add("vivo");

        return q;
    }
}
