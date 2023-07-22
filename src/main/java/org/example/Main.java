package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    static ArrayBlockingQueue<String> arrayA = new ArrayBlockingQueue<>(100);
    static ArrayBlockingQueue<String> arrayB = new ArrayBlockingQueue<>(100);
    static ArrayBlockingQueue<String> arrayC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                try {
                    arrayA.put(texts[i]);
                    arrayB.put(texts[i]);
                    arrayC.put(texts[i]);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        new Thread(() -> {
            String examineeA;
            String largestA = null;
            int countA = 0;
            int maxCountA = 0;
            for (int i = 0; i < texts.length; i++) {
                try {
                    examineeA = arrayA.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (char x : examineeA.toCharArray()) {
                    if (x == 'a')
                        countA++;
                }
                if (countA > maxCountA) {
                    maxCountA = countA;
                    largestA = examineeA;
                }
            }
            System.out.println("The text containing the largest number of 'a' characters " + largestA);
        }).start();

        new Thread(() -> {
            String examineeB;
            String largestB = null;
            int countB = 0;
            int maxCountB = 0;
            for (int i = 0; i < texts.length; i++) {
                try {
                    examineeB = arrayB.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (char x : examineeB.toCharArray()) {
                    if (x == 'b')
                        countB++;
                }
                if (countB > maxCountB) {
                    maxCountB = countB;
                    largestB = examineeB;
                }
            }
            System.out.println("The text containing the largest number of 'b' characters " + largestB);
        }).start();
        new Thread(() -> {
            String examineeC;
            String largestC = null;
            int countC = 0;
            int maxCountC = 0;
            for (int i = 0; i < texts.length; i++) {
                try {
                    examineeC = arrayC.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (char x : examineeC.toCharArray()) {
                    if (x == 'c')
                        countC++;
                }
                if (countC > maxCountC) {
                    maxCountC = countC;
                    largestC = examineeC;
                }
            }
            System.out.println("The text containing the largest number of 'c' characters " + largestC);
        }).start();

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}