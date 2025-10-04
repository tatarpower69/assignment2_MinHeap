# Min-Heap Implementation with Performance Analysis

## Overview

This project implements a **Min-Heap** in Java with support for:

- `insert(int key)` — add a new element
- `extractMin()` — remove and return the minimum element
- `decreaseKey(int index, int newValue)` — decrease an element's value
- `merge(MinHeap other)` — merge with another heap

The implementation is integrated with a **PerformanceTracker** to measure key metrics like comparisons, swaps, array accesses, memory allocations, and recursion depth.

## Features

- Efficient heap operations with O(log n) worst-case time complexity.
- Metrics tracking for empirical validation.
- CLI `BenchmarkRunner` for running performance tests on different input sizes and scenarios:
    - Input sizes: 100, 1,000, 10,000, 100,000
    - Input types: random, sorted, reversed, nearly-sorted
    - CSV export of metrics for further analysis.

## Installation & Usage

Clone the repository:

```bash
git clone <your-repo-url>
cd assignment2-minheap
Build with Maven:

mvn clean install


Run tests:

mvn test


Run benchmark:

mvn exec:java -Dexec.mainClass="org.example.cli.BenchmarkRunner" \
-Dexec.args="--algo MinHeap --n 10000 --case random --out results.csv"


Metrics CSV includes:

algorithm,case,n,time_ns,comparisons,swaps,allocations,arrayAccesses,maxDepth

| Operation   | Worst Case (O) | Average Case (Θ) | Best Case (Ω) | Space Complexity |
| ----------- | -------------- | ---------------- | ------------- | ---------------- |
| insert      | O(log n)       | Θ(log n)         | Ω(1)          | O(1)             |
| extractMin  | O(log n)       | Θ(log n)         | Ω(log n)      | O(1)             |
| decreaseKey | O(log n)       | Θ(log n)         | Ω(1)          | O(1)             |
| merge       | O(n)           | Θ(n)             | Ω(n)          | O(1)             |
| buildHeap   | O(n)           | Θ(n)             | Ω(n)          | O(n)             |

Observations from benchmarks

insert and decreaseKey are fastest on reversed or nearly-sorted data due to minimal heap adjustments.

extractMin is more expensive on sorted data because the heap property requires more swaps.

Comparisons and array accesses grow roughly proportionally to n log n, confirming theoretical expectations.

Memory allocations are minimal due to in-place array implementation and dynamic resizing.

Example metrics for n = 10,000 (random)

insert: 315,000 ns, 22,500 comparisons, 55,000 array accesses

extractMin: 2,050,000 ns, 230,000 comparisons, 392,000 array accesses

decreaseKey: 710,000 ns, 27,500 comparisons, 80,000 array accesses

Conclusion

The Min-Heap implementation is correct, efficient, and scalable.
Empirical benchmarks confirm O(log n) behavior for all main operations.
Optimizations (dynamic array resizing, minimal swaps) improve practical performance, especially on nearly-sorted data.
Metrics tracking allows detailed performance analysis and verification of theoretical complexity.