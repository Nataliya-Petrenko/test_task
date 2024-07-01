import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

// Algorithm for calculating the median of a stream of numbers from:
// https://www.geeksforgeeks.org/median-of-stream-of-integers-running-integers/

// Formula for calculating the average value of a stream of numbers:
// (prev_avg*n + x)/(n+1), where n from 0
// https://www.geeksforgeeks.org/average-of-a-stream-of-numbers/

public class FileProcessing {

    private int max;
    private int min;
    private int count;
    private double average;
    private double median;

    private final PriorityQueue<Double> greaterValues;
    private final PriorityQueue<Double> smallerValues;

    private List<Integer> increasingNumbers;
    private List<Integer> decreasingNumbers;

    private List<Integer> increasingNumbersMax;
    private List<Integer> decreasingNumbersMax;

    private final Path filePath;

    public FileProcessing(final Path filePath) {
        this.max = Integer.MIN_VALUE;
        this.min = Integer.MAX_VALUE;
        this.count = 0;
        this.average = 0;
        this.greaterValues = new PriorityQueue<>();
        this.smallerValues = new PriorityQueue<>();
        this.increasingNumbers = new ArrayList<>();
        this.decreasingNumbers = new ArrayList<>();
        this.increasingNumbersMax = new ArrayList<>();
        this.decreasingNumbersMax = new ArrayList<>();
        this.filePath = filePath;
    }

    public void calculateStatistics() {
        long start = System.currentTimeMillis();


        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            int number = 0;

            while ((line = reader.readLine()) != null) {

                try {
                    number = Integer.parseInt(line);
                    max = Math.max(max, number);
                    min = Math.min(min, number);

                    sortingNumbersForMedian(number);

                    count++; // for avg
                    average = (average * (count - 1) + number) / count;

                    increasingNumbersSequence(number);
                    decreasingNumbersSequence(number);

                } catch (NumberFormatException e) {
                    System.out.println("Invalid line (will be skipped): " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        median = median();

        updateIncreasingNumbersMax(increasingNumbersMax, increasingNumbers);
        updateDecreasingNumbersMax(decreasingNumbersMax, decreasingNumbers);

        long finish = System.currentTimeMillis();

        System.out.println("Statistics calculation took " + (finish - start) / 1000 + " seconds\n");

    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public double getMedian() {
        return median;
    }

    public double getAverage() {
        return average;
    }

    public List<Integer> getIncreasingNumbersMax() {
        return new ArrayList<>(increasingNumbersMax);
    }

    public List<Integer> getDecreasingNumbersMax() {
        return new ArrayList<>(decreasingNumbersMax);
    }

    public void showStatistics() {
        System.out.println("Max: " + max);
        System.out.println("Min: " + min);
        System.out.println("Median: " + median);
        System.out.println("Average: " + average);
        System.out.println("Longest increasing sequence: " + increasingNumbersMax);
        System.out.println("Longest decreasing sequence: " + decreasingNumbersMax);
        System.out.println();
    }

    private void sortingNumbersForMedian(final int number) {
        this.smallerValues.add(-1.0 * number);  // Negation for treating it as max heap
        Double smallerValue = this.smallerValues.poll();
        if (smallerValue != null) {
            this.greaterValues.add(-1.0 * smallerValue);  // Move the largest element
        }  // from s to g to keep s as a max-heap with the smaller half and g as a min-heap with the larger half.
        if (this.greaterValues.size() > this.smallerValues.size()) { // Balance the heaps:
            // if g has more elements than s, move the smallest element from g back to s.
            this.smallerValues.add(-1.0 * this.greaterValues.poll());
        }
    }

    private double median() {
        Double smallerValue = this.smallerValues.peek();
        Double greaterValue = this.greaterValues.peek();

        if (this.greaterValues.size() != this.smallerValues.size()) {
            if (smallerValue != null) {
                return -1.0 * smallerValue;
            } else {
                throw new IllegalStateException("Expected smallerValues to have an element.");
            }
        } else {
            if (smallerValue != null && greaterValue != null) {
                return (greaterValue - smallerValue) / 2;
            } else {
                throw new IllegalStateException("Expected smallerValues and  greaterValues to have elements.");
            }
        }
    }

    private void updateIncreasingNumbersMax(List<Integer> increasingNumbersMax, List<Integer> increasingNumbers) {
        if (increasingNumbers.size() > increasingNumbersMax.size()) {
            this.increasingNumbersMax = new ArrayList<>();
            this.increasingNumbersMax = List.copyOf(increasingNumbers);
        }
    }

    private void updateIncreasingNumbers(int number) {
        this.increasingNumbers = new ArrayList<>();
        this.increasingNumbers.add(number);
    }

    private void updateDecreasingNumbersMax(List<Integer> decreasingNumbersMax, List<Integer> decreasingNumbers) {
        if (decreasingNumbers.size() > decreasingNumbersMax.size()) {
            this.decreasingNumbersMax = new ArrayList<>();
            this.decreasingNumbersMax = List.copyOf(decreasingNumbers);
        }
    }

    private void updateDecreasingNumbers(int number) {
        this.decreasingNumbers = new ArrayList<>();
        this.decreasingNumbers.add(number);
    }

    private void increasingNumbersSequence(int number) {
        if (count == 1) {
            increasingNumbers.add(number);
            increasingNumbersMax.add(number);
        } else {
            if (number > increasingNumbers.get(increasingNumbers.size() - 1)) {
                increasingNumbers.add(number);
            } else {
                updateIncreasingNumbersMax(increasingNumbersMax, increasingNumbers);
                updateIncreasingNumbers(number);
            }
        }
    }

    private void decreasingNumbersSequence(int number) {
        if (count == 1) {
            decreasingNumbers.add(number);
            decreasingNumbersMax = List.copyOf(decreasingNumbers);
        } else {
            if (number < decreasingNumbers.get(decreasingNumbers.size() - 1)) {
                decreasingNumbers.add(number);
            } else {
                updateDecreasingNumbersMax(decreasingNumbersMax, decreasingNumbers);
                updateDecreasingNumbers(number);
            }
        }
    }
}

