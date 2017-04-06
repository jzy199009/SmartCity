package cn.jzy.smartcity.utils.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.jzy.smartcity.utils.MyLogger;

/**
 * Created by Administrator on 2017/4/6.
 * 网络缓存工具类(图片)
 */
public class NetCacheUtils {

    private static final String TAG = "NetCacheUtils";

    private Context context;

    //从网络获取图片
    public void getBitmapFromNet(Context context, ImageView iv, String url) {
        this.context = context;

        //当我们快速滑动组图列表时，我们发现，图片错乱的现象。
//        原因
//        由于网络问题，当前条目加载了之前的图片。
//        思路
//        我们可以给ImageView打个标记，该标记就是图片地址
//        当显示图片时，匹配url地址。是否一致。
//        一致，显示图片，不一致，放弃显示图片。

        // 让ImageView和url关联起来
        iv.setTag(url);

        //异步操作
        new BitmapTask().execute(iv, url);
    }

    //异步任务
    class BitmapTask extends AsyncTask<Object, Void, Bitmap> {

        private ImageView iv;
        private String url;

        @Override
        protected Bitmap doInBackground(Object... params) {
            //获取参数
            iv = (ImageView) params[0];
            url = (String) params[1];

            //下载图片
            Bitmap bitmap = downloadBitmap(url);
            MyLogger.i(TAG, "从网络上加载了图片");
            //执行磁盘缓存
            LocalCacheUtils.saveCache(context,bitmap,url);
            //把数据缓存在内存中
            MemoryCacheUtils.saveCache(bitmap,url);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //获取ImageView对应的url
            //一致，显示图片，不一致，放弃显示图片
            String url = (String) iv.getTag();
            if (bitmap != null && this.url.equals(url)) {
                iv.setImageBitmap(bitmap);
            }
        }
    }

    //下载图片
    private Bitmap downloadBitmap(String url) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(5000);
            conn.connect();
            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream inputStream = conn.getInputStream();
                //把流转换成Bitmap对象
                bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return bitmap;
    }
}
