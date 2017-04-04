package cn.jzy.smartcity.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cn.jzy.smartcity.bean.NewsCenterTabBean;

/**
 * Created by Administrator on 2017/4/1.
 */
public class SwitchImageVPAdapter extends PagerAdapter {

    private List<ImageView> imageViews;
    private List<NewsCenterTabBean.TopNewsBean> topnews;

    public SwitchImageVPAdapter(List<ImageView> imageViews, List<NewsCenterTabBean.TopNewsBean> topnews) {
        this.imageViews = imageViews;
        this.topnews = topnews;
    }

    @Override
    public int getCount() {
        return imageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = imageViews.get(position);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
