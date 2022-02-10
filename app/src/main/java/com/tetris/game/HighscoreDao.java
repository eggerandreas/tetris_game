package com.tetris.game;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface HighscoreDao {

    @Insert
    public void insertScore(Highscore highscore);

    @Update
    public void updateScore(Highscore highscore);

    @Delete
    public void remove(Highscore highscore);

    @Query("SELECT * FROM Highscore")
    public List<Highscore> getAllHighscores();

}