package rentit;

public class Car extends Vehicle {

    public Car(String vehicleId, String model, double pricePerDay, int availableCount) {
        super(vehicleId, model, pricePerDay, availableCount);
    }

    @Override
    public String getVehicleType() {
        return "Car";
    }
}