package cn.jzy.smartcity.fragment;

import android.view.View;

import cn.jzy.smartcity.base.BaseFragment;

/**
 * Created by Administrator on 2017/3/30.
 */
public class SettingFragment extends BaseFragment {
    @Override
    public void initTitle() {
        setIbMenuDisplayState(false);
        setIbPicTypeDisplayState(false);
        setTitle("设置");
    }

    @Override
    public View createContent() {
        return null;
    }
}
