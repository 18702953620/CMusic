package com.ch.cmusic.model;

import android.text.TextUtils;

/**
 * 作者： ch
 * 时间： 2019/3/22 0022-下午 3:36
 * 描述：
 * 来源：
 */

public class MusicModel {

    private String path;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MusicModel(String path) {
        this.path = path;
        if (!TextUtils.isEmpty(path) && path.contains("/")) {
            this.title = path.substring(path.lastIndexOf("/") + 1);
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
