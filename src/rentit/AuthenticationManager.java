package rentit;

import java.util.ArrayList;

public class AuthenticationManager {

    private ArrayList<Customer> customers;
    private Customer currentCustomer;
    private CreditWallet currentWallet;

    public AuthenticationManager() {
        customers = CustomerStorage.loadCustomers();
        currentCustomer = null;
        currentWallet = null;
    }

    public boolean signUp(String name, String username, String password) {
        if (name == null || name.trim().isEmpty()
                || username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return false;
        }

        String cleanUsername = username.trim();
        String hashedPassword = PasswordUtil.hashPassword(password);
        Customer customer = new Customer(name.trim(), cleanUsername,
                hashedPassword, 5000);

        if (!CustomerStorage.addCustomer(customer)) {
            return false;
        }

        customers = CustomerStorage.loadCustomers();
        currentCustomer = findCustomer(cleanUsername);
        if (currentCustomer == null) {
            return false;
        }
        currentWallet = new CreditWallet(currentCustomer.getCredits(),
                currentCustomer.getUsername());
        return true;
    }

    public boolean login(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        // Reload from disk each login so this process does not use an old balance.
        customers = CustomerStorage.loadCustomers();
        Customer customer = findCustomer(username);
        if (customer != null) {
            String enteredPasswordHash = PasswordUtil.hashPassword(password);
            if (customer.getPassword().equals(enteredPasswordHash)) {
                currentCustomer = customer;
                currentWallet = new CreditWallet(customer.getCredits(),
                        customer.getUsername());
                return true;
            }
        }
        return false;
    }

    /** Wallet changes are saved immediately; this syncs only the in-memory object. */
    public void saveCurrentCustomer() {
        if (currentCustomer != null && currentWallet != null) {
            currentCustomer.setCredits(currentWallet.getCredits());
        }
    }

    public void logout() {
        saveCurrentCustomer();
        currentCustomer = null;
        currentWallet = null;
    }

    public Customer getCurrentCustomer() { return currentCustomer; }
    public CreditWallet getCurrentWallet() { return currentWallet; }
    public boolean isLoggedIn() { return currentCustomer != null; }

    private Customer findCustomer(String username) {
        if (username == null) {
            return null;
        }
        for (Customer customer : customers) {
            if (customer.getUsername().equalsIgnoreCase(username.trim())) {
                return customer;
            }
        }
        return null;
    }
}
