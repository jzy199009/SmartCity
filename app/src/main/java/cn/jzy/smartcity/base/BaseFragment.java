package cn.jzy.smartcity.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzy.smartcity.R;
import cn.jzy.smartcity.activity.MainActivity;

/**
 * Created by Administrator on 2017/3/30.
 */
public abstract class BaseFragment extends Fragment {

    @BindView(R.id.ib_menu)
    ImageButton mIbMenu;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ib_pic_type)
    public ImageButton mIbPicType;
    @BindView(R.id.container)
    public FrameLayout mContainer;

    //是否已经加载数据
    public boolean hasLoadData;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //root不为空的情况:
        //1.如果attachToRoot为true,就直接将这个布局添加到root父布局了,并且返回的view就是父布局
        //2.如果attachToRoot为false,就不会添加这个布局到root父布局,返回的view为resource指定的布局
        View view = inflater.inflate(R.layout.fragment_tab_base, container, false);
        ButterKnife.bind(this, view);

//        mIbMenu = (ImageButton) view.findViewById(R.id.ib_menu);
//        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
//        mIbPicType = (ImageButton) view.findViewById(R.id.ib_pic_type);
//        mContainer = (FrameLayout) view.findViewById(R.id.container);
//
//        mIbMenu.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initTitle();
    }

    //初始化标题   让每个子类去进行实现
    public abstract void initTitle();

    //设置Menu的显示状态
    public void setIbMenuDisplayState(boolean isShow) {
        mIbMenu.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    //设置PicType的显示状态
    public void setIbPicTypeDisplayState(boolean isShow) {
        mIbPicType.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    //设置标题内容
    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    //创建内容
    public abstract View createContent();

    //向容器里面添加内容
    public void addView(View view) {
        //清空原来的内容
        mContainer.removeAllViews();
        //添加内容
        mContainer.addView(view);
    }

    @OnClick(R.id.ib_menu)
    public void click(View view) {
        //对于侧滑菜单进行切换
        //目标：获取SlidingMenu -->MainActivity
        ((MainActivity) getActivity()).mSlidingMenu.toggle();
    }

//    @Override
//    public void onClick(View v) {
//        //对于侧滑菜单进行切换
//        //目标：获取SlidingMenu -->MainActivity
//        ((MainActivity)getActivity()).mSlidingMenu.toggle();
//    }
}
