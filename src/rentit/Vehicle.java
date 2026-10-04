package rentit;

public abstract class Vehicle implements Rentable  {

    private String vehicleId;
    private String model;
    private double pricePerDay;
    private int availableCount;

    public Vehicle(String vehicleId, String model, double pricePerDay, int availableCount) {
        this.vehicleId = vehicleId;
        this.model = model;
        this.pricePerDay = pricePerDay;
        this.availableCount = availableCount;
    }
    
    public String getVehicleId() {
        return vehicleId;
    }

    public String getModel() {
        return model;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public int getAvailableCount() {
        return availableCount;
    }
    
    public abstract String getVehicleType();
    
    public void bookVehicle() throws VehicleNotAvailableException {
        if (availableCount > 0) {
            availableCount--;
        } else {
            throw new VehicleNotAvailableException("Vehicle is not available for booking.");
        }
    }
    public void cancelBooking() {
        availableCount++;
    }
    @Override
    public void rent() {
        System.out.println("Vehicle rented: " + model);
    }

}