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

public class SignupPanel extends JPanel {

    private final Color RENTIT_BLUE = new Color(22, 119, 232);
    private final Color RENTIT_NAVY = new Color(17, 24, 39);
    private final Color LIGHT_BLUE = new Color(238, 246, 255);
    private final Color BORDER_BLUE = new Color(216, 231, 248);
    private final Color TEXT_GRAY = new Color(100, 110, 125);

    private AuthenticationManager authenticationManager;
    private Runnable onLogin;
    private Runnable onSignupSuccess;
    private JTextField nameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public SignupPanel(
            AuthenticationManager authenticationManager,
            Runnable onLogin,
            Runnable onSignupSuccess) {

        this.authenticationManager =
                authenticationManager;

        this.onLogin =
                onLogin;

        this.onSignupSuccess =
                onSignupSuccess;

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
                                25,
                                45,
                                25,
                                45
                        )
                )
        );

        card.setPreferredSize(
                new Dimension(420, 540)
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.insets =
                new Insets(5, 0, 5, 0);

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
                        32
                )
        );

        logo.setForeground(RENTIT_BLUE);

        card.add(logo, gbc);

        gbc.gridy++;

        JLabel title =
                new JLabel(
                        "Create your account",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        23
                )
        );

        title.setForeground(RENTIT_NAVY);

        card.add(title, gbc);

        gbc.gridy++;

        JLabel subtitle =
                new JLabel(
                        "Join RentIT and start your journey",
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
                new Insets(16, 0, 4, 0);

        card.add(
                createLabel("Full name"),
                gbc
        );

        gbc.gridy++;

        nameField =
                createTextField();

        card.add(nameField, gbc);

        gbc.gridy++;
        gbc.insets =
                new Insets(8, 0, 4, 0);

        card.add(
                createLabel("Username"),
                gbc
        );

        gbc.gridy++;

        usernameField =
                createTextField();

        card.add(usernameField, gbc);

        gbc.gridy++;
        gbc.insets =
                new Insets(8, 0, 4, 0);

        card.add(
                createLabel("Password"),
                gbc
        );

        gbc.gridy++;

        passwordField =
                createPasswordField();

        card.add(passwordField, gbc);

        gbc.gridy++;
        gbc.insets =
                new Insets(8, 0, 4, 0);

        card.add(
                createLabel("Confirm password"),
                gbc
        );

        gbc.gridy++;

        confirmPasswordField =
                createPasswordField();

        card.add(
                confirmPasswordField,
                gbc
        );

        gbc.gridy++;
        gbc.insets =
                new Insets(15, 0, 6, 0);

        JButton signupButton =
                new JButton("Create Account");

        stylePrimaryButton(signupButton);

        signupButton.addActionListener(
                e -> handleSignup()
        );

        card.add(
                signupButton,
                gbc
        );

        gbc.gridy++;

        JButton loginButton =
                new JButton("Already have an account? Login");

        styleSecondaryButton(loginButton);

        loginButton.addActionListener(
                e -> showLogin()
        );

        card.add(
                loginButton,
                gbc
        );

        centerPanel.add(card);

        add(
                centerPanel,
                BorderLayout.CENTER
        );
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
                new Dimension(320, 38)
        );

        field.setBorder(
                BorderFactory.createLineBorder(
                        BORDER_BLUE
                )
        );

        return field;
    }

    private JPasswordField createPasswordField() {

        JPasswordField field =
                new JPasswordField();

        field.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        field.setPreferredSize(
                new Dimension(320, 38)
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
                new Dimension(320, 42)
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
                        13
                )
        );

        button.setPreferredSize(
                new Dimension(320, 40)
        );

        button.setFocusPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    private void handleSignup() {

        String name =
                nameField.getText().trim();

        String username =
                usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        String confirmPassword =
                new String(
                        confirmPasswordField.getPassword()
                );

        if (name.isEmpty() ||
                username.isEmpty() ||
                password.isEmpty() ||
                confirmPassword.isEmpty()) {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields.",
                    "Signup",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!password.equals(confirmPassword)) {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Passwords do not match.",
                    "Signup",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        boolean success =
                authenticationManager.signUp(
                        name,
                        username,
                        password
                );

        if (success) {

            onSignupSuccess.run();

        } else {

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Username already exists or the details are invalid.",
                    "Signup Failed",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void showLogin() {

        onLogin.run();
    }
}