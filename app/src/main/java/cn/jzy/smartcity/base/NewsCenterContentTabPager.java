package cn.jzy.smartcity.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzy.smartcity.R;
import cn.jzy.smartcity.adapter.NewsListAdapter;
import cn.jzy.smartcity.adapter.SwitchImageVPAdapter;
import cn.jzy.smartcity.bean.NewsCenterTabBean;
import cn.jzy.smartcity.utils.CacheUtils;
import cn.jzy.smartcity.utils.Constant;
import cn.jzy.smartcity.utils.MyLogger;
import cn.jzy.smartcity.utils.MyToast;
import cn.jzy.smartcity.utils.bitmap.BitmapUtils;
import cn.jzy.smartcity.view.RecycleViewDivider;
import cn.jzy.smartcity.view.RefreshRecyclerView;
import cn.jzy.smartcity.view.SwitchImageViewViewPager;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/1.
 * 新闻中心的内容tab页面
 */
public class NewsCenterContentTabPager implements ViewPager.OnPageChangeListener, RefreshRecyclerView.OnRefreshListener, RefreshRecyclerView.OnLoadMoreListener {
    private static final String TAG = "NewsCenterContentTabPager";
    //    @BindView(R.id.vp_switch_image)
    SwitchImageViewViewPager mVpSwitchImage;
    //    @BindView(R.id.tv_title)
    TextView mTvTitle;
    //    @BindView(R.id.ll_point_container)
    LinearLayout mLlPointContainer;
    @BindView(R.id.rv_news)
    RefreshRecyclerView mRvNews;

    private Context mContext;
    public View mView;
    private NewsCenterTabBean mNewsCenterTabBean;
    private List<ImageView> mImageViews;

    //处理轮播图的自动切换  （消息机制）
    private Handler mHandler = new Handler();

    //判断是否在切换
     public boolean hasSwitch;


    public NewsCenterContentTabPager(Context context) {
        mContext = context;
        mView = initView();
    }

