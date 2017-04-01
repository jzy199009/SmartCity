package cn.jzy.smartcity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzy.smartcity.R;
import cn.jzy.smartcity.adapter.GuideVPAdapter;
import cn.jzy.smartcity.utils.Constant;
import cn.jzy.smartcity.utils.MyLogger;
import cn.jzy.smartcity.utils.SPUtils;

/**
 * Created by Administrator on 2017/3/29.
 */
public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {

    private static final String TAG = "GuideActivity";
    @BindView(R.id.vp)
    ViewPager mVp;
    @BindView(R.id.bt_start)
    Button mBtStart;
    @BindView(R.id.container_gray_point)
    LinearLayout mContainerGrayPoint;
    @BindView(R.id.red_point)
    View mRedPoint;

    //向导图片
    private int[] imgs = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    private List<ImageView> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去除标题,必须放在setContentView前面
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);


        initViewPager();

        initGrayPoint();
    }

    //动态的创建灰色的点
    private void initGrayPoint() {
        for (int resId : imgs) {
            View view = new View(this);
            view.setBackgroundResource(R.drawable.point_gray_bg);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
            params.rightMargin = 20;//设置右边距
            mContainerGrayPoint.addView(view, params);
        }
    }

    //初始化ViewPager的数据
    private void initViewPager() {

        views = new ArrayList<>();
        for (int resId : imgs) {
            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(resId);
            views.add(iv);
        }

        mVp.setAdapter(new GuideVPAdapter(views));

        //设置页面的滑动监听
        mVp.addOnPageChangeListener(this);
    }

    //小灰点之间的宽度
    private int width;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //position:当前滑动页面的下标    positionOffset：页面的滑动比率
        //positionOffsetPixels：页面滑动的实际像素

        //MyLogger.i(TAG,"position:"+position+",positionOffset:"+positionOffset+",positionOffsetPixels:"+positionOffsetPixels);

        if (width == 0) {
            width = mContainerGrayPoint.getChildAt(1).getLeft() - mContainerGrayPoint.getChildAt(0).getLeft();
            MyLogger.i(TAG, "width:" + width);
        }

        //修改小红点与相对布局的左边距
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRedPoint.getLayoutParams();
        params.leftMargin = (int) (position * width + positionOffset * width);
        mRedPoint.setLayoutParams(params);
    }

    //页面选中的时候调用
    @Override
    public void onPageSelected(int position) {
        if (position == views.size() - 1) {
            mBtStart.setVisibility(View.VISIBLE);
        } else {
            mBtStart.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick(R.id.bt_start)
    public void onClick() {
        //进入主界面
        SPUtils.saveBoolean(GuideActivity.this, Constant.KEY_HAS_GUIDE, true);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}
