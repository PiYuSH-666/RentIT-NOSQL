package rentit;

public class Main {

    public static void main(String[] args) {

        // Create vehicle objects
        Car car = new Car("CAR001", "Maruti Swift", 1200, 3);
        Bike bike = new Bike("BIKE001", "Yamaha MT-15", 800, 2);
        Scooter scooter = new Scooter("SCOOTER001", "Honda Activa 6G", 500, 4);

        // Create rental manager
        RentalManager manager = new RentalManager();

        // Add vehicles to the manager
        manager.addVehicle(car);
        manager.addVehicle(bike);
        manager.addVehicle(scooter);

        // Display available vehicles
        System.out.println("Available Vehicles:");
        manager.displayVehicles();

        // Find a vehicle by ID
        Vehicle foundVehicle = manager.findVehicleById("CAR001");

        if (foundVehicle != null) {
            System.out.println("Found vehicle: " + foundVehicle.getModel());
        } else {
            System.out.println("Vehicle not found.");
        }
        
        CreditWallet wallet = new CreditWallet();

        System.out.println("Starting credits: ₹" + wallet.getCredits());

        // Create a booking
        try {
            Booking booking = new Booking("Rahul", car, 2, wallet);
            
            System.out.println("Credits after booking: ₹" + wallet.getCredits());

            System.out.println("Car available after booking: "
                    + car.getAvailableCount());

            System.out.println("Customer: " + booking.getCustomerName());
            System.out.println("Vehicle: " + booking.getVehicle().getModel());
            System.out.println("Days: " + booking.getDays());
            System.out.println("Total price: ₹" + booking.getTotalPrice());

            booking.cancelBooking();

            System.out.println("Car available after cancellation: "
                    + car.getAvailableCount());

        }  catch (VehicleNotAvailableException
                | InsufficientCreditsException
                | InvalidBookingException e) {
            System.out.println(e.getMessage());
        }

        // Interface polymorphism
        Rentable rentable = car;
        rentable.rent();
        
       
    }
}