    //加载布局
    private View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.newscenter_content_tab, null);

        //记得加this,不然会有问题,把当前的控件赋值给这个类
        ButterKnife.bind(this, view);

        return view;
    }

    public void loadNetData(final String url) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToast.show(mContext, "加载数据失败");

                        //读取缓存
                        try {
                            String json = CacheUtils.readCache(mContext, url);
                            if(!TextUtils.isEmpty(json)){
                                processData(json);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyLogger.i(TAG, response);
                        //解析数据
                        processData(response);

                        //缓存数据
                        try {
                            CacheUtils.saveCache(mContext,url,response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    //把json字符串转换成对应的数据模型
    private void processData(String json) {
        Gson gson = new Gson();
        mNewsCenterTabBean = gson.fromJson(json, NewsCenterTabBean.class);

        //把数据绑定给对应的控件
        bindDataToView();

        //把当前的NewsCenterContentTabPager对象传递给SwitchImageViewViewPager
        mVpSwitchImage.setTabPager(this);

        //把当前的NewsCenterContentTabPager对象传递给RefreshRecyclerView
        mRvNews.setContentTabPager(this);

    }

    //绑定数据给控件
    private void bindDataToView() {
        loadSwitchImageViewLayout();
        initSwitchImageView();
        initPoint();
        initRVNews();
    }

    //动态加载轮播图的布局
    private void loadSwitchImageViewLayout() {
        View view = View.inflate(mContext, R.layout.switch_imageview, null);
        //手动的初始化控件
        mVpSwitchImage = (SwitchImageViewViewPager) view.findViewById(R.id.vp_switch_image);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mLlPointContainer = (LinearLayout) view.findViewById(R.id.ll_point_container);
        //把轮播图添加给RefreshRecyclerView
        mRvNews.addSwitchImageView(view);
    }


    //初始化新闻列表
    private void initRVNews() {
        //设置布局管理器
        mRvNews.setLayoutManager(new LinearLayoutManager(mContext));
        //设置条目的分割线
        mRvNews.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, 1, Color.BLUE));

        //设置数据   RecyclerView   Adapter   ViewHolder
        NewsListAdapter adapter = new NewsListAdapter(mContext, mNewsCenterTabBean.data.news);
        mRvNews.setAdapter(adapter);

        //设置下拉刷新的监听
        mRvNews.setOnRefreshListener(this);

        //设置上拉加载的监听
        mRvNews.setOnLoadMoreListener(this);
    }

    //初始化轮播图的数据
    private void initSwitchImageView() {
        //加载图片，并绑定到Viewpager上
        mImageViews = new ArrayList<>();
        int size = mNewsCenterTabBean.data.topnews.size();
        //轮播图的前后各加一个图片
        for (int i = -1; i < size + 1; i++) {
            NewsCenterTabBean.TopNewsBean topNewsBean = null;
            if (i == -1) {
                //添加最后的一张图片
                topNewsBean = mNewsCenterTabBean.data.topnews.get(size - 1);
            } else if (i == size) {
                topNewsBean = mNewsCenterTabBean.data.topnews.get(0);
            } else {
                topNewsBean = mNewsCenterTabBean.data.topnews.get(i);
            }
            ImageView imageView = new ImageView(mContext);

            //采用我们自己的图片缓存工具
            BitmapUtils.display(mContext,imageView,topNewsBean.topimage);
            //Picasso.with(mContext).load(topNewsBean.topimage).into(imageView);
            mImageViews.add(imageView);
        }


        //创建轮播图适配器
        SwitchImageVPAdapter adapter = new SwitchImageVPAdapter(mImageViews, mNewsCenterTabBean.data.topnews);

        //绑定ViewPager
        mVpSwitchImage.setAdapter(adapter);

        //让轮播图默认显示第一张图片
        mVpSwitchImage.setCurrentItem(1, false);

        //初始化轮播图的文字显示
        mTvTitle.setText(mNewsCenterTabBean.data.topnews.get(0).title);

        //给轮播图设置滑动监听
        mVpSwitchImage.addOnPageChangeListener(this);


    }

    //初始化点
    private void initPoint() {
        //清空容器里面的布局
        mLlPointContainer.removeAllViews();

        for (int i = 0; i < mNewsCenterTabBean.data.topnews.size(); i++) {
            //小圆点
            View view = new View(mContext);
            //设置背景颜色
            view.setBackgroundResource(R.drawable.point_gray_bg);
            //布局参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            //右边距
            params.rightMargin = 20;
            mLlPointContainer.addView(view, params);
        }
        //让第一个点的背景为红色
        mLlPointContainer.getChildAt(0).setBackgroundResource(R.drawable.point_red_bg);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //修正下标
        int pageIndex = 0;
        //正确数据的大小
        int size = mNewsCenterTabBean.data.topnews.size();

        if (position == 0) {
            pageIndex = size - 1;
            //切换到最后一个页面
            mVpSwitchImage.setCurrentItem(size, false);
        } else if (position == size + 1) {
            pageIndex = 0;
            //切换到第一个页面
            mVpSwitchImage.setCurrentItem(1, false);
        } else {
            pageIndex = position - 1;
        }


        //设置轮播图的文字显示
        mTvTitle.setText(mNewsCenterTabBean.data.topnews.get(pageIndex).title);

        //修改轮播图点的背景
        int childCount = mLlPointContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = mLlPointContainer.getChildAt(i);
            if (i == pageIndex) {
                //选中的页面
                child.setBackgroundResource(R.drawable.point_red_bg);
            } else {
                //未选中的页面
                child.setBackgroundResource(R.drawable.point_gray_bg);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //开始切换
    public void startSwitch() {
        hasSwitch = true;
        //往Handler里面的消息队列里面发送一个延时的消息
        mHandler.postDelayed(new SwitchTask(), 3000);
    }

    //停止切换
    public void stopSwitch() {
        hasSwitch = false;
        mHandler.removeCallbacksAndMessages(null);
    }


    //切换的任务
    private class SwitchTask implements Runnable {
        @Override
        public void run() {
            if (mVpSwitchImage != null) {
                //切换逻辑
                int currentItem = mVpSwitchImage.getCurrentItem();
                //判断是否最后一页
                if (currentItem == mNewsCenterTabBean.data.topnews.size() - 1) {
                    currentItem = 0;
                } else {
                    currentItem++;
                }
                mVpSwitchImage.setCurrentItem(currentItem);
            }

            mHandler.postDelayed(this, 3000);
        }
    }

    @Override
    public void onRefresh() {
        final String url = Constant.HOST + mNewsCenterTabBean.data.more;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToast.show(mContext, "联网获取数据失败");

                        //读取缓存
                        try {
                            String json = CacheUtils.readCache(mContext, url);
                            if(!TextUtils.isEmpty(json)){
                                processMoreData(json);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                        //隐藏头
                        mRvNews.hideHeaderView(false);

                        //由于上层RefreshRecyclerView中嵌套了轮播图SwitchImageViewViewPager，
//                        当RefreshRecyclerView下拉刷新完成以后，
//                        此时，下面的轮播图SwitchImageViewViewPager 并没有获得抬起的事件。
//                        因此轮播图一致处于停止状态。

                        //手动开启轮播图切换
                        if (!hasSwitch) {
                            startSwitch();
                        }

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        processMoreData(response);

                        //缓存数据
                        try {
                            CacheUtils.saveCache(mContext,url,response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //隐藏头
                        //mRvNews.hideHeaderView(true);

                        //postDelayed运行在主线程,可以不使用延时操作,因为本身获取数据的时候会耗时
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRvNews.hideHeaderView(true);
                            }
                        }, 2000);

                        //手动开启轮播图切换
                        if (!hasSwitch) {
                            startSwitch();
                        }
                    }
                });
    }

    private void processMoreData(String response) {
        Gson gson = new Gson();
        NewsCenterTabBean tabBean = gson.fromJson(response, NewsCenterTabBean.class);
        mNewsCenterTabBean.data.news.addAll(0, tabBean.data.news);
    }

    @Override
    public void onLoadMore() {
        final String url = Constant.HOST + mNewsCenterTabBean.data.more;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToast.show(mContext, "联网获取数据失败");

                        //读取缓存
                        try {
                            String json = CacheUtils.readCache(mContext, url);
                            if(!TextUtils.isEmpty(json)){
                                processLoadMoreData(json);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                        //隐藏脚
                        mRvNews.hideFooterView();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        processLoadMoreData(response);

                        //缓存数据
                        try {
                            CacheUtils.saveCache(mContext,url,response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //隐藏脚
                        //mRvNews.hideFooterView();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRvNews.hideFooterView();
                            }
                        }, 2000);
                    }
                });
    }

    private void processLoadMoreData(String response) {
        Gson gson = new Gson();
        NewsCenterTabBean tabBean = gson.fromJson(response, NewsCenterTabBean.class);
        mNewsCenterTabBean.data.news.addAll(tabBean.data.news);
    }
}
