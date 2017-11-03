package cn.cndoppler.p2p.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cndoppler.p2p.R;
import cn.cndoppler.p2p.adapter.ProductAdapter;
import cn.cndoppler.p2p.adapter.ProductAdapter1;
import cn.cndoppler.p2p.bean.Product;
import cn.cndoppler.p2p.common.AppNetConfig;
import cn.cndoppler.p2p.common.BaseFragment;
import cn.cndoppler.p2p.ui.MyTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends BaseFragment {


    @BindView(R.id.tv_product_title)
    MyTextView tvProductTitle;
    @BindView(R.id.lv_product_list)
    ListView lvProductList;
    private List<Product> productList;

    public ProductListFragment() {
        // Required empty public constructor
    }

    @Override
    protected void initData(String content) {
        if (!TextUtils.isEmpty(content)){
            JSONObject jsonObject = JSON.parseObject(content);
            boolean success = jsonObject.getBoolean("success");
            if(success) {
                String data = jsonObject.getString("data");
                //获取集合数据
                productList = JSON.parseArray(data, Product.class);

                //方式一：没有抽取
           /* ProductAdapter productAdapter = new ProductAdapter(productList);
            lvProductList.setAdapter(productAdapter);//显示列表*/

//            //方式二：抽取了，但是抽取力度小 （可以作为选择）
            ProductAdapter1 productAdapter1 = new ProductAdapter1(productList);
            lvProductList.setAdapter(productAdapter1);//显示列表

                //方式三：抽取了，但是没有使用ViewHolder，getView()优化的不够
//            ProductAdapter2 productAdapter2 = new ProductAdapter2(productList);
//            lvProductList.setAdapter(productAdapter2);//显示列表

                //方式四：抽取了，最好的方式.（可以作为选择）
               /* ProductAdapter3 productAdapter3 = new ProductAdapter3(productList);
                lvProductList.setAdapter(productAdapter3);//显示列表*/
            }
        }
    }

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    protected String getUrl() {
        return AppNetConfig.PRODUCT;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_product_list;
    }

}
