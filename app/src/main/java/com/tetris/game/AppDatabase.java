package com.tetris.game;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Highscore.class}, version = 1)
@TypeConverters({DatatypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract HighscoreDao getHighscoreDao();
}
