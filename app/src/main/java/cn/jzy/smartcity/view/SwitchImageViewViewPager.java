package cn.jzy.smartcity.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import cn.jzy.smartcity.base.NewsCenterContentTabPager;
import cn.jzy.smartcity.utils.MyLogger;
import cn.jzy.smartcity.utils.MyToast;

/**
 * Created by Administrator on 2017/4/2.
 */
public class SwitchImageViewViewPager extends ViewPager {

    private static final String TAG = "SwitchImageViewViewPager";
    //控制轮播图开始和停止的对象
    private NewsCenterContentTabPager tabPager;

    //在哪里调用？ 在加载数据成功之后，就可以把该对象设置过来
    public void setTabPager(NewsCenterContentTabPager tabPager) {
        this.tabPager = tabPager;
    }

    public SwitchImageViewViewPager(Context context) {
        super(context);
    }

    public SwitchImageViewViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int startX;
    private int startY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                MyLogger.i(TAG, "按下");
                //停止轮播图的切换
                tabPager.stopSwitch();

                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                MyLogger.i(TAG, "移动");
                //停止轮播图的切换
                tabPager.stopSwitch();

                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                //移动距离
                int disX = moveX - startX;
                int disY = moveY - startY;

                //处理的是水平的滑动   并且是向右的
                if (Math.abs(disX) > Math.abs(disY) && disX > 0) {
                    //请求外层控件不要拦截事件
                    requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                MyLogger.i(TAG, "抬起");

                //处理点击事件
                //处理点击事件
                int upX = (int) ev.getX();
                int upY = (int) ev.getY();
                if(startX == upX && startY == upY){
                    //点击事件
                    MyToast.show(getContext(),"点击事件");
                }
                //开始切换
                tabPager.startSwitch();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
