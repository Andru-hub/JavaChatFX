package com.example.javachatfx;

import java.io.IOException;

public interface IUserPost {
    public void startMesager(String adressUser, int portUser, String nickname) throws IOException;
}
