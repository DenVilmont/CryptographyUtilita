package com;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {
    public static final String cryptoDefaultAlphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя.,\":-–!? ";

    public static char encryptChar(char ch, int cryptoKey) {

        int index = cryptoDefaultAlphabet.indexOf(ch);
        if (index == -1) {
            return ch;
        }
        int newIndex = index + cryptoKey;
        if (newIndex < 0) {
            while (newIndex < 0) {
                newIndex += cryptoDefaultAlphabet.length();
            }
        }
        if (newIndex >= cryptoDefaultAlphabet.length()) {
            while (newIndex >= cryptoDefaultAlphabet.length()) {
                newIndex -= cryptoDefaultAlphabet.length();
            }
        }
        return cryptoDefaultAlphabet.charAt(newIndex);
    }

    public static List<Map.Entry<Character, Integer>> countLetras(String strDecoded) {
        if (strDecoded == null || strDecoded.isEmpty()) {
            return null;
        }
        var resultChars = new HashMap<Character, Integer>();
        for (Character aChar : strDecoded.toCharArray()) {
            if (resultChars.containsKey(aChar)) {
                resultChars.put(aChar, resultChars.get(aChar) + 1);
            } else {
                resultChars.put(aChar, 1);
            }
        }

        return resultChars.entrySet().stream().sorted((e1, e2) -> -e1.getValue().compareTo(e2.getValue())).collect(Collectors.toList());
    }

    public static String readFile(String fileDirectory) throws IOException {
        File fileIn = new File(fileDirectory);
        FileReader fr = new FileReader(fileIn);
        BufferedReader reader = new BufferedReader(fr);

        String line = reader.readLine();
        StringBuilder stringReaded = new StringBuilder(line);
        while (line != null) {
            line = reader.readLine();
            stringReaded.append(" ").append(line);
        }
        return stringReaded.toString();

    }
}
