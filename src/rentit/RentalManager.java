package rentit;

import java.util.ArrayList;

public class RentalManager {

    private ArrayList<Vehicle> vehicles;

    public RentalManager() {
        vehicles = new ArrayList<>();
    }
    
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void displayVehicles() {

        for (Vehicle vehicle : vehicles) {
            System.out.println(
                vehicle.getVehicleType() + ": "
                + vehicle.getModel()
                + " - ₹" + vehicle.getPricePerDay() + "/day"
            );
        }
    }
    public Vehicle findVehicleById(String vehicleId) {

        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleId().equals(vehicleId)) {
                return vehicle;
            }
        }

        return null;
    }
    
    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

}