package com.example.javachatfx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrationController {
    @FXML
    private Button regSignInButton;

    @FXML
    private TextField reg_age_field;

    @FXML
    private TextField reg_email_field;

    @FXML
    private TextField reg_login_field;

    @FXML
    private PasswordField reg_password_field;

    @FXML
    private TextField reg_telephone_field;

    @FXML
    void initialize() {
        regSignInButton.setOnAction(event ->{
            UserRegAuthDB userRegDB = new UserRegAuthDB();

            String regLoginText = reg_login_field.getText().trim();
            String regLoginPassword = reg_password_field.getText().trim();
            String regLoginEmail = reg_email_field.getText().trim();
            String regLoginTelephone = reg_telephone_field.getText().trim();
            String regLoginAge = reg_age_field.getText().trim();
            Integer Age = Integer.valueOf(regLoginAge);

            if(!regLoginText.equals("") && !regLoginPassword.equals("") && !regLoginEmail.equals("") && !regLoginTelephone.equals("") && !regLoginAge.equals("")) {
                User userReg = new User(regLoginText, regLoginEmail, Age, regLoginTelephone, regLoginPassword);
                userRegDB.registration(userReg.getLogin(), userReg.getEmail(), userReg.getAge(), userReg.getPhoneUser(), userReg.getPassword());
            } else
                System.out.println("Data entered incorrectly");

        });
    }
}
