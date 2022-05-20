package com.golan.amit.puzekmathgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    public static final String DEBUGTAG = "MGAG";
    public static final boolean DEBUG = false;
    ImageView mainIv;
    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init() {
        mainIv = (ImageView)findViewById(R.id.mainPicId);
        mainIv.setOnTouchListener(this);

        anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        mainIv.startAnimation(anim);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Intent intent = new Intent(this, GameMainActivity.class);
        startActivity(intent);
        return true;
    }
}
