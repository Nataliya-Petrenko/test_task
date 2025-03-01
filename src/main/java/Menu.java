import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class Menu {
    private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));
    private static final Path DEFAULT_PATH = Path.of("src/main/resources/10m.txt");

    public Path readPathFromConsole() {
        String line;

        do {
            showMenu();
            line = readLineFromConsole();
            if (line.equals("0")) {
                System.exit(0);
            }
            if (line.equals("1")) {
                if (isValidFile(DEFAULT_PATH)) {
                    System.out.println("Selected file: " + DEFAULT_PATH + "\n");
                    return DEFAULT_PATH;
                }
            } else {
                Path newPath = Path.of(line);
                if (isValidFile(newPath)) {
                    System.out.println("Selected file: " + newPath + "\n");
                    return newPath;
                }
            }
        } while (true);

    }

    private boolean isValidFile(final Path filePath) {
        if (!isTxtFile(filePath)) {
            System.out.println("The file is not a *.txt file: " + filePath + "\n");
            return false;
        }
        if (!fileExists(filePath)) {
            System.out.println("The file does not exist: " + filePath + "\n");
            return false;
        }
        if (!isNonEmpty(filePath)) {
            System.out.println("The file is empty: " + filePath + "\n");
            return false;
        }
        return true;
    }

    private boolean fileExists(final Path filePath) {
        return Files.exists(filePath);
    }

    private boolean isNonEmpty(final Path filePath) {
        try {
            return Files.size(filePath) > 0;
        } catch (IOException e) {
            System.out.println("Error checking file size: " + e.getMessage());
            return false;
        }
    }

    private boolean isTxtFile(final Path filePath) {
        return filePath.toString().endsWith(".txt");
    }

    private void showMenu() {
        System.out.println("--------------------------------------------------------------------------------------\n");
        System.out.println("WARNING! " +
                "Only *.txt files are supported.\n" +
                "Each line must contain only one integer, lines that do not meet this criterion will be ignored.\n\n" +
                "Do you want to get statistic from default file (10m.txt)? Enter 1\n" +
                "Do you want to get statistic from your file? Enter file path\n" +
                "Enter 0 to exit the program\n");
    }

    private String readLineFromConsole() {
        try {
            return READER.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
