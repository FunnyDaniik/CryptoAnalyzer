package com.javarush.timoshenko;

import com.javarush.timoshenko.core.*;
import com.javarush.timoshenko.exception.CipherException;
import java.util.Scanner;

public class MainApp {
    private FileManager fileManager;
    private Validator validator;
    private CaesarCipher cipher;
    private BruteForce bruteForce;
    private StatisticalAnalyzer statisticalAnalyzer;
    private Scanner scanner;
    private char[] currentAlphabet;

    public MainApp() {
        scanner = new Scanner(System.in);
        selectAlphabet();
        initializeComponents();
    }

    private void selectAlphabet() {
        System.out.println("=== Выбор алфавита ===");
        System.out.println("1. Русский алфавит");
        System.out.println("2. Английский алфавит");

        int choice = getIntInput("Выберите алфавит: ");

        switch (choice) {
            case 1 -> currentAlphabet = CaesarCipher.RUSSIAN_ALPHABET;
            case 2 -> currentAlphabet = CaesarCipher.ENGLISH_ALPHABET;
            default -> {
                System.out.println("Неверный выбор. Используется русский алфавит.");
                currentAlphabet = CaesarCipher.RUSSIAN_ALPHABET;
            }
        }

        System.out.println("Выбран алфавит: " +
                (currentAlphabet == CaesarCipher.RUSSIAN_ALPHABET ? "Русский" : "Английский"));
    }

    private void initializeComponents() {
        fileManager = new FileManager();
        validator = new Validator(currentAlphabet);
        cipher = new CaesarCipher(currentAlphabet);
        bruteForce = new BruteForce(cipher);
        statisticalAnalyzer = new StatisticalAnalyzer(cipher);
    }

    public void run() {
        while (true) {
            printMenu();
            int choice = getIntInput("Выберите опцию: ");

            switch (choice) {
                case 1 -> encrypt();
                case 2 -> decryptWithKey();
                case 3 -> bruteForceDecrypt();
                case 4 -> statisticalAnalysis();
                case 5 -> changeAlphabet();
                case 0 -> {
                    System.out.println("Выход из программы.");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== Шифр Цезаря ===");
        System.out.println("Алфавит: " +
                (currentAlphabet == CaesarCipher.RUSSIAN_ALPHABET ? "Русский" : "Английский"));
        System.out.println("1. Зашифровать текст");
        System.out.println("2. Расшифровать текст с ключом");
        System.out.println("3. Взлом brute force");
        System.out.println("4. Статистический анализ");
        System.out.println("5. Сменить алфавит");
        System.out.println("0. Выход");
    }

    private void encrypt() {
        try {
            String inputFile = getFileInput("Введите путь к исходному файлу: ", false);
            String outputFile = getFileInput("Введите путь для сохранения результата: ", true);
            int key = getIntInput("Введите ключ (число от 0 до " + (currentAlphabet.length - 1) + "): ");

            if (!validator.isValidKey(key)) {
                System.out.println("Неверный ключ!");
                return;
            }

            String text = fileManager.readFile(inputFile);
            String encrypted = cipher.encrypt(text, key);
            fileManager.writeFile(encrypted, outputFile);

            System.out.println("Текст успешно зашифрован и сохранен в: " + outputFile);

        } catch (CipherException e) {
            System.out.println("Ошибка при шифровании: " + e.getMessage());
        }
    }

    private void decryptWithKey() {
        try {
            String inputFile = getFileInput("Введите путь к зашифрованному файлу: ", false);
            String outputFile = getFileInput("Введите путь для сохранения результата: ", true);
            int key = getIntInput("Введите ключ: ");

            if (!validator.isValidKey(key)) {
                System.out.println("Неверный ключ!");
                return;
            }

            String text = fileManager.readFile(inputFile);
            String decrypted = cipher.decrypt(text, key);
            fileManager.writeFile(decrypted, outputFile);

            System.out.println("Текст успешно расшифрован и сохранен в: " + outputFile);

        } catch (CipherException e) {
            System.out.println("Ошибка при расшифровке: " + e.getMessage());
        }
    }

    private void bruteForceDecrypt() {
        try {
            String inputFile = getFileInput("Введите путь к зашифрованному файлу: ", false);
            String sampleFile = getFileInput("Введите путь к файлу-образцу (или Enter чтобы пропустить): ", true);
            String outputFile = getFileInput("Введите путь для сохранения результата: ", true);

            String encryptedText = fileManager.readFile(inputFile);
            String sampleText = "";

            if (!sampleFile.isEmpty() && validator.isFileExists(sampleFile)) {
                sampleText = fileManager.readFile(sampleFile);
            }

            String decrypted = bruteForce.decryptByBruteForce(encryptedText, sampleText);
            fileManager.writeFile(decrypted, outputFile);

            System.out.println("Brute force завершен. Результат сохранен в: " + outputFile);

        } catch (CipherException e) {
            System.out.println("Ошибка при brute force: " + e.getMessage());
        }
    }

    private void statisticalAnalysis() {
        try {
            String inputFile = getFileInput("Введите путь к зашифрованному файлу: ", false);
            String sampleFile = getFileInput("Введите путь к файлу-образцу: ", false);
            String outputFile = getFileInput("Введите путь для сохранения результата: ", true);

            String encryptedText = fileManager.readFile(inputFile);
            String sampleText = fileManager.readFile(sampleFile);

            int shift = statisticalAnalyzer.findMostLikelyShift(encryptedText, sampleText);
            String decrypted = cipher.decrypt(encryptedText, shift);

            fileManager.writeFile(decrypted, outputFile);

            System.out.println("Статистический анализ завершен. Найденный ключ: " + shift);
            System.out.println("Результат сохранен в: " + outputFile);

        } catch (CipherException e) {
            System.out.println("Ошибка при статистическом анализе: " + e.getMessage());
        }
    }

    private void changeAlphabet() {
        selectAlphabet();
        initializeComponents();
        System.out.println("Алфавит изменен.");
    }

    private String getFileInput(String prompt, boolean canBeEmpty) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty() && canBeEmpty) {
                return input;
            }

            if (input.isEmpty() && !canBeEmpty) {
                System.out.println("Путь не может быть пустым.");
                continue;
            }

            if (!canBeEmpty && !validator.isFileExists(input)) {
                System.out.println("Файл не существует: " + input);
                continue;
            }

            if (canBeEmpty && !input.isEmpty() && !validator.isValidOutputPath(input)) {
                System.out.println("Невозможно записать в указанный путь: " + input);
                continue;
            }

            return input;
        }
    }

    private int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите целое число.");
            }
        }
    }

    public static void main(String[] args) {
        new MainApp().run();
    }
}
