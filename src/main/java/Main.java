public class Main {
    public static void main(String[] args) {
        String filePath = "src/main/resources/10m.txt";
        ReadNumbersFromFile readNumbersFromFile = new ReadNumbersFromFile(filePath);
        readNumbersFromFile.readingFile();
    }
}
