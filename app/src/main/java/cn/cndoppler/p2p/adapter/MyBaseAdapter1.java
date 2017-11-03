package cn.cndoppler.p2p.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/11/3 0003.
 */

public abstract class MyBaseAdapter1<T> extends BaseAdapter {

    public List<T> list;

    public MyBaseAdapter1(List<T> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = myGetView(position,convertView, parent);
        return convertView;
    }

    public abstract View myGetView(int position, View convertView, ViewGroup parent);
}
