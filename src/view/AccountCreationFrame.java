package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import controller.MainController;
import model.PoppinsFont;

// AccountCreationFrame
// FCP - Sonic Music
// Samantha Mac
// June 1, 2024

// This frame allows users to create an account
public class AccountCreationFrame extends JFrame {
    // Fields
    private String newUsername;
    private String newPassword;
    private String newName;

    // Fonts
    PoppinsFont poppins = new PoppinsFont();
    Color sonicGreen = new Color(26, 179, 60);

    // Components
    JPanel accountPanel;
    JTextField nameField;
    JTextField usernameField;
    JPasswordField passwordField;
    JLabel loginLabel;
    JButton createAccountButton;

    // Constructor
    public AccountCreationFrame() {
        setupFrame();
        setupComponents();
        
        accountPanel.revalidate();
        accountPanel.repaint();
    }

    private void setupFrame() {
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize dimensions
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.WHITE);
        setVisible(true);
    }

    private void setupComponents() {
        // Setup panel to hold components
        accountPanel = new JPanel();
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS)); // Stack components vertically
        accountPanel.setBackground(Color.WHITE);
        accountPanel.setBorder(new EmptyBorder(120, 20, 20, 20)); // Add padding at the top

        // Add Sonic Music logo
        JLabel logoLabel = new JLabel(new ImageIcon("images/small_logo.png"));
        logoLabel.setAlignmentX(CENTER_ALIGNMENT); // Set alignment
        accountPanel.add(logoLabel);

        // Add a header
        JLabel header = new JLabel("Join SonicMusic today");
        header.setFont(poppins.getPoppinsBold());
        header.setAlignmentX(CENTER_ALIGNMENT); // Set alignment
        accountPanel.add(header);

        accountPanel.add(Box.createVerticalStrut(20)); // Add space between components
        
        // Add text fields for user input
        // Name label
        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setFont(poppins.getPoppinsRegular());
        nameLabel.setAlignmentX(CENTER_ALIGNMENT); // Set alignment
        accountPanel.add(nameLabel);

        // Name input
        nameField = createStyledTextField();
        accountPanel.add(nameField);

        accountPanel.add(Box.createVerticalStrut(10)); // Add space between components

        // Username label
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(poppins.getPoppinsRegular());
        usernameLabel.setAlignmentX(CENTER_ALIGNMENT); // Set alignment
        accountPanel.add(usernameLabel);

        // Username input
        usernameField = createStyledTextField();
        accountPanel.add(usernameField);

        accountPanel.add(Box.createVerticalStrut(10)); // Add space between components

        // Password label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(poppins.getPoppinsRegular());
        passwordLabel.setAlignmentX(CENTER_ALIGNMENT); // Set alignment
        accountPanel.add(passwordLabel);

        // Password input
        passwordField = createStyledPasswordField();
        accountPanel.add(passwordField);

        accountPanel.add(Box.createVerticalStrut(20)); // Add space between components

        // Add button for user to create a new account
        createAccountButton = new JButton(new ImageIcon("images/CreateAccountButton.png"));
        createAccountButton.setAlignmentX(CENTER_ALIGNMENT); // Set alignment
        createAccountButton.setBorder(new EmptyBorder(0,0,0,0)); // Remove border
        createAccountButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Change cursor icon
        // Create account button action listener
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve information from textfields
                newName = nameField.getText();
                newUsername = usernameField.getText();
                newPassword = new String(passwordField.getPassword());

                // Ensure all fields are completed
                if (newName.isEmpty() || newUsername.isEmpty() || newPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid name, username, and password.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Check if account exists
                    if (!MainController.doesUserExist(newUsername)) {
                        // Allow user to create new account
                        MainController.createAccount(newUsername, newPassword, newName);
                        // Let user into application
                        MainController.dashboardFrame = new DashboardFrame();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Account already exists. Please login to existing account.", "Error", JOptionPane.ERROR_MESSAGE);
                        // Clear existing fields
                        nameField.setText("");
                        usernameField.setText("");
                        passwordField.setText("");
                    }
                }
            }
        });

        accountPanel.add(createAccountButton); // Add to panel

        accountPanel.add(Box.createVerticalStrut(10)); // Add space between components

        
        // Add label for user to create a new account
        loginLabel = new JLabel("Already have an account? Login to your account here.");
        loginLabel.setFont(poppins.getPoppinsRegular());
        loginLabel.setForeground(sonicGreen);
        loginLabel.setAlignmentX(CENTER_ALIGNMENT); // Set alignment
        loginLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	// Return user to login page
                new LoginFrame();
                dispose(); // Close current frame
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        accountPanel.add(loginLabel); // Add to panel
        
        add(accountPanel); // Add main panel to frame
    }

    // This styles a text field
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(25); // Set length
        textField.setMaximumSize(new Dimension(400, 40));
        textField.setFont(poppins.getPoppinsRegular());

        // Set green border and rounded corners
        Border lineBorder = new LineBorder(sonicGreen, 2, true);
        textField.setBorder(lineBorder);

        return textField;
    }
    
    // This applies the same styling for a password field
    private JPasswordField createStyledPasswordField() {
    	// Hides password input
        JPasswordField passwordField = new JPasswordField(25); // Set length
        passwordField.setMaximumSize(new Dimension(400, 40));

        // Set green border and rounded corners
        Border lineBorder = new LineBorder(sonicGreen, 2, true);
        passwordField.setBorder(lineBorder);

        return passwordField;
    }
}
