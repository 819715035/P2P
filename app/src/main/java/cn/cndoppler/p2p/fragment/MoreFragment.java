package cn.cndoppler.p2p.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;

import cn.cndoppler.p2p.R;
import cn.cndoppler.p2p.common.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends BaseFragment {


    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    protected void initData(String content) {

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

}
