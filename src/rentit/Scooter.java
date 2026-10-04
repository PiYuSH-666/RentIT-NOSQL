package rentit;

public class Scooter extends Vehicle {

    public Scooter(String vehicleId, String model, double pricePerDay, int availableCount) {
        super(vehicleId, model, pricePerDay, availableCount);
    }

    @Override
    public String getVehicleType() {
        return "Scooter";
    }
}