package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import controller.ImageToRGBController;
import controller.MainController;
import model.PoppinsFont;

// Generator Panel
// FCP - Sonic Music
// Samantha Mac
// June 1, 2024

// This panel allows users to input information to create a new playlist
public class GeneratorPanel extends JPanel {
	// Fonts
	PoppinsFont poppins = new PoppinsFont();
	Color sonicGreen = new Color(26, 179, 60);

	// Components
	private JPanel mainPanel;
	private JTextField nameField; // User indicates playlist name
	private JTextField lengthField; // User indicates playlist length
	private JButton uploadButton;
	JLabel uploadedImageLabel; // Displays uploaded image
	private JButton generatePlaylistButton; // Starts playlist generation
	private JProgressBar loader;

	// Stores uploaded image
	private BufferedImage uploadedImage;

	// Controller for image analysis
	public static ImageToRGBController imageToRGBController;

	// Constructor
	public GeneratorPanel() {
		setupPanel();
	}

	// Setters and getters
	public BufferedImage getUploadedImage() {
		return uploadedImage;
	}

	public void setUploadedImage(BufferedImage uploadedImage) {
		this.uploadedImage = uploadedImage;
	}

	// Utility methods
	public void setupPanel() {
	    // Add vertical boxlayout
	    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	    setBackground(Color.WHITE);
	    setBorder(new EmptyBorder(50, 50, 20, 20)); // Add padding

	    // Add main panel to hold contents
	    mainPanel = new JPanel();
	    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	    mainPanel.setBackground(Color.WHITE);
	    add(mainPanel);
	    
	    // Header and instructions
	    JLabel header = new JLabel("Generate a Playlist");
	    header.setFont(poppins.getPoppinsBold());
	    header.setAlignmentX(LEFT_ALIGNMENT); // Set alignment
	    mainPanel.add(header);
	    mainPanel.add(Box.createVerticalStrut(10));

	    JLabel instruction = new JLabel(
	            "Create personalized Spotify playlists based on the colours in your favourite photos.");
	    instruction.setFont(poppins.getPoppinsRegular());
	    mainPanel.add(instruction);
	    mainPanel.add(Box.createVerticalStrut(40));

	    // Step 1: Get playlist name
	    JLabel step1Label = new JLabel("Step 1: Name your new playlist");
	    step1Label.setFont(poppins.getPoppinsSemiBold());
	    mainPanel.add(step1Label);
	    mainPanel.add(Box.createVerticalStrut(10));
	    nameField = createStyledTextField();
	    mainPanel.add(nameField);
	    mainPanel.add(Box.createVerticalStrut(20));

	    // Step 2: Get playlist length
	    JLabel step2Label = new JLabel("Step 2: Enter the number of songs you would like on your playlist");
	    step2Label.setFont(poppins.getPoppinsSemiBold());
	    mainPanel.add(step2Label);
	    mainPanel.add(Box.createVerticalStrut(10));
	    lengthField = createStyledTextField();
	    mainPanel.add(lengthField);
	    mainPanel.add(Box.createVerticalStrut(20));

	    // Step 3: Get playlist contents
	    JLabel step3Label = new JLabel("Step 3: Upload an image to analyze for your playlist's content");
	    step3Label.setFont(poppins.getPoppinsSemiBold());
	    mainPanel.add(step3Label);
	    mainPanel.add(Box.createVerticalStrut(10));
	    uploadImage(); // Add button and functionality to upload an image
	    uploadedImageLabel = new JLabel();
	    mainPanel.add(uploadButton);
	    mainPanel.add(uploadedImageLabel);
	    mainPanel.add(Box.createVerticalStrut(20));

	    // Generate playlist with this information
	    generatePlaylist();
	}

	// This styles a text field
	private JTextField createStyledTextField() {
		JTextField textField = new JTextField(25); // Set length
		textField.setMaximumSize(new Dimension(400, 40));
		textField.setPreferredSize(new Dimension(400, 40));
		textField.setFont(poppins.getPoppinsRegular());

		// Set green border and rounded corners
		Border lineBorder = new LineBorder(sonicGreen, 2, true);
		Border emptyBorder = new EmptyBorder(5, 5, 5, 5);
		CompoundBorder border = new CompoundBorder(lineBorder, emptyBorder);
		textField.setBorder(border);

		return textField;
	}

