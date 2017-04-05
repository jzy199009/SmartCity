package cn.jzy.smartcity.adapter;

import android.content.Context;
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
import cn.jzy.smartcity.bean.NewsCenterGroupImageViewBean;

/**
 * Created by Administrator on 2017/4/5.
 */
public class NewsCenterGroupImageViewAdapter extends RecyclerView.Adapter {
    //保存组图数据
    private List<NewsCenterGroupImageViewBean.NewsCenterGroupImageViewNewsBean> news;
    private Context context;

    public NewsCenterGroupImageViewAdapter(List<NewsCenterGroupImageViewBean.NewsCenterGroupImageViewNewsBean> news, Context context) {
        this.news = news;
        this.context = context;
    }

    //创建组图中条目布局
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_newscenter_group_imageview, parent, false);

        return new ViewHolder(view);
    }

    //绑定数据
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewsCenterGroupImageViewBean.NewsCenterGroupImageViewNewsBean newsBean = news.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        //Picasso主要采用的是图片三级缓存技术的实现
        Picasso.with(context).load(newsBean.listimage).into(viewHolder.mIv);
        viewHolder.mTvTitle.setText(newsBean.title);
    }

    @Override
    public int getItemCount() {
        return news != null ? news.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv)
        ImageView mIv;
        @BindView(R.id.tv_title)
        TextView mTvTitle;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
