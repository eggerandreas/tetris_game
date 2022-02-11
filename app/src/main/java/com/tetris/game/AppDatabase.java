package com.tetris.game;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Highscore.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HighscoreDao getHighscoreDao();
}
