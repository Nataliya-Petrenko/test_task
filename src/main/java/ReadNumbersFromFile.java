import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
// https://www.geeksforgeeks.org/median-of-stream-of-integers-running-integers/

// todo Warning! Only the first number in the line is read. If there are several numbers in one line, all but the first one will be ignored.

/*
Max: 49999978
Min: -49999996
Median: 25216.0
Average: 7364.4177062000945

increasingNumbersMax: [-48190694, -47725447, -43038241, -20190291, -17190728, -6172572, 8475960, 25205909, 48332507, 48676185]
countIncreasingNumbersMax: 10
decreasingNumbersMax: [47689379, 42381213, 30043880, 12102356, -4774057, -5157723, -11217378, -23005285, -23016933, -39209115, -49148762]
countDecreasingNumbersMax: 11

It took 7 seconds
 */

public class ReadNumbersFromFile { // todo Optional for possible Null

    private int max;
    private int min;
    private int count;
    private double avg;

    private final PriorityQueue<Double> greaterValues;
    private final PriorityQueue<Double> smallerValues;

    private List<Integer> increasingNumbers;
    private List<Integer> decreasingNumbers;

    private List<Integer> increasingNumbersMax;
    private List<Integer> decreasingNumbersMax;

    private final Path filePath;

    public ReadNumbersFromFile(final Path filePath) {
        this.max = Integer.MIN_VALUE;
        this.min = Integer.MAX_VALUE;
        this.count = 0;
        this.avg = 0;
        this.greaterValues = new PriorityQueue<>();
        this.smallerValues = new PriorityQueue<>();
        this.increasingNumbers = new ArrayList<>();
        this.decreasingNumbers = new ArrayList<>();
        this.increasingNumbersMax = new ArrayList<>();
        this.decreasingNumbersMax = new ArrayList<>();
        this.filePath = filePath;
    }

    public void readingFile() {
        long start = System.currentTimeMillis();


        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            int number = 0;

            while ((line = reader.readLine()) != null) {

                try {
                    number = Integer.parseInt(line); // Parse the token as an integer
                    max = Math.max(max, number);
                    min = Math.min(min, number);

                    sortingNumbersForMedian(number); // for MEDIAN  https://www.geeksforgeeks.org/median-of-stream-of-integers-running-integers/

                    count++; // for avg
                    avg = (avg * (count - 1) + number) / count; // Average (prev_avg*n + x)/(n+1) where n from 0 https://www.geeksforgeeks.org/average-of-a-stream-of-numbers/

                    increasingNumbersSequence(number);
                    decreasingNumbersSequence(number);

                } catch (NumberFormatException e) {
                    System.out.println("Invalid line (will be skipped): " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        double median = median(); // MEDIAN

        updateIncreasingNumbersMax(increasingNumbersMax, increasingNumbers);  // todo check with last number? (перевіряти, бо поточна послідовність перевіряється на макс кількість елементів тільки пілся того, як нове поточне число міняє напрямок, а додати перевірку чи є наступне число я не зрозуміла як
        updateDecreasingNumbersMax(decreasingNumbersMax, decreasingNumbers);

        long finish = System.currentTimeMillis();

        System.out.println("Max: " + max);
        System.out.println("Min: " + min);
        System.out.println("Median: " + median);
        System.out.println("Average: " + avg);
        System.out.println();
        System.out.println("increasingNumbersMax: " + increasingNumbersMax);
        System.out.println("countIncreasingNumbersMax: " + increasingNumbersMax.size());
        System.out.println("decreasingNumbersMax: " + decreasingNumbersMax);
        System.out.println("countDecreasingNumbersMax: " + decreasingNumbersMax.size());
        System.out.println();
        System.out.println("It took " + (finish - start) / 1000 + " seconds\n");

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
        if (increasingNumbers.size() > increasingNumbersMax.size()) { // is new sequence of numbers longer?
            this.increasingNumbersMax = new ArrayList<>();
            this.increasingNumbersMax = List.copyOf(increasingNumbers); // assign new max sequence
        }
    }

    private void updateIncreasingNumbers(int number) {
        this.increasingNumbers = new ArrayList<>();         // clear current sequence
        this.increasingNumbers.add(number);    // add current into new current sequence
    }

    private void updateDecreasingNumbersMax(List<Integer> decreasingNumbersMax, List<Integer> decreasingNumbers) {
        if (decreasingNumbers.size() > decreasingNumbersMax.size()) { // is new sequence of numbers longer?
            this.decreasingNumbersMax = new ArrayList<>();
            this.decreasingNumbersMax = List.copyOf(decreasingNumbers); // assign new max sequence
        }
    }

    private void updateDecreasingNumbers(int number) {
        this.decreasingNumbers = new ArrayList<>();         // clear current sequence
        this.decreasingNumbers.add(number);    // add current into new current sequence
    }

    private void increasingNumbersSequence(int number) {
        if (count == 1) {
            increasingNumbers.add(number);
            increasingNumbersMax.add(number);
        } else {
            if (number > increasingNumbers.get(increasingNumbers.size() - 1)) {
                increasingNumbers.add(number);
            } else {  // todo if delete else then we can not to do check after while
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
            } else { //todo if delete else then we can not to do check after while
                updateDecreasingNumbersMax(decreasingNumbersMax, decreasingNumbers);
                updateDecreasingNumbers(number);
            }
        }
    }
}

