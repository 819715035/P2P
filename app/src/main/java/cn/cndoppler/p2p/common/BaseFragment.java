package cn.cndoppler.p2p.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cndoppler.p2p.ui.LoadingPage;
import cn.cndoppler.p2p.util.UIUtils;

/**
 * Created by Administrator on 2017/11/2 0002.
 */

public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;
    private LoadingPage loadingPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        loadingPage = new LoadingPage(container.getContext()) {

            @Override
            public int layoutId() {
                return getLayout();
            }

            @Override
            protected RequestParams params() {
                return getParams();
            }

            @Override
            public String url() {
                return getUrl();
            }

            @Override
            protected void onSuccss(ResultState resultState, View view_success) {
                unbinder = ButterKnife.bind(BaseFragment.this, view_success);
                initData(resultState.getContent());
            }

        };


        return loadingPage;
    }

    //为了保证loadingPage不为null


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        show();
    }

    //初始化
    protected abstract void initData(String content);

    protected abstract RequestParams getParams();

    protected abstract String getUrl();

    //得到布局id
    public abstract int getLayout();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void show(){
        loadingPage.show();
    }
}
