package com.example.javachatfx;

import java.io.IOException;

public interface IUserPost {
    public void sendMessageUser(String adressUser, int portUser, String nickname) throws IOException;
}
