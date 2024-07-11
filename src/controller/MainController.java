package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import model.User;
import view.DashboardFrame;
import view.LoginFrame;

//MainController
//FCP - Sonic Music
//Samantha Mac
//June 1, 2024

// This class controls account creation and login
public class MainController {
	// Fields
	private static ArrayList<User> users; // Stores all existing users
	
	// Information about current user that logged in
	public static String activeName;
	public static String activeUsername;

	// Components
	public static LoginFrame loginFrame;
	public static DashboardFrame dashboardFrame;
	// Sub-controllers
	public static PlaylistController playlistController;

	// Contructor
	public MainController() {
		// Create arraylist of all existing users
		users = new ArrayList<User>();

		// Create new frame for user to login
		loginFrame = new LoginFrame();
	}

	// Utility methods
	
	// Combine all utility methods to form organized and reusable code
	public static boolean verifyUser(String currUsername, String currPassword) {
		// Check if account exists
		if (doesUserExist(currUsername)) {
			// Verify password if username exists
			if (checkPassword(currUsername, currPassword)) {
				return true;
			}
		}
		// If account does not exist
		return false;
	}

	// Verify that user is creating a unique account
	public static boolean doesUserExist(String currUsername) {
		// Iterate through users
		for (User user : users) {
			// Compare user input to saved users
			if (user.getUsername().equals(currUsername))
				return true;
		}
		// Inform user that account does not exist
		return false; // If user does not exist
	}
	
	// Check if inputted password matches stored password
	private static boolean checkPassword(String currUsername, String currPassword) {
		// Iterate through users
		for (User user : users) {
			// Find matching username
			if (user.getUsername().equals(currUsername)) {
				// Compare password
				if (user.getPassword().equals(currPassword)) {
					// Update information about current user
					activeName = user.getName();
					activeUsername = user.getUsername();
					return true; // Verify password matches
				}
			}
		}
		// Inform user of incorrect password
		JOptionPane.showMessageDialog(null, "Incorrect password.", "Error", JOptionPane.ERROR_MESSAGE);
		return false; // If password does not match
	}

	// Match existing user to user database
	public static void initializeUsers() {
		// Clear array of users
		users.clear();
		// If txt file exists/ is accessible
		try {
			// Read txt using Scanner
			Scanner inputFile = new Scanner(new File("data/users.txt"));
			// Delimit to read each line of the txt file
			inputFile.useDelimiter(",");

			// Read new lines in the csv file
			while (inputFile.hasNext()) {
				// Store each piece of information as properties of an User object
				String username = inputFile.next();
				username = username.replace("\n", "");
				String password = inputFile.next();
				String name = inputFile.next();

				// Create new object instance
				User user = new User(username, password, name);
				// Add to array list
				users.add(user);
			}
			inputFile.close(); // Close reader

		} catch (FileNotFoundException e) {
			System.out.println("File error"); // If file not found
		}
	}

	// Verify that enough info is provided to create and account
	// Create txt files to permanently store user data
	public static void createAccount(String newUsername, String newPassword, String newName) {
		// Create a writer that can append text to the txt file
		BufferedWriter writer = null;
		
		// Update who the active user is 
		activeUsername = newUsername;
		activeName = newName;
		
		try {
			// Open the users.txt file
			writer = new BufferedWriter(new FileWriter("data/users.txt", true));

			// Write the username, password, and name to the file
			writer.newLine(); // Move to new line
			writer.write(newUsername + "," + newPassword + "," + newName + ",");

			// Create a new folder for the user
			storeAccountInfo(newUsername);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// https://www.geeksforgeeks.org/io-bufferedwriter-class-methods-java/
		finally { //
			// Properly close file writer
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// Initialize users again
		initializeUsers();
	}

	// Creates a folder to store all of the user's playlists
	private static void storeAccountInfo(String newUsername) {
		// Create a new folder to store all playlist txt files containing songs
		File newMainFolder = new File("data", newUsername);

		// try to make the new folder
		if (!newMainFolder.exists()) {
			boolean isCreated = newMainFolder.mkdir(); // Returns if folder exists
			if (isCreated) {
				System.out.println(newMainFolder.getPath());
			}
		}
		
		// Create an images folder for each user's playlist covers
		File newImagesFolder = new File("images", newUsername);
		
		// try to make the new folder
		if (!newImagesFolder.exists()) {
			boolean isCreated = newImagesFolder.mkdir(); // Returns if folder exists
			if (isCreated) {
				System.out.println(newImagesFolder.getPath());
			}
		}
	}
}
