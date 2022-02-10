package com.tetris.game;

import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DatatypeConverter {

    @TypeConverter
    public static LocalDateTime fromTimestamp(Long timestamp) {
        if (timestamp != null) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        } else {
            return null;
        }
    }

    @TypeConverter
    public static Long fromDate(LocalDateTime date) {
        if (date != null) {
            return date.toInstant(ZoneOffset.UTC).toEpochMilli();
        } else {
            return null;
        }
    }
}