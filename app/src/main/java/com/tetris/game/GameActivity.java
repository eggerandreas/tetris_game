package com.tetris.game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public static GameState gameState = new GameState(24, 20, TetrisFigureType.getRandomTetrisFigure());
    private TetrisView tetrisView;
    private Button left;
    private Button right;
    private Button turn;
    private Button pause;
    private TextView score;
    private Handler handler;
    private Runnable loop;
    private int delayFactor;
    private int delay;
    private int delayLowerLimit;
    private int tempScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tempScore = 0;

        // config the used views
        tetrisView = findViewById(R.id.tetris_view);
        left = findViewById(R.id.button_left);
        turn = findViewById(R.id.button_turn);
        right = findViewById(R.id.button_right);
        pause = findViewById(R.id.button_pause);
        score = findViewById(R.id.game_score);

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

                    // set some delay
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // intent with score value to game over activity
                            Intent i = new Intent(getBaseContext(), GameOverActivity.class);
                            i.putExtra("hallo", tempScore);
                            startActivity(i);

                        }
                    }, 3000);

                }
            }

        };
        loop.run();

    }


    @Override
    public void onClick(View action) {
        if (action == left) {
            gameState.moveFallingTetrisFigureLeft();

        } else if (action == right) {
            gameState.moveFallingTetrisFigureRight();

        } else if (action == turn) {
            gameState.rotateFallingTetrisFigureAntiClock();

        } else if (action == pause) {
            if (gameState.status) {
                if (gameState.pause) {
                    gameState.pause = false;
                    pause.setText(R.string.pause);

                } else {
                    pause.setText(R.string.play);
                    gameState.pause = true;

                }
            }
        }
    }
}