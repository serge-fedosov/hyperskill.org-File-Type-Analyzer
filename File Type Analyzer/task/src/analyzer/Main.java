package analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {


    public static boolean naiveSearch(byte[] allBytes, byte[] patternBytes) {
        for (int i = 0; i <= allBytes.length - patternBytes.length; i++) {

            boolean found = true;
            for (int j = 0; j < patternBytes.length; j++) {
                if (allBytes[i + j] != patternBytes[j]) {
                    found = false;
                    break;
                }
            }

            if (found) {
                return true;
            }
        }

        return false;
    }

    public static boolean kmpSearch(byte[] allBytes, byte[] patternBytes) {
        for (int i = 0; i < allBytes.length - patternBytes.length; i++) {

            boolean found = true;
            for (int j = 0; j < patternBytes.length; j++) {
                if (allBytes[i + j] != patternBytes[j]) {
                    found = false;
                    break;
                }
            }

            if (found) {
                return true;
            }
        }

        return false;
    }


    public static void main(String[] args) {

        if (args.length < 4) {
            System.out.println("Please provide algorithm, file, pattern string, and the result string");
            System.exit(0);
        }

        String method = args[0];
        String file = args[1];
        String pattern = args[2];
        String result = args[3];

        //System.out.println("method=" + method + " file=" + file + " pattern=" + pattern + " result=" + result);

        byte[] patternBytes = pattern.getBytes();

        byte[] allBytes = null;
        try {
            allBytes = Files.readAllBytes(Paths.get(file));

        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        long time1 = System.nanoTime();

        if ("--naive".equals(method)) {

            if (naiveSearch(allBytes, patternBytes)) {
                System.out.println(result);
            } else {
                System.out.println("Unknown file type");
            }
        } else {
            if (naiveSearch(allBytes, patternBytes)) {
                System.out.println(result);
            } else {
                System.out.println("Unknown file type");
            }
        }

        long timeDiff = System.nanoTime() - time1;
        double time = 1.0 * timeDiff / 1000000000;

        System.out.println("It took " + time + " seconds");

    }
}
