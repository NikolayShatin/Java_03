package ru.geekbrains.june.chat.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private String username;
    private DataInputStream in;
    private DataOutputStream out;
    private Date date; // добавили время, когда клиент подключился к серверу
    private SimpleDateFormat formatDate; // форматирование времени
    private String mail; // сохраним для смены имени по имейлу
    private final Logger LOGGER = LogManager.getLogger(ClientHandler.class); // лог событий

    public String getUsername() {
        return username;
    } // получение имени пользователя

    public ClientHandler(Server server, Socket socket) { // конструктор сетевого обмена данными с клиентом
        try {
            this.server = server;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            server.getCachedThreadPool().execute(()->{logic();}); // запускаем поток в кэше потоков
            //new Thread(() -> logic()).start(); // вынесли поток в отдельный метод, чтобы повысить читабельность
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) { //отправка сообщений
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logic() { // метод с циклами авторизации и обмена сообщениями
        try {
            while (!consumeAuthorizeMessage(in.readUTF())); //
            while (consumeRegularMessage(in.readUTF())); //
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            LOGGER.info("Клиент " + username + " отключился");
            server.unsubscribe(this);
            closeConnection();
        }
    }

    private boolean consumeRegularMessage(String inputMessage) { // обработка комманд деавторизаци и личных сообщений
        if (inputMessage.startsWith("/")) {
            if (inputMessage.equals("/exit")) {
                sendMessage("/exit");
                return false;
            }
            if (inputMessage.startsWith("/w ")) {
                String[] tokens = inputMessage.split("\\s+", 3);
                server.sendPersonalMessage(this, tokens[1], tokens[2]);
            }
            if (inputMessage.startsWith("/chname ")) { // меняем имя пользователя
                String[] tokens = inputMessage.split("\\s+", 2);
                server.getAuthenticationProvider().changeName(tokens[1], mail);
                sendMessage("/exit");
            }


            return true;
        }
        server.broadcastMessage(username + ": " + inputMessage);
        return true;
    }

    private boolean consumeAuthorizeMessage(String message) { // в авторизацию добавлена отсылку клиенту авторизованного имени и дату авторизации
        if (message.startsWith("/auth ")) { // /auth bob@freemail.com 123456
            String[] tokens = message.split("\\s+");
            if (tokens.length == 1 || tokens.length == 2) {
                sendMessage("SERVER: Вы не указали имя пользователя или пароль");
                return false;
            }
            if (tokens.length > 3) {
                sendMessage("SERVER: Имя пользователя не может состоять из нескольких слов");
                return false;
            }
            String selectedUsername = server.getAuthenticationProvider().getUsernameByLoginAndPassword(tokens[1] , tokens[2]); // проверяем, что
            if (selectedUsername == null) {
                sendMessage("SERVER: Неверное имя пользователя или пароль");
                return false;
            }
            username = selectedUsername;
            mail = tokens[1];
            date = new Date();
            formatDate = new SimpleDateFormat("HH:mm:ss yyyy.MM.dd");
            sendMessage("/authok "+username+ " " + formatDate.format(date));// отправка авторизационного сообщения с доп информацией о дате и имени
            server.subscribe(this);
            LOGGER.info("Клиент " + username + " подтключился");
            return true;
        } else {
            sendMessage("SERVER: Вам необходимо авторизоваться");
            return false;
        }
    }

    private void closeConnection() { // аккуратное закрытие соединения с клиентом
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //server.getCachedThreadPool().shutdown(); // закрываем пул потоков

    }
}
