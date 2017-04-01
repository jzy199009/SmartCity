package cn.jzy.smartcity.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/30.
 * 新闻中心对应的模型对象
 */
public class NewsCenterBean {

    public List<NewsCenterMenuBean> data;
    public List<String> extend;
    public int retcode;

    //侧滑菜单对应的分类数据
    public class NewsCenterMenuBean {
        public List<NewsCenterNewsTabBean> children;
        public int id;
        public String title;
        public String url;
        public String url1;
        public String dayurl;
        public String excurl;
        public String weekurl;
        public int type;
    }

    //新闻中心新闻tab的模型
    public class NewsCenterNewsTabBean {
        public int id;
        public String title;
        public String url;
        public int type;
    }
}
