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
        this.tracker = tracker == null ? new PerformanceTracker() : tracker;
    }

    public void insert(int value) {
        if (size == heap.length) resize();
        heap[size] = value;
        tracker.incrementAllocations();
        siftUp(size++);
    }

    public int extractMin() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        int min = heap[0];
        heap[0] = heap[--size];
        siftDown(0);
        return min;
    }

    public void decreaseKey(int index, int newValue) {
        if (index < 0 || index >= size) throw new IllegalArgumentException("Invalid index");
        if (newValue > heap[index]) throw new IllegalArgumentException("New key is greater");
        heap[index] = newValue;
        siftUp(index);
    }

    public void merge(MinHeap other) {
        int newSize = this.size + other.size;
        int[] merged = Arrays.copyOf(this.heap, newSize);
        System.arraycopy(other.heap, 0, merged, this.size, other.size);
        this.heap = merged;
        this.size = newSize;
        buildHeap();
    }

    private void siftUp(int i) {
        while (i > 0) {
            int p = (i - 1) / 2;
            tracker.incrementComparisons();
            if (heap[i] < heap[p]) {
                swap(i, p);
                i = p;
            } else break;
        }
    }

    private void siftDown(int i) {
        int n = size;
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;
            if (left < n) { tracker.incrementComparisons(); if (heap[left] < heap[smallest]) smallest = left; }
            if (right < n) { tracker.incrementComparisons(); if (heap[right] < heap[smallest]) smallest = right; }
            if (smallest != i) { swap(i, smallest); i = smallest; } else break;
        }
    }

    private void buildHeap() {
        for (int i = size / 2 - 1; i >= 0; i--) siftDown(i);
    }

    private void swap(int i, int j) {
        tracker.incrementSwaps();
        int t = heap[i];
        heap[i] = heap[j];
        heap[j] = t;
    }

    private void resize() {
        tracker.incrementAllocations();
        heap = Arrays.copyOf(heap, heap.length * 2);
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public int peek() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        return heap[0];
    }


    public int findIndexOf(int value) {
        for (int i = 0; i < size; i++) {
            if (heap[i] == value) return i;
        }
        return -1;
    }
}
