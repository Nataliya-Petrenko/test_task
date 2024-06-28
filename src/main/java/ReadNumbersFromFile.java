import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
// https://www.geeksforgeeks.org/median-of-stream-of-integers-running-integers/


// reading 17 s
// 1-4 <30s

//It took 26 seconds with sout numbers

/*
Max: 49999978
Min: -49999996
Median: 25216.0
Average: 7364.4177062000945

increasingNumbersMax: [-48190694, -47725447, -43038241, -20190291, -17190728, -6172572, 8475960, 25205909, 48332507, 48676185]
countIncreasingNumbersMax: 10
decreasingNumbersMax: [47689379, 42381213, 30043880, 12102356, -4774057, -5157723, -11217378, -23005285, -23016933, -39209115, -49148762]
countDecreasingNumbersMax: 11

It took 9 seconds
 */

public class ReadNumbersFromFile { // todo Optional for possible Null
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        int count = 0;
        double avg = 0;

        PriorityQueue<Double> greaterValues = new PriorityQueue<>();
        PriorityQueue<Double> smallerValues = new PriorityQueue<>();

        List<Integer> increasingNumbers = new ArrayList<>();
        List<Integer> decreasingNumbers = new ArrayList<>();

        List<Integer> increasingNumbersMax = new ArrayList<>();
        List<Integer> decreasingNumbersMax = new ArrayList<>();

        String filePath = "src/main/resources/10m.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                int number;

                String[] tokens = line.split("\\s+"); // Split the line into tokens based on whitespace
                for (String token : tokens) {

                    try {
                        number = Integer.parseInt(token); // Parse the token as an integer
                        count++; // for avg
                        max = maxNumber(count, number, max); // MAX and MIN VALUE
                        min = minNumber(count, number, min); // MAX and MIN VALUE
                        sortingNumbersForMedian(smallerValues, greaterValues, number); // for MEDIAN  https://www.geeksforgeeks.org/median-of-stream-of-integers-running-integers/
                        avg = (avg * count + number) / (count + 1); // Average (prev_avg*n + x)/(n+1)  https://www.geeksforgeeks.org/average-of-a-stream-of-numbers/

                        // sequence of numbers
                        // for increasing
                        if (count == 1) {
                            increasingNumbers.add(number);
                            increasingNumbersMax.add(number);
                        } else {
                            if (number > increasingNumbers.get(increasingNumbers.size() - 1)) {
                                increasingNumbers.add(number);
                            } else {
                                increasingNumbersMax = updateIncreasingNumbersMax(increasingNumbersMax, increasingNumbers);
                                increasingNumbers = updateIncreasingNumbers(number);
                            }
                        }
                        // for decreasing
                        if (count == 1) {
                            decreasingNumbers.add(number);
                            decreasingNumbersMax = List.copyOf(decreasingNumbers);
                        } else {
                            if (number < decreasingNumbers.get(decreasingNumbers.size() - 1)) {
                                decreasingNumbers.add(number);
                            } else {
                                decreasingNumbersMax = updateDecreasingNumbersMax(decreasingNumbersMax, decreasingNumbers);
                                decreasingNumbers = updateDecreasingNumbers(number);
                            }
                        }
                        // sequence of numbers //

                    } catch (NumberFormatException e) {
                        // Handle the case where the token is not a valid integer
                        System.out.println("Invalid number: " + token);
                    }


                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        // MEDIAN
        double median = median(smallerValues, greaterValues);
        // MEDIAN //

        increasingNumbersMax = updateIncreasingNumbersMax(increasingNumbersMax, increasingNumbers);  // todo check with last number? (перевіряти, бо поточна послідовність перевіряється на макс кількість елементів тільки пілся того, як нове поточне число міняє напрямок, а додати перевірку чи є наступне число я не зрозуміла як
        decreasingNumbersMax = updateDecreasingNumbersMax(decreasingNumbersMax, decreasingNumbers);

        long finish = System.currentTimeMillis();
        System.out.println();
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
        System.out.println("It took " + (finish - start) / 1000 + " seconds");

    }

    public static int maxNumber(final int count, final int number, final int max) {
        if (count == 1) {
            return number;
        } else {
            if (max < number) {
                return number;
            }
            return max;
        }
    }

    public static int minNumber(final int count, final int number, final int min) {
        if (count == 1) {
            return number;
        } else {
            if (min > number) {
                return number;
            }
            return min;
        }
    }

    public static void sortingNumbersForMedian(final PriorityQueue<Double> smallerValues, final PriorityQueue<Double> greaterValues, final int number) { // todo PriorityQueue by this?
        smallerValues.add(-1.0 * number); // Negate array[i] and add to s to simulate max-heap behavior.  // Negation for treating it as max heap
        greaterValues.add(-1.0 * smallerValues.poll()); // Move the largest element
        // from s to g to keep s as a max-heap with the smaller half and g as a min-heap with the larger half.
        if (greaterValues.size() > smallerValues.size()) { // Balance the heaps:
            // if g has more elements than s, move the smallest element from g back to s.
            smallerValues.add(-1.0 * greaterValues.poll());
        }
    }

    /*
            if (greaterValues.size() != smallerValues.size()) {
            median = -1.0 * smallerValues.peek();
        } else {
            median = (greaterValues.peek() - smallerValues.peek()) / 2;
        }
     */

    public static double median(final PriorityQueue<Double> smallerValues, final PriorityQueue<Double> greaterValues) {
        if (greaterValues.size() != smallerValues.size()) {
            return -1.0 * smallerValues.peek();
        } else {
            return (greaterValues.peek() - smallerValues.peek()) / 2;
        }
    }

    public static List<Integer> updateIncreasingNumbersMax(List<Integer> increasingNumbersMax, List<Integer> increasingNumbers) {
        if (increasingNumbers.size() > increasingNumbersMax.size()) { // is new sequence of numbers longer?
            return List.copyOf(increasingNumbers); // assign new max sequence
        }
        return increasingNumbersMax;
    }

    public static List<Integer> updateIncreasingNumbers(int number) {
        List<Integer> increasingNumbers = new ArrayList<>();         // clear current sequence
        increasingNumbers.add(number);    // add current into new current sequence
        return increasingNumbers;
    }

    public static List<Integer> updateDecreasingNumbersMax(List<Integer> decreasingNumbersMax, List<Integer> decreasingNumbers) {
        if (decreasingNumbers.size() > decreasingNumbersMax.size()) { // is new sequence of numbers longer?
            return List.copyOf(decreasingNumbers); // assign new max sequence
        }
        return decreasingNumbersMax;
    }

    public static List<Integer> updateDecreasingNumbers(int number) {
        List<Integer> decreasingNumbers = new ArrayList<>();         // clear current sequence
        decreasingNumbers.add(number);    // add current into new current sequence
        return decreasingNumbers;
    }

}

