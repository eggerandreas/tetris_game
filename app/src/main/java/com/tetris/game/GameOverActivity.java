package com.tetris.game;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;


@RequiresApi(api = Build.VERSION_CODES.O)
public class GameOverActivity extends AppCompatActivity implements View.OnClickListener {

    protected AppDatabase db;
    protected TextInputEditText name;
    protected Highscore highscore;
    protected String ranking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        db = DatabaseUtils.getAppDatabase(this.getApplicationContext());

        // String score = getIntent().getIntExtra("hallo", GameActivity.tempScore);
        //TextView GameOverTxt = (TextView) findViewById(R.id.score);
        //GameOverTxt.setText("YOUR SCORE: " + tempScore);
        //ranking = score;
        //findViewById(R.id.button_save).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        highscore.name = name.getText().toString();
        highscore.score = ranking;

        db.getHighscoreDao().insertScore(highscore);

        Intent BackToMenu = new Intent(this, MainActivity.class);
        startActivity(BackToMenu);
    }
}
