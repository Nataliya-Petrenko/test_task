package org.nataliia_petrenko;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadNumbersFromFile {
    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        String filePath = "src/main/resources/10m.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                // Split the line into tokens based on whitespace
                String[] tokens = line.split("\\s+");

                for (String token : tokens) {
                    try {
                        // Parse the token as an integer
                        int number = Integer.parseInt(token);
                        // Print the number (or process it as needed)
                        System.out.println(number);
                    } catch (NumberFormatException e) {
                        // Handle the case where the token is not a valid integer
                        System.out.println("Invalid number: " + token);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        long finish = System.currentTimeMillis();

        System.out.println( "It took " + (finish - start) / 1000 + " seconds");

    }
}

