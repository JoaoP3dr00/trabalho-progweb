package com.example.trabalho1.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.trabalho1.model.Avistamento;

import java.util.List;

@Dao
public interface AvistamentoDao {

    @Insert
    void inserir(Avistamento avistamento);

    @Update
    void atualizar(Avistamento avistamento);

    @Delete
    void excluir(Avistamento avistamento);

    @Query("SELECT * FROM avistamento ORDER BY id DESC")
    List<Avistamento> listarTodos();

    @Query("SELECT * FROM avistamento WHERE especie LIKE '%' || :busca || '%' OR local LIKE '%' || :busca || '%' OR descricao LIKE '%' || :busca || '%' ORDER BY id DESC")
    List<Avistamento> buscar(String busca);
}
