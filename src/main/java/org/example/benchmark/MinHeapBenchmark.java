package org.example.benchmark;
import java.util.*;

import java.io.*;

public class MinHeapBenchmark {
    public static void main(String[] args) throws Exception {
        int[] sizes = {100, 1000, 10000};
        String[] cases = {"random", "sorted", "reversed", "nearly-sorted"};

        try (FileWriter fw = new FileWriter("results.csv")) {
            fw.write("algorithm,case,n,time_ns\n");

            for (int n : sizes) {
                for (String caseType : cases) {
                    int[] input = generateInput(n, caseType, new Random(12345));

                    long start = System.nanoTime();
                    Arrays.sort(Arrays.copyOf(input, input.length)); // или твой MinHeap
                    long end = System.nanoTime();

                    fw.write(String.format("Arrays.sort,%s,%d,%d%n", caseType, n, end - start));
                }
            }
        }
        System.out.println("Benchmark finished, results.csv ready.");
    }

    private static int[] generateInput(int n, String caseType, Random rnd) {
        int[] a = new int[n];
        switch (caseType) {
            case "sorted": for (int i=0;i<n;i++) a[i]=i; break;
            case "reversed": for (int i=0;i<n;i++) a[i]=n-i; break;
            case "nearly-sorted":
                for (int i=0;i<n;i++) a[i]=i;
                for (int s=0;s<Math.max(1,n/100);s++) {
                    int x=rnd.nextInt(n), y=rnd.nextInt(n);
                    int t=a[x]; a[x]=a[y]; a[y]=t;
                }
                break;
            default: for (int i=0;i<n;i++) a[i]=rnd.nextInt(1_000_000); break;
        }
        return a;
    }
}
