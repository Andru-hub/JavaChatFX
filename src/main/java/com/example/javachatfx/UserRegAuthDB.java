package com.example.javachatfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;


public class UserRegAuthDB {
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASS = "root";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/chatdb";
    private Connection connectionDB;
    private void showError(String e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("AUTHORIZATION ERROR");
        alert.setHeaderText(e);
        VBox dialogPaneContent = new VBox();
        alert.getDialogPane().setContent(dialogPaneContent);
        alert.showAndWait();
    }

    public void registration(String loginUserReg, String emailUserReg, int ageUserReg, String phoneUserReg, String passwordUserReg){
        try {
            this.connectionDB = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            String sqlAdd = "INSERT INTO public.users (id, loginuser, emailuser, ageuser, phoneuser, passworduser) VALUES (DEFAULT, ?, ?, ?, ?, ?);";
            PreparedStatement preparedStatementAdd = connectionDB.prepareStatement(sqlAdd);
            preparedStatementAdd.setString(1, loginUserReg);
            preparedStatementAdd.setString(2, emailUserReg);
            preparedStatementAdd.setInt(3, ageUserReg);
            preparedStatementAdd.setString(4, phoneUserReg);
            preparedStatementAdd.setString(5, passwordUserReg);
            preparedStatementAdd.executeUpdate();
            System.out.println(loginUserReg + " вы зарегистрированы!");
        } catch (SQLException sqlException) {
            System.out.println("БД SQL Exception: " + sqlException);
        }
    }

    public void authorization(String emailUserAuth, String passwordUserAuth){
        try {
            this.connectionDB = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASS);
            String sqlAuth = "SELECT id, loginuser, emailuser, ageuser, phoneuser, passworduser FROM users WHERE emailuser = ? AND passworduser = ?;";
            PreparedStatement preparedStatementAuth = connectionDB.prepareStatement(sqlAuth);
            preparedStatementAuth.setString(1, emailUserAuth);
            preparedStatementAuth.setString(2, passwordUserAuth);
            ResultSet resultSet = preparedStatementAuth.executeQuery();
            if (resultSet.next()){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("ChatWindow.fxml"));
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.showAndWait();

            } else {
                showError("Ошибка авторизации, проверьте данные или зарегистрируйтесь\nauthorization error, please check the data or register");
                System.out.println("((((");
            }
        } catch (SQLException sqlException) {
            System.out.println("БД SQL Exception: " + sqlException);
        }
    }
}
