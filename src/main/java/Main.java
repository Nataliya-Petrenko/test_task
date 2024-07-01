import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Path path;

        while (true) {
            Menu menu = new Menu();
            path = menu.readPathFromConsole();
            System.out.println("Selected file: " + path.toString() + "\n");

            FileProcessing fileProcessing = new FileProcessing(path);
            fileProcessing.readingFileAndShowStatistic();

            System.out.println("Max: " + fileProcessing.getMax());
            System.out.println("Min: " + fileProcessing.getMin());
            System.out.println("Median: " + fileProcessing.getMedian());
            System.out.println("Average: " + fileProcessing.getAvg());
            System.out.println("Longest increasing sequence: " + fileProcessing.getIncreasingNumbersMax());
            System.out.println("Longest decreasing sequence: " + fileProcessing.getDecreasingNumbersMax());
            System.out.println();

        }

    }
}
