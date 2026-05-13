package com.example.trabalho1.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AppDao {
    // --- Operações de Usuário ---
    @Insert
    void inserirUsuario(Usuario usuario);

    @Update
    void atualizarUsuario(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE email = :email AND senha = :senha LIMIT 1")
    Usuario logarUsuario(String email, String senha);

    // --- Operações de Tarefa ---
    @Insert
    void inserirTarefa(Tarefa tarefa);

    @Update
    void atualizarTarefa(Tarefa tarefa);

    @Delete
    void deletarTarefa(Tarefa tarefa);

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    Usuario buscarPorEmail(String email);

    @Query("SELECT * FROM tarefas WHERE id = :id LIMIT 1")
    Tarefa buscarTarefaPorId(int id);

    @Query("SELECT * FROM tarefas")
    List<Tarefa> listarTarefas();

    @Query("SELECT * FROM tarefas WHERE titulo LIKE :busca")
    List<Tarefa> buscarTarefa(String busca);
}
