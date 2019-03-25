package com.ch.cmusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

import com.ch.cmusic.LogUtils;
import com.ch.cmusic.model.MusicModel;

import java.io.IOException;

/**
 * 作者： ch
 * 时间： 2019/3/22 0022-上午 9:57
 * 描述： 音乐播放service
 * 来源：
 */

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private int duration;
    private int state;
    private MusicModel currentMusic;
    private MusicUpdataListener updataListener;

    public void setUpdataListener(MusicUpdataListener updataListener) {
        this.updataListener = updataListener;
    }

    /**
     * 空闲
     */
    public final static int STATE_NONE = 0;
    /**
     * 播放
     */
    public static final int STATE_PLAYING = 1;
    /**
     * 暂停
     */
    public final static int STATE_PAUSED = 2;
    /**
     * 停止
     */
    public final static int STATE_STOPPED = 3;

    @Override

    public IBinder onBind(Intent intent) {
        return new ServiceBinder();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                if (updataListener != null) {
                    updataListener.onBufferingUpdate(mp, percent);
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (updataListener != null) {
                    updataListener.onCompletion(mp);
                }
            }
        });
        setState(STATE_NONE);
    }

    /**
     * 开始播放
     */
    public void start() {
        // 装载完毕回调
        setState(STATE_PLAYING);
        mediaPlayer.start();
        duration = mediaPlayer.getDuration();
        if (updataListener != null) {
            updataListener.realPlay(currentMusic);
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (state == STATE_PLAYING) {
            mediaPlayer.pause();
            setState(STATE_PAUSED);
            if (updataListener != null) {
                updataListener.onPause();
            }
        }
    }

    public int getDuration() {
        return duration;
    }

    /**
     * 停止播放
     */
    public void stop() {
        mediaPlayer.pause();
        mediaPlayer.reset();
        currentMusic = null;
        setState(STATE_STOPPED);
    }

    /**
     * 退出
     */
    public void quit() {
        mediaPlayer.reset();
        mediaPlayer.release();
        currentMusic = null;
        updataListener = null;
        stopSelf();
    }

    /**
     * 进度
     *
     * @param pos
     */
    public void seekto(long pos) {
        if (state == STATE_PLAYING || state == STATE_PAUSED) {


            if (pos < 0) {
                pos = 0;
            } else if (pos > mediaPlayer.getDuration()) {
                pos = mediaPlayer.getDuration();
            }
            mediaPlayer.seekTo((int) pos);
            start();
        }
    }

    /**
     * 当前的播放进度
     *
     * @return
     */
    public long getCurrentProgress() {
        if (mediaPlayer == null || getState() == STATE_STOPPED || getState() == STATE_NONE) {

            return 0;
        }
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * 播放
     *
     * @param model
     */
    public void play(MusicModel model) {
        if (model == null || TextUtils.isEmpty(model.getPath())) {
            return;
        }
        try {
            LogUtils.log("play path=" + model.getPath());

            mediaPlayer.reset();

            mediaPlayer.setDataSource(model.getPath());
            // 通过异步的方式装载媒体资源
            mediaPlayer.prepareAsync();
            currentMusic = model;
            if (updataListener != null) {
                updataListener.startPlay(currentMusic);
            }
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    start();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置状态
     *
     * @param state
     */
    private void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public MusicModel getCurrentMusic() {
        return currentMusic;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class ServiceBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }


    public interface MusicUpdataListener {
        void onCompletion(MediaPlayer mediaPlayer);

        void onBufferingUpdate(MediaPlayer mediaPlayer, int percent);

        void onProgress(int progress, int duration);

        void startPlay(MusicModel model);

        void realPlay(MusicModel model);

        void onPause();
    }

}
