package cn.jzy.smartcity.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzy.smartcity.R;
import cn.jzy.smartcity.adapter.XWrapAdapter;

/**
 * Created by Administrator on 2017/4/3.
 * 上拉下拉加载数据的RecyclerView
 */
public class RefreshRecyclerView extends RecyclerView {
    @BindView(R.id.iv_arrow)
    ImageView mIvArrow;
    @BindView(R.id.pb)
    ProgressBar mPb;
    @BindView(R.id.tv_state)
    TextView mTvState;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.default_header)
    LinearLayout mDefaultHeader;
    private ViewGroup mHeaderView;
    private View mFooterView;
    private int mHeaderMeasuredHeight;
    private int mFooterMeasuredHeight;
    private LinearLayoutManager lm;
    private int disY;
    //记录按下的位置
    private int downY;

    //头的状态
    private int mHeaderState = DOWN_REFRESH_STATE;
    //下拉刷新
    private final static int DOWN_REFRESH_STATE = 0;
    //释放刷新
    private final static int RELEASE_REFRESH_STATE = 1;
    //正在加载
    private final static int REFRESHING_STATE = 2;
    private Animation animtion1;
    private Animation animtion2;


    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //初始化
    private void init() {
        initHeaderView();
        initFooterView();
        initAnimation();
    }

    //初始化头
    private void initHeaderView() {
        mHeaderView = (ViewGroup) inflate(getContext(), R.layout.header, null);
        ButterKnife.bind(this,mHeaderView);

        //隐藏进度条
        mPb.setVisibility(View.INVISIBLE);

        //测量默认头的高度
        mDefaultHeader.measure(0,0);
        //获取测量后的高度
        mHeaderMeasuredHeight = mDefaultHeader.getMeasuredHeight();
        System.out.println("mHeaderMeasuredHeight = " + mHeaderMeasuredHeight);
        //隐藏头
        mDefaultHeader.setPadding(0,-mHeaderMeasuredHeight,0,0);
    }

    //初始化脚
    private void initFooterView() {
        mFooterView = inflate(getContext(), R.layout.footer, null);

        //测量
        mFooterView.measure(0,0);
        mFooterMeasuredHeight = mFooterView.getMeasuredHeight();
        System.out.println("mFooterMeasuredHeight = " + mFooterMeasuredHeight);
        //隐藏
        mFooterView.setPadding(0,-mFooterMeasuredHeight,0,0);
    }

    //初始化动画
    private void initAnimation() {
        animtion1 = createAnimation1();
        animtion2 = createAnimation2();
    }

    //从下拉刷新切换到释放刷新的动画
    private Animation createAnimation1() {
        Animation animation = new RotateAnimation(0,-180,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        return animation;
    }

    //从释放刷新切换到下拉刷新的动画
    private Animation createAnimation2() {
        Animation animation = new RotateAnimation(-180,-360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        return animation;
    }

    //重写setAdpater方法，将头布局和脚布局添加到RecycleView上
    @Override
    public void setAdapter(Adapter adapter) {
        adapter = new XWrapAdapter(mHeaderView, mFooterView, adapter);
        super.setAdapter(adapter);
    }

    //添加轮播图的方法
    public void addSwitchImageView(View view) {
        mHeaderView.addView(view);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        lm = (LinearLayoutManager) layout;
    }

    //分发事件
//原因：没有使用onTouchEvent()是因为dispatchTouchEvent()方法回调的频率高一些
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN://按下
                //MyLogger.i(TAG,"按下");
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE://移动
                //MyLogger.i(TAG,"移动");
                int moveY = (int) ev.getY();

                //条件 RecyclerView的第一个条目的下标是0 && 往下拽的行为
                disY = moveY - downY;
                int firstVisibleItemPosition = lm.findFirstVisibleItemPosition();
                int top = -mHeaderMeasuredHeight + disY;
                if(firstVisibleItemPosition == 0 && disY > 0){
                    //头布局为下拉刷新并且top>=0（头布局完全显示出来）
                    //更改头布局状为：释放刷新状态
                    if(mHeaderState == DOWN_REFRESH_STATE && top >= 0){
                        //由下拉刷新变为释放刷新
                        mHeaderState = RELEASE_REFRESH_STATE;
                        mTvState.setText("释放刷新");
                        //执行动画
                        mIvArrow.startAnimation(animtion1);
                        //头布局为释放刷新并且top < 0（头布局开始隐藏）
                        //更改头布局状为：下拉刷新状态
                    }else if(mHeaderState == RELEASE_REFRESH_STATE && top < 0){
                        mHeaderState = DOWN_REFRESH_STATE;
                        mTvState.setText("下拉刷新");
                        //执行动画
                        mIvArrow.startAnimation(animtion2);
                    }

                    //执行头的显示和隐藏操作
                    mDefaultHeader.setPadding(0,top,0,0);

                    return true;// 自己处理用户触摸滑动的事件.结束方法否则手指滑动的过程中会产生误差
                }
                break;
            case MotionEvent.ACTION_UP://弹起
            case MotionEvent.ACTION_CANCEL://事件取消
            case MotionEvent.ACTION_OUTSIDE://外部点击
                //MyLogger.i(TAG,"弹起");
                int mFirstVisibleItemPosition = lm.findFirstVisibleItemPosition();
                if(mFirstVisibleItemPosition == 0 && disY > 0){
                    if(mHeaderState == DOWN_REFRESH_STATE){
                        //隐藏头
                        mDefaultHeader.setPadding(0,-mHeaderMeasuredHeight,0,0);
                    }else if(mHeaderState == RELEASE_REFRESH_STATE){
                        //把状态切换为正在加载
                        //把头缩回置本身头的高度
                        //隐藏箭头 显示进度条
                        mHeaderState = REFRESHING_STATE;
                        mTvState.setText("正在加载");
                        mDefaultHeader.setPadding(0,0,0,0);
                        //清除动画后控件才可以隐藏
                        mIvArrow.clearAnimation();
                        mIvArrow.setVisibility(View.INVISIBLE);
                        mPb.setVisibility(View.VISIBLE);
                        //加载最新的数据
                        if (mOnRefreshListener!=null){
                            mOnRefreshListener.onRefresh();
                        }
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    //加载最新数据的接口
    public interface OnRefreshListener{
        void onRefresh();
    }

    private OnRefreshListener mOnRefreshListener;

    public void setOnRefreshListener(OnRefreshListener mOnRefreshListener){
        this.mOnRefreshListener = mOnRefreshListener;
    }

    //隐藏头
    public void hideHeaderView(boolean loadState){
        //隐藏进度条，显示箭头，修改状态，修改文字内容，通过数据加载成功的状态去判断是否更改上次加载数据的实现
        mPb.setVisibility(View.INVISIBLE);
        mIvArrow.setVisibility(View.VISIBLE);
        mHeaderState = DOWN_REFRESH_STATE;
        mTvState.setText("下拉刷新");
        mDefaultHeader.setPadding(0,-mHeaderMeasuredHeight,0,0);

        if(loadState){
            String dateStr = DateFormat.getDateFormat(getContext()).format(System.currentTimeMillis());
            String timeStr = DateFormat.getTimeFormat(getContext()).format(System.currentTimeMillis());
            mTvTime.setText(dateStr+" " + timeStr);
        }
        //刷新数据
        getAdapter().notifyDataSetChanged();
    }


//    //加载更多的接口
//    public interface OnLoadMoreListener{
//        void onLoadMore();
//    }
//
//    private OnLoadMoreListener mOnLoadMoreListener;
//
//    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener){
//        this.mOnLoadMoreListener = mOnLoadMoreListener;
//    }

}
