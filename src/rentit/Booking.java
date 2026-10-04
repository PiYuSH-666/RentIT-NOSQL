package rentit;

public class Booking {

    private String customerName;
    private Vehicle vehicle;
    private int days;
    private double totalPrice;

    public Booking(String customerName, Vehicle vehicle, int days)
            throws VehicleNotAvailableException {

        this.customerName = customerName;
        this.vehicle = vehicle;
        this.days = days;

        vehicle.bookVehicle();

        this.totalPrice = vehicle.getPricePerDay() * days;
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