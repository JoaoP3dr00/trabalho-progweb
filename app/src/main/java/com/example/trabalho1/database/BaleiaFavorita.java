package com.example.trabalho1.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "baleias_favoritas")
public class BaleiaFavorita {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int usuarioId;
    public int baleiaResId;
}
