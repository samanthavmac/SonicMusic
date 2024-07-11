package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import controller.MainController;
import model.Playlist;
import model.Song;
import model.PoppinsFont;

// PlaylistLibraryPanel
// FCP - Sonic Music
// Samantha Mac
// June 1, 2024

// This panel displays users' playlists and their contents
public class LibraryPanel extends JPanel {
	// Fonts
	private PoppinsFont poppins = new PoppinsFont();
	private Color sonicGreen = new Color(26, 179, 60);
	private Color lightGreen = new Color(203, 248, 182);

	// Components
	private JPanel songListPanel; // Panel to hold the list of songs
	private JPanel playlistListPanel;
	private JToggleButton editPlaylistButton;
	private JToggleButton editSongButton;
	private JPanel topPanel;
	private JLabel backButton;

	// Flags to track playlist editing
	private boolean isEditingPlaylists = false;
	private boolean isEditingSongs = false;

	// Constructor
	public LibraryPanel() {
		setupPanel(); // Style panel
	}

	private void setupPanel() {
		// Add vertical box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(lightGreen);
		setBorder(new EmptyBorder(20, 40, 20, 40));

		// Set up panels
		topPanel = new JPanel();
		setupTopPanel();
		add(topPanel);

		playlistListPanel = new JPanel(); // Lists all playlists for this user
		playlistListPanel.setLayout(new BoxLayout(playlistListPanel, BoxLayout.Y_AXIS)); // vertical layout
		playlistListPanel.setBackground(lightGreen);
		add(playlistListPanel);

		songListPanel = new JPanel(); // Lists contents of a playlist
		songListPanel.setLayout(new BoxLayout(songListPanel, BoxLayout.Y_AXIS)); // vertical layout
		songListPanel.setBackground(lightGreen);
		add(songListPanel);

		setupPlaylistListPanel();

		// Initially only show the Top and PlaylistList panel
		playlistListPanel.setVisible(true);
		songListPanel.setVisible(false);
	}

