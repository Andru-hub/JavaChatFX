package com.example.javachatfx;
/**
 * Класс номера телефона (user) со свойствами phonenamber;
 */
public class UserPhone {
    private String numberphone, obrnumberphone;
    /**
     * Конструктор класса создает и приводит к стандартному виду номер телефона пользователя
     * @param numberphone не обработанный номер телефона пользователя
     */
    public UserPhone(String numberphone){
        setNumberPhone(numberphone);
    }
    public String getNumberPhone(){
        return obrnumberphone;
    }
    public void setNumberPhone(String numberphone){
        normalizationUserPhone(numberphone);
    }
    /**
     * Метод преобразование номера телефона в нормальный вид
     * @param numberphone - не обработанный номер телефона пользователя
     */
    protected void normalizationUserPhone(String numberphone){
        String nphone;
        nphone = numberphone.replaceAll("(\\D)*", "");
        int len = nphone.length();
        if (len >= 10){
            obrnumberphone = "+" + nphone;
        }
        else {
            System.out.println("NUMBER FORMAT ERROR");
        }
    }
}
