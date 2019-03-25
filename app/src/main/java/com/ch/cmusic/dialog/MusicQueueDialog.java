package com.ch.cmusic.dialog;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ch.cmusic.R;
import com.ch.cmusic.adapter.MusicAdapter;
import com.ch.cmusic.base.FM_Base_Dialog;
import com.ch.cmusic.manager.QueueManager;

import butterknife.BindView;

/**
 * 作者： ch
 * 时间： 2019/3/22 0022-下午 5:31
 * 描述：
 * 来源：
 */

public class MusicQueueDialog extends FM_Base_Dialog {
    @BindView(R.id.rv_queue)
    RecyclerView rvQueue;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_music_queue;
    }

    @Override
    protected void initView() {
        MusicAdapter musicAdapter = new MusicAdapter(QueueManager.getInstance().getMusicList());
        rvQueue.setLayoutManager(new LinearLayoutManager(context));
        rvQueue.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        musicAdapter.bindToRecyclerView(rvQueue);
        rvQueue.setAdapter(musicAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        fixHeight();
    }
}
