package by.rozze.weblab3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("3 лабораторная запущена через Apache Ant.");
        System.out.println("Аргументы запуска: " + Arrays.toString(args));
        System.out.println("Версия Java: " + System.getProperty("java.version"));
        System.out.println("Кодировка файла: " + System.getProperty("file.encoding"));
        System.out.println("Максимальная память JVM: " + Runtime.getRuntime().maxMemory());
    }
}