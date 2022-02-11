package com.tetris.game;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public static GameState gameState = new GameState(24, 20, TetrisFigureType.getRandomTetrisFigure());
    private TetrisView tetrisView;
    private ImageButton left;
    private ImageButton right;
    private ImageButton turn;
    private Button pause;
    private TextView score;
    private Handler handler;
    private Runnable loop;
    private int delayFactor;
    private int delay;
    private int delayLowerLimit;
    private int tempScore;
    MediaPlayer tetris_sound;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tetris_sound= MediaPlayer.create(GameActivity.this, R.raw.tetris);
        tetris_sound.setLooping(true); // Set looping
        tetris_sound.setVolume(100, 100);
        tetris_sound.start();

        // hide Action Bar
        getSupportActionBar().hide();

        // score for intent to GameOver activity
        tempScore = 0;

        // config the used views
        tetrisView = findViewById(R.id.tetris_view);

        // button move left
        left = findViewById(R.id.button_left);

        // button turn
        turn = findViewById(R.id.button_turn);

        // button turn right
        right = findViewById(R.id.button_right);

        pause = findViewById(R.id.button_pause);
        score = findViewById(R.id.game_score);


        // set dark / light mode colors to buttons
        int nightModeFlags =
                getApplicationContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                left.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.wave));
                right.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.wave));
                turn.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.wave));
                pause.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.ocean));
                pause.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.wave));
                break;

            case Configuration.UI_MODE_NIGHT_NO:

                left.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.deep_aqua));
                right.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.deep_aqua));
                turn.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.deep_aqua));
                pause.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.wave));
                pause.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

            default:
                break;
        }


        // set onclicklisteners
        left.setOnClickListener(this);
        turn.setOnClickListener(this);
        right.setOnClickListener(this);
        pause.setOnClickListener(this);

        delay = 500;
        delayLowerLimit = 200;
        delayFactor = 2;

        // get the intent with the difficulty information from main activity
        Intent i = getIntent();
        int difficulty = i.getIntExtra("difficulty", 0);

        // set the speed of the game based on the difficulty
        if (difficulty == 2 && !gameState.difficultMode) {
            delay = delay/delayFactor;
            gameState.difficultMode = true;
        } else {
            delay = delay * delayFactor;
            gameState.difficultMode = false;
        }
        
        handler = new Handler(Looper.getMainLooper());
        loop = new Runnable() {
            public void run() {
                if (gameState.status) {
                    if (!gameState.pause) {
                        boolean success = gameState.moveFallingTetrisFigureDown();
                        if (!success) {
                            gameState.paintTetrisFigure(gameState.falling);
                            gameState.lineRemove();

                            gameState.pushNewTetrisFigure(TetrisFigureType.getRandomTetrisFigure());

                            if (gameState.score % 10 == 9 && delay >= delayLowerLimit) {
                                delay = delay / delayFactor + 1;
                            }
                            gameState.incrementScore();

                            // update score
                            ++tempScore;
                            String stringScore = Integer.toString(gameState.score);
                            score.setText(stringScore);


                        }
                        tetrisView.invalidate();
                    }
                    handler.postDelayed(this, delay);
                } else {

                    // alert when game over
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameActivity.this);
                    alertDialogBuilder.setTitle("Game Over");
                    alertDialogBuilder.setIcon(R.drawable.ic_game_over);
                    alertDialogBuilder.setMessage("You made a good game!");
                    alertDialogBuilder.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {

                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent i = new Intent(getBaseContext(), GameOverActivity.class);
                            i.putExtra("hallo", tempScore);
                            tetris_sound.stop();
                            tetris_sound.release();
                            startActivity(i);
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
            }

        };
        loop.run();

    }



    @Override
    public void onClick(View action) {
        if (action == left) {

            // change color when click
            gameState.moveFallingTetrisFigureLeft();

        } else if (action == right) {

            // change color when click
            gameState.moveFallingTetrisFigureRight();

        } else if (action == turn) {

            // change color when click
            gameState.rotateFallingTetrisFigureAntiClock();

        } else if (action == pause) {
            if (gameState.status) {
                if (gameState.pause) {
                    gameState.pause = false;
                    pause.setText(R.string.pause);
                    tetris_sound.start();

                } else {
                    pause.setText(R.string.play);
                    gameState.pause = true;
                    tetris_sound.pause();

                }
            }
        }
    }
}