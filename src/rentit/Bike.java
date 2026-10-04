package rentit;

public class Bike extends Vehicle {

    public Bike(String vehicleId, String model, double pricePerDay, int availableCount) {
        super(vehicleId, model, pricePerDay, availableCount);
    }

    @Override
    public String getVehicleType() {
        return "Bike";
    }
}