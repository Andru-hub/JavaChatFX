package com.example.javachatfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.w3c.dom.Entity;

import java.io.*;
import java.net.Socket;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatWindow{
    public ObservableList<String> chatHistory = FXCollections.observableArrayList();
    public static int PORT = 9999;
    UserRegAuthDB userRegAuthDB = new UserRegAuthDB();
    PGP pgp = new PGP();
    UserPost userPost;
    Socket socket;
    private PublicKey publicKeyServer;
    private String adressUser = "192.168.1.48";
    private String nicknameUser = "DDDD";
    private Date date_long;
    private SimpleDateFormat date;
    @FXML
    private TextArea messageBox;
    @FXML
    public TextArea chatPane;
    @FXML
    private Button sendMessageButton;

    @FXML
    void initialize() throws IOException {
        nicknameUser = userRegAuthDB.loginuser;
        try {
            socket = new Socket(adressUser, PORT);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // записывает в поток публичный ключ (PublicKey)
            objectOutputStream.writeObject(pgp.getPublicKey());
            // очищает буфер и сбрасываем его содержимое в выходной поток (на сервер)
            objectOutputStream.flush();

            // считываем из потока объект (получаем ключ от сервера)
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            publicKeyServer = (PublicKey) objectInputStream.readObject();

            chatPane.appendText(publicKeyServer.toString());

            System.out.println(chatPane);
            userPost = new UserPost(socket, publicKeyServer, chatPane);
            Thread x = new Thread(userPost);
            x.start();

        } catch (IOException ioex) {
            // Обрывается соединение при возникновении ошибки сокета
            System.out.println("Socket failed");
            System.out.println("An exception of type IOException was thrown by the sendMessageUser method: " + ioex);

        } catch (Exception e) {
            System.out.println("An exception of type Exception was thrown by the sendMessageUser method: " + e);
        }
    }


    public void sendMethod(KeyEvent event) throws Exception {
        if (event.getCode() == KeyCode.ENTER) {
            sendButtonAction();
        }
    }

    public void sendButtonAction() throws Exception {
        String msg = messageBox.getText();
        date_long = new Date(); // текущая дата
        date = new SimpleDateFormat("HH:mm:ss");
        msg = nicknameUser + "|" + date.format(date_long) + "|" + msg;
        msg = pgp.encrypt(msg, publicKeyServer);
        if (!messageBox.getText().isEmpty()) {
            userPost.out.write(msg + "\n");
            userPost.out.flush();
            messageBox.clear();
        }
    }
}
