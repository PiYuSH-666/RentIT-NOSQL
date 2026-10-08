package rentit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BookingManager {

    private ArrayList<Booking> bookings;

    private String storageFile;

    public BookingManager() {

        bookings = new ArrayList<>();

        storageFile = "bookings.txt";
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

    // =====================================================
    // CUSTOMER BOOKING FILE
    // =====================================================

    public void setCustomer(String username) {

        if (username == null ||
                username.trim().isEmpty()) {

            storageFile = "bookings.txt";

        } else {

            storageFile =
                    "bookings_"
                    + username.trim()
                    + ".txt";
        }

        bookings.clear();
    }

    // =====================================================
    // DISPLAY BOOKINGS
    // =====================================================

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

    // =====================================================
    // SAVE BOOKINGS
    // =====================================================

    public void saveBookings() {

        try (
            FileWriter writer =
                    new FileWriter(storageFile)
        ) {

            for (Booking booking : bookings) {

                writer.write(
                        booking.getCustomerName()
                        + "|"
                        + booking.getVehicle().getVehicleId()
                        + "|"
                        + booking.getVehicle().getModel()
                        + "|"
                        + booking.getDays()
                        + "|"
                        + booking.getPickupLocation()
                        + "|"
                        + booking.getReturnLocation()
                        + "|"
                        + booking.getTotalPrice()
                        + System.lineSeparator()
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Error saving bookings: "
                    + e.getMessage()
            );
        }
    }

    // =====================================================
    // LOAD BOOKINGS
    // =====================================================

    public void loadBookings(
            RentalManager rentalManager,
            CreditWallet wallet) {

        try (
            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(storageFile)
                    )
        ) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data =
                        line.split("\\|");

                if (data.length != 7) {
                    continue;
                }

                String customerName =
                        data[0];

                String vehicleId =
                        data[1];

                int days =
                        Integer.parseInt(data[3]);

                String pickupLocation =
                        data[4];

                String returnLocation =
                        data[5];

                double totalPrice =
                        Double.parseDouble(data[6]);

                Vehicle vehicle =
                        rentalManager.findVehicleById(
                                vehicleId
                        );

                if (vehicle != null) {

                    // Vehicle availability is restored globally by
                    // RentalManager/VehicleAvailabilityStorage.
                    // Do NOT change availability or credits here.

                    Booking booking =
                            new Booking(
                                    customerName,
                                    vehicle,
                                    days,
                                    pickupLocation,
                                    returnLocation,
                                    totalPrice,
                                    wallet
                            );

                    bookings.add(booking);
                }
            }

        } catch (IOException e) {

            // No booking file yet.
            // This is normal for a new customer.

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid booking data."
            );

    }
}
        }
