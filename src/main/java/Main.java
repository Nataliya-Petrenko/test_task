import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {

        Path path;

        while (true) {
            Menu menu = new Menu();
            path = menu.readPathFromConsole();
            System.out.println("Selected file: " + path.toString() + "\n");

            FileProcessing fileProcessing = new FileProcessing(path);
            fileProcessing.calculateStatistics();
            fileProcessing.showStatistics();
        }

    }
}
