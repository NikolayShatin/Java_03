package ru.geekbrains.june.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<ClientHandler> clients;

    public Server() { // конструктор сервера
        try {
            this.clients = new ArrayList<>();
            ServerSocket serverSocket = new ServerSocket(8189);
            System.out.println("Сервер запущен. Ожидаем подключение клиентов..");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Подключился новый клиент");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void subscribe(ClientHandler c) { //подписка клиента
        broadcastMessage("В чат зашел пользователь " + c.getUsername());
        clients.add(c);
        broadcastClientList();
    }

    public synchronized void unsubscribe(ClientHandler c) { // удаление клиента из листа
        clients.remove(c);
        broadcastMessage("Из чата вышел пользователь " + c.getUsername());
        broadcastClientList();
    }

    public synchronized void broadcastMessage(String message) { // отправка сообщения всем в чате
        for (ClientHandler c : clients) {
            c.sendMessage(message);
        }
    }

    public synchronized void broadcastClientList() { // отправка листа с клиентами
        StringBuilder builder = new StringBuilder(clients.size() * 10);
        builder.append("/clients_list ");
        for (ClientHandler c : clients) {
            builder.append(c.getUsername()).append(" ");
        }
        String clientsListStr = builder.toString();
        broadcastMessage(clientsListStr);
    }

    public synchronized boolean isUsernameUsed(String username) { // проверка имени пользователя на повторы
        for (ClientHandler c : clients) {
            if (c.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void sendPersonalMessage(ClientHandler sender, String receiverUsername, String message) { // отправка персональных сообщений
        if (sender.getUsername().equalsIgnoreCase(receiverUsername)) {
            sender.sendMessage("Нельзя отправлять личные сообщения самому себе");
            return;
        }
        for (ClientHandler c : clients) {
            if (c.getUsername().equalsIgnoreCase(receiverUsername)) {
                c.sendMessage("от " + sender.getUsername() + ": " + message);
                sender.sendMessage("пользователю " + receiverUsername + ": " + message);
                return;
            }
        }
        sender.sendMessage("Пользователь " + receiverUsername + " не в сети");
    }
}
