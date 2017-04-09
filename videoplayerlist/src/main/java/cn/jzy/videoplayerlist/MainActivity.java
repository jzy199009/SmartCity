package cn.jzy.videoplayerlist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzy.videoplayerlist.adapter.VideoPlayListAdatper;
import cn.jzy.videoplayerlist.bean.VideoPlayerItemInfo;
import cn.jzy.videoplayerlist.view.RecycleViewDivider;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;
    private List<VideoPlayerItemInfo> videoPlayerItemInfoList;
    private LinearLayoutManager mLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //网络视频路径
        String url = "http://flv2.bn.netease.com/videolib3/1604/28/fVobI0704/SD/fVobI0704-mobile.mp4";

        //数据的初始化
        videoPlayerItemInfoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            videoPlayerItemInfoList.add(new VideoPlayerItemInfo(i,url));
        }

        //初始化RecyclerView
        mLl = new LinearLayoutManager(this);
        //设置布局管理器
        mRv.setLayoutManager(mLl);
        //添加分割线
        mRv.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL,1, Color.BLUE));
        //创建适配器
        mRv.setAdapter(new VideoPlayListAdatper(this,videoPlayerItemInfoList));

    }
}
