package com.tetris.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RankingActivity extends AppCompatActivity {

    protected AppDatabase db;
    protected HighscoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        getSupportActionBar().setTitle("Highscores");

        db = DatabaseUtils.getAppDatabase(this.getApplicationContext());
        adapter = new HighscoreAdapter(db.getHighscoreDao());

        RecyclerView list = findViewById(R.id.score_list);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        adapter.reload();
        super.onStart();
    }
}