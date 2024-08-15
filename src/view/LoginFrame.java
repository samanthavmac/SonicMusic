package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
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

//LoginFrame
//FCP - Sonic Music
//Samantha Mac
//June 1, 2024

// This frame allows users to log into an existing account
public class LoginFrame extends JFrame {
	// Fields
	private String currUsername;
	private String currPassword;

	// Fonts
	private PoppinsFont poppins = new PoppinsFont();
	private Color sonicGreen = new Color(26, 179, 60);

	// Components
	private JPanel loginPanel;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JLabel createAccountLabel;

	// Constructor
	public LoginFrame() {
		setupFrame();
		setupComponents();
		MainController.initializeUsers(); // Get all existing users

		loginPanel.revalidate();
		loginPanel.repaint();
	}

	private void setupFrame() {
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize dimensions
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.WHITE);
		setVisible(true);
	}

	private void setupComponents() {
		// Setup panel to hold components
		loginPanel = new JPanel();
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS)); // Stack components vertically
		loginPanel.setBackground(Color.WHITE);
		loginPanel.setBorder(new EmptyBorder(150, 20, 20, 20)); // Add padding at the top
		add(loginPanel);

		// Add Sonic Music logo
		JLabel logoLabel = new JLabel(new ImageIcon("images/small_logo.png"));
		logoLabel.setAlignmentX(CENTER_ALIGNMENT); // Set alignment
		loginPanel.add(logoLabel);

		// Add a header
		JLabel header = new JLabel("Where colour meets music");
		header.setFont(poppins.getPoppinsBold());
		header.setAlignmentX(CENTER_ALIGNMENT); // Set alignment
		loginPanel.add(header);

		loginPanel.add(Box.createVerticalStrut(20)); // Add space between components

		// Add text fields for user input
		// Username label
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setFont(poppins.getPoppinsRegular());
		usernameLabel.setAlignmentX(CENTER_ALIGNMENT); // Set alignment
		loginPanel.add(usernameLabel);

		// Username input
		usernameField = createStyledTextField();
		loginPanel.add(usernameField);

		loginPanel.add(Box.createVerticalStrut(10)); // Add space between components

		// Password label
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setFont(poppins.getPoppinsRegular());
		passwordLabel.setAlignmentX(CENTER_ALIGNMENT); // Set alignment
		loginPanel.add(passwordLabel);

		// Password input
		passwordField = createStyledPasswordField();
		loginPanel.add(passwordField);

		loginPanel.add(Box.createVerticalStrut(20)); // Add space between components

		// Add login button for user to submit login info
		loginButton = new JButton(new ImageIcon("images/LoginButton.png"));
		loginButton.setBorder(new EmptyBorder(0, 0, 0, 0)); // Remove border
		loginButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Change cursor icon
		loginButton.setAlignmentX(CENTER_ALIGNMENT); // Set alignment
		// Verify user information after login attempt
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Retrieve information from textfields
				currUsername = usernameField.getText();
				currPassword = new String(passwordField.getPassword());

				// Ensure all fields are completed
				if (currUsername.isEmpty() || currPassword.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter a valid username and password.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					// Check if account exists
					if (!MainController.doesUserExist(currUsername)) {
						JOptionPane.showMessageDialog(null, "Account does not exist. Please create a new account.",
								"Error", JOptionPane.ERROR_MESSAGE);
					} else {
						// Check if account exists and if password matches
						if (MainController.verifyUser(currUsername, currPassword)) {
							// Let user into application
							MainController.dashboardFrame = new DashboardFrame();
							dispose(); // Close window
						}
					}

					// Clear existing fields
					usernameField.setText("");
					passwordField.setText("");
				}
			}
		});

		loginPanel.add(loginButton); // Add to panel

		loginPanel.add(Box.createVerticalStrut(10)); // Add space between components

		// Add label for user to create a new account
		createAccountLabel = new JLabel("Don't have an existing account? Create a new account here.");
		createAccountLabel.setFont(poppins.getPoppinsRegular());
		createAccountLabel.setForeground(sonicGreen);
		createAccountLabel.setAlignmentX(CENTER_ALIGNMENT); // Set alignment
		createAccountLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		createAccountLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new AccountCreationFrame();
				dispose(); // Close current frame
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});

		loginPanel.add(createAccountLabel); // Add to panel
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

	// This applies the same stylings to a password field
	private JPasswordField createStyledPasswordField() {
		JPasswordField passwordField = new JPasswordField(25); // Set length
		passwordField.setMaximumSize(new Dimension(400, 40));

		// Set green border
		Border lineBorder = new LineBorder(sonicGreen, 2, true);
		passwordField.setBorder(lineBorder);

		return passwordField;
	}
}
