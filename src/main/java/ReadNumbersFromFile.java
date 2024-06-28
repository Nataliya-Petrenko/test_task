import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

// reading 17 s
// 1-4 <30s
public class ReadNumbersFromFile {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        double median;
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

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                int number;
                // Split the line into tokens based on whitespace
                String[] tokens = line.split("\\s+");
                for (String token : tokens) {

                    try {
                        // Parse the token as an integer
                        number = Integer.parseInt(token);
                        // Print the number (or process it as needed)
                        System.out.println(number);


                        count++;

                        // MAX and MIN VALUE

                        if (count == 1) {
                            max = number;
                            min = number;
                        } else {
                            if (max < number) {
                                max = number;
                            }
                            if (min > number) {
                                min = number;
                            }
                        }

                        // MAX and MIN VALUE //

                        // MEDIAN
                        // https://www.geeksforgeeks.org/median-of-stream-of-integers-running-integers/

                        // Negation for treating it as max heap
                        smallerValues.add(-1.0 * number); // Negate array[i] and add to s to simulate max-heap behavior.
                        greaterValues.add(-1.0 * smallerValues.poll()); // Move the largest element
                        // from s to g to keep s as a max-heap with the smaller half and g as a min-heap with the larger half.
                        if (greaterValues.size() > smallerValues.size()) { // Balance the heaps:
                            // if g has more elements than s, move the smallest element from g back to s.
                            smallerValues.add(-1.0 * greaterValues.poll());
                        }

                        // MEDIAN //

                        // Average (prev_avg*n + x)/(n+1)
                        // https://www.geeksforgeeks.org/average-of-a-stream-of-numbers/

                        avg = (avg * count + number) / (count + 1);

                        // Average //

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
        if (greaterValues.size() != smallerValues.size()) {
            median = -1.0 * smallerValues.peek();
        } else {
            median = (greaterValues.peek() - smallerValues.peek()) / 2;
        }
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
}