	// This sets up the top section with the back button and logout button
	private void setupTopPanel() {
		// Style panel
		topPanel.setBackground(lightGreen);
		topPanel.setLayout(new BorderLayout());
		topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10)); // set max height

		// Add a log out button to switch accounts
		JLabel logoutButton = new JLabel("Logout");
		logoutButton.setFont(poppins.getPoppinsRegular());
		logoutButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		// Add button functionality
		logoutButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Reset login info
				MainController.activeName = "";
				MainController.activeUsername = "";
				// Close application
				MainController.dashboardFrame.dispose();
				// Open login frame
				new LoginFrame();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});

		topPanel.add(logoutButton, BorderLayout.EAST);

		// Add a back button to return to all playlists
		backButton = new JLabel("View all playlists");
		backButton.setFont(poppins.getPoppinsRegular());
		backButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		// Add button functionality
		backButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Display all playlists
				playlistListPanel.setVisible(true);
				songListPanel.setVisible(false);
				setupPlaylistListPanel();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});

		topPanel.add(backButton, BorderLayout.WEST);
		topPanel.add(Box.createVerticalStrut(80)); // Add spacing
	}

	// This styles that panel that displays user's playlists
	private void setupPlaylistListPanel() {
		playlistListPanel.removeAll(); // Remove any old content
		backButton.setVisible(false);// Hide back button

		// Add title
		JLabel header = new JLabel(MainController.activeName + "'s Playlists");
		header.setFont(poppins.getPoppinsBold());
		header.setAlignmentX(LEFT_ALIGNMENT); // Set alignment
		playlistListPanel.add(header);
		playlistListPanel.add(Box.createVerticalStrut(10));

		// Add button to select and delete playlists
		editPlaylistButton = new JToggleButton("Edit Playlists");
		editPlaylistButton.setFont(poppins.getPoppinsRegular());
		editPlaylistButton.setSelected(false);
		editPlaylistButton.setForeground(sonicGreen);
		editPlaylistButton.setBackground(Color.white); // Set background to white
		editPlaylistButton.setOpaque(true);
		editPlaylistButton.setBorderPainted(false); // Remove border
		editPlaylistButton.setFocusPainted(false); // Remove blue border in active state
	    editPlaylistButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		// Add button functionality
		editPlaylistButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				isEditingPlaylists = editPlaylistButton.isSelected(); // Set flag
				// Check if in editing mode
				if (isEditingPlaylists) {
					editPlaylistButton.setText("Select a playlist to delete");
					editPlaylistButton.setForeground(Color.BLACK);
				} 
				else {
					editPlaylistButton.setText("Edit Playlists");
					editPlaylistButton.setForeground(sonicGreen);
					isEditingPlaylists = false;
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});

		playlistListPanel.add(editPlaylistButton);
		playlistListPanel.add(Box.createVerticalStrut(20));

		// Update flags
		isEditingSongs = false;
		isEditingPlaylists = false;
		
		displayPlaylists(); // Display panel of playlists

		// Update display
		playlistListPanel.revalidate();
		playlistListPanel.repaint();
		songListPanel.removeAll(); // Clear contents of singular playlist panel
	}

	// This styles the panel that displays a panel's contents
	public void setupSongListPanel(String playlistName) {
		// Clear existing content
		songListPanel.removeAll();
		backButton.setVisible(true);// Show back button

		// Add image of album cover
		JLabel albumCoverLabel = new JLabel();
		ImageIcon albumCover = new ImageIcon("images/" + MainController.activeUsername + "/" + playlistName + ".png");
		Image albumCoverImage = albumCover.getImage();
		// Scale image
		Image scaledAlbumCoverImage = albumCoverImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		albumCoverLabel.setIcon(new ImageIcon(scaledAlbumCoverImage));
		albumCoverLabel.setMaximumSize(new Dimension(100, 100));
		albumCoverLabel.setPreferredSize(new Dimension(100, 100));
		songListPanel.add(albumCoverLabel);

		songListPanel.add(Box.createVerticalStrut(10));

		// Add title
		JLabel header = new JLabel(playlistName);
		header.setFont(poppins.getPoppinsBold());
		header.setAlignmentX(LEFT_ALIGNMENT); // Set alignment
		songListPanel.add(header);
		songListPanel.add(Box.createVerticalStrut(10));

		// Add button to select and delete playlist's contents
		editSongButton = new JToggleButton("Edit Playlist");		
		editSongButton.setFont(poppins.getPoppinsRegular());
		editSongButton.setForeground(sonicGreen);
		editSongButton.setBackground(Color.white); // Set background to white
		editSongButton.setOpaque(true);
		editSongButton.setBorderPainted(false); // Remove border
		editSongButton.setFocusPainted(false); // Remove blue border in active state
		editSongButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		// Add button functionality
		editSongButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				isEditingSongs = editSongButton.isSelected(); // Set flag
				// Check if in editing mode
				if (isEditingSongs) {
					editSongButton.setText("Select a song to delete");
					editSongButton.setForeground(Color.BLACK);
				}
				else {
					editSongButton.setText("Edit Playlist");
					editSongButton.setForeground(sonicGreen);
					isEditingSongs = false;
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});

		songListPanel.add(editSongButton);
		songListPanel.add(Box.createVerticalStrut(20));
		
		displaySongs(playlistName); // Display playlist contents

		// Update flags
		isEditingSongs = false;
		isEditingPlaylists = false;
		
		// Update display
		songListPanel.revalidate();
		songListPanel.repaint();

		// Toggle visibility
		playlistListPanel.setVisible(false);
		songListPanel.setVisible(true);
	}

	// Display playlists as a list
	private void displayPlaylists() {
		// Create scroll panel of playlists
		JPanel playlistListScrollPanel = new JPanel();
		playlistListScrollPanel.setLayout(new BoxLayout(playlistListScrollPanel, BoxLayout.Y_AXIS)); // Vertical layout
		playlistListScrollPanel.setBackground(lightGreen);

		JScrollPane scrollPane = new JScrollPane(playlistListScrollPanel); // Add scroll panel
		scrollPane.setBorder(null);
		// Add/disable scroll bars
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// Iterate through user's playlists
		for (int i = 0; i < MainController.playlistController.playlists.size(); i++) {
			// Display each playlist
			playlistListScrollPanel.add(displaySingularPlaylist(i));
			playlistListScrollPanel.add(Box.createVerticalStrut(10)); // Add space between playlists
		}

		playlistListPanel.add(scrollPane);
	}

	// Display each playlist as a row
	private JPanel displaySingularPlaylist(int i) {
		// Create a panel row for each playlist
		JPanel playlistPanel = new JPanel();
		playlistPanel.setLayout(new BorderLayout());
		playlistPanel.setBackground(lightGreen);
		// Adjust the empty border to reduce padding at the bottom
		playlistPanel
				.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1, true), new EmptyBorder(10, 10, 5, 10)));
		playlistPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		playlistPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85)); // set max height
		playlistPanel.setMinimumSize(new Dimension(Integer.MAX_VALUE, 85)); // Set maximum height
		playlistPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 85)); // Set maximum height

		// Add image of album cover
		JLabel albumCoverLabel = new JLabel();
		ImageIcon albumCover = new ImageIcon("images/" + MainController.activeUsername + "/"
				+ MainController.playlistController.playlists.get(i).getName() + ".png"); // Set your image path here
		Image albumCoverImage = albumCover.getImage();
		// Scale image
		Image scaledAlbumCoverImage = albumCoverImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		albumCoverLabel.setIcon(new ImageIcon(scaledAlbumCoverImage));
		albumCoverLabel.setPreferredSize(new Dimension(50, 50));
		playlistPanel.add(albumCoverLabel, BorderLayout.WEST);

		// Create a panel for playlist details
		JPanel playlistDetailsPanel = new JPanel();
		playlistDetailsPanel.setLayout(new BoxLayout(playlistDetailsPanel, BoxLayout.Y_AXIS));
		playlistDetailsPanel.setBackground(lightGreen);
		playlistDetailsPanel.setBorder(new EmptyBorder(0, 10, 0, 10));

		// Playlist name
		JLabel playlistName = new JLabel(MainController.playlistController.playlists.get(i).getName());
		playlistName.setFont(poppins.getPoppinsSemiBold());
		playlistDetailsPanel.add(playlistName);

		// Secondary label (e.g., number of songs)
		JLabel secondaryLabel = new JLabel(MainController.playlistController.playlists.get(i).getLength() + " songs");
		secondaryLabel.setFont(poppins.getPoppinsRegular());
		playlistDetailsPanel.add(secondaryLabel);

		playlistPanel.add(playlistDetailsPanel, BorderLayout.CENTER);

		// Add a mouse listener
		// When clicked, playlist contents expand or you can delet a playlist
		playlistPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (isEditingPlaylists) {
					// Confirm delete action
					int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this playlist?",
							"Yes, delete.", JOptionPane.YES_NO_OPTION);
					if (input == JOptionPane.YES_OPTION) {
						// Remove song
						MainController.playlistController.deletePlaylist(i, playlistName.getText());
						setupPlaylistListPanel(); // Refresh the playlist list
					}
				} else {
					// Display playlist contents
					setupSongListPanel(MainController.playlistController.playlists.get(i).getName());
				}
			}
		});

		return playlistPanel;
	}

	// Display all songs in list format
	public void displaySongs(String playlistName) {
		// Create scroll panel of songs
		JPanel songListScrollPanel = new JPanel();
		songListScrollPanel.setLayout(new BoxLayout(songListScrollPanel, BoxLayout.Y_AXIS)); // vertical layout
		songListScrollPanel.setBackground(lightGreen);

		JScrollPane scrollPane = new JScrollPane(songListScrollPanel); // Add scroll panel
		scrollPane.setBorder(null);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// Retrieve songs from desired playlist
		Playlist playlist = MainController.playlistController.retrievePlaylist(playlistName);

		// Add visual display of each song
		for (int i = 0; i < playlist.getLength(); i++) {
			// Read txt file of songs and convert into Song objects
			songListScrollPanel.add(displaySingularSong(playlist, i));
			songListScrollPanel.add(Box.createVerticalStrut(10)); // Add space between songs
		}

		songListPanel.add(scrollPane);

		// Update display
		revalidate();
		repaint();
	}

	// Display each song as a row
	private JPanel displaySingularSong(Playlist playlist, int i) {
		// Create panel to add song info to
		JPanel songPanel = new JPanel();
		songPanel.setLayout(new BorderLayout());
		songPanel.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1, true), new EmptyBorder(10, 10, 10, 10)));
		songPanel.setBackground(lightGreen);
		songPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Change cursor
		songPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120)); // Set maximum height
		songPanel.setMinimumSize(new Dimension(Integer.MAX_VALUE, 120)); // Set maximum height

		// Create a panel to hold the album cover and play button
		JPanel albumAndPlayPanel = new JPanel();
		albumAndPlayPanel.setLayout(new BoxLayout(albumAndPlayPanel, BoxLayout.X_AXIS));
		albumAndPlayPanel.setBackground(lightGreen);
		albumAndPlayPanel.setBorder(new EmptyBorder(0, 0, 0, 10)); // Add spacing between album cover and play button

		// Add play button to the panel
		JButton playButton = new JButton(new ImageIcon("images/PlayButton.png"));
		playButton.setBorderPainted(false);
		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Open Spotify link of the song
				try {
					Desktop.getDesktop().browse(
							new URI("https://open.spotify.com/track/" + (playlist.getSongs().get(i).getTrackID())));
				} catch (IOException | URISyntaxException ex) {
					ex.printStackTrace();
				}
			}
		});
		albumAndPlayPanel.add(playButton);

		// Display album cover
		try { 
			URL url = new URL(playlist.getSongs().get(i).getAlbumCoverURL()); // Retrieve image address
			ImageIcon albumIcon = new ImageIcon(url);
			Image albumImage = albumIcon.getImage();
			Image scaledAlbumImage = albumImage.getScaledInstance(90, 90, Image.SCALE_SMOOTH); // Set size of cover
			
			// Contain image within a label
			JLabel albumCover = new JLabel(new ImageIcon(scaledAlbumImage));
			albumCover.setMaximumSize(new Dimension(90, 90));
			albumAndPlayPanel.add(albumCover);
		} catch (Exception e) {
			e.printStackTrace();
		}

		songPanel.add(albumAndPlayPanel, BorderLayout.WEST);

		// Create a panel for track name, artist, duration, and rating
		JPanel trackDetailsPanel = new JPanel();
		trackDetailsPanel.setLayout(new BoxLayout(trackDetailsPanel, BoxLayout.Y_AXIS));
		trackDetailsPanel.setBackground(lightGreen);
		trackDetailsPanel.setBorder(new EmptyBorder(0, 10, 0, 10));

		// Labels with song info
		JLabel trackNameLabel = new JLabel(trimText(playlist.getSongs().get(i).getName()));
		trackNameLabel.setFont(poppins.getPoppinsRegularBold());

		JLabel artistLabel = new JLabel(playlist.getSongs().get(i).getArtist());
		artistLabel.setFont(poppins.getPoppinsSubtitle());

		JLabel durationLabel = new JLabel(formatDuration(playlist.getSongs().get(i).getDuration()));
		durationLabel.setFont(poppins.getPoppinsSubtitle());
		durationLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		JLabel ratingLabel = new JLabel("Rating: " + playlist.getSongs().get(i).getPopularity() + "%");
		ratingLabel.setFont(poppins.getPoppinsSubtitle());
		ratingLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		// Add components to subpanel
		trackDetailsPanel.add(trackNameLabel);
		trackDetailsPanel.add(artistLabel);
		trackDetailsPanel.add(durationLabel);
		trackDetailsPanel.add(ratingLabel);
		
		// Add a mouse listener
		// When clicked, you can delete a song
		songPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (isEditingSongs) {
					// Confirm delete action
					int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this song?",
							"Yes, delete.", JOptionPane.YES_NO_OPTION);
					
					if (input == JOptionPane.YES_OPTION) {
						// Remove song from playlist permanently
						MainController.playlistController.deleteSong(i, trackNameLabel.getText(), playlist.getName());
						setupSongListPanel(playlist.getName()); // Refresh the song list
					}
				}
			}
		});

		songPanel.add(trackDetailsPanel, BorderLayout.CENTER);

		return songPanel;
	}

	// Trim song name and add elipses
	private String trimText(String text) {
		if (text.length() > 30) {
			// Return modified string
			return text.substring(0, 30) + "...";
		}
		return text;
	}

	// Convert duration from milliseconds
	private static String formatDuration(long ms) {
		// Use Java time library
		Duration duration = Duration.ofMillis(ms);

		// Get minutes and seconds
		long minutes = duration.toMinutes();
		long seconds = duration.minusMinutes(minutes).getSeconds(); // Excess seconds

		return String.format("%02d:%02d", minutes, seconds);
	}
}
