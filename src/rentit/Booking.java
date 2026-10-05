package rentit;

public class Booking {

    private String customerName;
    private Vehicle vehicle;
    private int days;
    private double totalPrice;

    private String pickupLocation;
    private String returnLocation;

    public Booking(String customerName, Vehicle vehicle, int days,
                   String pickupLocation, String returnLocation,
                   CreditWallet wallet)
            throws VehicleNotAvailableException,
                   InsufficientCreditsException,
                   InvalidBookingException {

        if (days <= 0) {
            throw new InvalidBookingException(
                    "Booking days must be greater than 0."
            );
        }

        if (pickupLocation == null || pickupLocation.trim().isEmpty()
                || returnLocation == null || returnLocation.trim().isEmpty()) {

            throw new InvalidBookingException(
                    "Pickup and return locations are required."
            );
        }

        this.customerName = customerName;
        this.vehicle = vehicle;
        this.days = days;
        this.pickupLocation = pickupLocation;
        this.returnLocation = returnLocation;

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

    public String getPickupLocation() {
        return pickupLocation;
    }

    public String getReturnLocation() {
        return returnLocation;
    }

    public void cancelBooking() {
        vehicle.cancelBooking();
    }
}