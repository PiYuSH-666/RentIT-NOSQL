package rentit.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import rentit.Booking;
import rentit.BookingManager;
import rentit.CreditWallet;

public class BookingsPanel extends JPanel {

    // RentIT brand colors
    private final Color RENTIT_BLUE =
            new Color(22, 119, 232);

    private final Color RENTIT_NAVY =
            new Color(17, 24, 39);

    private final Color TEXT_GRAY =
            new Color(100, 110, 125);

    private final Color BORDER_BLUE =
            new Color(216, 231, 248);
    private BookingManager bookingManager;
    private JPanel bookingsList;
    private CreditWallet wallet;
    private JLabel creditsLabel;
    private VehiclesPanel vehiclesPanel;

    public BookingsPanel(
            BookingManager bookingManager,
            CreditWallet wallet,
            VehiclesPanel vehiclesPanel)  {
    	
    	this.bookingManager = bookingManager;
    	this.wallet = wallet;
    	this.vehiclesPanel = vehiclesPanel;

        setLayout(new BorderLayout());

        setBackground(Color.WHITE);

        // =================================================
        // TITLE
        // =================================================

     // =================================================
     // HEADER
     // =================================================

     JPanel headerPanel =
             new JPanel(
                     new BorderLayout()
             );

     headerPanel.setBackground(
             Color.WHITE
     );

     headerPanel.setBorder(
             BorderFactory.createEmptyBorder(
                     30,
                     70,
                     15,
                     70
             )
     );

     // Title

     JLabel title =
             new JLabel(
                     "My Bookings"
             );

     title.setFont(
             new Font(
                     "Arial",
                     Font.BOLD,
                     30
             )
     );

     title.setForeground(
             RENTIT_NAVY
     );

     // Credits

     creditsLabel =
             new JLabel(
                     "Available Credits: ₹"
                             + wallet.getCredits()
             );

     creditsLabel.setFont(
             new Font(
                     "Arial",
                     Font.BOLD,
                     16
             )
     );

     creditsLabel.setForeground(
             RENTIT_BLUE
     );

     headerPanel.add(
             title,
             BorderLayout.WEST
     );

     headerPanel.add(
             creditsLabel,
             BorderLayout.EAST
     );

     add(
             headerPanel,
             BorderLayout.NORTH
     );

        // =================================================
        // BOOKING LIST
        // =================================================

        bookingsList =
                new JPanel();

        bookingsList.setLayout(
                new javax.swing.BoxLayout(
                        bookingsList,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        bookingsList.setBackground(
                Color.WHITE
        );

        bookingsList.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        70,
                        30,
                        70
                )
        );

        ArrayList<Booking> bookings =
                bookingManager.getBookings();

        if (bookings.isEmpty()) {

            JLabel emptyLabel =
                    new JLabel(
                            "No bookings found."
                    );

            emptyLabel.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            16
                    )
            );

            emptyLabel.setForeground(
                    TEXT_GRAY
            );

            emptyLabel.setAlignmentX(
                    LEFT_ALIGNMENT
            );

