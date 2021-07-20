package ru.geekbrains.june.chat.server;

import java.sql.*;

public class DataBaseAuthenticationProvider implements AuthenticationProvider {

    private static Connection connection; //Интерфейс Connection описывает соединение с базой данных.
    // При старте программы должен быть выполнен метод connect(), который откроет соединение с БД и сохранит
    // его в поле Connection connection.
    private static Statement statement;
    //Statement представляет собой интерфейс для отправки запросов в БД.


    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:javadb.db"); // DriverManager представляет собой
        // менеджер JDBC драйверов. При запуске программы, все подключенные к проекту драйвера, регистрируются в DriverManager.
        statement = connection.createStatement();
    }


    public void disconnect() {
        if (statement != null) { // вначале закрываем его, так как он создан на основе connection
            try {
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }


    public void createTable() throws SQLException { // запрос на создание таблицы в базе данных
        String sql =
                ("CREATE TABLE IF NOT EXISTS users (\n" +
                        "        id    INTEGER PRIMARY KEY AUTOINCREMENT not null,\n" +
                        "        login  TEXT not null,\n" +
                        "        password TEXT not null,\n" +
                        "        name TEXT\n" +
                        "    );");



        System.out.println(sql);
        statement.executeUpdate(sql);
    }


    public void insertUsers() throws SQLException { // добавить пользователей в чат
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("insert into users (login, password, name) values (?, ?, ?)")) {
            preparedStatement.setString(1, "bob@freemail.com");
            preparedStatement.setString(2, "123456");
            preparedStatement.setString(3, "Bob");
            preparedStatement.addBatch();
            preparedStatement.setString(1, "john@freemail.com");
            preparedStatement.setString(2, "123456");
            preparedStatement.setString(3, "John");
            preparedStatement.addBatch();
            preparedStatement.executeBatch();// взаимодействие с БД происходит только в этой строчке
        }
    }

    public void changeName(String name, String login) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("update users set name = ? where login = ? ")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, login);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }





    public void dropTable() throws SQLException { // удалить таблицу
        statement.execute("drop table users;");
    }



    @Override
    public String getUsernameByLoginAndPassword(String login, String password) {
        String temp = "name";
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement("select * from users where login = ? and password = ?")) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                return rs.getString(4);
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
        }


        return null;
    }
}
