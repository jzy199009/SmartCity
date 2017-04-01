package cn.jzy.smartcity.fragment;

import android.view.View;

import cn.jzy.smartcity.base.BaseFragment;
import cn.jzy.smartcity.base.BaseLoadNetDataOperator;

/**
 * Created by Administrator on 2017/3/30.
 */
public class GovaffairsFragment extends BaseFragment implements BaseLoadNetDataOperator{
    @Override
    public void initTitle() {

        setIbMenuDisplayState(true);
        setIbPicTypeDisplayState(false);
        setTitle("人口管理");
    }

    @Override
    public View createContent() {
        return null;
    }

    @Override
    public void loadNetData() {

    }
}
