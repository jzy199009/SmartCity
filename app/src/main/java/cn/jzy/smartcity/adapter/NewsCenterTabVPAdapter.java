package cn.jzy.smartcity.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.jzy.smartcity.base.NewsCenterContentTabPager;
import cn.jzy.smartcity.bean.NewsCenterBean;
import cn.jzy.smartcity.utils.Constant;

/**
 * Created by Administrator on 2017/4/1.
 */
public class NewsCenterTabVPAdapter extends PagerAdapter {
    private List<NewsCenterContentTabPager> views;
    private List<NewsCenterBean.NewsCenterNewsTabBean> children;

    public NewsCenterTabVPAdapter(List<NewsCenterContentTabPager> views, List<NewsCenterBean.NewsCenterNewsTabBean> children) {
        this.views = views;
        this.children = children;
    }

    @Override
    public int getCount() {
        return views!=null?views.size():0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position).mView;
        container.addView(view);
        
        //当新闻中心页的Viewpager切换页面时，进行加载网络数据,即加载Viewpager下面的新闻列表
        NewsCenterContentTabPager tabPager = views.get(position);
        // /10007/list_1.json 需要在前面进行路径的拼接
        String url = Constant.HOST + children.get(position).url;
        tabPager.loadNetData(url);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return children.get(position).title;
    }
}
