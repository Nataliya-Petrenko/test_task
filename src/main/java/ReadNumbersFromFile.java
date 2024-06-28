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

        int countIncreasingNumbers = 0;
        int countDecreasingNumbers = 0;
        List<Integer> increasingNumbers = new ArrayList<>();
        List<Integer> decreasingNumbers = new ArrayList<>();

        int countIncreasingNumbersMax = 0;
        int countDecreasingNumbersMax = 0;
        List<Integer> increasingNumbersMax = new ArrayList<>();
        List<Integer> decreasingNumbersMax = new ArrayList<>();

        String filePath = "src/main/resources/10m.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                int number;
                // Split the line into tokens based on whitespace
                String[] tokens = line.split("\\s+");
                for (String token : tokens) {

                    try {
                        // Parse the token as an integer
                        number = Integer.parseInt(token);
                        // Print the number (or process it as needed)
//                        System.out.println(number);


                        count++;

                        // MAX and MIN VALUE
                        max = maxNumber(count, number, max);
                        min = minNumber(count, number, min);

                        // MEDIAN  https://www.geeksforgeeks.org/median-of-stream-of-integers-running-integers/
                        sortingNumbersForMedian(smallerValues, greaterValues, number);

                        // Average (prev_avg*n + x)/(n+1)  https://www.geeksforgeeks.org/average-of-a-stream-of-numbers/
                        avg = (avg * count + number) / (count + 1);

                        // sequence of numbers

                        if (count == 1) {

                            // for increasing
                            increasingNumbers.add(number);
                            countIncreasingNumbers++;
                            increasingNumbersMax = List.copyOf(increasingNumbers);
                            countIncreasingNumbersMax = 1;

                            // for decreasing
                            decreasingNumbers.add(number);
                            countDecreasingNumbers++;
                            decreasingNumbersMax = List.copyOf(decreasingNumbers);
                            countDecreasingNumbersMax = 1;
                        } else {
                            // for increasing
                            if (number > increasingNumbers.get(increasingNumbers.size() - 1)) {
                                increasingNumbers.add(number);
                                countIncreasingNumbers++;
                            } else {
                                if (countIncreasingNumbers > countIncreasingNumbersMax) { // is new sequence of numbers longer?
                                    increasingNumbersMax = new ArrayList<>();                         // clear old max sequence
                                    increasingNumbersMax = List.copyOf(increasingNumbers); // assign new max sequence
                                    countIncreasingNumbersMax = countIncreasingNumbers;    // assign new max count
                                }
                                increasingNumbers = new ArrayList<>();         // clear current sequence
                                increasingNumbers.add(number);    // add current into new current sequence
                                countIncreasingNumbers = 1;       // start new current count
                            }

                            // for decreasing

                            if (number < decreasingNumbers.get(decreasingNumbers.size() - 1)) { // todo якщо це останнє число то перевірити цей масив на максимальний
                                decreasingNumbers.add(number);
                                countDecreasingNumbers++;
                            } else {
                                if (countDecreasingNumbers > countDecreasingNumbersMax) { // is new sequence of numbers longer?
                                    decreasingNumbersMax = new ArrayList<>();                         // clear old max sequence
                                    decreasingNumbersMax = List.copyOf(decreasingNumbers); // assign new max sequence
                                    countDecreasingNumbersMax = countDecreasingNumbers;    // assign new max count
                                }
                                decreasingNumbers = new ArrayList<>();         // clear current sequence
                                decreasingNumbers.add(number);    // add current into new current sequence
                                countDecreasingNumbers = 1;       // start new current count
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

        long finish = System.currentTimeMillis();
        System.out.println();
        System.out.println("Max: " + max);
        System.out.println("Min: " + min);
        System.out.println("Median: " + median);
        System.out.println("Average: " + avg);
        System.out.println();
        System.out.println("increasingNumbersMax: " + increasingNumbersMax);
        System.out.println("countIncreasingNumbersMax: " + countIncreasingNumbersMax);
        System.out.println("decreasingNumbersMax: " + decreasingNumbersMax);
        System.out.println("countDecreasingNumbersMax: " + countDecreasingNumbersMax);
        System.out.println();
        System.out.println( "It took " + (finish - start) / 1000 + " seconds");

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
            return  (greaterValues.peek() - smallerValues.peek()) / 2;
        }
    }
}

