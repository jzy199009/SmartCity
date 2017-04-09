package cn.jzy.smartcity.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzy.smartcity.R;
import cn.jzy.smartcity.activity.NewsDetailActivity;
import cn.jzy.smartcity.bean.NewsCenterTabBean;
import cn.jzy.smartcity.utils.Constant;
import cn.jzy.smartcity.utils.SPUtils;
import cn.jzy.smartcity.utils.bitmap.BitmapUtils;

/**
 * Created by Administrator on 2017/4/2.
 */
public class NewsListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<NewsCenterTabBean.NewsBean> news;

    public NewsListAdapter(Context context, List<NewsCenterTabBean.NewsBean> news) {
        mContext = context;
        this.news = news;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_news, parent, false);
        return new ViewHolder(view);
    }

    //绑定数据
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final NewsCenterTabBean.NewsBean newsBean = news.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;

        //Picasso.with(mContext).load(newsBean.listimage).into(viewHolder.mIvIcon);

        //采用我们自己的图片缓存工具有很多bug,工作用Picasso
        BitmapUtils.display(mContext,viewHolder.mIvIcon,newsBean.listimage);
        viewHolder.mTvTitle.setText(newsBean.title);
        viewHolder.mTvTime.setText(newsBean.pubdate);

        //判断每条新闻是否已经被查看过，如果查看，修改字体样式为灰色
        String readNews = SPUtils.getString(mContext, Constant.KEY_HAS_READ, "");
        if (readNews.contains(newsBean.id)) {
            viewHolder.mTvTitle.setTextColor(Color.GRAY);
        } else {
            viewHolder.mTvTitle.setTextColor(Color.BLACK);
        }

        //条目点击事件
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到新闻详情界面
                Intent intent = new Intent(mContext,NewsDetailActivity.class);
                intent.putExtra("url",newsBean.url);
                mContext.startActivity(intent);

                //存储该条新闻的唯一标识
                String id = newsBean.id;
                //存储在哪里？Sp   File   DB（)实际开发中要存储在数据库!
                String readNews = SPUtils.getString(mContext, Constant.KEY_HAS_READ, "");
                if (!readNews.contains(id)){
                    String value = readNews + "," + id;
                    //存储
                    SPUtils.saveString(mContext,Constant.KEY_HAS_READ,value);
                    //刷新界面
                    //notifyDataSetChanged();
                    viewHolder.mTvTitle.setTextColor(Color.GRAY);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return news != null ? news.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_icon)
        ImageView mIvIcon;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_time)
        TextView mTvTime;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
