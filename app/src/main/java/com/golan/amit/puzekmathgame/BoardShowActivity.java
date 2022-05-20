package com.golan.amit.puzekmathgame;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class BoardShowActivity extends AppCompatActivity implements View.OnTouchListener {

    ImageView ivBoardTable;
    Animation scaleAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_show);

        ivBoardTable = (ImageView)findViewById(R.id.ivBoardTableId);
        ivBoardTable.setOnTouchListener(this);
        scaleAnim = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        ivBoardTable.startAnimation(scaleAnim);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(MainActivity.DEBUGTAG, "finishing multiplication board display page");
        finish();
        return true;
    }
}
