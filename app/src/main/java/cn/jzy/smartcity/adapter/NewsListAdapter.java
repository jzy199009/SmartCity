package cn.jzy.smartcity.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzy.smartcity.R;
import cn.jzy.smartcity.activity.NewsDetailActivity;
import cn.jzy.smartcity.bean.NewsCenterTabBean;

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
        ViewHolder viewHolder = (ViewHolder) holder;

        Picasso.with(mContext).load(newsBean.listimage).into(viewHolder.mIvIcon);
        viewHolder.mTvTitle.setText(newsBean.title);
        viewHolder.mTvTime.setText(newsBean.pubdate);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到新闻详情界面
                Intent intent = new Intent(mContext,NewsDetailActivity.class);
                intent.putExtra("url",newsBean.url);
                mContext.startActivity(intent);
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
