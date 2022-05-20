package com.golan.amit.puzekmathgame;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.golan.amit.puzekmathgame.helper.DatabaseHelper;
import com.golan.amit.puzekmathgame.helper.MathGameHelper;

public class GameMainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    TextView tvMainData, tvSummary;
    EditText etDiv, etMod;
    Button btnSubmit, btnFinish, btnHint, btnDisplayBoard;
    MathGameHelper mgh;
    private DatabaseHelper db = new DatabaseHelper(this);
    SoundPool sp;
    int applause_sp, punch_sp;   //  sound id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);

        init();
        play();
    }

    private void init() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME).build();
            sp = new SoundPool.Builder().setMaxStreams(10)
                    .setAudioAttributes(aa).build();
        } else {
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }
        applause_sp = sp.load(this, R.raw.applause, 1);
        punch_sp = sp.load(this, R.raw.punch, 1);

        tvMainData = (TextView) findViewById(R.id.tvMathDataId);
        etDiv = (EditText) findViewById(R.id.etDivId);
        etMod = (EditText) findViewById(R.id.etModId);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        btnFinish = (Button) findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(this);
        btnFinish.setOnLongClickListener(this);
        btnHint = (Button) findViewById(R.id.btnHint);
        btnHint.setOnClickListener(this);
        btnDisplayBoard = (Button)findViewById(R.id.btnDisplayBoard);
        btnDisplayBoard.setOnClickListener(this);
        tvSummary = (TextView) findViewById(R.id.tvSummaryId);

        mgh = new MathGameHelper();

        int currHighScore = db.getHighScore();
        if(MainActivity.DEBUG) {
            Log.d(MainActivity.DEBUGTAG, String.format("current fetched high score is: %d", currHighScore));
        }
        mgh.setCurrent_high_score(currHighScore);


    }

    private void play() {
        if (mgh.getRounds() == MathGameHelper.ROUNDS) {
            displaySummaryStats();
            if(mgh.getScore() > mgh.getCurrent_high_score()) {
                if(MainActivity.DEBUG) {
                    Log.d(MainActivity.DEBUGTAG, String.format("Great. new high score %d is set", mgh.getScore()));
                }
                db.insertHighScore(mgh.getScore());
                Intent intent = new Intent(this, NewRecordActivity.class);
                startActivity(intent);
            }
            displayDialog();
        }
        etDiv.setText("");
        etMod.setText("");
        etDiv.requestFocus();
        for(int i = 0; i <2; i++) {
            mgh.generateRandomValues();
//            tvMainData.setText(String.format("%d mod %d = ?", mgh.getOne(), mgh.getTwo()));
            tvMainData.setText(String.format("מה השארית של %d חלקי %d ?", mgh.getOne(), mgh.getTwo()));
//            SystemClock.sleep(500);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit) {
            int divInt, modInt;
            String divStr = null, modStr = null;
            try {
                if(etDiv.getText() == null) {
                    divInt = 0;
                }
                else {
                    divStr = etDiv.getText().toString();
                    if(divStr == null || divStr == "") {
                        divInt = 0;
                    } else {
                        divInt = Integer.parseInt(divStr);
                    }
                }
                if(etMod.getText() == null) {
                    modInt = 0;
                } else {
                    modStr = etMod.getText().toString();
                    if (modStr == null || modStr == "") {
                        modInt = 0;
                    } else {
                        modInt = Integer.parseInt(modStr);
                    }
                }
            } catch (Exception e) {
                Log.e(MainActivity.DEBUGTAG, "Exception while parsing user data");
                Toast.makeText(this, "תו לא חוקי", Toast.LENGTH_SHORT).show();
                return;
            }

            /**
             * SUCCESS!!
             */
            if (divInt == mgh.getDivRes() && modInt == mgh.getModRes()) {

                /**
                 * Applause
                 */

                sp.play(applause_sp, 1,1,0,0,1);
                v.setAlpha(1);

                Toast.makeText(this, R.string.great, Toast.LENGTH_LONG).show();
                mgh.increaseRounds();
                mgh.increaseSuccesses();
                mgh.addToScore(mgh.getSuccesses() * MathGameHelper.SCORE_INCREASE_FACTOR);
                String tmpDisplayScore = String.format("score: %d", mgh.getScore());

                if(mgh.getCurrent_high_score() > 0) {
                    tmpDisplayScore += String.format(", high score: %d", mgh.getCurrent_high_score());
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(MainActivity.DEBUG) {
                        Log.d(MainActivity.DEBUGTAG, "Setting info-style");
                    }
                    tvSummary.setTextAppearance(R.style.info_style);    //  TODO find why not working
                }
                tvSummary.setText(tmpDisplayScore);
                /**
                 * WON THE GAME
                 */

                play();
            } else {
                mgh.increaseAttempts();
                mgh.increaseTotalAttempts();
                mgh.subtractFromScore(mgh.getTotalAttempts() * MathGameHelper.SCORE_DECREASE_FACTOR);
                if (mgh.getAttempts() > MathGameHelper.ATTEMPTS) {
                    /***
                     * punch sound
                     */

                    sp.play(punch_sp, 1,1,0,0,1);
                    v.setAlpha(1);

                    Toast.makeText(this, "טעות. עוברים לתרגיל הבא.", Toast.LENGTH_LONG).show();
                    mgh.resetAttempts();
                    mgh.increaseRounds();
                    mgh.increaseFailCount();
                    mgh.subtractFromScore(mgh.getFailCount() * MathGameHelper.SCORE_DECREASE_FACTOR_FAIL);
                    play();
                } else {
                    Toast.makeText(this, R.string.wrong, Toast.LENGTH_SHORT).show();
                }
            }

            if(MainActivity.DEBUG) {
                Log.d(MainActivity.DEBUGTAG, String.format("rounds: %d, good: %d, wrong: %d, attempts: %d, score: %d",
                        mgh.getRounds(), mgh.getSuccesses(), mgh.getFailCount(), mgh.getTotalAttempts(), mgh.getScore()));
            }

        } else if (v == btnFinish) {
            this.finishAffinity();
        } else if (v == btnHint) {
            if (mgh.getHints() < MathGameHelper.HINTS) {
                mgh.subtractFromScore((MathGameHelper.SCORE_DECREASE_FACTOR -2));
                String tmpDivResStr = String.format("תוצאת החלוקה היא %d", mgh.getDivRes());
//                Toast.makeText(this, "Division result: " + mgh.getDivRes(), Toast.LENGTH_LONG).show();
                Toast.makeText(this, tmpDivResStr, Toast.LENGTH_LONG).show();
                mgh.increaseHints();
            } else {
                Toast.makeText(this, "נגמרו הרמזים. ניתן להשתמש בלוח הכפל.", Toast.LENGTH_SHORT).show();
                btnDisplayBoard.setVisibility(View.VISIBLE);
                btnHint.setVisibility(View.INVISIBLE);
            }
        } else if(v == btnDisplayBoard) {
            mgh.subtractFromScore((MathGameHelper.SCORE_DECREASE_FACTOR -1));
            Intent i = new Intent(this, BoardShowActivity.class);
            startActivity(i);
            if(MainActivity.DEBUG) {
                Log.d(MainActivity.DEBUGTAG, "moving to display multiplication board");
            }
        }
    }

    private void displaySummaryStats() {
        String statStr = String.format("\n\nGood answers: %d\nWrong answers: %d\nTotal fail attempts: %d, Total SCORE: %d",
                mgh.getSuccesses(), mgh.getFailCount(), mgh.getTotalAttempts(), mgh.getScore());
        tvSummary.setText(statStr);
    }

    private void displayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(GameMainActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GameMainActivity.this.finishAffinity();
            }
        });
        builder.setMessage("Game Over, Click Yes for another game");
        builder.setTitle("Game over");
        final AlertDialog dlg = builder.create();
        dlg.show();
    }

    @Override
    public boolean onLongClick(View v) {
        if(v == btnFinish) {
            try {
                db.insertHighScore(0);
                if(MainActivity.DEBUG) {
                    Log.d(MainActivity.DEBUGTAG, "Reset high score successfully");
                }
                Toast.makeText(this, "reset high-score to 0", Toast.LENGTH_LONG).show();
                mgh.setCurrent_high_score(0);
                return true;
            } catch (Exception ei) {
                Log.e(MainActivity.DEBUGTAG, "Reset high score failed");
                Toast.makeText(this, "reset high-score to 0 failed", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }
}
