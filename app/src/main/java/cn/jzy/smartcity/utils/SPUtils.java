package cn.jzy.smartcity.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.SharedPreferences.*;

/**
 * Created by Administrator on 2017/3/29.
 */
public class SPUtils {

    private final static String name = "config";
    private final static int mode = Context.MODE_PRIVATE;

    /**
     * 保存首选项
     * @param context
     * @param key
     * @param value
     */
    public static void saveBoolean(Context context, String key, boolean value){

        SharedPreferences sp =  context.getSharedPreferences(name,mode);
        Editor editor = sp.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public static void saveInt(Context context,String key,int value){

        SharedPreferences sp = context.getSharedPreferences(name, mode);
        Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static void saveString(Context context,String key,String value){

        SharedPreferences sp = context.getSharedPreferences(name, mode);
        Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }


    /**
     * 获取首选项
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean(Context context,String key,boolean defValue){
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        return sp.getBoolean(key, defValue);
    }

    public static int getInt(Context context,String key,int defValue){
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        return sp.getInt(key, defValue);
    }

    public static String getString(Context context,String key,String defValue){
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        return sp.getString(key, defValue);
    }
}
