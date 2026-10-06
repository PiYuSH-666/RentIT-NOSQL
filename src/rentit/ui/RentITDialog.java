package rentit.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class RentITDialog {

    // =====================================================
    // RENTIT COLORS
    // =====================================================

    private static final Color RENTIT_BLUE =
            new Color(22, 119, 232);

    private static final Color RENTIT_NAVY =
            new Color(17, 24, 39);

    private static final Color TEXT_GRAY =
            new Color(100, 110, 125);

    private static final Color LIGHT_BLUE =
            new Color(238, 246, 255);

    private static final Color BORDER_BLUE =
            new Color(216, 231, 248);

    // =====================================================
    // MESSAGE DIALOG
    // =====================================================

    public static void showMessage(
            Component parent,
            String title,
            String message,
            String buttonText) {

        JDialog dialog =
                createDialog(parent, title);

        JPanel content =
                createContentPanel();

        JLabel icon =
                createIconLabel("!");

        JLabel messageLabel =
                createMessageLabel(message);

        JPanel messagePanel =
                new JPanel(new BorderLayout(15, 0));

        messagePanel.setOpaque(false);

        messagePanel.add(
                icon,
                BorderLayout.WEST
        );

        messagePanel.add(
                messageLabel,
                BorderLayout.CENTER
        );

        content.add(
                messagePanel,
                BorderLayout.CENTER
        );

        JButton button =
                createPrimaryButton(buttonText);

        button.addActionListener(
                e -> dialog.dispose()
        );

        JPanel buttonPanel =
                createButtonPanel();

        buttonPanel.add(button);

        content.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        dialog.add(content);

        dialog.pack();

        dialog.setSize(
                Math.max(dialog.getWidth(), 430),
                Math.max(dialog.getHeight(), 210)
        );

        dialog.setLocationRelativeTo(parent);

        dialog.setVisible(true);
    }

    // =====================================================
    // CONFIRMATION DIALOG
    // =====================================================

    public static boolean showConfirm(
            Component parent,
            String title,
            String message,
            String confirmText,
            String cancelText) {

        final boolean[] confirmed =
                { false };

        JDialog dialog =
                createDialog(parent, title);

        JPanel content =
                createContentPanel();

        JLabel icon =
                createIconLabel("?");

        JLabel messageLabel =
                createMessageLabel(message);

        JPanel messagePanel =
                new JPanel(new BorderLayout(15, 0));

        messagePanel.setOpaque(false);

        messagePanel.add(
                icon,
                BorderLayout.WEST
        );

        messagePanel.add(
                messageLabel,
                BorderLayout.CENTER
        );

        content.add(
                messagePanel,
                BorderLayout.CENTER
        );

        JButton cancelButton =
                createSecondaryButton(cancelText);

        JButton confirmButton =
                createPrimaryButton(confirmText);

        cancelButton.addActionListener(
                e -> {
                    confirmed[0] = false;
                    dialog.dispose();
                }
        );

        confirmButton.addActionListener(
                e -> {
                    confirmed[0] = true;
                    dialog.dispose();
                }
        );

        JPanel buttonPanel =
                createButtonPanel();

        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);

        content.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        dialog.add(content);

        dialog.pack();

        dialog.setSize(
                Math.max(dialog.getWidth(), 470),
                Math.max(dialog.getHeight(), 230)
        );

        dialog.setLocationRelativeTo(parent);

        dialog.setVisible(true);

        return confirmed[0];
    }

    // =====================================================
    // CREATE DIALOG
    // =====================================================

    private static JDialog createDialog(
            Component parent,
            String title) {

        Window owner =
                SwingUtilities.getWindowAncestor(parent);

        JDialog dialog =
                new JDialog(
                        owner,
                        title,
                        JDialog.ModalityType.APPLICATION_MODAL
                );

        dialog.setUndecorated(false);

        dialog.setResizable(false);

        dialog.setDefaultCloseOperation(
                JDialog.DISPOSE_ON_CLOSE
        );

        return dialog;
    }

    // =====================================================
    // CONTENT PANEL
    // =====================================================

    private static JPanel createContentPanel() {

        JPanel content =
                new JPanel(
                        new BorderLayout(
                                0,
                                20
                        )
                );

        content.setBackground(Color.WHITE);

        content.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_BLUE
                        ),
                        BorderFactory.createEmptyBorder(
                                25,
                                28,
                                22,
                                28
                        )
                )
        );

        return content;
    }

    // =====================================================
    // ICON
    // =====================================================

    private static JLabel createIconLabel(
            String symbol) {

        JLabel icon =
                new JLabel(
                        symbol,
                        SwingConstants.CENTER
                );

        icon.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        icon.setForeground(RENTIT_BLUE);

        icon.setBackground(LIGHT_BLUE);

        icon.setOpaque(true);

        icon.setPreferredSize(
                new Dimension(
                        48,
                        48
                )
        );

        return icon;
    }

    // =====================================================
    // MESSAGE
    // =====================================================

    private static JLabel createMessageLabel(
            String message) {

        JLabel label =
                new JLabel(
                        "<html><div style='width:330px;'>"
                                + message.replace(
                                        "\n",
                                        "<br>"
                                )
                                + "</div></html>"
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        label.setForeground(RENTIT_NAVY);

        return label;
    }

    // =====================================================
    // BUTTON PANEL
    // =====================================================

    private static JPanel createButtonPanel() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        panel.setOpaque(false);

        return panel;
    }

    // =====================================================
    // PRIMARY BUTTON
    // =====================================================

    private static JButton createPrimaryButton(
            String text) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(Color.WHITE);

        button.setBackground(RENTIT_BLUE);

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        22,
                        10,
                        22
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    // =====================================================
    // SECONDARY BUTTON
    // =====================================================

    private static JButton createSecondaryButton(
            String text) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(RENTIT_NAVY);

        button.setBackground(Color.WHITE);

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_BLUE
                        ),
                        BorderFactory.createEmptyBorder(
                                9,
                                21,
                                9,
                                21
                        )
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }
}