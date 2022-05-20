package com.golan.amit.puzekmathgame;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class NewRecordActivity extends AppCompatActivity implements View.OnTouchListener {

    ImageView ivNewRecord;
    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);

        ivNewRecord = (ImageView) findViewById(R.id.ivNewRecordId);
        ivNewRecord.setOnTouchListener(this);

        anim = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        ivNewRecord.startAnimation(anim);
//        displayAndGo();
    }

    private void displayAndGo() {
        SystemClock.sleep(4000);
        finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        finish();
        return false;
    }
}
