package cn.jzy.a360news.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */
public class ResuleBean {
    public List<NewsData> data;
    public int error;

    public class NewsData{
        public int id;
        public List<String> imgs;
        public String title;
        public String weburl;
    }
}
