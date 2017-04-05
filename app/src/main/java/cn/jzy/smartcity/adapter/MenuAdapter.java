package cn.jzy.smartcity.adapter;

import android.content.Context;
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
import cn.jzy.smartcity.activity.MainActivity;
import cn.jzy.smartcity.base.BaseFragment;
import cn.jzy.smartcity.bean.NewsCenterBean;
import cn.jzy.smartcity.fragment.NewsCenterFragment;

/**
 * Created by Administrator on 2017/3/31.
 */
public class MenuAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<NewsCenterBean.NewsCenterMenuBean> data;

    //默认选中的条目下标(0)
    private int selectedPosition;

    public void setData(List<NewsCenterBean.NewsCenterMenuBean> data) {
        this.data = data;

        //刷新显示
        notifyDataSetChanged();
    }

    public MenuAdapter(Context context, List<NewsCenterBean.NewsCenterMenuBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final NewsCenterBean.NewsCenterMenuBean newsCenterMenuBean = data.get(position);
        MyViewHolder viewHolder = (MyViewHolder) holder;

        viewHolder.mTvMenuTitle.setText(newsCenterMenuBean.title);
        //System.out.println("selectedPosition = " + selectedPosition);
        //选中
        if (selectedPosition == position) {
            viewHolder.mTvMenuTitle.setTextColor(Color.RED);
            viewHolder.mIvArrow.setImageResource(R.drawable.menu_arr_select);
        } else {
            //未选中
            viewHolder.mIvArrow.setImageResource(R.drawable.menu_arr_normal);
            viewHolder.mTvMenuTitle.setTextColor(Color.WHITE);
        }

        //处理条目点击事件
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否点击的同一个条目，不是才刷新界面
                //这里通过selectedPosition记录了点击item的索引。这样就可以防止用户
                //多次点击同一个条目，多次刷新的效果
                if (selectedPosition != position) {
                    selectedPosition = position;

                    //刷新界面
                    notifyDataSetChanged();

                    //修改对应tab页面的标题
                    BaseFragment baseFragment = ((MainActivity) context).getCurrentTabFragment();
                    baseFragment.setTitle(newsCenterMenuBean.title);

                    if (baseFragment instanceof NewsCenterFragment) {
                        NewsCenterFragment newsCenterFragment = (NewsCenterFragment) baseFragment;
                        //切换内容
                        newsCenterFragment.switchContent(position);
                    }
                }

                //关闭侧滑菜单
                ((MainActivity)context).mSlidingMenu.toggle();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_arrow)
        ImageView mIvArrow;
        @BindView(R.id.tv_menu_title)
        TextView mTvMenuTitle;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
