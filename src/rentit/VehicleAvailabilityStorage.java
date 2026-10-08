package rentit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Stores vehicle availability in one shared local file.
 *
 * The file is shared by every RentIT account, so booking a vehicle
 * under one account changes what every other account sees.
 */
public final class VehicleAvailabilityStorage {

    private static final String STORAGE_FILE =
            "vehicle_availability.txt";

    private VehicleAvailabilityStorage() {
        // Utility class
    }

    public static void loadAll(ArrayList<Vehicle> vehicles) {

        boolean fileExists = false;

        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(STORAGE_FILE))) {

            fileExists = true;

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data =
                        line.split("\\|");

                if (data.length != 2) {
                    continue;
                }

                try {
                    String vehicleId = data[0];
                    int availableCount =
                            Integer.parseInt(data[1]);

                    for (Vehicle vehicle : vehicles) {

                        if (vehicle.getVehicleId()
                                .equals(vehicleId)) {

                            vehicle.setAvailableCount(
                                    availableCount
                            );
                            break;
                        }
                    }

                } catch (NumberFormatException e) {
                    // Ignore invalid availability lines.
                }
            }

        } catch (IOException e) {
            // First run: the shared file does not exist yet.
        }

        if (!fileExists) {
            saveAll(vehicles);
        }
    }

    /**
     * Saves only the changed vehicle's availability.
     * This is important because different accounts may have
     * different RentalManager objects in memory.
     */
    public static void saveVehicle(Vehicle changedVehicle) {

        LinkedHashMap<String, Integer> availability =
                new LinkedHashMap<>();

        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(STORAGE_FILE))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data =
                        line.split("\\|");

                if (data.length != 2) {
                    continue;
                }

                try {
                    availability.put(
                            data[0],
                            Integer.parseInt(data[1])
                    );
                } catch (NumberFormatException e) {
                    // Ignore invalid lines.
                }
            }

        } catch (IOException e) {
            // The file may not exist yet.
        }

        availability.put(
                changedVehicle.getVehicleId(),
                changedVehicle.getAvailableCount()
        );

        writeAvailability(availability);
    }

    private static void saveAll(
            ArrayList<Vehicle> vehicles) {

        LinkedHashMap<String, Integer> availability =
                new LinkedHashMap<>();

        for (Vehicle vehicle : vehicles) {

            availability.put(
                    vehicle.getVehicleId(),
                    vehicle.getAvailableCount()
            );
        }

        writeAvailability(availability);
    }

    private static void writeAvailability(
            LinkedHashMap<String, Integer> availability) {

        try (FileWriter writer =
                     new FileWriter(STORAGE_FILE)) {

            for (Map.Entry<String, Integer> entry :
                    availability.entrySet()) {

                writer.write(
                        entry.getKey()
                        + "|"
                        + entry.getValue()
                        + System.lineSeparator()
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Error saving vehicle availability: "
                    + e.getMessage()
            );
        }
    }
}
