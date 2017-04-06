package cn.jzy.smartcity.utils.bitmap;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Administrator on 2017/4/6.
 *
 * 内存缓存：通过HashMap来进行数据的存储
 */
public class MemoryCacheUtils {

    static {
        //caches = new HashMap<>();//Android虚拟机的内存只有16M,产生OOM异常（内存溢出）
        //java语言提供了另外一种机制：软引用、弱引用、虚引用
        //软引用：当虚拟机内存不足的时候，回收软引用的对象
        //弱引用:当对象没有应用的时候，马上回收
        //虚引用 ：任何情况下都可能回收
        //java默认的数据类型是强引用类型
        //caches = new HashMap<>();

        //因为从 Android 2.3 (API Level 9)开始，垃圾回收器会更倾向于回收持有软引用或弱引用的对象，这让软引用和弱引用变得不再可靠。
//        	使用最近最少算法机制。来缓存每个对象的。
//        	把近期最少使用的数据从缓存中移除，保留使用最频繁的数据
//        	LruCache内存维护一个LinkedHashMap，来维护每个缓存对象
//        	从LruCache取出对象，它会把当前使用的对象进行移动到LinkedHashMap尾端
//        	如果添加一个缓存对象，LruCache会把当前对象也放在LinkedHashMap尾端
//        	在LinkedHashMap的顶端就是最近最少使用的缓存对象。也就是被移除的对象了。

        long maxMemory = Runtime.getRuntime().maxMemory();//获取Dalvik 虚拟机最大的内存大小：16(不一定)

        lruCache = new LruCache<String,Bitmap>((int) (maxMemory / 8)) {//指定内存缓存集合的大小
            //获取图片的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };
    }

    //private static HashMap<String, SoftReference<Bitmap>> caches;
    private static LruCache<String, Bitmap> lruCache;

    //写缓存
    public static void saveCache(Bitmap bitmap, String url){
        //caches.put(url,bitmap);
        //将bitmap由强引用状态，设置软引用状态
//        SoftReference<Bitmap> soft = new SoftReference<Bitmap>(bitmap);
//        caches.put(url,soft);

        lruCache.put(url,bitmap);
    }

    //读缓存
    public static Bitmap readCache(String url){
        //return caches.get(url);
//        SoftReference<Bitmap> soft = caches.get(url);
//        if(soft != null){
//            Bitmap bitmap = soft.get();
//            return bitmap;
//        }
//        return null;

        return lruCache.get(url);

    }
}
