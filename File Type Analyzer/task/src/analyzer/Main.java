package analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Pattern {
    Integer priority;
    String pattern;
    String description;
    Integer hash;

    public Pattern(Integer priority, String pattern, String description, Integer hash) {
        this.priority = priority;
        this.pattern = pattern;
        this.description = description;
        this.hash = hash;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getPattern() {
        return pattern;
    }

    public String getDescription() {
        return description;
    }

    public Integer getHash() {
        return hash;
    }
}

public class Main {

    public static boolean arrEquals(byte[] arr1, byte[] arr2) {
        if (arr1.length != arr2.length) return false;
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) return false;
        }
        return true;
    }

    public static String rkSearch(byte[] t, List<Pattern> list) {
        String result = "Unknown file type";

        HashSet<String> hashSet = new HashSet<>();

//        for (int i = 0; i < t.length; i++) {
        for (int i = t.length - 1; i >= 0; i--) {
            for (var pattern : list) {
                if (pattern.getHash() == hash(Arrays.copyOfRange(t, i, i + pattern.getPattern().length()))
                        && arrEquals(Arrays.copyOfRange(t, i, i + pattern.getPattern().length()), pattern.getPattern().getBytes())) {
//                    return pattern.getDescription();
                    hashSet.add(pattern.getPattern());
                }
            }
        }

        if (hashSet.size() > 0) {
            for (var pattern : list) {
                if (hashSet.contains(pattern.getPattern())) {
                    return pattern.getDescription();
                }
            }
        }

        return result;
    }

    public static int hash(byte[] pattern) {
        BigInteger m = BigInteger.ONE;
        BigInteger m3 = BigInteger.valueOf(3);
        BigInteger sum = BigInteger.ZERO;

        for (int i = 0; i < pattern.length; i++) {
            sum = sum.add(m.multiply(BigInteger.valueOf(pattern[i])));
            m = m.multiply(m3);
        }

        return sum.mod(BigInteger.valueOf(11)).intValue();
    }

    public static void main(String[] args) throws InterruptedException {

        if (args.length < 2) {
            System.out.println("Please provide directory with files and file with patterns");
            System.exit(0);
        }

        String dir = args[0];
        String patternsFileName = args[1];

//        dir = "C:\\java\\hyperskill.org\\!files2\\";
//        patternsFileName = "C:\\java\\hyperskill.org\\!files\\patterns.db ";
//        System.out.println("dir=" + dir + " patterns=" + patternsFileName);

        File patternsFile = new File(patternsFileName);
        List<Pattern> list = new ArrayList<>();
        try (Scanner scanner = new Scanner(patternsFile)) {
            while (scanner.hasNext()) {
                String[] arr = scanner.nextLine().split(";");
                String pattern = arr[1].substring(1, arr[1].length() - 1);
                String description = arr[2].substring(1, arr[2].length() - 1);
                list.add(new Pattern(Integer.parseInt(arr[0]), pattern, description, hash(pattern.getBytes())));
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + patternsFile);
        }
        list.sort(Comparator.comparing(Pattern::getPriority).reversed());

        File[] files = new File(dir).listFiles();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (var file : files) {
            if (file.isFile()) {
                executor.submit(() -> {
                    byte[] allBytes = null;
                    try {
                        allBytes = Files.readAllBytes(Paths.get(String.valueOf(file)));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        return;
                    }

                    System.out.println(file.getName() + ": " + rkSearch(allBytes, list));
                });
            }
        }
        executor.shutdown();
        executor.awaitTermination(3000, TimeUnit.MILLISECONDS);
    }
}
