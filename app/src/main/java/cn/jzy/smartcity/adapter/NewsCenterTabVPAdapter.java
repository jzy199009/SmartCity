package cn.jzy.smartcity.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.jzy.smartcity.base.NewsCenterContentTabPager;
import cn.jzy.smartcity.bean.NewsCenterBean;

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
