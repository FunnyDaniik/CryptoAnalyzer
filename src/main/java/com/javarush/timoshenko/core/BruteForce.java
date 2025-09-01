package com.javarush.timoshenko.core;

public class BruteForce {
    private final CaesarCipher cipher;

    public BruteForce(CaesarCipher cipher) {
        this.cipher = cipher;
    }

    public String decryptByBruteForce(String encryptedText, String sampleText) {
        int bestKey = 0;
        double bestMatch = 0;
        char[] alphabet = cipher.getAlphabet();


        for (int key = 0; key < alphabet.length; key++) {
            String decrypted = cipher.decrypt(encryptedText, key);
            double matchScore = calculateMatchScore(decrypted, sampleText);

            if (matchScore > bestMatch) {
                bestMatch = matchScore;
                bestKey = key;
            }
        }

        return cipher.decrypt(encryptedText, bestKey);
    }

    private double calculateMatchScore(String text, String sampleText) {
        if (sampleText == null || sampleText.isEmpty()) {

            return calculateLetterFrequency(text);
        }


        return calculateFrequencySimilarity(text, sampleText);
    }

    private double calculateLetterFrequency(String text) {
        char[] alphabet = cipher.getAlphabet();
        boolean isRussian = alphabet == CaesarCipher.RUSSIAN_ALPHABET;


        double[] expectedFrequencies;
        int letterCount;

        if (isRussian) {
            expectedFrequencies = new double[]{
                    0.0801, 0.0159, 0.0454, 0.0170, 0.0298, 0.0845, 0.0094, 0.0165, 0.0735,
                    0.0121, 0.0349, 0.0440, 0.0321, 0.0670, 0.1097, 0.0281, 0.0473, 0.0547,
                    0.0626, 0.0262, 0.0026, 0.0097, 0.0048, 0.0144, 0.0073, 0.0036, 0.0004,
                    0.0190, 0.0174, 0.0032, 0.0064, 0.0201
            };
            letterCount = 32;
        } else {
            expectedFrequencies = new double[]{
                    0.0812, 0.0149, 0.0271, 0.0432, 0.1202, 0.0230, 0.0203, 0.0592, 0.0731,
                    0.0010, 0.0069, 0.0398, 0.0261, 0.0695, 0.0768, 0.0182, 0.0011, 0.0602,
                    0.0628, 0.0910, 0.0288, 0.0111, 0.0209, 0.0017, 0.0211, 0.0007
            };
            letterCount = 26;
        }

        int[] actualCounts = new int[letterCount];
        int totalLetters = 0;

        for (char c : text.toLowerCase().toCharArray()) {
            for (int i = 0; i < letterCount; i++) {
                if (c == alphabet[i]) {
                    actualCounts[i]++;
                    totalLetters++;
                    break;
                }
            }
        }

        if (totalLetters == 0) return 0;

        double correlation = 0;
        for (int i = 0; i < letterCount; i++) {
            double actualFrequency = (double) actualCounts[i] / totalLetters;
            correlation += Math.min(actualFrequency, expectedFrequencies[i]);
        }

        return correlation;
    }

    private double calculateFrequencySimilarity(String text, String sample) {
        char[] alphabet = cipher.getAlphabet();

        int[] textFreq = calculateCharacterFrequency(text, alphabet);
        int[] sampleFreq = calculateCharacterFrequency(sample, alphabet);

        double similarity = 0;
        int total = 0;

        for (int i = 0; i < textFreq.length; i++) {
            if (sampleFreq[i] > 0) {
                similarity += Math.min(textFreq[i], sampleFreq[i]);
                total += sampleFreq[i];
            }
        }

        return total > 0 ? similarity / total : 0;
    }

    private int[] calculateCharacterFrequency(String text, char[] alphabet) {
        int[] frequency = new int[alphabet.length];
        for (char c : text.toLowerCase().toCharArray()) {
            for (int i = 0; i < alphabet.length; i++) {
                if (c == alphabet[i]) {
                    frequency[i]++;
                    break;
                }
            }
        }
        return frequency;
    }
}
