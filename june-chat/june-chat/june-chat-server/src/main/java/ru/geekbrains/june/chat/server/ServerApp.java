package ru.geekbrains.june.chat.server;

import java.sql.SQLException;

public class ServerApp {
    /*
        Домашнее задание:
        1. Разобраться с кодом, проставить "глобальные" комментарии
        2. В любом виде, на клиенте выведите его имя на интерфейс
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








