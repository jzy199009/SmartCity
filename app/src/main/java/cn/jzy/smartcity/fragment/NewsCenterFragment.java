package cn.jzy.smartcity.fragment;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.viewpagerindicator.TabPageIndicator;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import cn.jzy.smartcity.R;
import cn.jzy.smartcity.activity.MainActivity;
import cn.jzy.smartcity.adapter.NewsCenterTabVPAdapter;
import cn.jzy.smartcity.base.BaseFragment;
import cn.jzy.smartcity.base.BaseLoadNetDataOperator;
import cn.jzy.smartcity.base.NewsCenterContentTabPager;
import cn.jzy.smartcity.bean.NewsCenterBean;
import cn.jzy.smartcity.utils.Constant;
import cn.jzy.smartcity.utils.MyLogger;
import cn.jzy.smartcity.utils.MyToast;
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

    @Override
    public void initTitle() {
        setIbMenuDisplayState(true);
        setIbPicTypeDisplayState(true);
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
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        MyLogger.i(TAG, response);
                        //把response  == json 转换成对应的数据模型
                        processData(response);
                    }
                });

    }

    //把Json格式的字符串转换成对应的模型对象
    private void processData(String json) {
        Gson gson = new Gson();
        mNewsCenterBean = gson.fromJson(json, NewsCenterBean.class);

        //把数据传递给MainActivity
        ((MainActivity) getActivity()).setNewsCenterMenuBeenList(mNewsCenterBean.data);

        //创建布局
        View view = createContent();
        //加载布局
        addView(view);
    }
}
