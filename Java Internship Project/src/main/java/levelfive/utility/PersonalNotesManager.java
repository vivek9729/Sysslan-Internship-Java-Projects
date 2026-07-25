package levelfive.utility;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PersonalNotesManager {

    private static final String FILE_NAME = "notes.txt";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;

        do {
            displayMenu();
            while (!scanner.hasNextInt()) {
                System.out.print("Enter a valid option: ");
                scanner.next();
            }

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addNote();
                    break;
                case 2:
                    viewNotes();
                    break;
                case 3:
                    searchNotes();
                    break;
                case 4:
                    deleteNote();
                    break;
                case 5:
                    showStatistics();
                    break;
                case 6:
                    System.out.println("Application closed.");
                    break;

                default:
                    System.out.println("Invalid option.");
            }

        } while (choice != 6);
        scanner.close();
    }

    private static void displayMenu() {

        System.out.println("\n========== Personal Notes Manager ==========");
        System.out.println("1. Add Note");
        System.out.println("2. View Notes");
        System.out.println("3. Search Notes");
        System.out.println("4. Delete Note");
        System.out.println("5. Show Statistics");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    private static void addNote() {

        System.out.print("Enter Note Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter Note Content: ");
        String content = scanner.nextLine();

        String dateTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(FILE_NAME, true))) {

            writer.write("[" + dateTime + "]");
            writer.newLine();

            writer.write("Title: " + title);
            writer.newLine();

            writer.write(content);
            writer.newLine();

            writer.write("--------------------------------------------");
            writer.newLine();

            System.out.println("Note saved successfully.");

        } catch (IOException exception) {
            System.out.println("Unable to save note.");
        }
    }

    private static void viewNotes() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No notes available.");
            return;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException exception) {
            System.out.println("Unable to read notes.");
        }
    }

    private static void searchNotes() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No notes available.");
            return;
        }
        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine().toLowerCase();

        boolean found = false;

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.toLowerCase().contains(keyword)) {
                    System.out.println(line);
                    found = true;
                }
            }

        } catch (IOException exception) {
            System.out.println("Unable to search notes.");
        }

        if (!found) {
            System.out.println("No matching notes found.");
        }
    }

    private static void deleteNote() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No notes available.");
            return;
        }

        System.out.print("Enter note title to delete: ");
        String title = scanner.nextLine();
        List<String> lines = new ArrayList<>();

        boolean skip = false;
        boolean deleted = false;

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.equals("Title: " + title)) {
                    skip = true;
                    deleted = true;
                    continue;
                }

                if (skip && line.equals("--------------------------------------------")) {
                    skip = false;
                    continue;
                }

                if (!skip) {
                    lines.add(line);
                }
            }

        } catch (IOException exception) {
            System.out.println("Unable to process file.");
            return;
        }

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException exception) {
            System.out.println("Unable to update notes.");
            return;
        }

        if (deleted) {
            System.out.println("Note deleted successfully.");
        } 
        
        else {
            System.out.println("Title not found.");
        }
    }

    private static void showStatistics() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No notes available.");
            return;
        }

        int noteCount = 0;
        int lineCount = 0;

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                lineCount++;

                if (line.startsWith("Title:")) {
                    noteCount++;
                }
            }

        } catch (IOException exception) {
            System.out.println("Unable to read notes.");
            return;
        }

        System.out.println("\n========== Statistics ==========");
        System.out.println("Total Notes : " + noteCount);
        System.out.println("Total Lines : " + lineCount);
    }
}