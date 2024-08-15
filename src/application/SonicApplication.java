package application;

import controller.MainController;

/*
 * Samantha Mac
 * June 14, 2024
 * ICS4U1-01
 * Mr. Fernandes
 * 
 * Sonic Music - Final Summative Project
 * An application that analyzes the dominant colours in a photo and generates a playlist based on the emotions these colours represent.
 * 
 * How to use Sonic Music:
 * 1. Login or create an account (username: 'samvmac', password: '123')
 * 2. Enter a playlist name, length, and upload an image
 * *Upload any image you would like that has small dimensions
 * 3. View and remove songs from your new playlist via the right panel!
 * 4. All playlists will be saved to your account
 * 
 * Features:
 * 1. Generate playlists based on the colours of a photo
 * 1a. Identifies dominant colours in a photo
 * 1b. Finds synonyms/associated adjectives of the identified colour names
 * 2. Save playlists to your account (along with a playlist cover image)
 * 3. Delete playlists from your account
 * 4. Play songs on Spotify
 * 5. Log in to and log out of an existing account
 * 6. Create a new account
 * 
 * Major Skills:
 * 1. Creating endpoints to connect to external APIs (Spotify API, ColourAPI, DataMuse) and importing libraries
 * 2. Algorithm design (k-means clustering for image analysis, playlist generation, account creation)
 * 3. Use of data structures (hashmaps, 2D arrays, array lists)
 * 4. Object-oriented programming principles (class design, encapsulation)
 * 5. Code modularity to make creating the GUI more efficient
 * 6. File I/O operations to read and write in txt files
 * 
 * Areas of Concern:
 * 1. Generating a playlist: It will take a while for the GUI to find and display songs because all songs and images for the songs are retrieved from the internet.
 * 2. Loading a playlist's contents: It will take a while for the GUI to update because all images for the songs are retrieved from the internet.
 * 3. The DataMuse API is a bit limiting and may recommend unfitting adjectives 
 * 
 * Notes:
 * All algorithms/backend can be found in the controllers
 * 1. ImageToRGBController: Using k-means values to identify dominant colours in an image
 * 2. RGBToAdjectiveController: Uses the ColourAPI to convert RGB values into colour names, then uses the DataMuseAPI to find associated adjectives
 * 3. PlaylistController: Connects to Spotify API to retrieve songs; generates the entire playlist; modifying playlists 
 * 4. MainController: Login and account creation; creating new folders/txt files for each user
 *
 */

// This class initializes the entire SonicMusic program
public class SonicApplication {
	// Main method
	public static void main(String[] args) {
		// Initializes main controller
		new MainController();
	}
}
