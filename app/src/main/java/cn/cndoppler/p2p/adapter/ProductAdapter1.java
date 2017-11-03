package cn.cndoppler.p2p.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.cndoppler.p2p.R;
import cn.cndoppler.p2p.bean.Product;
import cn.cndoppler.p2p.ui.RoundProgress;

/**
 * Created by Administrator on 2017/11/3 0003.
 */

public class ProductAdapter1 extends MyBaseAdapter1<Product> {

    public ProductAdapter1(List<Product> list) {
        super(list);
    }

    @Override
    public View myGetView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_product_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //装配数据
        Product product = list.get(position);
        viewHolder.pMinnum.setText(product.memberNum);
        viewHolder.pMinzouzi.setText(product.minTouMoney);
        viewHolder.pMoney.setText(product.money);
        viewHolder.pName.setText(product.name);
        viewHolder.pProgresss.setProgress(Integer.parseInt(product.progress));
        viewHolder.pSuodingdays.setText(product.suodingDays);
        viewHolder.pYearlv.setText(product.yearRate);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.p_name)
        TextView pName;
        @BindView(R.id.p_money)
        TextView pMoney;
        @BindView(R.id.p_yearlv)
        TextView pYearlv;
        @BindView(R.id.p_suodingdays)
        TextView pSuodingdays;
        @BindView(R.id.p_minzouzi)
        TextView pMinzouzi;
        @BindView(R.id.p_minnum)
        TextView pMinnum;
        @BindView(R.id.p_progresss)
        RoundProgress pProgresss;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
