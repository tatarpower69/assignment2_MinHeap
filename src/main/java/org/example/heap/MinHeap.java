package org.example.heap;

import org.example.metrics.PerformanceTracker;
import java.util.Arrays;

public class MinHeap {
    private int[] heap;
    private int size;
    private final PerformanceTracker tracker;

    public MinHeap(int capacity, PerformanceTracker tracker) {
        this.heap = new int[capacity];
        this.size = 0;
        this.tracker = tracker;
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
        int[] merged = Arrays.copyOf(heap, size + other.size);
        System.arraycopy(other.heap, 0, merged, size, other.size);
        heap = merged;
        size += other.size;
        buildHeap();
    }

    private void siftUp(int i) {
        while (i > 0 && heap[parent(i)] > heap[i]) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    private void siftDown(int i) {
        int min = i;
        int left = left(i);
        int right = right(i);
        if (left < size && heap[left] < heap[min]) min = left;
        if (right < size && heap[right] < heap[min]) min = right;
        if (min != i) {
            swap(i, min);
            siftDown(min);
        }
    }

    private void buildHeap() {
        for (int i = size / 2 - 1; i >= 0; i--) siftDown(i);
    }

    private void swap(int i, int j) {
        tracker.incrementSwaps();
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    private int parent(int i) { return (i - 1) / 2; }
    private int left(int i) { return 2 * i + 1; }
    private int right(int i) { return 2 * i + 2; }

    private void resize() {
        heap = Arrays.copyOf(heap, heap.length * 2);
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public int peek() { return size == 0 ? Integer.MIN_VALUE : heap[0]; }
}
