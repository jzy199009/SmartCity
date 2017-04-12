package cn.jzy.videoplayerlist.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzy.videoplayerlist.R;
import cn.jzy.videoplayerlist.adapter.VideoPlayListAdatper;
import cn.jzy.videoplayerlist.utils.MediaHelper;

/**
 * Created by Administrator on 2017/4/9.
 * 对应视频播放控制界面的封装
 */
public class MyVideoMediaController extends RelativeLayout {
    private static final int MSG_HIDE_TITLE = 0;
    private static final int MSG_UPDATE_TIME_PROGRESS = 1;
    private static final int MSG_HIDE_CONTROLLER = 2;
    private static final String TAG = "MyVideoMediaController";

    @BindView(R.id.pb_loading)
    ProgressBar mPbLoading;
    @BindView(R.id.iv_replay)
    ImageView mIvReplay;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.rl_play_finish)
    RelativeLayout mRlPlayFinish;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_play)
    ImageView mIvPlay;
    @BindView(R.id.tv_all_time)
    TextView mTvAllTime;
    @BindView(R.id.tv_use_time)
    TextView mTvUseTime;
    @BindView(R.id.seekBar)
    SeekBar mSeekBar;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.iv_fullscreen)
    ImageView mIvFullscreen;
    @BindView(R.id.ll_play_control)
    LinearLayout mLlPlayControl;

    private boolean hasPause;//是否暂停

    //消息处理器
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_HIDE_TITLE:
                    mTvTitle.setVisibility(View.GONE);
                    break;
                case MSG_UPDATE_TIME_PROGRESS:
                    updatePlayTimeAndProgress();
                    break;
                case MSG_HIDE_CONTROLLER:
                    showOrHideVideoController();
                    break;
                default:
                    break;
            }
        }
    };
    private boolean isAnim;

    public void delayHideTitle() {
        //移除消息
        handler.removeMessages(MSG_HIDE_TITLE);
        //发送一个空的延时2秒消息
        handler.sendEmptyMessageDelayed(MSG_HIDE_TITLE, 2000);
    }


    public MyVideoMediaController(Context context) {
        this(context, null);
    }

    public MyVideoMediaController(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyVideoMediaController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    //初始化控件
    public void initView() {
        View view = View.inflate(getContext(), R.layout.video_controller, this);
        ButterKnife.bind(this, view);

        initViewDisplay();

        //监听视频播放时的点击界面
        setOnTouchListener(onTouchListener);
        //设置SeekBar的拖动监听
        mSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

        //播放完成的界面要拦截之前的触摸事件
        mRlPlayFinish.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        //拖动的过程中调用
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                //把视频跳转到对应的位置
                //获取拖动进度
                //int progress = seekBar.getProgress();
                //获取视频总时长
                int duration = MediaHelper.getInstance().getDuration();
                //根据当前进度百分比，计算出视频播放位置
                int position = (int) (duration * progress * 1f / 100f + 0.5f);
                MediaHelper.getInstance().seekTo(position);
            }
        }

        //开始拖动的时候调用
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            handler.removeMessages(MSG_HIDE_CONTROLLER);

            //暂停视频的播放、停止时间和进度条的更新
            MediaHelper.pause();
            handler.removeMessages(MSG_UPDATE_TIME_PROGRESS);
        }

        //停止拖动时调用
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //把视频跳转到对应的位置
            //获取拖动进度
            int progress = seekBar.getProgress();
            //获取视频总时长
            int duration = MediaHelper.getInstance().getDuration();
            //根据当前进度百分比，计算出视频播放位置
            int position = (int) (duration * progress * 1f / 100f + 0.5f);
            MediaHelper.getInstance().seekTo(position);
            //开始播放、开始时间和进度条的更新
            MediaHelper.play();
            updatePlayTimeAndProgress();
            if (hasPause) {
                mIvPlay.setImageResource(R.drawable.new_pause_video);
            }

            handler.sendEmptyMessageDelayed(MSG_HIDE_CONTROLLER,2000);
        }
    };

    private OnTouchListener onTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //按下+已经播放了
            if (event.getAction() == MotionEvent.ACTION_DOWN && myVideoPlayer.hasPlay) {
                //显示或者隐藏视频控制界面
                showOrHideVideoController();
            }
            return true;//去处理事件
        }
    };

    private void showOrHideVideoController() {
        if (mLlPlayControl.getVisibility() == View.GONE) {
            //显示（标题、播放按钮、视频进度控制）
            mTvTitle.setVisibility(View.VISIBLE);
            mIvPlay.setVisibility(View.VISIBLE);
            //加载动画
            if (!isAnim) {
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_enter);
                animation.setAnimationListener(new SimpleAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        super.onAnimationEnd(animation);
                        mLlPlayControl.setVisibility(View.VISIBLE);
                        //过2秒后自动隐藏
                        handler.sendEmptyMessageDelayed(MSG_HIDE_CONTROLLER,2000);
                    }
                });
                //执行动画
                mLlPlayControl.startAnimation(animation);
            }
        } else {
            //隐藏（标题、播放按钮、视频进度控制）
            mTvTitle.setVisibility(View.GONE);
            mIvPlay.setVisibility(View.GONE);
            //加载动画
            if (!isAnim) {
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_exit);
                animation.setAnimationListener(new SimpleAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        super.onAnimationEnd(animation);
                        mLlPlayControl.setVisibility(View.GONE);
                    }
                });
                //执行动画
                mLlPlayControl.startAnimation(animation);
            }
        }
    }

    //更新进度条的第二进度（缓存）
    public void updateSeekBarSecondProgress(int percent) {
        mSeekBar.setSecondaryProgress(percent);
    }

    //设置播放视频的总时长
    public void setDuration(int duration) {
        String time = formatDuration(duration);
        mTvTime.setText(time);
        mTvUseTime.setText("00:00");
    }

    //格式化时间 00：00
    public String formatDuration(int duration) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        return dateFormat.format(new Date(duration));
    }

    //更新播放的时间和进度
    public void updatePlayTimeAndProgress() {
        //获取目前播放的进度
        int currentPosition = MediaHelper.getInstance().getCurrentPosition();
        //格式化
        String useTime = formatDuration(currentPosition);
        mTvUseTime.setText(useTime);
        //更新进度
        int duration = MediaHelper.getInstance().getDuration();
        if (duration == 0) {
            return;
        }
        int progress = 100 * currentPosition / duration;
        mSeekBar.setProgress(progress);

        handler.sendEmptyMessageDelayed(MSG_UPDATE_TIME_PROGRESS, 500);
    }

    //显示视频播放完成的界面
    public void showPlayFinishView() {
        mTvTitle.setVisibility(View.VISIBLE);
        mRlPlayFinish.setVisibility(View.VISIBLE);
        mTvAllTime.setVisibility(View.VISIBLE);
    }

    private int position;
    public void setPosition(int position) {
        this.position = position;
    }

    private VideoPlayListAdatper adapter;
    public void setAdapter(VideoPlayListAdatper videoPlayListAdatper) {
        this.adapter = videoPlayListAdatper;
    }

    //简单的动画监听器（不需要其他的监听器去实现多余的方法）
    private class SimpleAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            isAnim = true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            isAnim = false;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    //初始化控件的显示状态
    public void initViewDisplay() {


        mTvTitle.setVisibility(View.VISIBLE);
        mIvPlay.setVisibility(View.VISIBLE);
        mIvPlay.setImageResource(R.drawable.new_play_video);
        mTvAllTime.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.GONE);
        mLlPlayControl.setVisibility(View.GONE);
        mRlPlayFinish.setVisibility(View.GONE);
        mTvUseTime.setText("00:00");
        mSeekBar.setProgress(0);
        mSeekBar.setSecondaryProgress(0);
    }


    @OnClick({R.id.iv_replay, R.id.iv_share, R.id.iv_play, R.id.iv_fullscreen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_replay:
                //隐藏播放完成界面
                mRlPlayFinish.setVisibility(View.GONE);
                //隐藏时间
                mTvAllTime.setVisibility(View.GONE);
                mTvUseTime.setText("00:00");
                //进度条
                mSeekBar.setProgress(0);
                //把媒体播放器的位置移动到开始的位置
                MediaHelper.getInstance().seekTo(0);
                //开始播放
                MediaHelper.play();
                //延时隐藏标题
                delayHideTitle();
                break;
            case R.id.iv_share:
                break;
            case R.id.iv_play:
                //点击一个新的条目进行播放
                //点击的条目下标是否是之前播放的条目下标
                if(position != adapter.currentPosition && adapter.currentPosition != -1 ) {
                    Log.i(TAG, "点击了其他的条目");

                    //让其他的条目停止播放(还原条目开始的状态)
                    MediaHelper.release();
                    //把播放条目的下标设置给适配器
                    adapter.setPlayPosition(position);
                    //刷新显示
                    adapter.notifyDataSetChanged();
                    //播放
                    mIvPlay.setVisibility(View.GONE);
                    mTvAllTime.setVisibility(View.GONE);
                    mPbLoading.setVisibility(View.VISIBLE);
                    //视频播放界面也需要显示
                    myVideoPlayer.setVideoViewVisiable(true);
                    mIvPlay.setImageResource(R.drawable.new_pause_video);

                    return;
                }

                //如果正在播放，暂停，更改为播放状态
                if (MediaHelper.getInstance().isPlaying()) {
                    //暂停
                    MediaHelper.pause();
                    //移除隐藏Controller布局的消息
                    handler.removeMessages(MSG_HIDE_CONTROLLER);
                    //移除更新播放时间和进度的消息
                    handler.removeMessages(MSG_UPDATE_TIME_PROGRESS);
                    mIvPlay.setImageResource(R.drawable.new_play_video);
                    hasPause = true;
                } else {
                    if (hasPause) {
                        //继续播放
                        MediaHelper.play();
                        showOrHideVideoController();
                        updatePlayTimeAndProgress();
                        hasPause = false;
                    }else{
                        //播放
                        //隐藏播放按钮
                        mIvPlay.setVisibility(View.GONE);
                        //隐藏播放时间控件
                        mTvAllTime.setVisibility(View.GONE);
                        //显示进度条
                        mPbLoading.setVisibility(View.VISIBLE);
                        //视频播放界面也需要显示
                        myVideoPlayer.setVideoViewVisiable(true);
                        //把播放条目的下标设置给适配器
                        adapter.setPlayPosition(position);
                    }
                    mIvPlay.setImageResource(R.drawable.new_pause_video);
                }

                break;
            case R.id.iv_fullscreen:
                break;
        }
    }

    private MyVideoPlayer myVideoPlayer;

    public void setMyVideoPlayer(MyVideoPlayer myVideoPlayer) {
        this.myVideoPlayer = myVideoPlayer;
    }

    public void setPbLoadingVisiable(boolean b) {
        mPbLoading.setVisibility(b ? View.VISIBLE : View.GONE);
    }

}