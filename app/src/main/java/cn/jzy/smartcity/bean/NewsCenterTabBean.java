package cn.jzy.smartcity.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/1.
 * 新闻中心子tab的数据模型
 */
public class NewsCenterTabBean {
    public NewsCenterDataBean data;
    public int retcode;

    public class NewsCenterDataBean{
        public String countcommenturl;
        public String more;
        public List<NewsBean> news;
        public String title;
        public List<TopicBean> topic;
        public List<TopNewsBean> topnews;
    }

    //轮播图
    public class TopNewsBean{
        public boolean comment;
        public String commentlist;
        public String commenturl;
        public String  id;
        public String pubdate;
        public String title;
        public String topimage;
        public String type;
        public String url;
    }

    public class TopicBean{
        public String description;
        public String id;
        public String listimage;
        public String sort;
        public String title;
        public String url;
    }

    //新闻列表数据模型
    public class NewsBean{
        public String comment;
        public String commentlist;
        public String commenturl;
        public String id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }
}
