package com.javarush.timoshenko.core;

public class StatisticalAnalyzer {
    private final CaesarCipher cipher;

    public StatisticalAnalyzer(CaesarCipher cipher) {
        this.cipher = cipher;
    }

    public int findMostLikelyShift(String encryptedText, String representativeText) {
        int bestShift = 0;
        double bestScore = 0;
        char[] alphabet = cipher.getAlphabet();


        int[] refFrequencies = calculateCharacterFrequency(representativeText, alphabet);

        for (int shift = 0; shift < alphabet.length; shift++) {
            String decrypted = cipher.decrypt(encryptedText, shift);
            int[] decryptedFrequencies = calculateCharacterFrequency(decrypted, alphabet);

            double score = calculateFrequencyScore(decryptedFrequencies, refFrequencies);

            if (score > bestScore) {
                bestScore = score;
                bestShift = shift;
            }
        }

        return bestShift;
    }

    private int[] calculateCharacterFrequency(String text, char[] alphabet) {
        int[] frequency = new int[alphabet.length];
        int total = 0;

        for (char c : text.toLowerCase().toCharArray()) {
            for (int i = 0; i < alphabet.length; i++) {
                if (c == alphabet[i]) {
                    frequency[i]++;
                    total++;
                    break;
                }
            }
        }


        if (total > 0) {
            for (int i = 0; i < frequency.length; i++) {
                frequency[i] = (frequency[i] * 1000) / total;
            }
        }

        return frequency;
    }

    private double calculateFrequencyScore(int[] freq1, int[] freq2) {
        double score = 0;
        for (int i = 0; i < freq1.length; i++) {
            if (freq2[i] > 0) {
                score += Math.min(freq1[i], freq2[i]);
            }
        }
        return score;
    }
}
