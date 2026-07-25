package levelfive.backuptool;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class FolderBackupTool {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("========== Automated Folder Backup Tool ==========");

        System.out.print("Enter Source Folder Path: ");
        String sourcePath = scanner.nextLine().trim();

        System.out.print("Enter Backup Destination Path: ");
        String destinationPath = scanner.nextLine().trim();

        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath);

        if (!Files.exists(source) || !Files.isDirectory(source)) {
            System.out.println("Invalid source folder.");
            scanner.close();
            return;
        }

        Path backupFolder = destination.resolve(source.getFileName() + "_Backup");
        AtomicInteger copiedFiles = new AtomicInteger();

        try {
            Files.createDirectories(backupFolder);
            Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult preVisitDirectory(Path directory,
                                                         BasicFileAttributes attributes)
                        throws IOException {

                    Path targetDirectory =
                            backupFolder.resolve(source.relativize(directory));

                    Files.createDirectories(targetDirectory);

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file,
                                                 BasicFileAttributes attributes)
                        throws IOException {

                    Path targetFile =
                            backupFolder.resolve(source.relativize(file));

                    Files.copy(file,
                            targetFile,
                            StandardCopyOption.REPLACE_EXISTING);

                    copiedFiles.incrementAndGet();

                    System.out.println("Copied : " + file.getFileName());
                    return FileVisitResult.CONTINUE;
                }
            });

            System.out.println("\n========== Backup Completed ==========");
            System.out.println("Backup Location : " + backupFolder);
            System.out.println("Total Files Copied : " + copiedFiles.get());

        } catch (IOException exception) {

            System.out.println("Backup failed.");
            System.out.println(exception.getMessage());

        }
        scanner.close();
    }
}