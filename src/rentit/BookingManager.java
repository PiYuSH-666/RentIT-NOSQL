package rentit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Persists bookings while merging updates made by other RentIT processes. */
public class BookingManager {
    private static final Object FILE_LOCK_MONITOR = new Object();
    private ArrayList<Booking> bookings = new ArrayList<Booking>();
    private String storageFile = "bookings.txt";
    private RentalManager lastRentalManager;
    private CreditWallet lastWallet;

    public void addBooking(Booking booking) {
        if (booking != null) bookings.add(booking);
    }
    public void removeBooking(Booking booking) { bookings.remove(booking); }
    public ArrayList<Booking> getBookings() { return bookings; }

    public void setCustomer(String username) {
        storageFile = (username == null || username.trim().isEmpty())
                ? "bookings.txt" : "bookings_" + username.trim() + ".txt";
        bookings.clear();
    }

    public void displayBookings() {
        for (Booking b : bookings) {
            System.out.println(b.getCustomerName() + " - " + b.getVehicle().getModel()
                    + " - " + b.getDays() + " days - ₹" + b.getTotalPrice());
        }
    }

    /**
     * Merges local bookings with the latest file while holding a process-shared lock.
     * Existing rows and cancellation tombstones are never discarded by a stale window.
     */
    public synchronized void saveBookings() {
        withLock(() -> {
            LinkedHashMap<String, Row> rows = readRows();
            for (Booking b : bookings) {
                if (b == null) continue;
                Row old = rows.get(b.getBookingId());
                if (old == null) rows.put(b.getBookingId(), Row.fromBooking(b, false));
                else if (!old.cancelled) rows.put(b.getBookingId(), Row.fromBooking(b, false));
                // A persisted cancellation tombstone wins over a stale in-memory booking.
            }
            writeRows(rows);
            return null;
        });
    }

    /**
     * Cancels only if the latest shared booking file still marks this ID active.
     * A tombstone is written before refund/release, so another app instance cannot
     * perform the same cancellation while this operation is running.
     */
    public synchronized boolean cancelBooking(Booking booking) {
        if (booking == null || booking.isCancelled()) return false;
        Boolean result = withLock(() -> {
            LinkedHashMap<String, Row> rows = readRows();
            Row row = rows.get(booking.getBookingId());
            if (row == null || row.cancelled) return false;

            rows.put(row.id, row.withCancelled(true));
            writeRows(rows);
            if (booking.cancelBooking()) {
                booking.setCancelled(true);
                bookings.remove(booking);
                return true;
            }
            // If cancellation reports failure, restore the active row for retry.
            rows.put(row.id, row.withCancelled(false));
            writeRows(rows);
            return false;
        });
        return Boolean.TRUE.equals(result);
    }

    public synchronized void refreshFromStorage() {
        if (lastRentalManager != null && lastWallet != null) {
            loadBookings(lastRentalManager, lastWallet);
        }
    }

    public synchronized void loadBookings(RentalManager rentalManager, CreditWallet wallet) {
        lastRentalManager = rentalManager;
        lastWallet = wallet;
        bookings.clear();
        withLock(() -> {
            LinkedHashMap<String, Row> rows = readRows();
            // Rewrite once to migrate old seven-column rows to stable IDs/status columns.
            writeRows(rows);
            for (Row row : rows.values()) {
                if (row.cancelled) continue;
                Vehicle vehicle = rentalManager.findVehicleById(row.vehicleId);
                if (vehicle != null) {
                    bookings.add(new Booking(row.id, row.customerName, vehicle, row.days,
                            row.pickup, row.dropoff, row.totalPrice, wallet, false));
                }
            }
            return null;
        });
    }

    private interface LockedOperation<T> { T run() throws IOException; }

