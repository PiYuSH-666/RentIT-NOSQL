package rentit.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import rentit.AuthenticationManager;

public class LoginPanel extends JPanel {

    private final Color RENTIT_BLUE = new Color(22, 119, 232);
    private final Color RENTIT_NAVY = new Color(17, 24, 39);
    private final Color LIGHT_BLUE = new Color(238, 246, 255);
    private final Color BORDER_BLUE = new Color(216, 231, 248);
    private final Color TEXT_GRAY = new Color(100, 110, 125);

    private AuthenticationManager authenticationManager;
    private Runnable onLoginSuccess;
    private Runnable onSignup;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPanel(
            AuthenticationManager authenticationManager,
            Runnable onLoginSuccess,
            Runnable onSignup) {

        this.authenticationManager =
                authenticationManager;

        this.onLoginSuccess =
                onLoginSuccess;

        this.onSignup =
                onSignup;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        createUI();
    }

    private void createUI() {

        JPanel centerPanel =
                new JPanel(new GridBagLayout());

        centerPanel.setBackground(Color.WHITE);

        JPanel card =
                new JPanel(new GridBagLayout());

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_BLUE
                        ),
                        BorderFactory.createEmptyBorder(
                                35,
                                45,
                                35,
                                45
                        )
                )
        );

        card.setPreferredSize(
                new Dimension(420, 470)
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.insets =
                new Insets(7, 0, 7, 0);

        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel logo =
                new JLabel(
                        "RentIT",
                        SwingConstants.CENTER
                );

        logo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        34
                )
        );

        logo.setForeground(RENTIT_BLUE);

        card.add(logo, gbc);

        gbc.gridy++;

        JLabel title =
                new JLabel(
                        "Welcome back",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        title.setForeground(RENTIT_NAVY);

        card.add(title, gbc);

        gbc.gridy++;

        JLabel subtitle =
                new JLabel(
                        "Login to continue to RentIT",
                        SwingConstants.CENTER
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(TEXT_GRAY);

        card.add(subtitle, gbc);

        gbc.gridy++;
        gbc.insets =
                new Insets(25, 0, 5, 0);

        JLabel usernameLabel =
                createLabel("Username");

        card.add(usernameLabel, gbc);

        gbc.gridy++;

        usernameField =
                createTextField();

        card.add(usernameField, gbc);

        gbc.gridy++;
        gbc.insets =
                new Insets(12, 0, 5, 0);

        JLabel passwordLabel =
                createLabel("Password");

        card.add(passwordLabel, gbc);

        gbc.gridy++;

        passwordField =
                new JPasswordField();

        passwordField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        passwordField.setPreferredSize(
                new Dimension(320, 42)
        );

        passwordField.setBorder(
                BorderFactory.createLineBorder(
                        BORDER_BLUE
                )
        );

        card.add(passwordField, gbc);

        gbc.gridy++;
        gbc.insets =
                new Insets(22, 0, 8, 0);

        JButton loginButton =
                new JButton("Login");

        stylePrimaryButton(loginButton);

        loginButton.addActionListener(
                e -> handleLogin()
        );

        card.add(loginButton, gbc);

        gbc.gridy++;

        JButton signupButton =
                new JButton("Create a new account");

        styleSecondaryButton(signupButton);

        signupButton.addActionListener(
                e -> showSignup()
        );

        card.add(signupButton, gbc);

        centerPanel.add(card);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JLabel createLabel(String text) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        label.setForeground(RENTIT_NAVY);

        return label;
    }

    private JTextField createTextField() {

        JTextField field =
                new JTextField();

        field.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        field.setPreferredSize(
                new Dimension(320, 42)
        );

        field.setBorder(
                BorderFactory.createLineBorder(
                        BORDER_BLUE
                )
        );

        return field;
    }

    private void stylePrimaryButton(
            JButton button) {

        button.setBackground(RENTIT_BLUE);
        button.setForeground(Color.WHITE);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        button.setPreferredSize(
                new Dimension(320, 44)
        );

        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    private void styleSecondaryButton(
            JButton button) {

        button.setBackground(LIGHT_BLUE);
        button.setForeground(RENTIT_BLUE);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        button.setPreferredSize(
                new Dimension(320, 42)
        );

        button.setFocusPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    private void handleLogin() {

        String username =
                usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        if (username.isEmpty() ||
                password.isEmpty()) {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password.",
                    "Login",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        boolean success =
                authenticationManager.login(
                        username,
                        password
                );

        if (success) {

            onLoginSuccess.run();

        } else {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password.",
                    "Login Failed",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void showSignup() {

        onSignup.run();
    }
}