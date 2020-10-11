// Cracker.java
/*
 Generates SHA hashes of short strings in parallel.
*/

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Semaphore;

public class Cracker {
    // Array of chars used to produce strings
    public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final int MAX_WORKERS = CHARS.length;

    private static byte[] target;
    private static int maxLength;

    private static Semaphore workersAreDone;

    /*
     Given a byte[] array, produces a hex String,
     such as "234a6f". with 2 chars for each byte in the array.
     (provided code)
    */
    public static String hexToString(byte[] bytes) {
        StringBuilder buff = new StringBuilder();
        for (int aByte : bytes) {
            int val = aByte;
            val = val & 0xff;  // remove higher bits, sign
            if (val < 16) buff.append('0'); // leading 0
            buff.append(Integer.toString(val, 16));
        }
        return buff.toString();
    }

    /*
     Given a string of hex byte values such as "24a26f", creates
     a byte[] array of those values, one byte value -128..127
     for each 2 chars.
     (provided code)
    */
    public static byte[] hexToArray(String hex) {
        byte[] result = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            result[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
        }
        return result;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Args: target length [workers]");
            return;
        }

        if (args.length == 1) {
            printHashForWord(args[0]);
            return;
        }

        target = hexToArray(args[0]);
        maxLength = Integer.parseInt(args[1]);

        int numWorkers = 1;
        if (args.length > 2 && Integer.parseInt(args[2]) > 1) {
            numWorkers = Integer.parseInt(args[2]);
        }

        numWorkers = Math.min(numWorkers, MAX_WORKERS);
        workersAreDone = new Semaphore(0);

        startWorkers(numWorkers);
        waitWorkers(numWorkers);

        System.out.println("all done");
    }

    private static void printHashForWord(String word) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException ignore) { return; }

        digest.reset();
        digest.update(word.getBytes());
        byte[] bytes = digest.digest();
        System.out.println("match: " + hexToString(bytes) + " -> " + word);
    }

    private static void startWorkers(int numWorkers) {
        int segment = CHARS.length / numWorkers;
        for (int i = 0; i < numWorkers; i++) {
            int start = i * segment;
            int end = (i == numWorkers - 1) ? CHARS.length : (start + segment);
            new Thread(new Worker(start, end)).start();
        }
    }

    private static void waitWorkers(int numWorkers) {
        try {
            workersAreDone.acquire(numWorkers);
        } catch (Exception ignored) { }
    }

    private static class Worker implements Runnable {
        private MessageDigest digest;
        private final int start;
        private final int end;

        public Worker(int start, int end) {
            this.start = start;
            this.end = end;

            try {
                digest = MessageDigest.getInstance("SHA");
            } catch (Exception ignored) { }
        }

        public void run() {
            for (int i = start; i < end; i++) {
                search(Character.toString(CHARS[i]));
            }
            workersAreDone.release();
        }

        private void search(String soFar) {
            if (soFar.length() >= maxLength) {
                return;
            }

            digest.reset();
            digest.update(soFar.getBytes());
            byte[] bytes = digest.digest();

            if (MessageDigest.isEqual(bytes, target)) {
                printMessage(soFar, bytes);
            }

            for (char ch : CHARS) {
                search(soFar + ch);
            }
        }

        private void printMessage(String str, byte[] bytes) {
            System.out.println("match: " + str + " -> " + hexToString(bytes));
        }
    }
}
