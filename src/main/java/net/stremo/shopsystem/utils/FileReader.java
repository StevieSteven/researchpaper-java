package net.stremo.shopsystem.utils;

import java.io.*;

public class FileReader {
    private static String basePath = "/data/";

    public static String readFileContent(String fileName){
        StringBuilder stringBuilder = new StringBuilder("");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(FileReader.class.getResourceAsStream(basePath + fileName)))) {
            reader.lines().forEach(stringBuilder::append);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


}
