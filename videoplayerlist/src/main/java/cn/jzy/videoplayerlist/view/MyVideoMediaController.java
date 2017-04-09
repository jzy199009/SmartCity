package cn.jzy.videoplayerlist.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzy.videoplayerlist.R;

/**
 * Created by Administrator on 2017/4/9.
 * 对应视频播放控制界面的封装
 */
public class MyVideoMediaController extends RelativeLayout {
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
    private void initView() {
        View view = View.inflate(getContext(), R.layout.video_controller, this);
        ButterKnife.bind(this,view);

        initViewDisplay();
    }

    //初始化控件的显示状态
    public void initViewDisplay() {
        mTvTitle.setVisibility(View.VISIBLE);
        mIvPlay.setVisibility(View.VISIBLE);
        //mIvPlay.setImageResource(R.drawable.new_play_video);
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
                break;
            case R.id.iv_share:
                break;
            case R.id.iv_play:
                //隐藏播放按钮
                mIvPlay.setVisibility(View.GONE);
                //隐藏播放时间控件
                mTvAllTime.setVisibility(View.GONE);
                //显示进度条
                mPbLoading.setVisibility(View.VISIBLE);
                //视频播放界面也需要显示
                myVideoPlayer.setVideoViewVisiable(true);

                break;
            case R.id.iv_fullscreen:
                break;
        }
    }

    private MyVideoPlayer myVideoPlayer;
    public void setMyVideoPlayer(MyVideoPlayer myVideoPlayer) {
        this.myVideoPlayer = myVideoPlayer;
    }

}