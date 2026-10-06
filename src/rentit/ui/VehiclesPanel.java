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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import rentit.RentalManager;
import rentit.Vehicle;

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

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public VehiclesPanel(RentalManager manager) {

        setLayout(new BorderLayout());

        setBackground(Color.WHITE);

        // =================================================
        // PAGE HEADER
        // =================================================

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
                        new GridLayout(2, 1)
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

        // =================================================
        // VEHICLE GRID
        // =================================================

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

        // Get vehicles from the existing backend
        ArrayList<Vehicle> vehicles =
                manager.getVehicles();

        for (Vehicle vehicle : vehicles) {

            vehicleGrid.add(
                    createVehicleCard(vehicle)
            );
        }

        // =================================================
        // SCROLL PANE
        // =================================================

        JScrollPane scrollPane =
                new JScrollPane(vehicleGrid);

        scrollPane.setBorder(null);

        scrollPane.setBackground(Color.WHITE);

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        add(
                scrollPane,
                BorderLayout.CENTER
        );
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

        // =================================================
        // IMAGE
        // =================================================

        JLabel imageLabel =
                new JLabel(
                        loadVehicleImage(
                                vehicle
                        )
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

        // =================================================
        // INFORMATION
        // =================================================

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

        informationPanel.add(
                modelLabel
        );

        informationPanel.add(
                typeLabel
        );

        informationPanel.add(
                priceLabel
        );

        informationPanel.add(
                availabilityLabel
        );

        card.add(
                informationPanel,
                BorderLayout.CENTER
        );

        // =================================================
        // BUTTON
        // =================================================

        JButton detailsButton =
                new JButton(
                        "View Details"
                );
        detailsButton.addActionListener(e -> {

            String details =
                    "Vehicle: " + vehicle.getModel()
                    + "\nType: " + vehicle.getVehicleType()
                    + "\nPrice: ₹" + vehicle.getPricePerDay() + "/day"
                    + "\nAvailable: " + vehicle.getAvailableCount()
                    + "\nFuel: " + vehicle.getFuelType()
                    + "\nTransmission: " + vehicle.getTransmission()
                    + "\nSeats: " + vehicle.getSeats()
                    + "\nBody Type: " + vehicle.getBodyType();

            JOptionPane.showMessageDialog(
                    this,
                    details,
                    vehicle.getModel() + " - Details",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        detailsButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        detailsButton.setForeground(
                Color.WHITE
        );

        detailsButton.setBackground(
                RENTIT_BLUE
        );

        detailsButton.setFocusPainted(false);

        detailsButton.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        18,
                        10,
                        18
                )
        );

        detailsButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
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
    // VEHICLE IMAGE
    // =====================================================

    private ImageIcon loadVehicleImage(
            Vehicle vehicle) {

        String imagePath;

        if (vehicle.getVehicleType()
                .equalsIgnoreCase("Car")) {

            imagePath =
                    "images/car.png";

        } else if (vehicle.getVehicleType()
                .equalsIgnoreCase("Bike")) {

            imagePath =
                    "images/bike.png";

        } else {

            imagePath =
                    "images/scooter.png";
        }

        try {

            Image image =
                    ImageIO.read(
                            new File(imagePath)
                    );

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
}