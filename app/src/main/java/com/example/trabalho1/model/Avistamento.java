package com.example.trabalho1.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "avistamento")
public class Avistamento {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String especie;
    public String local;
    public String data;
    public String descricao;
}
