package rentit;

import java.util.ArrayList;

public class AuthenticationManager {

    private ArrayList<Customer> customers;

    private Customer currentCustomer;

    private CreditWallet currentWallet;

    public AuthenticationManager() {

        customers =
                CustomerStorage.loadCustomers();

        currentCustomer = null;
        currentWallet = null;
    }

    public boolean signUp(
            String name,
            String username,
            String password) {

        if (name == null ||
                name.trim().isEmpty()) {

            return false;
        }

        if (username == null ||
                username.trim().isEmpty()) {

            return false;
        }

        if (password == null ||
                password.trim().isEmpty()) {

            return false;
        }

        if (findCustomer(username) != null) {

            return false;
        }

        Customer customer =
                new Customer(
                        name.trim(),
                        username.trim(),
                        password,
                        5000
                );

        customers.add(customer);

        CustomerStorage.saveCustomers(
                customers
        );

        currentCustomer = customer;

        currentWallet =
                new CreditWallet(
                        customer.getCredits()
                );

        return true;
    }

    public boolean login(
            String username,
            String password) {

        Customer customer =
                findCustomer(username);

        if (customer != null &&
                customer.getPassword()
                        .equals(password)) {

            currentCustomer = customer;

            currentWallet =
                    new CreditWallet(
                            customer.getCredits()
                    );

            return true;
        }

        return false;
    }

    public void saveCurrentCustomer() {

        if (currentCustomer == null ||
                currentWallet == null) {

            return;
        }

        currentCustomer.setCredits(
                currentWallet.getCredits()
        );

        CustomerStorage.saveCustomers(
                customers
        );
    }

    public void logout() {

        saveCurrentCustomer();

        currentCustomer = null;
        currentWallet = null;
    }

    public Customer getCurrentCustomer() {

        return currentCustomer;
    }

    public CreditWallet getCurrentWallet() {

        return currentWallet;
    }

    public boolean isLoggedIn() {

        return currentCustomer != null;
    }

    private Customer findCustomer(
            String username) {

        for (Customer customer : customers) {

            if (customer.getUsername()
                    .equalsIgnoreCase(username)) {

                return customer;
            }
        }

        return null;
    }
}