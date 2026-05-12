package com.example.trabalho1.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.trabalho1.model.Avistamento;
import com.example.trabalho1.model.Usuario;

@Database(entities = {Usuario.class, Avistamento.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instancia;

    public abstract UsuarioDao usuarioDao();
    public abstract AvistamentoDao avistamentoDao();

    public static synchronized AppDatabase getInstancia(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "baleias_db"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instancia;
    }
}
