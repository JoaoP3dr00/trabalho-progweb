package com.example.trabalho1.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuario")
public class Usuario {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String nome;
    public String email;
    public String senha;
    public String foto;
}
