package com.javarush.timoshenko.core;

import java.io.File;

public class Validator {
    private final char[] alphabet;

    public Validator(char[] alphabet) {
        this.alphabet = alphabet;
    }

    public boolean isValidKey(int key) {
        return key >= 0 && key < alphabet.length;
    }

    public boolean isFileExists(String filePath) {
        return new File(filePath).exists();
    }

    public boolean isValidOutputPath(String filePath) {
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(filePath);
            java.nio.file.Path parent = path.getParent();
            return parent == null || java.nio.file.Files.exists(parent) || java.nio.file.Files.isWritable(parent);
        } catch (Exception e) {
            return false;
        }
    }

    public char[] getAlphabet() {
        return alphabet;
    }
}
