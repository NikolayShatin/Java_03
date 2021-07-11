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


//                "create table if not exists users (\n" +
//                "id integer primary key autoincrement not null,\n" +
//                "login text not null,\n"+
//                "password text not null\n" +
//                "name text not null\n" +
//                ");";
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


//        for (int i = 0; i<count; i++){
//            String sql = "Insert into users (login, password) values('name"+ i +"', '+123456 + ');";
//            statement.executeUpdate(sql);
//        }


    public void dropTable() throws SQLException { // удалить таблицу
        statement.execute("drop table users;");
    }

    public void readTable() throws SQLException {
        String sql = "select * from users;";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) { // пока есть следующий элемент
            System.out.println(rs.getInt("id") + " " +
                    rs.getString(2) + " " +
                    rs.getString(3) + " " +
                    rs.getString(4));
        }
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
