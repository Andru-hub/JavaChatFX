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
public class UserPost implements Runnable{
    ChatWindow chatWindow;
    PGP pgp = new PGP();
    private PublicKey publicKey;
    private static final String HASCONNECTED = "has connected";
    private Socket socket;
    public BufferedReader in;
    public BufferedWriter out;
    private BufferedReader inUser;
    private TextArea textArea1;

    public UserPost(Socket socket, PublicKey publicKey, TextArea textArea) {
        this.socket = socket;
        this.publicKey = publicKey;
        this.textArea1 = textArea;
    }

    public void run() {
        String message;
        try {
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(HASCONNECTED);
            System.out.println(textArea1);
            while (true){
                message = in.readLine();
                message = pgp.encrypt(message, publicKey);
                textArea1.appendText(message);
            }

        } catch (Exception e){
            System.out.println("Eroro");
        }
    }
}