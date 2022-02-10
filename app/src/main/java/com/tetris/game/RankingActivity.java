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

        db = DatabaseUtils.getAppDatabase(this.getApplicationContext());
        adapter = new HighscoreAdapter(db.getHighscoreDao());

        RecyclerView list = findViewById(R.id.task_list);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.go_back, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_back:
                Intent i = new Intent(this,MainActivity.class);
                this.startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        adapter.reload();
        super.onStart();
    }
}