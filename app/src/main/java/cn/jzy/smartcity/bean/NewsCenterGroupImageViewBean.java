package cn.jzy.smartcity.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/4.
 * 新闻中心组图的数据模型
 */
public class NewsCenterGroupImageViewBean {
    public NewsCenterGroupImageViewDataBean data;
    public int retcode;

    //新闻中心数据模型
    public class NewsCenterGroupImageViewDataBean{
        public String countcommenturl;
        public String more;
        public List<NewsCenterGroupImageViewNewsBean> news;
        public String title;
        public List topic;
    }

    //真实的组图数据模型
    public class NewsCenterGroupImageViewNewsBean{
        public boolean comment;
        public String commentlist;
        public String commenturl;
        public String id;
        public String largeimage;
        public String listimage;//图片
        public String pubdate;
        public String smallimage;
        public String title;//标题
        public String type;
        public String url;
    }
}
