package cn.cndoppler.p2p.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cndoppler.p2p.R;
import cn.cndoppler.p2p.bean.Index;
import cn.cndoppler.p2p.common.BaseFragment;
import cn.cndoppler.p2p.ui.randomLayout.StellarMap;
import cn.cndoppler.p2p.util.ToastUtils;
import cn.cndoppler.p2p.util.UIUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductRecommondFragment extends BaseFragment {


    @BindView(R.id.stellar_map)
    StellarMap stellarMap;
    //提供装配的数据
    private String[] datas = new String[]{"新手福利计划", "财神道90天计划", "硅谷钱包计划", "30天理财计划(加息2%)", "180天理财计划(加息5%)", "月月升理财计划(加息10%)",
            "中情局投资商业经营", "大学老师购买车辆", "屌丝下海经商计划", "美人鱼影视拍摄投资", "Android培训老师自己周转", "养猪场扩大经营",
            "旅游公司扩大规模", "铁路局回款计划", "屌丝迎娶白富美计划"
    };
    private Random random = new Random();

    public ProductRecommondFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initData(String content) {
        StellarAdapter adapter = new StellarAdapter();
        stellarMap.setAdapter(adapter);
        //设置stellarMap的内边距
        int leftPadding = UIUtils.dp2px(10);
        int topPadding = UIUtils.dp2px(5);
        int rightPadding = UIUtils.dp2px(10);
        int bottomPadding = UIUtils.dp2px(5);
        stellarMap.setInnerPadding(leftPadding, topPadding, rightPadding, bottomPadding);
        //必须调用如下的两个方法，否则stellarMap不能显示数据
        //设置显示的数据在x轴、y轴方向上的稀疏度
        stellarMap.setRegularity(5,7);
        //设置初始化显示的组别，以及是否需要使用动画
        stellarMap.setGroup(0,true);
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
        return R.layout.fragment_product_recommond;
    }

    //提供Adapter的实现类
    class StellarAdapter implements StellarMap.Adapter {

        //获取组的个数
        @Override
        public int getGroupCount() {
            return 2;
        }

        //返回每组中显示的数据的个数
        @Override
        public int getCount(int group) {
            if (group ==0){
                return datas.length / 2;
            }else{
                return datas.length-datas.length/2;
            }
        }

        //返回具体的view.
        //position:不同的组别，position都是从0开始。
        @Override
        public View getView(int group, int position, View convertView) {
            final TextView tv = new TextView(getActivity());
            int index;
            //设置文本的内容
            if (group ==0){
                index = position;
            }else{
                index = group*8+position-1;
            }
            tv.setText(datas[index]);
            //设置字体的大小
            tv.setTextSize(UIUtils.dp2px(5+random.nextInt(5)));
            //设置字体的颜色
            int red = random.nextInt(211);//00 ~ ff ; 0 ~ 255
            int green = random.nextInt(211);
            int blue = random.nextInt(211);
            tv.setTextColor(Color.rgb(red, green, blue));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showToastShort(getActivity(),tv.getText().toString());
                }
            });
            return tv;
        }

        //返回下一组显示平移动画的组别。查看源码发现，此方法从未被调用。所以可以不重写
        @Override
        public int getNextGroupOnPan(int group, float degree) {
            return 0;
        }

        //返回下一组显示缩放动画的组别。
        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            if (group ==0){
                return 1;
            }
            return 0;
        }
    }

}
