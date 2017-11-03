package cn.cndoppler.p2p.fragment;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cndoppler.p2p.R;
import cn.cndoppler.p2p.common.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class InverstFragment extends BaseFragment {


    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.product_vp)
    ViewPager productVp;
    private List<BaseFragment> fragmentList = new ArrayList<>();;

    public InverstFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initData(String content) {
        //1.加载三个不同的Fragment：ProductListFragment,ProductRecommondFragment,ProductHotFragment.
        initFragments();
        //2.ViewPager设置三个Fragment的显示
        MyAdapter adapter = new MyAdapter(getFragmentManager());
        productVp.setAdapter(adapter);
        //将tablayout与ViewPager关联
        tablayout.setupWithViewPager(productVp);

    }

    private void initFragments() {
        ProductListFragment productListFragment = new ProductListFragment();
        ProductRecommondFragment productRecommondFragment = new ProductRecommondFragment();
        ProductHotFragment productHotFragment = new ProductHotFragment();
        //添加到集合中
        fragmentList.add(productListFragment);
        fragmentList.add(productRecommondFragment);
        fragmentList.add(productHotFragment);
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
        return R.layout.fragment_invest;
    }

    /**
     * 提供PagerAdapter的实现
     * 如果ViewPager中加载的是Fragment,则提供的Adpater可以继承于具体的：FragmentStatePagerAdapter或FragmentPagerAdapter
     * FragmentStatePagerAdapter:适用于ViewPager中加载的Fragment过多，会根据最近最少使用算法，实现内存中Fragment的清理，避免溢出
     * FragmentPagerAdapter:适用于ViewPager中加载的Fragment不多时，系统不会清理已经加载的Fragment。
     */
    class MyAdapter extends FragmentPagerAdapter {


        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

        //提供tablayout的显示内容
        @Override
        public CharSequence getPageTitle(int position) {
            //方式一：
            if(position == 0){
                return "全部理财";
            }else if(position == 1){
                return "推荐理财";
            }else if(position == 2){
                return "热门理财";
            }else{
                return "全部理财";
            }
           /* //方式二：
            return UIUtils.getStringArr(R.array.invest_tab)[position];*/
        }
    }
}
