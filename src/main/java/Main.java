import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/main/resources/10m.txt";
        ReadNumbersFromFile readNumbersFromFile = new ReadNumbersFromFile(filePath);
        try {
            readNumbersFromFile.readingFile();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);  // todo ask about another file
        }
    }
}
