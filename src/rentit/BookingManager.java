package rentit;

import java.util.ArrayList;

public class BookingManager {

    private ArrayList<Booking> bookings;

    public BookingManager() {
        bookings = new ArrayList<>();
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public void displayBookings() {
        for (Booking booking : bookings) {
            System.out.println(
                    booking.getCustomerName()
                    + " - "
                    + booking.getVehicle().getModel()
                    + " - "
                    + booking.getDays()
                    + " days"
                    + " - ₹"
                    + booking.getTotalPrice()
            );
        }
    }
}