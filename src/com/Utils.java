package com;

public class Utils {
    public static final String cryptoDefaultAlphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя.,\":-–!? ";

    public static char encryptChar(char ch, int cryptoKey) {

        int index = cryptoDefaultAlphabet.indexOf(ch);
        if (index == -1){
            return ch;
        }
        int newIndex = index + cryptoKey;
        if (newIndex < 0){
            while (newIndex < 0){
                newIndex += cryptoDefaultAlphabet.length();
            }
        }
        if (newIndex >= cryptoDefaultAlphabet.length()){
            while (newIndex >= cryptoDefaultAlphabet.length()){
                newIndex -= cryptoDefaultAlphabet.length();
            }
        }
        return cryptoDefaultAlphabet.charAt(newIndex);
    }
}
