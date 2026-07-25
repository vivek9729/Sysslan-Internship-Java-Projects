package levelfive.inventory;

import java.io.*;
import java.util.*;

public class InventoryManagementSystem {
    private static final String FILE_NAME = "inventory.csv";
    private static final List<Product> inventory = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        loadInventory();
        int choice;

        do {
            System.out.println("\n========== Inventory Management ==========");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Search Product");
            System.out.println("4. Update Quantity");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                scanner.next();
            }

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addProduct();
                    break;

                case 2:
                    displayProducts();
                    break;

                case 3:
                    searchProduct();
                    break;

                case 4:
                    updateQuantity();
                    break;

                case 5:
                    saveInventory();
                    System.out.println("Inventory saved successfully.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 5);
        scanner.close();
    }

    private static void addProduct() {
        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Product Price: ");
        double price = scanner.nextDouble();

        System.out.print("Enter Product Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        inventory.add(new Product(name, price, quantity));
        saveInventory();
        System.out.println("Product added successfully.");
    }

    private static void displayProducts() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        System.out.printf("%-20s %-12s %-10s%n",
                "Product", "Price", "Quantity");

        for (Product product : inventory) {
            System.out.printf("%-20s %-12.2f %-10d%n",
                    product.name,
                    product.price,
                    product.quantity);
        }
    }

    private static void searchProduct() {
        System.out.print("Enter Product Name: ");
        String search = scanner.nextLine().trim();

        for (Product product : inventory) {
            if (product.name.equalsIgnoreCase(search)) {
                System.out.println("\nProduct Found");
                System.out.println("Name : " + product.name);
                System.out.println("Price : " + product.price);
                System.out.println("Quantity : " + product.quantity);
                return;
            }
        }
        System.out.println("Product not found.");
    }

    private static void updateQuantity() {
        System.out.print("Enter Product Name: ");
        String search = scanner.nextLine().trim();

        for (Product product : inventory) {
            if (product.name.equalsIgnoreCase(search)) {
                System.out.print("Enter New Quantity: ");
                product.quantity = scanner.nextInt();
                scanner.nextLine();
                saveInventory();
                System.out.println("Quantity updated.");
                return;
            }
        }

        System.out.println("Product not found.");
    }

    private static void loadInventory() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length == 3) {
                    inventory.add(new Product(
                            data[0],
                            Double.parseDouble(data[1]),
                            Integer.parseInt(data[2])));
                }
            }

        } catch (IOException exception) {
            System.out.println("Unable to load inventory.");
        }
    }

    private static void saveInventory() {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Product product : inventory) {
                writer.write(product.name + ","
                        + product.price + ","
                        + product.quantity);

                writer.newLine();
            }

        } catch (IOException exception) {
            System.out.println("Unable to save inventory.");
        }
    }

    static class Product {
        String name;
        double price;
        int quantity;

        Product(String name,
                double price,
                int quantity) {

            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
    }
}