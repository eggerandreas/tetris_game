package com.tetris.game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    DrawView drawView;
    GameState gameState;
    RelativeLayout gameButtons;
    Button left;
    Button right;
    Button rotateAc;
    FrameLayout game;
    Button pause;
    TextView score;
    Handler handler;
    Runnable loop;
    int delayFactor;
    int delay;
    int delayLowerLimit;
    Intent i;
    int tempScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tempScore = 0;
        gameState = new GameState(24, 20, TetrisFigureType.getRandomTetrisFigure());

        drawView = new DrawView(this, gameState);
        drawView.setBackgroundColor(Color.WHITE);

        game = new FrameLayout(this);
        gameButtons = new RelativeLayout(this);



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



        // create the used Buttons
        left = new Button(this);
        left.setText(R.string.left);
        left.setId(R.id.left);

        right = new Button(this);
        right.setText(R.string.right);
        right.setId(R.id.right);

        rotateAc = new Button(this);
        rotateAc.setText(R.string.rotate_ac);
        rotateAc.setId(R.id.rotate_ac);

        pause = new Button(this);
        pause.setText(R.string.pause);
        pause.setId(R.id.pause);

        score = new TextView(this);
        score.setText(R.string.score);
        score.setId(R.id.score);
        score.setTextSize(20);


        // create the Layouts
        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams leftButton = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams rightButton = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams downButton = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams pausebutton = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams scoretext = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        // set and add the layouts / views
        gameButtons.setLayoutParams(rl);
        gameButtons.addView(left);
        gameButtons.addView(right);
        gameButtons.addView(rotateAc);
        gameButtons.addView(pause);
        gameButtons.addView(score);

        // add some Rules
        leftButton.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        leftButton.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        rightButton.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        rightButton.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        downButton.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        downButton.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        pausebutton.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        pausebutton.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

        scoretext.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        scoretext.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

        left.setLayoutParams(leftButton);
        right.setLayoutParams(rightButton);
        rotateAc.setLayoutParams(downButton);
        pause.setLayoutParams(pausebutton);
        score.setLayoutParams(scoretext);

        game.addView(drawView);
        game.addView(gameButtons);
        setContentView(game);

        View leftButtonListener = findViewById(R.id.left);
        leftButtonListener.setOnClickListener(this);

        View rightButtonListener = findViewById(R.id.right);
        rightButtonListener.setOnClickListener(this);

        View rotateACButtonListener = findViewById(R.id.rotate_ac);
        rotateACButtonListener.setOnClickListener(this);

        View pauseButtonListener = findViewById(R.id.pause);
        pauseButtonListener.setOnClickListener(this);



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
                            ++tempScore;

                        }
                        drawView.invalidate();
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

        } else if (action == rotateAc) {
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