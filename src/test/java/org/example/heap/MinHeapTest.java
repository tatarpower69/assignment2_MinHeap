package org.example.heap;

import org.example.metrics.PerformanceTracker;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class MinHeapTest {

    @Test
    void testEmptyHeap() {
        MinHeap h = new MinHeap();
        assertTrue(h.isEmpty());
        assertEquals(0, h.size());
        assertThrows(IllegalStateException.class, h::extractMin);
        assertThrows(IllegalStateException.class, h::peek);
    }

    @Test
    void testSingleElement() {
        MinHeap h = new MinHeap(8, new PerformanceTracker());
        h.insert(42);
        assertFalse(h.isEmpty());
        assertEquals(1, h.size());
        assertEquals(42, h.peek());
        assertEquals(42, h.extractMin());
        assertTrue(h.isEmpty());
    }

    @Test
    void testDuplicates() {
        MinHeap h = new MinHeap(8, new PerformanceTracker());
        h.insert(5); h.insert(5); h.insert(5);
        assertEquals(3, h.size());
        assertEquals(5, h.extractMin());
        assertEquals(5, h.extractMin());
        assertEquals(5, h.extractMin());
    }

    @Test
    void testSortedAndReversed() {
        MinHeap a = new MinHeap(32, new PerformanceTracker());
        for (int i = 0; i < 10; i++) a.insert(i);
        for (int i = 0; i < 10; i++) assertEquals(i, a.extractMin());

        MinHeap b = new MinHeap(32, new PerformanceTracker());
        for (int i = 9; i >= 0; i--) b.insert(i);
        for (int i = 0; i < 10; i++) assertEquals(i, b.extractMin());
    }

    @Test
    void testNearlySorted() {
        int n = 100;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = i;
        Random rnd = new Random(42);
        for (int k = 0; k < Math.max(1, n/100); k++) {
            int x = rnd.nextInt(n), y = rnd.nextInt(n);
            int t = arr[x]; arr[x] = arr[y]; arr[y] = t;
        }
        MinHeap h = new MinHeap(n+10, new PerformanceTracker());
        for (int v : arr) h.insert(v);
        int last = Integer.MIN_VALUE;
        while (!h.isEmpty()) {
            int cur = h.extractMin();
            assertTrue(cur >= last);
            last = cur;
        }
    }

    @Test
    void testDecreaseKey() {
        MinHeap h = new MinHeap(16, new PerformanceTracker());
        h.insert(10); h.insert(20); h.insert(30);
        int idx = h.findIndexOf(30);
        assertTrue(idx >= 0);
        h.decreaseKey(idx, 5);
        assertEquals(5, h.peek());
    }

    @Test
    void testMerge() {
        MinHeap a = new MinHeap(8, new PerformanceTracker());
        a.insert(1); a.insert(4);
        MinHeap b = new MinHeap(8, new PerformanceTracker());
        b.insert(2); b.insert(3);
        a.merge(b);
        assertEquals(4, a.size());
        assertEquals(1, a.extractMin());
        assertEquals(2, a.extractMin());
        assertEquals(3, a.extractMin());
        assertEquals(4, a.extractMin());
    }

    @Test
    void propertyRandomTest() {
        Random rnd = new Random(123);
        for (int t = 0; t < 5; t++) {
            int n = 200;
            int[] arr = new int[n];
            MinHeap h = new MinHeap(n+10, new PerformanceTracker());
            for (int i = 0; i < n; i++) {
                arr[i] = rnd.nextInt(10000);
                h.insert(arr[i]);
            }
            Arrays.sort(arr);
            for (int i = 0; i < n; i++) {
                assertEquals(arr[i], h.extractMin());
            }
        }
    }
}
