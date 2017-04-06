package cn.jzy.smartcity.fragment;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.viewpagerindicator.TabPageIndicator;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import cn.jzy.smartcity.R;
import cn.jzy.smartcity.activity.MainActivity;
import cn.jzy.smartcity.adapter.NewsCenterGroupImageViewAdapter;
import cn.jzy.smartcity.adapter.NewsCenterTabVPAdapter;
import cn.jzy.smartcity.base.BaseFragment;
import cn.jzy.smartcity.base.BaseLoadNetDataOperator;
import cn.jzy.smartcity.base.NewsCenterContentTabPager;
import cn.jzy.smartcity.bean.NewsCenterBean;
import cn.jzy.smartcity.bean.NewsCenterGroupImageViewBean;
import cn.jzy.smartcity.utils.CacheUtils;
import cn.jzy.smartcity.utils.Constant;
import cn.jzy.smartcity.utils.MyLogger;
import cn.jzy.smartcity.utils.MyToast;
import cn.jzy.smartcity.view.RecycleViewDivider;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/3/30.
 */
public class NewsCenterFragment extends BaseFragment implements BaseLoadNetDataOperator {
    private static final String TAG = "NewsCenterFragment";

    private TabPageIndicator tabPageIndicator;
    private ImageButton ibNext;
    private ViewPager vpNewsCenterContent;
    private NewsCenterBean mNewsCenterBean;
    private List<NewsCenterContentTabPager> mViews;
    private RecyclerView mRvGroupImageView;
    private LinearLayoutManager mLlm;
    private GridLayoutManager mGlm;

    @Override
    public void initTitle() {
        setIbMenuDisplayState(true);
        setIbPicTypeDisplayState(false);
        setTitle("新闻");
    }

