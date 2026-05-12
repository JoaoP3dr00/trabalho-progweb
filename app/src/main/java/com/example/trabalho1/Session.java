package com.example.trabalho1;

public class Session {

    private static int userId = -1;
    private static String userName = null;
    private static String userFoto = null;

    public static void login(int id, String nome, String foto) {
        userId = id;
        userName = nome;
        userFoto = foto;
    }

    public static void logout() {
        userId = -1;
        userName = null;
        userFoto = null;
    }

    public static boolean isLoggedIn() {
        return userId != -1;
    }

    public static int getUserId() {
        return userId;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getUserFoto() {
        return userFoto;
    }
}
