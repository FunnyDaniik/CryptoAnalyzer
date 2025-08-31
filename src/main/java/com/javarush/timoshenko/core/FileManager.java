package com.javarush.timoshenko.core;

import com.javarush.timoshenko.exception.CipherException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileManager {

    public String readFile(String filePath) throws CipherException {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new CipherException("Ошибка чтения файла: " + filePath, e);
        }
    }

    public void writeFile(String content, String filePath) throws CipherException {
        try {
            Files.write(Paths.get(filePath), content.getBytes());
        } catch (IOException e) {
            throw new CipherException("Ошибка записи в файл: " + filePath, e);
        }
    }
}
