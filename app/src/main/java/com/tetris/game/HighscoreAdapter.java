package com.tetris.game;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HighscoreAdapter extends RecyclerView.Adapter<HighscoreAdapter.HighscoreViewHolder> {

    public static class HighscoreViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView score;

        public HighscoreViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_name);
            score = itemView.findViewById(R.id.list_score);
        }
    }

    protected List<Highscore> highscoreList;
    protected HighscoreDao dao;

    public HighscoreAdapter(HighscoreDao dao) {
        this.dao = dao;
    }

    public void reload() {
        this.highscoreList = dao.getAllHighscores();
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HighscoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.highscore, parent, false);
        return new HighscoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HighscoreViewHolder holder, int position) {
        Highscore highscore = highscoreList.get(position);
        holder.name.setText(highscore.name);
        holder.score.setText(highscore.score);
    }

    @Override
    public int getItemCount() {
        return highscoreList.size();
    }

}

