package ru.geekbrains.june.chat.server;

import java.sql.SQLException;

public class ServerApp {
    /*
        Домашнее задание:
        1. Разобраться с кодом, проставить "глобальные" комментарии
        2. В любом виде, на клиенте выведите его имя на интерфейс
    */
    public static void main(String[] args) {

        DataBase dataBase = new DataBase();

        try {
            dataBase.connect();
            dataBase.dropTable();
            dataBase.createTable();
            dataBase.insertUsers(10);
            dataBase.readTable();
            dataBase.disconnect();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            dataBase.disconnect();
        }


        new Server();




    }
}








