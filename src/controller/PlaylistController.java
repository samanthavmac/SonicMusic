package controller;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

import model.Playlist;
import model.RGBColour;
import model.Song;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

//Playlist Controller
//FCP - Sonic Music
//Samantha Mac
//June 8, 2024

// This class can create and open playlists
public class PlaylistController {
    // Fields
    public static ArrayList<Playlist> playlists; // Retrieve all user playlists
    public static ArrayList<Song> activeSongs; // Songs in the playlist that is currently open
    public static ArrayList<String> allAdjectives;

    // Counters
    int currSong = 1;

    // Sub-controllers
    ImageToRGBController imageToRGBController; // Detects colours in image

    // Spotify Fields
    private final SpotifyApi spotifyApi;
    // Spotify authenticator fields
    private static final String CLIENT_ID = ""; // From my Spotify Developer API
    private static final String CLIENT_SECRET = "";
    private static final String REDIRECT_URI = "http://localhost:8888/callback";

    // Default Constructor
    // Source: https://github.com/spotify-web-api-java/spotify-web-api-java/blob/master/examples/data/search/simplified/SearchTracksExample.java
    public PlaylistController() {
        // Create a new SpotifyApi instance
        this.spotifyApi = new SpotifyApi.Builder().setClientId(CLIENT_ID) // From Spotify Developer API
                .setClientSecret(CLIENT_SECRET) // From Spotify Developer API
                .setRedirectUri(SpotifyHttpManager.makeUri(REDIRECT_URI)) // For authentication
                .build(); // Build the SpotifyApi instance

        try {
            // Get an access token
            String accessToken = spotifyApi.clientCredentials().build().execute().getAccessToken();
            // For future requests
            spotifyApi.setAccessToken(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Once user logs in, retrieve their existing playlists
        getExistingPlaylists();
    }

    // Utility methods
    
    // Retrieve names of all existing playlists for this user
    private boolean getExistingPlaylists() {
        playlists = new ArrayList<Playlist>(); // Initialize playlist array

        // Get user's unique file path
        File userFolder = new File("data", MainController.activeUsername);

        // Check if user has existing playlists
        if (userFolder.isDirectory()) {
            // List the contents of the folder
            String[] existingPlaylists = userFolder.list();

            // User has existing playlists
            if (existingPlaylists != null && existingPlaylists.length > 0) {
                // Iterate through all possible playlists
                for (int i = 0; i < existingPlaylists.length; i++) {
                    // Read the content of each playlist file
                    File playlistFile = new File(userFolder, existingPlaylists[i]);
                    // Generate playlist and add it to the collection of playlists
                    playlists.add(retrievePlaylist(existingPlaylists[i]));
                }
                
                return true; // Has existing playlists
            }
        }
        
        return false; // No existing playlists
    }

    // Retrieve playlist contents and name
    public static Playlist retrievePlaylist(String playlistFile) {
        // Create array list of songs within this particular playlist
        ArrayList<Song> songs = new ArrayList<Song>();

        // If txt file exists/ is accessible
        try {
            // Read txt using Scanner
            Scanner inputFile = new Scanner(new File("data/" + MainController.activeUsername + "/" + playlistFile));
            // Delimit to read each line of the txt file
            inputFile.useDelimiter(",");

            while (inputFile.hasNext()) {
                // Store each piece of information as properties of a Song object
                String name = inputFile.next();
                name = name.replace("\n", "");
                String artist = inputFile.next();
                int duration = inputFile.nextInt();
                int popularity = inputFile.nextInt();
                String trackId = inputFile.next();
                String albumCoverURL = inputFile.next();

                // Create new object instance
                Song song = new Song(name, artist, duration, popularity, trackId, albumCoverURL);
                // Add to array list
                songs.add(song);
            }
            
            inputFile.close(); // Close reader
          
            // Create a new playlist object
            Playlist playlist = new Playlist(playlistFile, songs, songs.size());

            return playlist;

        } catch (FileNotFoundException e) {
            System.out.println("File error"); // If file not found
        }

        return null;
    }

    // Produces a new playlist based on the uploaded image
    public void generatePlaylist(int playlistLength, String playlistName) {
        // Retrieve colours from the photo
        // Will automatically retrieve adjectives too
        imageToRGBController = new ImageToRGBController();
        
        // Playlist generation begins
        // Determine total number of adjectives across all colours
        int adjectiveCount = 0;

        // Store all adjectives
        allAdjectives = new ArrayList<String>();
        
        // Iterate through all entries in the map
        for (Map.Entry<RGBColour, ArrayList<String>> entry : imageToRGBController.adjectiveController.dominantColourAdjectives.entrySet()) {
            // Add all adjectives to array
            allAdjectives.addAll(entry.getValue());
            // Update adjectiveCount amount
            adjectiveCount += entry.getValue().size();
        }
        
        // Determine the number of queries each ajective gets
        int[] songsPerQuery = new int[adjectiveCount];
        
        // Initialize array
        Arrays.fill(songsPerQuery, 0);
        
        int currAdjective = 0; // Track current adjective
        // Divide number of queries evenly amongst adjectives
        for (int i = 0; i < playlistLength; i++) {
            // Increment the query count for the current adjective
            songsPerQuery[currAdjective]++;
            // Move onto next adjective, loop if last adjective is reached
            currAdjective = (currAdjective + 1) % adjectiveCount;
        }
    
        // Create a temporary list to store songs
        ArrayList<Song> tempSongs = new ArrayList<>();

        // Query Spotify API to find songs for each adjective
        for (int i = 0; i < adjectiveCount; i++) {
            findSongs(tempSongs, allAdjectives.get(i), songsPerQuery[i]);
        }

        // Append all found songs to the playlist file
        appendSongsToFile(playlistLength, playlistName, tempSongs);
        
        // Add new playlist to array list
        playlists.add(retrievePlaylist(playlistName));
    }

    // Source: https://developer.spotify.com/documentation/web-api
    // Searches for songs on Spotify that match adjectives
    public void findSongs(ArrayList<Song> tempSongs, String query, int numQueries) {
        // Use search track feature from Spotify's API
        SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(query).limit(numQueries).build();

        try {
            // Store all tracks in an array
            Track[] tracks = searchTracksRequest.execute().getItems();

            // Iterate through track list
            for (Track track : tracks) {
                String trackName = track.getName().replaceAll(",", ""); // Remove commas
                String artistName = track.getArtists()[0].getName().replaceAll(",", ""); // Remove commas
                int durationMs = track.getDurationMs();
                int popularity = track.getPopularity();
                String trackId = track.getId();
                String albumCoverUrl = track.getAlbum().getImages()[0].getUrl();

                // Check if the song is already in the temporary list
                boolean isDuplicate = false;
                for (Song song : tempSongs) {
                	// Check if song is a duplicate based on name and artists
                    if (song.getName().equals(trackName) && song.getArtist().equals(artistName)) {
                        isDuplicate = true; // Set flag
                        break;
                    }
                }

                // If not a duplicate, add the song to the temporary list
                if (!isDuplicate) {
                    tempSongs.add(new Song(trackName, artistName, durationMs, popularity, trackId, albumCoverUrl));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Each playlist has a txt file to store its contents
    public void createPlaylistFile(String playlistName) {
        // Create a txt file that will store all of the track IDs
        File file = new File("data/" + MainController.activeUsername + "/" + playlistName);

        try {
            // Create the file if it doesn't exist
            if (file.createNewFile()) {
                System.out.println(file);
            } else {
                System.out.println("Already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Append a list of songs to a txt file
    private void appendSongsToFile(int playlistLength, String playlistName, ArrayList<Song> songs) {
        // Create a writer that can append text to the txt file
        BufferedWriter writer = null;
        try {
            // Open this playlist's particular txt file
            writer = new BufferedWriter(new FileWriter("data/" + MainController.activeUsername + "/" + playlistName, true));

            for (int i = 0; i < songs.size(); i++) {
                Song song = songs.get(i);
                // Write the song details to the file
                writer.write(song.getName() + "," + song.getArtist() + "," + song.getDuration() + "," + song.getPopularity() + "," + song.getTrackID() + "," + song.getAlbumCoverURL() + ",");

                if (i != songs.size() - 1) { // No new line for last song
                    writer.newLine(); // Move to new line
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally { 
            // Properly close file writer
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // This will permanently delete a playlist from the users account
    public void deletePlaylist(int i, String playlistName) {
        // Get path to playlist file
        File playlistFile = new File("data", MainController.activeUsername + "/" + playlistName);

        // Check if file exists and delete it
        if (playlistFile.exists() && playlistFile.delete()) {
        	playlists.remove(i); // Remove playlist from array list
        } else {
            System.out.println("Did not delete");
        }
    }

    public static void deleteSong(int i, String songName, String playlistName) {
        // Get path to playlist file
        File playlistFile = new File("data/" + MainController.activeUsername + "/" + playlistName);

        // Create a temporary list to hold lines of the playlist
        ArrayList<String> temp = new ArrayList<String>();

        // Create a reader to read playlist contents
        try (BufferedReader reader = new BufferedReader(new FileReader(playlistFile))) {

            String songLine; // Get current line from txt file

            // Read each line from the original file
            while ((songLine = reader.readLine()) != null) {
                // Check if the line contains the song name to delete
                if (!songLine.contains(songName)) {
                	temp.add(songLine); // Add line to the temporary list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Rewrite the playlist file with updated contents
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(playlistFile))) {
            // Write each line from the temporary list back to the playlist file
            for (int j = 0; j < temp.size(); j++) {
                writer.write(temp.get(j));
                if (j < temp.size() - 1) { // Add newline for all but the last line
                    writer.newLine();
                }
            }
        } catch (IOException e) {
           e.printStackTrace();
        }        
        
        // Update playlist length
        // Iterate through playlists
        for (Playlist playlist : playlists) {
        	// If playlist name matches
        	if (playlist.getName().equals(playlistName))
                playlist.setLength(playlist.getLength() - 1); // Decrement playlist length
        }
    }
}
