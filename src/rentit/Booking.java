package rentit;

public class Booking {

    private String customerName;
    private Vehicle vehicle;
    private int days;
    private double totalPrice;

    public Booking(String customerName, Vehicle vehicle, int days,
            CreditWallet wallet)
     throws VehicleNotAvailableException,
            InsufficientCreditsException,
            InvalidBookingException {

 if (days <= 0) {
     throw new InvalidBookingException(
             "Booking days must be greater than 0."
     );
 }

 this.customerName = customerName;
 this.vehicle = vehicle;
 this.days = days;

        this.totalPrice = vehicle.getPricePerDay() * days;

     // Check credits before changing vehicle availability
        if (wallet.getCredits() < totalPrice) {
            throw new InsufficientCreditsException(
                    "Insufficient credits to complete the booking."
            );
        }

        // Book the vehicle
        vehicle.bookVehicle();

        // Deduct credits after successful booking
        wallet.deductCredits(totalPrice);
    }

    public String getCustomerName() {
        return customerName;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getDays() {
        return days;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void cancelBooking() {
        vehicle.cancelBooking();
    }
}