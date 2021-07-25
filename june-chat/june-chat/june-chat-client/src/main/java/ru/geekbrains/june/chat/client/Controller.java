package ru.geekbrains.june.chat.client;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller {
    @FXML
    TextArea chatArea;

    @FXML
    TextField messageField, usernameField;

    @FXML
    HBox authPanel, msgPanel, statusPanel; // добавили панель отображения статуса

    @FXML
    ListView<String> clientsListView;

    @FXML
    Label labelName; // отображение статуса реализовано через лейбл

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String nickname;

    public void setAuthorized(boolean authorized) { // метод, который переключает интерфейс после авторизации
        msgPanel.setVisible(authorized);
        msgPanel.setManaged(authorized);
        authPanel.setVisible(!authorized);
        authPanel.setManaged(!authorized);
        clientsListView.setVisible(authorized);
        clientsListView.setManaged(authorized);
        statusPanel.setVisible(authorized); // добавлена панель вывода статуса клиента
        statusPanel.setManaged(authorized); // добавлена панель вывода статуса клиента
    }

    public void sendMessage() { // отправка сообщений на сервер
        try {
            out.writeUTF(messageField.getText());
            messageField.clear();
            messageField.requestFocus();
        } catch (IOException e) {
            showError("Невозможно отправить сообщение на сервер");
        }
    }

    public void sendCloseRequest() { // закрытие соединения при нажатии на крестик
        try {
            if (out != null) {
                out.writeUTF("/exit");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tryToAuth() { // метод, отсылающий на сервер имя и команду авторизации
        connect();
        try {
            out.writeUTF("/auth " + usernameField.getText());
            usernameField.clear();
        } catch (IOException e) {
            showError("Невозможно отправить запрос авторизации на сервер");
        }
    }

    public void connect() { // метод, описывающий подключение к серверу, логика общения клиента с сервером вынесена в отдельный метод
        if (socket != null && !socket.isClosed()) {
            return;
        }
        try {
            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> logic()).start();
        } catch (IOException e) {
            showError("Невозможно подключиться к серверу");
        }
    }

    private void logic() { // метод, содержащий 2 цикла - авторизацию и обмен сообщениями с сервером
        try {
            while (true) { // цикл авторизации
                String inputMessage = in.readUTF();
                if (inputMessage.equals("/exit")) {
                    closeConnection();
                }
                if (inputMessage.startsWith("/authok ")) {
                    setAuthorized(true);
                    Platform.runLater(() -> setLabelText(inputMessage));// в JavaFX потоке установим текст для лейбла и выведем 100 строк чата
                    break;
                }

                chatArea.appendText(inputMessage + "\n");
            }
            while (true) { // цикл обмена сообщениями
                String inputMessage = in.readUTF();
                if (inputMessage.startsWith("/")) {
                    if (inputMessage.equals("/exit")) {
                        break;
                    }
                    // /clients_list bob john
                    if (inputMessage.startsWith("/clients_list ")) { // добавление клиентов в клиентский лист
                        Platform.runLater(() -> {
                            String[] tokens = inputMessage.split("\\s+");
                            clientsListView.getItems().clear(); // предварительно очищаем предыдущее состояние
                            for (int i = 1; i < tokens.length; i++) {
                                clientsListView.getItems().add(tokens[i]);
                            }
                        });
                    }
                    continue;
                }
                chatArea.appendText(inputMessage + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }


    private void prepairFile() {
        File file = new File(nickname + ".txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void closeConnection() { // метод для отключения от сервера
        setAuthorized(false);

        prepairFile(); // при закрытии приложения осуществляем запись в файл
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(nickname + ".txt", true))) {

            bufferedWriter.write(chatArea.getText()); // запись в файл текста из окна чата
        } catch (IOException e) {
            e.printStackTrace();
        }


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
    }

    public void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).showAndWait();
    } // вывод сообщений об ошибках

    public void clientsListDoubleClick(MouseEvent mouseEvent) { // запуск общения с конкретным клиентом
        if (mouseEvent.getClickCount() == 2) {
            String selectedUser = clientsListView.getSelectionModel().getSelectedItem();
            messageField.setText("/w " + selectedUser + " ");
            messageField.requestFocus();
            messageField.selectEnd();
        }
    }

    private void setLabelText(String inputMessage) { // метод для форматирования и вывода текста на лейбл и в чат
        String[] tokens = inputMessage.split("\\s+");
        nickname = tokens[1];
        tokens[1] = "Вы вошли в чат под именем " + tokens[1];
        tokens[2] = " в " + tokens[2];
        tokens[3] = " // " + tokens[3];
        labelName.setText(tokens[1] + tokens[2] + tokens[3]);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(nickname + ".txt"))) { // загрузка сообщений чата для конкретного пользователя
            String line;
            int line_count = 0;
            while (((line = bufferedReader.readLine())!= null)&&line_count<100) {
                chatArea.appendText(line + "\n"); // чтение из файла
                line_count++;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
