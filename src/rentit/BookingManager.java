package rentit;

import java.io.BufferedReader;
import java.io.FileReader;
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
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
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
    
    public void loadBookings(RentalManager rentalManager) {

        try (BufferedReader reader =
                     new BufferedReader(new FileReader("bookings.txt"))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split("\\|");

                if (data.length != 7) {
                    continue;
                }

                String customerName = data[0];
                String vehicleId = data[1];
                int days = Integer.parseInt(data[3]);
                String pickupLocation = data[4];
                String returnLocation = data[5];
                double totalPrice = Double.parseDouble(data[6]);

                Vehicle vehicle =
                        rentalManager.findVehicleById(vehicleId);

                if (vehicle != null) {

                    Booking booking = new Booking(
                            customerName,
                            vehicle,
                            days,
                            pickupLocation,
                            returnLocation,
                            totalPrice
                    );

                    bookings.add(booking);
                }
            }

            System.out.println("Bookings loaded successfully.");

        } catch (IOException e) {

            System.out.println(
                    "No saved bookings found."
            );

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid booking data in file."
            );
        }
    }
}