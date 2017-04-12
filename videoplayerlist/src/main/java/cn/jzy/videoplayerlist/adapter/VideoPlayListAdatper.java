package cn.jzy.videoplayerlist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzy.videoplayerlist.R;
import cn.jzy.videoplayerlist.bean.VideoPlayerItemInfo;
import cn.jzy.videoplayerlist.view.MyVideoPlayer;

/**
 * Created by Administrator on 2017/4/8.
 */
public class VideoPlayListAdatper extends RecyclerView.Adapter {
    private Context context;
    private List<VideoPlayerItemInfo> videoPlayerItemInfoList;

    //记录之前播放的条目下标
    public int currentPosition = -1;

    public VideoPlayListAdatper(Context context, List<VideoPlayerItemInfo> videoPlayerItemInfoList) {
        this.context = context;
        this.videoPlayerItemInfoList = videoPlayerItemInfoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video_play, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;

        //获取到条目对应的数据
        VideoPlayerItemInfo info = videoPlayerItemInfoList.get(position);
        //传递给条目里面的MyVideoPlayer
        viewHolder.mVideoPlayer.setPlayData(info);
        //把条目的下标传递给MyVideoMediaController对象
        viewHolder.mVideoPlayer.mMediaController.setPosition(position);

        //把Adapter对象传递给MyVideoMediaController对象
        viewHolder.mVideoPlayer.mMediaController.setAdapter(this);

        if(position != currentPosition){
            //设置为初始化状态
            viewHolder.mVideoPlayer.initViewDisplay();
        }
    }

    @Override
    public int getItemCount() {
        return videoPlayerItemInfoList != null ? videoPlayerItemInfoList.size() : 0;
    }

    public void setPlayPosition(int position) {
        currentPosition = position;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_bg)
        ImageView mIvBg;
        @BindView(R.id.videoPlayer)
        MyVideoPlayer mVideoPlayer;
        @BindView(R.id.iv_author)
        ImageView mIvAuthor;
        @BindView(R.id.tv_author_name)
        TextView mTvAuthorName;
        @BindView(R.id.tv_play_count)
        TextView mTvPlayCount;
        @BindView(R.id.iv_comment)
        ImageView mIvComment;
        @BindView(R.id.tv_comment_count)
        TextView mTvCommentCount;
        @BindView(R.id.iv_comment_more)
        ImageView mIvCommentMore;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
