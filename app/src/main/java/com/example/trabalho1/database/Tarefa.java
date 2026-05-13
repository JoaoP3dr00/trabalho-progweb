package com.example.trabalho1.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tarefas")
public class Tarefa {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String titulo;
    public String descricao;
}
