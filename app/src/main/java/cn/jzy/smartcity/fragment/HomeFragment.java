package cn.jzy.smartcity.fragment;

import android.view.View;

import cn.jzy.smartcity.base.BaseFragment;
import cn.jzy.smartcity.base.BaseLoadNetDataOperator;

/**
 * Created by Administrator on 2017/3/30.
 */
public class HomeFragment extends BaseFragment implements BaseLoadNetDataOperator{
    @Override
    public void initTitle() {
        setIbMenuDisplayState(false);
        setIbPicTypeDisplayState(false);
        setTitle("首页");
    }

    @Override
    public View createContent() {
        return null;
    }

    @Override
    public void loadNetData() {

    }
}
