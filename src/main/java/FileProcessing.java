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

                    count++;
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

        updateIncreasingNumbersMax();
        updateDecreasingNumbersMax();

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
        smallerValues.add(-1.0 * number);  // Negation for treating it as max heap
        Double smallerValue = smallerValues.poll();
        if (smallerValue != null) {
            greaterValues.add(-1.0 * smallerValue);  // Move the largest element
        }  // from s to g to keep s as a max-heap with the smaller half and g as a min-heap with the larger half.
        if (greaterValues.size() > smallerValues.size()) { // Balance the heaps:
            // if g has more elements than s, move the smallest element from g back to s.
            smallerValues.add(-1.0 * greaterValues.poll());
        }
    }

    private double median() {
        Double smallerValue = smallerValues.peek();
        Double greaterValue = greaterValues.peek();

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

    private void updateIncreasingNumbersMax() {
        if (increasingNumbers.size() > increasingNumbersMax.size()) {
            increasingNumbersMax = new ArrayList<>();
            increasingNumbersMax = List.copyOf(increasingNumbers);
        }
    }

    private void updateIncreasingNumbers(final int number) {
        increasingNumbers = new ArrayList<>();
        increasingNumbers.add(number);
    }

    private void updateDecreasingNumbersMax() {
        if (decreasingNumbers.size() > decreasingNumbersMax.size()) {
            decreasingNumbersMax = new ArrayList<>();
            decreasingNumbersMax = List.copyOf(decreasingNumbers);
        }
    }

    private void updateDecreasingNumbers(final int number) {
        decreasingNumbers = new ArrayList<>();
        decreasingNumbers.add(number);
    }

    private void increasingNumbersSequence(final int number) {
        if (count == 1) {
            increasingNumbers.add(number);
            increasingNumbersMax.add(number);
        } else {
            if (number > increasingNumbers.get(increasingNumbers.size() - 1)) {
                increasingNumbers.add(number);
            } else {
                updateIncreasingNumbersMax();
                updateIncreasingNumbers(number);
            }
        }
    }

    private void decreasingNumbersSequence(final int number) {
        if (count == 1) {
            decreasingNumbers.add(number);
            decreasingNumbersMax = List.copyOf(decreasingNumbers);
        } else {
            if (number < decreasingNumbers.get(decreasingNumbers.size() - 1)) {
                decreasingNumbers.add(number);
            } else {
                updateDecreasingNumbersMax();
                updateDecreasingNumbers(number);
            }
        }
    }
}

