package com.tetris.game;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Highscore {

    @PrimaryKey(autoGenerate = true)
    protected int id;
    protected String name;
    protected int score;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
