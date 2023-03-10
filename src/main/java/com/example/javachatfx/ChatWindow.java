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
import java.util.Scanner;

public class ChatWindow{
    private String message;
    public static int PORT = 9999;
    PGP pgp = new PGP();
    Socket socket;
    private PublicKey publicKeyServer;
    private String adressUser = "192.168.17.30";
    private String nicknameUser = "Andru";
    private Date date_long;
    private SimpleDateFormat date;
    public BufferedReader in;
    public BufferedWriter out;
    @FXML
    private TextArea messageBox;
    @FXML
    public TextArea chatPane;
    @FXML
    private Button buttonSend;

    private static final String HASCONNECTED = "Connection established, streams created";

    @FXML
    void initialize() throws IOException, ClassNotFoundException {
            this.socket = new Socket(adressUser, PORT);
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(pgp.getPublicKey());
            objectOutputStream.flush();

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            publicKeyServer = (PublicKey) objectInputStream.readObject();


            System.out.println(HASCONNECTED);

            new ReadMsg().start();
        buttonSend.setOnAction(event -> {
            try {
                sendMessageButton();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    private class ReadMsg extends Thread {
        @Override
        public void run() {
            String message;
            try {
                while (true) {
                    // ???????? ?????????????????? ?? ??????????????
                    message = in.readLine();
                    // ?????????????????? ?????????????????? ???? ??????????????
                    message = pgp.decrypt(message);
                    if (message.equals("chat_stop")) {
                        break; // ?????????????? ???? ?????????? ???????? ???????????? "chat_stop"
                    }
                    // ?????????? ?????????????????? ?? ?????????????? ???? ??????????????
                    chatPane.appendText(message + "\n");
                }
            } catch (IOException ioException) {
                System.out.println("An exception of type IOException was thrown by the ReadMsg method: " + ioException);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void sendMessageButton() throws Exception {
        date_long = new Date(); // ?????????????? ????????
        date = new SimpleDateFormat("HH:mm:ss");
        message = nicknameUser + "|" + date.format(date_long) + "|" + messageBox.getText();
        // ???????????????????? ??????????????????
        message = pgp.encrypt(message, publicKeyServer);
        out.write(message + "\n");
        out.flush();
        messageBox.setText("");
    }

}
