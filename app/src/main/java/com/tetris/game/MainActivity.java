package com.tetris.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int DIFFICULTY_EASY = 1;
    public static final int DIFFICULTY_HARD = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.easy_menu_button).setOnClickListener(this);
        findViewById(R.id.hard_menu_button).setOnClickListener(this);
        findViewById(R.id.ranking_button).setOnClickListener(view -> {
            Intent intent = new Intent(this, RankingActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onClick(View view) {
        int difficulty = 0;

        switch (view.getId()){
            case R.id.easy_menu_button:
                difficulty = DIFFICULTY_EASY;
                break;
            case R.id.hard_menu_button:
                difficulty = DIFFICULTY_HARD;
                break;
        }

        Intent StartGame = new Intent(this, GameActivity.class);
        StartGame.putExtra("difficulty", difficulty);
        startActivity(StartGame);
    }
}