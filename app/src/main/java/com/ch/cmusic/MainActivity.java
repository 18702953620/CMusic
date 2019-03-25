package com.ch.cmusic;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ch.cmusic.adapter.MusicAdapter;
import com.ch.cmusic.dialog.MusicQueueDialog;
import com.ch.cmusic.manager.MusicManager;
import com.ch.cmusic.manager.QueueManager;
import com.ch.cmusic.model.MusicModel;
import com.ch.cmusic.weigit.MarqueeTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.rv_music)
    RecyclerView rvMusic;
    @BindView(R.id.tv_play)
    MarqueeTextView btnPlay;
    private List<MusicModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        addListener();
        MusicManager.getInstance().bindPlayService();
        MusicManager.getInstance().startService();
        QueueManager.getInstance().setPlayModel(QueueManager.MODEL_RANDOM);

    }

    private void addListener() {
        btnPlay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new MusicQueueDialog().show(getSupportFragmentManager(), "MusicQueueDialog");

                return true;
            }
        });
    }

    private void init() {
        list = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        list.add(new MusicModel(path + "/MIUI/music/mp3/Cool_Tobu_Good Life EDM.mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/Find U_吴俊达_Find U.mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/FOR REAL_杨美娜_FOR REAL.mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/Good Times_Tobu_Good Times.mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/Hope (Original Mix)_Tobu_Hope.mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/HYPE (HM prod)_Bikabreezy_HYPE (HM prod).mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/kisskisskiss_拾音社_kisskisskiss.mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/La Playa_MT Boxer_La Playa.mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/Lights_Axero_Outertone 005 - The Void.mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/loss of bone_Thunse_loss of bone.mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/Mesmerize_Tobu_Mesmerize.mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/River_Axero_River.mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/Seven_Tobu_Seven.mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/Wild_Monogem_100% - EP.mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/一起来 (电影《燃烧的夏之龙虎斗》推广曲)_黄宇哲_一起来.mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/健康中国_马锐_健康中国.mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/南宁不夜_杨历川 & 房少强 & 李天臻 & 鲨豚兄弟_南宁不夜.mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/孤独症的猫_熊汝霖_孤独症的猫.mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/我爱你 (Prod.王晓夫+St.K4N3+2Majik)_贺仙人_我爱你 (Prod.王晓夫+St.K4N3+2Majik).mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/我要把你们都变成吃货_张靖怡_我要把你们都变成吃货.mp3"));
        list.add(new MusicModel(path + "/MIUI/music/mp3/梦 (《嘿！好样的》主题曲)_金玟岐_梦.mp3"));

        final MusicAdapter musicAdapter = new MusicAdapter(list);
        rvMusic.setLayoutManager(new LinearLayoutManager(this));
        rvMusic.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvMusic.setAdapter(musicAdapter);

        MusicManager.getInstance().addUpdataListener(new MusicManager.MusicUpdataListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

            }

            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {

            }

            @Override
            public void onProgress(int progress, int duration) {

            }

            @Override
            public void startPlay(MusicModel model) {
                btnPlay.setText(model.getTitle());
            }

            @Override
            public void realPlay(MusicModel model) {

            }

            @Override
            public void onPause() {

            }
        });

        musicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MusicManager.getInstance().play(list.get(position));
                musicAdapter.notifyDataSetChanged();
                Log.e("cheng", "play");
            }
        });

    }

    @Override
    protected void onDestroy() {
        MusicManager.getInstance().quit();
        super.onDestroy();

    }

    @OnClick(R.id.tv_play)
    public void onViewClicked() {
        startActivity(new Intent(this, DetailActivity.class));
    }
}
