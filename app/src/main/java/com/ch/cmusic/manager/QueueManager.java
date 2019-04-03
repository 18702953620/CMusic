package com.ch.cmusic.manager;

import com.ch.cmusic.LogUtils;
import com.ch.cmusic.model.MusicModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 作者： ch
 * 时间： 2019/3/22 0022-下午 3:35
 * 描述： 播放队列 管理器
 * 来源：
 */

public class QueueManager {

    private static QueueManager INSTANCE;


    private List<MusicModel> musicModelList;
    private int currentPosition;
    /**
     * 顺序播放
     */
    public static final int MODEL_ORDER = 0;
    /**
     * 单曲循环
     */
    public static final int MODEL_SINGLE = 1;
    /**
     * 随机
     */
    public static final int MODEL_RANDOM = 2;

    /**
     * 播放模式
     */
    private int playModel;

    public static QueueManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new QueueManager();
        }
        return INSTANCE;
    }

    public int getPlayModel() {
        return playModel;
    }

    public void setPlayModel(int playModel) {
        this.playModel = playModel;
    }

    private void checkNull() {
        if (musicModelList == null) {
            musicModelList = new ArrayList<>();
        }
    }

    public List<MusicModel> getMusicList() {
        return musicModelList;
    }

    /**
     * 添加
     *
     */
    public void addMusic(MusicModel model) {
        checkNull();
        int position = musicModelList.indexOf(model);
        if (position == -1) {
            musicModelList.add(model);
            currentPosition = musicModelList.indexOf(model);
        } else {
            currentPosition = position;
        }
        LogUtils.log("addmuic to queue,currentPosition=" + currentPosition);
    }

    /**
     * 添加
     *
     */
    public void addMusic(List<MusicModel> list) {
        checkNull();
        musicModelList.addAll(list);
    }

    /**
     * 上一个
     *
     */
    public MusicModel before() {
        checkNull();
        int size = musicModelList.size();
        if (size == 0) {
            currentPosition = 0;
            return null;
        }
        if (playModel == MODEL_ORDER) {
            //顺序
            if (currentPosition - 1 >= 0 && currentPosition - 1 < size) {
                currentPosition--;
                return musicModelList.get(currentPosition);
            }
        } else if (playModel == MODEL_SINGLE) {
            //单曲
            return musicModelList.get(currentPosition);
        } else if (playModel == MODEL_RANDOM) {
            //随机
            currentPosition = new Random().nextInt(size);
            return musicModelList.get(currentPosition);
        }

        return null;
    }


    /**
     * 下一个
     *
     */
    public MusicModel next() {
        checkNull();
        int size = musicModelList.size();
        if (size == 0) {
            currentPosition = 0;
            return null;
        }
        if (playModel == MODEL_ORDER) {
            //顺序
            if (size > currentPosition + 1) {
                currentPosition++;
                return musicModelList.get(currentPosition);
            }
        } else if (playModel == MODEL_SINGLE) {
            //单曲
            return musicModelList.get(currentPosition);
        } else if (playModel == MODEL_RANDOM) {
            //随机
            currentPosition = new Random().nextInt(size);
            return musicModelList.get(currentPosition);
        }


        return null;
    }

    /**
     * 清空
     */
    public void clear() {
        checkNull();
        musicModelList.clear();
    }
}
