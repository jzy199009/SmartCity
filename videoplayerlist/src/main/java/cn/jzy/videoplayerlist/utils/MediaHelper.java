package cn.jzy.videoplayerlist.utils;

import android.media.MediaPlayer;

/**
 * Created by Administrator on 2017/4/9.
 * 多媒体的工具类(单例设计模式)
 */
public final class MediaHelper {

    //私有构造
    private MediaHelper() {
    }

    private static MediaPlayer mPlayer;

    //获取多媒体对象
    public static MediaPlayer getInstance(){
        if(mPlayer == null){
            mPlayer = new MediaPlayer();
        }
        return  mPlayer;
    }

    //播放
    public static void play(){
        if(mPlayer != null){
            mPlayer.start();
        }
    }

    //暂停
    public static void pause(){
        if(mPlayer != null){
            mPlayer.pause();
        }
    }

    //释放
    public static void release(){
        if(mPlayer != null){
            mPlayer.release();
            mPlayer = null;
        }
    }
}
