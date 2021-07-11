package ru.geekbrains.june.chat.server;

import java.sql.SQLException;

public class ServerApp {
    /*
        Домашнее задание:
        1. Добавить в сетевой чат авторизацию через базу данных SQLite.
        2.*Добавить в сетевой чат возможность смены ника.
    */
    public static void main(String[] args) {

        DataBaseAuthenticationProvider dataBaseAuthenticationProvider = new DataBaseAuthenticationProvider();

        try {
            dataBaseAuthenticationProvider.connect();
            dataBaseAuthenticationProvider.dropTable();
            dataBaseAuthenticationProvider.createTable();
            dataBaseAuthenticationProvider.insertUsers();
            System.out.println(dataBaseAuthenticationProvider.getUsernameByLoginAndPassword("john@freemail.com", "123456"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        new Server();




    }
}








