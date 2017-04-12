package cn.jzy.videoplayerlist.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzy.videoplayerlist.R;
import cn.jzy.videoplayerlist.bean.VideoPlayerItemInfo;
import cn.jzy.videoplayerlist.utils.MediaHelper;

/**
 * Created by Administrator on 2017/4/9.
 * 对于视频播放界面的一个封装类
 */
public class MyVideoPlayer extends RelativeLayout {
    private static final String TAG = "MyVideoPlayer";
    @BindView(R.id.video_view)
    TextureView mVideoView;
    @BindView(R.id.mediaController)
    public MyVideoMediaController mMediaController;
    private Surface mSurface;
    public MediaPlayer player;
    public boolean hasPlay;//是否播放了

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

        //进行TextureView控件创建的监听
        mVideoView.setSurfaceTextureListener(textureListener);
    }

    private TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        //创建完成  TextureView才可以进行视频画面的显示
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.i(TAG,"onSurfaceTextureAvailable");
            //连接对象（MediaPlayer和TextureView）
             mSurface = new Surface(surface);
            play(info.url);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            //Log.i(TAG,"onSurfaceTextureSizeChanged");
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            //Log.i(TAG,"onSurfaceTextureDestroyed");
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            // Log.i(TAG,"onSurfaceTextureUpdated");
        }
    };

    //视频播放（视频的初始化）
    private void play(String url) {
        try {
            player = MediaHelper.getInstance();
            player.reset();
            player.setDataSource(url);
            //让MediaPlayer和TextureView进行视频画面的结合
            player.setSurface(mSurface);
            //设置监听
            player.setOnBufferingUpdateListener(onBufferingUpdateListener);
            player.setOnCompletionListener(onCompletionListener);
            player.setOnErrorListener(onErrorListener);
            player.setOnPreparedListener(onPreparedListener);
            player.setScreenOnWhilePlaying(true);//在视频播放的时候保持屏幕的高亮
            //异步准备
            player.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //准备完成监听
    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            //隐藏视频加载进度条
            mMediaController.setPbLoadingVisiable(false);
            //进行视频的播放
            MediaHelper.play();
            hasPlay = true;
            //隐藏标题
            mMediaController.delayHideTitle();
            //设置视频的总时长
            mMediaController.setDuration(player.getDuration());
            //更新播放的时间和进度
            mMediaController.updatePlayTimeAndProgress();
        }
    };

    //错误监听
    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return true;
        }
    };

    //播放完成监听
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            //视频播放完成
            mMediaController.showPlayFinishView();
        }
    };

    //缓冲的监听
    private MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            //更新进度条的第二进度（缓存进度）
            mMediaController.updateSeekBarSecondProgress(percent);
        }
    };

    public void setVideoViewVisiable(boolean b) {
        mVideoView.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    //初始化控件的显示状态
    public void initViewDisplay() {
        mVideoView.setVisibility(View.GONE);
        mMediaController.initViewDisplay();
    }


    private VideoPlayerItemInfo info;
    public void setPlayData(VideoPlayerItemInfo info) {
        this.info = info;
    }
}
