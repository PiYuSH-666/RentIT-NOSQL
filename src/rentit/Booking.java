package rentit;

import java.util.UUID;

public class Booking {
    private String bookingId;
    private String customerName;
    private Vehicle vehicle;
    private int days;
    private double totalPrice;
    private String pickupLocation;
    private String returnLocation;
    private CreditWallet wallet;
    private boolean cancelled;

    public Booking(String customerName, Vehicle vehicle, int days,
                   String pickupLocation, String returnLocation,
                   CreditWallet wallet)
            throws VehicleNotAvailableException, InsufficientCreditsException,
                   InvalidBookingException {
        if (vehicle == null || wallet == null) {
            throw new InvalidBookingException("Vehicle and wallet are required.");
        }
        if (days <= 0) {
            throw new InvalidBookingException("Booking days must be greater than 0.");
        }
        if (pickupLocation == null || pickupLocation.trim().isEmpty()
                || returnLocation == null || returnLocation.trim().isEmpty()) {
            throw new InvalidBookingException("Pickup and return locations are required.");
        }
        this.bookingId = UUID.randomUUID().toString();
        this.customerName = customerName;
        this.vehicle = vehicle;
        this.days = days;
        this.pickupLocation = pickupLocation;
        this.returnLocation = returnLocation;
        this.wallet = wallet;
        this.totalPrice = vehicle.getPricePerDay() * days;

        vehicle.bookVehicle();
        try {
            wallet.deductCredits(totalPrice);
        } catch (InsufficientCreditsException e) {
            vehicle.cancelBooking();
            throw e;
        }
    }

    /** Restores a saved booking without charging or reserving again. */
    public Booking(String customerName, Vehicle vehicle, int days,
                   String pickupLocation, String returnLocation,
                   double totalPrice, CreditWallet wallet) {
        this(UUID.randomUUID().toString(), customerName, vehicle, days,
                pickupLocation, returnLocation, totalPrice, wallet, false);
    }

    /** Restores a persisted booking with its stable cross-process ID. */
    public Booking(String bookingId, String customerName, Vehicle vehicle, int days,
                   String pickupLocation, String returnLocation,
                   double totalPrice, CreditWallet wallet, boolean cancelled) {
        this.bookingId = (bookingId == null || bookingId.trim().isEmpty())
                ? UUID.randomUUID().toString() : bookingId;
        this.customerName = customerName;
        this.vehicle = vehicle;
        this.days = days;
        this.pickupLocation = pickupLocation;
        this.returnLocation = returnLocation;
        this.totalPrice = totalPrice;
        this.wallet = wallet;
        this.cancelled = cancelled;
    }

    public String getBookingId() { return bookingId; }
    public String getCustomerName() { return customerName; }
    public Vehicle getVehicle() { return vehicle; }
    public int getDays() { return days; }
    public double getTotalPrice() { return totalPrice; }
    public String getPickupLocation() { return pickupLocation; }
    public String getReturnLocation() { return returnLocation; }
    public boolean isCancelled() { return cancelled; }

    /** Local cancellation fallback. UI cancellation should go through BookingManager. */
    public boolean cancelBooking() {
        if (cancelled) return false;
        if (wallet != null && !wallet.addCredits(totalPrice)) return false;
        if (!vehicle.cancelBooking()) {
            if (wallet != null) {
                try { wallet.deductCredits(totalPrice); }
                catch (InsufficientCreditsException e) {
                    System.out.println("Could not roll back cancellation refund: " + e.getMessage());
                }
            }
            return false;
        }
        cancelled = true;
        return true;
    }

    void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
}
