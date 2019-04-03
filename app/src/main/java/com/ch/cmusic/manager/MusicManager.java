package com.ch.cmusic.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;

import com.ch.cmusic.LogUtils;
import com.ch.cmusic.MyApp;
import com.ch.cmusic.model.MusicModel;
import com.ch.cmusic.service.MusicService;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： ch
 * 时间： 2019/3/22 0022-上午 11:51
 * 描述： 播放管理器
 * 来源：
 */

public class MusicManager {
    private static MusicManager INSTANCE;
    private MusicService musicService;
    private List<MusicUpdataListener> updataListeners;
    private final Handler progressHandler = new Handler();


    private MusicService.MusicUpdataListener upListener = new MusicService.MusicUpdataListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {

            MusicModel musicModel = QueueManager.getInstance().next();

            if (musicModel != null) {
                //播放下一首
                play(musicModel);
            }


            if (updataListeners == null) {
                return;
            }
            for (MusicUpdataListener listener : updataListeners) {
                if (listener != null) {
                    listener.onCompletion(mediaPlayer);
                }
            }
        }

        @Override
        public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
            if (updataListeners == null) {
                return;
            }
            for (MusicUpdataListener listener : updataListeners) {
                if (listener != null) {
                    listener.onBufferingUpdate(mediaPlayer, percent);
                }
            }
        }

        @Override
        public void onProgress(int progress, int duration) {
            if (updataListeners == null) {
                return;
            }
            for (MusicUpdataListener listener : updataListeners) {
                if (listener != null) {
                    listener.onProgress(progress, duration);
                }
            }
        }


        @Override
        public void startPlay(MusicModel model) {
            if (updataListeners == null) {
                return;
            }
            for (MusicUpdataListener listener : updataListeners) {
                if (listener != null) {
                    listener.startPlay(model);
                }
            }
        }

        @Override
        public void realPlay(MusicModel model) {
            if (updataListeners == null) {
                return;
            }
            for (MusicUpdataListener listener : updataListeners) {
                if (listener != null) {
                    listener.realPlay(model);
                }
            }
        }

        @Override
        public void onPause() {
            if (updataListeners == null) {
                return;
            }
            for (MusicUpdataListener listener : updataListeners) {
                if (listener != null) {
                    listener.onPause();
                }
            }
        }
    };

    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            boolean isUpdate = musicService.getState() == MusicService.STATE_PLAYING;
            if (isUpdate && upListener != null && getCurrentMusic() != null) {
                upListener.onProgress((int) musicService.getCurrentProgress(),
                        musicService.getDuration());
            }

            if (isUpdate && updataListeners != null && getCurrentMusic() != null) {

                for (MusicUpdataListener listener : updataListeners) {
                    listener.onProgress((int) musicService.getCurrentProgress(),
                            musicService.getDuration());
                }
            }
            progressHandler.postDelayed(this, 100);
        }
    };


    public static MusicManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MusicManager();
        }
        return INSTANCE;
    }

    public void addUpdataListener(MusicUpdataListener listener) {
        if (updataListeners == null) {
            updataListeners = new ArrayList<>();
        }

        if (listener == null) {
            return;
        }
        updataListeners.add(listener);
    }


    public void removeUpdataListener(MusicUpdataListener listener) {
        if (updataListeners != null) {
            updataListeners.remove(listener);
        }

    }


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.ServiceBinder) service).getService();
            //设置 Service 接口，便于管理 Progress、onCompletion 等
            musicService.setUpdataListener(upListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    /**
     * 启动服务
     */
    public void startService() {
        MyApp myApp = MyApp.getMyApp();
        Intent intent = new Intent(myApp, MusicService.class);
        intent.setAction(MusicService.ACTION_START);
        myApp.startService(intent);
    }

    /**
     * 绑定服务
     */
    public void bindPlayService() {
        MyApp myApp = MyApp.getMyApp();
        myApp.bindService(new Intent(myApp, MusicService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 解除绑定
     */
    public void unbindService() {
        if (null != mConnection) {
            MyApp myApp = MyApp.getMyApp();
            myApp.unbindService(mConnection);
        }
    }

    /**
     * 停止服务
     */
    public void stopService() {
        MyApp myApp = MyApp.getMyApp();
        myApp.stopService(new Intent(myApp, MusicService.class));
    }

    /**
     * 退出
     */
    public void quit() {
        LogUtils.log("quit");

        progressHandler.removeCallbacks(progressRunnable);

        QueueManager.getInstance().clear();

        if (musicService != null) {
            musicService.quit();
        }
        if (updataListeners != null) {
            updataListeners.clear();
        }

        unbindService();
        stopService();
    }

    /**
     * 播放
     *
     * @param model
     */
    public void play(MusicModel model) {
        if (musicService == null) {
            LogUtils.log("musicService is null");
            return;
        }
        if (model == null) {
            LogUtils.log("model is null");
        }
        //添加到播放队列
        QueueManager.getInstance().addMusic(model);


        musicService.play(model);
        progressHandler.post(progressRunnable);
    }

    public void play(List<MusicModel> list) {
        play(list, 0);
    }

    /**
     * 播放
     *
     * @param list
     * @param position
     */
    public void play(List<MusicModel> list, int position) {
        if (list == null || list.size() == 0) {
            LogUtils.log("list MusicModel is null");
            return;
        }

        if (position >= list.size()) {
            LogUtils.log("invalid position");
            return;
        }
        //添加到播放队列
        QueueManager.getInstance().addMusic(list);

        play(list.get(position));

    }

    /**
     * 暂停
     */
    public void pause() {
        if (musicService == null) {
            return;
        }
        LogUtils.log("music pause");
        musicService.pause();
        progressHandler.removeCallbacks(progressRunnable);
    }

    /**
     * 开始播放 （暂停之后）
     */
    public void start() {
        if (musicService == null) {
            return;
        }
        musicService.start();
        LogUtils.log("music start");
        progressHandler.post(progressRunnable);
    }

    /**
     * 是否在播放
     *
     * @return
     */
    public boolean isPlaying() {
        if (musicService == null) {
            return false;
        }
        return musicService.getState() == MusicService.STATE_PLAYING;

    }

    /**
     * 获取当前播放的音乐
     *
     * @return
     */
    public MusicModel getCurrentMusic() {
        if (musicService == null) {
            return null;
        }
        return musicService.getCurrentMusic();
    }

    /**
     * @param position
     */
    public void seekTo(long position) {
        if (musicService == null) {
            return;
        }
        LogUtils.log("music seekto position=" + position);
        musicService.seekto(position);
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
