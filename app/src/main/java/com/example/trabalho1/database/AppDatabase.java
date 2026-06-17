package com.example.trabalho1.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Usuario.class, Tarefa.class, BaleiaFavorita.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDao appDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "trabalho1_db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                    seedAdmin(INSTANCE);
                }
            }
        }
        return INSTANCE;
    }

    private static void seedAdmin(AppDatabase db) {
        if (db.appDao().buscarPorEmail("admin@seatask.com") == null) {
            Usuario admin = new Usuario();
            admin.nome = "Administrador";
            admin.email = "admin@seatask.com";
            admin.senha = com.example.trabalho1.SecurityUtils.hashSenha("admin123");
            admin.palavraChave = "master";
            admin.isAdmin = true;
            db.appDao().inserirUsuario(admin);
        }
    }
}
