package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.MainController;
import controller.PlaylistController;

//DashboardFrame
//FCP - Sonic Music
//Samantha Mac
//June 1, 2024

// This is the main display for users to create and view playlists
public class DashboardFrame extends JFrame {
	// Components
	// JPanels
	JPanel mainPanel;
	public static GeneratorPanel generatorPanel;
	public static LibraryPanel libraryPanel;

	// Constructor
	public DashboardFrame() {
		super("SonicMusic Dashboard"); // Frame name

		setupFrame(); // Style frame

		// Create new controller
		MainController.playlistController = new PlaylistController();

		// Create new panels
		mainPanel = new JPanel();
		generatorPanel = new GeneratorPanel();
		libraryPanel = new LibraryPanel();
		
		// Size panel widths
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // get current screen size
        int screenWidth = (int) screenSize.getWidth();
        // Set widths
        generatorPanel.setMaximumSize(new Dimension((int)(screenWidth / 5.0 * 3.0), Integer.MAX_VALUE));
        generatorPanel.setPreferredSize(new Dimension((int)(screenWidth / 5.0 * 3.0), Integer.MAX_VALUE));
        libraryPanel.setMaximumSize(new Dimension((int)(screenWidth / 5.0 * 2.0), Integer.MAX_VALUE));
        libraryPanel.setPreferredSize(new Dimension((int)(screenWidth / 5.0 * 2.0), Integer.MAX_VALUE));
        
		// Add panels to frame
		add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(generatorPanel);
		mainPanel.add(libraryPanel);
	}

	public void setupFrame() {
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize dimensions
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
