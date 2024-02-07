package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        int numThreads = 1000;
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            Thread thread = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int countR = countRightTurns(route);
                updateMap(countR);
                System.out.println("Повороты направо засчитываются: " + countR);
            });
            threads[i] = thread;
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        printResults();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int countRightTurns(String route) {
        int count = 0;
        for (int i = 0; i < route.length(); i++) {
            if (route.charAt(i) == 'R') {
                count++;
            }
        }
        return count;
    }

    public static void updateMap(int countR) {
        synchronized (sizeToFreq) {
            sizeToFreq.put(countR, sizeToFreq.getOrDefault(countR, 0) + 1);
        }
    }

    public static void printResults() {
        int maxFreq = 0;
        int mostCommonSize = 0;

        for (int size : sizeToFreq.keySet()) {
            int freq = sizeToFreq.get(size);
            if (freq > maxFreq) {
                maxFreq = freq;
                mostCommonSize = size;
            }
        }

        System.out.println("Самое частое количество повторений: " + mostCommonSize + " (встретилось " + maxFreq + " раз)");
        System.out.println("Другие размеры:");
        for (int size : sizeToFreq.keySet()) {
            if (size != mostCommonSize) {
                System.out.println("- " + size + " (" + sizeToFreq.get(size) + " раз)");
            }
        }
    }

}
