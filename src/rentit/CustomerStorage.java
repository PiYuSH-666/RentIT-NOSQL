package rentit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/** Text-file persistence for customers and their credit balances. */
public class CustomerStorage {

    private static final String FILE_NAME = "customers.txt";
    private static final String LOCK_FILE = "customers.txt.lock";

    private CustomerStorage() { }

    public static synchronized void saveCustomers(ArrayList<Customer> customers) {
        try (FileChannel channel = FileChannel.open(Paths.get(LOCK_FILE),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE);
             FileLock lock = channel.lock()) {
            writeCustomers(customers);
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }

    /** Adds one account without replacing newer data for other accounts. */
    public static synchronized boolean addCustomer(Customer customer) {
        try (FileChannel channel = FileChannel.open(Paths.get(LOCK_FILE),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE);
             FileLock lock = channel.lock()) {
            ArrayList<Customer> customers = readCustomersAndMigrate();
            for (Customer existing : customers) {
                if (existing.getUsername().equalsIgnoreCase(customer.getUsername())) {
                    return false;
                }
            }
            customers.add(customer);
            writeCustomers(customers);
            return true;
        } catch (IOException e) {
            System.out.println("Error adding customer: " + e.getMessage());
            return false;
        }
    }

    /** Atomically adjusts one account's credits and returns the new balance. */
    public static synchronized double adjustCredits(String username, double change)
            throws IOException, InsufficientCreditsException {
        try (FileChannel channel = FileChannel.open(Paths.get(LOCK_FILE),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE);
             FileLock lock = channel.lock()) {

            ArrayList<Customer> customers = readCustomersAndMigrate();
            Customer target = null;
            for (Customer customer : customers) {
                if (customer.getUsername().equalsIgnoreCase(username)) {
                    target = customer;
                    break;
                }
            }
            if (target == null) {
                throw new IOException("Customer account was not found.");
            }

            double newBalance = target.getCredits() + change;
            if (newBalance < -0.000001) {
                throw new InsufficientCreditsException(
                        "Insufficient credits to complete the booking.");
            }
            if (newBalance < 0) {
                newBalance = 0;
            }
            target.setCredits(newBalance);
            writeCustomers(customers);
            return newBalance;
        }
    }

    public static synchronized ArrayList<Customer> loadCustomers() {
        try (FileChannel channel = FileChannel.open(Paths.get(LOCK_FILE),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE);
             FileLock lock = channel.lock()) {
            ArrayList<Customer> customers = readCustomersAndMigrate();
            writeCustomersIfNeeded(customers);
            return customers;
        } catch (IOException e) {
            System.out.println("Error loading customers: " + e.getMessage());
            return new ArrayList<Customer>();
        }
    }

    private static ArrayList<Customer> readCustomersAndMigrate() throws IOException {
        ArrayList<Customer> customers = new ArrayList<Customer>();
        if (!Files.exists(Paths.get(FILE_NAME))) {
            return customers;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|", -1);
                if (data.length != 4) {
                    continue;
                }
                try {
                    String password = data[2];
                    if (!PasswordUtil.isHashed(password)) {
                        password = PasswordUtil.hashPassword(password);
                    }
                    customers.add(new Customer(data[0], data[1], password,
                            Double.parseDouble(data[3])));
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid customer data.");
                }
            }
        }
        return customers;
    }

    private static void writeCustomersIfNeeded(ArrayList<Customer> customers)
            throws IOException {
        // Rewriting here also completes any legacy plaintext-password migration.
        writeCustomers(customers);
    }

    private static void writeCustomers(List<Customer> customers) throws IOException {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            for (Customer customer : customers) {
                writer.write(customer.getName() + "|"
                        + customer.getUsername() + "|"
                        + customer.getPassword() + "|"
                        + customer.getCredits() + System.lineSeparator());
            }
        }
    }
}
