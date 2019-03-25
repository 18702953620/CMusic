package com.ch.cmusic;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ch.cmusic.dialog.MusicQueueDialog;
import com.ch.cmusic.manager.MusicManager;
import com.ch.cmusic.manager.QueueManager;
import com.ch.cmusic.model.MusicModel;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.ll_down)
    LinearLayout llDown;
    @BindView(R.id.iv_audio_bg)
    ImageView ivAudioBg;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.sb_audio)
    IndicatorSeekBar sbAudio;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.iv_previous)
    ImageView ivPrevious;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.under_view)
    View underView;
    @BindView(R.id.tv_play_list)
    TextView tvPlayList;
    @BindView(R.id.ll_play)
    LinearLayout llPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        initView();
        addListener();
    }

    private void addListener() {
        MusicManager.getInstance().addUpdataListener(new MusicManager.MusicUpdataListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

            }

            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {

            }

            @Override
            public void onProgress(int progress, int duration) {
                sbAudio.setMax(duration);
                sbAudio.setProgress(progress);
            }


            @Override
            public void startPlay(MusicModel model) {
                tvTitle.setText(model.getTitle());
                ivPlay.setBackgroundResource(R.mipmap.audio_detail_play);
            }

            @Override
            public void realPlay(MusicModel model) {
                tvTitle.setText(model.getTitle());
                ivPlay.setBackgroundResource(R.mipmap.audio_detail_play);
            }

            @Override
            public void onPause() {
                ivPlay.setBackgroundResource(R.mipmap.audio_detail_stop);
            }
        });
        sbAudio.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                MusicManager.getInstance().seekTo(seekBar.getProgress());
            }
        });

    }

    private void initView() {
        if (MusicManager.getInstance().isPlaying()) {
            MusicModel current = MusicManager.getInstance().getCurrentMusic();
            tvTitle.setText(current.getTitle());
            ivPlay.setBackgroundResource(R.mipmap.audio_detail_play);

        }


    }

    @OnClick({R.id.ll_down, R.id.iv_previous, R.id.iv_play, R.id.iv_next, R.id.tv_play_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_down:
                finish();
                break;
            case R.id.iv_previous:
                MusicModel musicModel1 = QueueManager.getInstance().before();
                if (musicModel1 != null) {
                    MusicManager.getInstance().play(musicModel1);
                } else {
                    Toast.makeText(DetailActivity.this, "已经是第一个了", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_play:
                if (MusicManager.getInstance().isPlaying()) {
                    MusicManager.getInstance().pause();
                } else {
                    MusicManager.getInstance().start();
                }
                break;
            case R.id.iv_next:
                MusicModel musicModel = QueueManager.getInstance().next();
                if (musicModel != null) {
                    MusicManager.getInstance().play(musicModel);
                } else {
                    Toast.makeText(DetailActivity.this, "已经是最后一个了", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_play_list:
                new MusicQueueDialog().show(getSupportFragmentManager(), "MusicQueueDialog");
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.exit_from_bottom);

    }
}
