package cn.jzy.smartcity.utils;

/**
 * Created by Administrator on 2017/3/29.
 *
 * 常量类
 */
public class Constant {

    //是否已经执行完向导
    public final static String KEY_HAS_GUIDE = "key_has_guide";

    //是否已经读过条目,读过就变灰色
    public final static String KEY_HAS_READ = "key_has_read";

    //服务器的主机
    public final static String HOST = "http://10.0.2.2:8080/zhbj";

    //新闻中心页面的数据地址
    public final static String NEWSCENTER_URL = HOST + "/categories.json";
}
