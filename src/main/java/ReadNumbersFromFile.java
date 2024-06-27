import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;

// reading 17 s
public class ReadNumbersFromFile {
    public static void main(String[] args) {

        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
//        double median = 0; // ??????????
        int count = 0;
        double avg = 0;

        // Declaring two min heap
        PriorityQueue<Double> greaterValues = new PriorityQueue<>();
        PriorityQueue<Double> smallerValues = new PriorityQueue<>();

        long start = System.currentTimeMillis();

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

                        avg = (avg * count + number) / (count + 1);

                        // Average //


                    } catch (NumberFormatException e) {
                        // Handle the case where the token is not a valid integer
                        System.out.println("Invalid number: " + token);
                    }




                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        double median;
        // MEDIAN
        if (greaterValues.size() != smallerValues.size()) {
            median = -1.0 * smallerValues.peek();
        } else {
            median = (greaterValues.peek() - smallerValues.peek()) / 2;
        }
        // MEDIAN //

        long finish = System.currentTimeMillis();
        System.out.println("Max: " + max);
        System.out.println("Min: " + min);
        System.out.println("Median: " + median);
        System.out.println("Average: " + avg);


        System.out.println( "It took " + (finish - start) / 1000 + " seconds");

    }
}

