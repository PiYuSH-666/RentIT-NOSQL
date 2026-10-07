package rentit;

public class CreditWallet {

    private double credits;

    public CreditWallet() {
        credits = 5000;
    }

    public CreditWallet(double startingCredits) {
        credits = startingCredits;
    }

    public double getCredits() {
        return credits;
    }

    public void addCredits(double amount) {

        if (amount > 0) {
            credits += amount;
        }
    }

    public void deductCredits(double amount)
            throws InsufficientCreditsException {

        if (amount <= 0) {
            return;
        }

        if (amount <= credits) {

            credits -= amount;

        } else {

            throw new InsufficientCreditsException(
                    "Insufficient credits to complete the booking."
            );
        }
    }
}