import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {

        Path path;
        Menu menu = new Menu();

        while (true) {
            path = menu.readPathFromConsole();
            FileProcessing fileProcessing = new FileProcessing(path);
            fileProcessing.calculateStatistics();
            fileProcessing.showStatistics();
        }

    }
}
