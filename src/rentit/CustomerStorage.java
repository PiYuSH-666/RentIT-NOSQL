package rentit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CustomerStorage {

    private static final String FILE_NAME = "customers.txt";

    public static void saveCustomers(ArrayList<Customer> customers) {

        try (FileWriter writer = new FileWriter(FILE_NAME)) {

            for (Customer customer : customers) {

                writer.write(
                        customer.getName() + "|"
                        + customer.getUsername() + "|"
                        + customer.getPassword() + "|"
                        + customer.getCredits()
                        + System.lineSeparator()
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Error saving customers: "
                    + e.getMessage()
            );
        }
    }

    public static ArrayList<Customer> loadCustomers() {

        ArrayList<Customer> customers =
                new ArrayList<>();

        try (
            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(FILE_NAME)
                    )
        ) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data =
                        line.split("\\|");

                if (data.length != 4) {
                    continue;
                }

                String name = data[0];
                String username = data[1];
                String password = data[2];
                double credits =
                        Double.parseDouble(data[3]);

                customers.add(
                        new Customer(
                                name,
                                username,
                                password,
                                credits
                        )
                );
            }

        } catch (IOException e) {

            // No customers.txt yet.
            // It will be created when the first
            // customer signs up.

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid customer data."
            );
        }

        return customers;
    }
}