package com.javarush.timoshenko.core;

import java.util.HashMap;
import java.util.Map;

public class CaesarCipher {
    public static final char[] RUSSIAN_ALPHABET = {
            'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о',
            'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э',
            'ю', 'я', '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '
    };

    public static final char[] ENGLISH_ALPHABET = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '.', ',', '"', '\'',
            ':', '!', '?', ' '
    };

    private final Map<Character, Integer> charToIndex;
    private final char[] alphabet;

    public CaesarCipher(char[] alphabet) {
        this.alphabet = alphabet;
        this.charToIndex = new HashMap<>();
        for (int i = 0; i < alphabet.length; i++) {
            charToIndex.put(alphabet[i], i);
        }
    }

    public String encrypt(String text, int shift) {
        return processText(text, shift);
    }

    public String decrypt(String encryptedText, int shift) {
        return processText(encryptedText, alphabet.length - (shift % alphabet.length));
    }

    private String processText(String text, int shift) {
        StringBuilder result = new StringBuilder();

        for (char character : text.toCharArray()) {
            char lowerChar = Character.toLowerCase(character);
            Integer index = charToIndex.get(lowerChar);

            if (index != null) {
                int newIndex = (index + shift) % alphabet.length;
                char encryptedChar = alphabet[newIndex];

                // Сохраняем регистр оригинального символа
                if (Character.isUpperCase(character)) {
                    encryptedChar = Character.toUpperCase(encryptedChar);
                }

                result.append(encryptedChar);
            } else {
                result.append(character);
            }
        }

        return result.toString();
    }

    public char[] getAlphabet() {
        return alphabet;
    }
}
