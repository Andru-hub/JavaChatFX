package com.example.javachatfx;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс (UserPost) ;
 * @author Владимиров Андрей ИБС - 12, Владимир Яровой ИБС - 12 , СПБГУТ
 */
public class UserPost{
    ChatWindow chatWindow;
    PGP pgp = new PGP();
    private PublicKey publicKey;
    private static final String HASCONNECTED = "Connection established, streams created";
    private Socket socket;
    public BufferedReader in;
    public BufferedWriter out;
    private BufferedReader inUser;
    private TextArea textArea1;
    private PublicKey publicKeyServer;

    String mess;

    public void setMess(String mess1) {
        this.mess = mess1;
    }

    public UserPost(Socket socket, TextArea textArea) throws IOException {
        try {
            this.socket = socket;
            this.publicKey = publicKey;
            this.textArea1 = textArea;
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(pgp.getPublicKey());
            objectOutputStream.flush();

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            publicKeyServer = (PublicKey) objectInputStream.readObject();

            textArea1.appendText(publicKeyServer.toString());
            System.out.println(HASCONNECTED);
            System.out.println(textArea1);

            new ReadMsg().start();

        } catch (Exception e){
            System.out.println("Eroro");
        }
    }

    private class ReadMsg extends Thread {
        @Override
        public void run() {
            String message;
            try {
                while (true) {
                    // ждем сообщения с сервера
                    message = in.readLine();
                    // дишифруем сообщение от сервера
                    message = pgp.decrypt(message);
                    if (message.equals("chat_stop")) {
                        break; // выходим из цикла если пришло "chat_stop"
                    }
                    // пишем сообщение с сервера на консоль
                    textArea1.appendText(message);
                }
            } catch (IOException ioException) {
                System.out.println("An exception of type IOException was thrown by the ReadMsg method: " + ioException);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public class WriteMsg extends Thread {
        @Override
        public void run() {
            while (true){
                String smess, sMessUser;
                try {
                    smess = mess;
                    if (smess.equalsIgnoreCase("chat_stop")) {
                        break;
                    } else {
                        sMessUser = pgp.encrypt(smess, publicKeyServer);
                        out.write(sMessUser + "\n");
                        out.flush();
                        break;
                    }
                } catch (IOException ioException) {
                    System.out.println("An exception of type IOException was thrown by the WriteMsg method: " + ioException);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }
}