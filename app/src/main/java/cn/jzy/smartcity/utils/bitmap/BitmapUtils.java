package cn.jzy.smartcity.utils.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import cn.jzy.smartcity.utils.MyLogger;

/**
 * Created by Administrator on 2017/4/6.
 * 图片三级缓存框架
 */
public class BitmapUtils {

    private static final String TAG = "BitmapUtils";

    static {
        netCacheUtils = new NetCacheUtils();
        localCacheUtils = new LocalCacheUtils();
        memoryCacheUtils = new MemoryCacheUtils();
    }

    private static NetCacheUtils netCacheUtils;
    private static LocalCacheUtils localCacheUtils;
    private static MemoryCacheUtils memoryCacheUtils;

    //显示图片
    public static void display(Context context, ImageView iv, String url){
        Bitmap bitmap = null;

        //内存缓存
        bitmap = memoryCacheUtils.readCache(url);
        if(bitmap != null){
            iv.setImageBitmap(bitmap);
            MyLogger.i(TAG,"从内存获取了图片");
            return;
        }

        //磁盘缓存
        bitmap = localCacheUtils.readCache(context,url);
        if(bitmap != null){
            iv.setImageBitmap(bitmap);
            MyLogger.i(TAG,"从磁盘获取了图片");
            return;
        }

        //网络缓存
        netCacheUtils.getBitmapFromNet(context,iv,url);
    }
}
