package cn.jzy.smartcity.base;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzy.smartcity.R;

/**
 * Created by Administrator on 2017/4/1.
 * 新闻中心的内容tab页面
 */
public class NewsCenterContentTabPager {
    @BindView(R.id.vp_switch_image)
    ViewPager mVpSwitchImage;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ll_point_container)
    LinearLayout mLlPointContainer;

    private Context mContext;
    public View mView;

    public NewsCenterContentTabPager(Context context) {
        mContext = context;
        mView = initView();
    }

    //加载布局
    private View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.newscenter_content_tab, null);
        ButterKnife.bind(view);
        return view;
    }
}
