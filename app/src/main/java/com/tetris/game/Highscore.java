package com.tetris.game;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.time.LocalDateTime;

@Entity
public class Highscore {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String score;

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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

}