    private <T> T withLock(LockedOperation<T> operation) {
        // The monitor prevents overlapping Java FileLocks inside one JVM; the
        // lock file itself coordinates separate RentIT processes.
        synchronized (FILE_LOCK_MONITOR) {
            Path lockPath = Paths.get(storageFile + ".lock");
            try (FileChannel channel = FileChannel.open(lockPath,
                        StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                 FileLock ignored = channel.lock()) {
                return operation.run();
            } catch (IOException e) {
                System.out.println("Booking storage error: " + e.getMessage());
                return null;
            }
        }
    }

    private LinkedHashMap<String, Row> readRows() throws IOException {
        LinkedHashMap<String, Row> rows = new LinkedHashMap<String, Row>();
        Path path = Paths.get(storageFile);
        if (!Files.exists(path)) return rows;
        Map<String, Integer> legacyOccurrences = new LinkedHashMap<String, Integer>();
        try (BufferedReader reader = new BufferedReader(new FileReader(storageFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] d = line.split("\\|", -1);
                if (d.length != 7 && d.length != 9) continue;
                try {
                    String base = String.join("|", d[0], d[1], d[2], d[3], d[4], d[5], d[6]);
                    String id;
                    boolean cancelled = false;
                    if (d.length == 9) {
                        id = d[7];
                        cancelled = "CANCELLED".equals(d[8]);
                        if (id.trim().isEmpty()) id = UUID.nameUUIDFromBytes(base.getBytes("UTF-8")).toString();
                    } else {
                        int occurrence = legacyOccurrences.containsKey(base) ? legacyOccurrences.get(base) + 1 : 1;
                        legacyOccurrences.put(base, occurrence);
                        id = UUID.nameUUIDFromBytes((base + "#" + occurrence).getBytes("UTF-8")).toString();
                    }
                    Row row = new Row(id, d[0], d[1], d[2], Integer.parseInt(d[3]),
                            d[4], d[5], Double.parseDouble(d[6]), cancelled);
                    Row previous = rows.get(id);
                    // Cancellation wins if a malformed/duplicate file contains both states.
                    if (previous == null || row.cancelled) rows.put(id, row);
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid booking data.");
                } catch (java.io.UnsupportedEncodingException impossible) {
                    throw new IOException(impossible);
                }
            }
        }
        return rows;
    }

    private void writeRows(LinkedHashMap<String, Row> rows) throws IOException {
        Path temp = Paths.get(storageFile + ".tmp");
        try (FileWriter writer = new FileWriter(temp.toFile())) {
            for (Row row : rows.values()) {
                writer.write(row.customerName + "|" + row.vehicleId + "|" + row.model + "|"
                        + row.days + "|" + cleanField(row.pickup) + "|" + cleanField(row.dropoff)
                        + "|" + row.totalPrice + "|" + row.id + "|"
                        + (row.cancelled ? "CANCELLED" : "ACTIVE") + System.lineSeparator());
            }
        }
        try {
            Files.move(temp, Paths.get(storageFile), java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                    java.nio.file.StandardCopyOption.ATOMIC_MOVE);
        } catch (java.nio.file.AtomicMoveNotSupportedException e) {
            Files.move(temp, Paths.get(storageFile), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private String cleanField(String value) {
        return value == null ? "" : value.replace('|', '/').replace('\n', ' ').replace('\r', ' ');
    }

    private static final class Row {
        final String id, customerName, vehicleId, model, pickup, dropoff;
        final int days;
        final double totalPrice;
        final boolean cancelled;
        Row(String id, String customerName, String vehicleId, String model, int days,
            String pickup, String dropoff, double totalPrice, boolean cancelled) {
            this.id = id; this.customerName = customerName; this.vehicleId = vehicleId;
            this.model = model; this.days = days; this.pickup = pickup; this.dropoff = dropoff;
            this.totalPrice = totalPrice; this.cancelled = cancelled;
        }
        Row withCancelled(boolean value) {
            return new Row(id, customerName, vehicleId, model, days, pickup, dropoff, totalPrice, value);
        }
        static Row fromBooking(Booking b, boolean cancelled) {
            return new Row(b.getBookingId(), b.getCustomerName(), b.getVehicle().getVehicleId(),
                    b.getVehicle().getModel(), b.getDays(), b.getPickupLocation(),
                    b.getReturnLocation(), b.getTotalPrice(), cancelled);
        }
    }
}