	// Validate image user selects from their file explorer
	public void uploadImage() {
		// Create button
		uploadButton = new JButton(new ImageIcon("images/UploadImageButton.png"));
		uploadButton.setFont(poppins.getPoppinsRegular());
		uploadButton.setBorder(new EmptyBorder(0, 0, 0, 0));
		uploadButton.setBackground(sonicGreen);
		uploadButton.setForeground(Color.WHITE);
		uploadButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Change cursor

		// Add button functionality
		uploadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Get image file from Finder
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // Do not allow directories

				// Open finder
				int result = fileChooser.showOpenDialog(GeneratorPanel.this);
				// Check if the user approved the file selection
				if (result == JFileChooser.APPROVE_OPTION) {
					// Get photo selected by user
					File selectedFile = fileChooser.getSelectedFile();
					// Display image
					displayImage(selectedFile);
				}
			}
		});
	}
	
	// Display image user selects from their file explorer
    private void displayImage(File file) {
        try {
            // Read image file
            uploadedImage = ImageIO.read(file);
            // Check if an image is uploaded
            if (uploadedImage != null) {
            	 ImageIcon imageIcon = new ImageIcon(uploadedImage); // Create image icon
                 // Resize album covers to be the same size
                 Image image = imageIcon.getImage();
                 Image scaledImage = image.getScaledInstance(150, 150, Image.SCALE_SMOOTH); // Resizing the image

                 // Add resized image as a JLabel
                uploadedImageLabel.setIcon(new ImageIcon(scaledImage));

                uploadButton.setIcon(new ImageIcon("images/ReuploadImageButton.png")); // For user friendliness
                mainPanel.add(generatePlaylistButton); // Allow user to generate playlist once image is uploaded
                // Reload GUI
                revalidate();
                repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Please upload a valid image file.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save the uploaded image to the images folder
    private void saveImageToFile(BufferedImage image, String playlistName) {
        try {
            // Generate a custom name for the image
            File imageFile = new File("images/" + MainController.activeUsername, playlistName + ".png");

            // Save the image as a PNG file
            ImageIO.write(image, "png", imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	// Verify all user inputs and begin playlist generation
	public void generatePlaylist() {
		// Create button to start playlist generation
		generatePlaylistButton = new JButton(new ImageIcon("images/GeneratePlaylistButton.png"));
		generatePlaylistButton.setFont(poppins.getPoppinsRegular());
		generatePlaylistButton.setBorder(new EmptyBorder(0, 0, 0, 0));
		generatePlaylistButton.setBackground(sonicGreen);
		generatePlaylistButton.setForeground(Color.WHITE);
		generatePlaylistButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

		// Add button functionality
		generatePlaylistButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Check that user uploaded an image and indicated playlist length
				if (uploadedImage == null || nameField.getText().equals("") 
						|| isUniquePlaylistName(nameField.getText())|| lengthField.getText().equals("") || Integer.parseInt(lengthField.getText()) <= 0) {
					JOptionPane.showMessageDialog(null, "Please submit a unique playlist name and valid playlist length and upload an image.",
							"Error", JOptionPane.ERROR_MESSAGE);
				} else {
					String playlistName = nameField.getText();

					// Only proceed if the user provides a name
					if (playlistName != null && !playlistName.equals("")) {
						startLoadingAnimation(new Runnable() {
		                    @Override
		                    public void run() {
		                    	// Save image to file to display as a playlist cover
		                        saveImageToFile(uploadedImage, playlistName);

		                        // Find songs that match the playlist
		                        MainController.playlistController.generatePlaylist(Integer.parseInt(lengthField.getText()),
		                                playlistName);
		                        
		                        // Automatically display new playlist
		                        DashboardFrame.libraryPanel.setupSongListPanel(playlistName);
		                        
		                        // Clear input fields
		                        nameField.setText("");
		                        lengthField.setText("");
		                        uploadButton.setIcon(new ImageIcon("images/UploadImageButton.png"));
		                        uploadedImageLabel.setIcon(null);
		                    }
		                });
					}
				}				
			}
		});
	}

	// Playlists take some time to process
	// Add loading screen while playlist loads
	private void startLoadingAnimation(final Runnable task) {
		// Display the loading panel
		JPanel loadingPanel = new JPanel();
		loadingPanel.setLayout(new BoxLayout(loadingPanel, BoxLayout.Y_AXIS));
		loadingPanel.setBackground(Color.WHITE);
        add(loadingPanel); // Show loading message

		// Add label for user friendliness
        JLabel messageLabel = new JLabel("Sonic Music is generating your playlist. Hang tight!");
        messageLabel.setAlignmentX(CENTER_ALIGNMENT); // Set alignment
        messageLabel.setFont(poppins.getPoppinsSemiBold());
        messageLabel.setForeground(sonicGreen);
        loadingPanel.add(messageLabel);
        
        // Add progress bar
        loader = new JProgressBar(0, 100);
        int increment = (int) (99 / Integer.parseInt(lengthField.getText())); // Each song takes roughly one second to load
        loader.setStringPainted(true);
        
        // Create a Timer to update the progress bar every second
        Timer timer = new Timer(1000, e -> {
            int currValue = loader.getValue(); // Get progress
            if (currValue < 99) {
                loader.setValue(currValue + increment); // Increment based on number of songs
            } else {
            	loader.setValue(99);
                ((Timer) e.getSource()).stop(); // When timer should stop
            }
        });
        timer.start();
        loadingPanel.add(loader);

        // Add loading graphic
        JLabel loadingGraphic = new JLabel(new ImageIcon("images/LoadingGraphic.jpeg"));
        loadingPanel.add(loadingGraphic); // Show loading message

		mainPanel.setVisible(false); // Hide contents again

		// Start a new thread to execute the task
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				task.run(); // Execute the task
				loadingPanel.setVisible(false); // Close the loading dialog
				mainPanel.setVisible(true); // Show original panel again
			}
		});

		// Start the thread
		thread.start();
	}
	
	// Check if the playlist name is the same as an existing playlist
	private boolean isUniquePlaylistName(String newName) {
		// Iterate through playlists
	    for (int i = 0; i < MainController.playlistController.playlists.size(); i++) {
	    	// Check if another playlist name matches
	        if (newName.equals(MainController.playlistController.playlists.get(i).getName())) {
	            return true;
	        }
	    }
	    return false;
	}
}
