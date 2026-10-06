package rentit.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicScrollBarUI;

import rentit.Booking;
import rentit.BookingManager;
import rentit.CreditWallet;
import rentit.InsufficientCreditsException;
import rentit.InvalidBookingException;
import rentit.RentalManager;
import rentit.Vehicle;
import rentit.VehicleNotAvailableException;

public class VehiclesPanel extends JPanel {

    // =====================================================
    // RENTIT BRAND COLORS
    // =====================================================

    private final Color RENTIT_BLUE =
            new Color(22, 119, 232);

    private final Color RENTIT_NAVY =
            new Color(17, 24, 39);

    private final Color LIGHT_BLUE =
            new Color(238, 246, 255);

    private final Color BORDER_BLUE =
            new Color(216, 231, 248);

    private final Color TEXT_GRAY =
            new Color(100, 110, 125);

    private BookingManager bookingManager;
    private CreditWallet wallet;
    private RentalManager rentalManager;

    // Cache the three scaled vehicle images so each vehicle card
    // does not read and scale the same image again.
    private ImageIcon carImageIcon;
    private ImageIcon bikeImageIcon;
    private ImageIcon scooterImageIcon;

    // Current vehicle category shown on the page.
    // "All" shows every vehicle.
    private String selectedCategory = "All";

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public VehiclesPanel(
            RentalManager manager,
            BookingManager bookingManager,
            CreditWallet wallet) {

        this.bookingManager = bookingManager;
        this.wallet = wallet;
        this.rentalManager = manager;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Load each vehicle image once and reuse it for all cards.
        carImageIcon = loadVehicleImage("images/car.png");
        bikeImageIcon = loadVehicleImage("images/bike.png");
        scooterImageIcon = loadVehicleImage("images/scooter.png");

        // Keep the original navigation behavior: the Vehicles page
        // is ready before the user clicks the Vehicles tab.
        buildVehiclePage();
    }

    // =====================================================
    // BUILD VEHICLE PAGE
    // =====================================================

    private void buildVehiclePage() {

        removeAll();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // -------------------------------------------------
        // HEADER
        // -------------------------------------------------

        JPanel headerPanel =
                new JPanel(new BorderLayout());

        headerPanel.setBackground(Color.WHITE);

        headerPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        70,
                        15,
                        70
                )
        );

        JLabel title =
                new JLabel(
                        "Our Vehicles",
                        SwingConstants.LEFT
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        title.setForeground(RENTIT_NAVY);

        JLabel subtitle =
                new JLabel(
                        "Choose a vehicle that fits your journey."
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        subtitle.setForeground(TEXT_GRAY);

        JPanel titleText =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        titleText.setOpaque(false);

        titleText.add(title);
        titleText.add(subtitle);

        headerPanel.add(
                titleText,
                BorderLayout.WEST
        );

        add(
                headerPanel,
                BorderLayout.NORTH
        );

        // -------------------------------------------------
        // VEHICLE GRID
        // -------------------------------------------------

        JPanel vehicleGrid =
                new JPanel(
                        new GridLayout(
                                0,
                                3,
                                22,
                                22
                        )
                );

        vehicleGrid.setBackground(Color.WHITE);

        vehicleGrid.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        70,
                        30,
                        70
                )
        );

        ArrayList<Vehicle> vehicles =
                rentalManager.getVehicles();

        for (Vehicle vehicle : vehicles) {

            if (!selectedCategory.equalsIgnoreCase("All")
                    && !vehicle.getVehicleType()
                            .equalsIgnoreCase(selectedCategory)) {
                continue;
            }

            vehicleGrid.add(
                    createVehicleCard(vehicle)
            );
        }

        // -------------------------------------------------
        // SCROLL PANE
        // -------------------------------------------------

        JScrollPane scrollPane =
                new JScrollPane(vehicleGrid);

        scrollPane.setBorder(null);

        scrollPane.setBackground(Color.WHITE);

        scrollPane.getVerticalScrollBar()
        .setUnitIncrement(16);

scrollPane.getVerticalScrollBar()
        .setPreferredSize(
                new Dimension(
                        10,
                        Integer.MAX_VALUE
                )
        );

