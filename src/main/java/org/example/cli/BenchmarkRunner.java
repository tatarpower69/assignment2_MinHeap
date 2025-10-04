package org.example.cli;

import org.example.heap.MinHeap;
import org.example.metrics.PerformanceTracker;

import java.util.*;
import java.io.File;

public class BenchmarkRunner {

    public static void main(String[] args) {
        Map<String, String> opts = parseArgs(args);
        String algo = opts.getOrDefault("algo", "MinHeap");
        int n = Integer.parseInt(opts.getOrDefault("n", "1000"));
        String caseType = opts.getOrDefault("case", "random").toLowerCase();
        String out = opts.getOrDefault("out", "results.csv");
        int repeats = Integer.parseInt(opts.getOrDefault("repeats", "5"));

        System.out.printf("Benchmark %s | n=%d | case=%s | repeats=%d | out=%s%n",
                algo, n, caseType, repeats, out);

        Random baseRnd = new Random(12345);
        List<Long> times = new ArrayList<>();

        File outFile = new File(out);
        File parent = outFile.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();

        for (int r = 0; r < repeats; r++) {
            PerformanceTracker tracker = new PerformanceTracker();
            int[] input = generateInput(n, caseType, new Random(baseRnd.nextLong()));
            long t0 = System.nanoTime();
            MinHeap heap = new MinHeap(Math.max(16, n + 4), tracker);
            for (int v : input) heap.insert(v);
            while (!heap.isEmpty()) heap.extractMin();
            long t1 = System.nanoTime();
            long elapsed = t1 - t0;
            times.add(elapsed);
            tracker.writeToCSV(out, algo, caseType, n, elapsed);
            System.out.printf(" run %d: %d ns%n", r + 1, elapsed);
        }

        double mean = times.stream().mapToDouble(Long::doubleValue).average().orElse(0.0);
        double sd = 0.0;
        if (times.size() > 1) {
            double sum = times.stream().mapToDouble(Long::doubleValue).map(x -> (x - mean) * (x - mean)).sum();
            sd = Math.sqrt(sum / (times.size() - 1));
        }
        System.out.printf("mean=%.2f ns  sd=%.2f ns%n", mean, sd);
    }

    private static int[] generateInput(int n, String caseType, Random rnd) {
        int[] a = new int[n];
        switch (caseType) {
            case "sorted":
                for (int i = 0; i < n; i++) a[i] = i;
                break;
            case "reversed":
            case "reverse":
                for (int i = 0; i < n; i++) a[i] = n - i;
                break;
            case "nearly-sorted":
            case "nearly":
                for (int i = 0; i < n; i++) a[i] = i;
                int swaps = Math.max(1, n / 100); // 1% swaps
                for (int s = 0; s < swaps; s++) {
                    int x = rnd.nextInt(n);
                    int y = rnd.nextInt(n);
                    int t = a[x]; a[x] = a[y]; a[y] = t;
                }
                break;
            case "random":
            default:
                for (int i = 0; i < n; i++) a[i] = rnd.nextInt(1_000_000);
                break;
        }
        return a;
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> m = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            if (!a.startsWith("--")) continue;
            String key = a.substring(2);
            if (i + 1 < args.length) {
                m.put(key, args[++i]);
            } else {
                m.put(key, "");
            }
        }
        return m;
    }
}
