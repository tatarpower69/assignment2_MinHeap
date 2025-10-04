package org.example.heap;

import org.example.metrics.PerformanceTracker;
import java.util.Arrays;

public class MinHeap {
    private int[] heap;
    private int size;
    private final PerformanceTracker tracker;

    public MinHeap() { this(16, new PerformanceTracker()); }

    public MinHeap(int capacity) { this(capacity, new PerformanceTracker()); }

    public MinHeap(int capacity, PerformanceTracker tracker) {
        this.heap = new int[Math.max(2, capacity)];
        this.size = 0;
        this.tracker = (tracker == null) ? new PerformanceTracker() : tracker;
    }

    public MinHeap(int[] arr, PerformanceTracker tracker) {
        this(Math.max(arr.length, 16), tracker);
        System.arraycopy(arr, 0, this.heap, 0, arr.length);
        this.size = arr.length;
        buildHeap();
    }

    private void ensureCapacity(int minCapacity) {
        if (heap.length < minCapacity) {
            int newCap = Math.max(heap.length * 2, minCapacity);
            tracker.incrementAllocations();
            heap = Arrays.copyOf(heap, newCap);
        }
    }

    public void insert(int value) {
        ensureCapacity(size + 1);
        int i = size++;
        while (i > 0) {
            int p = (i - 1) / 2;
            tracker.incrementComparisons();
            if (heap[p] <= value) break;
            heap[i] = heap[p];
            tracker.incrementArrayAccesses();
            i = p;
        }
        heap[i] = value;
        tracker.incrementArrayAccesses();
    }

    public int extractMin() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        int result = heap[0];
        tracker.incrementArrayAccesses();
        int val = heap[--size];
        tracker.incrementArrayAccesses();
        if (size > 0) siftDownWithVal(0, val);
        return result;
    }

    public void decreaseKey(int index, int newValue) {
        if (index < 0 || index >= size) throw new IllegalArgumentException("Invalid index");
        tracker.incrementArrayAccesses();
        if (newValue > heap[index]) throw new IllegalArgumentException("New key is greater than current key");
        int i = index;
        while (i > 0) {
            int p = (i - 1) / 2;
            tracker.incrementComparisons();
            if (heap[p] <= newValue) break;
            heap[i] = heap[p];
            tracker.incrementArrayAccesses();
            i = p;
        }
        heap[i] = newValue;
        tracker.incrementArrayAccesses();
    }

    public void merge(MinHeap other) {
        if (other == null || other.size == 0) return;
        ensureCapacity(size + other.size);
        System.arraycopy(other.heap, 0, heap, size, other.size);
        tracker.incrementArrayAccesses();
        size += other.size;
        buildHeap();
    }

    private void siftDownWithVal(int i, int val) {
        int half = size / 2;
        while (i < half) {
            int left = 2 * i + 1;
            int right = left + 1;
            int smallest = left;

            if (right < size && heap[right] < heap[left]) smallest = right;
            tracker.incrementComparisons();

            if (heap[smallest] >= val) break;
            heap[i] = heap[smallest];
            tracker.incrementArrayAccesses();
            i = smallest;
        }
        heap[i] = val;
        tracker.incrementArrayAccesses();
    }

    private void buildHeap() {
        for (int i = (size >> 1) - 1; i >= 0; i--) siftDownWithVal(i, heap[i]);
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public int peek() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        tracker.incrementArrayAccesses();
        return heap[0];
    }

    public int findIndexOf(int value) {
        for (int i = 0; i < size; i++) {
            tracker.incrementArrayAccesses();
            if (heap[i] == value) return i;
        }
        return -1;
    }
}
