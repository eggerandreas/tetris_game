package com.tetris.game;

import android.content.Context;
import androidx.room.Room;

public class DatabaseUtils {

    protected static AppDatabase instance;

    public static AppDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "ranking")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

}