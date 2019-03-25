package com.ch.cmusic.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;

import com.ch.cmusic.R;
import com.ch.cmusic.manager.MusicManager;
import com.ch.cmusic.model.MusicModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 作者： ch
 * 时间： 2019/3/22 0022-上午 11:35
 * 描述：
 * 来源：
 */

public class MusicAdapter extends BaseQuickAdapter<MusicModel, BaseViewHolder> {


    public MusicAdapter(@Nullable List<MusicModel> data) {
        super(R.layout.item_music, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MusicModel item) {

        MusicModel current = MusicManager.getInstance().getCurrentMusic();
        if (current != null && current.getPath().equals(item.getPath())) {
            helper.setTextColor(R.id.tv_title, Color.parseColor("#008577"));
            helper.setTextColor(R.id.tv_path, Color.parseColor("#008577"));
            if (getRecyclerView() != null) {
                getRecyclerView().smoothScrollToPosition(helper.getAdapterPosition());
            }
        } else {
            helper.setTextColor(R.id.tv_title, Color.parseColor("#333333"));
            helper.setTextColor(R.id.tv_path, Color.parseColor("#333333"));
        }

        String title = item.getPath().substring(item.getPath().lastIndexOf("/") + 1);
        helper.setText(R.id.tv_title, title);
        helper.setText(R.id.tv_path, item.getPath());

    }

}
