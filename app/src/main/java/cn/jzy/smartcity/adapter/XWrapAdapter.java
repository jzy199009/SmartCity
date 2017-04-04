package cn.jzy.smartcity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/4/2.
 * 包装的适配器：区分头、脚、正常的数据(返回值是0)
 */
public class XWrapAdapter extends RecyclerView.Adapter {
    //头布局
    private View mHeaderView;
    //脚布局
    private View mFooterView;
    //正常的适配器
    private RecyclerView.Adapter mAdapter;
    private int mAdjPoistion;
    private int mAdapterCount;

    public XWrapAdapter(View headerView, View footerView, RecyclerView.Adapter adapter) {
        mHeaderView = headerView;
        mFooterView = footerView;
        mAdapter = adapter;
    }

    //处理条目的类型
    @Override
    public int getItemViewType(int position) {
        //头
        if (position == 0) {
            return RecyclerView.INVALID_TYPE;//-1
        }
        //正常的布局
        mAdjPoistion = position - 1;
        mAdapterCount = mAdapter.getItemCount();
        if (mAdjPoistion < mAdapterCount) {
            return mAdapter.getItemViewType(mAdjPoistion);
        }
        //脚
        return RecyclerView.INVALID_TYPE - 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RecyclerView.INVALID_TYPE) {
            //头
            return new HeaderViewHolder(mHeaderView);
        } else if (viewType == RecyclerView.INVALID_TYPE - 1) {
            //脚
            return new FooterViewHolder(mFooterView);
        }

        //正常
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    //绑定数据
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //头
        if (position == 0) {
            return;
        }
        //正常布局
        if (mAdjPoistion < mAdapterCount) {
            mAdapter.onBindViewHolder(holder, mAdjPoistion);
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 2;
    }

    //头的ViewHolder
    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    //脚的ViewHolder
    static class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
