package rentit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Stores shared vehicle availability in a text file.
 * A separate lock file coordinates access between separate RentIT processes.
 */
public final class VehicleAvailabilityStorage {

    private static final String STORAGE_FILE = "vehicle_availability.txt";
    private static final String LOCK_FILE = "vehicle_availability.txt.lock";

    private VehicleAvailabilityStorage() {
        // Utility class
    }

    public static synchronized void loadAll(ArrayList<Vehicle> vehicles) {
        try (FileChannel channel = FileChannel.open(Paths.get(LOCK_FILE),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE);
             FileLock lock = channel.lock()) {

            LinkedHashMap<String, Integer> availability = readAvailability();
            boolean changed = false;

            if (availability.isEmpty()) {
                for (Vehicle vehicle : vehicles) {
                    availability.put(vehicle.getVehicleId(), vehicle.getAvailableCount());
                }
                changed = true;
            } else {
                for (Vehicle vehicle : vehicles) {
                    if (!availability.containsKey(vehicle.getVehicleId())) {
                        availability.put(vehicle.getVehicleId(), vehicle.getAvailableCount());
                        changed = true;
                    }
                }
            }

            if (changed) {
                writeAvailability(availability);
            }

            for (Vehicle vehicle : vehicles) {
                Integer count = availability.get(vehicle.getVehicleId());
                if (count != null) {
                    vehicle.setAvailableCount(count.intValue());
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading vehicle availability: " + e.getMessage());
        }
    }

    /** Atomically reserves one vehicle using the latest saved count. */
    static synchronized int reserveVehicle(Vehicle vehicle) throws IOException {
        try (FileChannel channel = FileChannel.open(Paths.get(LOCK_FILE),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE);
             FileLock lock = channel.lock()) {

            LinkedHashMap<String, Integer> availability = readAvailability();
            Integer savedCount = availability.get(vehicle.getVehicleId());
            int currentCount = savedCount == null
                    ? vehicle.getAvailableCount() : savedCount.intValue();

            if (currentCount <= 0) {
                return -1;
            }

            int newCount = currentCount - 1;
            availability.put(vehicle.getVehicleId(), Integer.valueOf(newCount));
            writeAvailability(availability);
            return newCount;
        }
    }

    /** Atomically returns one vehicle using the latest saved count. */
    static synchronized int releaseVehicle(Vehicle vehicle) throws IOException {
        try (FileChannel channel = FileChannel.open(Paths.get(LOCK_FILE),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE);
             FileLock lock = channel.lock()) {

            LinkedHashMap<String, Integer> availability = readAvailability();
            Integer savedCount = availability.get(vehicle.getVehicleId());
            int currentCount = savedCount == null
                    ? vehicle.getAvailableCount() : savedCount.intValue();
            int newCount = currentCount + 1;
            availability.put(vehicle.getVehicleId(), Integer.valueOf(newCount));
            writeAvailability(availability);
            return newCount;
        }
    }

    private static LinkedHashMap<String, Integer> readAvailability() throws IOException {
        LinkedHashMap<String, Integer> availability = new LinkedHashMap<String, Integer>();
        Path file = Paths.get(STORAGE_FILE);
        if (!java.nio.file.Files.exists(file)) {
            return availability;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(STORAGE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length != 2) {
                    continue;
                }
                try {
                    int count = Integer.parseInt(data[1]);
                    if (count >= 0) {
                        availability.put(data[0], Integer.valueOf(count));
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid lines and keep valid availability entries.
                }
            }
        }
        return availability;
    }

    private static void writeAvailability(LinkedHashMap<String, Integer> availability)
            throws IOException {
        try (FileWriter writer = new FileWriter(STORAGE_FILE)) {
            for (Map.Entry<String, Integer> entry : availability.entrySet()) {
                writer.write(entry.getKey() + "|" + entry.getValue()
                        + System.lineSeparator());
            }
        }
    }
}