    @Override
    public View createContent() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.newscenter_content, (ViewGroup) getView(), false);

        //初始化TabPageIndicator
        tabPageIndicator = (TabPageIndicator) view.findViewById(R.id.tabPagerIndicator);
        ibNext = (ImageButton) view.findViewById(R.id.ib_next);
        vpNewsCenterContent = (ViewPager) view.findViewById(R.id.vp_newscenter_content);

        //点击箭头，切换到下一页
        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取ViewPager当前显示页面的下标
                int currentItem = vpNewsCenterContent.getCurrentItem();

                if (currentItem != mNewsCenterBean.data.get(0).children.size() - 1) {
                    vpNewsCenterContent.setCurrentItem(currentItem + 1);
                }
            }
        });

        //初始化ViewPager
        initViewPager();
        return view;
    }

    private void initViewPager() {
        //保存ViewPager显示每个条目的集合
        mViews = new ArrayList<>();
        //获取每个item的数据(顶部导航tab)
        for (NewsCenterBean.NewsCenterNewsTabBean tabBean :
                mNewsCenterBean.data.get(0).children) {
            //创建view并添加到集合中
            NewsCenterContentTabPager tabPager = new NewsCenterContentTabPager(getContext());
            mViews.add(tabPager);
        }

        //创建适配器
        NewsCenterTabVPAdapter adapter = new NewsCenterTabVPAdapter(mViews, mNewsCenterBean.data.get(0).children);

        //设置适配器
        vpNewsCenterContent.setAdapter(adapter);

        //让TabPagerIndicator和ViewPager进行联合
        tabPageIndicator.setViewPager(vpNewsCenterContent);


        //让新闻中心第一个子tab的轮播图开始切换
        mViews.get(0).startSwitch();

        //给ViewPager设置页面切换监听
        //注意：ViewPager和TabPagerIndicator配合使用，监听只能设置给TabPagerIndicator
        tabPageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //当前的开始切换，其他的tab停止切换
                for (int i = 0; i < mViews.size(); i++) {
                    if (i == position) {
                        //选中页
                        mViews.get(i).startSwitch();
                    } else {
                        //未选中页
                        mViews.get(i).stopSwitch();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    //使用okHttpUtil第三方开源框架
    @Override
    public void loadNetData() {
        final String url = Constant.NEWSCENTER_URL;
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToast.show(getActivity(), "获取新闻中心数据失败");

                        //读取缓存
                        try {
                            String json = CacheUtils.readCache(getContext(), url);
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
                        //把response  == json 转换成对应的数据模型
                        processData(response);

                        //缓存数据
                        try {
                            CacheUtils.saveCache(getContext(),url,response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    //把Json格式的字符串转换成对应的模型对象
    private void processData(String json) {
        hasLoadData = true;//已经加载数据

        Gson gson = new Gson();
        mNewsCenterBean = gson.fromJson(json, NewsCenterBean.class);

        //把数据传递给MainActivity
        ((MainActivity) getActivity()).setNewsCenterMenuBeenList(mNewsCenterBean.data);

        //创建布局
        View view = createContent();
        //加载布局
        addView(view);

        //把布局添加到缓存的容器中
        cacheViews.put(0, view);
    }

    private Map<Integer, View> cacheViews = new HashMap<>();

    //切换内容
    public void switchContent(int position) {
        //标题右边的切换图标
        if (position == 2) {
            //显示
            mIbPicType.setVisibility(View.VISIBLE);
        } else {
            //隐藏
            mIbPicType.setVisibility(View.GONE);
        }

        //先从缓存的容器里面去获取
        View view = cacheViews.get(position);

        //缓存中没有，创建新的布局，缓存起来
        if (view == null) {
            //创建里面的内容
            mContainer.removeAllViews();

            if (position == 2) {
                //组图
                //初始化组图布局
                view = createGroupImageViewLayout();
                //将组图布局添加到新闻中心页中的FramenLayout容器中
                addView(view);
                //放入缓存中，如果缓存中有数据，直接取出
                cacheViews.put(position, view);
                //加载组图数据
                loadGroupImageViewData(position);
            }
        } else if (view != null) {
            //添加布局
            addView(view);
        }

    }


    //加载组图的布局
    private View createGroupImageViewLayout() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.newscenter_group_imageview, (ViewGroup) getActivity()
                .getWindow().getDecorView(), false);
        mRvGroupImageView = (RecyclerView) view.findViewById(R.id.rv_group_imageview);

        //有的时候是列表，有的时候是网格
        //线性布局管理器
        mLlm = new LinearLayoutManager(getContext());
        //网格布局管理器
        mGlm = new GridLayoutManager(getContext(), 2);

        mRvGroupImageView.setLayoutManager(mLlm);
        //添加分割线
        //mRvGroupImageView.addItemDecoration(new RecycleViewDivider(getContext(),LinearLayoutManager.HORIZONTAL,1, Color.BLUE));

        return view;
    }

    //加载组图数据
    private void loadGroupImageViewData(int position) {
        //获取路径
        final String url = Constant.HOST + mNewsCenterBean.data.get(position).url;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        MyToast.show(getContext(), "获取组图失败");

                        //读取缓存
                        try {
                            String json = CacheUtils.readCache(getContext(), url);
                            if(!TextUtils.isEmpty(json)){
                                processGroupImageViewData(json);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyLogger.i(TAG, response);
                        System.out.println(response);

                        processGroupImageViewData(response);

                        //缓存数据
                        try {
                            CacheUtils.saveCache(getContext(),url,response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //处理组图数据
    private void processGroupImageViewData(String response) {
        Gson gson = new Gson();
        //转化为数据模型
        NewsCenterGroupImageViewBean newsCenterGroupImageViewBean = gson.fromJson(response,
                NewsCenterGroupImageViewBean.class);

        //绑定适配器给rvGroupImageView
        mRvGroupImageView.setAdapter(new NewsCenterGroupImageViewAdapter(newsCenterGroupImageViewBean.data.news, getContext()));
    }


    //组图的显示状态
    private int groupImageViewState = LIST_STATE;
    private final static int LIST_STATE = 0;
    private final static int GRID_STATE = 1;


    @OnClick(R.id.ib_pic_type)
    public void switchListGridState(View view) {
        MyToast.show(getContext(),"切换");
        //如果是列表，切换至网格
        if (groupImageViewState == LIST_STATE) {
            groupImageViewState = GRID_STATE;
            mIbPicType.setBackgroundResource(R.drawable.icon_pic_list_type);
            mRvGroupImageView.setLayoutManager(mGlm);
            //添加垂直方向的分割线
            mRvGroupImageView.addItemDecoration(new RecycleViewDivider(getContext(), GridLayoutManager.VERTICAL, 1, Color.CYAN));
        } else {//如果是网络，切换至列表
            groupImageViewState = LIST_STATE;
            mIbPicType.setBackgroundResource(R.drawable.icon_pic_grid_type);
            mRvGroupImageView.setLayoutManager(mLlm);
            //添加水平方向的分割线
            //mRvGroupImageView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 1, Color.BLACK));
        }
    }
}
