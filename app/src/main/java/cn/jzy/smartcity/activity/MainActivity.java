package cn.jzy.smartcity.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzy.smartcity.R;
import cn.jzy.smartcity.adapter.MainVPFragmentAdapter;
import cn.jzy.smartcity.adapter.MenuAdapter;
import cn.jzy.smartcity.base.BaseFragment;
import cn.jzy.smartcity.base.BaseLoadNetDataOperator;
import cn.jzy.smartcity.bean.NewsCenterBean;
import cn.jzy.smartcity.fragment.GovaffairsFragment;
import cn.jzy.smartcity.fragment.HomeFragment;
import cn.jzy.smartcity.fragment.NewsCenterFragment;
import cn.jzy.smartcity.fragment.SettingFragment;
import cn.jzy.smartcity.fragment.SmartServiceFragment;
import cn.jzy.smartcity.view.NoScrollViewPager;

/**
 * Created by Administrator on 2017/3/29.
 */
public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.rg_tab)
    RadioGroup mRgTab;
    @BindView(R.id.tab_vp)
    NoScrollViewPager mTabVp;
    public SlidingMenu mSlidingMenu;
    private List<Fragment> mFragments;

    List<NewsCenterBean.NewsCenterMenuBean> mNewsCenterMenuBeenList;
    private MenuAdapter mMenuAdapter;

    //设置侧滑菜单的数据
    public void setNewsCenterMenuBeenList(List<NewsCenterBean.NewsCenterMenuBean> newsCenterMenuBeenList) {
        mNewsCenterMenuBeenList = newsCenterMenuBeenList;

        mMenuAdapter.setNewsCenterMenuBeenList(newsCenterMenuBeenList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViewPager();
        //设置RaidoGroup的选择改变监听
        mRgTab.setOnCheckedChangeListener(this);

        initSlidingMenu();

        initRecyclerView();
    }

    //初始化Menu的RecyclerView
    private void initRecyclerView() {
        //从SlidingMenu里面找到RecyclerView
        RecyclerView rvMenu = (RecyclerView) mSlidingMenu.findViewById(R.id.rv_menu);

        //设置布局管理器
        rvMenu.setLayoutManager(new LinearLayoutManager(this));//默认是垂直方向

        //创建适配器给RecyclerView进行数据绑定
        //默认为空
        mMenuAdapter = new MenuAdapter(this, null);
        rvMenu.setAdapter(mMenuAdapter);

    }

    //初始化侧滑菜单
    //我们这里借助了第三方开源框：SlidingMenu
    private void initSlidingMenu() {
        //创建侧滑菜单对象
        mSlidingMenu = new SlidingMenu(this);
        //设置菜单从左边滑出
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        //设置侧滑菜单，默认不可以滑出
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        //设置侧滑菜单滑出后，主界面的宽度
        mSlidingMenu.setBehindOffset(250);

        //设置侧滑菜单的宽度
        //slidingmenu.setBehindWidth(250);

        //以内容的形式添加到Activity
        mSlidingMenu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);
        //设置侧滑菜单的布局
        mSlidingMenu.setMenu(R.layout.activity_main_menu);

    }

    private void initViewPager() {
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new NewsCenterFragment());
        mFragments.add(new SmartServiceFragment());
        mFragments.add(new GovaffairsFragment());
        mFragments.add(new SettingFragment());

        mTabVp.setAdapter(new MainVPFragmentAdapter(getSupportFragmentManager(),mFragments));

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        int item = 0;
        switch(checkedId) {
            case R.id.rb_home:
                item = 0;
                mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);//无法滑出侧滑菜单
                break;
            case R.id.rb_newscener:
                item = 1;
                mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                break;
            case R.id.rb_smartservice:
                item = 2;
                mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                break;
            case R.id.rb_govaffairs:
                item = 3;
                mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                break;
            case R.id.rb_setting:
                item = 4;
                mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                break;
            default:
                break;
        }

        //让上面的ViewPager切换到对应的页面
        mTabVp.setCurrentItem(item,false);


        //加载网络数据的入口,这一步很重要,没有这一步侧滑菜单获取不到内容,神奇的转换,好好体会
        BaseFragment baseFragment = (BaseFragment) mFragments.get(item);//强制类型提升
        if (baseFragment instanceof BaseLoadNetDataOperator) {
            ((BaseLoadNetDataOperator) baseFragment).loadNetData();
        }
    }

    //获取当前选中的TabFragment
    public BaseFragment getCurrentTabFragment(){
        int currentItem = mTabVp.getCurrentItem();
        BaseFragment baseFragment = (BaseFragment) mFragments.get(currentItem);
        return baseFragment;
    }
}