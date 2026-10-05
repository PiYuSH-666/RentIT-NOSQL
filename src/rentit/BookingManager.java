package rentit;

import java.io.FileWriter;
import java.io.IOException;
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

    public void saveBookings() {

        try (FileWriter writer = new FileWriter("bookings.txt")) {

            for (Booking booking : bookings) {

                writer.write(
                        booking.getCustomerName() + "|"
                        + booking.getVehicle().getVehicleId() + "|"
                        + booking.getVehicle().getModel() + "|"
                        + booking.getDays() + "|"
                        + booking.getPickupLocation() + "|"
                        + booking.getReturnLocation() + "|"
                        + booking.getTotalPrice()
                        + System.lineSeparator()
                );
            }

            System.out.println("Bookings saved successfully.");

        } catch (IOException e) {

            System.out.println(
                    "Error saving bookings: " + e.getMessage()
            );
        }
    }
}