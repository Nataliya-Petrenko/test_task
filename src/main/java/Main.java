import java.io.FileNotFoundException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {

        while (true) {
            Menu menu = new Menu();
            Path path = menu.readPathFromConsole();
            System.out.println("File selected: " + path.toString() + "\n");
        }



//        String filePath = "src/main/resources/10m.txt";
//        ReadNumbersFromFile readNumbersFromFile = new ReadNumbersFromFile(filePath);
//        try {
//            readNumbersFromFile.readingFile();
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);  // todo ask about another file
//        }


    }
}
