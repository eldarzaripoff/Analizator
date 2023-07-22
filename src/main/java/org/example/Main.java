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
        threadExecutor('a', arrayA, texts);
        threadExecutor('b', arrayB, texts);
        threadExecutor('c', arrayC, texts);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void threadExecutor(char letter, ArrayBlockingQueue<String> array, String[] texts) {
        new Thread(() -> {
            String examinee;
            String largest = null;
            int count = 0;
            int maxCount = 0;
            for (int i = 0; i < texts.length; i++) {
                try {
                    examinee = array.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (char x : examinee.toCharArray()) {
                    if (x == letter)
                        count++;
                }
                if (count > maxCount) {
                    maxCount = count;
                    largest = examinee;
                }
            }
            System.out.println("The text containing the largest number of '" + letter + "' characters " + largest);
        }).start();
    }
}