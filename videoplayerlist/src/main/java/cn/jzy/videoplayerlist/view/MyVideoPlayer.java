package cn.jzy.videoplayerlist.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.*;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzy.videoplayerlist.R;

/**
 * Created by Administrator on 2017/4/9.
 * 对于视频播放界面的一个封装类
 */
public class MyVideoPlayer extends RelativeLayout {
    @BindView(R.id.video_view)
    TextureView mVideoView;
    @BindView(R.id.mediaController)
    MyVideoMediaController mMediaController;

    public MyVideoPlayer(Context context) {
        this(context, null);
    }

    public MyVideoPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.video_play, this);
        ButterKnife.bind(this,view);

        initViewDisplay();

        //把MyVideoPlayer对象传递给MyVideoMediaController
        mMediaController.setMyVideoPlayer(this);
    }

    public void setVideoViewVisiable(boolean b) {
        mVideoView.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    //初始化控件的显示状态
    public void initViewDisplay() {
        mVideoView.setVisibility(View.GONE);
        //mMediaController.initViewDisplay();
    }


}
