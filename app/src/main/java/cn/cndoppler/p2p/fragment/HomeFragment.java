package cn.cndoppler.p2p.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import cn.cndoppler.p2p.R;
import cn.cndoppler.p2p.bean.Image;
import cn.cndoppler.p2p.bean.Index;
import cn.cndoppler.p2p.bean.Product;
import cn.cndoppler.p2p.common.AppNetConfig;
import cn.cndoppler.p2p.common.BaseFragment;
import cn.cndoppler.p2p.ui.RoundProgress;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {


    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_home_product)
    TextView tvHomeProduct;
    @BindView(R.id.tv_home_yearrate)
    TextView tvHomeYearrate;
    @BindView(R.id.roundProgress)
    RoundProgress roundProgress;
    private View view;
    private Index index;
    private int currentProress;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < currentProress; i++) {
                roundProgress.setProgress(i + 1);

                SystemClock.sleep(20);
                //强制重绘
//                roundProHome.invalidate();//只有主线程才可以如此调用
                roundProgress.postInvalidate();//主线程、分线程都可以如此调用
            }
        }
    };

    public HomeFragment() {
        // Required empty public constructor

    }

    public void initData(String content) {
        //
        index = new Index();
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
        //获取数据中的进度值
        currentProress = Integer.parseInt(index.product.progress);

        //在分线程中，实现进度的动态变化
        new Thread(runnable).start();


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
        banner.setImages(imagesUrl);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        String[] titles = new String[]{"分享砍学费", "人脉总动员", "想不到你是这样的app", "购物节，爱不单行"};
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
    protected RequestParams getParams() {
        return null;
    }

    @Override
    protected String getUrl() {
        return AppNetConfig.INDEX;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_home;
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
