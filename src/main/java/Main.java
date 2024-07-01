import java.io.FileNotFoundException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path path;

        while (true) {
            Menu menu = new Menu();
            path = menu.readPathFromConsole();
            System.out.println("File selected: " + path.toString() + "\n");

            ReadNumbersFromFile readNumbersFromFile = new ReadNumbersFromFile(path);
            readNumbersFromFile.readingFile();
        }



    }
}
