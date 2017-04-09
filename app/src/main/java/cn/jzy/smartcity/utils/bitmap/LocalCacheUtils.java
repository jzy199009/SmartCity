package cn.jzy.smartcity.utils.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import cn.jzy.smartcity.utils.Md5Utils;

/**
 * Created by Administrator on 2017/4/6.
 * <p>
 * 磁盘缓存（存放于该应用程序的cache目录下的zhbj_cache目录）
 */
public class LocalCacheUtils {
    //写缓存
    public static void saveCache(Context context, Bitmap bitmap, String url) {
        //缓存目录
        File dir = new File(context.getCacheDir(), "zhbj_cache");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //把图片缓存在缓存目录
        File file = new File(dir, Md5Utils.encode(url));
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (bitmap !=null) {
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
        }
    }

    //读缓存
    public static Bitmap readCache(Context context,String url){
        File dir = new File(context.getCacheDir(), "zhbj_cache");
        if(!dir.exists()){
            return null;
        }
        File file = new File(dir, Md5Utils.encode(url));
        if(!file.exists()){
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        //把数据缓存在内存中
        MemoryCacheUtils.saveCache(bitmap,url);
        return bitmap;
    }
}
