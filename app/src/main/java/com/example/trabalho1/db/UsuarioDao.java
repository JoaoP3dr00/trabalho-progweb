package com.example.trabalho1.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.trabalho1.model.Usuario;

@Dao
public interface UsuarioDao {

    @Insert
    void inserir(Usuario usuario);

    @Query("SELECT * FROM usuario WHERE email = :email AND senha = :senha LIMIT 1")
    Usuario login(String email, String senha);

    @Query("SELECT * FROM usuario WHERE email = :email LIMIT 1")
    Usuario buscarPorEmail(String email);

    @Query("UPDATE usuario SET senha = :novaSenha WHERE email = :email")
    void alterarSenha(String email, String novaSenha);
}
