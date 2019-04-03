package com.ch.cmusic.recever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.ch.cmusic.service.MusicService;

/**
 * 作者： ch
 * 时间： 2019/4/3 0003-上午 9:57
 * 描述： 拔出耳机监听
 * 来源：
 */

public class BecomingNoisyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        switch (action) {
            case AudioManager.ACTION_AUDIO_BECOMING_NOISY:
                Intent musicIntent = new Intent(context, MusicService.class);
                musicIntent.setAction(MusicService.ACTION_PAUSE);
                context.startService(musicIntent);
                break;
        }

    }
}
