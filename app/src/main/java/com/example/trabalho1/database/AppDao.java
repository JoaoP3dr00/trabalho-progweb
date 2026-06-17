package com.example.trabalho1.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AppDao {
    @Insert
    void inserirUsuario(Usuario usuario);

    @Update
    void atualizarUsuario(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE email = :email AND senha = :senha LIMIT 1")
    Usuario logarUsuario(String email, String senha);

    @Insert
    void inserirTarefa(Tarefa tarefa);

    @Update
    void atualizarTarefa(Tarefa tarefa);

    @Delete
    void deletarTarefa(Tarefa tarefa);

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    Usuario buscarPorEmail(String email);

    @Query("SELECT * FROM usuarios")
    List<Usuario> listarTodosUsuarios();

    @Query("SELECT COUNT(*) FROM usuarios")
    int contarUsuarios();

    @Query("SELECT COUNT(*) FROM tarefas")
    int contarTarefas();

    @Query("SELECT COUNT(*) FROM baleias_favoritas")
    int contarFavoritos();

    @Delete
    void deletarUsuario(Usuario usuario);

    @Query("SELECT * FROM tarefas WHERE id = :id LIMIT 1")
    Tarefa buscarTarefaPorId(int id);

    @Query("SELECT * FROM tarefas WHERE usuarioId = :userId")
    List<Tarefa> listarTarefasPorUsuario(int userId);

    @Query("SELECT * FROM tarefas WHERE usuarioId = :userId AND titulo LIKE :busca")
    List<Tarefa> buscarTarefaPorUsuario(int userId, String busca);

    @Query("SELECT * FROM tarefas WHERE usuarioId = :userId AND favorito = 1")
    List<Tarefa> listarTarefasFavoritas(int userId);

    @Insert
    void inserirBaleiaFavorita(BaleiaFavorita baleia);

    @Query("DELETE FROM baleias_favoritas WHERE usuarioId = :userId AND baleiaResId = :resId")
    void removerBaleiaFavorita(int userId, int resId);

    @Query("SELECT * FROM baleias_favoritas WHERE usuarioId = :userId")
    List<BaleiaFavorita> listarBaleiasFavoritas(int userId);

    @Query("SELECT EXISTS(SELECT 1 FROM baleias_favoritas WHERE usuarioId = :userId AND baleiaResId = :resId)")
    boolean isBaleiaFavorita(int userId, int resId);
}