scrollPane.getVerticalScrollBar()
        .setUI(
                new BasicScrollBarUI() {

                    @Override
                    protected void configureScrollBarColors() {

                        thumbColor =
                                RENTIT_BLUE;

                        trackColor =
                                Color.WHITE;
                    }

                    @Override
                    protected JButton createDecreaseButton(
                            int orientation) {

                        return createZeroButton();
                    }

                    @Override
                    protected JButton createIncreaseButton(
                            int orientation) {

                        return createZeroButton();
                    }

                    @Override
                    protected void paintThumb(
                            java.awt.Graphics g,
                            javax.swing.JComponent c,
                            java.awt.Rectangle thumbBounds) {

                        if (thumbBounds.isEmpty()
                                || !scrollbar.isEnabled()) {
                            return;
                        }

                        java.awt.Graphics2D g2 =
                                (java.awt.Graphics2D)
                                        g.create();

                        g2.setRenderingHint(
                                java.awt.RenderingHints.KEY_ANTIALIASING,
                                java.awt.RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        g2.setColor(
                                RENTIT_BLUE
                        );

                        g2.fillRoundRect(
                                thumbBounds.x + 2,
                                thumbBounds.y,
                                thumbBounds.width - 4,
                                thumbBounds.height,
                                8,
                                8
                        );

                        g2.dispose();
                    }

                    private JButton createZeroButton() {

                        JButton button =
                                new JButton();

                        button.setPreferredSize(
                                new Dimension(
                                        0,
                                        0
                                )
                        );

                        button.setMinimumSize(
                                new Dimension(
                                        0,
                                        0
                                )
                        );

                        button.setMaximumSize(
                                new Dimension(
                                        0,
                                        0
                                )
                        );

                        return button;
                    }
                }
        );

        add(
                scrollPane,
                BorderLayout.CENTER
        );

        revalidate();
        repaint();
    }

    // =====================================================
    // VEHICLE CARD
    // =====================================================

    private JPanel createVehicleCard(
            Vehicle vehicle) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_BLUE,
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                12,
                                12,
                                12,
                                12
                        )
                )
        );

        card.setPreferredSize(
                new Dimension(
                        300,
                        290
                )
        );

        // -------------------------------------------------
        // IMAGE
        // -------------------------------------------------

        JLabel imageLabel =
                new JLabel(
                        loadVehicleImage(vehicle)
                );

        imageLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        imageLabel.setPreferredSize(
                new Dimension(
                        270,
                        135
                )
        );

        card.add(
                imageLabel,
                BorderLayout.NORTH
        );

        // -------------------------------------------------
        // INFORMATION
        // -------------------------------------------------

        JPanel informationPanel =
                new JPanel(
                        new GridLayout(
                                0,
                                1,
                                0,
                                4
                        )
                );

        informationPanel.setOpaque(false);

        JLabel modelLabel =
                new JLabel(
                        vehicle.getModel()
                );

        modelLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        19
                )
        );

        modelLabel.setForeground(
                RENTIT_NAVY
        );

        JLabel typeLabel =
                new JLabel(
                        vehicle.getVehicleType()
                );

        typeLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        typeLabel.setForeground(
                TEXT_GRAY
        );

        JLabel priceLabel =
                new JLabel(
                        "₹"
                        + vehicle.getPricePerDay()
                        + "/day"
                );

        priceLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        priceLabel.setForeground(
                RENTIT_BLUE
        );

        JLabel availabilityLabel =
                new JLabel(
                        "Available: "
                        + vehicle.getAvailableCount()
                );

        availabilityLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        availabilityLabel.setForeground(
                TEXT_GRAY
        );

        informationPanel.add(modelLabel);
        informationPanel.add(typeLabel);
        informationPanel.add(priceLabel);
        informationPanel.add(availabilityLabel);

        card.add(
                informationPanel,
                BorderLayout.CENTER
        );

        // -------------------------------------------------
        // VIEW DETAILS BUTTON
        // -------------------------------------------------

        JButton detailsButton =
                new JButton(
                        "View Details"
                );

        stylePrimaryButton(detailsButton);

        detailsButton.addActionListener(
                e -> showVehicleDetails(vehicle)
        );

        JPanel buttonPanel =
                new JPanel(
                        new BorderLayout()
                );

        buttonPanel.setOpaque(false);

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        0,
                        0,
                        0
                )
        );

        buttonPanel.add(
                detailsButton,
                BorderLayout.EAST
        );

        card.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        return card;
    }

    // =====================================================
    // VEHICLE DETAILS
    // =====================================================

    private void showVehicleDetails(
            Vehicle vehicle) {

        JDialog dialog =
                new JDialog();

        dialog.setTitle(
                vehicle.getModel()
                        + " - Details"
        );

        dialog.setModal(true);

        dialog.setSize(
                520,
                600
        );

        dialog.setLocationRelativeTo(this);

        dialog.setResizable(false);

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(
                                0,
                                15
                        )
                );

        mainPanel.setBackground(
                Color.WHITE
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        25,
                        20,
                        25
                )
        );

        // -------------------------------------------------
        // VEHICLE IMAGE
        // -------------------------------------------------

        JLabel imageLabel =
                new JLabel(
                        loadVehicleImage(vehicle)
                );

        imageLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        imageLabel.setPreferredSize(
                new Dimension(
                        450,
                        170
                )
        );

        mainPanel.add(
                imageLabel,
                BorderLayout.NORTH
        );

        // -------------------------------------------------
        // TITLE
        // -------------------------------------------------

        JLabel modelLabel =
                new JLabel(
                        vehicle.getModel()
                );

        modelLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25
                )
        );

        modelLabel.setForeground(
                RENTIT_NAVY
        );

        JLabel typeLabel =
                new JLabel(
                        vehicle.getVehicleType()
                );

        typeLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        typeLabel.setForeground(
                TEXT_GRAY
        );

        JPanel titlePanel =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        titlePanel.setOpaque(false);

        titlePanel.add(modelLabel);
        titlePanel.add(typeLabel);

        // -------------------------------------------------
        // PRICE BOX
        // -------------------------------------------------

        JPanel pricePanel =
                new JPanel(
                        new BorderLayout()
                );

        pricePanel.setBackground(
                LIGHT_BLUE
        );

        pricePanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_BLUE
                        ),
                        BorderFactory.createEmptyBorder(
                                12,
                                15,
                                12,
                                15
                        )
                )
        );

        JLabel priceLabel =
                new JLabel(
                        "₹"
                        + vehicle.getPricePerDay()
                        + " / day"
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

        JLabel availableLabel =
                new JLabel(
                        "Available: "
                        + vehicle.getAvailableCount()
                );

        availableLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        availableLabel.setForeground(
                RENTIT_NAVY
        );

        pricePanel.add(
                priceLabel,
                BorderLayout.WEST
        );

        pricePanel.add(
                availableLabel,
                BorderLayout.EAST
        );

        // -------------------------------------------------
        // SPECIFICATIONS
        // -------------------------------------------------

        JPanel specsPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                10,
                                10
                        )
                );

        specsPanel.setBackground(
                Color.WHITE
        );

        specsPanel.add(
                createSpecLabel(
                        "Fuel",
                        vehicle.getFuelType()
                )
        );

        specsPanel.add(
                createSpecLabel(
                        "Transmission",
                        vehicle.getTransmission()
                )
        );

        specsPanel.add(
                createSpecLabel(
                        "Seats",
                        String.valueOf(
                                vehicle.getSeats()
                        )
                )
        );

        specsPanel.add(
                createSpecLabel(
                        "Body Type",
                        vehicle.getBodyType()
                )
        );

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout(
                                0,
                                12
                        )
                );

        centerPanel.setBackground(
                Color.WHITE
        );

        centerPanel.add(
                titlePanel,
                BorderLayout.NORTH
        );

        JPanel middlePanel =
                new JPanel(
                        new BorderLayout(
                                0,
                                12
                        )
                );

        middlePanel.setBackground(
                Color.WHITE
        );

        middlePanel.add(
                pricePanel,
                BorderLayout.NORTH
        );

        middlePanel.add(
                specsPanel,
                BorderLayout.CENTER
        );

        centerPanel.add(
                middlePanel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        // -------------------------------------------------
        // BUTTONS
        // -------------------------------------------------

        JPanel buttonPanel =
                new JPanel(
                        new BorderLayout(
                                10,
                                0
                        )
                );

        buttonPanel.setOpaque(false);

        JButton closeButton =
                new JButton(
                        "Close"
                );

        JButton bookButton =
                new JButton(
                        "Book Now"
                );

        styleSecondaryButton(
                closeButton
        );

        stylePrimaryButton(
                bookButton
        );

        closeButton.addActionListener(
                e -> dialog.dispose()
        );

        bookButton.addActionListener(
                e -> {

                    dialog.dispose();

                    showBookingForm(
                            vehicle
                    );
                }
        );

        buttonPanel.add(
                closeButton,
                BorderLayout.WEST
        );

        buttonPanel.add(
                bookButton,
                BorderLayout.EAST
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        dialog.add(mainPanel);

        dialog.setVisible(true);
    }

    // =====================================================
    // BOOKING FORM
    // =====================================================

    private void showBookingForm(
            Vehicle vehicle) {

        JDialog dialog =
                new JDialog();

        dialog.setTitle(
                "Book " + vehicle.getModel()
        );

        dialog.setModal(true);

        dialog.setSize(
                500,
                620
        );

        dialog.setLocationRelativeTo(this);

        dialog.setResizable(false);

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(
                                0,
                                15
                        )
                );

        mainPanel.setBackground(
                Color.WHITE
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        30,
                        25,
                        30
                )
        );

        // -------------------------------------------------
        // HEADER
        // -------------------------------------------------

        JLabel titleLabel =
                new JLabel(
                        "Book your "
                        + vehicle.getModel()
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        titleLabel.setForeground(
                RENTIT_NAVY
        );

        JLabel subtitleLabel =
                new JLabel(
                        "Enter your rental details below."
                );

        subtitleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitleLabel.setForeground(
                TEXT_GRAY
        );

        JPanel headerPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        headerPanel.setOpaque(false);

        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        // -------------------------------------------------
        // FORM
        // -------------------------------------------------

        JPanel formPanel =
                new JPanel(
                        new GridLayout(
                                8,
                                1,
                                0,
                                5
                        )
                );

        formPanel.setBackground(
                Color.WHITE
        );

        // NAME

        JLabel nameLabel =
                createFormLabel(
                        "Your name"
                );

        JTextField nameField =
                createTextField(
                        "Enter your full name"
                );

        formPanel.add(nameLabel);
        formPanel.add(nameField);

        // DAYS

        JLabel daysLabel =
                createFormLabel(
                        "Rental duration"
                );

        JSpinner daysSpinner =
                new JSpinner(
                        new SpinnerNumberModel(
                                1,
                                1,
                                30,
                                1
                        )
                );

        daysSpinner.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        JFormattedTextField spinnerText =
                ((JSpinner.DefaultEditor)
                        daysSpinner.getEditor())
                        .getTextField();

        spinnerText.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                ));

        // Keep the number on the LEFT side
        spinnerText.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        formPanel.add(daysLabel);
        formPanel.add(daysSpinner);

        // PICKUP

        JLabel pickupLabel =
                createFormLabel(
                        "Pickup location"
                );

        JTextField pickupField =
                createTextField(
                        "Enter pickup location"
                );

        formPanel.add(pickupLabel);
        formPanel.add(pickupField);

        // RETURN

        JLabel returnLabel =
                createFormLabel(
                        "Return location"
                );

        JTextField returnField =
                createTextField(
                        "Enter return location"
                );

        formPanel.add(returnLabel);
        formPanel.add(returnField);

        mainPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        // -------------------------------------------------
        // SUMMARY
        // -------------------------------------------------

        JLabel totalLabel =
                new JLabel();

        totalLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        totalLabel.setForeground(
                RENTIT_BLUE
        );

        JLabel creditsLabel =
                new JLabel(
                        String.format(
                                "Available credits: ₹%.2f",
                                wallet.getCredits()
                        )
                );

        creditsLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        creditsLabel.setForeground(
                RENTIT_NAVY
        );

        JPanel summaryPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        summaryPanel.setBackground(
                LIGHT_BLUE
        );

        summaryPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_BLUE
                        ),
                        BorderFactory.createEmptyBorder(
                                12,
                                15,
                                12,
                                15
                        )
                )
        );

        summaryPanel.add(totalLabel);
        summaryPanel.add(creditsLabel);

        // -------------------------------------------------
        // LIVE TOTAL
        // -------------------------------------------------

        Runnable updateTotal =
                () -> {

                    try {

                        int days =
                                Integer.parseInt(
                                        spinnerText.getText().trim()
                                );

                        if (days < 1) {
                            return;
                        }

                        double total =
                                vehicle.getPricePerDay()
                                * days;

                        totalLabel.setText(
                                String.format(
                                        "Total: ₹%.2f",
                                        total
                                )
                        );

                        totalLabel.revalidate();
                        totalLabel.repaint();

                    } catch (NumberFormatException ex) {

                        // User may temporarily have an empty
                        // or incomplete value while typing.
                    }
                };

        // Initial total
        updateTotal.run();

        // Update when ▲ / ▼ is clicked
        daysSpinner.addChangeListener(
                e -> updateTotal.run()
        );

        // Update while the user types directly
        spinnerText.getDocument()
                .addDocumentListener(
                        new javax.swing.event.DocumentListener() {

                            private void update() {
                                updateTotal.run();
                            }

                            @Override
                            public void insertUpdate(
                                    javax.swing.event.DocumentEvent e) {
                                update();
                            }

                            @Override
                            public void removeUpdate(
                                    javax.swing.event.DocumentEvent e) {
                                update();
                            }

                            @Override
                            public void changedUpdate(
                                    javax.swing.event.DocumentEvent e) {
                                update();
                            }
                        }
                );

        // Also update when the user types directly
        spinnerText.addPropertyChangeListener(
                "value",
                e -> updateTotal.run()
        );

        // -------------------------------------------------
        // BOTTOM AREA
        // -------------------------------------------------

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout(
                                0,
                                12
                        )
                );

        bottomPanel.setOpaque(false);

        bottomPanel.add(
                summaryPanel,
                BorderLayout.NORTH
        );

        JPanel buttonPanel =
                new JPanel(
                        new BorderLayout(
                                10,
                                0
                        )
                );

        buttonPanel.setOpaque(false);

        JButton cancelButton =
                new JButton(
                        "Cancel"
                );

        JButton confirmButton =
                new JButton(
                        "Confirm Booking"
                );

        styleSecondaryButton(
                cancelButton
        );

        stylePrimaryButton(
                confirmButton
        );

        cancelButton.addActionListener(
                e -> dialog.dispose()
        );

        confirmButton.addActionListener(
                e -> {

                	String customerName =
                	        getFieldValue(nameField);

                	String pickupLocation =
                	        getFieldValue(pickupField);

                	String returnLocation =
                	        getFieldValue(returnField);

                    int days =
                            (Integer)
                            daysSpinner.getValue();

                    if (customerName.isEmpty()) {

                        RentITDialog.showMessage(
                                dialog,
                                "Missing Information",
                                "Please enter your name.",
                                "OK"
                        );

                        nameField.requestFocus();

                        return;
                    }

                    if (pickupLocation.isEmpty()) {

                        RentITDialog.showMessage(
                                dialog,
                                "Missing Information",
                                "Please enter the pickup location.",
                                "OK"
                        );

                        pickupField.requestFocus();

                        return;
                    }

                    if (returnLocation.isEmpty()) {

                        RentITDialog.showMessage(
                                dialog,
                                "Missing Information",
                                "Please enter the return location.",
                                "OK"
                        );

                        returnField.requestFocus();

                        return;
                    }

                    try {

                        Booking booking =
                                new Booking(
                                        customerName,
                                        vehicle,
                                        days,
                                        pickupLocation,
                                        returnLocation,
                                        wallet
                                );

                        bookingManager.addBooking(
                                booking
                        );

                        bookingManager.saveBookings();

                        refreshVehicles();

                        dialog.dispose();

                        RentITDialog.showMessage(
                                this,
                                "Booking Confirmed",
                                "Your booking has been confirmed!\n\n"
                                        + "Vehicle: "
                                        + vehicle.getModel()
                                        + "\nDays: "
                                        + days
                                        + "\nTotal: ₹"
                                        + booking.getTotalPrice(),
                                "OK"
                        );

                    } catch (
                            VehicleNotAvailableException
                            | InsufficientCreditsException
                            | InvalidBookingException ex) {

                    	RentITDialog.showMessage(
                    	        this,
                    	        "Booking Failed",
                    	        ex.getMessage(),
                    	        "OK"
                    	);
                    }
                }
        );

        buttonPanel.add(
                cancelButton,
                BorderLayout.WEST
        );

        buttonPanel.add(
                confirmButton,
                BorderLayout.EAST
        );

        bottomPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        dialog.add(mainPanel);

        dialog.setVisible(true);
    }

    // =====================================================
    // SPECIFICATION LABEL
    // =====================================================

    private JLabel createSpecLabel(
            String title,
            String value) {

        JLabel label =
                new JLabel(
                        "<html><b>"
                        + title
                        + "</b><br>"
                        + value
                        + "</html>"
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        label.setForeground(
                RENTIT_NAVY
        );

        label.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_BLUE
                        ),
                        BorderFactory.createEmptyBorder(
                                8,
                                10,
                                8,
                                10
                        )
                )
        );

        return label;
    }

    // =====================================================
    // FORM LABEL
    // =====================================================

    private JLabel createFormLabel(
            String text) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        label.setForeground(
                RENTIT_NAVY
        );

        return label;
    }

    // =====================================================
    // TEXT FIELD
    // =====================================================
     
    private String getFieldValue(
            JTextField field) {

        boolean showingPlaceholder =
                Boolean.TRUE.equals(
                        field.getClientProperty(
                                "showingPlaceholder"
                        )
                );

        if (showingPlaceholder) {
            return "";
        }

        return field.getText().trim();
    }
    
    private JTextField createTextField(
            String placeholder) {

        JTextField field =
                new JTextField();

        field.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        field.setPreferredSize(
                new Dimension(
                        100,
                        38
                )
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_BLUE
                        ),
                        BorderFactory.createEmptyBorder(
                                5,
                                10,
                                5,
                                10
                        )
                )
        );

        Color placeholderColor =
                new Color(
                        160,
                        170,
                        185
                );

        Color normalColor =
                Color.BLACK;

        // Show placeholder initially
        field.setText(placeholder);
        field.setForeground(
                placeholderColor
        );

        field.putClientProperty(
                "showingPlaceholder",
                true
        );

        field.addFocusListener(
                new java.awt.event.FocusAdapter() {

                    @Override
                    public void focusGained(
                            java.awt.event.FocusEvent e) {

                        boolean showingPlaceholder =
                                Boolean.TRUE.equals(
                                        field.getClientProperty(
                                                "showingPlaceholder"
                                        )
                                );

                        if (showingPlaceholder) {

                            field.setText("");

                            field.setForeground(
                                    normalColor
                            );

                            field.putClientProperty(
                                    "showingPlaceholder",
                                    false
                            );
                        }
                    }

                    @Override
                    public void focusLost(
                            java.awt.event.FocusEvent e) {

                        if (field.getText()
                                .trim()
                                .isEmpty()) {

                            field.setText(
                                    placeholder
                            );

                            field.setForeground(
                                    placeholderColor
                            );

                            field.putClientProperty(
                                    "showingPlaceholder",
                                    true
                            );
                        }
                    }
                }
        );

        return field;
    }

    // =====================================================
    // PRIMARY BUTTON STYLE
    // =====================================================

    private void stylePrimaryButton(
            JButton button) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                RENTIT_BLUE
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        18,
                        10,
                        18
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    // =====================================================
    // SECONDARY BUTTON STYLE
    // =====================================================

    private void styleSecondaryButton(
            JButton button) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(
                RENTIT_NAVY
        );

        button.setBackground(
                Color.WHITE
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_BLUE
                        ),
                        BorderFactory.createEmptyBorder(
                                9,
                                18,
                                9,
                                18
                        )
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    // =====================================================
    // VEHICLE IMAGE
    // =====================================================

    private ImageIcon loadVehicleImage(
            Vehicle vehicle) {

        if (vehicle.getVehicleType()
                .equalsIgnoreCase("Car")) {
            return carImageIcon;
        }

        if (vehicle.getVehicleType()
                .equalsIgnoreCase("Bike")) {
            return bikeImageIcon;
        }

        return scooterImageIcon;
    }

    private ImageIcon loadVehicleImage(
            String imagePath) {

        try {

            Image image =
                    ImageIO.read(
                            new File(imagePath)
                    );

            if (image == null) {
                return new ImageIcon();
            }

            Image scaledImage =
                    image.getScaledInstance(
                            230,
                            120,
                            Image.SCALE_SMOOTH
                    );

            return new ImageIcon(
                    scaledImage
            );

        } catch (Exception e) {

            return new ImageIcon();
        }
    }

    // =====================================================
    // CATEGORY FILTER
    // =====================================================

    public void setSelectedCategory(String category) {

        if (category == null || category.trim().isEmpty()) {
            selectedCategory = "All";
        } else {
            selectedCategory = category;
        }

        buildVehiclePage();
    }

    // =====================================================
    // REFRESH VEHICLES
    // =====================================================

    public void refreshVehicles() {
        buildVehiclePage();
    }
}