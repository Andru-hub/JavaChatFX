package com.example.javachatfx;

import javax.crypto.Cipher;
import java.security.PublicKey;
import java.util.Base64;

public interface IPGP {
    public String encrypt(String message, PublicKey spkay) throws Exception;
    public String decrypt(String encryptedMessage) throws Exception;
}
