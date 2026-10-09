package rentit;

import java.io.IOException;

public class CreditWallet {

    private double credits;
    private String username;

    public CreditWallet() {
        credits = 5000;
        username = null;
    }

    public CreditWallet(double startingCredits) {
        credits = startingCredits;
        username = null;
    }

    /** Wallet linked to a saved customer account. */
    public CreditWallet(double startingCredits, String username) {
        credits = startingCredits;
        this.username = username;
    }

    public double getCredits() {
        return credits;
    }

    /** Adds credits and persists them immediately for a logged-in account. */
    public boolean addCredits(double amount) {
        if (amount <= 0) {
            return true;
        }
        if (username == null) {
            credits += amount;
            return true;
        }
        try {
            credits = CustomerStorage.adjustCredits(username, amount);
            return true;
        } catch (IOException e) {
            System.out.println("Could not save credits: " + e.getMessage());
            return false;
        } catch (InsufficientCreditsException e) {
            return false;
        }
    }

    /** Deducts from the latest saved balance, not a stale in-memory balance. */
    public void deductCredits(double amount) throws InsufficientCreditsException {
        if (amount <= 0) {
            return;
        }
        if (username == null) {
            if (amount > credits) {
                throw new InsufficientCreditsException(
                        "Insufficient credits to complete the booking.");
            }
            credits -= amount;
            return;
        }
        try {
            credits = CustomerStorage.adjustCredits(username, -amount);
        } catch (IOException e) {
            throw new InsufficientCreditsException(
                    "Could not save your balance. Please try again.");
        }
    }

    /** Refreshes the balance after a successful login or account operation. */
    public void refreshCredits(double currentCredits) {
        credits = currentCredits;
    }

    /** Reloads this account's latest balance after another RentIT window changes it. */
    public void refreshSavedCredits() {
        if (username == null || username.trim().isEmpty()) {
            return;
        }
        for (Customer customer : CustomerStorage.loadCustomers()) {
            if (customer.getUsername().equalsIgnoreCase(username)) {
                credits = customer.getCredits();
                return;
            }
        }
    }
}
