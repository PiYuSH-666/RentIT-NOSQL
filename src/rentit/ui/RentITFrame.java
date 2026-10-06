package rentit.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import rentit.Bike;
import rentit.Car;
import rentit.RentalManager;
import rentit.Scooter;
import rentit.BookingManager;
import rentit.CreditWallet;

public class RentITFrame extends JFrame {

    // =====================================================
    // RENTIT BRAND COLORS
    // =====================================================

    private final Color RENTIT_BLUE = new Color(22, 119, 232);
    private final Color RENTIT_NAVY = new Color(17, 24, 39);
    private final Color LIGHT_BLUE = new Color(238, 246, 255);
    private final Color BORDER_BLUE = new Color(216, 231, 248);
    private final Color TEXT_GRAY = new Color(100, 110, 125);

    // Footer is only shown when the window is maximized.
    private JPanel footerPanel;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private RentalManager rentalManager;
    private BookingManager bookingManager;
    private CreditWallet wallet;
    private BookingsPanel bookingsPanel;
    private VehiclesPanel vehiclesPanel;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public RentITFrame() {

        setTitle("RentIT - Vehicle Rental");

        setSize(1200, 720);

        setMinimumSize(
                new Dimension(1000, 650)
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);
        
        rentalManager = createRentalManager();
        
        bookingManager = new BookingManager();
        
        wallet = new CreditWallet();

        

        JPanel mainPanel =
                new JPanel(new BorderLayout());

        mainPanel.setBackground(
                Color.WHITE
        );

        // =================================================
        // NAVIGATION BAR
        // =================================================

        mainPanel.add(
                createNavigationBar(),
                BorderLayout.NORTH
        );

        // =================================================
        // HOME CONTENT
        // =================================================

        JPanel homeContent =
                new JPanel(new BorderLayout());

        homeContent.setBackground(
                Color.WHITE
        );

        homeContent.add(
                new HeroPanel(),
                BorderLayout.NORTH
        );

        // =================================================
        // CATEGORY SECTION
        // =================================================

        JPanel categorySection =
                new JPanel(new BorderLayout());

        categorySection.setBackground(
                Color.WHITE
        );

        JLabel chooseLabel =
                new JLabel(
                        "Choose your ride",
                        SwingConstants.CENTER
                );

        chooseLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        chooseLabel.setForeground(
                RENTIT_NAVY
        );

        chooseLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        0,
                        5,
                        0
                )
        );

        categorySection.add(
                chooseLabel,
                BorderLayout.NORTH
        );

        // =================================================
        // VEHICLE CARDS
        // =================================================

        JPanel cardsPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                22,
                                0
                        )
                );

        cardsPanel.setBackground(
                Color.WHITE
        );

        // Keep cards compact
        cardsPanel.setPreferredSize(
                new Dimension(
                        1060,
                        195
                )
        );

        cardsPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        3,
                        0,
                        0,
                        0
                )
        );

        cardsPanel.add(
                createVehicleCard(
                        "Cars",
                        "Comfortable cars for every journey",
                        "images/car.png"
                )
        );

        cardsPanel.add(
                createVehicleCard(
                        "Bikes",
                        "Ride freely and explore more",
                        "images/bike.png"
                )
        );

        cardsPanel.add(
                createVehicleCard(
                        "Scooters",
                        "Easy and affordable city rides",
                        "images/scooter.png"
                )
        );

        // =================================================
        // CARD HOLDER
        // =================================================

        JPanel cardsHolder =
                new JPanel(
                        new BorderLayout()
                );

        cardsHolder.setBackground(
                Color.WHITE
        );

        cardsHolder.setBorder(
                BorderFactory.createEmptyBorder(
                        3,
                        70,
                        20,
                        70
                )
        );

        cardsHolder.add(
                cardsPanel,
                BorderLayout.NORTH
        );

        categorySection.add(
                cardsHolder,
                BorderLayout.CENTER
        );

        homeContent.add(
                categorySection,
                BorderLayout.CENTER
        );

        cardLayout = new CardLayout();

        contentPanel =
                new JPanel(cardLayout);

        contentPanel.setBackground(
                Color.WHITE
        );

        contentPanel.add(
                homeContent,
                "HOME"
        );

        vehiclesPanel = new VehiclesPanel(
                rentalManager,
                bookingManager,
                wallet
        );

        contentPanel.add(
                vehiclesPanel,
                "VEHICLES"
        );
        
        bookingsPanel = new BookingsPanel(
                bookingManager,
                wallet,
                vehiclesPanel
        );

        contentPanel.add(
                bookingsPanel,
                "BOOKINGS"
        );
        mainPanel.add(
                contentPanel,
                BorderLayout.CENTER
        );

        // =================================================
        // FOOTER
        // =================================================

        footerPanel = createFooter();

        mainPanel.add(
                footerPanel,
                BorderLayout.SOUTH
        );

        // Start hidden so the normal-sized window remains
        // exactly like the current Home page.
        footerPanel.setVisible(false);

        add(mainPanel);

        // Show footer only after the user maximizes the window.
        addWindowStateListener(
                e -> updateFooterVisibility(e)
        );
    }

    // =====================================================
    // FOOTER
    // =====================================================

    private JPanel createFooter() {

        JPanel footer =
                new JPanel(
                        new BorderLayout()
                );

        footer.setPreferredSize(
                new Dimension(
                        1200,
                        115
                )
        );

        footer.setBackground(
                Color.BLACK
        );

        footer.setBorder(
                BorderFactory.createEmptyBorder(
                        16,
                        70,
                        16,
                        70
                )
        );

        JPanel footerContent =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        footerContent.setOpaque(false);

        JLabel title =
                new JLabel(
                        "RentIT - Vehicle Rental"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        title.setForeground(
                Color.WHITE
        );

        JLabel information =
                new JLabel(
                        "Piyush  |  Manipal University Jaipur  |  Rajasthan  |  PIN: XXXXX  |  Contact: +91 8XXXXXXX"
                );

        information.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        information.setForeground(
                Color.WHITE
        );

        footerContent.add(title);
        footerContent.add(information);

        footer.add(
                footerContent,
                BorderLayout.CENTER
        );

        return footer;
    }

    private void updateFooterVisibility(WindowEvent e) {

        boolean maximized =
                (e.getNewState() & JFrame.MAXIMIZED_BOTH)
                        == JFrame.MAXIMIZED_BOTH;

        footerPanel.setVisible(maximized);

        revalidate();
        repaint();
    }

    // =====================================================
    // NAVIGATION BAR
    // =====================================================

    private JPanel createNavigationBar() {

        JPanel navigationBar =
                new JPanel(new BorderLayout());

        navigationBar.setPreferredSize(
                new Dimension(
                        1200,
                        105
                )
        );

        navigationBar.setBackground(
                Color.WHITE
        );

        navigationBar.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        BORDER_BLUE
                )
        );

        // =================================================
        // LOGO
        // =================================================

        JPanel logoPanel =
                new JPanel(new BorderLayout());

        logoPanel.setOpaque(false);

        logoPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        35,
                        8,
                        0
                )
        );

        BufferedImage logoImage =
                loadImage(
                        "images/logo.png"
                );

        if (logoImage != null) {

            BufferedImage croppedLogo =
                    cropLogo(logoImage);

            JLabel logoLabel =
                    new JLabel(
                            createScaledIcon(
                                    croppedLogo,
                                    250,
                                    85
                            )
                    );

            logoPanel.add(
                    logoLabel,
                    BorderLayout.CENTER
            );

        } else {

            JPanel textLogo =
                    new JPanel();

            textLogo.setOpaque(false);

            JLabel rentText =
                    new JLabel("Rent");

            rentText.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            32
                    )
            );

            rentText.setForeground(
                    RENTIT_NAVY
            );

            JLabel itText =
                    new JLabel("IT");

            itText.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            32
                    )
            );

            itText.setForeground(
                    RENTIT_BLUE
            );

            textLogo.add(rentText);
            textLogo.add(itText);

            logoPanel.add(
                    textLogo,
                    BorderLayout.CENTER
            );
        }

        navigationBar.add(
                logoPanel,
                BorderLayout.WEST
        );

        // =================================================
        // MENU
        // =================================================

        JPanel menuPanel =
                new JPanel();

        menuPanel.setOpaque(false);

        menuPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        0,
                        0,
                        35
                )
        );

        menuPanel.add(
                createNavButton(
                        "Home",
                        true
                )
        );

        menuPanel.add(
                createNavButton(
                        "Vehicles",
                        false
                )
        );

        menuPanel.add(
                createNavButton(
                        "My Bookings",
                        false
                )
        );

        navigationBar.add(
                menuPanel,
                BorderLayout.EAST
        );

        return navigationBar;
    }

    // =====================================================
    // NAVIGATION BUTTON
    // =====================================================

    private JButton createNavButton(
            String text,
            boolean active) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Arial",
                        active
                                ? Font.BOLD
                                : Font.PLAIN,
                        16
                )
        );

        button.setForeground(
                active
                        ? RENTIT_BLUE
                        : RENTIT_NAVY
        );

        button.setBackground(
                Color.WHITE
        );

        button.setBorderPainted(false);

        button.setFocusPainted(false);

        button.setContentAreaFilled(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        16,
                        12,
                        16
                )
        );
        if (text.equals("Home")) {

            button.addActionListener(
                    e -> cardLayout.show(
                            contentPanel,
                            "HOME"
                    )
            );

        } else if (text.equals("Vehicles")) {

            button.addActionListener(
                    e -> cardLayout.show(
                            contentPanel,
                            "VEHICLES"
                    )
            );
        } else if (text.equals("My Bookings")) {

            button.addActionListener(e -> {

                bookingsPanel.refreshBookings();

                cardLayout.show(
                        contentPanel,
                        "BOOKINGS"
                );
            });
        }

        return button;
    }

    // =====================================================
    // VEHICLE CARD CREATION
    // =====================================================

    private JPanel createVehicleCard(
            String title,
            String description,
            String imagePath) {

        return new VehicleCard(
                title,
                description,
                imagePath
        );
    }

    // =====================================================
    // HERO PANEL
    // =====================================================

    private class HeroPanel extends JPanel {

        private BufferedImage heroImage;

        public HeroPanel() {

            // Taller hero
            setPreferredSize(
                    new Dimension(
                            1200,
                            290
                    )
            );

            setBackground(
                    LIGHT_BLUE
            );

            heroImage =
                    loadImage(
                            "images/hero.jpg"
                    );
        }

        @Override
        protected void paintComponent(
                Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D)
                            g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width =
                    getWidth();

            int height =
                    getHeight();

            // =================================================
            // HERO IMAGE
            // =================================================

            if (heroImage != null) {

                double imageRatio =
                        (double)
                        heroImage.getWidth()
                        / heroImage.getHeight();

                double panelRatio =
                        (double)
                        width
                        / height;

                int drawWidth;
                int drawHeight;

                if (imageRatio > panelRatio) {

                    drawHeight =
                            height;

                    drawWidth =
                            (int)
                            (
                                    height
                                    * imageRatio
                            );

                } else {

                    drawWidth =
                            width;

                    drawHeight =
                            (int)
                            (
                                    width
                                    / imageRatio
                            );
                }

                int x =
                        (
                                width
                                - drawWidth
                        ) / 2;

                int y =
                        (
                                height
                                - drawHeight
                        ) / 2;

                g2.drawImage(
                        heroImage,
                        x,
                        y,
                        drawWidth,
                        drawHeight,
                        null
                );

            } else {

                GradientPaint gradient =
                        new GradientPaint(
                                0,
                                0,
                                new Color(
                                        215,
                                        234,
                                        255
                                ),
                                width,
                                height,
                                Color.WHITE
                        );

                g2.setPaint(
                        gradient
                );

                g2.fillRect(
                        0,
                        0,
                        width,
                        height
                );
            }

            // =================================================
            // DARK OVERLAY
            // =================================================

            g2.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            0.28f
                    )
            );

            g2.setColor(
                    Color.BLACK
            );

            g2.fillRect(
                    0,
                    0,
                    width,
                    height
            );

            g2.setComposite(
                    AlphaComposite.SrcOver
            );

            // =================================================
            // HERO TITLE
            // =================================================

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            42
                    )
            );

            g2.setColor(
                    Color.WHITE
            );

            g2.drawString(
                    "Rent",
                    70,
                    100
            );

            g2.setColor(
                    RENTIT_BLUE
            );

            g2.drawString(
                    "Your Way",
                    170,
                    100
            );

            // =================================================
            // HERO SUBTITLE
            // =================================================

            g2.setColor(
                    Color.WHITE
            );

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            19
                    )
            );

            g2.drawString(
                    "Cars • Bikes • Scooters",
                    73,
                    137
            );

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            15
                    )
            );

            g2.drawString(
                    "Choose your ride and start your journey.",
                    73,
                    167
            );

            g2.dispose();
        }
    }

    // =====================================================
    // VEHICLE CARD
    // =====================================================

    private class VehicleCard
            extends JPanel {

        private String title;
        private String description;
        private BufferedImage vehicleImage;

        private boolean hovered =
                false;

        private double mouseX =
                0.5;

        private double mouseY =
                0.5;

        private double animation =
                0.0;

        private Timer animationTimer;

        public VehicleCard(
                String title,
                String description,
                String imagePath) {

            this.title =
                    title;

            this.description =
                    description;

            vehicleImage =
                    loadImage(
                            imagePath
                    );

            setOpaque(false);

            setPreferredSize(
                    new Dimension(
                            245,
                            190
                    )
            );

            // =================================================
            // MOUSE ENTER / EXIT
            // =================================================

            addMouseListener(
                    new MouseAdapter() {

                        @Override
                        public void mouseEntered(
                                MouseEvent e) {

                            hovered =
                                    true;

                            startAnimation();
                        }

                        @Override
                        public void mouseExited(
                                MouseEvent e) {

                            hovered =
                                    false;

                            mouseX =
                                    0.5;

                            mouseY =
                                    0.5;

                            startAnimation();
                        }

                        @Override
                        public void mouseClicked(
                                MouseEvent e) {

                            System.out.println(
                                    title
                                    + " selected"
                            );
                        }
                    }
            );

            // =================================================
            // MOUSE MOVEMENT
            // =================================================

            addMouseMotionListener(
                    new MouseMotionAdapter() {

                        @Override
                        public void mouseMoved(
                                MouseEvent e) {

                            mouseX =
                                    (double)
                                    e.getX()
                                    / getWidth();

                            mouseY =
                                    (double)
                                    e.getY()
                                    / getHeight();

                            repaint();
                        }
                    }
            );
        }

        // =====================================================
        // HOVER ANIMATION
        // =====================================================

        private void startAnimation() {

            if (animationTimer != null
                    && animationTimer.isRunning()) {

                animationTimer.stop();
            }

            animationTimer =
                    new Timer(
                            15,
                            e -> {

                                double target =
                                        hovered
                                                ? 1.0
                                                : 0.0;

                                animation +=
                                        (
                                                target
                                                - animation
                                        )
                                        * 0.18;

                                repaint();

                                if (Math.abs(
                                        target
                                        - animation
                                ) < 0.01) {

                                    animation =
                                            target;

                                    (
                                            (Timer)
                                            e.getSource()
                                    ).stop();
                                }
                            }
                    );

            animationTimer.start();
        }

        // =====================================================
        // DRAW CARD
        // =====================================================

        @Override
        protected void paintComponent(
                Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D)
                            g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY
            );

            int width =
                    getWidth();

            int height =
                    getHeight();

            // =================================================
            // 3D MOVEMENT
            // =================================================

            double tiltX =
                    (
                            mouseX
                            - 0.5
                    )
                    * 0.08
                    * animation;

            double tiltY =
                    (
                            mouseY
                            - 0.5
                    )
                    * 0.08
                    * animation;

            int lift =
                    (int)
                    (
                            9
                            * animation
                    );

            // =================================================
            // SHADOW
            // =================================================

            int shadowOffset =
                    5
                    + (
                            (int)
                            (
                                    9
                                    * animation
                            )
                    );

            g2.setColor(
                    new Color(
                            0,
                            0,
                            0,
                            28
                    )
            );

            RoundRectangle2D shadow =
                    new RoundRectangle2D.Double(
                            7,
                            shadowOffset,
                            width - 14,
                            height
                                    - shadowOffset
                                    - 4,
                            22,
                            22
                    );

            g2.fill(
                    shadow
            );

            // =================================================
            // CARD
            // =================================================

            int cardX =
                    4;

            int cardY =
                    4 - lift;

            int cardWidth =
                    width - 8;

            int cardHeight =
                    height - 12;

            Shape cardShape =
                    new RoundRectangle2D.Double(
                            cardX,
                            cardY,
                            cardWidth,
                            cardHeight,
                            22,
                            22
                    );

            g2.setColor(
                    Color.WHITE
            );

            g2.fill(
                    cardShape
            );

            g2.setColor(
                    BORDER_BLUE
            );

            g2.setStroke(
                    new BasicStroke(
                            hovered
                                    ? 1.8f
                                    : 1f
                    )
            );

            g2.draw(
                    cardShape
            );

            // =================================================
            // VEHICLE IMAGE
            // =================================================

            Shape oldClip =
                    g2.getClip();

            g2.clip(
                    cardShape
            );

            if (vehicleImage != null) {

                /*
                 * Keep the vehicle image inside the
                 * upper part of the card.
                 *
                 * The image is scaled proportionally,
                 * so car/bike/scooter images are not
                 * stretched or distorted.
                 */

                int maxImageWidth =
                        220;

                int maxImageHeight =
                        105;

                double imageRatio =
                        (double)
                        vehicleImage.getWidth()
                        / vehicleImage.getHeight();

                int baseWidth =
                        maxImageWidth;

                int baseHeight =
                        (int)
                        (
                                baseWidth
                                / imageRatio
                        );

                if (baseHeight > maxImageHeight) {

                    baseHeight =
                            maxImageHeight;

                    baseWidth =
                            (int)
                            (
                                    baseHeight
                                    * imageRatio
                            );
                }

                /*
                 * Small enlargement when
                 * the mouse hovers over card.
                 */

                double scale =
                        1.0
                        + (
                                0.08
                                * animation
                        );

                int imageWidth =
                        (int)
                        (
                                baseWidth
                                * scale
                        );

                int imageHeight =
                        (int)
                        (
                                baseHeight
                                * scale
                        );

                /*
                 * Center the vehicle image.
                 */

                int imageX =
                        (
                                width
                                - imageWidth
                        ) / 2;

                /*
                 * Keep image near the
                 * top of the card.
                 */

                int imageY =
                        8 - lift;

                /*
                 * Small movement during hover.
                 */

                imageX +=
                        (int)
                        (
                                tiltX
                                * 25
                        );

                imageY +=
                        (int)
                        (
                                tiltY
                                * 15
                        );

                g2.drawImage(
                        vehicleImage,
                        imageX,
                        imageY,
                        imageWidth,
                        imageHeight,
                        null
                );

            } else {

                drawVehiclePlaceholder(
                        g2,
                        title,
                        width,
                        62 - lift
                );
            }

            g2.setClip(
                    oldClip
            );

            // =================================================
            // TITLE
            // =================================================

            g2.setColor(
                    RENTIT_NAVY
            );

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            19
                    )
            );

            g2.drawString(
                    title,
                    18,
                    height
                            - 55
                            - lift
            );

            // =================================================
            // DESCRIPTION
            // =================================================

            g2.setColor(
                    TEXT_GRAY
            );

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            11
                    )
            );

            g2.drawString(
                    description,
                    18,
                    height
                            - 36
                            - lift
            );

            // =================================================
            // EXPLORE BUTTON
            // =================================================

            int buttonWidth =
                    94;

            int buttonHeight =
                    28;

            int buttonX =
                    width
                    - buttonWidth
                    - 16;

            int buttonY =
                    height
                    - buttonHeight
                    - 12
                    - lift;

            RoundRectangle2D buttonShape =
                    new RoundRectangle2D.Double(
                            buttonX,
                            buttonY,
                            buttonWidth,
                            buttonHeight,
                            14,
                            14
                    );

            g2.setColor(
                    RENTIT_BLUE
            );

            g2.fill(
                    buttonShape
            );

            g2.setColor(
                    Color.WHITE
            );

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            11
                    )
            );

            g2.drawString(
                    "Explore  →",
                    buttonX + 14,
                    buttonY + 19
            );

            g2.dispose();
        }

        // =====================================================
        // FALLBACK VEHICLE GRAPHIC
        // =====================================================

        private void drawVehiclePlaceholder(
                Graphics2D g2,
                String title,
                int width,
                int y) {

            g2.setColor(
                    LIGHT_BLUE
            );

            g2.fillOval(
                    width / 2 - 42,
                    y - 36,
                    84,
                    84
            );

            g2.setColor(
                    RENTIT_BLUE
            );

            g2.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            14
                    )
            );

            String text;

            if (title.equals("Cars")) {

                text = "CAR";

            } else if (
                    title.equals("Bikes")) {

                text = "BIKE";

            } else {

                text = "SCOOTER";
            }

            int textWidth =
                    g2.getFontMetrics()
                            .stringWidth(
                                    text
                            );

            g2.drawString(
                    text,
                    width / 2
                            - textWidth / 2,
                    y + 5
            );
        }
    }

    // =====================================================
    // LOAD IMAGE
    // =====================================================

    private BufferedImage loadImage(
            String path) {

        try {

            File file =
                    new File(path);

            if (file.exists()) {

                return ImageIO.read(
                        file
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Could not load image: "
                    + path
            );
        }

        return null;
    }

    // =====================================================
    // CROP LOGO
    // =====================================================

    private BufferedImage cropLogo(
            BufferedImage original) {

        int width =
                original.getWidth();

        int height =
                original.getHeight();

        int top = 0;

        int bottom =
                height - 1;

        // Find top of white logo area

        for (
                int y = 0;
                y < height;
                y++) {

            long brightness = 0;

            for (
                    int x = 0;
                    x < width;
                    x++) {

                int rgb =
                        original.getRGB(
                                x,
                                y
                        );

                int red =
                        (rgb >> 16)
                                & 0xFF;

                int green =
                        (rgb >> 8)
                                & 0xFF;

                int blue =
                        rgb & 0xFF;

                brightness +=
                        (
                                red
                                + green
                                + blue
                        ) / 3;
            }

            double average =
                    (double)
                    brightness
                    / width;

            if (average > 180) {

                top = y;

                break;
            }
        }

        // Find bottom of white logo area

        for (
                int y = height - 1;
                y >= 0;
                y--) {

            long brightness = 0;

            for (
                    int x = 0;
                    x < width;
                    x++) {

                int rgb =
                        original.getRGB(
                                x,
                                y
                        );

                int red =
                        (rgb >> 16)
                                & 0xFF;

                int green =
                        (rgb >> 8)
                                & 0xFF;

                int blue =
                        rgb & 0xFF;

                brightness +=
                        (
                                red
                                + green
                                + blue
                        ) / 3;
            }

            double average =
                    (double)
                    brightness
                    / width;

            if (average > 180) {

                bottom = y;

                break;
            }
        }

        int cropHeight =
                bottom
                - top
                + 1;

        if (cropHeight <= 0) {

            return original;
        }

        return original.getSubimage(
                0,
                top,
                width,
                cropHeight
        );
    }

    // =====================================================
    // SCALE LOGO
    // =====================================================

    private javax.swing.ImageIcon createScaledIcon(
            BufferedImage image,
            int width,
            int height) {

        double ratio =
                (double)
                image.getWidth()
                / image.getHeight();

        int newWidth =
                width;

        int newHeight =
                (int)
                (
                        width
                        / ratio
                );

        if (newHeight > height) {

            newHeight =
                    height;

            newWidth =
                    (int)
                    (
                            height
                            * ratio
                    );
        }

        java.awt.Image scaled =
                image.getScaledInstance(
                        newWidth,
                        newHeight,
                        java.awt.Image.SCALE_SMOOTH
                );

        return new javax.swing.ImageIcon(
                scaled
        );
    }
    
    
    private RentalManager createRentalManager() {

        RentalManager manager =
                new RentalManager();

        // Cars
        manager.addVehicle(
                new Car(
                        "CAR001",
                        "Maruti Swift",
                        1200,
                        3,
                        "Petrol",
                        "Manual",
                        5,
                        "Hatchback"
                )
        );

        manager.addVehicle(
                new Car(
                        "CAR002",
                        "Honda City",
                        1800,
                        2,
                        "Petrol",
                        "Automatic",
                        5,
                        "Sedan"
                )
        );

        manager.addVehicle(
                new Car(
                        "CAR003",
                        "Hyundai Creta",
                        2500,
                        1,
                        "Diesel",
                        "Automatic",
                        5,
                        "SUV"
                )
        );

        // Bikes
        manager.addVehicle(
                new Bike(
                        "BIKE001",
                        "Yamaha MT-15",
                        800,
                        2,
                        "Petrol",
                        "Manual",
                        2,
                        "Street Bike"
                )
        );

        manager.addVehicle(
                new Bike(
                        "BIKE002",
                        "Royal Enfield Classic 350",
                        1000,
                        2,
                        "Petrol",
                        "Manual",
                        2,
                        "Cruiser"
                )
        );

        manager.addVehicle(
                new Bike(
                        "BIKE003",
                        "KTM Duke 250",
                        1200,
                        1,
                        "Petrol",
                        "Manual",
                        2,
                        "Naked Bike"
                )
        );

        // Scooters
        manager.addVehicle(
                new Scooter(
                        "SCOOTER001",
                        "Honda Activa 6G",
                        500,
                        3,
                        "Petrol",
                        "Automatic",
                        2,
                        "Scooter"
                )
        );

        manager.addVehicle(
                new Scooter(
                        "SCOOTER002",
                        "TVS Jupiter",
                        500,
                        2,
                        "Petrol",
                        "Automatic",
                        2,
                        "Scooter"
                )
        );

        manager.addVehicle(
                new Scooter(
                        "SCOOTER003",
                        "Ola S1",
                        700,
                        2,
                        "Electric",
                        "Automatic",
                        2,
                        "Electric Scooter"
                )
        );

        return manager;
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(
            String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    RentITFrame frame =
                            new RentITFrame();

                    frame.setVisible(
                            true
                    );
                }
        );
    }
}
