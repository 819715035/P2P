package cn.cndoppler.p2p.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cndoppler.p2p.R;
import cn.cndoppler.p2p.bean.Image;
import cn.cndoppler.p2p.bean.Index;
import cn.cndoppler.p2p.bean.Product;
import cn.cndoppler.p2p.common.AppNetConfig;
import cn.cndoppler.p2p.util.ToastUtils;
import cn.cndoppler.p2p.util.UIUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_home_product)
    TextView tvHomeProduct;
    @BindView(R.id.tv_home_yearrate)
    TextView tvHomeYearrate;
    Unbinder unbinder;
    private View view;
    private Index index;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        //初始化数据
        initData();

        return view;
    }

    private void initData() {
        //
        index = new Index();
        AsyncHttpClient client = new AsyncHttpClient();
        //访问的url
        String url = AppNetConfig.INDEX;
        client.post(url,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                //200：响应成功
                //解析json数据：GSON / FASTJSON
                JSONObject jsonObject = JSON.parseObject(content);
                //解析json对象数据
                String proInfo = jsonObject.getString("proInfo");
                Product product = JSON.parseObject(proInfo, Product.class);
                //解析json数组数据
                String imageArr = jsonObject.getString("imageArr");
                List<Image> images = jsonObject.parseArray(imageArr, Image.class);
                index.product = product;
                index.images = images;
                //更新页面数据
                tvHomeProduct.setText(product.name);
                tvHomeYearrate.setText(product.yearRate + "%");

                //设置banner样式
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
                //设置图片加载器
                banner.setImageLoader(new GlideImageLoader());
                //设置图片地址构成的集合
                ArrayList<String> imagesUrl = new ArrayList<String>(index.images.size());
//                for(int i = 0; i < imagesUrl.size(); i++) {//imagesUrl.size():0
                for (int i = 0; i < index.images.size(); i++) {//index.images.size():4
                    imagesUrl.add(AppNetConfig.BASE_URL+index.images.get(i).IMAURL);
                }
                //设置图片集合
                banner.setImages(imagesUrl);
                //设置banner动画效果
                banner.setBannerAnimation(Transformer.DepthPage);
                //设置标题集合（当banner样式有显示title时）
                String[] titles = new String[]{"分享砍学费","人脉总动员","想不到你是这样的app","购物节，爱不单行"};
                banner.setBannerTitles(Arrays.asList(titles));
                //设置自动轮播，默认为true
                banner.isAutoPlay(true);
                //设置轮播时间
                banner.setDelayTime(1500);
                //设置指示器位置（当banner模式中有指示器时）
                banner.setIndicatorGravity(BannerConfig.CENTER);
                //banner设置方法全部调用完毕时最后调用
                banner.start();
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
               ToastUtils.showToastShort(UIUtils.getContext(), "联网获取数据失败");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */

            //Glide 加载图片简单用法
            Glide.with(context).load(path).into(imageView);

            //用fresco加载图片简单用法，记得要写下面的createImageView方法
            Uri uri = Uri.parse((String) path);
            imageView.setImageURI(uri);
        }
    }
}