            bookingsList.add(
                    emptyLabel
            );

        } else {

            for (Booking booking : bookings) {

                bookingsList.add(
                        createBookingCard(
                                booking
                        )
                );

                bookingsList.add(
                        javax.swing.Box.createRigidArea(
                                new Dimension(
                                        0,
                                        15
                                )
                        )
                );
            }
        }

        JScrollPane scrollPane =
                new JScrollPane(
                        bookingsList
                );

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollPane.setBackground(
                Color.WHITE
        );

        scrollPane.getViewport().setBackground(
                Color.WHITE
        );

        add(
                scrollPane,
                BorderLayout.CENTER
        );
         
    }

    // =====================================================
    // BOOKING CARD
    // =====================================================

    private JPanel createBookingCard(
            Booking booking) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(
                Color.WHITE
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_BLUE
                        ),
                        BorderFactory.createEmptyBorder(
                                18,
                                20,
                                18,
                                20
                        )
                )
        );

        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        220
                )
        );

        // =================================================
        // VEHICLE NAME
        // =================================================

        JLabel vehicleLabel =
                new JLabel(
                        booking.getVehicle().getModel()
                );

        vehicleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        vehicleLabel.setForeground(
                RENTIT_NAVY
        );

        // =================================================
        // BOOKING DETAILS
        // =================================================

        JPanel detailsPanel =
                new JPanel();

        detailsPanel.setLayout(
                new javax.swing.BoxLayout(
                        detailsPanel,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        detailsPanel.setBackground(
                Color.WHITE
        );

        detailsPanel.add(
                createDetailLabel(
                        "Customer: "
                                + booking.getCustomerName()
                )
        );

        detailsPanel.add(
                createDetailLabel(
                        "Vehicle Type: "
                                + booking.getVehicle()
                                        .getVehicleType()
                )
        );

        detailsPanel.add(
                createDetailLabel(
                        "Days: "
                                + booking.getDays()
                )
        );

        detailsPanel.add(
                createDetailLabel(
                        "Pickup: "
                                + booking.getPickupLocation()
                )
        );

        detailsPanel.add(
                createDetailLabel(
                        "Return: "
                                + booking.getReturnLocation()
                )
        );

        // =================================================
        // PRICE
        // =================================================

        JLabel priceLabel =
                new JLabel(
                        "₹"
                                + booking.getTotalPrice()
                );

        priceLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        priceLabel.setForeground(
                RENTIT_BLUE
        );

        priceLabel.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        card.add(
                vehicleLabel,
                BorderLayout.NORTH
        );

        card.add(
                detailsPanel,
                BorderLayout.CENTER
        );

        card.add(
                priceLabel,
                BorderLayout.EAST
        );

        // =================================================
        // CANCEL BUTTON
        // =================================================

        JButton cancelButton =
                new JButton("Cancel Booking");

        cancelButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        cancelButton.setForeground(
                Color.WHITE
        );

        cancelButton.setBackground(
                RENTIT_BLUE
        );

        cancelButton.setFocusPainted(
                false
        );

        cancelButton.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        16,
                        8,
                        16
                )
        );

        cancelButton.setCursor(
                new java.awt.Cursor(
                        java.awt.Cursor.HAND_CURSOR
                )
        );

        cancelButton.addActionListener(e -> {

        	boolean confirmed =
        	        RentITDialog.showConfirm(
        	                this,
        	                "Cancel Booking",
        	                "Are you sure you want to cancel this booking?\n"
        	                        + "₹" + booking.getTotalPrice()
        	                        + " will be refunded to your credits.",
        	                "Yes, Cancel",
        	                "No"
        	        );

        	if (confirmed) {

                booking.cancelBooking();

                bookingManager.removeBooking(
                        booking
                );
                bookingManager.saveBookings();

                refreshBookings();
                
                vehiclesPanel.refreshVehicles();

                RentITDialog.showMessage(
                        this,
                        "Booking Cancelled",
                        "Booking cancelled successfully.\n"
                                + "₹" + booking.getTotalPrice()
                                + " has been refunded.",
                        "OK"
                );
            }
        });

        card.add(
                cancelButton,
                BorderLayout.SOUTH
        );

        return card;
    }

    // =====================================================
    // DETAIL LABEL
    // =====================================================

    private JLabel createDetailLabel(
            String text) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        label.setForeground(
                TEXT_GRAY
        );

        label.setBorder(
                BorderFactory.createEmptyBorder(
                        3,
                        0,
                        3,
                        0
                )
        );

        return label;
    }
    
    public void refreshBookings() {
    	
    	creditsLabel.setText(
    	        "Available Credits: ₹"
    	                + wallet.getCredits()
    	);

        bookingsList.removeAll();

        ArrayList<Booking> bookings =
                bookingManager.getBookings();

        if (bookings.isEmpty()) {

            JLabel emptyLabel =
                    new JLabel(
                            "No bookings found."
                    );

            emptyLabel.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            16
                    )
            );

            emptyLabel.setForeground(
                    TEXT_GRAY
            );

            emptyLabel.setAlignmentX(
                    LEFT_ALIGNMENT
            );

            bookingsList.add(
                    emptyLabel
            );

        } else {

            for (Booking booking : bookings) {

                bookingsList.add(
                        createBookingCard(
                                booking
                        )
                );

                bookingsList.add(
                        javax.swing.Box.createRigidArea(
                                new Dimension(
                                        0,
                                        15
                                )
                        )
                );
            }
        }

        bookingsList.revalidate();
        bookingsList.repaint();
    }
    
}